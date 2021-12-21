package su.nsk.iae.post.generator.promela.model.vars;

import java.util.List;
import java.util.function.Consumer;
import su.nsk.iae.post.generator.promela.model.PromelaElementList;
import su.nsk.iae.post.generator.promela.model.UnknownElementException;
import su.nsk.iae.post.poST.SimpleSpecificationInit;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class PromelaVarsHelper {
  public static PromelaElementList<PromelaVar> getVars(final List<VarInitDeclaration> varDecls) {
    final PromelaElementList<PromelaVar> res = new PromelaElementList<PromelaVar>();
    final Consumer<VarInitDeclaration> _function = (VarInitDeclaration v) -> {
      res.add(PromelaVarsHelper.getVar(v));
    };
    varDecls.forEach(_function);
    return res;
  }
  
  public static PromelaVar getVar(final VarInitDeclaration varDecl) {
    final SimpleSpecificationInit simpleSpec = varDecl.getSpec();
    if ((simpleSpec != null)) {
      final String name = varDecl.getVarList().getVars().get(0).getName();
      final String type = simpleSpec.getType().toUpperCase();
      if (type != null) {
        switch (type) {
          case "BOOL":
            return new PromelaBoolVar(name);
          default:
            throw new UnknownElementException();
        }
      } else {
        throw new UnknownElementException();
      }
    }
    return null;
  }
}
