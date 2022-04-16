package su.nsk.iae.post.generator.promela.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.promela.context.NamespaceContext;
import su.nsk.iae.post.generator.promela.exceptions.NotSupportedElementException;
import su.nsk.iae.post.generator.promela.exceptions.WrongModelStateException;
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar;

@SuppressWarnings("all")
public class VarSettingProgram implements IPromelaElement {
  private static final String processPrefix = "specialProcess";
  
  private static final String mTypePrefix = "sP";
  
  private static final String gremlinProcessId = "Gremlin";
  
  private static final String varsSetterProcessId = "VarsSetter";
  
  private String firstProcessMType;
  
  private final ArrayList<Supplier<String>> gremlinTextSuppliers = new ArrayList<Supplier<String>>();
  
  private final HashMap<String, List<String>> outputToInputVars = new HashMap<String, List<String>>();
  
  private String gremlinProcessFullId;
  
  private String gremlinMTypeFullId;
  
  private String varsSetterProcessFullId;
  
  private String varsSetterMTypeFullId;
  
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
              throw new NotSupportedElementException();
            }
          } else {
            throw new NotSupportedElementException();
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
    boolean _isEmpty_2 = res.isEmpty();
    if (_isEmpty_2) {
      throw new WrongModelStateException();
    }
    return res;
  }
  
  @Override
  public String toText() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//-----------------------------------------------------------------------------");
    _builder.newLine();
    _builder.append("//-----------------------------------------------------------------------------");
    _builder.newLine();
    _builder.append("//input, output, inout vars setting program");
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
        {
          boolean _isEmpty_1 = this.outputToInputVars.isEmpty();
          boolean _not_1 = (!_isEmpty_1);
          if (_not_1) {
            _builder.append("\t\t\t");
            _builder.append("__currentProcess ! ");
            String _name_2 = NamespaceContext.getName(this.varsSetterMTypeFullId);
            _builder.append(_name_2, "\t\t\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t\t\t");
            _builder.append("__currentProcess ! ");
            String _name_3 = NamespaceContext.getName(this.firstProcessMType);
            _builder.append(_name_3, "\t\t\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
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
      boolean _isEmpty_2 = this.outputToInputVars.isEmpty();
      boolean _not_2 = (!_isEmpty_2);
      if (_not_2) {
        _builder.append("active proctype ");
        String _name_4 = NamespaceContext.getName(this.varsSetterProcessFullId);
        _builder.append(_name_4);
        _builder.append("() {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("do :: __currentProcess ? ");
        String _name_5 = NamespaceContext.getName(this.varsSetterMTypeFullId);
        _builder.append(_name_5, "\t");
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
                String _name_6 = NamespaceContext.getName(in);
                _builder.append(_name_6, "\t\t\t");
                _builder.append(" = ");
                String _name_7 = NamespaceContext.getName(outToIns.getKey());
                _builder.append(_name_7, "\t\t\t");
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
        String _name_8 = NamespaceContext.getName(this.firstProcessMType);
        _builder.append(_name_8, "\t\t\t");
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
    return _builder.toString();
  }
}
