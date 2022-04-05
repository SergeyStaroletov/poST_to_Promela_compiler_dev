package su.nsk.iae.post.generator.promela.expressions

import su.nsk.iae.post.poST.Expression
import su.nsk.iae.post.generator.promela.model.UnknownElementException
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
import su.nsk.iae.post.generator.promela.model.NotSupportedElementException
import su.nsk.iae.post.generator.promela.context.PromelaContext
import su.nsk.iae.post.poST.IntegerLiteral

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
					else {
						throw new NotSupportedElementException();
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
					throw new NotSupportedElementException();
				}
				else {
					throw new UnknownElementException();
				}
			}
			else {
				return new PromelaExpression.Primary(getExpr(expr.nestExpr));
			}
		}
		else if (expr instanceof UnaryExpression) {
			return new PromelaExpression.Not(getExpr(expr.right));
		}
		else {
			val left = getExpr(expr.left);
			val right = getExpr(expr.right);
			var String opLiteral;
			switch (expr.class) {
				case ExpressionImpl/*OR*/: opLiteral = "||"
				case XorExpressionImpl: opLiteral = "^"
				case AndExpressionImpl: opLiteral = "&"
				case CompExpressionImpl: opLiteral = getCompLiteral(expr)
				case EquExpressionImpl: opLiteral = (expr as EquExpressionImpl).equOp.literal
				case AddExpressionImpl: opLiteral = (expr as AddExpressionImpl).addOp.literal
				case MulExpressionImpl: opLiteral = getMulLiteral(expr)
				case PowerExpressionImpl: throw new NotSupportedElementException()
				default: throw new UnknownElementException()
			}
			return new PromelaExpression.Binary(left, opLiteral, right);
		}
	}
	
	private static def getCompLiteral(Expression expr) {
		switch ((expr as CompExpressionImpl).compOp.literal) {
			case "=": "=="
			case "<>": "!="
			default: throw new UnknownElementException()
		}
	}
	
	private static def getMulLiteral(Expression expr) {
		switch ((expr as MulExpressionImpl).compOp.literal) {
			case "MOD": "%"
			default: (expr as MulExpressionImpl).compOp.literal
		}
	}
}