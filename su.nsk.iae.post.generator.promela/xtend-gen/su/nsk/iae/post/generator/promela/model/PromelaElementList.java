package su.nsk.iae.post.generator.promela.model;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class PromelaElementList<E extends IPromelaElement> extends ArrayList<E> implements IPromelaElement {
  private final String separator;
  
  public PromelaElementList(final String separator) {
    this.separator = separator;
  }
  
  public PromelaElementList() {
    this("");
  }
  
  public PromelaElementList<E> addElements(final Collection<E> elements) {
    this.addAll(elements);
    return this;
  }
  
  @Override
  public String toText() {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _hasElements = false;
      for(final E e : this) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(this.separator, "");
        }
        String _text = e.toText();
        _builder.append(_text);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
}
