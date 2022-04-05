package su.nsk.iae.post.generator.promela.expressions;

import com.google.common.base.Objects;
import su.nsk.iae.post.generator.promela.context.NamespaceContext;
import su.nsk.iae.post.generator.promela.context.PromelaContext;
import su.nsk.iae.post.generator.promela.model.NotSupportedElementException;
import su.nsk.iae.post.generator.promela.model.UnknownElementException;
import su.nsk.iae.post.poST.Constant;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.IntegerLiteral;
import su.nsk.iae.post.poST.NumericLiteral;
import su.nsk.iae.post.poST.PrimaryExpression;
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
              throw new NotSupportedElementException();
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
                throw new NotSupportedElementException();
              } else {
                throw new UnknownElementException();
              }
            }
          }
        } else {
          PromelaExpression _expr = PromelaExpressionsHelper.getExpr(((PrimaryExpression)expr).getNestExpr());
          return new PromelaExpression.Primary(_expr);
        }
      }
    } else {
      if ((expr instanceof UnaryExpression)) {
        PromelaExpression _expr_1 = PromelaExpressionsHelper.getExpr(((UnaryExpression)expr).getRight());
        return new PromelaExpression.Not(_expr_1);
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
            throw new NotSupportedElementException();
          }
        }
        if (!_matched) {
          throw new UnknownElementException();
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
          throw new UnknownElementException();
      }
    } else {
      throw new UnknownElementException();
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
