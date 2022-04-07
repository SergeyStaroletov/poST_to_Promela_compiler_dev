package su.nsk.iae.post.generator.promela.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.Conversions;
import su.nsk.iae.post.generator.promela.model.WrongModelStateException;

@SuppressWarnings("all")
public class NamespaceContext {
  public static class Namespace {
    private NamespaceContext.Namespace parent;
    
    private List<NamespaceContext.Namespace> children = new ArrayList<NamespaceContext.Namespace>();
    
    private String name;
    
    private String fullName;
    
    private Map<String, String> fullIds = new HashMap<String, String>();
    
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
}
