package iae.post.generator.promela.exceptions

class NotSupportedElementException extends RuntimeException {
	String elementType;
	
	new (String elementType) {
		this.elementType = elementType;
	}
	
	override getMessage() {
		return "Not supported element: " + elementType;
	}
}
