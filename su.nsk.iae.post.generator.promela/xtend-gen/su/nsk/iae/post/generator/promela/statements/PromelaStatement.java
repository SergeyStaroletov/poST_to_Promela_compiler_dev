package su.nsk.iae.post.generator.promela.statements;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import su.nsk.iae.post.generator.promela.PromelaContext;
import su.nsk.iae.post.generator.promela.context.CurrentContext;
import su.nsk.iae.post.generator.promela.context.NamespaceContext;
import su.nsk.iae.post.generator.promela.context.PostConstructContext;
import su.nsk.iae.post.generator.promela.expressions.PromelaExpression;
import su.nsk.iae.post.generator.promela.expressions.PromelaExpressionsHelper;
import su.nsk.iae.post.generator.promela.model.IPromelaElement;
import su.nsk.iae.post.generator.promela.model.PromelaElementList;
import su.nsk.iae.post.generator.promela.model.PromelaProcess;
import su.nsk.iae.post.generator.promela.model.PromelaState;
import su.nsk.iae.post.generator.promela.model.WrongModelStateException;
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar;
import su.nsk.iae.post.poST.AssignmentStatement;
import su.nsk.iae.post.poST.CaseElement;
import su.nsk.iae.post.poST.CaseStatement;
import su.nsk.iae.post.poST.Constant;
import su.nsk.iae.post.poST.ErrorProcessStatement;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.IfStatement;
import su.nsk.iae.post.poST.SetStateStatement;
import su.nsk.iae.post.poST.SignedInteger;
import su.nsk.iae.post.poST.StartProcessStatement;
import su.nsk.iae.post.poST.StatementList;
import su.nsk.iae.post.poST.StopProcessStatement;
import su.nsk.iae.post.poST.TimeoutStatement;
import su.nsk.iae.post.poST.Variable;

@SuppressWarnings("all")
public abstract class PromelaStatement implements IPromelaElement, PostConstructContext.IPostConstuctible {
  public static class Assign extends PromelaStatement {
    private final String fullVarName;
    
    private final PromelaExpression value;
    
    public Assign(final AssignmentStatement s) {
      this.fullVarName = NamespaceContext.getFullId(s.getVariable().getName());
      this.value = PromelaExpressionsHelper.getExpr(s.getValue());
    }
    
    @Override
    public String toText() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(this.fullVarName);
      _builder.append(" = ");
      String _text = this.value.toText();
      _builder.append(_text);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    }
  }
  
  public static class If extends PromelaStatement {
    private PromelaExpression mainCond;
    
    private PromelaElementList<? extends PromelaStatement> mainCase;
    
    private List<Map.Entry<PromelaExpression, PromelaElementList<? extends PromelaStatement>>> elseIfCases = new ArrayList<Map.Entry<PromelaExpression, PromelaElementList<? extends PromelaStatement>>>();
    
    private PromelaElementList<? extends PromelaStatement> elseCase;
    
    public If(final IfStatement s) {
      this.mainCond = PromelaExpressionsHelper.getExpr(s.getMainCond());
      this.mainCase = new PromelaElementList<PromelaStatement>().addElements(PromelaStatementsHelper.getStatementList(s.getMainStatement()));
      final Procedure2<Expression, Integer> _function = (Expression cond, Integer i) -> {
        PromelaExpression _expr = PromelaExpressionsHelper.getExpr(cond);
        PromelaElementList<PromelaStatement> _addElements = new PromelaElementList<PromelaStatement>().addElements(PromelaStatementsHelper.getStatementList(s.getElseIfStatements().get((i).intValue())));
        AbstractMap.SimpleEntry<PromelaExpression, PromelaElementList<? extends PromelaStatement>> _simpleEntry = new AbstractMap.SimpleEntry<PromelaExpression, PromelaElementList<? extends PromelaStatement>>(_expr, _addElements);
        this.elseIfCases.add(_simpleEntry);
      };
      IterableExtensions.<Expression>forEach(s.getElseIfCond(), _function);
      PromelaElementList<PromelaStatement> _xifexpression = null;
      StatementList _elseStatement = s.getElseStatement();
      boolean _tripleNotEquals = (_elseStatement != null);
      if (_tripleNotEquals) {
        _xifexpression = new PromelaElementList<PromelaStatement>().addElements(PromelaStatementsHelper.getStatementList(s.getElseStatement()));
      } else {
        _xifexpression = null;
      }
      this.elseCase = _xifexpression;
    }
    
    @Override
    public String toText() {
      String _xifexpression = null;
      if ((this.elseIfCases.isEmpty() && (this.elseCase == null))) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("if :: ");
        String _text = this.mainCond.toText();
        _builder.append(_text);
        _builder.append(" -> {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _text_1 = this.mainCase.toText();
        _builder.append(_text_1, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("} :: else -> skip; fi;");
        _builder.newLine();
        _xifexpression = _builder.toString();
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("if");
        _builder_1.newLine();
        _builder_1.append("\t");
        _builder_1.append(":: ");
        String _text_2 = this.mainCond.toText();
        _builder_1.append(_text_2, "\t");
        _builder_1.append(" -> {");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("\t\t");
        String _text_3 = this.mainCase.toText();
        _builder_1.append(_text_3, "\t\t");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("\t");
        _builder_1.append("}");
        _builder_1.newLine();
        {
          for(final Map.Entry<PromelaExpression, PromelaElementList<? extends PromelaStatement>> c : this.elseIfCases) {
            _builder_1.append("\t");
            _builder_1.append(":: else -> if :: ");
            String _text_4 = c.getKey().toText();
            _builder_1.append(_text_4, "\t");
            _builder_1.append(" -> {");
            _builder_1.newLineIfNotEmpty();
            _builder_1.append("\t");
            _builder_1.append("\t");
            String _text_5 = c.getValue().toText();
            _builder_1.append(_text_5, "\t\t");
            _builder_1.newLineIfNotEmpty();
            _builder_1.append("\t");
            _builder_1.append("}");
            _builder_1.newLine();
          }
        }
        {
          if ((this.elseCase != null)) {
            _builder_1.append("\t");
            _builder_1.append(":: else -> {");
            _builder_1.newLine();
            _builder_1.append("\t");
            _builder_1.append("\t");
            String _text_6 = this.elseCase.toText();
            _builder_1.append(_text_6, "\t\t");
            _builder_1.newLineIfNotEmpty();
            _builder_1.append("\t");
            _builder_1.append("}");
            _builder_1.newLine();
          } else {
            _builder_1.append("\t");
            _builder_1.append(":: else -> skip;");
            _builder_1.newLine();
          }
        }
        {
          for(final Map.Entry<PromelaExpression, PromelaElementList<? extends PromelaStatement>> c_1 : this.elseIfCases) {
            _builder_1.append("fi; ");
          }
        }
        _builder_1.append("fi;");
        _builder_1.newLineIfNotEmpty();
        _xifexpression = _builder_1.toString();
      }
      return _xifexpression;
    }
  }
  
  public static class Case extends PromelaStatement {
    private static int caseStatements = 0;
    
    private String caseConditionValueVarName = ("tmp__caseCondVal" + Integer.valueOf(PromelaStatement.Case.caseStatements++));
    
    private PromelaExpression cond;
    
    private List<Map.Entry<List<SignedInteger>, PromelaElementList<? extends PromelaStatement>>> caseElements = new ArrayList<Map.Entry<List<SignedInteger>, PromelaElementList<? extends PromelaStatement>>>();
    
    private PromelaElementList<? extends PromelaStatement> elseCase;
    
    public Case(final CaseStatement s) {
      this.cond = PromelaExpressionsHelper.getExpr(s.getCond());
      final Consumer<CaseElement> _function = (CaseElement e) -> {
        EList<SignedInteger> _caseListElement = e.getCaseList().getCaseListElement();
        PromelaElementList<PromelaStatement> _addElements = new PromelaElementList<PromelaStatement>().addElements(PromelaStatementsHelper.getStatementList(e.getStatement()));
        AbstractMap.SimpleEntry<List<SignedInteger>, PromelaElementList<? extends PromelaStatement>> _simpleEntry = new AbstractMap.SimpleEntry<List<SignedInteger>, PromelaElementList<? extends PromelaStatement>>(_caseListElement, _addElements);
        this.caseElements.add(_simpleEntry);
      };
      s.getCaseElements().forEach(_function);
      PromelaElementList<PromelaStatement> _xifexpression = null;
      StatementList _elseStatement = s.getElseStatement();
      boolean _tripleNotEquals = (_elseStatement != null);
      if (_tripleNotEquals) {
        _xifexpression = new PromelaElementList<PromelaStatement>().addElements(PromelaStatementsHelper.getStatementList(s.getElseStatement()));
      } else {
        _xifexpression = null;
      }
      this.elseCase = _xifexpression;
    }
    
    @Override
    public String toText() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("int ");
      _builder.append(this.caseConditionValueVarName);
      _builder.append(" = ");
      String _text = this.cond.toText();
      _builder.append(_text);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.append("if");
      _builder.newLine();
      {
        for(final Map.Entry<List<SignedInteger>, PromelaElementList<? extends PromelaStatement>> e : this.caseElements) {
          _builder.append("\t");
          _builder.append(":: ");
          {
            List<SignedInteger> _key = e.getKey();
            boolean _hasElements = false;
            for(final SignedInteger c : _key) {
              if (!_hasElements) {
                _hasElements = true;
              } else {
                _builder.appendImmediate(" || ", "\t");
              }
              _builder.append("(");
              String _value = c.getValue();
              _builder.append(_value, "\t");
              _builder.append(" == ");
              _builder.append(this.caseConditionValueVarName, "\t");
              _builder.append(")");
            }
          }
          _builder.append(" -> {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          String _text_1 = e.getValue().toText();
          _builder.append(_text_1, "\t\t");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("}");
          _builder.newLine();
        }
      }
      {
        if ((this.elseCase != null)) {
          _builder.append("\t");
          _builder.append(":: else -> {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          String _text_2 = this.elseCase.toText();
          _builder.append(_text_2, "\t\t");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("}");
          _builder.newLine();
        } else {
          _builder.append("\t");
          _builder.append(":: else -> skip;");
          _builder.newLine();
        }
      }
      _builder.append("fi;");
      _builder.newLine();
      return _builder.toString();
    }
  }
  
  public static class StartProcess extends PromelaStatement {
    private String curProgramName;
    
    private String processShortName;
    
    private String processStateMTypeVar;
    
    private String processFirstStateMTypeName;
    
    private String timeoutVarName;
    
    public StartProcess(final StartProcessStatement s) {
      this.curProgramName = CurrentContext.getCurProgram().getShortName();
      this.processShortName = s.getProcess().getName();
    }
    
    @Override
    public void postConstruct() {
      final Function1<PromelaProcess, Boolean> _function = (PromelaProcess p) -> {
        return Boolean.valueOf((p.getProgramName().equals(this.curProgramName) && p.getShortName().equals(this.processShortName)));
      };
      final PromelaProcess process = IterableExtensions.<PromelaProcess>findFirst(PromelaContext.getContext().getAllProcesses(), _function);
      if ((process == null)) {
        throw new WrongModelStateException();
      }
      this.processStateMTypeVar = process.getStateVarMType();
      final PromelaState state = process.getStates().get(0);
      this.processFirstStateMTypeName = state.getStateMType();
      String _xifexpression = null;
      PromelaStatement.Timeout _timeout = state.getTimeout();
      boolean _tripleNotEquals = (_timeout != null);
      if (_tripleNotEquals) {
        _xifexpression = process.getTimeoutVar().getName();
      } else {
        _xifexpression = null;
      }
      this.timeoutVarName = _xifexpression;
    }
    
    @Override
    public String toText() {
      StringConcatenation _builder = new StringConcatenation();
      {
        if ((this.timeoutVarName != null)) {
          _builder.append(this.timeoutVarName);
          _builder.append(" = 1;");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append(this.processStateMTypeVar);
      _builder.append(" = ");
      _builder.append(this.processFirstStateMTypeName);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    }
  }
  
  public static class SetState extends PromelaStatement {
    private String nextStateName;
    
    private PromelaProcess curProcess;
    
    private PromelaState curState;
    
    private String processStateMTypeVar;
    
    private String stateMTypeName;
    
    private String timeoutVarName;
    
    public SetState(final SetStateStatement s) {
      String _xifexpression = null;
      boolean _isNext = s.isNext();
      if (_isNext) {
        _xifexpression = null;
      } else {
        _xifexpression = s.getState().getName();
      }
      this.nextStateName = _xifexpression;
      this.curProcess = CurrentContext.getCurProcess();
      this.curState = CurrentContext.getCurState();
      this.processStateMTypeVar = this.curProcess.getStateVarMType();
    }
    
    @Override
    public void postConstruct() {
      PromelaState nextState = null;
      if ((this.nextStateName != null)) {
        final Function1<PromelaState, Boolean> _function = (PromelaState state) -> {
          return Boolean.valueOf(state.getShortName().equals(this.nextStateName));
        };
        nextState = IterableExtensions.<PromelaState>findFirst(this.curProcess.getStates(), _function);
      } else {
        final int stateIndex = this.curProcess.getStates().indexOf(this.curState);
        nextState = this.curProcess.getStates().get((stateIndex + 1));
      }
      this.stateMTypeName = nextState.getStateMType();
      String _xifexpression = null;
      PromelaStatement.Timeout _timeout = nextState.getTimeout();
      boolean _tripleNotEquals = (_timeout != null);
      if (_tripleNotEquals) {
        _xifexpression = this.curProcess.getTimeoutVar().getName();
      } else {
        _xifexpression = null;
      }
      this.timeoutVarName = _xifexpression;
    }
    
    @Override
    public String toText() {
      StringConcatenation _builder = new StringConcatenation();
      {
        if ((this.timeoutVarName != null)) {
          _builder.append(this.timeoutVarName);
          _builder.append(" = 1;");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append(this.processStateMTypeVar);
      _builder.append(" = ");
      _builder.append(this.stateMTypeName);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    }
  }
  
  public static class StopProcess extends PromelaStatement {
    private String processShortName;
    
    private String curProgramName;
    
    private String processStateMTypeVar;
    
    private String processStopStateFullName;
    
    public StopProcess(final StopProcessStatement s) {
      String _xifexpression = null;
      Variable _process = s.getProcess();
      boolean _tripleNotEquals = (_process != null);
      if (_tripleNotEquals) {
        _xifexpression = s.getProcess().getName();
      } else {
        _xifexpression = CurrentContext.getCurProcess().getShortName();
      }
      this.processShortName = _xifexpression;
      this.curProgramName = CurrentContext.getCurProgram().getShortName();
    }
    
    @Override
    public void postConstruct() {
      final Function1<PromelaProcess, Boolean> _function = (PromelaProcess p) -> {
        return Boolean.valueOf((p.getProgramName().equals(this.curProgramName) && p.getShortName().equals(this.processShortName)));
      };
      final PromelaProcess process = IterableExtensions.<PromelaProcess>findFirst(PromelaContext.getContext().getAllProcesses(), _function);
      this.processStateMTypeVar = process.getStateVarMType();
      this.processStopStateFullName = process.getStopStateMType();
    }
    
    @Override
    public String toText() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(this.processStateMTypeVar);
      _builder.append(" = ");
      _builder.append(this.processStopStateFullName);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    }
  }
  
  public static class ErrorProcess extends PromelaStatement {
    private String processShortName;
    
    private String curProgramName;
    
    private String processStateMTypeVar;
    
    private String processErrorStateFullName;
    
    public ErrorProcess(final ErrorProcessStatement s) {
      String _xifexpression = null;
      Variable _process = s.getProcess();
      boolean _tripleNotEquals = (_process != null);
      if (_tripleNotEquals) {
        _xifexpression = s.getProcess().getName();
      } else {
        _xifexpression = CurrentContext.getCurProcess().getShortName();
      }
      this.processShortName = _xifexpression;
      this.curProgramName = CurrentContext.getCurProgram().getShortName();
    }
    
    @Override
    public void postConstruct() {
      final Function1<PromelaProcess, Boolean> _function = (PromelaProcess p) -> {
        return Boolean.valueOf((p.getProgramName().equals(this.curProgramName) && p.getShortName().equals(this.processShortName)));
      };
      final PromelaProcess process = IterableExtensions.<PromelaProcess>findFirst(PromelaContext.getContext().getAllProcesses(), _function);
      this.processStateMTypeVar = process.getStateVarMType();
      this.processErrorStateFullName = process.getErrorStateMtype();
    }
    
    @Override
    public String toText() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(this.processStateMTypeVar);
      _builder.append(" = ");
      _builder.append(this.processErrorStateFullName);
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    }
  }
  
  public static class ResetTimer extends PromelaStatement {
    private String timeoutVarName;
    
    public ResetTimer() {
      final PromelaVar.TimeInterval timeoutVar = CurrentContext.getCurProcess().getTimeoutVar();
      String _xifexpression = null;
      if ((timeoutVar != null)) {
        _xifexpression = timeoutVar.getName();
      } else {
        _xifexpression = null;
      }
      this.timeoutVarName = _xifexpression;
    }
    
    @Override
    public String toText() {
      StringConcatenation _builder = new StringConcatenation();
      {
        if ((this.timeoutVarName != null)) {
          _builder.append(this.timeoutVarName);
          _builder.append(" = 1;");
          _builder.newLineIfNotEmpty();
        }
      }
      return _builder.toString();
    }
  }
  
  public static class Timeout extends PromelaStatement {
    private final PromelaVar.TimeInterval timeoutVar;
    
    private final PromelaExpression timeoutValue;
    
    private final PromelaElementList<PromelaStatement> timeoutStatements = new PromelaElementList<PromelaStatement>();
    
    public Timeout(final TimeoutStatement t) {
      this.timeoutVar = CurrentContext.getCurProcess().getTimeoutVar();
      Constant _const = t.getConst();
      boolean _tripleNotEquals = (_const != null);
      if (_tripleNotEquals) {
        String _interval = t.getConst().getTime().getInterval();
        final PromelaExpression.TimeConstant timeoutConst = new PromelaExpression.TimeConstant(_interval);
        PromelaContext.getContext().addTimeVal(timeoutConst);
        this.timeoutValue = timeoutConst;
      } else {
        String _fullId = NamespaceContext.getFullId(t.getVariable().getName());
        PromelaExpression.Var _var = new PromelaExpression.Var(_fullId);
        this.timeoutValue = _var;
      }
      this.timeoutStatements.addElements(PromelaStatementsHelper.getStatementList(t.getStatement()));
    }
    
    public PromelaVar.TimeInterval getTimeoutVar() {
      return this.timeoutVar;
    }
    
    public PromelaExpression getTimeoutValue() {
      return this.timeoutValue;
    }
    
    @Override
    public String toText() {
      String _xblockexpression = null;
      {
        final String timeoutVarName = this.timeoutVar.getName();
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("if :: ");
        _builder.append(timeoutVarName);
        _builder.append(" < ");
        String _text = this.timeoutValue.toText();
        _builder.append(_text);
        _builder.append(" -> {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _text_1 = this.timeoutStatements.toText();
        _builder.append(_text_1, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("} :: else -> skip; fi;");
        _builder.newLine();
        _builder.append(timeoutVarName);
        _builder.append(" = ");
        _builder.append(timeoutVarName);
        _builder.append(" + 1;");
        _builder.newLineIfNotEmpty();
        _xblockexpression = _builder.toString();
      }
      return _xblockexpression;
    }
  }
  
  public PromelaStatement() {
    PostConstructContext.register(this);
  }
  
  @Override
  public void postConstruct() {
  }
}
