package su.nsk.iae.post.generator.promela.model;

import java.util.ArrayList;
import java.util.function.Consumer;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class PromelaElementList<E extends IPromelaElement> extends ArrayList<E> implements IPromelaElement {
  @Override
  public String toText() {
    final StringConcatenation stringConcatenation = new StringConcatenation();
    final Consumer<E> _function = (E e) -> {
      stringConcatenation.append(e.toText());
    };
    this.forEach(_function);
    return stringConcatenation.toString();
  }
}
