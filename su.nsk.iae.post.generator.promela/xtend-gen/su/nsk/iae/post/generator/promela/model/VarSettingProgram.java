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
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar;

@SuppressWarnings("all")
public class VarSettingProgram implements IPromelaElement {
  private static final String gremlinMType = "sP__Gremlin";
  
  private static final String varsSetterMType = "sP__VarsSetter";
  
  private String firstProcessMType;
  
  private final ArrayList<Supplier<String>> gremlinTextSuppliers = new ArrayList<Supplier<String>>();
  
  private final HashMap<String, List<String>> outputToInputVars = new HashMap<String, List<String>>();
  
  public String setFirstProcess(final String firstProcessMType) {
    return this.firstProcessMType = firstProcessMType;
  }
  
  public boolean addGremlinVar(final PromelaVar v) {
    boolean _xblockexpression = false;
    {
      final String fullId = v.getName();
      boolean _xifexpression = false;
      if ((v instanceof PromelaVar.Bool)) {
        final Supplier<String> _function = () -> {
          String _xblockexpression_1 = null;
          {
            final String name = NamespaceContext.getName(fullId);
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("if");
            _builder.newLine();
            _builder.append("\t");
            _builder.append(":: true -> ");
            _builder.append(name, "\t");
            _builder.append(" = true;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append(":: true -> ");
            _builder.append(name, "\t");
            _builder.append(" = false;");
            _builder.newLineIfNotEmpty();
            _builder.append("fi;");
            _builder.newLine();
            _xblockexpression_1 = _builder.toString();
          }
          return _xblockexpression_1;
        };
        _xifexpression = this.gremlinTextSuppliers.add(_function);
      } else {
        throw new NotSupportedElementException();
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public boolean addOutputToInputAssignments(final String outFullId, final List<String> inFullIds) {
    final Function<String, List<String>> _function = (String it) -> {
      return new ArrayList<String>();
    };
    return this.outputToInputVars.computeIfAbsent(outFullId, _function).addAll(inFullIds);
  }
  
  public ArrayList<String> getProcessMTypes() {
    final ArrayList<String> res = new ArrayList<String>();
    boolean _isEmpty = this.gremlinTextSuppliers.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      res.add(VarSettingProgram.gremlinMType);
    }
    boolean _isEmpty_1 = this.outputToInputVars.isEmpty();
    boolean _not_1 = (!_isEmpty_1);
    if (_not_1) {
      res.add(VarSettingProgram.varsSetterMType);
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
        _builder.append("active proctype specialProcess__Gremlin() {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("do :: __currentProcess ? ");
        _builder.append(VarSettingProgram.gremlinMType, "\t");
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
            _builder.append(VarSettingProgram.varsSetterMType, "\t\t\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t\t\t");
            _builder.append("__currentProcess ! ");
            String _name = NamespaceContext.getName(this.firstProcessMType);
            _builder.append(_name, "\t\t\t");
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
        _builder.append("active proctype specialProcess__VarsSetter() {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("do :: __currentProcess ? ");
        _builder.append(VarSettingProgram.varsSetterMType, "\t");
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
                String _name_1 = NamespaceContext.getName(in);
                _builder.append(_name_1, "\t\t\t");
                _builder.append(" = ");
                String _name_2 = NamespaceContext.getName(outToIns.getKey());
                _builder.append(_name_2, "\t\t\t");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
        _builder.append("\t\t\t");
        _builder.append("__currentProcess ! ");
        String _name_3 = NamespaceContext.getName(this.firstProcessMType);
        _builder.append(_name_3, "\t\t\t");
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
