package su.nsk.iae.post.generator.promela.model.vars;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.promela.context.NamespaceContext;
import su.nsk.iae.post.generator.promela.exceptions.WrongModelStateException;
import su.nsk.iae.post.generator.promela.model.IPromelaElement;
import su.nsk.iae.post.generator.promela.model.expressions.PromelaExpression;

@SuppressWarnings("all")
public abstract class PromelaVar implements IPromelaElement {
  public static class Bool extends PromelaVar {
    public Bool(final String name) {
      super(name, "bool");
    }
  }
  
  public static class Short extends PromelaVar {
    private boolean wasSByte = false;
    
    public Short(final String name) {
      super(name, "short");
    }
    
    public Short(final String name, final boolean wasSByte) {
      super(name, "short");
      this.wasSByte = wasSByte;
    }
    
    public boolean getWasSByte() {
      return this.wasSByte;
    }
  }
  
  public static class Int extends PromelaVar {
    public Int(final String name) {
      super(name, "int");
    }
  }
  
  public static class Byte extends PromelaVar {
    public Byte(final String name) {
      super(name, "byte");
    }
  }
  
  public static class Unsigned extends PromelaVar {
    public Unsigned(final String name, final int bits) {
      super(name, "unsigned", (": " + Integer.valueOf(bits)));
    }
    
    public String setBits(final int bits) {
      return this.after = (": " + Integer.valueOf(bits));
    }
  }
  
  public static class TimeInterval extends PromelaVar {
    private String originalValue;
    
    private String valueAfterInterval;
    
    private boolean bitsSet = false;
    
    public TimeInterval(final String name) {
      super(name, "unsigned", ": 0");
    }
    
    public String setOriginalValue(final String originalValue) {
      return this.originalValue = originalValue;
    }
    
    public String setValueAfterInterval(final long interval) {
      String _xblockexpression = null;
      {
        long _value = new PromelaExpression.TimeConstant(this.originalValue).getValue();
        long _divide = (_value / interval);
        long ms = (_divide * interval);
        long s = (ms / 1000);
        long _ms = ms;
        ms = (_ms % 1000);
        long m = (s / 60);
        long _s = s;
        s = (_s % 60);
        long h = (m / 60);
        long _m = m;
        m = (_m % 60);
        long d = (h / 24);
        long _h = h;
        h = (_h % 24);
        String _xifexpression = null;
        if ((d > 0)) {
          _xifexpression = (Long.valueOf(d) + "d");
        } else {
          _xifexpression = "";
        }
        String _xifexpression_1 = null;
        if ((h > 0)) {
          _xifexpression_1 = (Long.valueOf(h) + "h");
        } else {
          _xifexpression_1 = "";
        }
        String _plus = (_xifexpression + _xifexpression_1);
        String _xifexpression_2 = null;
        if ((m > 0)) {
          _xifexpression_2 = (Long.valueOf(m) + "m");
        } else {
          _xifexpression_2 = "";
        }
        String _plus_1 = (_plus + _xifexpression_2);
        String _xifexpression_3 = null;
        if ((s > 0)) {
          _xifexpression_3 = (Long.valueOf(s) + "s");
        } else {
          _xifexpression_3 = "";
        }
        String _plus_2 = (_plus_1 + _xifexpression_3);
        String _xifexpression_4 = null;
        if ((ms > 0)) {
          _xifexpression_4 = (Long.valueOf(ms) + "ms");
        } else {
          _xifexpression_4 = "";
        }
        String _plus_3 = (_plus_2 + _xifexpression_4);
        _xblockexpression = this.valueAfterInterval = _plus_3;
      }
      return _xblockexpression;
    }
    
    public String setBits(final int bits) {
      String _xblockexpression = null;
      {
        this.bitsSet = true;
        _xblockexpression = this.after = (": " + Integer.valueOf(bits));
      }
      return _xblockexpression;
    }
    
    @Override
    public String toText() {
      String _xblockexpression = null;
      {
        if ((((!this.constant) && (!this.bitsSet)) && (!this.ignored))) {
          throw new WrongModelStateException();
        }
        StringConcatenation _builder = new StringConcatenation();
        {
          if ((((this.valueAfterInterval != null) && (this.originalValue != null)) && (!this.valueAfterInterval.equals(this.originalValue)))) {
            String _trim = super.toText().trim();
            _builder.append(_trim);
            _builder.append(" //");
            _builder.append(this.originalValue);
            _builder.append(" -> ");
            _builder.append(this.valueAfterInterval);
            _builder.newLineIfNotEmpty();
          } else {
            if ((this.originalValue != null)) {
              String _trim_1 = super.toText().trim();
              _builder.append(_trim_1);
              _builder.append(" //");
              _builder.append(this.originalValue);
              _builder.newLineIfNotEmpty();
            } else {
              String _text = super.toText();
              _builder.append(_text);
              _builder.newLineIfNotEmpty();
            }
          }
        }
        _xblockexpression = _builder.toString();
      }
      return _xblockexpression;
    }
  }
  
  public static class VarProxy extends PromelaVar {
    private PromelaVar v = null;
    
    public VarProxy(final String name, final String type) {
      super(name, type);
    }
    
    public PromelaVar setVar(final PromelaVar v) {
      return this.v = v;
    }
    
    @Override
    public String toText() {
      if (this.ignored) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("//ignored: ");
        String _xifexpression = null;
        if ((this.v == null)) {
          _xifexpression = this.name;
        } else {
          _xifexpression = this.v.name;
        }
        String _name = NamespaceContext.getName(_xifexpression);
        _builder.append(_name);
        _builder.newLineIfNotEmpty();
        return _builder.toString();
      }
      if ((this.v == null)) {
        throw new WrongModelStateException();
      } else {
        return this.v.toText();
      }
    }
  }
  
  public static class Array extends PromelaVar {
    private int firstIndex;
    
    private int length;
    
    private List<PromelaExpression> values;
    
    public Array(final String name, final String typeName, final int firstIndex, final int lastIndex, final List<PromelaExpression> values) {
      super(name, typeName);
      this.firstIndex = firstIndex;
      this.length = ((lastIndex - firstIndex) + 1);
      this.values = values;
    }
    
    @Override
    public String toText() {
      String _xblockexpression = null;
      {
        final String name = NamespaceContext.getName(this.name);
        StringConcatenation _builder = new StringConcatenation();
        {
          if ((this.values != null)) {
            _builder.append(this.typeName);
            _builder.append(" ");
            _builder.append(name);
            _builder.append("[");
            _builder.append(this.length);
            _builder.append("] = {");
            {
              boolean _hasElements = false;
              for(final PromelaExpression v : this.values) {
                if (!_hasElements) {
                  _hasElements = true;
                } else {
                  _builder.appendImmediate(", ", "");
                }
                String _text = v.toText();
                _builder.append(_text);
              }
            }
            _builder.append("};");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append(this.typeName);
            _builder.append(" ");
            _builder.append(name);
            _builder.append("[");
            _builder.append(this.length);
            _builder.append("];");
            _builder.newLineIfNotEmpty();
          }
        }
        _xblockexpression = _builder.toString();
      }
      return _xblockexpression;
    }
    
    public int getFirstIndex() {
      return this.firstIndex;
    }
  }
  
  protected String name;
  
  protected String typeName;
  
  protected String after;
  
  protected boolean constant;
  
  protected PromelaExpression value;
  
  protected boolean ignored = false;
  
  public PromelaVar(final String name, final String typeName, final String after) {
    this.name = name;
    this.typeName = typeName;
    this.after = after;
  }
  
  public PromelaVar(final String name, final String typeName) {
    this(name, typeName, null);
  }
  
  public PromelaVar setConstantTrue() {
    this.constant = true;
    return this;
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean isConstant() {
    return this.constant;
  }
  
  public PromelaVar setValue(final PromelaExpression value) {
    this.value = value;
    return this;
  }
  
  public boolean setIgnoredTrue() {
    return this.ignored = true;
  }
  
  public PromelaExpression getValue() {
    return this.value;
  }
  
  @Override
  public String toText() {
    String _xifexpression = null;
    if (this.ignored) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("//ignored: ");
      String _name = NamespaceContext.getName(this.name);
      _builder.append(_name);
      _builder.newLineIfNotEmpty();
      _xifexpression = _builder.toString();
    } else {
      String _xifexpression_1 = null;
      if ((this.value != null)) {
        StringConcatenation _builder_1 = new StringConcatenation();
        {
          if (this.constant) {
            _builder_1.append("#define ");
            String _name_1 = NamespaceContext.getName(this.name);
            _builder_1.append(_name_1);
            _builder_1.append(" ");
            String _text = this.value.toText();
            _builder_1.append(_text);
            _builder_1.newLineIfNotEmpty();
          } else {
            if ((this.after != null)) {
              _builder_1.append(this.typeName);
              _builder_1.append(" ");
              String _name_2 = NamespaceContext.getName(this.name);
              _builder_1.append(_name_2);
              _builder_1.append(" ");
              _builder_1.append(this.after);
              _builder_1.append(" = ");
              String _text_1 = this.value.toText();
              _builder_1.append(_text_1);
              _builder_1.append(";");
              _builder_1.newLineIfNotEmpty();
            } else {
              _builder_1.append(this.typeName);
              _builder_1.append(" ");
              String _name_3 = NamespaceContext.getName(this.name);
              _builder_1.append(_name_3);
              _builder_1.append(" = ");
              String _text_2 = this.value.toText();
              _builder_1.append(_text_2);
              _builder_1.append(";");
              _builder_1.newLineIfNotEmpty();
            }
          }
        }
        _xifexpression_1 = _builder_1.toString();
      } else {
        StringConcatenation _builder_2 = new StringConcatenation();
        {
          if (this.constant) {
            _builder_2.append("#define ");
            String _name_4 = NamespaceContext.getName(this.name);
            _builder_2.append(_name_4);
            _builder_2.newLineIfNotEmpty();
          } else {
            if ((this.after != null)) {
              _builder_2.append(this.typeName);
              _builder_2.append(" ");
              String _name_5 = NamespaceContext.getName(this.name);
              _builder_2.append(_name_5);
              _builder_2.append(" ");
              _builder_2.append(this.after);
              _builder_2.append(";");
              _builder_2.newLineIfNotEmpty();
            } else {
              _builder_2.append(this.typeName);
              _builder_2.append(" ");
              String _name_6 = NamespaceContext.getName(this.name);
              _builder_2.append(_name_6);
              _builder_2.append(";");
              _builder_2.newLineIfNotEmpty();
            }
          }
        }
        _xifexpression_1 = _builder_2.toString();
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
}
