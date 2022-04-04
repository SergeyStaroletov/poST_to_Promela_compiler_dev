package su.nsk.iae.post.generator.promela.model.vars;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.promela.expressions.PromelaExpression;
import su.nsk.iae.post.generator.promela.model.IPromelaElement;
import su.nsk.iae.post.generator.promela.model.WrongModelStateException;

@SuppressWarnings("all")
public abstract class PromelaVar implements IPromelaElement {
  public static class Bool extends PromelaVar {
    public Bool(final String name) {
      super(name, "bool");
    }
  }
  
  public static class Short extends PromelaVar {
    public Short(final String name) {
      super(name, "short");
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
    
    private boolean bitsSet = false;
    
    public TimeInterval(final String name) {
      super(name, "unsigned", ": 0");
    }
    
    public String setOriginalValue(final String originalValue) {
      return this.originalValue = originalValue;
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
          if ((this.originalValue != null)) {
            String _trim = super.toText().trim();
            _builder.append(_trim);
            _builder.append(" //");
            _builder.append(this.originalValue);
            _builder.newLineIfNotEmpty();
          } else {
            String _text = super.toText();
            _builder.append(_text);
            _builder.newLineIfNotEmpty();
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
        _builder.append(_xifexpression);
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
  
  @Override
  public String toText() {
    String _xifexpression = null;
    if (this.ignored) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("//ignored: ");
      _builder.append(this.name);
      _builder.newLineIfNotEmpty();
      _xifexpression = _builder.toString();
    } else {
      String _xifexpression_1 = null;
      if ((this.value != null)) {
        StringConcatenation _builder_1 = new StringConcatenation();
        {
          if (this.constant) {
            _builder_1.append("#define ");
            _builder_1.append(this.name);
            _builder_1.append(" ");
            String _text = this.value.toText();
            _builder_1.append(_text);
            _builder_1.newLineIfNotEmpty();
          } else {
            if ((this.after != null)) {
              _builder_1.append(this.typeName);
              _builder_1.append(" ");
              _builder_1.append(this.name);
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
              _builder_1.append(this.name);
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
            _builder_2.append(this.name);
            _builder_2.newLineIfNotEmpty();
          } else {
            if ((this.after != null)) {
              _builder_2.append(this.typeName);
              _builder_2.append(" ");
              _builder_2.append(this.name);
              _builder_2.append(" ");
              _builder_2.append(this.after);
              _builder_2.append(";");
              _builder_2.newLineIfNotEmpty();
            } else {
              _builder_2.append(this.typeName);
              _builder_2.append(" ");
              _builder_2.append(this.name);
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
