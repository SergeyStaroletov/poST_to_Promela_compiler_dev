package su.nsk.iae.post.generator.promela.context

import java.util.Map
import java.util.HashMap
import su.nsk.iae.post.generator.promela.model.WrongModelStateException
import su.nsk.iae.post.generator.promela.context.NamespaceContext.Namespace
import java.util.List
import java.util.ArrayList
import java.util.AbstractMap
import java.util.LinkedList
import su.nsk.iae.post.generator.promela.context.NamespaceContext.FullIdParts

class FullIdsToNamesMapper {
	Map<String, String> fullIdsToNames = new HashMap();
	SimplifyingNamespace rootNamespace = new SimplifyingNamespace(null);
	
	def processNamespace(Namespace namespace) {
		copyToSimplifyingNamespace(namespace, rootNamespace);
		shortenChildrenNamespaceNames(rootNamespace);
		fillNames(rootNamespace, rootNamespace.name);
		return;
	}
	
	def getName(String fullId) {
		var res = fullIdsToNames.get(fullId);
		if (res === null) {
			throw new WrongModelStateException();
		}
		return res;
	}
	
	private def copyToSimplifyingNamespace(Namespace namespace, SimplifyingNamespace simplifyingNamespace) {
		namespace.childrenNamespaces.forEach[ns | copyToSimplifyingNamespace(ns,
			simplifyingNamespace.addChildNamespace(ns.name)
		)];
		namespace.fullIdParts.forEach[id, fullIdParts |
			simplifyingNamespace.addFullId(fullIdParts);
		];
	}
	
	private def shortenChildrenNamespaceNames(SimplifyingNamespace namespace) {
		val prefixToName = new HashMap<String, List<String>>();
		val conflictingPrefixes = new ArrayList<String>();
		for (c : namespace.children) {
			val prefix = c.name.substring(0, 1);
			val names = prefixToName.computeIfAbsent(prefix, [new ArrayList()]);
			names.add(c.name);
			if (names.size == 2) {
				conflictingPrefixes.add(prefix);
			}
		}
		while (!conflictingPrefixes.isEmpty()) {
			for (conflictingPrefix : conflictingPrefixes) {
				val prefixLength = conflictingPrefix.length + 1;
				val conflictingNames = prefixToName.remove(conflictingPrefix);
				for (name : conflictingNames) {
					val prefix = name.substring(0, prefixLength);
					val names = prefixToName.computeIfAbsent(prefix, [new ArrayList()]);
					names.add(name);
					if (names.size == 2) {
						conflictingPrefixes.add(prefix);
					}
				}
			}
		}
		
		val nameToPrefix = new HashMap<String, String>();
		for (entry : prefixToName.entrySet) {
			val prefix = entry.key;
			val name = entry.value.get(0);
			nameToPrefix.put(name, prefix);
		}
		for (c : namespace.children) {
			c.setName(nameToPrefix.get(c.name));
		}
		
		namespace.children.forEach[shortenChildrenNamespaceNames];
	}
	
	private def fillNames(SimplifyingNamespace namespace, String prevNsPart) {
		val nsPart = (prevNsPart !== null ? prevNsPart + "__" : "") +
						(namespace.name !== null ? namespace.name : "");
		for (fullIdParts : namespace.fullIds) {
			val prefixPart = fullIdParts.prefix !== null ? fullIdParts.prefix + "___" : "";
			val idPart = "__" + fullIdParts.id;
			fullIdsToNames.put(fullIdParts.fullId, prefixPart + nsPart + idPart);
		}
		
		namespace.children.forEach[c | fillNames(c, prevNsPart)];
	}
	
	
	private static class SimplifyingNamespace {
		String name;//short name
		List<SimplifyingNamespace> children = new ArrayList();
		List<FullIdParts> fullIds = new LinkedList();
		
		new (String name) {
			this.name = name;
		}
		
		def setName(String name) {
			this.name = name;
		}
		
		def addChildNamespace(String name) {
			val child = new SimplifyingNamespace(name);
			children.add(child);
			return child;
		}
		
		def addFullId(FullIdParts fullId) {
			fullIds.add(fullId);
		}
	}
}