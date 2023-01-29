package iae.post.generator.promela.exceptions

class ConflictingOutputsOrInOutsException extends RuntimeException {
	String input1;
	String input2;
	
	new (String in1, String in2) {
		this.input1 = in1;
		this.input2 = in2;
	}
	
	override getMessage() {
		return "Conflicting output or inout vars full ids: " + input1 + ", " + input2;
	}
}