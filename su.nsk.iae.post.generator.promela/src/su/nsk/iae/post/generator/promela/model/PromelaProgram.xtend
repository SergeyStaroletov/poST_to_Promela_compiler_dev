package su.nsk.iae.post.generator.promela.model

import su.nsk.iae.post.generator.promela.model.vars.*
import su.nsk.iae.post.generator.promela.model.IPromelaElement
import su.nsk.iae.post.poST.Program
import java.util.stream.Collectors
import java.util.List
import java.util.ArrayList
import su.nsk.iae.post.generator.promela.context.NamespaceContext
import su.nsk.iae.post.generator.promela.context.CurrentContext

class PromelaProgram implements IPromelaElement {
	final String shortName;
	final String fullName;
	final PromelaElementList<PromelaVar> inVars = new PromelaElementList();
	final PromelaElementList<PromelaVar> outVars = new PromelaElementList();
	final PromelaElementList<PromelaVar> inOutVars = new PromelaElementList();
	final PromelaElementList<PromelaVar> vars = new PromelaElementList();
	final PromelaElementList<PromelaVar> constants = new PromelaElementList();
	
	final PromelaElementList<PromelaProcess> processes = new PromelaElementList("\r\n");
	
	new(Program program) {
		CurrentContext.startProgram(this);
		this.shortName = program.name;
		this.fullName = NamespaceContext.addId(program.name);
		NamespaceContext.startNamespace(program.name);
		
		program.progInVars.forEach([d | inVars.addAll(PromelaVarsHelper.getVars(d.vars))]);
		program.progOutVars.forEach([d | outVars.addAll(PromelaVarsHelper.getVars(d.vars))]);
		program.progInOutVars.forEach([d | inOutVars.addAll(PromelaVarsHelper.getVars(d.vars))]);
		program.progVars.forEach([d | PromelaVarsHelper.getVars(d.vars, d.const)
			.forEach([v | (v.isConstant() ? constants : vars).add(v)])
		]);
		//todo other var types
		
		program.processes.forEach[p | processes.add(new PromelaProcess(p))];
		
		NamespaceContext.endNamespace();
		CurrentContext.stopProgram();
	}
	
	override toText() {
		'''
			//-----------------------------------------------------------------------------
			//-----------------------------------------------------------------------------
			//program «NamespaceContext.getName(fullName)»
			//-----------------------------------------------------------------------------
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
			«processes.toText()»
		''';
	}
	
	def getShortName() {
		return shortName;
	}
	
}