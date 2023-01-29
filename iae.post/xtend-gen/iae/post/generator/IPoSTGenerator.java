package iae.post.generator;

import iae.post.poST.Model;
import org.eclipse.xtext.generator.IGenerator2;

@SuppressWarnings("all")
public interface IPoSTGenerator extends IGenerator2 {
  void setModel(final Model model);
}
