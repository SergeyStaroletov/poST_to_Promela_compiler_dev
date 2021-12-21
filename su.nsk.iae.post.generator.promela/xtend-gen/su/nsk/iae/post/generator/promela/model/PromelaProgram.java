package su.nsk.iae.post.generator.promela.model;

import java.util.function.Consumer;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar;
import su.nsk.iae.post.generator.promela.model.vars.PromelaVarsHelper;
import su.nsk.iae.post.poST.InputOutputVarDeclaration;
import su.nsk.iae.post.poST.InputVarDeclaration;
import su.nsk.iae.post.poST.OutputVarDeclaration;
import su.nsk.iae.post.poST.Program;

@SuppressWarnings("all")
public class PromelaProgram implements IPromelaElement {
  private final String name;
  
  private final PromelaElementList<PromelaVar> inVars = new PromelaElementList<PromelaVar>();
  
  private final PromelaElementList<PromelaVar> outVars = new PromelaElementList<PromelaVar>();
  
  private final PromelaElementList<PromelaVar> inOutVars = new PromelaElementList<PromelaVar>();
  
  public PromelaProgram(final Program program) {
    this.name = program.getName();
    final Consumer<InputVarDeclaration> _function = (InputVarDeclaration d) -> {
      this.inVars.addAll(PromelaVarsHelper.getVars(d.getVars()));
    };
    program.getProgInVars().forEach(_function);
    final Consumer<OutputVarDeclaration> _function_1 = (OutputVarDeclaration d) -> {
      this.outVars.addAll(PromelaVarsHelper.getVars(d.getVars()));
    };
    program.getProgOutVars().forEach(_function_1);
    final Consumer<InputOutputVarDeclaration> _function_2 = (InputOutputVarDeclaration d) -> {
      this.inOutVars.addAll(PromelaVarsHelper.getVars(d.getVars()));
    };
    program.getProgInOutVars().forEach(_function_2);
  }
  
  @Override
  public String toText() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//program \"");
    _builder.append(this.name);
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      boolean _isEmpty = this.inVars.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append("//input variables");
        _builder.newLine();
        String _text = this.inVars.toText();
        _builder.append(_text);
        _builder.newLineIfNotEmpty();
        _builder.newLine();
      }
    }
    {
      boolean _isEmpty_1 = this.outVars.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      if (_not_1) {
        _builder.append("//output variables");
        _builder.newLine();
        String _text_1 = this.outVars.toText();
        _builder.append(_text_1);
        _builder.newLineIfNotEmpty();
        _builder.newLine();
      }
    }
    {
      boolean _isEmpty_2 = this.inOutVars.isEmpty();
      boolean _not_2 = (!_isEmpty_2);
      if (_not_2) {
        _builder.append("//inout variables");
        _builder.newLine();
        String _text_2 = this.inOutVars.toText();
        _builder.append(_text_2);
        _builder.newLineIfNotEmpty();
        _builder.newLine();
      }
    }
    return _builder.toString();
  }
}
