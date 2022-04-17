package su.nsk.iae.post.generator.promela.exceptions;

@SuppressWarnings("all")
public class NotSupportedElementException extends RuntimeException {
  private String elementType;
  
  public NotSupportedElementException(final String elementType) {
    this.elementType = elementType;
  }
  
  @Override
  public String getMessage() {
    return ("Not supported element: " + this.elementType);
  }
}
