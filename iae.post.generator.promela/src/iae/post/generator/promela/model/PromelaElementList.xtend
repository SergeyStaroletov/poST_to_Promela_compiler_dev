package iae.post.generator.promela.model

import java.util.ArrayList
import org.eclipse.xtend2.lib.StringConcatenation
import java.util.Collection

class PromelaElementList<E extends IPromelaElement> extends ArrayList<E> implements IPromelaElement {
	final String separator;
	
	new(String separator) {
		this.separator = separator;
	}
	
	new() {
		this("");
	}
	
	def addElements(Collection<E> elements) {
		this.addAll(elements);
		return this;
	}
	
	override toText() {
		'''
			«FOR e : this SEPARATOR separator»
				«e.toText()»
			«ENDFOR»
		'''
	}
}