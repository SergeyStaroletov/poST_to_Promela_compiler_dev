package su.nsk.iae.post.generator.promela.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import su.nsk.iae.post.generator.promela.model.WrongModelStateException;

@SuppressWarnings("all")
public class NamespaceContext {
  public static class Namespace {
    private NamespaceContext.Namespace parent;
    
    private List<NamespaceContext.Namespace> children = new ArrayList<NamespaceContext.Namespace>();
    
    private String name;
    
    private String fullName;
    
    private Map<String, String> fullIds = new HashMap<String, String>();
    
    private Map<String, NamespaceContext.FullIdParts> fullIdParts = new HashMap<String, NamespaceContext.FullIdParts>();
    
    public Namespace(final NamespaceContext.Namespace parent, final String name) {
      String _xifexpression = null;
      if (((parent != null) && (parent.fullName != null))) {
        _xifexpression = (parent.fullName + "__");
      } else {
        _xifexpression = "";
      }
      final String prefix = _xifexpression;
      this.name = name;
      String _xifexpression_1 = null;
      if ((name != null)) {
        _xifexpression_1 = (prefix + name);
      } else {
        _xifexpression_1 = null;
      }
      this.fullName = _xifexpression_1;
      this.parent = parent;
    }
    
    public Map<String, String> getFullIds() {
      return this.fullIds;
    }
    
    public Map<String, NamespaceContext.FullIdParts> getFullIdParts() {
      return this.fullIdParts;
    }
    
    public List<NamespaceContext.Namespace> getChildrenNamespaces() {
      return this.children;
    }
    
    public String getName() {
      return this.name;
    }
    
    public String getFullName() {
      return this.fullName;
    }
  }
  
  public static class FullIdParts {
    private String prefix;
    
    private List<String> namespaceNames;
    
    private String id;
    
    public FullIdParts(final String prefix, final List<String> namespaceNames, final String id) {
      this.prefix = prefix;
      this.namespaceNames = namespaceNames;
      this.id = id;
    }
    
    public String getPrefix() {
      return this.prefix;
    }
    
    public List<String> getNamespaceNames() {
      return this.namespaceNames;
    }
    
    public String getId() {
      return this.id;
    }
    
    public String getFullId() {
      String _xifexpression = null;
      if ((this.prefix != null)) {
        _xifexpression = (this.prefix + "__");
      } else {
        _xifexpression = "";
      }
      final String prefixPart = _xifexpression;
      final Function1<String, String> _function = (String n) -> {
        return (n + "__");
      };
      String _join = IterableExtensions.join(ListExtensions.<String, String>map(this.namespaceNames, _function));
      String _plus = (prefixPart + _join);
      return (_plus + this.id);
    }
  }
  
  private static NamespaceContext.Namespace rootNamespace = new NamespaceContext.Namespace(null, null);
  
  private static NamespaceContext.Namespace current = NamespaceContext.rootNamespace;
  
  private static FullIdsToNamesMapper namesMapper = new FullIdsToNamesMapper();
  
  public static void startNamespace(final String name) {
    if ((NamespaceContext.current == null)) {
      NamespaceContext.current = NamespaceContext.rootNamespace;
    }
    NamespaceContext.Namespace newNamespace = new NamespaceContext.Namespace(NamespaceContext.current, name);
    NamespaceContext.current.children.add(newNamespace);
    NamespaceContext.current = newNamespace;
  }
  
  public static void endNamespace() {
    NamespaceContext.current = NamespaceContext.current.parent;
    if ((NamespaceContext.current == null)) {
      throw new WrongModelStateException();
    }
  }
  
  public static String getCurrentNamespaceShortName() {
    final String[] nameParts = NamespaceContext.current.fullName.split("__");
    int _size = ((List<String>)Conversions.doWrapArray(nameParts)).size();
    int _minus = (_size - 1);
    return nameParts[_minus];
  }
  
  public static String addId(final String id, final String attribute) {
    String _xifexpression = null;
    if ((attribute != null)) {
      _xifexpression = (attribute + "__");
    } else {
      _xifexpression = "";
    }
    final String attributePrefix = _xifexpression;
    String _xifexpression_1 = null;
    if ((NamespaceContext.current.fullName != null)) {
      _xifexpression_1 = (NamespaceContext.current.fullName + "__");
    } else {
      _xifexpression_1 = "";
    }
    final String namespacePrefix = _xifexpression_1;
    final String fullId = ((attributePrefix + namespacePrefix) + id);
    NamespaceContext.current.fullIds.put((attributePrefix + id), fullId);
    List<String> _xifexpression_2 = null;
    if ((NamespaceContext.current.fullName == null)) {
      _xifexpression_2 = new ArrayList<String>();
    } else {
      _xifexpression_2 = (List<String>)Conversions.doWrapArray(NamespaceContext.current.fullName.split("__"));
    }
    NamespaceContext.FullIdParts _fullIdParts = new NamespaceContext.FullIdParts(attribute, _xifexpression_2, id);
    NamespaceContext.current.fullIdParts.put((attributePrefix + id), _fullIdParts);
    return fullId;
  }
  
  public static String addId(final String id) {
    return NamespaceContext.addId(id, null);
  }
  
  public static String getFullId(final String id, final String attribute) {
    String _xifexpression = null;
    if ((attribute != null)) {
      _xifexpression = (attribute + "__");
    } else {
      _xifexpression = "";
    }
    final String idWithAttribute = (_xifexpression + id);
    NamespaceContext.Namespace cur = NamespaceContext.current;
    String fullId = null;
    while (((fullId == null) && (cur != null))) {
      {
        fullId = cur.fullIds.get(idWithAttribute);
        cur = cur.parent;
      }
    }
    if ((fullId == null)) {
      throw new WrongModelStateException();
    }
    return fullId;
  }
  
  public static String getFullId(final String id) {
    return NamespaceContext.getFullId(id, null);
  }
  
  public static void prepareNamesMapping() {
    NamespaceContext.namesMapper.processNamespace(NamespaceContext.rootNamespace);
  }
  
  public static String getName(final String fullId) {
    return NamespaceContext.namesMapper.getName(fullId);
  }
  
  public static FullIdsToNamesMapper clearContext() {
    FullIdsToNamesMapper _xblockexpression = null;
    {
      NamespaceContext.Namespace _namespace = new NamespaceContext.Namespace(null, null);
      NamespaceContext.rootNamespace = _namespace;
      NamespaceContext.current = NamespaceContext.rootNamespace;
      FullIdsToNamesMapper _fullIdsToNamesMapper = new FullIdsToNamesMapper();
      _xblockexpression = NamespaceContext.namesMapper = _fullIdsToNamesMapper;
    }
    return _xblockexpression;
  }
}
