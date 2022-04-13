package su.nsk.iae.post.generator.promela.context

import su.nsk.iae.post.generator.promela.model.PromelaProgram
import su.nsk.iae.post.generator.promela.model.PromelaProcess
import su.nsk.iae.post.generator.promela.model.PromelaState
import su.nsk.iae.post.generator.promela.statements.PromelaStatement

class CurrentContext {
	static var PromelaProgram curProgram;
	static var PromelaProcess curProcess;
	static var PromelaState curState;
	
	static def void startProgram(PromelaProgram p) {
		curProgram = p;
	}
	static def void startProcess(PromelaProcess p) {
		curProcess = p;
	}
	static def void startState(PromelaState s) {
		curState = s;
	}
	
	static def void stopProgram() {
		curProgram = null;
		stopProcess();
	}
	static def void stopProcess() {
		curProcess = null;
		stopState();
	}
	static def void stopState() {
		curState = null;
	}
	
	static def getCurProgram() {
		return curProgram;
	}
	static def  getCurProcess() {
		return curProcess;
	}
	static def getCurState() {
		return curState;
	}
	
	static def clearContext() {
		stopProgram();
	}
	
}