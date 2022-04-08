package su.nsk.iae.post.generator.promela.model

import su.nsk.iae.post.generator.promela.context.NamespaceContext
import java.util.ArrayList
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar
import java.util.function.Supplier
import java.util.List
import java.util.HashMap

class VarSettingProgram implements IPromelaElement {
	static val gremlinMType = "sP__Gremlin";
	static val varsSetterMType = "sP__VarsSetter";
	
	var String firstProcessMType;
	val gremlinTextSuppliers = new ArrayList<Supplier<String>>();
	val outputToInputVars = new HashMap<String, List<String>>();//ouputFullId -> List<inputFullId>
	
	def setFirstProcess(String firstProcessMType) {
		this.firstProcessMType = firstProcessMType;
	}
	
	def addGremlinVar(PromelaVar v) {
		val fullId = v.name;
		if (v instanceof PromelaVar.Bool) {
			gremlinTextSuppliers.add[
				val name = NamespaceContext.getName(fullId);
				'''
					if
						:: true -> «name» = true;
						:: true -> «name» = false;
					fi;
				'''
			];
		}
		else {
			throw new NotSupportedElementException();
		}
	}
	
	def addOutputToInputAssignments(String outFullId, List<String> inFullIds) {
		outputToInputVars.computeIfAbsent(outFullId, [new ArrayList()]).addAll(inFullIds);
	}
	
	def getProcessMTypes() {
		val res = new ArrayList<String>();
		if (!gremlinTextSuppliers.isEmpty()) {
			res.add(gremlinMType);
		}
		if (!outputToInputVars.isEmpty()) {
			res.add(varsSetterMType);
		}
		if (res.isEmpty()) {
			throw new WrongModelStateException();
		}
		return res;
	}
	
	override toText() {
		'''
			//-----------------------------------------------------------------------------
			//-----------------------------------------------------------------------------
			//input, output, inout vars setting program
			//-----------------------------------------------------------------------------
			//-----------------------------------------------------------------------------
			
			«IF !gremlinTextSuppliers.isEmpty()»
				active proctype specialProcess__Gremlin() {
					do :: __currentProcess ? «gremlinMType» ->
						atomic {
							«FOR gremlinTextSupplier : gremlinTextSuppliers»
								«gremlinTextSupplier.get()»
							«ENDFOR»
							«IF !outputToInputVars.isEmpty()»
								__currentProcess ! «varsSetterMType»;
							«ELSE»
								__currentProcess ! «NamespaceContext.getName(firstProcessMType)»;
							«ENDIF»
						}
					od;
				}
			«ENDIF»
			
			«IF !outputToInputVars.isEmpty()»
				active proctype specialProcess__VarsSetter() {
					do :: __currentProcess ? «varsSetterMType» ->
						atomic {
							«FOR outToIns : outputToInputVars.entrySet»
								«FOR in : outToIns.value»
									«NamespaceContext.getName(in)» = «NamespaceContext.getName(outToIns.key)»;
								«ENDFOR»
							«ENDFOR»
							__currentProcess ! «NamespaceContext.getName(firstProcessMType)»;
						}
					od;
				}
			«ENDIF»
		''';
	}
	
}