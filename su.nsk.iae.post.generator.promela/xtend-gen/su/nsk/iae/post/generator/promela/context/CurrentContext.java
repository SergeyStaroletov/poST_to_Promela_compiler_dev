package su.nsk.iae.post.generator.promela.context;

import su.nsk.iae.post.generator.promela.model.PromelaProcess;
import su.nsk.iae.post.generator.promela.model.PromelaProgram;
import su.nsk.iae.post.generator.promela.model.PromelaState;

@SuppressWarnings("all")
public class CurrentContext {
  private static PromelaProgram curProgram;
  
  private static PromelaProcess curProcess;
  
  private static PromelaState curState;
  
  public static void startProgram(final PromelaProgram p) {
    CurrentContext.curProgram = p;
  }
  
  public static void startProcess(final PromelaProcess p) {
    CurrentContext.curProcess = p;
  }
  
  public static void startState(final PromelaState s) {
    CurrentContext.curState = s;
  }
  
  public static void stopProgram() {
    CurrentContext.curProgram = null;
    CurrentContext.curProcess = null;
    CurrentContext.curState = null;
  }
  
  public static void stopProcess() {
    CurrentContext.curProcess = null;
    CurrentContext.curState = null;
  }
  
  public static void stopState() {
    CurrentContext.curState = null;
  }
  
  public static PromelaProgram getCurProgram() {
    return CurrentContext.curProgram;
  }
  
  public static PromelaProcess getCurProcess() {
    return CurrentContext.curProcess;
  }
  
  public static PromelaState getCurState() {
    return CurrentContext.curState;
  }
  
  public static PromelaState clearContext() {
    PromelaState _xblockexpression = null;
    {
      CurrentContext.curProgram = null;
      CurrentContext.curProcess = null;
      _xblockexpression = CurrentContext.curState = null;
    }
    return _xblockexpression;
  }
}
