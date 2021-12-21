package su.nsk.iae.post.generator.promela.model.vars;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.promela.model.IPromelaElement;

@SuppressWarnings("all")
public abstract class PromelaVar implements IPromelaElement {
  private String name;
  
  private String typeName;
  
  public PromelaVar(final String name, final String typeName) {
    this.name = name;
    this.typeName = typeName;
  }
  
  @Override
  public String toText() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.typeName);
    _builder.append(" ");
    _builder.append(this.name);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
}
