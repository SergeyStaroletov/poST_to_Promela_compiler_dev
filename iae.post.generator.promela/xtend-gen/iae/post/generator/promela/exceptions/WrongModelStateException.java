package iae.post.generator.promela.exceptions;

@SuppressWarnings("all")
public class WrongModelStateException extends RuntimeException {
  private String message;
  
  public WrongModelStateException(final String message) {
    this.message = message;
  }
  
  @Override
  public String getMessage() {
    return this.message;
  }
}
