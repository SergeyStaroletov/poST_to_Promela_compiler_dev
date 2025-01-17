package iae.post.generator.promela.model.vars

import iae.post.poST.VarInitDeclaration
import iae.post.generator.promela.model.PromelaElementList
import java.util.List
import iae.post.generator.promela.exceptions.UnknownElementException
import iae.post.generator.promela.exceptions.NotSupportedElementException
import iae.post.generator.promela.context.PromelaContext
import java.util.ArrayList
import iae.post.generator.promela.context.NamespaceContext
import iae.post.generator.promela.model.expressions.PromelaExpressionsHelper
import iae.post.poST.PrimaryExpression
import iae.post.poST.IntegerLiteral
import iae.post.generator.promela.context.WarningsContext

class PromelaVarsHelper {	
	static def PromelaElementList<PromelaVar> getVars(List<VarInitDeclaration> varDecls, boolean const) {
		val res = new PromelaElementList<PromelaVar>();
		varDecls.forEach([v | res.addAll(getVar(v))]);
		if (const) {
			res.forEach[v | v.setConstantTrue()];
		}
		return res;
	}
	
	static def PromelaElementList<PromelaVar> getVars(List<VarInitDeclaration> varDecls) {
		return getVars(varDecls, false);
	}
	
	private static def List<PromelaVar> getVar(VarInitDeclaration varDecl) {
		val simpleSpec = varDecl.spec;
		if (simpleSpec !== null) {
			val type = simpleSpec.type.toUpperCase();
			val varValue = simpleSpec.value;
			val List<PromelaVar> res = new ArrayList();
			varDecl.varList.vars.forEach[v | {
				val simpleVar = getPromelaSimpleVar(type, NamespaceContext.addId(v.name));
				if (varValue !== null) {
					simpleVar.setValue(PromelaExpressionsHelper.getExpr(varValue));
					if ("TIME".equals(type) && varValue instanceof PrimaryExpression) {
						(simpleVar as PromelaVar.TimeInterval).setOriginalValue(
							(varValue as PrimaryExpression).const.time.interval
						);
					}
				}
				res.add(simpleVar);
			}];
			return res;
		}
		val arrSpec = varDecl.arrSpec;
		if (arrSpec !== null) {
			if (arrSpec.init.interval === null) {
				throw new NotSupportedElementException("Array with no interval specified");
			}
			var int intervalStart = -1;
			var int intervalEnd = -1;
			val intervalStartExpr = arrSpec.init.interval.start;
			val intervalEndExpr = arrSpec.init.interval.end;
			if (intervalStartExpr instanceof PrimaryExpression) {
				if (intervalEndExpr instanceof PrimaryExpression) {
					if (intervalStartExpr.const !== null
						&& intervalEndExpr.const !== null
						&& intervalStartExpr.const.num !== null
						&& intervalEndExpr.const.num !== null
					) {
						val start = intervalStartExpr.const.num;
						val end = intervalEndExpr.const.num;
						if (start instanceof IntegerLiteral) {
							if (end instanceof IntegerLiteral) {
								intervalStart = Integer.valueOf(start.value.value);
								intervalEnd = Integer.valueOf(end.value.value);
							}
						}
					}
				}
			}
			if (intervalStart == -1 || intervalEnd == -1) {
				throw new NotSupportedElementException("Array with interval specified by expressions more complex than numbers");
			}
			val typeName = postToPromelaTypeNameForArray(arrSpec.init.type);
			
			val initValueExpressions = arrSpec.values === null ? null : arrSpec.values.elements.map[e |
				return PromelaExpressionsHelper.getExpr(e);
			];
			
			val start = intervalStart;
			val end = intervalEnd;
			val List<PromelaVar> res = new ArrayList();
			varDecl.varList.vars.forEach[v | 
				val fullName = NamespaceContext.addId(v.name);
				val arrayVariable = new PromelaVar.Array(fullName, typeName, start, end, initValueExpressions);
				PromelaContext.context.addArrayVar(arrayVariable);
				res.add(arrayVariable);
			];
			return res;
		}
		throw new NotSupportedElementException("VarInitDeclaration which is not simple or array variable (" + varDecl + ")");
	}
	
	private static def getPromelaSimpleVar(String type, String name) {
		val context = PromelaContext.getContext();
		switch (type) {				
			//signed integer
			case "SINT": return new PromelaVar.Short(name, true)
			case "INT": return new PromelaVar.Short(name)
			case "DINT": return new PromelaVar.Int(name)
			case "LINT": return notSupportedType(new PromelaVar.Int(name), "LINT", "DINT")
			
			//unsigned integer
			case "USINT": return new PromelaVar.Byte(name)
			case "UINT": return new PromelaVar.Unsigned(name, 16)
			case "UDINT": return notSupportedType(new PromelaVar.Int(name), "UDINT", "DINT")
			case "ULINT": return notSupportedType(new PromelaVar.Int(name), "ULINT", "DINT")
			
			//real
			case "REAL": return notSupportedType(new PromelaVar.Int(name), "REAL", "DINT")
			case "LREAL": return notSupportedType(new PromelaVar.Int(name), "LREAL", "DINT")
			
			//bit string
			case "BOOL": return new PromelaVar.Bool(name)
			case "BYTE": return new PromelaVar.Byte(name)
			case "WORD": return new PromelaVar.Short(name)
			case "DWORD": return new PromelaVar.Int(name)
			case "LWORD": return notSupportedType(new PromelaVar.Int(name), "LWORD", "DWORD")
			
			//time
			case "TIME": context.addTimeVar(name)
			
			//string
			case "STRING": throw new NotSupportedElementException("poST variable of type STRING")
			case "WSTRING": throw new NotSupportedElementException("poST variable of type WSTRING")
			
			default: throw new UnknownElementException("poST variable of type " + type)
		}
	}
	
	private static def <T extends PromelaVar> T notSupportedType(T v, String oldType, String newType) {
		WarningsContext.addWarning('''Type �oldType� is not fully supported. Variable �v.name��
			� registered as of type �newType�''');
		return v;
	}
	
	private static def String postToPromelaTypeNameForArray(String type) {
		if ("SINT".equals(type)) {
			WarningsContext.addWarning("Type ARRAY OF SINT can only be translated to byte[] which are 8-bit instead of 16-bit");
			return "byte";
		}
		if ("TIME".equals(type)) {
			throw new NotSupportedElementException("Array of TIME variables");
		}
		return getPromelaSimpleVar(type, null).typeName;
	}
	
}