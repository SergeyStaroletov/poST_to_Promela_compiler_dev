package su.nsk.iae.post.generator.promela.context

import java.util.Map
import java.util.HashMap
import su.nsk.iae.post.generator.promela.exceptions.WrongModelStateException
import java.util.List
import java.util.ArrayList

class NamespaceContext {
	static var NAMES_WITH_NUMBERS_MODE = true;
	
	static var Namespace rootNamespace = new Namespace(null, null);
	static var Namespace current = rootNamespace;
	static FullIdsToNamesMapper namesMapper = new FullIdsToNamesMapper(NAMES_WITH_NUMBERS_MODE);
	
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
			throw new WrongModelStateException("Attempted to end root namespace");
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
		current.fullIdParts.put(attributePrefix + id,
			new FullIdParts(
				attribute,
				current.fullName === null ? new ArrayList() : current.fullName.split("__"),
				id
			)
		);
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
			throw new WrongModelStateException('''No fullId for id "«id»" and attribute "«attribute»"''');
		}
		return fullId;
	}
	
	static def String getFullId(String id) {
		return getFullId(id, null);
	}	
	
	static def prepareNamesMapping() {
		namesMapper.processNamespace(rootNamespace);
	}
	
	static def getName(String fullId) {
		return namesMapper.getName(fullId);
	}
	
	static def getNameWithNamespaces(String fullId) {
		return namesMapper.getNameWithNamespaces(fullId);
	}
	
	static def isNamesWithNumbersMode() {
		return namesMapper.isNumberMode();
	}
	
	static def clearContext() {
		rootNamespace = new Namespace(null, null);
		current = rootNamespace;
		namesMapper = new FullIdsToNamesMapper(namesMapper.isNumberMode);
	}
	
	
	public static class Namespace {
		Namespace parent;
		List<Namespace> children = new ArrayList();
		String name;
		String fullName;
		Map<String, String> fullIds = new HashMap();//id -> fullId
		Map<String, FullIdParts> fullIdParts = new HashMap();//id -> fullIdParts
		
		new (Namespace parent, String name) {
			val prefix = parent !== null && parent.fullName !== null ? parent.fullName + "__" : "";
			this.name = name;
			this.fullName = name !== null ? prefix + name : null;
			this.parent = parent;
		}
		
		def getFullIds() {
			return fullIds;
		}
		
		def getFullIdParts() {
			return fullIdParts;
		}
		
		def getChildrenNamespaces() {
			return children;
		}
		
		def getName() {
			return name;
		}
		
		def getFullName() {
			return fullName;
		}
	}
	
	public static class FullIdParts {
		String prefix;
		List<String> namespaceNames;
		String id;
		
		new (String prefix, List<String> namespaceNames, String id) {
			this.prefix = prefix;
			this.namespaceNames = namespaceNames;
			this.id = id;
		}
		
		def getPrefix() {
			return prefix;
		}
		
		def getNamespaceNames() {
			return namespaceNames;
		}
		
		def getId() {
			return id;
		}
		
		def getFullId() {
			val prefixPart = prefix !== null ? prefix + "__" : "";
			return prefixPart + namespaceNames.map[n | n + "__"].join() + id;
		}
	}
}