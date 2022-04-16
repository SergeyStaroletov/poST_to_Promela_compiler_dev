package su.nsk.iae.post.generator.promela.context

import java.util.Map
import java.util.HashMap
import su.nsk.iae.post.generator.promela.exceptions.WrongModelStateException
import su.nsk.iae.post.generator.promela.context.NamespaceContext.Namespace
import java.util.List
import java.util.ArrayList
import java.util.LinkedList
import su.nsk.iae.post.generator.promela.context.NamespaceContext.FullIdParts
import java.util.Map.Entry
import java.util.AbstractMap.SimpleEntry

class FullIdsToNamesMapper {
	boolean numberMode;
	def isNumberMode() {
		return numberMode;
	}
	new (boolean numberMode) {
		this.numberMode = numberMode;
	}
	
	Map<String, FullIdParts> fullIdToParts = new HashMap();
	Map<String, String> fullIdsToNames = new HashMap();
	Map<String, String> fullIdsToNamesWithNumbers = new HashMap();
	SimplifyingNamespace rootNamespace = new SimplifyingNamespace(null);
	
	def processNamespace(Namespace namespace) {
		copyToSimplifyingNamespace(namespace, rootNamespace);
		shortenChildrenNamespaceNames(rootNamespace);
		fillFullIdParts(rootNamespace);
		val separator = isSingleSeparatorEnough(rootNamespace) ? "_" : "__";
		fillNames(rootNamespace, rootNamespace.name, separator);
		fillNamesWithNumbers();
		return;
	}
	
	def getName(String fullId) {
		var res = (numberMode ? fullIdsToNamesWithNumbers : fullIdsToNames).get(fullId);
		if (res === null) {
			throw new WrongModelStateException();
		}
		return res;
	}
	
	def getNameWithNamespaces(String fullId) {
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
		var conflictingPrefixes = new ArrayList<String>();
		for (c : namespace.children) {
			val prefix = c.name.substring(0, 1);
			val names = prefixToName.computeIfAbsent(prefix, [new ArrayList()]);
			names.add(c.name);
			if (names.size == 2) {
				conflictingPrefixes.add(prefix);
			}
		}
		while (!conflictingPrefixes.isEmpty()) {
			val newConflictingPrefixes = new ArrayList<String>();
			for (conflictingPrefix : conflictingPrefixes) {
				val prefixLength = conflictingPrefix.length + 1;
				val conflictingNames = prefixToName.remove(conflictingPrefix);
				for (name : conflictingNames) {
					val prefix = name.substring(0, Math.min(prefixLength, name.length));
					val names = prefixToName.computeIfAbsent(prefix, [new ArrayList()]);
					names.add(name);
					if (names.size == 2) {
						newConflictingPrefixes.add(prefix);
					}
				}
			}
			conflictingPrefixes = newConflictingPrefixes;
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
	
	private def boolean isSingleSeparatorEnough(SimplifyingNamespace namespace) {
		if (namespace.name != null && namespace.name.contains("_")) {
			return false;
		}
		for (c : namespace.children) {
			if (!isSingleSeparatorEnough(c)) {
				return false;
			}
		}
		return true;
	}
	
	private def fillFullIdParts(SimplifyingNamespace namespace) {
		namespace.fullIds.forEach[fullIdParts | fullIdToParts.put(fullIdParts.fullId, fullIdParts)];
		
		namespace.children.forEach[c | fillFullIdParts(c)];
	}
	
	private def fillNames(SimplifyingNamespace namespace, String prevNsPart, String separator) {
		val nsPart = (prevNsPart === null || prevNsPart.isEmpty() ? "" : prevNsPart + separator) +
						(namespace.name !== null ? namespace.name : "");
		for (fullIdParts : namespace.fullIds) {
			val prefix = translatePrefix(fullIdParts.prefix);
			val prefixPart = prefix !== null ?
								prefix + (!nsPart.isEmpty() ? separator + "_" : "")
								: "";
			val prefixAndNsPart = prefixPart + nsPart;
			val id = fullIdParts.id;
			val idPart = (prefixAndNsPart.isEmpty() ? "" : separator + "_") + id;
			fullIdsToNames.put(fullIdParts.fullId, prefixAndNsPart + idPart);
		}
		
		namespace.children.forEach[c | fillNames(c, nsPart, separator)];
	}
	
	private def fillNamesWithNumbers() {
		val prefixesShownInNumberMode = new ArrayList();
		prefixesShownInNumberMode.addAll("curS", "s", "timeout", "process", "sP", "p");
		val newNamesToFullIdIdAndPrefix = new HashMap<String, List<FullIdIdAndPrefix>>();
		for (entry : fullIdToParts.entrySet) {
			val fullId = entry.key;
			val id = entry.value.id;
			val originalPrefix = entry.value.prefix;
			val prefix = prefixesShownInNumberMode.contains(originalPrefix) ?
							translatePrefix(originalPrefix) : null;
			
			val newName = prefix === null ? id : id + "___" + prefix;
			newNamesToFullIdIdAndPrefix
				.computeIfAbsent(newName, [new ArrayList()])
				.add(new FullIdIdAndPrefix(fullId, id, prefix));
		}
		for (fullIdIdAndPrefixes : newNamesToFullIdIdAndPrefix.values) {
			val severalVarsWithThisNewName = fullIdIdAndPrefixes.size > 1;
			var counter = 0;
			for (fullIdIdAndPrefix : fullIdIdAndPrefixes) {
				val fullId = fullIdIdAndPrefix.fullId;
				val id = fullIdIdAndPrefix.id;
				val prefix = fullIdIdAndPrefix.prefix;
				val postfixPart = '''«prefix»«severalVarsWithThisNewName ? counter++ : ""»''';
				val newName = id + (!postfixPart.isBlank() ? "__" + postfixPart : "");
				fullIdsToNamesWithNumbers.put(fullId, newName);
			}
		}
	}
	
	private def String translatePrefix(String prefix) {
		switch (prefix) {
			case "curS": return "cs"
			case "timeout": return "t"
			case "sP": return "sp"
			default: return prefix
		}
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
	
	private static class FullIdIdAndPrefix {
		public String fullId;
		public String id;
		public String prefix;
		
		new (String fullId, String id, String prefix) {
			this.fullId = fullId;
			this.id = id;
			this.prefix = prefix;
		}
	}
	
}