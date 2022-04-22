package su.nsk.iae.post.generator.promela;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import su.nsk.iae.post.generator.IPoSTGenerator;
import su.nsk.iae.post.poST.Model;

@SuppressWarnings("all")
public class PromelaGenerator implements IPoSTGenerator {
  @Override
  public void setModel(final Model model) {
    throw new Error("Unresolved compilation problems:"
      + "\nInvalid number of arguments. The constructor PromelaModel(Model, boolean) is not applicable for the arguments (Model)");
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
