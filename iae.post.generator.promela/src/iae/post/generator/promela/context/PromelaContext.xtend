package iae.post.generator.promela.context

import iae.post.generator.promela.model.vars.PromelaVar
import java.util.List
import java.util.ArrayList
import iae.post.generator.promela.model.PromelaProcess
import iae.post.generator.promela.model.expressions.PromelaExpression
import iae.post.generator.promela.model.VarSettingProgram
import iae.post.generator.promela.model.statements.PromelaStatement
import java.util.Map
import java.util.HashMap

class PromelaContext {
	static var PromelaContext context;
	static def getContext() {
		if (context === null) {
			context = new PromelaContext();
		}
		return context;
	}
	
	static def clearContext() {
		context = null;
		PromelaStatement.Case.resetCounter();
	}
	
	var List<PromelaVar.TimeInterval> timeVars = new ArrayList();
	var List<PromelaExpression.TimeConstant> timeVals = new ArrayList();
	var List<PromelaProcess> allProcesses = new ArrayList();
	var VarSettingProgram varSettingProgram;
	var Map<String, PromelaVar.Array> arrayVars = new HashMap();
	
	def addTimeVar(String name) {
		val res = new PromelaVar.TimeInterval(name);
		timeVars.add(res);
		return res;
	}
	
	def addTimeVal(PromelaExpression.TimeConstant value) {
		timeVals.add(value);
	}
	
	def addProcessToMType(PromelaProcess process) {
		allProcesses.add(process);
	}
	
	def setVarSettingProgram(VarSettingProgram varSettingProgram) {
		this.varSettingProgram = varSettingProgram;
	}
	
	def addArrayVar(PromelaVar.Array arrayVar) {
		arrayVars.put(arrayVar.name, arrayVar);
	}
	
	def getMetadataAndInitText() {
		val startProcessMType = varSettingProgram !== null ?
									varSettingProgram.processMTypes.get(0)
									: allProcesses.get(0).nameMType;
		'''			
			mtype:P__ = {
				�IF varSettingProgram !== null�
					�FOR varSetterProcessMType : varSettingProgram.processMTypes�
						�NamespaceContext.getName(varSetterProcessMType)�,
					�ENDFOR�
				�ENDIF�
				�FOR process : allProcesses SEPARATOR ','�
					�NamespaceContext.getName(process.nameMType)�
				�ENDFOR�
			}
			chan �NamespaceContext.getName("__currentProcess")� = [1] of { mtype:P__ };
			
			�FOR process : allProcesses�
				�process.getStatesMTypeText(//true if first process of program
					process.fullName.equals(
						allProcesses.findFirst[p | p.programName.equals(process.programName)].fullName
					)
				)�
				�IF process.timeoutVar !== null�
					�process.timeoutVar.toText�
				�ENDIF�
				
			�ENDFOR�
			init {
				�NamespaceContext.getName("__currentProcess")� ! �NamespaceContext.getName(startProcessMType)�;
			}
			
		'''
	}
	
	def getVarSettingProgram() {
		varSettingProgram;
	}
	
	def getTimeVars() {
		return timeVars;
	}
	
	def getTimeVals() {
		return timeVals;
	}
	
	def getAllProcesses() {
		return allProcesses;
	}
	
	def getArrayVar(String fullName) {
		return arrayVars.get(fullName);
	}
}
