package su.nsk.iae.post.generator.promela.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import su.nsk.iae.post.generator.promela.context.NamespaceContext;
import su.nsk.iae.post.generator.promela.model.WrongModelStateException;

@SuppressWarnings("all")
public class FullIdsToNamesMapper {
  private static class SimplifyingNamespace {
    private String name;
    
    private List<FullIdsToNamesMapper.SimplifyingNamespace> children = new ArrayList<FullIdsToNamesMapper.SimplifyingNamespace>();
    
    private List<NamespaceContext.FullIdParts> fullIds = new LinkedList<NamespaceContext.FullIdParts>();
    
    public SimplifyingNamespace(final String name) {
      this.name = name;
    }
    
    public String setName(final String name) {
      return this.name = name;
    }
    
    public FullIdsToNamesMapper.SimplifyingNamespace addChildNamespace(final String name) {
      final FullIdsToNamesMapper.SimplifyingNamespace child = new FullIdsToNamesMapper.SimplifyingNamespace(name);
      this.children.add(child);
      return child;
    }
    
    public boolean addFullId(final NamespaceContext.FullIdParts fullId) {
      return this.fullIds.add(fullId);
    }
  }
  
  private Map<String, String> fullIdsToNames = new HashMap<String, String>();
  
  private FullIdsToNamesMapper.SimplifyingNamespace rootNamespace = new FullIdsToNamesMapper.SimplifyingNamespace(null);
  
  public void processNamespace(final NamespaceContext.Namespace namespace) {
    this.copyToSimplifyingNamespace(namespace, this.rootNamespace);
    this.shortenChildrenNamespaceNames(this.rootNamespace);
    this.fillNames(this.rootNamespace, this.rootNamespace.name);
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
    final BiConsumer<String, NamespaceContext.FullIdParts> _function_1 = (String id, NamespaceContext.FullIdParts fullIdParts) -> {
      simplifyingNamespace.addFullId(fullIdParts);
    };
    namespace.getFullIdParts().forEach(_function_1);
  }
  
  private void shortenChildrenNamespaceNames(final FullIdsToNamesMapper.SimplifyingNamespace namespace) {
    final HashMap<String, List<String>> prefixToName = new HashMap<String, List<String>>();
    final ArrayList<String> conflictingPrefixes = new ArrayList<String>();
    for (final FullIdsToNamesMapper.SimplifyingNamespace c : namespace.children) {
      {
        final String prefix = c.name.substring(0, 1);
        final Function<String, List<String>> _function = (String it) -> {
          return new ArrayList<String>();
        };
        final List<String> names = prefixToName.computeIfAbsent(prefix, _function);
        names.add(c.name);
        int _size = names.size();
        boolean _equals = (_size == 2);
        if (_equals) {
          conflictingPrefixes.add(prefix);
        }
      }
    }
    while ((!conflictingPrefixes.isEmpty())) {
      for (final String conflictingPrefix : conflictingPrefixes) {
        {
          int _length = conflictingPrefix.length();
          final int prefixLength = (_length + 1);
          final List<String> conflictingNames = prefixToName.remove(conflictingPrefix);
          for (final String name : conflictingNames) {
            {
              final String prefix = name.substring(0, prefixLength);
              final Function<String, List<String>> _function = (String it) -> {
                return new ArrayList<String>();
              };
              final List<String> names = prefixToName.computeIfAbsent(prefix, _function);
              names.add(name);
              int _size = names.size();
              boolean _equals = (_size == 2);
              if (_equals) {
                conflictingPrefixes.add(prefix);
              }
            }
          }
        }
      }
    }
    final HashMap<String, String> nameToPrefix = new HashMap<String, String>();
    Set<Map.Entry<String, List<String>>> _entrySet = prefixToName.entrySet();
    for (final Map.Entry<String, List<String>> entry : _entrySet) {
      {
        final String prefix = entry.getKey();
        final String name = entry.getValue().get(0);
        nameToPrefix.put(name, prefix);
      }
    }
    for (final FullIdsToNamesMapper.SimplifyingNamespace c_1 : namespace.children) {
      c_1.setName(nameToPrefix.get(c_1.name));
    }
    final Consumer<FullIdsToNamesMapper.SimplifyingNamespace> _function = (FullIdsToNamesMapper.SimplifyingNamespace it) -> {
      this.shortenChildrenNamespaceNames(it);
    };
    namespace.children.forEach(_function);
  }
  
  private void fillNames(final FullIdsToNamesMapper.SimplifyingNamespace namespace, final String prevNsPart) {
    String _xifexpression = null;
    if ((prevNsPart != null)) {
      _xifexpression = (prevNsPart + "__");
    } else {
      _xifexpression = "";
    }
    String _xifexpression_1 = null;
    if ((namespace.name != null)) {
      _xifexpression_1 = namespace.name;
    } else {
      _xifexpression_1 = "";
    }
    final String nsPart = (_xifexpression + _xifexpression_1);
    for (final NamespaceContext.FullIdParts fullIdParts : namespace.fullIds) {
      {
        String _xifexpression_2 = null;
        String _prefix = fullIdParts.getPrefix();
        boolean _tripleNotEquals = (_prefix != null);
        if (_tripleNotEquals) {
          String _prefix_1 = fullIdParts.getPrefix();
          _xifexpression_2 = (_prefix_1 + "___");
        } else {
          _xifexpression_2 = "";
        }
        final String prefixPart = _xifexpression_2;
        String _id = fullIdParts.getId();
        final String idPart = ("__" + _id);
        this.fullIdsToNames.put(fullIdParts.getFullId(), ((prefixPart + nsPart) + idPart));
      }
    }
    final Consumer<FullIdsToNamesMapper.SimplifyingNamespace> _function = (FullIdsToNamesMapper.SimplifyingNamespace c) -> {
      this.fillNames(c, prevNsPart);
    };
    namespace.children.forEach(_function);
  }
}
