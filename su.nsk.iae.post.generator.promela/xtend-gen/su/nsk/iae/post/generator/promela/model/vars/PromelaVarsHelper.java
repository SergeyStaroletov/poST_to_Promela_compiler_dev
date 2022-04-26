package su.nsk.iae.post.generator.promela.model.vars;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import su.nsk.iae.post.generator.promela.context.NamespaceContext;
import su.nsk.iae.post.generator.promela.context.PromelaContext;
import su.nsk.iae.post.generator.promela.context.WarningsContext;
import su.nsk.iae.post.generator.promela.exceptions.NotSupportedElementException;
import su.nsk.iae.post.generator.promela.exceptions.UnknownElementException;
import su.nsk.iae.post.generator.promela.model.PromelaElementList;
import su.nsk.iae.post.generator.promela.model.expressions.PromelaExpression;
import su.nsk.iae.post.generator.promela.model.expressions.PromelaExpressionsHelper;
import su.nsk.iae.post.poST.ArrayInitialization;
import su.nsk.iae.post.poST.ArrayInterval;
import su.nsk.iae.post.poST.ArraySpecificationInit;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.IntegerLiteral;
import su.nsk.iae.post.poST.NumericLiteral;
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
      final Expression varValue = simpleSpec.getValue();
      final List<PromelaVar> res = new ArrayList<PromelaVar>();
      final Consumer<SymbolicVariable> _function = (SymbolicVariable v) -> {
        final PromelaVar simpleVar = PromelaVarsHelper.getPromelaSimpleVar(type, NamespaceContext.addId(v.getName()));
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
      ArrayInterval _interval = arrSpec.getInit().getInterval();
      boolean _tripleEquals = (_interval == null);
      if (_tripleEquals) {
        throw new NotSupportedElementException("Array with no interval specified");
      }
      int intervalStart = (-1);
      int intervalEnd = (-1);
      final Expression intervalStartExpr = arrSpec.getInit().getInterval().getStart();
      final Expression intervalEndExpr = arrSpec.getInit().getInterval().getEnd();
      if ((intervalStartExpr instanceof PrimaryExpression)) {
        if ((intervalEndExpr instanceof PrimaryExpression)) {
          if (((((((PrimaryExpression)intervalStartExpr).getConst() != null) && (((PrimaryExpression)intervalEndExpr).getConst() != null)) && (((PrimaryExpression)intervalStartExpr).getConst().getNum() != null)) && (((PrimaryExpression)intervalEndExpr).getConst().getNum() != null))) {
            final NumericLiteral start = ((PrimaryExpression)intervalStartExpr).getConst().getNum();
            final NumericLiteral end = ((PrimaryExpression)intervalEndExpr).getConst().getNum();
            if ((start instanceof IntegerLiteral)) {
              if ((end instanceof IntegerLiteral)) {
                intervalStart = (Integer.valueOf(((IntegerLiteral)start).getValue().getValue())).intValue();
                intervalEnd = (Integer.valueOf(((IntegerLiteral)end).getValue().getValue())).intValue();
              }
            }
          }
        }
      }
      if (((intervalStart == (-1)) || (intervalEnd == (-1)))) {
        throw new NotSupportedElementException("Array with interval specified by expressions more complex than numbers");
      }
      final String typeName = PromelaVarsHelper.postToPromelaTypeNameForArray(arrSpec.getInit().getType());
      List<PromelaExpression> _xifexpression = null;
      ArrayInitialization _values = arrSpec.getValues();
      boolean _tripleEquals_1 = (_values == null);
      if (_tripleEquals_1) {
        _xifexpression = null;
      } else {
        final Function1<Expression, PromelaExpression> _function_1 = (Expression e) -> {
          return PromelaExpressionsHelper.getExpr(e);
        };
        _xifexpression = ListExtensions.<Expression, PromelaExpression>map(arrSpec.getValues().getElements(), _function_1);
      }
      final List<PromelaExpression> initValueExpressions = _xifexpression;
      final int start_1 = intervalStart;
      final int end_1 = intervalEnd;
      final List<PromelaVar> res_1 = new ArrayList<PromelaVar>();
      final Consumer<SymbolicVariable> _function_2 = (SymbolicVariable v) -> {
        final String fullName = NamespaceContext.addId(v.getName());
        final PromelaVar.Array arrayVariable = new PromelaVar.Array(fullName, typeName, start_1, end_1, initValueExpressions);
        PromelaContext.getContext().addArrayVar(arrayVariable);
        res_1.add(arrayVariable);
      };
      varDecl.getVarList().getVars().forEach(_function_2);
      return res_1;
    }
    throw new NotSupportedElementException((("VarInitDeclaration which is not simple or array variable (" + varDecl) + ")"));
  }
  
  private static PromelaVar getPromelaSimpleVar(final String type, final String name) {
    PromelaVar.TimeInterval _xblockexpression = null;
    {
      final PromelaContext context = PromelaContext.getContext();
      PromelaVar.TimeInterval _switchResult = null;
      if (type != null) {
        switch (type) {
          case "SINT":
            return new PromelaVar.Short(name, true);
          case "INT":
            return new PromelaVar.Short(name);
          case "DINT":
            return new PromelaVar.Int(name);
          case "LINT":
            PromelaVar.Int _int = new PromelaVar.Int(name);
            return PromelaVarsHelper.<PromelaVar.Int>notSupportedType(_int, "LINT", "DINT");
          case "USINT":
            return new PromelaVar.Byte(name);
          case "UINT":
            return new PromelaVar.Unsigned(name, 16);
          case "UDINT":
            PromelaVar.Int _int_1 = new PromelaVar.Int(name);
            return PromelaVarsHelper.<PromelaVar.Int>notSupportedType(_int_1, "UDINT", "DINT");
          case "ULINT":
            PromelaVar.Int _int_2 = new PromelaVar.Int(name);
            return PromelaVarsHelper.<PromelaVar.Int>notSupportedType(_int_2, "ULINT", "DINT");
          case "REAL":
            PromelaVar.Int _int_3 = new PromelaVar.Int(name);
            return PromelaVarsHelper.<PromelaVar.Int>notSupportedType(_int_3, "REAL", "DINT");
          case "LREAL":
            PromelaVar.Int _int_4 = new PromelaVar.Int(name);
            return PromelaVarsHelper.<PromelaVar.Int>notSupportedType(_int_4, "LREAL", "DINT");
          case "BOOL":
            return new PromelaVar.Bool(name);
          case "BYTE":
            return new PromelaVar.Byte(name);
          case "WORD":
            return new PromelaVar.Short(name);
          case "DWORD":
            return new PromelaVar.Int(name);
          case "LWORD":
            PromelaVar.Int _int_5 = new PromelaVar.Int(name);
            return PromelaVarsHelper.<PromelaVar.Int>notSupportedType(_int_5, "LWORD", "DWORD");
          case "TIME":
            _switchResult = context.addTimeVar(name);
            break;
          case "STRING":
            throw new NotSupportedElementException("poST variable of type STRING");
          case "WSTRING":
            throw new NotSupportedElementException("poST variable of type WSTRING");
          default:
            throw new UnknownElementException(("poST variable of type " + type));
        }
      } else {
        throw new UnknownElementException(("poST variable of type " + type));
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
  
  private static <T extends PromelaVar> T notSupportedType(final T v, final String oldType, final String newType) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type ");
    _builder.append(oldType);
    _builder.append(" is not fully supported. Variable ");
    _builder.append(v.name);
    _builder.append(" registered as of type ");
    _builder.append(newType);
    WarningsContext.addWarning(_builder.toString());
    return v;
  }
  
  private static String postToPromelaTypeNameForArray(final String type) {
    boolean _equals = "SINT".equals(type);
    if (_equals) {
      WarningsContext.addWarning("Type ARRAY OF SINT can only be translated to byte[] which are 8-bit instead of 16-bit");
      return "byte";
    }
    boolean _equals_1 = "TIME".equals(type);
    if (_equals_1) {
      throw new NotSupportedElementException("Array of TIME variables");
    }
    return PromelaVarsHelper.getPromelaSimpleVar(type, null).typeName;
  }
}
