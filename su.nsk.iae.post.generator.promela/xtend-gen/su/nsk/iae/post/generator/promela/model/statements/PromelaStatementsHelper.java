package su.nsk.iae.post.generator.promela.model.statements;

import com.google.common.base.Objects;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import su.nsk.iae.post.generator.promela.exceptions.UnknownElementException;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.StatementList;
import su.nsk.iae.post.poST.impl.AssignmentStatementImpl;
import su.nsk.iae.post.poST.impl.CaseStatementImpl;
import su.nsk.iae.post.poST.impl.ErrorProcessStatementImpl;
import su.nsk.iae.post.poST.impl.ForStatementImpl;
import su.nsk.iae.post.poST.impl.IfStatementImpl;
import su.nsk.iae.post.poST.impl.RepeatStatementImpl;
import su.nsk.iae.post.poST.impl.ResetTimerStatementImpl;
import su.nsk.iae.post.poST.impl.SetStateStatementImpl;
import su.nsk.iae.post.poST.impl.StartProcessStatementImpl;
import su.nsk.iae.post.poST.impl.StopProcessStatementImpl;
import su.nsk.iae.post.poST.impl.TimeoutStatementImpl;
import su.nsk.iae.post.poST.impl.WhileStatementImpl;

@SuppressWarnings("all")
public class PromelaStatementsHelper {
  public static List<PromelaStatement> getStatementList(final StatementList statements) {
    final Function1<Statement, PromelaStatement> _function = (Statement s) -> {
      return PromelaStatementsHelper.getStatement(s);
    };
    return ListExtensions.<Statement, PromelaStatement>map(statements.getStatements(), _function);
  }
  
  public static PromelaStatement getStatement(final Statement s) {
    Class<? extends Statement> _class = s.getClass();
    boolean _matched = false;
    if (Objects.equal(_class, AssignmentStatementImpl.class)) {
      _matched=true;
      return new PromelaStatement.Assign(((AssignmentStatementImpl) s));
    }
    if (!_matched) {
      if (Objects.equal(_class, IfStatementImpl.class)) {
        _matched=true;
        return new PromelaStatement.If(((IfStatementImpl) s));
      }
    }
    if (!_matched) {
      if (Objects.equal(_class, CaseStatementImpl.class)) {
        _matched=true;
        return new PromelaStatement.Case(((CaseStatementImpl) s));
      }
    }
    if (!_matched) {
      if (Objects.equal(_class, StartProcessStatementImpl.class)) {
        _matched=true;
        return new PromelaStatement.StartProcess(((StartProcessStatementImpl) s));
      }
    }
    if (!_matched) {
      if (Objects.equal(_class, StopProcessStatementImpl.class)) {
        _matched=true;
        return new PromelaStatement.StopProcess(((StopProcessStatementImpl) s));
      }
    }
    if (!_matched) {
      if (Objects.equal(_class, ErrorProcessStatementImpl.class)) {
        _matched=true;
        return new PromelaStatement.ErrorProcess(((ErrorProcessStatementImpl) s));
      }
    }
    if (!_matched) {
      if (Objects.equal(_class, SetStateStatementImpl.class)) {
        _matched=true;
        return new PromelaStatement.SetState(((SetStateStatementImpl) s));
      }
    }
    if (!_matched) {
      if (Objects.equal(_class, ResetTimerStatementImpl.class)) {
        _matched=true;
        return new PromelaStatement.ResetTimer();
      }
    }
    if (!_matched) {
      if (Objects.equal(_class, TimeoutStatementImpl.class)) {
        _matched=true;
        return new PromelaStatement.Timeout(((TimeoutStatementImpl) s));
      }
    }
    if (!_matched) {
      if (Objects.equal(_class, WhileStatementImpl.class)) {
        _matched=true;
        return new PromelaStatement.While(((WhileStatementImpl) s));
      }
    }
    if (!_matched) {
      if (Objects.equal(_class, RepeatStatementImpl.class)) {
        _matched=true;
        return new PromelaStatement.Repeat(((RepeatStatementImpl) s));
      }
    }
    if (!_matched) {
      if (Objects.equal(_class, ForStatementImpl.class)) {
        _matched=true;
        return new PromelaStatement.For(((ForStatementImpl) s));
      }
    }
    String _string = s.getClass().toString();
    String _plus = ("Statement of type " + _string);
    String _plus_1 = (_plus + " (");
    String _plus_2 = (_plus_1 + s);
    String _plus_3 = (_plus_2 + ")");
    throw new UnknownElementException(_plus_3);
  }
}
