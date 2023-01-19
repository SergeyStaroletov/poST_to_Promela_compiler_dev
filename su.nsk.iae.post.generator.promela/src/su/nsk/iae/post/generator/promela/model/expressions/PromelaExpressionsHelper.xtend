package su.nsk.iae.post.generator.promela.model.expressions

import su.nsk.iae.post.poST.Expression
import su.nsk.iae.post.generator.promela.exceptions.UnknownElementException
import su.nsk.iae.post.poST.PrimaryExpression
import su.nsk.iae.post.poST.UnaryExpression
import su.nsk.iae.post.generator.promela.context.NamespaceContext
import su.nsk.iae.post.poST.impl.ExpressionImpl
import su.nsk.iae.post.poST.impl.XorExpressionImpl
import su.nsk.iae.post.poST.impl.AndExpressionImpl
import su.nsk.iae.post.poST.impl.CompExpressionImpl
import su.nsk.iae.post.poST.impl.EquExpressionImpl
import su.nsk.iae.post.poST.impl.AddExpressionImpl
import su.nsk.iae.post.poST.impl.MulExpressionImpl
import su.nsk.iae.post.poST.impl.PowerExpressionImpl
import su.nsk.iae.post.generator.promela.exceptions.NotSupportedElementException
import su.nsk.iae.post.generator.promela.context.PromelaContext
import su.nsk.iae.post.poST.IntegerLiteral
import su.nsk.iae.post.poST.RealLiteral

class PromelaExpressionsHelper {
	static def PromelaExpression getExpr(Expression expr) {
		if (expr instanceof PrimaryExpression) {
			if (expr.variable !== null) {
				return new PromelaExpression.Var(NamespaceContext.getFullId(expr.variable.name));
			}
			else if (expr.const !== null) {
				if (expr.const.num !== null) {
					val num = expr.const.num;
					if (num instanceof IntegerLiteral) {
						return new PromelaExpression.Constant(num.value.value);
					}
					else if (num instanceof RealLiteral) {
						return new PromelaExpression.Constant(
							String.valueOf(Math.round(Double.parseDouble(num.value))));
					}
					else {
						throw new UnknownElementException("Number constant of type " + num.class.toString);
					}
				}
				else if (expr.const.time !== null) {
					val res = new PromelaExpression.TimeConstant(expr.const.time.interval);
					PromelaContext.getContext().addTimeVal(res);
					return res;
				}
				else if (expr.const.oth !== null) {
					if ("TRUE".equals(expr.const.oth)) {
						return new PromelaExpression.Constant("true");
					}
					if ("FALSE".equals(expr.const.oth)) {
						return new PromelaExpression.Constant("false");
					}
					throw new NotSupportedElementException('"' + expr.const.oth + '"');
				}
				else {
					throw new UnknownElementException("Constant (" + expr.const + ")");
				}
			}
			else if (expr.procStatus !== null) {
				return new PromelaExpression.ProcessStatus(expr.procStatus);
			}
			else if (expr.array !== null) {
				return new PromelaExpression.ArrayVar(
					PromelaContext.context.getArrayVar(NamespaceContext.getFullId(expr.array.variable.name)),
					getExpr(expr.array.index)
				);
			}
			else {
				return new PromelaExpression.Primary(getExpr(expr.nestExpr));
			}
		}
		else if (expr instanceof UnaryExpression) {
			return "-".equals(expr.unOp.literal) ?
				new PromelaExpression.Invert(getExpr(expr.right)) :
				new PromelaExpression.Not(getExpr(expr.right));
		}
		else {
			val left = getExpr(expr.left);
			val right = getExpr(expr.right);
			var String opLiteral;
			switch (expr.class) {
				case ExpressionImpl/*OR*/: opLiteral = "||"
				case XorExpressionImpl: opLiteral = "^"
				case AndExpressionImpl: opLiteral = "&&"
				case CompExpressionImpl: opLiteral = getCompLiteral(expr)
				case EquExpressionImpl: opLiteral = (expr as EquExpressionImpl).equOp.literal
				case AddExpressionImpl: opLiteral = (expr as AddExpressionImpl).addOp.literal
				case MulExpressionImpl: opLiteral = getMulLiteral(expr)
				case PowerExpressionImpl: throw new NotSupportedElementException(expr.class.toString)
				default: throw new UnknownElementException("Expression of type " + expr.class.toString + " (" + expr + ")")
			}
			return new PromelaExpression.Binary(left, opLiteral, right);
		}
	}
	
	private static def getCompLiteral(Expression expr) {
		switch ((expr as CompExpressionImpl).compOp.literal) {
			case "=": "=="
			case "<>": "!="
			default: throw new UnknownElementException('"' + (expr as CompExpressionImpl).compOp.literal + "\" in CompExpressionImpl")
		}
	}
	
	private static def getMulLiteral(Expression expr) {
		switch ((expr as MulExpressionImpl).compOp.literal) {
			case "MOD": "%"
			default: (expr as MulExpressionImpl).compOp.literal
		}
	}
}