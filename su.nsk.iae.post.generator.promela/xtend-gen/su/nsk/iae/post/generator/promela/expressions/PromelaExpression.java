package su.nsk.iae.post.generator.promela.expressions;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import su.nsk.iae.post.generator.promela.model.IPromelaElement;

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
      return this.name;
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
}
