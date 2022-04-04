package su.nsk.iae.post.generator.promela.model;

import java.util.function.Consumer;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.promela.context.CurrentContext;
import su.nsk.iae.post.generator.promela.context.NamespaceContext;
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar;
import su.nsk.iae.post.generator.promela.model.vars.PromelaVarsHelper;
import su.nsk.iae.post.poST.InputOutputVarDeclaration;
import su.nsk.iae.post.poST.InputVarDeclaration;
import su.nsk.iae.post.poST.OutputVarDeclaration;
import su.nsk.iae.post.poST.Program;
import su.nsk.iae.post.poST.VarDeclaration;

@SuppressWarnings("all")
public class PromelaProgram implements IPromelaElement {
  private final String shortName;
  
  private final String fullName;
  
  private final PromelaElementList<PromelaVar> inVars = new PromelaElementList<PromelaVar>();
  
  private final PromelaElementList<PromelaVar> outVars = new PromelaElementList<PromelaVar>();
  
  private final PromelaElementList<PromelaVar> inOutVars = new PromelaElementList<PromelaVar>();
  
  private final PromelaElementList<PromelaVar> vars = new PromelaElementList<PromelaVar>();
  
  private final PromelaElementList<PromelaVar> constants = new PromelaElementList<PromelaVar>();
  
  private final PromelaElementList<PromelaProcess> processes = new PromelaElementList<PromelaProcess>("\r\n");
  
  public PromelaProgram(final Program program) {
    CurrentContext.startProgram(this);
    this.shortName = program.getName();
    this.fullName = NamespaceContext.addId(program.getName());
    NamespaceContext.startNamespace(program.getName());
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
    final Consumer<VarDeclaration> _function_3 = (VarDeclaration d) -> {
      final Consumer<PromelaVar> _function_4 = (PromelaVar v) -> {
        PromelaElementList<PromelaVar> _xifexpression = null;
        boolean _isConstant = v.isConstant();
        if (_isConstant) {
          _xifexpression = this.constants;
        } else {
          _xifexpression = this.vars;
        }
        _xifexpression.add(v);
      };
      PromelaVarsHelper.getVars(d.getVars(), d.isConst()).forEach(_function_4);
    };
    program.getProgVars().forEach(_function_3);
    final Consumer<su.nsk.iae.post.poST.Process> _function_4 = (su.nsk.iae.post.poST.Process p) -> {
      PromelaProcess _promelaProcess = new PromelaProcess(p);
      this.processes.add(_promelaProcess);
    };
    program.getProcesses().forEach(_function_4);
    NamespaceContext.endNamespace();
    CurrentContext.stopProgram();
  }
  
  @Override
  public String toText() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//-----------------------------------------------------------------------------");
    _builder.newLine();
    _builder.append("//-----------------------------------------------------------------------------");
    _builder.newLine();
    _builder.append("//program ");
    _builder.append(this.fullName);
    _builder.newLineIfNotEmpty();
    _builder.append("//-----------------------------------------------------------------------------");
    _builder.newLine();
    _builder.append("//-----------------------------------------------------------------------------");
    _builder.newLine();
    _builder.newLine();
    {
      boolean _isEmpty = this.constants.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append("//constants");
        _builder.newLine();
        String _text = this.constants.toText();
        _builder.append(_text);
        _builder.newLineIfNotEmpty();
        _builder.newLine();
      }
    }
    {
      boolean _isEmpty_1 = this.inVars.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      if (_not_1) {
        _builder.append("//input");
        _builder.newLine();
        String _text_1 = this.inVars.toText();
        _builder.append(_text_1);
        _builder.newLineIfNotEmpty();
        _builder.newLine();
      }
    }
    {
      boolean _isEmpty_2 = this.outVars.isEmpty();
      boolean _not_2 = (!_isEmpty_2);
      if (_not_2) {
        _builder.append("//output");
        _builder.newLine();
        String _text_2 = this.outVars.toText();
        _builder.append(_text_2);
        _builder.newLineIfNotEmpty();
        _builder.newLine();
      }
    }
    {
      boolean _isEmpty_3 = this.inOutVars.isEmpty();
      boolean _not_3 = (!_isEmpty_3);
      if (_not_3) {
        _builder.append("//inout");
        _builder.newLine();
        String _text_3 = this.inOutVars.toText();
        _builder.append(_text_3);
        _builder.newLineIfNotEmpty();
        _builder.newLine();
      }
    }
    {
      boolean _isEmpty_4 = this.vars.isEmpty();
      boolean _not_4 = (!_isEmpty_4);
      if (_not_4) {
        _builder.append("//vars");
        _builder.newLine();
        String _text_4 = this.vars.toText();
        _builder.append(_text_4);
        _builder.newLineIfNotEmpty();
        _builder.newLine();
      }
    }
    String _text_5 = this.processes.toText();
    _builder.append(_text_5);
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String getShortName() {
    return this.shortName;
  }
}
