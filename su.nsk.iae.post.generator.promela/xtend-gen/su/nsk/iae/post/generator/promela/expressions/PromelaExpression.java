package su.nsk.iae.post.generator.promela.expressions;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import su.nsk.iae.post.generator.promela.context.CurrentContext;
import su.nsk.iae.post.generator.promela.context.NamespaceContext;
import su.nsk.iae.post.generator.promela.context.PostConstructContext;
import su.nsk.iae.post.generator.promela.context.PromelaContext;
import su.nsk.iae.post.generator.promela.model.IPromelaElement;
import su.nsk.iae.post.generator.promela.model.PromelaProcess;
import su.nsk.iae.post.poST.ProcessStatusExpression;

@SuppressWarnings("all")
public abstract class PromelaExpression implements IPromelaElement {
  public static class Constant extends PromelaExpression {
    protected String value;
    
    public Constant(final String value) {
      this.value = value;
    }
    
    @Override
    public String toText() {
      return this.value;
    }
  }
  
  public static class TimeConstant extends PromelaExpression {
    protected long value;
    
    public TimeConstant(final String interval) {
      this.value = 0;
      String curInterval = interval.replace("ms", "t");
      curInterval = this.addFirstPart(curInterval, "d", (((24 * 60) * 60) * 1000));
      curInterval = this.addFirstPart(curInterval, "h", ((60 * 60) * 1000));
      curInterval = this.addFirstPart(curInterval, "m", (60 * 1000));
      curInterval = this.addFirstPart(curInterval, "s", 1000);
      curInterval = this.addFirstPart(curInterval, "t", 1);
      curInterval = "";
    }
    
    public long setValue(final long value) {
      return this.value = value;
    }
    
    public long getValue() {
      return this.value;
    }
    
    @Override
    public String toText() {
      return String.valueOf(this.value);
    }
    
    private String addFirstPart(final String interval, final String suffix, final int multiplier) {
      boolean _isBlank = interval.isBlank();
      if (_isBlank) {
        return "";
      }
      final Function1<String, String> _function = (String s) -> {
        return s.trim();
      };
      final List<String> strs = ListExtensions.<String, String>map(((List<String>)Conversions.doWrapArray(interval.split(suffix))), _function);
      int _length = strs.get(0).length();
      int _length_1 = interval.length();
      boolean _lessThan = (_length < _length_1);
      if (_lessThan) {
        long _value = this.value;
        long _parseLong = Long.parseLong(strs.get(0));
        long _multiply = (_parseLong * multiplier);
        this.value = (_value + _multiply);
      }
      int _length_2 = ((Object[])Conversions.unwrapArray(strs, Object.class)).length;
      int _minus = (_length_2 - 1);
      return strs.get(_minus);
    }
  }
  
  public static class Var extends PromelaExpression {
    protected String name;
    
    public Var(final String name) {
      this.name = name;
    }
    
    public String getName() {
      return this.name;
    }
    
    @Override
    public String toText() {
      return NamespaceContext.getName(this.name);
    }
  }
  
  public static class Not extends PromelaExpression {
    protected PromelaExpression internal;
    
    public Not(final PromelaExpression internal) {
      this.internal = internal;
    }
    
    @Override
    public String toText() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("!");
      String _text = this.internal.toText();
      _builder.append(_text);
      return _builder.toString();
    }
  }
  
  public static class Invert extends PromelaExpression {
    protected PromelaExpression internal;
    
    public Invert(final PromelaExpression internal) {
      this.internal = internal;
    }
    
    @Override
    public String toText() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("-");
      String _text = this.internal.toText();
      _builder.append(_text);
      return _builder.toString();
    }
  }
  
  public static class Primary extends PromelaExpression {
    protected PromelaExpression internal;
    
    public Primary(final PromelaExpression internal) {
      this.internal = internal;
    }
    
    @Override
    public String toText() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("(");
      String _text = this.internal.toText();
      _builder.append(_text);
      _builder.append(")");
      return _builder.toString();
    }
  }
  
  public static class Binary extends PromelaExpression {
    private String opSymbol;
    
    private PromelaExpression left;
    
    private PromelaExpression right;
    
    public Binary(final PromelaExpression left, final String opSymbol, final PromelaExpression right) {
      this.opSymbol = opSymbol;
      this.left = left;
      this.right = right;
    }
    
    @Override
    public String toText() {
      StringConcatenation _builder = new StringConcatenation();
      String _text = this.left.toText();
      _builder.append(_text);
      _builder.append(" ");
      _builder.append(this.opSymbol);
      _builder.append(" ");
      String _text_1 = this.right.toText();
      _builder.append(_text_1);
      return _builder.toString();
    }
  }
  
  public static class ProcessStatus extends PromelaExpression implements PostConstructContext.IPostConstuctible {
    private String programName;
    
    private String processName;
    
    private boolean active;
    
    private boolean inactive;
    
    private boolean stop;
    
    private String processMtype;
    
    private String stopStateMType;
    
    private String errorStateMType;
    
    public ProcessStatus(final ProcessStatusExpression processStatusExpression) {
      this.active = processStatusExpression.isActive();
      this.inactive = processStatusExpression.isInactive();
      this.stop = processStatusExpression.isStop();
      this.programName = CurrentContext.getCurProgram().getShortName();
      this.processName = processStatusExpression.getProcess().getName();
      PostConstructContext.register(this);
    }
    
    @Override
    public void postConstruct() {
      final Function1<PromelaProcess, Boolean> _function = (PromelaProcess p) -> {
        return Boolean.valueOf((p.getProgramName().equals(this.programName) && p.getShortName().equals(this.processName)));
      };
      final PromelaProcess process = IterableExtensions.<PromelaProcess>findFirst(PromelaContext.getContext().getAllProcesses(), _function);
      this.processMtype = process.getNameMType();
      this.stopStateMType = process.getStopStateMType();
      this.errorStateMType = process.getErrorStateMtype();
    }
    
    @Override
    public String toText() {
      final String pMType = NamespaceContext.getName(this.processMtype);
      final String sMType = NamespaceContext.getName(this.stopStateMType);
      final String eMType = NamespaceContext.getName(this.errorStateMType);
      String _xifexpression = null;
      if (this.active) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("(");
        _builder.append(pMType);
        _builder.append(" != ");
        _builder.append(sMType);
        _builder.append(" && ");
        _builder.append(pMType);
        _builder.append(" != ");
        _builder.append(eMType);
        _builder.append(")");
        _xifexpression = _builder.toString();
      } else {
        String _xifexpression_1 = null;
        if (this.inactive) {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("(");
          _builder_1.append(pMType);
          _builder_1.append(" == ");
          _builder_1.append(sMType);
          _builder_1.append(" || ");
          _builder_1.append(pMType);
          _builder_1.append(" == ");
          _builder_1.append(eMType);
          _builder_1.append(")");
          _xifexpression_1 = _builder_1.toString();
        } else {
          String _xifexpression_2 = null;
          if (this.stop) {
            StringConcatenation _builder_2 = new StringConcatenation();
            _builder_2.append(pMType);
            _builder_2.append(" == ");
            _builder_2.append(sMType);
            _xifexpression_2 = _builder_2.toString();
          } else {
            StringConcatenation _builder_3 = new StringConcatenation();
            _builder_3.append(pMType);
            _builder_3.append(" == ");
            _builder_3.append(eMType);
            _xifexpression_2 = _builder_3.toString();
          }
          _xifexpression_1 = _xifexpression_2;
        }
        _xifexpression = _xifexpression_1;
      }
      return _xifexpression;
    }
  }
}
