package su.nsk.iae.post.generator.promela.expressions

import su.nsk.iae.post.generator.promela.model.IPromelaElement

abstract class PromelaExpression implements IPromelaElement {
	
	static class Constant extends PromelaExpression {
		protected String value;
		
		new (String value) {
			this.value = value;
		}
		
		override toText() {
			return value;
		}
		
	}
	
	static class TimeConstant extends PromelaExpression {
		protected long value; //milliseconds
		
		new (String interval) {
			value = 0;
			var curInterval = interval.replace("ms", "t");
			curInterval = addFirstPart(curInterval, "d", 24 * 60 * 60 * 1000);
			curInterval = addFirstPart(curInterval, "h", 60 * 60 * 1000);
			curInterval = addFirstPart(curInterval, "m", 60 * 1000);
			curInterval = addFirstPart(curInterval, "s", 1000);
			curInterval = addFirstPart(curInterval, "t", 1);
			curInterval = "";
		}
		
		def setValue(long value) {
			this.value = value;
		}
		
		def getValue() {
			return value;
		}
		
		override toText() {
			return String.valueOf(value);
		}
		
		private def addFirstPart(String interval, String suffix, int multiplier) {
			if (interval.blank) {
				return "";
			}
			val strs = interval.split(suffix).map[s | s.trim()];
			if (strs.get(0).length < interval.length) {
				value += Long.parseLong(strs.get(0)) * multiplier;
			}
			return strs.get(strs.length - 1);
		}
		
	}
	
	static class Var extends PromelaExpression {
		protected String name;
		
		new (String name) {
			this.name = name;
		}
		
		def getName() {
			return name;
		}
		
		override toText() {
			return name;
		}
		
	}
	
	static class Not extends PromelaExpression {
		protected PromelaExpression internal;
		
		new (PromelaExpression internal) {
			this.internal = internal;
		}
		
		override toText() {
			'''!«this.internal.toText()»'''
		}
		
	}
	
	static class Primary extends PromelaExpression {
		protected PromelaExpression internal;
		
		new (PromelaExpression internal) {
			this.internal = internal;
		}
		
		override toText() {
			'''(«this.internal.toText()»)'''
		}
		
	}
	
	
	static class Binary extends PromelaExpression {
		String opSymbol;
		PromelaExpression left;
		PromelaExpression right;
		
		new (PromelaExpression left, String opSymbol, PromelaExpression right) {
			this.opSymbol = opSymbol;
			this.left = left;
			this.right = right;
		}
		
		override toText() {
			'''«left.toText()» «opSymbol» «right.toText()»''';
		}
		
	}
}