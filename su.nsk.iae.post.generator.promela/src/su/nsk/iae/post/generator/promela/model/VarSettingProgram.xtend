package su.nsk.iae.post.generator.promela.model

import su.nsk.iae.post.generator.promela.context.NamespaceContext
import java.util.ArrayList
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar
import java.util.function.Supplier
import java.util.List
import java.util.HashMap
import su.nsk.iae.post.generator.promela.exceptions.NotSupportedElementException
import su.nsk.iae.post.generator.promela.exceptions.WrongModelStateException

class VarSettingProgram implements IPromelaElement {
	static val processPrefix = "specialProcess";
	static val mTypePrefix = "sP";
	static val gremlinProcessId = "Gremlin";
	static val varsSetterProcessId = "VarsSetter";
	
	var String firstProcessMType;
	val gremlinTextSuppliers = new ArrayList<Supplier<String>>();
	val outputToInputVars = new HashMap<String, List<String>>();//ouputFullId -> List<inputFullId>
	var String gremlinProcessFullId;
	var String gremlinMTypeFullId;
	var String varsSetterProcessFullId;
	var String varsSetterMTypeFullId;
	
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
						:: «name» = true;
						:: «name» = false;
					fi;
				'''
			];
		}
		else if (v instanceof PromelaVar.Byte) {
			gremlinTextSuppliers.add[
				val name = NamespaceContext.getName(fullId);
				'''
					select («name» : 0..255);
				'''
			];
		}
		else if (v instanceof PromelaVar.Short) {
			if (v.getWasSByte()) {
				gremlinTextSuppliers.add[
					val name = NamespaceContext.getName(fullId);
					'''
						select («name» : -128..127);
					'''
				];
			}
			else {
				throw new NotSupportedElementException();
			}
		}
		else {
			throw new NotSupportedElementException();
		}
		if (gremlinProcessFullId === null) {
			gremlinProcessFullId = NamespaceContext.addId(gremlinProcessId, processPrefix);
			gremlinMTypeFullId = NamespaceContext.addId(gremlinProcessId, mTypePrefix);
		}
	}
	
	def addOutputToInputAssignments(String outFullId, List<String> inFullIds) {
		outputToInputVars.computeIfAbsent(outFullId, [new ArrayList()]).addAll(inFullIds);
		if (varsSetterProcessFullId === null) {
			varsSetterProcessFullId = NamespaceContext.addId(varsSetterProcessId, processPrefix);
			varsSetterMTypeFullId = NamespaceContext.addId(varsSetterProcessId, mTypePrefix);
		}
	}
	
	def getProcessMTypes() {
		val res = new ArrayList<String>();
		if (!gremlinTextSuppliers.isEmpty()) {
			res.add(gremlinMTypeFullId);
		}
		if (!outputToInputVars.isEmpty()) {
			res.add(varsSetterMTypeFullId);
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
				active proctype «NamespaceContext.getName(gremlinProcessFullId)»() {
					do :: __currentProcess ? «NamespaceContext.getName(gremlinMTypeFullId)» ->
						atomic {
							«FOR gremlinTextSupplier : gremlinTextSuppliers»
								«gremlinTextSupplier.get()»
							«ENDFOR»
							«IF !outputToInputVars.isEmpty()»
								__currentProcess ! «NamespaceContext.getName(varsSetterMTypeFullId)»;
							«ELSE»
								__currentProcess ! «NamespaceContext.getName(firstProcessMType)»;
							«ENDIF»
						}
					od;
				}
			«ENDIF»
			
			«IF !outputToInputVars.isEmpty()»
				active proctype «NamespaceContext.getName(varsSetterProcessFullId)»() {
					do :: __currentProcess ? «NamespaceContext.getName(varsSetterMTypeFullId)» ->
						atomic {
							«FOR outToIns : outputToInputVars.entrySet»
								«FOR in : outToIns.value»
									«NamespaceContext.getName(in)» = «NamespaceContext.getName(outToIns.key)»;«
									»«IF NamespaceContext.isNamesWithNumbersMode
										» //«NamespaceContext.getNameWithNamespaces(in)» <- «
										NamespaceContext.getNameWithNamespaces(outToIns.key)»«
									ENDIF»
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