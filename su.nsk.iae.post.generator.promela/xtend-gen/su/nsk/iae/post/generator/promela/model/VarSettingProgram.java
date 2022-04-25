package su.nsk.iae.post.generator.promela.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import su.nsk.iae.post.generator.promela.context.NamespaceContext;
import su.nsk.iae.post.generator.promela.exceptions.NotSupportedElementException;
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar;

@SuppressWarnings("all")
public class VarSettingProgram implements IPromelaElement {
  private static final String processPrefix = "specialProcess";
  
  private static final String mTypePrefix = "sP";
  
  private static final String helperProcessId = "Helper";
  
  private static final String gremlinProcessId = "Gremlin";
  
  private static final String varsSetterProcessId = "VarsSetter";
  
  private String firstProcessMType;
  
  private final ArrayList<Supplier<String>> gremlinTextSuppliers = new ArrayList<Supplier<String>>();
  
  private final HashMap<String, List<String>> outputToInputVars = new HashMap<String, List<String>>();
  
  private String helperProcessFullId;
  
  private String helperMTypeFullId;
  
  private String gremlinProcessFullId;
  
  private String gremlinMTypeFullId;
  
  private String varsSetterProcessFullId;
  
  private String varsSetterMTypeFullId;
  
  public VarSettingProgram() {
    this.helperProcessFullId = NamespaceContext.addId(VarSettingProgram.helperProcessId, VarSettingProgram.processPrefix);
    this.helperMTypeFullId = NamespaceContext.addId(VarSettingProgram.helperProcessId, VarSettingProgram.mTypePrefix);
  }
  
  public String setFirstProcess(final String firstProcessMType) {
    return this.firstProcessMType = firstProcessMType;
  }
  
  public String addGremlinVar(final PromelaVar v) {
    String _xblockexpression = null;
    {
      final String fullId = v.getName();
      if ((v instanceof PromelaVar.Bool)) {
        final Supplier<String> _function = () -> {
          String _xblockexpression_1 = null;
          {
            final String name = NamespaceContext.getName(fullId);
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("if");
            _builder.newLine();
            _builder.append("\t");
            _builder.append(":: ");
            _builder.append(name, "\t");
            _builder.append(" = true;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append(":: ");
            _builder.append(name, "\t");
            _builder.append(" = false;");
            _builder.newLineIfNotEmpty();
            _builder.append("fi;");
            _builder.newLine();
            _xblockexpression_1 = _builder.toString();
          }
          return _xblockexpression_1;
        };
        this.gremlinTextSuppliers.add(_function);
      } else {
        if ((v instanceof PromelaVar.Byte)) {
          final Supplier<String> _function_1 = () -> {
            String _xblockexpression_1 = null;
            {
              final String name = NamespaceContext.getName(fullId);
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("select (");
              _builder.append(name);
              _builder.append(" : 0..255);");
              _builder.newLineIfNotEmpty();
              _xblockexpression_1 = _builder.toString();
            }
            return _xblockexpression_1;
          };
          this.gremlinTextSuppliers.add(_function_1);
        } else {
          if ((v instanceof PromelaVar.Short)) {
            boolean _wasSByte = ((PromelaVar.Short)v).getWasSByte();
            if (_wasSByte) {
              final Supplier<String> _function_2 = () -> {
                String _xblockexpression_1 = null;
                {
                  final String name = NamespaceContext.getName(fullId);
                  StringConcatenation _builder = new StringConcatenation();
                  _builder.append("select (");
                  _builder.append(name);
                  _builder.append(" : -128..127);");
                  _builder.newLineIfNotEmpty();
                  _xblockexpression_1 = _builder.toString();
                }
                return _xblockexpression_1;
              };
              this.gremlinTextSuppliers.add(_function_2);
            } else {
              String _string = ((PromelaVar.Short)v).getClass().toString();
              String _plus = (_string + " as gremlin variable");
              throw new NotSupportedElementException(_plus);
            }
          } else {
            String _string_1 = v.getClass().toString();
            String _plus_1 = (_string_1 + " as gremlin variable");
            throw new NotSupportedElementException(_plus_1);
          }
        }
      }
      String _xifexpression = null;
      if ((this.gremlinProcessFullId == null)) {
        String _xblockexpression_1 = null;
        {
          this.gremlinProcessFullId = NamespaceContext.addId(VarSettingProgram.gremlinProcessId, VarSettingProgram.processPrefix);
          _xblockexpression_1 = this.gremlinMTypeFullId = NamespaceContext.addId(VarSettingProgram.gremlinProcessId, VarSettingProgram.mTypePrefix);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String addOutputToInputAssignments(final String outFullId, final List<String> inFullIds) {
    String _xblockexpression = null;
    {
      final Function<String, List<String>> _function = (String it) -> {
        return new ArrayList<String>();
      };
      this.outputToInputVars.computeIfAbsent(outFullId, _function).addAll(inFullIds);
      String _xifexpression = null;
      if ((this.varsSetterProcessFullId == null)) {
        String _xblockexpression_1 = null;
        {
          this.varsSetterProcessFullId = NamespaceContext.addId(VarSettingProgram.varsSetterProcessId, VarSettingProgram.processPrefix);
          _xblockexpression_1 = this.varsSetterMTypeFullId = NamespaceContext.addId(VarSettingProgram.varsSetterProcessId, VarSettingProgram.mTypePrefix);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public ArrayList<String> getProcessMTypes() {
    final ArrayList<String> res = new ArrayList<String>();
    boolean _isEmpty = this.gremlinTextSuppliers.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      res.add(this.gremlinMTypeFullId);
    }
    boolean _isEmpty_1 = this.outputToInputVars.isEmpty();
    boolean _not_1 = (!_isEmpty_1);
    if (_not_1) {
      res.add(this.varsSetterMTypeFullId);
    }
    res.add(this.helperMTypeFullId);
    return res;
  }
  
  @Override
  public String toText() {
    String _xblockexpression = null;
    {
      final ArrayList<String> mtypes = this.getProcessMTypes();
      final String afterGremlinMType = this.getMTypeAfter(this.gremlinMTypeFullId, mtypes);
      final String afterVarsSetterMType = this.getMTypeAfter(this.varsSetterMTypeFullId, mtypes);
      final String afterHelperMType = this.getMTypeAfter(this.helperMTypeFullId, mtypes);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("//-----------------------------------------------------------------------------");
      _builder.newLine();
      _builder.append("//-----------------------------------------------------------------------------");
      _builder.newLine();
      _builder.append("//special processes");
      _builder.newLine();
      _builder.append("//-----------------------------------------------------------------------------");
      _builder.newLine();
      _builder.append("//-----------------------------------------------------------------------------");
      _builder.newLine();
      _builder.newLine();
      {
        boolean _isEmpty = this.gremlinTextSuppliers.isEmpty();
        boolean _not = (!_isEmpty);
        if (_not) {
          _builder.append("active proctype ");
          String _name = NamespaceContext.getName(this.gremlinProcessFullId);
          _builder.append(_name);
          _builder.append("() {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("do :: __currentProcess ? ");
          String _name_1 = NamespaceContext.getName(this.gremlinMTypeFullId);
          _builder.append(_name_1, "\t");
          _builder.append(" ->");
          _builder.newLineIfNotEmpty();
          _builder.append("\t\t");
          _builder.append("atomic {");
          _builder.newLine();
          {
            for(final Supplier<String> gremlinTextSupplier : this.gremlinTextSuppliers) {
              _builder.append("\t\t\t");
              String _get = gremlinTextSupplier.get();
              _builder.append(_get, "\t\t\t");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.append("\t\t\t");
          _builder.append("__currentProcess ! ");
          String _name_2 = NamespaceContext.getName(afterGremlinMType);
          _builder.append(_name_2, "\t\t\t");
          _builder.append(";");
          _builder.newLineIfNotEmpty();
          _builder.append("\t\t");
          _builder.append("}");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("od;");
          _builder.newLine();
          _builder.append("}");
          _builder.newLine();
        }
      }
      _builder.newLine();
      {
        boolean _isEmpty_1 = this.outputToInputVars.isEmpty();
        boolean _not_1 = (!_isEmpty_1);
        if (_not_1) {
          _builder.append("active proctype ");
          String _name_3 = NamespaceContext.getName(this.varsSetterProcessFullId);
          _builder.append(_name_3);
          _builder.append("() {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("do :: __currentProcess ? ");
          String _name_4 = NamespaceContext.getName(this.varsSetterMTypeFullId);
          _builder.append(_name_4, "\t");
          _builder.append(" ->");
          _builder.newLineIfNotEmpty();
          _builder.append("\t\t");
          _builder.append("atomic {");
          _builder.newLine();
          {
            Set<Map.Entry<String, List<String>>> _entrySet = this.outputToInputVars.entrySet();
            for(final Map.Entry<String, List<String>> outToIns : _entrySet) {
              {
                List<String> _value = outToIns.getValue();
                for(final String in : _value) {
                  _builder.append("\t\t\t");
                  String _name_5 = NamespaceContext.getName(in);
                  _builder.append(_name_5, "\t\t\t");
                  _builder.append(" = ");
                  String _name_6 = NamespaceContext.getName(outToIns.getKey());
                  _builder.append(_name_6, "\t\t\t");
                  _builder.append(";");
                  {
                    boolean _isNamesWithNumbersMode = NamespaceContext.isNamesWithNumbersMode();
                    if (_isNamesWithNumbersMode) {
                      _builder.append(" //");
                      String _nameWithNamespaces = NamespaceContext.getNameWithNamespaces(in);
                      _builder.append(_nameWithNamespaces, "\t\t\t");
                      _builder.append(" <- ");
                      String _nameWithNamespaces_1 = NamespaceContext.getNameWithNamespaces(outToIns.getKey());
                      _builder.append(_nameWithNamespaces_1, "\t\t\t");
                    }
                  }
                  _builder.newLineIfNotEmpty();
                }
              }
            }
          }
          _builder.append("\t\t\t");
          _builder.append("__currentProcess ! ");
          String _name_7 = NamespaceContext.getName(afterVarsSetterMType);
          _builder.append(_name_7, "\t\t\t");
          _builder.append(";");
          _builder.newLineIfNotEmpty();
          _builder.append("\t\t");
          _builder.append("}");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("od;");
          _builder.newLine();
          _builder.append("}");
          _builder.newLine();
        }
      }
      _builder.newLine();
      _builder.append("bool cycle__u;");
      _builder.newLine();
      _builder.append("active proctype ");
      String _name_8 = NamespaceContext.getName(this.helperProcessFullId);
      _builder.append(_name_8);
      _builder.append("() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("do :: __currentProcess ? ");
      String _name_9 = NamespaceContext.getName(this.helperMTypeFullId);
      _builder.append(_name_9, "\t");
      _builder.append(" ->");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("cycle__u = true;");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("atomic {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("cycle__u = false;");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("__currentProcess ! ");
      String _name_10 = NamespaceContext.getName(afterHelperMType);
      _builder.append(_name_10, "\t\t\t");
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("od;");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  private String getMTypeAfter(final String mtypeFullId, final List<String> mtypeFullIds) {
    final int index = mtypeFullIds.indexOf(mtypeFullId);
    String _xifexpression = null;
    if (((index != (-1)) && ((index + 1) < ((Object[])Conversions.unwrapArray(mtypeFullIds, Object.class)).length))) {
      _xifexpression = mtypeFullIds.get((index + 1));
    } else {
      _xifexpression = this.firstProcessMType;
    }
    return _xifexpression;
  }
}
