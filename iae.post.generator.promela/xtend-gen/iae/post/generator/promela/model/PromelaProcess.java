package iae.post.generator.promela.model;

import iae.post.generator.promela.context.CurrentContext;
import iae.post.generator.promela.context.NamespaceContext;
import iae.post.generator.promela.context.PromelaContext;
import iae.post.generator.promela.model.vars.PromelaVar;
import iae.post.generator.promela.model.vars.PromelaVarsHelper;
import iae.post.poST.InputOutputVarDeclaration;
import iae.post.poST.InputVarDeclaration;
import iae.post.poST.OutputVarDeclaration;
import iae.post.poST.State;
import iae.post.poST.TimeoutStatement;
import iae.post.poST.VarDeclaration;
import java.util.function.Consumer;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class PromelaProcess implements IPromelaElement {
  private String programName;
  
  private String shortName;
  
  private String fullName;
  
  private String nameMType;
  
  private String nextMType;
  
  private String stateTypeMType;
  
  private String stateVarMType;
  
  private String stateStopMType;
  
  private String stateErrorMType;
  
  private PromelaVar.TimeInterval timeoutVar;
  
  private final PromelaElementList<PromelaVar> inVars = new PromelaElementList<PromelaVar>();
  
  private final PromelaElementList<PromelaVar> outVars = new PromelaElementList<PromelaVar>();
  
  private final PromelaElementList<PromelaVar> inOutVars = new PromelaElementList<PromelaVar>();
  
  private final PromelaElementList<PromelaVar> vars = new PromelaElementList<PromelaVar>();
  
  private final PromelaElementList<PromelaVar> constants = new PromelaElementList<PromelaVar>();
  
  private PromelaElementList<PromelaState> states = new PromelaElementList<PromelaState>();
  
  public PromelaProcess(final iae.post.poST.Process p) {
    CurrentContext.startProcess(this);
    this.programName = CurrentContext.getCurProgram().getShortName();
    this.shortName = p.getName();
    this.fullName = NamespaceContext.addId(p.getName(), "process");
    this.nameMType = NamespaceContext.addId(p.getName(), "p");
    this.nextMType = this.nameMType;
    this.stateTypeMType = NamespaceContext.addId(p.getName(), "S");
    this.stateVarMType = NamespaceContext.addId(p.getName(), "curS");
    final Function1<State, Boolean> _function = (State state) -> {
      TimeoutStatement _timeout = state.getTimeout();
      return Boolean.valueOf((_timeout != null));
    };
    State _findFirst = IterableExtensions.<State>findFirst(p.getStates(), _function);
    boolean _tripleNotEquals = (_findFirst != null);
    if (_tripleNotEquals) {
      final String timeoutVarFullName = NamespaceContext.addId(this.shortName, "timeout");
      PromelaVar.TimeInterval _timeInterval = new PromelaVar.TimeInterval(timeoutVarFullName);
      this.timeoutVar = _timeInterval;
    }
    NamespaceContext.startNamespace(p.getName());
    final Consumer<InputVarDeclaration> _function_1 = (InputVarDeclaration d) -> {
      this.inVars.addAll(PromelaVarsHelper.getVars(d.getVars()));
    };
    p.getProcInVars().forEach(_function_1);
    final Consumer<OutputVarDeclaration> _function_2 = (OutputVarDeclaration d) -> {
      this.outVars.addAll(PromelaVarsHelper.getVars(d.getVars()));
    };
    p.getProcOutVars().forEach(_function_2);
    final Consumer<InputOutputVarDeclaration> _function_3 = (InputOutputVarDeclaration d) -> {
      this.inOutVars.addAll(PromelaVarsHelper.getVars(d.getVars()));
    };
    p.getProcInOutVars().forEach(_function_3);
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
    p.getProcVars().forEach(_function_4);
    final Consumer<State> _function_5 = (State s) -> {
      String _addId = NamespaceContext.addId(s.getName(), "s");
      PromelaState _promelaState = new PromelaState(s, _addId, this.stateVarMType);
      this.states.add(_promelaState);
    };
    p.getStates().forEach(_function_5);
    this.stateStopMType = NamespaceContext.addId("Stop", "s");
    this.stateErrorMType = NamespaceContext.addId("Error", "s");
    PromelaContext.getContext().addProcessToMType(this);
    NamespaceContext.endNamespace();
    CurrentContext.stopProcess();
  }
  
  public String setNextMType(final String p) {
    return this.nextMType = p;
  }
  
  public String getProgramName() {
    return this.programName;
  }
  
  public String getShortName() {
    return this.shortName;
  }
  
  public String getFullName() {
    return this.fullName;
  }
  
  public String getNameMType() {
    return this.nameMType;
  }
  
  public String getStopStateMType() {
    return this.stateStopMType;
  }
  
  public String getErrorStateMtype() {
    return this.stateErrorMType;
  }
  
  public String getStateVarMType() {
    return this.stateVarMType;
  }
  
  public PromelaElementList<PromelaState> getStates() {
    return this.states;
  }
  
  public PromelaVar.TimeInterval getTimeoutVar() {
    return this.timeoutVar;
  }
  
  public CharSequence getStatesMTypeText(final boolean initiallyRunning) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("mtype:");
    String _name = NamespaceContext.getName(this.stateTypeMType);
    _builder.append(_name);
    _builder.append(" = {");
    _builder.newLineIfNotEmpty();
    {
      for(final PromelaState s : this.states) {
        _builder.append("\t");
        String _name_1 = NamespaceContext.getName(s.getStateMType());
        _builder.append(_name_1, "\t");
        _builder.append(",");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    String _name_2 = NamespaceContext.getName(this.stateStopMType);
    _builder.append(_name_2, "\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _name_3 = NamespaceContext.getName(this.stateErrorMType);
    _builder.append(_name_3, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.append("mtype:");
    String _name_4 = NamespaceContext.getName(this.stateTypeMType);
    _builder.append(_name_4);
    _builder.append(" ");
    String _name_5 = NamespaceContext.getName(this.stateVarMType);
    _builder.append(_name_5);
    _builder.append(" = ");
    String _xifexpression = null;
    if (initiallyRunning) {
      _xifexpression = this.states.get(0).getStateMType();
    } else {
      _xifexpression = this.stateStopMType;
    }
    String _name_6 = NamespaceContext.getName(_xifexpression);
    _builder.append(_name_6);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
  public String toText() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("//-----------------------------------------------------------------------------");
    _builder.newLine();
    _builder.append("//");
    String _name = NamespaceContext.getName(this.fullName);
    _builder.append(_name);
    _builder.newLineIfNotEmpty();
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
    _builder.append("active proctype ");
    String _name_1 = NamespaceContext.getName(this.fullName);
    _builder.append(_name_1);
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("do :: ");
    String _name_2 = NamespaceContext.getName("__currentProcess");
    _builder.append(_name_2, "\t");
    _builder.append(" ? ");
    String _name_3 = NamespaceContext.getName(this.nameMType);
    _builder.append(_name_3, "\t");
    _builder.append(" ->");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("atomic {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("if");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    String _text_5 = this.states.toText();
    _builder.append(_text_5, "\t\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t\t");
    _builder.append(":: else -> skip;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("fi;");
    _builder.newLine();
    _builder.append("\t\t\t");
    String _name_4 = NamespaceContext.getName("__currentProcess");
    _builder.append(_name_4, "\t\t\t");
    _builder.append(" ! ");
    String _name_5 = NamespaceContext.getName(this.nextMType);
    _builder.append(_name_5, "\t\t\t");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("od;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
