package su.nsk.iae.post.generator.promela.exceptions;

@SuppressWarnings("all")
public class ConflictingOutputsOrInOutsException extends RuntimeException {
  private String input1;
  
  private String input2;
  
  public ConflictingOutputsOrInOutsException(final String in1, final String in2) {
    this.input1 = in1;
    this.input2 = in2;
  }
  
  @Override
  public String getMessage() {
    return ((("Conflicting output or inout vars full ids: " + this.input1) + ", ") + this.input2);
  }
}
