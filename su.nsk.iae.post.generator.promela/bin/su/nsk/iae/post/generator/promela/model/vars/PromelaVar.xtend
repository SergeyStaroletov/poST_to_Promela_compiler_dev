package su.nsk.iae.post.generator.promela.model.vars

import su.nsk.iae.post.generator.promela.model.IPromelaElement

abstract class PromelaVar implements IPromelaElement {
	String name;
	String typeName;
	
	new(String name, String typeName) {
		this.name = name;
		this.typeName = typeName;
	}
	
	override toText() {
		'''
			«typeName» «name»;
		'''
	}
}