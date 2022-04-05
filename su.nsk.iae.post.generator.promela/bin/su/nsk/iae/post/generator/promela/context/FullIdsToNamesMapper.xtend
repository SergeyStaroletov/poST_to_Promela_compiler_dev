package su.nsk.iae.post.generator.promela.context

import java.util.Map
import java.util.HashMap
import su.nsk.iae.post.generator.promela.model.WrongModelStateException
import su.nsk.iae.post.generator.promela.context.NamespaceContext.Namespace

class FullIdsToNamesMapper {
	Map<String, String> fullIdsToNames = new HashMap();
	
	def processNamespace(Namespace namespace) {
		namespace.childrenNamespaces.forEach[processNamespace];
		namespace.fullIds.forEach[id, fullId | {
			fullIdsToNames.put(fullId, fullId);
		}];
	}
	
	def getName(String fullId) {
		var res = fullIdsToNames.get(fullId);
		if (res === null) {
			throw new WrongModelStateException();
		}
		return res;
	}
}