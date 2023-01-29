package iae.post.generator.promela.model;

import iae.post.generator.promela.context.CurrentContext;
import iae.post.generator.promela.context.NamespaceContext;
import iae.post.generator.promela.model.vars.PromelaVar;
import iae.post.generator.promela.model.vars.PromelaVarsHelper;
import iae.post.poST.GlobalVarDeclaration;
import iae.post.poST.InputOutputVarDeclaration;
import iae.post.poST.InputVarDeclaration;
import iae.post.poST.OutputVarDeclaration;
import iae.post.poST.Program;
import iae.post.poST.VarDeclaration;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;

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
  
  public PromelaProgram(final Program program, final EList<GlobalVarDeclaration> globVars) {
    CurrentContext.startProgram(this);
    this.shortName = program.getName();
    this.fullName = NamespaceContext.addId(program.getName());
    NamespaceContext.startNamespace(program.getName());
    final Consumer<GlobalVarDeclaration> _function = (GlobalVarDeclaration d) -> {
      this.inOutVars.addAll(PromelaVarsHelper.getVars(d.getVarsSimple()));
    };
    globVars.forEach(_function);
    final Consumer<InputVarDeclaration> _function_1 = (InputVarDeclaration d) -> {
      this.inVars.addAll(PromelaVarsHelper.getVars(d.getVars()));
    };
    program.getProgInVars().forEach(_function_1);
    final Consumer<OutputVarDeclaration> _function_2 = (OutputVarDeclaration d) -> {
      this.outVars.addAll(PromelaVarsHelper.getVars(d.getVars()));
    };
    program.getProgOutVars().forEach(_function_2);
    final Consumer<InputOutputVarDeclaration> _function_3 = (InputOutputVarDeclaration d) -> {
      this.inOutVars.addAll(PromelaVarsHelper.getVars(d.getVars()));
    };
    program.getProgInOutVars().forEach(_function_3);
    final Consumer<VarDeclaration> _function_4 = (VarDeclaration d) -> {
      final Consumer<PromelaVar> _function_5 = (PromelaVar v) -> {
        PromelaElementList<PromelaVar> _xifexpression = null;
        boolean _isConstant = v.isConstant();
        if (_isConstant) {
          _xifexpression = this.constants;
        } else {
          _xifexpression = this.vars;
        }
        _xifexpression.add(v);
      };
      PromelaVarsHelper.getVars(d.getVars(), d.isConst()).forEach(_function_5);
    };
    program.getProgVars().forEach(_function_4);
    final Consumer<iae.post.poST.Process> _function_5 = (iae.post.poST.Process p) -> {
      PromelaProcess _promelaProcess = new PromelaProcess(p);
      this.processes.add(_promelaProcess);
    };
    program.getProcesses().forEach(_function_5);
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
    String _name = NamespaceContext.getName(this.fullName);
    _builder.append(_name);
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
  
  public PromelaElementList<PromelaVar> getInVars() {
    return this.inVars;
  }
  
  public PromelaElementList<PromelaVar> getOutVars() {
    return this.outVars;
  }
  
  public PromelaElementList<PromelaVar> getInOutVars() {
    return this.inOutVars;
  }
}
