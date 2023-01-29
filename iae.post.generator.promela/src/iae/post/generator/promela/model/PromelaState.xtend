package su.nsk.iae.post.generator.promela.model

import su.nsk.iae.post.poST.State
import su.nsk.iae.post.generator.promela.model.statements.PromelaStatement
import su.nsk.iae.post.generator.promela.model.statements.PromelaStatementsHelper
import su.nsk.iae.post.generator.promela.context.CurrentContext
import su.nsk.iae.post.generator.promela.context.NamespaceContext

class PromelaState implements IPromelaElement {
	final String shortName;
	final String stateMType;
	final String stateVarMType;
	final PromelaElementList<PromelaStatement> statements = new PromelaElementList();
	final PromelaStatement.Timeout timeout;
	
	new (State s, String stateMType, String stateVarMType) {
		CurrentContext.startState(this);
		this.shortName = s.name;
		this.stateMType = stateMType;
		this.stateVarMType = stateVarMType;
		statements.addElements(PromelaStatementsHelper.getStatementList(s.statement));
		this.timeout = s.timeout !== null ? new PromelaStatement.Timeout(s.timeout) : null;
		CurrentContext.stopState();
	}
	
	def getStateMType() {
		return stateMType;
	}
	
	def getShortName() {
		return shortName;
	}
	
	def getTimeout() {
		return timeout;
	}
	
	override toText() {
		'''
			:: «NamespaceContext.getName(stateMType)» == «NamespaceContext.getName(stateVarMType)» -> {
				«statements.toText»
				«IF timeout !== null»
					«timeout.toText»
				«ENDIF»
			}
		'''
	}
	
}