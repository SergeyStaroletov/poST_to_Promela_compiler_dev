package su.nsk.iae.post.generator.promela.context

import java.util.Map
import java.util.HashMap
import java.util.Stack
import su.nsk.iae.post.generator.promela.model.WrongModelStateException
import java.util.List
import java.util.ArrayList

class NamespaceContext {
	static var Namespace rootNamespace = new Namespace(null, null);
	static var Namespace current = rootNamespace;
	
	static def void startNamespace(String name) {
		if (current === null) {
			current = rootNamespace;
		}
		var newNamespace = new Namespace(current, name);
		current.children.add(newNamespace);
		current = newNamespace;
	}
	
	static def void endNamespace() {
		current = current.parent;
		if (current === null) {
			throw new WrongModelStateException();
		}
	}
	
	static def getCurrentNamespaceShortName() {
		val nameParts = current.fullName.split("__");
		return nameParts.get(nameParts.size - 1);
	}
	
	static def String addId(String id, String attribute) {
		val attributePrefix = attribute !== null ? attribute + "__" : "";
		val namespacePrefix = current.fullName !== null ? current.fullName + "__" : "";
		val fullId = attributePrefix + namespacePrefix + id;
		current.fullIds.put(attributePrefix + id, fullId);
		return fullId;
	}
	
	static def String addId(String id) {
		return addId(id, null);
	}
	
	static def String getFullId(String id, String attribute) {
		val idWithAttribute = (attribute !== null ? attribute + "__" : "") + id;
		var cur = current;
		var String fullId;
		while (fullId === null && cur !== null) {
			fullId = cur.fullIds.get(idWithAttribute);
			cur = cur.parent;
		}
		if (fullId === null) {
			throw new WrongModelStateException();
		}
		return fullId;
	}
	
	static def String getFullId(String id) {
		return getFullId(id, null);
	}	
	
	private static class Namespace {
		Namespace parent;
		List<Namespace> children = new ArrayList();
		String name;
		String fullName;
		Map<String, String> fullIds = new HashMap();//id -> fullId
		
		new (Namespace parent, String name) {
			val prefix = parent !== null && parent.fullName !== null ? parent.fullName + "__" : "";
			this.name = name;
			this.fullName = name !== null ? prefix + name : null;
			this.parent = parent;
		}
	}
}