package iae.post.generator.promela.exceptions

import java.lang.RuntimeException

class UnknownElementException extends RuntimeException {
	String unknownElement;
	
	new (String unknownElement) {
		this.unknownElement = unknownElement;
	}
	
	override getMessage() {
		return "Unknown element: " + unknownElement;
	}
}
