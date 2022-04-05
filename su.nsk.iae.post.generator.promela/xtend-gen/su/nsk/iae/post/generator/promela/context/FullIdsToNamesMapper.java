package su.nsk.iae.post.generator.promela.context;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import su.nsk.iae.post.generator.promela.context.NamespaceContext;
import su.nsk.iae.post.generator.promela.model.WrongModelStateException;

@SuppressWarnings("all")
public class FullIdsToNamesMapper {
  private Map<String, String> fullIdsToNames = new HashMap<String, String>();
  
  public FullIdsToNamesMapper(final NamespaceContext.Namespace namespace) {
    this.processNamespace(namespace);
  }
  
  public String getName(final String fullId) {
    String res = this.fullIdsToNames.get(fullId);
    if ((res == null)) {
      throw new WrongModelStateException();
    }
    return res;
  }
  
  private void processNamespace(final NamespaceContext.Namespace namespace) {
    final Consumer<NamespaceContext.Namespace> _function = (NamespaceContext.Namespace it) -> {
      this.processNamespace(it);
    };
    namespace.getChildrenNamespaces().forEach(_function);
    final BiConsumer<String, String> _function_1 = (String id, String fullId) -> {
      this.fullIdsToNames.put(fullId, (fullId + "_____"));
    };
    namespace.getFullIds().forEach(_function_1);
  }
}
