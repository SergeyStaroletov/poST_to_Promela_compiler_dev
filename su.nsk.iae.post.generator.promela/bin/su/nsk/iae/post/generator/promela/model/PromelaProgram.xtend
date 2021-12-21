package su.nsk.iae.post.generator.promela.model

import su.nsk.iae.post.generator.promela.model.vars.*
import su.nsk.iae.post.generator.promela.model.IPromelaElement
import su.nsk.iae.post.poST.Program
import java.util.stream.Collectors
import java.util.List
import java.util.ArrayList

class PromelaProgram implements IPromelaElement {
	final String name;
	final PromelaElementList<PromelaVar> inVars = new PromelaElementList();
	final PromelaElementList<PromelaVar> outVars = new PromelaElementList();
	final PromelaElementList<PromelaVar> inOutVars = new PromelaElementList();
	
	new(Program program) {
		name = program.name;
		program.progInVars.forEach([d | inVars.addAll(PromelaVarsHelper.getVars(d.vars))]);
		program.progOutVars.forEach([d | outVars.addAll(PromelaVarsHelper.getVars(d.vars))]);
		program.progInOutVars.forEach([d | inOutVars.addAll(PromelaVarsHelper.getVars(d.vars))]);
		//todo other var types
	}
	
	override toText() {
		'''
			//program "«name»"
			
			«IF !inVars.isEmpty()»
			//input variables
			«inVars.toText()»
			
			«ENDIF»
			«IF !outVars.isEmpty()»
			//output variables
			«outVars.toText()»
			
			«ENDIF»
			«IF !inOutVars.isEmpty()»
			//inout variables
			«inOutVars.toText()»
			
			«ENDIF»
		''';
	}
	
}