package su.nsk.iae.post.generator.promela;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import su.nsk.iae.post.generator.IPoSTGenerator;
import su.nsk.iae.post.generator.promela.model.PromelaModel;
import su.nsk.iae.post.poST.Model;

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
