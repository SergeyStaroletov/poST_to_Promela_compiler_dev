package su.nsk.iae.post.generator.promela.context

import su.nsk.iae.post.generator.promela.model.vars.PromelaVar
import java.util.List
import java.util.ArrayList
import su.nsk.iae.post.generator.promela.model.PromelaProcess
import su.nsk.iae.post.generator.promela.expressions.PromelaExpression
import su.nsk.iae.post.generator.promela.model.VarSettingProgram
import su.nsk.iae.post.generator.promela.statements.PromelaStatement

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
	
	def getMetadataAndInitText() {
		val startProcessMType = varSettingProgram !== null ?
									varSettingProgram.processMTypes.get(0)
									: allProcesses.get(0).nameMType;
		'''			
			mtype:P__ = {
				«IF varSettingProgram !== null»
					«FOR varSetterProcessMType : varSettingProgram.processMTypes»
						«NamespaceContext.getName(varSetterProcessMType)»,
					«ENDFOR»
				«ENDIF»
				«FOR process : allProcesses SEPARATOR ','»
					«NamespaceContext.getName(process.nameMType)»
				«ENDFOR»
			}
			chan «NamespaceContext.getName("__currentProcess")» = [1] of { mtype:P__ };
			
			«FOR process : allProcesses»
				«process.getStatesMTypeText(//true if first process of program
					process.fullName.equals(
						allProcesses.findFirst[p | p.programName.equals(process.programName)].fullName
					)
				)»
				«IF process.timeoutVar !== null»
					«process.timeoutVar.toText»
				«ENDIF»
				
			«ENDFOR»
			init {
				«NamespaceContext.getName("__currentProcess")» ! «NamespaceContext.getName(startProcessMType)»;
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
}
