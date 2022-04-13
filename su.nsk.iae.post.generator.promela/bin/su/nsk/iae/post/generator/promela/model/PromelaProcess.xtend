package su.nsk.iae.post.generator.promela.model

import su.nsk.iae.post.poST.Process
import su.nsk.iae.post.generator.promela.context.PromelaContext
import su.nsk.iae.post.generator.promela.context.NamespaceContext
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar
import su.nsk.iae.post.generator.promela.model.vars.PromelaVarsHelper
import su.nsk.iae.post.generator.promela.context.CurrentContext

class PromelaProcess implements IPromelaElement {
	String programName;
	String shortName;
	String fullName;
	String nameMType;
	String nextMType;
	String stateTypeMType;
	String stateVarMType;
	String stateStopMType;
	String stateErrorMType;
	PromelaVar.TimeInterval timeoutVar;
	final PromelaElementList<PromelaVar> inVars = new PromelaElementList();
	final PromelaElementList<PromelaVar> outVars = new PromelaElementList();
	final PromelaElementList<PromelaVar> inOutVars = new PromelaElementList();
	final PromelaElementList<PromelaVar> vars = new PromelaElementList();
	final PromelaElementList<PromelaVar> constants = new PromelaElementList();
	PromelaElementList<PromelaState> states = new PromelaElementList();
	
	new (Process p) {
		CurrentContext.startProcess(this);
		this.programName = CurrentContext.curProgram.shortName;
		this.shortName = p.name;
		this.fullName = NamespaceContext.addId(p.name, "process");
		this.nameMType = NamespaceContext.addId(p.name, "p");
		this.nextMType = nameMType;
		stateTypeMType = NamespaceContext.addId(p.name, "S");
		stateVarMType = NamespaceContext.addId(p.name, "curS");
		if (p.states.findFirst[state | state.timeout !== null] !== null) {
			val timeoutVarFullName = NamespaceContext.addId(this.shortName, "timeout");
			timeoutVar = new PromelaVar.TimeInterval(timeoutVarFullName);
		}
		NamespaceContext.startNamespace(p.name);
		
		p.procInVars.forEach([d | inVars.addAll(PromelaVarsHelper.getVars(d.vars))]);
		p.procOutVars.forEach([d | outVars.addAll(PromelaVarsHelper.getVars(d.vars))]);
		p.procInOutVars.forEach([d | inOutVars.addAll(PromelaVarsHelper.getVars(d.vars))]);
		p.procVars.forEach([d | PromelaVarsHelper.getVars(d.vars, d.const)
			.forEach([v | (v.isConstant() ? constants : vars).add(v)]);
		]);
		
		p.states.forEach[s | states.add(new PromelaState(s, NamespaceContext.addId(s.name, "s"), stateVarMType))];
		this.stateStopMType = NamespaceContext.addId("Stop", "s");
		this.stateErrorMType = NamespaceContext.addId("Error", "s");
		
		PromelaContext.getContext().addProcessToMType(this);
		
		NamespaceContext.endNamespace();
		CurrentContext.stopProcess();
	}
	
	def setNextMType(String p) {
		nextMType = p;
	}
	
	def getProgramName() {
		return programName;
	}
	
	def getShortName() {
		return shortName;
	}
	
	def getFullName() {
		return fullName;
	}
	
	def getNameMType() {
		return nameMType;
	}
	
	def getStopStateMType() {
		return stateStopMType;
	}
	
	def getErrorStateMtype() {
		return stateErrorMType;
	}
	
	def getStateVarMType() {
		return stateVarMType;
	}
	
	def getStates() {
		return states;
	}
	
	def getTimeoutVar() {
		return timeoutVar;
	}
	
	def getStatesMTypeText(boolean initiallyRunning) {
		'''
			mtype:«NamespaceContext.getName(stateTypeMType)» = {
				«FOR s : states»
					«NamespaceContext.getName(s.stateMType)»,
				«ENDFOR»
				«NamespaceContext.getName(stateStopMType)»,
				«NamespaceContext.getName(stateErrorMType)»
			}
			mtype:«NamespaceContext.getName(stateTypeMType)» «NamespaceContext.getName(stateVarMType)» = «
			NamespaceContext.getName(initiallyRunning ? states.get(0).stateMType : stateStopMType)»;
		'''
	}
	
	override toText() {
		'''
			
			//-----------------------------------------------------------------------------
			//«NamespaceContext.getName(fullName)»
			//-----------------------------------------------------------------------------
			
			«IF !constants.isEmpty()»
			//constants
			«constants.toText()»
			
			«ENDIF»
			«IF !inVars.isEmpty()»
			//input
			«inVars.toText()»
			
			«ENDIF»
			«IF !outVars.isEmpty()»
			//output
			«outVars.toText()»
			
			«ENDIF»
			«IF !inOutVars.isEmpty()»
			//inout
			«inOutVars.toText()»
			
			«ENDIF»
			«IF !vars.isEmpty()»
			//vars
			«vars.toText()»
			
			«ENDIF»
			active proctype «NamespaceContext.getName(fullName)»() {
				do :: «NamespaceContext.getName("__currentProcess")» ? «NamespaceContext.getName(nameMType)» ->
					atomic {
						if
							«states.toText()»
							:: else -> skip;
						fi;
						«NamespaceContext.getName("__currentProcess")» ! «NamespaceContext.getName(nextMType)»;
					}
				od;
			}
		'''
	}
	
}