package su.nsk.iae.post.generator.promela.model;

import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import su.nsk.iae.post.generator.promela.PromelaContext;
import su.nsk.iae.post.generator.promela.context.PostConstructContext;
import su.nsk.iae.post.generator.promela.expressions.PromelaExpression;
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar;
import su.nsk.iae.post.generator.promela.statements.PromelaStatement;
import su.nsk.iae.post.poST.Model;
import su.nsk.iae.post.poST.Program;

@SuppressWarnings("all")
public class PromelaModel implements IPromelaElement {
  private final PromelaElementList<PromelaProgram> programs = new PromelaElementList<PromelaProgram>("\r\n");
  
  public PromelaModel(final Model m) {
    final Consumer<Program> _function = (Program p) -> {
      PromelaProgram _promelaProgram = new PromelaProgram(p);
      this.programs.add(_promelaProgram);
    };
    m.getPrograms().forEach(_function);
    this.setTimeValues();
    this.setTimeoutVars();
    this.setNextProcesses();
    PostConstructContext.postConstruct();
  }
  
  @Override
  public String toText() {
    String _xblockexpression = null;
    {
      PromelaContext context = PromelaContext.getContext();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("//-----------------------------------------------------------------------------");
      _builder.newLine();
      _builder.append("//-----------------------------------------------------------------------------");
      _builder.newLine();
      _builder.append("//metadata && init");
      _builder.newLine();
      _builder.append("//-----------------------------------------------------------------------------");
      _builder.newLine();
      _builder.append("//-----------------------------------------------------------------------------");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.newLine();
      CharSequence _metadataAndInitText = context.getMetadataAndInitText();
      _builder.append(_metadataAndInitText);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      String _text = this.programs.toText();
      _builder.append(_text);
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  private long setTimeValues() {
    final List<PromelaExpression.TimeConstant> timeVals = PromelaContext.getContext().getTimeVals();
    boolean _isEmpty = timeVals.isEmpty();
    if (_isEmpty) {
      final List<PromelaVar.TimeInterval> timeVars = PromelaContext.getContext().getTimeVars();
      final Consumer<PromelaVar.TimeInterval> _function = (PromelaVar.TimeInterval v) -> {
        v.setIgnoredTrue();
      };
      timeVars.forEach(_function);
      return 0;
    } else {
      long divider = timeVals.get(0).getValue();
      for (final PromelaExpression.TimeConstant tv : timeVals) {
        divider = this.gcd(divider, tv.getValue());
      }
      for (final PromelaExpression.TimeConstant tv_1 : timeVals) {
        long _value = tv_1.getValue();
        long _divide = (_value / divider);
        tv_1.setValue(_divide);
      }
      return divider;
    }
  }
  
  private void setTimeoutVars() {
    final List<PromelaProcess> allProcesses = PromelaContext.getContext().getAllProcesses();
    final Consumer<PromelaProcess> _function = (PromelaProcess p) -> {
      final Function1<PromelaState, PromelaStatement.Timeout> _function_1 = (PromelaState s) -> {
        return s.getTimeout();
      };
      final Function1<PromelaStatement.Timeout, PromelaExpression> _function_2 = (PromelaStatement.Timeout t) -> {
        return t.getTimeoutValue();
      };
      final Function1<PromelaExpression, Long> _function_3 = (PromelaExpression expr) -> {
        if ((expr instanceof PromelaExpression.TimeConstant)) {
          return Long.valueOf(((PromelaExpression.TimeConstant)expr).getValue());
        } else {
          if ((expr instanceof PromelaExpression.Var)) {
            return Long.valueOf(256l);
          } else {
            throw new NotSupportedElementException();
          }
        }
      };
      final Function2<Long, Long, Long> _function_4 = (Long t1, Long t2) -> {
        return Long.valueOf(Math.max((t1).longValue(), (t2).longValue()));
      };
      Long maxTimeout = IterableExtensions.<Long>reduce(IterableExtensions.<PromelaExpression, Long>map(IterableExtensions.<PromelaStatement.Timeout, PromelaExpression>map(IterableExtensions.<PromelaStatement.Timeout>filterNull(ListExtensions.<PromelaState, PromelaStatement.Timeout>map(p.getStates(), _function_1)), _function_2), _function_3), _function_4);
      if (((maxTimeout != null) && ((maxTimeout).longValue() > 0))) {
        int bits = 0;
        while (((maxTimeout).longValue() > 0)) {
          {
            maxTimeout = Long.valueOf(((maxTimeout).longValue() >> 1));
            bits++;
          }
        }
        if ((bits >= 32)) {
          throw new NotSupportedElementException();
        }
        p.getTimeoutVar().setBits(bits);
      }
    };
    allProcesses.forEach(_function);
  }
  
  private void setNextProcesses() {
    final List<PromelaProcess> processes = PromelaContext.getContext().getAllProcesses();
    int _size = processes.size();
    int _minus = (_size - 1);
    PromelaProcess prev = processes.get(_minus);
    for (final PromelaProcess cur : processes) {
      {
        prev.setNextMType(cur.getNameMType());
        prev = cur;
      }
    }
  }
  
  private long gcd(final long v1, final long v2) {
    long a = v1;
    long b = v2;
    while ((b != 0)) {
      {
        final long remainder = (a % b);
        a = b;
        b = remainder;
      }
    }
    return a;
  }
}
