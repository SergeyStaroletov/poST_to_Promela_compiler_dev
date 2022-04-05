package su.nsk.iae.post.generator.promela.model.vars;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import su.nsk.iae.post.generator.promela.context.NamespaceContext;
import su.nsk.iae.post.generator.promela.context.PromelaContext;
import su.nsk.iae.post.generator.promela.expressions.PromelaExpressionsHelper;
import su.nsk.iae.post.generator.promela.model.NotSupportedElementException;
import su.nsk.iae.post.generator.promela.model.PromelaElementList;
import su.nsk.iae.post.generator.promela.model.UnknownElementException;
import su.nsk.iae.post.poST.ArraySpecificationInit;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.PrimaryExpression;
import su.nsk.iae.post.poST.SimpleSpecificationInit;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.VarInitDeclaration;

@SuppressWarnings("all")
public class PromelaVarsHelper {
  public static PromelaElementList<PromelaVar> getVars(final List<VarInitDeclaration> varDecls, final boolean const_) {
    final PromelaElementList<PromelaVar> res = new PromelaElementList<PromelaVar>();
    final Consumer<VarInitDeclaration> _function = (VarInitDeclaration v) -> {
      res.addAll(PromelaVarsHelper.getVar(v));
    };
    varDecls.forEach(_function);
    if (const_) {
      final Consumer<PromelaVar> _function_1 = (PromelaVar v) -> {
        v.setConstantTrue();
      };
      res.forEach(_function_1);
    }
    return res;
  }
  
  public static PromelaElementList<PromelaVar> getVars(final List<VarInitDeclaration> varDecls) {
    return PromelaVarsHelper.getVars(varDecls, false);
  }
  
  private static List<PromelaVar> getVar(final VarInitDeclaration varDecl) {
    final SimpleSpecificationInit simpleSpec = varDecl.getSpec();
    if ((simpleSpec != null)) {
      final String type = simpleSpec.getType().toUpperCase();
      final List<PromelaVar> res = new ArrayList<PromelaVar>();
      final Consumer<SymbolicVariable> _function = (SymbolicVariable v) -> {
        final PromelaVar simpleVar = PromelaVarsHelper.getPromelaSimpleVar(type, NamespaceContext.addId(v.getName()));
        final Expression varValue = simpleSpec.getValue();
        if ((varValue != null)) {
          simpleVar.setValue(PromelaExpressionsHelper.getExpr(varValue));
          if (("TIME".equals(type) && (varValue instanceof PrimaryExpression))) {
            ((PromelaVar.TimeInterval) simpleVar).setOriginalValue(
              ((PrimaryExpression) varValue).getConst().getTime().getInterval());
          }
        }
        res.add(simpleVar);
      };
      varDecl.getVarList().getVars().forEach(_function);
      return res;
    }
    final ArraySpecificationInit arrSpec = varDecl.getArrSpec();
    if ((arrSpec != null)) {
      throw new NotSupportedElementException();
    }
    throw new NotSupportedElementException();
  }
  
  private static PromelaVar getPromelaSimpleVar(final String type, final String name) {
    PromelaVar.TimeInterval _xblockexpression = null;
    {
      final PromelaContext context = PromelaContext.getContext();
      PromelaVar.TimeInterval _switchResult = null;
      if (type != null) {
        switch (type) {
          case "SINT":
            return new PromelaVar.Short(name);
          case "INT":
            return new PromelaVar.Short(name);
          case "DINT":
            return new PromelaVar.Int(name);
          case "LINT":
            throw new NotSupportedElementException();
          case "USINT":
            return new PromelaVar.Byte(name);
          case "UINT":
            return new PromelaVar.Unsigned(name, 16);
          case "UDINT":
            throw new NotSupportedElementException();
          case "ULINT":
            throw new NotSupportedElementException();
          case "REAL":
            throw new NotSupportedElementException();
          case "LREAL":
            throw new NotSupportedElementException();
          case "BOOL":
            return new PromelaVar.Bool(name);
          case "BYTE":
            return new PromelaVar.Byte(name);
          case "WORD":
            return new PromelaVar.Short(name);
          case "DWORD":
            return new PromelaVar.Int(name);
          case "LWORD":
            throw new NotSupportedElementException();
          case "TIME":
            _switchResult = context.addTimeVar(name);
            break;
          case "STRING":
            throw new NotSupportedElementException();
          case "WSTRING":
            throw new NotSupportedElementException();
          default:
            throw new UnknownElementException();
        }
      } else {
        throw new UnknownElementException();
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
}
