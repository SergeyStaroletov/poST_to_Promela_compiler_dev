package su.nsk.iae.post.generator.promela.model.vars

import su.nsk.iae.post.generator.promela.model.IPromelaElement
import su.nsk.iae.post.generator.promela.exceptions.WrongModelStateException
import su.nsk.iae.post.generator.promela.model.expressions.PromelaExpression
import su.nsk.iae.post.generator.promela.context.NamespaceContext
import java.util.List

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
			//ignored: «NamespaceContext.getName(name)»
		''' :
		value !== null ?
		'''
			«IF this.constant»
			#define «NamespaceContext.getName(name)» «value.toText»
			«ELSEIF this.after !== null»
			«typeName» «NamespaceContext.getName(name)» «after» = «value.toText»;
			«ELSE»
			«typeName» «NamespaceContext.getName(name)» = «value.toText»;
			«ENDIF»
		''' :
		'''
			«IF this.constant»
			#define «NamespaceContext.getName(name)»
			«ELSEIF this.after !== null»
			«typeName» «NamespaceContext.getName(name)» «after»;
			«ELSE»
			«typeName» «NamespaceContext.getName(name)»;
			«ENDIF»
		'''
	}
	
	static class Bool extends PromelaVar {
		new(String name) {
			super(name, "bool");
		}
	}
	
	static class Short extends PromelaVar {
		boolean wasSByte = false;
		
		new(String name) {
			super(name, "short");
		}
		
		new(String name, boolean wasSByte) {
			super(name, "short");
			this.wasSByte = wasSByte;
		}
		
		def getWasSByte() {
			return wasSByte;
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
		String valueAfterInterval;
		boolean bitsSet = false;
		
		new(String name) {
			super(name, "unsigned", ": 0");
		}
		
		def setOriginalValue(String originalValue) {
			this.originalValue = originalValue;
		}
		
		def setValueAfterInterval(long interval) {
			var ms = new PromelaExpression.TimeConstant(originalValue).value / interval * interval;
			var s = ms / 1000;
			ms %= 1000;
			var m = s / 60;
			s %= 60;
			var h = m / 60;
			m %= 60;
			var d = h / 24;
			h %= 24;
			this.valueAfterInterval =
						(d > 0 ? d + "d" : "") +
						(h > 0 ? h + "h" : "") +
						(m > 0 ? m + "m" : "") +
						(s > 0 ? s + "s" : "") +
						(ms > 0 ? ms + "ms" : "");
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
				«IF valueAfterInterval !== null && originalValue !== null && !valueAfterInterval.equals(originalValue)»
					«super.toText.trim» //«originalValue» -> «valueAfterInterval»
				«ELSEIF originalValue !== null»
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
					//ignored: «NamespaceContext.getName(v === null ? this.name : v.name)»
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
	
	static class Array extends PromelaVar {
		int firstIndex;
		int length;
		List<PromelaExpression> values;
		
		new (String name, String typeName, int firstIndex, int lastIndex, List<PromelaExpression> values) {
			super(name, typeName);
			this.firstIndex = firstIndex;
			this.length = lastIndex - firstIndex + 1;
			this.values = values;
		}
		
		override toText() {
			val name = NamespaceContext.getName(this.name);
			'''
				«IF values !== null»
					«typeName» «name»[«length»] = {«FOR v : values SEPARATOR ", "»«v.toText()»«ENDFOR»};
				«ELSE»
					«typeName» «name»[«length»];
				«ENDIF»
			'''
		}
		
		def getFirstIndex() {
			return firstIndex;
		}
	}
}