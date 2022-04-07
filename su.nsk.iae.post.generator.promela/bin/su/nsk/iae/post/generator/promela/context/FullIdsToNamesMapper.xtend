package su.nsk.iae.post.generator.promela.context

import java.util.Map
import java.util.HashMap
import su.nsk.iae.post.generator.promela.model.WrongModelStateException
import su.nsk.iae.post.generator.promela.context.NamespaceContext.Namespace
import java.util.List
import java.util.ArrayList
import java.util.AbstractMap
import java.util.LinkedList

class FullIdsToNamesMapper {
	Map<String, String> fullIdsToNames = new HashMap();
	SimplifyingNamespace rootNamespace = new SimplifyingNamespace(null);
	
	def processNamespace(Namespace namespace) {
		copyToSimplifyingNamespace(namespace, rootNamespace);
//		moveFromChildrenNamespaces(rootNamespace);
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
			val fullId = fullIdParts.fullId;
			simplifyingNamespace.addFullId(fullId);
			fullIdsToNames.put(fullId, fullId);
		];
	}
	
	private def moveFromChildrenNamespaces(SimplifyingNamespace namespace) {
		namespace.children.forEach[ns | moveFromChildrenNamespaces(ns)];
		
		//newId -> List<Entry<ns, oldId>>
		val idsNamespaces = new HashMap<String, List<Map.Entry<SimplifyingNamespace, String>>>();
		namespace.fullIds.forEach[id | {
			idsNamespaces
				.computeIfAbsent(id, [new ArrayList()])
				.add(new AbstractMap.SimpleEntry(namespace, id));
		}];
		namespace.children.forEach[ns | ns.fullIds.forEach[fullId |
			idsNamespaces
				.computeIfAbsent(deleteLastNamespacePart(fullId/*, ns.name*/), [new ArrayList()])
				.add(new AbstractMap.SimpleEntry(ns, fullId));
		]];
		
		idsNamespaces.forEach[id, listNsAndFullId | 
			if (listNsAndFullId.size == 1 || listNsAndFullId.get(0).key != namespace) {
				val ns = listNsAndFullId.get(0).key;
				val fullId = listNsAndFullId.get(0).value;
				ns.fullIds.remove(fullId);
				namespace.fullIds.add(id);
			}
		];
	}
	
	private def deleteLastNamespacePart(String fullId/*, String namespaceName*/) {
		val fullIdParts = new ArrayList(fullId.split("__").toList());
//		val newIdParts = fullIdParts.subList(0, fullIdParts.length - 2);
//		newIdParts.add(fullIdParts.get(fullIdParts.length - 1));
//		return String.join("__", newIdParts);
		return fullIdParts
					.remove(fullIdParts.length - 2/*fullIdParts.lastIndexOf(namespaceName)*/)
					.join("__");
	}
	
	
	private static class SimplifyingNamespace {
		String name;//short name
		List<SimplifyingNamespace> children = new ArrayList();
		List<String> fullIds = new LinkedList();
		
		new (String name) {
			this.name = name;
		}
		
		def addChildNamespace(String name) {
			val child = new SimplifyingNamespace(name);
			children.add(child);
			return child;
		}
		
		def addFullId(String fullId) {
			fullIds.add(fullId);
		}
	}
}