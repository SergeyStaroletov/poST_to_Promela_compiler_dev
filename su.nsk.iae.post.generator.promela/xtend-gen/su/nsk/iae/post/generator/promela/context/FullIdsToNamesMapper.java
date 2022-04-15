package su.nsk.iae.post.generator.promela.context;

import com.google.common.base.Objects;
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
import su.nsk.iae.post.generator.promela.exceptions.WrongModelStateException;

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
    String _xifexpression = null;
    boolean _isSingleSeparatorEnough = this.isSingleSeparatorEnough(this.rootNamespace);
    if (_isSingleSeparatorEnough) {
      _xifexpression = "_";
    } else {
      _xifexpression = "__";
    }
    final String separator = _xifexpression;
    this.fillNames(this.rootNamespace, this.rootNamespace.name, separator);
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
    ArrayList<String> conflictingPrefixes = new ArrayList<String>();
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
      {
        final ArrayList<String> newConflictingPrefixes = new ArrayList<String>();
        for (final String conflictingPrefix : conflictingPrefixes) {
          {
            int _length = conflictingPrefix.length();
            final int prefixLength = (_length + 1);
            final List<String> conflictingNames = prefixToName.remove(conflictingPrefix);
            for (final String name : conflictingNames) {
              {
                final String prefix = name.substring(0, Math.min(prefixLength, name.length()));
                final Function<String, List<String>> _function = (String it) -> {
                  return new ArrayList<String>();
                };
                final List<String> names = prefixToName.computeIfAbsent(prefix, _function);
                names.add(name);
                int _size = names.size();
                boolean _equals = (_size == 2);
                if (_equals) {
                  newConflictingPrefixes.add(prefix);
                }
              }
            }
          }
        }
        conflictingPrefixes = newConflictingPrefixes;
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
  
  private boolean isSingleSeparatorEnough(final FullIdsToNamesMapper.SimplifyingNamespace namespace) {
    if (((!Objects.equal(namespace.name, null)) && namespace.name.contains("_"))) {
      return false;
    }
    for (final FullIdsToNamesMapper.SimplifyingNamespace c : namespace.children) {
      boolean _isSingleSeparatorEnough = this.isSingleSeparatorEnough(c);
      boolean _not = (!_isSingleSeparatorEnough);
      if (_not) {
        return false;
      }
    }
    return true;
  }
  
  private void fillNames(final FullIdsToNamesMapper.SimplifyingNamespace namespace, final String prevNsPart, final String separator) {
    String _xifexpression = null;
    if (((prevNsPart == null) || prevNsPart.isEmpty())) {
      _xifexpression = "";
    } else {
      _xifexpression = (prevNsPart + separator);
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
        final String prefix = this.translatePrefix(fullIdParts.getPrefix());
        String _xifexpression_2 = null;
        if ((prefix != null)) {
          String _xifexpression_3 = null;
          boolean _isEmpty = nsPart.isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            _xifexpression_3 = (separator + "_");
          } else {
            _xifexpression_3 = "";
          }
          _xifexpression_2 = (prefix + _xifexpression_3);
        } else {
          _xifexpression_2 = "";
        }
        final String prefixPart = _xifexpression_2;
        final String prefixAndNsPart = (prefixPart + nsPart);
        final String id = fullIdParts.getId();
        String _xifexpression_4 = null;
        boolean _isEmpty_1 = prefixAndNsPart.isEmpty();
        if (_isEmpty_1) {
          _xifexpression_4 = "";
        } else {
          _xifexpression_4 = (separator + "_");
        }
        final String idPart = (_xifexpression_4 + id);
        this.fullIdsToNames.put(fullIdParts.getFullId(), (prefixAndNsPart + idPart));
      }
    }
    final Consumer<FullIdsToNamesMapper.SimplifyingNamespace> _function = (FullIdsToNamesMapper.SimplifyingNamespace c) -> {
      this.fillNames(c, nsPart, separator);
    };
    namespace.children.forEach(_function);
  }
  
  private String translatePrefix(final String prefix) {
    if (prefix != null) {
      switch (prefix) {
        case "curS":
          return "cs";
        case "timeout":
          return "t";
        case "sP":
          return "sp";
        default:
          return prefix;
      }
    } else {
      return prefix;
    }
  }
}
