package su.nsk.iae.post.generator.promela.statements

import java.util.AbstractMap
import java.util.ArrayList
import java.util.List
import java.util.Map
import su.nsk.iae.post.generator.promela.context.NamespaceContext
import su.nsk.iae.post.generator.promela.expressions.PromelaExpression
import su.nsk.iae.post.generator.promela.expressions.PromelaExpressionsHelper
import su.nsk.iae.post.generator.promela.model.IPromelaElement
import su.nsk.iae.post.generator.promela.model.PromelaElementList
import su.nsk.iae.post.poST.AssignmentStatement
import su.nsk.iae.post.poST.IfStatement
import su.nsk.iae.post.poST.CaseStatement
import su.nsk.iae.post.poST.SignedInteger
import su.nsk.iae.post.poST.StartProcessStatement
import su.nsk.iae.post.generator.promela.context.PromelaContext
import su.nsk.iae.post.generator.promela.model.WrongModelStateException
import su.nsk.iae.post.poST.StopProcessStatement
import su.nsk.iae.post.poST.ErrorProcessStatement
import su.nsk.iae.post.poST.SetStateStatement
import su.nsk.iae.post.generator.promela.context.CurrentContext
import su.nsk.iae.post.generator.promela.model.PromelaState
import su.nsk.iae.post.generator.promela.model.PromelaProcess
import su.nsk.iae.post.generator.promela.context.PostConstructContext
import su.nsk.iae.post.generator.promela.context.PostConstructContext.IPostConstuctible
import su.nsk.iae.post.poST.TimeoutStatement
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar

abstract class PromelaStatement implements IPromelaElement, IPostConstuctible {
	
	new () {
		PostConstructContext.register(this);
	}
	
	override void postConstruct() {}
	
	
	static class Assign extends PromelaStatement {
		final String fullVarName;
		final PromelaExpression value;
		
		new (AssignmentStatement s) {
			this.fullVarName = NamespaceContext.getFullId(s.variable.name);
			this.value = PromelaExpressionsHelper.getExpr(s.value);
		}
		
		override toText() {
			'''
				«fullVarName» = «value.toText»;
			'''
		}
	}
	
	static class If extends PromelaStatement {
		PromelaExpression mainCond;
		PromelaElementList<? extends PromelaStatement> mainCase;
		List<Map.Entry<PromelaExpression, PromelaElementList<? extends PromelaStatement>>> elseIfCases = new ArrayList();
		PromelaElementList<? extends PromelaStatement> elseCase;
		
		new (IfStatement s) {
			mainCond = PromelaExpressionsHelper.getExpr(s.mainCond);
			mainCase = new PromelaElementList().addElements(PromelaStatementsHelper.getStatementList(s.mainStatement));
			s.elseIfCond.forEach[cond, i | elseIfCases.add(new AbstractMap.SimpleEntry(
				PromelaExpressionsHelper.getExpr(cond),
				new PromelaElementList().addElements(PromelaStatementsHelper.getStatementList(s.elseIfStatements.get(i)))
			))];
			elseCase = s.elseStatement !== null ? new PromelaElementList().addElements(PromelaStatementsHelper.getStatementList(s.elseStatement)) : null;
		}
		
		override toText() {
			elseIfCases.empty && elseCase === null ?
			'''
				if :: «mainCond.toText» -> {
					«mainCase.toText»
				} :: else -> skip; fi;
			''' :
			'''
				if
					:: «mainCond.toText» -> {
						«mainCase.toText»
					}
«««					
					«FOR c : elseIfCases»
					:: else -> if :: «c.key.toText» -> {
						«c.value.toText»
					}
					«ENDFOR»
«««					
					«IF elseCase !== null»
					:: else -> {
						«elseCase.toText»
					}
					«ELSE»
					:: else -> skip;
					«ENDIF»
«««					
				«FOR c : elseIfCases»fi; «ENDFOR»fi;
			'''
		}
	}
	
	static class Case extends PromelaStatement {
		static int caseStatements = 0;
		
		String caseConditionValueVarName = "tmp__caseCondVal" + caseStatements++;
		PromelaExpression cond;
		List<Map.Entry<List<SignedInteger>, PromelaElementList<? extends PromelaStatement>>> caseElements = new ArrayList();
		PromelaElementList<? extends PromelaStatement> elseCase;
		
		new (CaseStatement s) {
			cond = PromelaExpressionsHelper.getExpr(s.cond);
			s.caseElements.forEach[e | caseElements.add(new AbstractMap.SimpleEntry(
				e.caseList.caseListElement,
				new PromelaElementList().addElements(PromelaStatementsHelper.getStatementList(e.statement))
			))];
			elseCase =  s.elseStatement !== null ? new PromelaElementList().addElements(PromelaStatementsHelper.getStatementList(s.elseStatement)) : null;
		}
		
		override toText() {
			'''
				int «caseConditionValueVarName» = «cond.toText»;
				if
«««				
					«FOR e : caseElements»
					:: «FOR c : e.key SEPARATOR ' || '»(«c.value» == «caseConditionValueVarName»)«ENDFOR» -> {
						«e.value.toText»
					}
					«ENDFOR»
«««					
					«IF elseCase !== null»
					:: else -> {
						«elseCase.toText»
					}
					«ELSE»
					:: else -> skip;
					«ENDIF»
«««					
				fi;
			'''
		}
	}
	
	static class StartProcess extends PromelaStatement {
		String curProgramName;
		String processShortName;
		
		String processStateMTypeVar;
		String processFirstStateMTypeName;
		String timeoutVarName;
		
		new (StartProcessStatement s) {
			this.curProgramName = CurrentContext.curProgram.shortName;
			this.processShortName = s.process.name;
		}
		
		override void postConstruct() {
			val process = PromelaContext.getContext().allProcesses
				.findFirst[p | p.programName.equals(curProgramName) && p.shortName.equals(processShortName)];
			if (process === null) {
				throw new WrongModelStateException();
			}
			this.processStateMTypeVar = process.stateVarMType;
			val state = process.states.get(0);
			this.processFirstStateMTypeName = state.stateMType;
			this.timeoutVarName = state.timeout !== null ? process.timeoutVar.name : null;
		}
		
		override toText() {
			'''
				«IF timeoutVarName !== null»
					«timeoutVarName» = 1;
				«ENDIF»
				«processStateMTypeVar» = «processFirstStateMTypeName»;
			'''
		}
	}
	
	static class SetState extends PromelaStatement {
		String nextStateName;
		PromelaProcess curProcess;
		PromelaState curState;
		
		String processStateMTypeVar;
		String stateMTypeName;
		String timeoutVarName;
		
		new (SetStateStatement s) {
			this.nextStateName = s.next ? null : s.state.name;
			this.curProcess = CurrentContext.curProcess;
			this.curState = CurrentContext.curState;
			this.processStateMTypeVar = curProcess.stateVarMType;
		}
		
		override void postConstruct() {
			var PromelaState nextState;
			if (nextStateName !== null) {
				nextState = curProcess.states.findFirst[state | state.shortName.equals(nextStateName)];
			}
			else {
				val stateIndex = curProcess.states.indexOf(curState);
				nextState = curProcess.states.get(stateIndex + 1);//"SET NEXT;" is forbidden in the last state in poST
			}
			this.stateMTypeName = nextState.stateMType;
			this.timeoutVarName = nextState.timeout !== null ? curProcess.timeoutVar.name : null;
		}
		
		override toText() {
			'''
				«IF timeoutVarName !== null»
					«timeoutVarName» = 1;
				«ENDIF»
				«processStateMTypeVar» = «stateMTypeName»;
			'''
		}
	}
	
	static class StopProcess extends PromelaStatement {
		String processShortName;
		String curProgramName;
		
		String processStateMTypeVar;
		String processStopStateFullName;
		
		new (StopProcessStatement s) {
			this.processShortName = s.process !== null ? s.process.name : CurrentContext.curProcess.shortName;
			this.curProgramName = CurrentContext.curProgram.shortName;
		}
		
		override void postConstruct() {
			val process = PromelaContext.getContext().allProcesses
				.findFirst[p | p.programName.equals(curProgramName) && p.shortName.equals(processShortName)];
			this.processStateMTypeVar = process.stateVarMType;
			this.processStopStateFullName = process.stopStateMType;
		}
		
		override toText() {
			'''
				«processStateMTypeVar» = «processStopStateFullName»;
			'''
		}
	}
	
	static class ErrorProcess extends PromelaStatement {
		String processShortName;
		String curProgramName;
		
		String processStateMTypeVar;
		String processErrorStateFullName;
		
		new (ErrorProcessStatement s) {
			this.processShortName = s.process !== null ? s.process.name : CurrentContext.curProcess.shortName;
			this.curProgramName = CurrentContext.curProgram.shortName;
		}
		
		override void postConstruct() {
			val process = PromelaContext.getContext().allProcesses
				.findFirst[p | p.programName.equals(curProgramName) && p.shortName.equals(processShortName)];
			this.processStateMTypeVar = process.stateVarMType;
			this.processErrorStateFullName = process.errorStateMtype;
		}
		
		override toText() {
			'''
				«processStateMTypeVar» = «processErrorStateFullName»;
			'''
		}
	}
	
	static class ResetTimer extends PromelaStatement {
		String timeoutVarName;
		
		new () {
			val timeoutVar = CurrentContext.curProcess.timeoutVar;
			this.timeoutVarName = timeoutVar !== null ? timeoutVar.name : null;
		}
		
		override toText() {
			'''
				«IF timeoutVarName !== null»
					«timeoutVarName» = 1;
				«ENDIF»
			'''
		}
		
	}
	
	static class Timeout extends PromelaStatement {
		final PromelaVar.TimeInterval timeoutVar;
		final PromelaExpression timeoutValue;
		final PromelaElementList<PromelaStatement> timeoutStatements = new PromelaElementList();
		
		new (TimeoutStatement t) {
			timeoutVar = CurrentContext.curProcess.timeoutVar;
			if (t.const !== null) {
				val timeoutConst = new PromelaExpression.TimeConstant(t.const.time.interval);
				PromelaContext.getContext().addTimeVal(timeoutConst);
				timeoutValue = timeoutConst;
			}
			else {
				timeoutValue = new PromelaExpression.Var(NamespaceContext.getFullId(t.variable.name));
			}
			timeoutStatements.addElements(PromelaStatementsHelper.getStatementList(t.statement));
		}
		
		def getTimeoutVar() {
			return timeoutVar;
		}
		
		def getTimeoutValue() {
			return timeoutValue;
		}
		
		override toText() {
			val timeoutVarName = timeoutVar.name;
			'''
				if :: «timeoutVarName» >= «timeoutValue.toText» -> {
					«timeoutStatements.toText»
				} :: else -> skip; fi;
				«timeoutVarName» = «timeoutVarName» + 1;
			'''
		}
		
	}
	
}