package su.nsk.iae.post.generator.promela.context;

import com.google.common.base.Objects;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import su.nsk.iae.post.generator.promela.context.NamespaceContext;
import su.nsk.iae.post.generator.promela.model.WrongModelStateException;

@SuppressWarnings("all")
public class FullIdsToNamesMapper {
  private static class SimplifyingNamespace {
    private String name;
    
    private List<FullIdsToNamesMapper.SimplifyingNamespace> children = new ArrayList<FullIdsToNamesMapper.SimplifyingNamespace>();
    
    private List<String> fullIds = new ArrayList<String>();
    
    public SimplifyingNamespace(final String name) {
      this.name = name;
    }
    
    public FullIdsToNamesMapper.SimplifyingNamespace addChildNamespace(final String name) {
      final FullIdsToNamesMapper.SimplifyingNamespace child = new FullIdsToNamesMapper.SimplifyingNamespace(name);
      this.children.add(child);
      return child;
    }
    
    public boolean addFullId(final String fullId) {
      return this.fullIds.add(fullId);
    }
  }
  
  private Map<String, String> fullIdsToNames = new HashMap<String, String>();
  
  private FullIdsToNamesMapper.SimplifyingNamespace rootNamespace = new FullIdsToNamesMapper.SimplifyingNamespace(null);
  
  public void processNamespace(final NamespaceContext.Namespace namespace) {
    this.copyToSimplifyingNamespace(namespace, this.rootNamespace);
    this.moveFromChildrenNamespaces(this.rootNamespace);
    return;
  }
  
  public String getName(final String fullId) {
    String res = this.fullIdsToNames.get(fullId);
    if ((res == null)) {
      throw new WrongModelStateException();
    }
    return res;
  }
  
  private void copyToSimplifyingNamespace(final NamespaceContext.Namespace namespace, final FullIdsToNamesMapper.SimplifyingNamespace simplifyingNamespace) {
    final Consumer<NamespaceContext.Namespace> _function = (NamespaceContext.Namespace ns) -> {
      this.copyToSimplifyingNamespace(ns, 
        simplifyingNamespace.addChildNamespace(ns.getName()));
    };
    namespace.getChildrenNamespaces().forEach(_function);
    final BiConsumer<String, String> _function_1 = (String id, String fullId) -> {
      simplifyingNamespace.addFullId(fullId);
      this.fullIdsToNames.put(fullId, fullId);
    };
    namespace.getFullIds().forEach(_function_1);
  }
  
  private void moveFromChildrenNamespaces(final FullIdsToNamesMapper.SimplifyingNamespace namespace) {
    final Consumer<FullIdsToNamesMapper.SimplifyingNamespace> _function = (FullIdsToNamesMapper.SimplifyingNamespace ns) -> {
      this.moveFromChildrenNamespaces(ns);
    };
    namespace.children.forEach(_function);
    final HashMap<String, List<Map.Entry<FullIdsToNamesMapper.SimplifyingNamespace, String>>> idsNamespaces = new HashMap<String, List<Map.Entry<FullIdsToNamesMapper.SimplifyingNamespace, String>>>();
    final Consumer<String> _function_1 = (String id) -> {
      final Function<String, List<Map.Entry<FullIdsToNamesMapper.SimplifyingNamespace, String>>> _function_2 = (String it) -> {
        return new ArrayList<Map.Entry<FullIdsToNamesMapper.SimplifyingNamespace, String>>();
      };
      List<Map.Entry<FullIdsToNamesMapper.SimplifyingNamespace, String>> _computeIfAbsent = idsNamespaces.computeIfAbsent(id, _function_2);
      AbstractMap.SimpleEntry<FullIdsToNamesMapper.SimplifyingNamespace, String> _simpleEntry = new AbstractMap.SimpleEntry<FullIdsToNamesMapper.SimplifyingNamespace, String>(namespace, id);
      _computeIfAbsent.add(_simpleEntry);
    };
    namespace.fullIds.forEach(_function_1);
    final Consumer<FullIdsToNamesMapper.SimplifyingNamespace> _function_2 = (FullIdsToNamesMapper.SimplifyingNamespace ns) -> {
      final Consumer<String> _function_3 = (String fullId) -> {
        final Function<String, List<Map.Entry<FullIdsToNamesMapper.SimplifyingNamespace, String>>> _function_4 = (String it) -> {
          return new ArrayList<Map.Entry<FullIdsToNamesMapper.SimplifyingNamespace, String>>();
        };
        List<Map.Entry<FullIdsToNamesMapper.SimplifyingNamespace, String>> _computeIfAbsent = idsNamespaces.computeIfAbsent(this.deleteLastNamespacePart(fullId), _function_4);
        AbstractMap.SimpleEntry<FullIdsToNamesMapper.SimplifyingNamespace, String> _simpleEntry = new AbstractMap.SimpleEntry<FullIdsToNamesMapper.SimplifyingNamespace, String>(ns, fullId);
        _computeIfAbsent.add(_simpleEntry);
      };
      ns.fullIds.forEach(_function_3);
    };
    namespace.children.forEach(_function_2);
    final BiConsumer<String, List<Map.Entry<FullIdsToNamesMapper.SimplifyingNamespace, String>>> _function_3 = (String id, List<Map.Entry<FullIdsToNamesMapper.SimplifyingNamespace, String>> listNsAndFullId) -> {
      if (((listNsAndFullId.size() == 1) || (!Objects.equal(listNsAndFullId.get(0).getKey(), namespace)))) {
        final FullIdsToNamesMapper.SimplifyingNamespace ns = listNsAndFullId.get(0).getKey();
        final String fullId = listNsAndFullId.get(0).getValue();
        ns.fullIds.remove(fullId);
        namespace.fullIds.add(id);
      }
    };
    idsNamespaces.forEach(_function_3);
  }
  
  private String deleteLastNamespacePart(final String fullId) {
    List<String> _list = IterableExtensions.<String>toList(((Iterable<String>)Conversions.doWrapArray(fullId.split("__"))));
    final ArrayList<String> fullIdParts = new ArrayList<String>(_list);
    int _length = ((Object[])Conversions.unwrapArray(fullIdParts, Object.class)).length;
    int _minus = (_length - 2);
    return String.join(fullIdParts.remove(_minus), "__");
  }
}
