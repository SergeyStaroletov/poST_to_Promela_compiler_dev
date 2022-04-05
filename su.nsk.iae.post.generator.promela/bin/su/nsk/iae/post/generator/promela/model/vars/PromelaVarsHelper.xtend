package su.nsk.iae.post.generator.promela.model.vars

import su.nsk.iae.post.poST.VarInitDeclaration
import su.nsk.iae.post.generator.promela.model.PromelaElementList
import java.util.List
import su.nsk.iae.post.generator.promela.model.UnknownElementException
import su.nsk.iae.post.generator.promela.model.NotSupportedElementException
import su.nsk.iae.post.generator.promela.context.PromelaContext
import java.util.ArrayList
import su.nsk.iae.post.generator.promela.context.NamespaceContext
import su.nsk.iae.post.generator.promela.expressions.PromelaExpressionsHelper
import su.nsk.iae.post.poST.Constant
import su.nsk.iae.post.poST.PrimaryExpression

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
			val List<PromelaVar> res = new ArrayList();
			varDecl.varList.vars.forEach[v | {
				val simpleVar = getPromelaSimpleVar(type, NamespaceContext.addId(v.name));
				val varValue = simpleSpec.value;
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
			throw new NotSupportedElementException();
			//todo
		}
		throw new NotSupportedElementException();
	}
	
	private static def getPromelaSimpleVar(String type, String name) {
		val context = PromelaContext.getContext();
		switch (type) {				
			//signed integer
			case "SINT": return new PromelaVar.Short(name)
			case "INT": return new PromelaVar.Short(name)
			case "DINT": return new PromelaVar.Int(name)
			case "LINT": throw new NotSupportedElementException()
			
			//unsigned integer
			case "USINT": return new PromelaVar.Byte(name)
			case "UINT": return new PromelaVar.Unsigned(name, 16)
			case "UDINT": throw new NotSupportedElementException()
			case "ULINT": throw new NotSupportedElementException()
			
			//real
			case "REAL": throw new NotSupportedElementException()
			case "LREAL": throw new NotSupportedElementException()
			
			//bit string
			case "BOOL": return new PromelaVar.Bool(name)
			case "BYTE": return new PromelaVar.Byte(name)
			case "WORD": return new PromelaVar.Short(name)
			case "DWORD": return new PromelaVar.Int(name)
			case "LWORD": throw new NotSupportedElementException()
			
			//time
			case "TIME": context.addTimeVar(name)
			
			//string
			case "STRING": throw new NotSupportedElementException()
			case "WSTRING": throw new NotSupportedElementException()
			
			default: throw new UnknownElementException()
		}
	}
}