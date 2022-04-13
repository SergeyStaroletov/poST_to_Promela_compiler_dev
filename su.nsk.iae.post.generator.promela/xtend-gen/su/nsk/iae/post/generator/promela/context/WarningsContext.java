package su.nsk.iae.post.generator.promela.context;

import java.util.ArrayList;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class WarningsContext {
  private static final ArrayList<String> warningTexts = new ArrayList<String>();
  
  public static boolean addWarning(final String message) {
    return WarningsContext.warningTexts.add(message);
  }
  
  public static CharSequence getWarningsText() {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final String wt : WarningsContext.warningTexts) {
        _builder.append("WARNING: ");
        _builder.append(wt);
        _builder.append(".");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public static void clearContext() {
    WarningsContext.warningTexts.clear();
  }
}
