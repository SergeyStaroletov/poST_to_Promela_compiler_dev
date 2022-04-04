package su.nsk.iae.post.generator.promela.context;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class PostConstructContext {
  public interface IPostConstuctible {
    void postConstruct();
  }
  
  private static List<PostConstructContext.IPostConstuctible> elements = new ArrayList<PostConstructContext.IPostConstuctible>();
  
  public static boolean register(final PostConstructContext.IPostConstuctible e) {
    return PostConstructContext.elements.add(e);
  }
  
  public static void postConstruct() {
    final Consumer<PostConstructContext.IPostConstuctible> _function = (PostConstructContext.IPostConstuctible e) -> {
      e.postConstruct();
    };
    PostConstructContext.elements.forEach(_function);
  }
}
