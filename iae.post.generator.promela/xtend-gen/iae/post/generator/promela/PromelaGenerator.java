package iae.post.generator.promela;

import iae.post.generator.IPoSTGenerator;
import iae.post.generator.promela.model.PromelaModel;
import iae.post.poST.Model;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;

@SuppressWarnings("all")
public class PromelaGenerator implements IPoSTGenerator {
  @Override
  public void setModel(final Model model) {
    System.out.println(new PromelaModel(model, false, false).toText());
  }
  
  @Override
  public void beforeGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
  }
  
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
  }
  
  @Override
  public void afterGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
  }
}
