package iae.post.generator.promela.context;

import iae.post.generator.promela.model.PromelaProcess;
import iae.post.generator.promela.model.PromelaProgram;
import iae.post.generator.promela.model.PromelaState;

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
    CurrentContext.stopProcess();
  }
  
  public static void stopProcess() {
    CurrentContext.curProcess = null;
    CurrentContext.stopState();
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
  
  public static void clearContext() {
    CurrentContext.stopProgram();
  }
}
