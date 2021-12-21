package su.nsk.iae.post.generator.promela.model

import java.util.ArrayList
import org.eclipse.xtend2.lib.StringConcatenation

class PromelaElementList<E extends IPromelaElement> extends ArrayList<E> implements IPromelaElement {
	override toText() {
		val stringConcatenation = new StringConcatenation();
		this.forEach([e | stringConcatenation.append(e.toText())]);
		return stringConcatenation.toString();
	}
}