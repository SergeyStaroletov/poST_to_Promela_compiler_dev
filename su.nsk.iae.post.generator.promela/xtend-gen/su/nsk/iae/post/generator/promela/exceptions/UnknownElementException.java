package su.nsk.iae.post.generator.promela.exceptions;

@SuppressWarnings("all")
public class UnknownElementException extends RuntimeException {
  private String unknownElement;
  
  public UnknownElementException(final String unknownElement) {
    this.unknownElement = unknownElement;
  }
  
  @Override
  public String getMessage() {
    return ("Unknown element: " + this.unknownElement);
  }
}
