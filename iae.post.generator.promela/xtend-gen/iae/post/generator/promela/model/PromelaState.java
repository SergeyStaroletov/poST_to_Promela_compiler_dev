package iae.post.generator.promela.model;

import iae.post.generator.promela.context.CurrentContext;
import iae.post.generator.promela.context.NamespaceContext;
import iae.post.generator.promela.model.statements.PromelaStatement;
import iae.post.generator.promela.model.statements.PromelaStatementsHelper;
import iae.post.poST.State;
import iae.post.poST.TimeoutStatement;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class PromelaState implements IPromelaElement {
  private final String shortName;
  
  private final String stateMType;
  
  private final String stateVarMType;
  
  private final PromelaElementList<PromelaStatement> statements = new PromelaElementList<PromelaStatement>();
  
  private final PromelaStatement.Timeout timeout;
  
  public PromelaState(final State s, final String stateMType, final String stateVarMType) {
    CurrentContext.startState(this);
    this.shortName = s.getName();
    this.stateMType = stateMType;
    this.stateVarMType = stateVarMType;
    this.statements.addElements(PromelaStatementsHelper.getStatementList(s.getStatement()));
    PromelaStatement.Timeout _xifexpression = null;
    TimeoutStatement _timeout = s.getTimeout();
    boolean _tripleNotEquals = (_timeout != null);
    if (_tripleNotEquals) {
      TimeoutStatement _timeout_1 = s.getTimeout();
      _xifexpression = new PromelaStatement.Timeout(_timeout_1);
    } else {
      _xifexpression = null;
    }
    this.timeout = _xifexpression;
    CurrentContext.stopState();
  }
  
  public String getStateMType() {
    return this.stateMType;
  }
  
  public String getShortName() {
    return this.shortName;
  }
  
  public PromelaStatement.Timeout getTimeout() {
    return this.timeout;
  }
  
  @Override
  public String toText() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(":: ");
    String _name = NamespaceContext.getName(this.stateMType);
    _builder.append(_name);
    _builder.append(" == ");
    String _name_1 = NamespaceContext.getName(this.stateVarMType);
    _builder.append(_name_1);
    _builder.append(" -> {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _text = this.statements.toText();
    _builder.append(_text, "\t");
    _builder.newLineIfNotEmpty();
    {
      if ((this.timeout != null)) {
        _builder.append("\t");
        String _text_1 = this.timeout.toText();
        _builder.append(_text_1, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
