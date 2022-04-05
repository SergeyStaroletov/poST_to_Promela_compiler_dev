package su.nsk.iae.post.generator.promela.context;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.promela.expressions.PromelaExpression;
import su.nsk.iae.post.generator.promela.model.PromelaProcess;
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar;

@SuppressWarnings("all")
public class PromelaContext {
  private static PromelaContext context;
  
  public static PromelaContext getContext() {
    if ((PromelaContext.context == null)) {
      PromelaContext _promelaContext = new PromelaContext();
      PromelaContext.context = _promelaContext;
    }
    return PromelaContext.context;
  }
  
  private List<PromelaVar.TimeInterval> timeVars = new ArrayList<PromelaVar.TimeInterval>();
  
  private List<PromelaExpression.TimeConstant> timeVals = new ArrayList<PromelaExpression.TimeConstant>();
  
  private List<PromelaProcess> allProcesses = new ArrayList<PromelaProcess>();
  
  public PromelaVar.TimeInterval addTimeVar(final String name) {
    final PromelaVar.TimeInterval res = new PromelaVar.TimeInterval(name);
    this.timeVars.add(res);
    return res;
  }
  
  public boolean addTimeVal(final PromelaExpression.TimeConstant value) {
    return this.timeVals.add(value);
  }
  
  public boolean addProcessToMType(final PromelaProcess process) {
    return this.allProcesses.add(process);
  }
  
  public CharSequence getMetadataAndInitText() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("mtype:P__ = {");
    _builder.newLine();
    {
      boolean _hasElements = false;
      for(final PromelaProcess process : this.allProcesses) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "\t");
        }
        _builder.append("\t");
        String _name = NamespaceContext.getName(process.getNameMType());
        _builder.append(_name, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.append("chan ");
    String _name_1 = NamespaceContext.getName("__currentProcess");
    _builder.append(_name_1);
    _builder.append(" = [1] of { mtype:P__ };");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      for(final PromelaProcess process_1 : this.allProcesses) {
        CharSequence _statesMTypeText = process_1.getStatesMTypeText();
        _builder.append(_statesMTypeText);
        _builder.newLineIfNotEmpty();
        {
          PromelaVar.TimeInterval _timeoutVar = process_1.getTimeoutVar();
          boolean _tripleNotEquals = (_timeoutVar != null);
          if (_tripleNotEquals) {
            String _text = process_1.getTimeoutVar().toText();
            _builder.append(_text);
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.newLine();
      }
    }
    _builder.append("init {");
    _builder.newLine();
    _builder.append("\t");
    String _name_2 = NamespaceContext.getName("__currentProcess");
    _builder.append(_name_2, "\t");
    _builder.append(" ! ");
    String _name_3 = NamespaceContext.getName(this.allProcesses.get(0).getNameMType());
    _builder.append(_name_3, "\t");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    return _builder;
  }
  
  public List<PromelaVar.TimeInterval> getTimeVars() {
    return this.timeVars;
  }
  
  public List<PromelaExpression.TimeConstant> getTimeVals() {
    return this.timeVals;
  }
  
  public List<PromelaProcess> getAllProcesses() {
    return this.allProcesses;
  }
}
