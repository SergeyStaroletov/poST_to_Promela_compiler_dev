package su.nsk.iae.post.generator.promela.model.expressions;

import com.google.common.base.Objects;
import su.nsk.iae.post.generator.promela.context.NamespaceContext;
import su.nsk.iae.post.generator.promela.context.PromelaContext;
import su.nsk.iae.post.generator.promela.exceptions.NotSupportedElementException;
import su.nsk.iae.post.generator.promela.exceptions.UnknownElementException;
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar;
import su.nsk.iae.post.poST.ArrayVariable;
import su.nsk.iae.post.poST.Constant;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.IntegerLiteral;
import su.nsk.iae.post.poST.NumericLiteral;
import su.nsk.iae.post.poST.PrimaryExpression;
import su.nsk.iae.post.poST.ProcessStatusExpression;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.TimeLiteral;
import su.nsk.iae.post.poST.UnaryExpression;
import su.nsk.iae.post.poST.impl.AddExpressionImpl;
import su.nsk.iae.post.poST.impl.AndExpressionImpl;
import su.nsk.iae.post.poST.impl.CompExpressionImpl;
import su.nsk.iae.post.poST.impl.EquExpressionImpl;
import su.nsk.iae.post.poST.impl.ExpressionImpl;
import su.nsk.iae.post.poST.impl.MulExpressionImpl;
import su.nsk.iae.post.poST.impl.PowerExpressionImpl;
import su.nsk.iae.post.poST.impl.XorExpressionImpl;

@SuppressWarnings("all")
public class PromelaExpressionsHelper {
  public static PromelaExpression getExpr(final Expression expr) {
    if ((expr instanceof PrimaryExpression)) {
      SymbolicVariable _variable = ((PrimaryExpression)expr).getVariable();
      boolean _tripleNotEquals = (_variable != null);
      if (_tripleNotEquals) {
        String _fullId = NamespaceContext.getFullId(((PrimaryExpression)expr).getVariable().getName());
        return new PromelaExpression.Var(_fullId);
      } else {
        Constant _const = ((PrimaryExpression)expr).getConst();
        boolean _tripleNotEquals_1 = (_const != null);
        if (_tripleNotEquals_1) {
          NumericLiteral _num = ((PrimaryExpression)expr).getConst().getNum();
          boolean _tripleNotEquals_2 = (_num != null);
          if (_tripleNotEquals_2) {
            final NumericLiteral num = ((PrimaryExpression)expr).getConst().getNum();
            if ((num instanceof IntegerLiteral)) {
              String _value = ((IntegerLiteral)num).getValue().getValue();
              return new PromelaExpression.Constant(_value);
            } else {
              String _string = num.getClass().toString();
              String _plus = ("Number constant of type " + _string);
              throw new NotSupportedElementException(_plus);
            }
          } else {
            TimeLiteral _time = ((PrimaryExpression)expr).getConst().getTime();
            boolean _tripleNotEquals_3 = (_time != null);
            if (_tripleNotEquals_3) {
              String _interval = ((PrimaryExpression)expr).getConst().getTime().getInterval();
              final PromelaExpression.TimeConstant res = new PromelaExpression.TimeConstant(_interval);
              PromelaContext.getContext().addTimeVal(res);
              return res;
            } else {
              String _oth = ((PrimaryExpression)expr).getConst().getOth();
              boolean _tripleNotEquals_4 = (_oth != null);
              if (_tripleNotEquals_4) {
                boolean _equals = "TRUE".equals(((PrimaryExpression)expr).getConst().getOth());
                if (_equals) {
                  return new PromelaExpression.Constant("true");
                }
                boolean _equals_1 = "FALSE".equals(((PrimaryExpression)expr).getConst().getOth());
                if (_equals_1) {
                  return new PromelaExpression.Constant("false");
                }
                String _oth_1 = ((PrimaryExpression)expr).getConst().getOth();
                String _plus_1 = ("\"" + _oth_1);
                String _plus_2 = (_plus_1 + "\"");
                throw new NotSupportedElementException(_plus_2);
              } else {
                Constant _const_1 = ((PrimaryExpression)expr).getConst();
                String _plus_3 = ("Constant (" + _const_1);
                String _plus_4 = (_plus_3 + ")");
                throw new UnknownElementException(_plus_4);
              }
            }
          }
        } else {
          ProcessStatusExpression _procStatus = ((PrimaryExpression)expr).getProcStatus();
          boolean _tripleNotEquals_5 = (_procStatus != null);
          if (_tripleNotEquals_5) {
            ProcessStatusExpression _procStatus_1 = ((PrimaryExpression)expr).getProcStatus();
            return new PromelaExpression.ProcessStatus(_procStatus_1);
          } else {
            ArrayVariable _array = ((PrimaryExpression)expr).getArray();
            boolean _tripleNotEquals_6 = (_array != null);
            if (_tripleNotEquals_6) {
              PromelaVar.Array _arrayVar = PromelaContext.getContext().getArrayVar(NamespaceContext.getFullId(((PrimaryExpression)expr).getArray().getVariable().getName()));
              PromelaExpression _expr = PromelaExpressionsHelper.getExpr(((PrimaryExpression)expr).getArray().getIndex());
              return new PromelaExpression.ArrayVar(_arrayVar, _expr);
            } else {
              PromelaExpression _expr_1 = PromelaExpressionsHelper.getExpr(((PrimaryExpression)expr).getNestExpr());
              return new PromelaExpression.Primary(_expr_1);
            }
          }
        }
      }
    } else {
      if ((expr instanceof UnaryExpression)) {
        PromelaExpression _xifexpression = null;
        boolean _equals_2 = "-".equals(((UnaryExpression)expr).getUnOp().getLiteral());
        if (_equals_2) {
          PromelaExpression _expr_2 = PromelaExpressionsHelper.getExpr(((UnaryExpression)expr).getRight());
          _xifexpression = new PromelaExpression.Invert(_expr_2);
        } else {
          PromelaExpression _expr_3 = PromelaExpressionsHelper.getExpr(((UnaryExpression)expr).getRight());
          _xifexpression = new PromelaExpression.Not(_expr_3);
        }
        return _xifexpression;
      } else {
        final PromelaExpression left = PromelaExpressionsHelper.getExpr(expr.getLeft());
        final PromelaExpression right = PromelaExpressionsHelper.getExpr(expr.getRight());
        String opLiteral = null;
        Class<? extends Expression> _class = expr.getClass();
        boolean _matched = false;
        if (Objects.equal(_class, ExpressionImpl.class)) {
          _matched=true;
          opLiteral = "||";
        }
        if (!_matched) {
          if (Objects.equal(_class, XorExpressionImpl.class)) {
            _matched=true;
            opLiteral = "^";
          }
        }
        if (!_matched) {
          if (Objects.equal(_class, AndExpressionImpl.class)) {
            _matched=true;
            opLiteral = "&";
          }
        }
        if (!_matched) {
          if (Objects.equal(_class, CompExpressionImpl.class)) {
            _matched=true;
            opLiteral = PromelaExpressionsHelper.getCompLiteral(expr);
          }
        }
        if (!_matched) {
          if (Objects.equal(_class, EquExpressionImpl.class)) {
            _matched=true;
            opLiteral = ((EquExpressionImpl) expr).getEquOp().getLiteral();
          }
        }
        if (!_matched) {
          if (Objects.equal(_class, AddExpressionImpl.class)) {
            _matched=true;
            opLiteral = ((AddExpressionImpl) expr).getAddOp().getLiteral();
          }
        }
        if (!_matched) {
          if (Objects.equal(_class, MulExpressionImpl.class)) {
            _matched=true;
            opLiteral = PromelaExpressionsHelper.getMulLiteral(expr);
          }
        }
        if (!_matched) {
          if (Objects.equal(_class, PowerExpressionImpl.class)) {
            _matched=true;
            String _string_1 = expr.getClass().toString();
            throw new NotSupportedElementException(_string_1);
          }
        }
        if (!_matched) {
          String _string_2 = expr.getClass().toString();
          String _plus_5 = ("Expression of type " + _string_2);
          String _plus_6 = (_plus_5 + " (");
          String _plus_7 = (_plus_6 + expr);
          String _plus_8 = (_plus_7 + ")");
          throw new UnknownElementException(_plus_8);
        }
        return new PromelaExpression.Binary(left, opLiteral, right);
      }
    }
  }
  
  private static String getCompLiteral(final Expression expr) {
    String _switchResult = null;
    String _literal = ((CompExpressionImpl) expr).getCompOp().getLiteral();
    if (_literal != null) {
      switch (_literal) {
        case "=":
          _switchResult = "==";
          break;
        case "<>":
          _switchResult = "!=";
          break;
        default:
          String _literal_1 = ((CompExpressionImpl) expr).getCompOp().getLiteral();
          String _plus = ("\"" + _literal_1);
          String _plus_1 = (_plus + "\" in CompExpressionImpl");
          throw new UnknownElementException(_plus_1);
      }
    } else {
      String _literal_1 = ((CompExpressionImpl) expr).getCompOp().getLiteral();
      String _plus = ("\"" + _literal_1);
      String _plus_1 = (_plus + "\" in CompExpressionImpl");
      throw new UnknownElementException(_plus_1);
    }
    return _switchResult;
  }
  
  private static String getMulLiteral(final Expression expr) {
    String _switchResult = null;
    String _literal = ((MulExpressionImpl) expr).getCompOp().getLiteral();
    if (_literal != null) {
      switch (_literal) {
        case "MOD":
          _switchResult = "%";
          break;
        default:
          _switchResult = ((MulExpressionImpl) expr).getCompOp().getLiteral();
          break;
      }
    } else {
      _switchResult = ((MulExpressionImpl) expr).getCompOp().getLiteral();
    }
    return _switchResult;
  }
}
