package iae.post.generator.promela.exceptions

class WrongModelStateException extends RuntimeException {
	String message;
	
	new (String message) {
		this.message = message;
	}
	
	override getMessage() {
		return message;
	}
}
