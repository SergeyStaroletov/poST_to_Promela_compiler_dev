package su.nsk.iae.post.generator.promela.model.vars

import su.nsk.iae.post.generator.promela.model.IPromelaElement
import su.nsk.iae.post.generator.promela.model.WrongModelStateException
import su.nsk.iae.post.generator.promela.expressions.PromelaExpression

abstract class PromelaVar implements IPromelaElement {
	protected String name;
	protected String typeName;
	protected String after;
	protected boolean constant;
	protected PromelaExpression value;
	protected boolean ignored = false;
	
	new(String name, String typeName, String after) {
		this.name = name;
		this.typeName = typeName;
		this.after = after;
	}
	
	new(String name, String typeName) {
		this(name, typeName, null);
	}
	
	def setConstantTrue() {
		this.constant = true;
		return this;
	}
	
	def getName() {
		return name;
	}
	
	def isConstant() {
		return this.constant;
	}
	
	def PromelaVar setValue(PromelaExpression value) {
		this.value = value;
		return this;
	}
	
	def setIgnoredTrue() {
		this.ignored = true;
	}
	
	def getValue() {
		return value;
	}
	
	override toText() {
		ignored ?
		'''
			//ignored: «name»
		''' :
		value !== null ?
		'''
			«IF this.constant»
			#define «name» «value.toText»
			«ELSEIF this.after !== null»
			«typeName» «name» «after» = «value.toText»;
			«ELSE»
			«typeName» «name» = «value.toText»;
			«ENDIF»
		''' :
		'''
			«IF this.constant»
			#define «name»
			«ELSEIF this.after !== null»
			«typeName» «name» «after»;
			«ELSE»
			«typeName» «name»;
			«ENDIF»
		'''
	}
	
	static class Bool extends PromelaVar {
		new(String name) {
			super(name, "bool");
		}
	}
	
	static class Short extends PromelaVar {
		new(String name) {
			super(name, "short");
		}
	}
	
	static class Int extends PromelaVar {
		new(String name) {
			super(name, "int");
		}
	}
	
	static class Byte extends PromelaVar {
		new(String name) {
			super(name, "byte");
		}
	}
	
	static class Unsigned extends PromelaVar {
		new(String name, int bits) {
			super(name, "unsigned", ": " + bits);
		}
		
		def setBits(int bits) {
			this.after = ": " + bits;
		}
	}
	
	static class TimeInterval extends PromelaVar {
		String originalValue;
		boolean bitsSet = false;
		
		new(String name) {
			super(name, "unsigned", ": 0");
		}
		
		def setOriginalValue(String originalValue) {
			this.originalValue = originalValue;
		}
		
		def setBits(int bits) {
			this.bitsSet = true;
			this.after = ": " + bits;
		}
		
		override String toText() {
			if (!constant && !bitsSet && !ignored) {
				throw new WrongModelStateException();
			}
			'''
				«IF originalValue !== null»
					«super.toText.trim» //«originalValue»
				«ELSE»
					«super.toText»
				«ENDIF»
			'''
		}
	}
	
	static class VarProxy extends PromelaVar {
		PromelaVar v = null;
		
		new(String name, String type) {
			super(name, type);
		}
		
		def setVar(PromelaVar v) {
			this.v = v;
		}
		
		override toText() {
			if (ignored) {
				return
				'''
					//ignored: «v === null ? this.name : v.name»
				''';
			}
			if (v === null) {
				throw new WrongModelStateException();
			}
			else {
				return v.toText();
			}
		}
	}
}