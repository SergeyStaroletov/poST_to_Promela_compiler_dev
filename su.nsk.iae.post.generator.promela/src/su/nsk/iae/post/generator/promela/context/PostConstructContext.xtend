package su.nsk.iae.post.generator.promela.context

import java.util.List
import java.util.ArrayList

class PostConstructContext {
	static List<IPostConstuctible> elements = new ArrayList();
	
	static def register(IPostConstuctible e) {
		elements.add(e);
	}
	
	static def postConstruct() {
		elements.forEach[e | e.postConstruct()];
	}
	
	static def clearContext() {
		elements = new ArrayList();
	}
	
	
	interface IPostConstuctible {
		def void postConstruct();
	}
}