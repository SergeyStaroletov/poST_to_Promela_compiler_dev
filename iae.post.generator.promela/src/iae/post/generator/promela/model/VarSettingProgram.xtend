package iae.post.generator.promela.model

import iae.post.generator.promela.context.NamespaceContext
import java.util.ArrayList
import iae.post.generator.promela.model.vars.PromelaVar
import java.util.function.Supplier
import java.util.List
import java.util.HashMap
import iae.post.generator.promela.exceptions.NotSupportedElementException
import iae.post.generator.promela.exceptions.WrongModelStateException
import iae.post.generator.promela.context.WarningsContext

class VarSettingProgram implements IPromelaElement {
	static val processPrefix = "specialProcess";
	static val mTypePrefix = "sP";
	static val helperProcessId = "Helper";
	static val gremlinProcessId = "Gremlin";
	static val varsSetterProcessId = "VarsSetter";
	
	var String firstProcessMType;
	val gremlinTextSuppliers = new ArrayList<Supplier<String>>();
	val outputToInputVars = new HashMap<String, List<String>>();//ouputFullId -> List<inputFullId>
	var String helperProcessFullId;
	var String helperMTypeFullId;
	var String gremlinProcessFullId;
	var String gremlinMTypeFullId;
	var String varsSetterProcessFullId;
	var String varsSetterMTypeFullId;
	
	new () {
		helperProcessFullId = NamespaceContext.addId(helperProcessId, processPrefix);
		helperMTypeFullId = NamespaceContext.addId(helperProcessId, mTypePrefix);
	}
	
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
				WarningsContext.addWarning('''Gremlin variable for full id «fullId» was not created:«
					» poST type SINT is not supported''')
				return;
			}
		}
		else {
			WarningsContext.addWarning('''Gremlin variable for full id «fullId» was not created:«
				» type «v.type» is not supported''')
			return;
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
		res.add(helperMTypeFullId);
		return res;
	}
	
	override toText() {
		val mtypes = getProcessMTypes();
		val afterGremlinMType = getMTypeAfter(gremlinMTypeFullId, mtypes);
		val afterVarsSetterMType = getMTypeAfter(varsSetterMTypeFullId, mtypes);
		val afterHelperMType = getMTypeAfter(helperMTypeFullId, mtypes);
		'''
			//-----------------------------------------------------------------------------
			//-----------------------------------------------------------------------------
			//special processes
			//-----------------------------------------------------------------------------
			//-----------------------------------------------------------------------------
			
			«IF !gremlinTextSuppliers.isEmpty()»
				active proctype «NamespaceContext.getName(gremlinProcessFullId)»() {
					do :: __currentProcess ? «NamespaceContext.getName(gremlinMTypeFullId)» ->
						atomic {
							«FOR gremlinTextSupplier : gremlinTextSuppliers»
								«gremlinTextSupplier.get()»
							«ENDFOR»
							__currentProcess ! «NamespaceContext.getName(afterGremlinMType)»;
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
							__currentProcess ! «NamespaceContext.getName(afterVarsSetterMType)»;
						}
					od;
				}
			«ENDIF»
			
			bool cycle__u;
			active proctype «NamespaceContext.getName(helperProcessFullId)»() {
				do :: __currentProcess ? «NamespaceContext.getName(helperMTypeFullId)» ->
					cycle__u = true;
					atomic {
						cycle__u = false;
						__currentProcess ! «NamespaceContext.getName(afterHelperMType)»;
					}
				od;
			}
		''';
	}
	
	private def getMTypeAfter(String mtypeFullId, List<String> mtypeFullIds) {
		val index = mtypeFullIds.indexOf(mtypeFullId);
		return index != -1 && index + 1 < mtypeFullIds.length ?
					mtypeFullIds.get(index + 1) : firstProcessMType;
	}
	
}