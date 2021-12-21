package su.nsk.iae.post.generator.promela;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import su.nsk.iae.post.generator.IPoSTGenerator;
import su.nsk.iae.post.generator.promela.model.PromelaProgram;
import su.nsk.iae.post.poST.Model;
import su.nsk.iae.post.poST.Program;

@SuppressWarnings("all")
public class PromelaGenerator implements IPoSTGenerator {
  @Override
  public void setModel(final Model model) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("---------");
    _builder.newLine();
    _builder.newLine();
    Program _get = model.getPrograms().get(0);
    String _text = new PromelaProgram(_get).toText();
    _builder.append(_text);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("---------");
    _builder.newLine();
    System.out.println(_builder);
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
