package iae.post.generator.promela.model.statements

import java.util.AbstractMap
import java.util.ArrayList
import java.util.List
import java.util.Map
import iae.post.generator.promela.context.NamespaceContext
import iae.post.generator.promela.model.expressions.PromelaExpression
import iae.post.generator.promela.model.expressions.PromelaExpressionsHelper
import iae.post.generator.promela.model.IPromelaElement
import iae.post.generator.promela.model.PromelaElementList
import iae.post.poST.AssignmentStatement
import iae.post.poST.IfStatement
import iae.post.poST.CaseStatement
import iae.post.poST.SignedInteger
import iae.post.poST.StartProcessStatement
import iae.post.generator.promela.context.PromelaContext
import iae.post.generator.promela.exceptions.WrongModelStateException
import iae.post.poST.StopProcessStatement
import iae.post.poST.ErrorProcessStatement
import iae.post.poST.SetStateStatement
import iae.post.generator.promela.context.CurrentContext
import iae.post.generator.promela.model.PromelaState
import iae.post.generator.promela.model.PromelaProcess
import iae.post.generator.promela.context.PostConstructContext
import iae.post.generator.promela.context.PostConstructContext.IPostConstuctible
import iae.post.poST.TimeoutStatement
import iae.post.generator.promela.model.vars.PromelaVar
import iae.post.poST.WhileStatement
import iae.post.poST.RepeatStatement
import iae.post.poST.ForStatement

abstract class PromelaStatement implements IPromelaElement, IPostConstuctible {
	
	new () {
		PostConstructContext.register(this);
	}
	
	override void postConstruct() {}
	
	
	static class Assign extends PromelaStatement {
		final String fullVarName;
		final PromelaExpression.ArrayVar arrayElement;
		final PromelaExpression value;
		
		new (AssignmentStatement s) {
			if (s.variable !== null) {
				this.fullVarName = NamespaceContext.getFullId(s.variable.name);
				this.arrayElement = null;
			}
			else {
				this.fullVarName = NamespaceContext.getFullId(s.array.variable.name);
				this.arrayElement = new PromelaExpression.ArrayVar(
					PromelaContext.context.getArrayVar(fullVarName),
					PromelaExpressionsHelper.getExpr(s.array.index)
				);
			}
			this.value = PromelaExpressionsHelper.getExpr(s.value);
		}
		
		override toText() {
			'''
				«IF arrayElement === null»
					«NamespaceContext.getName(fullVarName)» = «value.toText»;
				«ELSE»
					«arrayElement.toText» = «value.toText»;
				«ENDIF»
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
		
		String caseConditionValueVarName = "tmp___caseCondVal" + caseStatements++;
		PromelaExpression cond;
		List<Map.Entry<List<SignedInteger>, PromelaElementList<? extends PromelaStatement>>> caseElements = new ArrayList();
		PromelaElementList<? extends PromelaStatement> elseCase;
		
		new (CaseStatement s) {
			cond = PromelaExpressionsHelper.getExpr(s.cond);
			//todo: fix
			/* 
			s.caseElements.forEach[e | caseElements.add(new AbstractMap.SimpleEntry(
				e.caseList.caseListElement,
				new PromelaElementList().addElements(PromelaStatementsHelper.getStatementList(e.statement))
			))];
			elseCase =  s.elseStatement !== null ? new PromelaElementList().addElements(PromelaStatementsHelper.getStatementList(s.elseStatement)) : null;
		*/
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
		
		static def resetCounter() {
			caseStatements = 0;
		}
	}
	
	static class StartProcess extends PromelaStatement {
		String curProgramName;
		String curProcessName;
		String processShortName;
		
		String processStateMTypeVar;
		String processFirstStateMTypeName;
		String timeoutVarName;
		boolean startsInNextCycle;
		
		new (StartProcessStatement s) {
			this.curProgramName = CurrentContext.curProgram.shortName;
			this.curProcessName = CurrentContext.curProcess.shortName;
			this.processShortName = s.process !== null ? s.process.name : CurrentContext.curProcess.shortName;
		}
		
		override void postConstruct() {
			val process = PromelaContext.getContext().allProcesses
				.findFirst[p | p.programName.equals(curProgramName) && p.shortName.equals(processShortName)];
			if (process === null) {
				throw new WrongModelStateException("Could not get statement process");
			}
			this.processStateMTypeVar = process.stateVarMType;
			val state = process.states.get(0);
			this.processFirstStateMTypeName = state.stateMType;
			this.timeoutVarName = state.timeout !== null ? process.timeoutVar.name : null;
			val firstDefinedProcessShortName = PromelaContext.getContext().allProcesses
				.filter[p | p.programName.equals(curProgramName)]
				.map[p | p.shortName]
				.findFirst[name | name.equals(curProcessName) || name.equals(processShortName)];
			if (firstDefinedProcessShortName === null) {
				throw new WrongModelStateException("Could not get first defined process short name");
			}
			this.startsInNextCycle = firstDefinedProcessShortName.equals(processShortName);
		}
		
		override toText() {
			'''
				«IF timeoutVarName !== null»
					«NamespaceContext.getName(timeoutVarName)» = «startsInNextCycle ? 1 : 0»;
				«ENDIF»
				«NamespaceContext.getName(processStateMTypeVar)» = «NamespaceContext.getName(processFirstStateMTypeName)»;
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
					«NamespaceContext.getName(timeoutVarName)» = 1;
				«ENDIF»
				«NamespaceContext.getName(processStateMTypeVar)» = «NamespaceContext.getName(stateMTypeName)»;
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
				«NamespaceContext.getName(processStateMTypeVar)» = «NamespaceContext.getName(processStopStateFullName)»;
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
				«NamespaceContext.getName(processStateMTypeVar)» = «NamespaceContext.getName(processErrorStateFullName)»;
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
					«NamespaceContext.getName(timeoutVarName)» = 0;
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
			val timeoutVarName = NamespaceContext.getName(timeoutVar.name);
			'''
				if :: «timeoutVarName» > «timeoutValue.toText» -> {
					«timeoutStatements.toText»
				} :: else -> «timeoutVarName» = «timeoutVarName» + 1; fi;
			'''
		}
	}
	
	static class While extends PromelaStatement {
		PromelaExpression condition;
		PromelaElementList<? extends PromelaStatement> statements;
		
		new (WhileStatement w) {
			this.condition = PromelaExpressionsHelper.getExpr(w.cond);
			this.statements = new PromelaElementList()
				.addElements(PromelaStatementsHelper.getStatementList(w.statement));
		}
		
		override toText() {
			'''
				do
					:: «condition.toText» -> {
						«statements.toText»
					}
					:: else -> break;
				od;
			'''
		}
	}
	
	static class Repeat extends PromelaStatement {
		PromelaExpression condition;
		PromelaElementList<? extends PromelaStatement> statements;
		
		new (RepeatStatement r) {
			this.condition = PromelaExpressionsHelper.getExpr(r.cond);
			this.statements = new PromelaElementList()
				.addElements(PromelaStatementsHelper.getStatementList(r.statement));
		}
		
		override toText() {
			'''
				«statements.toText»
				do
					:: «condition.toText» -> {
						«statements.toText»
					}
					:: else -> break;
				od;
			'''
		}
	}
	
	static class For extends PromelaStatement {
		String varFullId;
		PromelaExpression start;
		PromelaExpression end;
		PromelaExpression step;
		PromelaElementList<? extends PromelaStatement> statements;
		
		new (ForStatement f) {
			this.varFullId = NamespaceContext.getFullId(f.variable.name);
			this.statements = new PromelaElementList()
				.addElements(PromelaStatementsHelper.getStatementList(f.statement));
			this.start = PromelaExpressionsHelper.getExpr(f.forList.start);
			this.end = PromelaExpressionsHelper.getExpr(f.forList.end);
			this.step = f.forList.step !== null ? PromelaExpressionsHelper.getExpr(f.forList.step) : null;
		}
		
		override toText() {
			val varName = NamespaceContext.getName(varFullId);
			'''
				«varName» = «start.toText»;
				do
					:: «varName» <= «end.toText» -> {
						«statements.toText»
						«varName» = «varName» + «IF step !== null»(«step.toText»)«ELSE»1«ENDIF»;
					}
					:: else -> break;
				od;
			'''
		}
	}
	
}