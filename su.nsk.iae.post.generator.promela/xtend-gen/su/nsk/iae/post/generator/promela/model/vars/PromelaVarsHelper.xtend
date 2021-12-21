package su.nsk.iae.post.generator.promela.model.vars

import su.nsk.iae.post.poST.VarInitDeclaration
import su.nsk.iae.post.generator.promela.model.PromelaElementList
import java.util.List
import su.nsk.iae.post.generator.promela.model.UnknownElementException

class PromelaVarsHelper {
	static def PromelaElementList<PromelaVar> getVars(List<VarInitDeclaration> varDecls) {
		val res = new PromelaElementList<PromelaVar>();
		varDecls.forEach([v | res.add(getVar(v))]);
		return res;
	}
	
	static def PromelaVar getVar(VarInitDeclaration varDecl) {
		val simpleSpec = varDecl.spec;
		if (simpleSpec !== null) {
			val name = varDecl.varList.vars.get(0).name;
			val type = simpleSpec.type.toUpperCase();
			switch (type) {
				case "BOOL": return new PromelaBoolVar(name)
				default: throw new UnknownElementException()
			}
		}
		return null;
	}
}