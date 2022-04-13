package su.nsk.iae.post.generator.promela.expressions

import su.nsk.iae.post.generator.promela.model.IPromelaElement
import su.nsk.iae.post.generator.promela.context.NamespaceContext
import su.nsk.iae.post.poST.ProcessStatusExpression
import su.nsk.iae.post.generator.promela.context.PromelaContext
import su.nsk.iae.post.generator.promela.context.CurrentContext
import su.nsk.iae.post.generator.promela.context.PostConstructContext
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar

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
			return NamespaceContext.getName(name);
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
	
	static class Invert extends PromelaExpression {
		protected PromelaExpression internal;
		
		new (PromelaExpression internal) {
			this.internal = internal;
		}
		
		override toText() {
			'''-«this.internal.toText()»'''
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
	
	static class ProcessStatus extends PromelaExpression implements PostConstructContext.IPostConstuctible {
		String programName;
		String processName;
		
		boolean active;
		boolean inactive;
		boolean stop;
		String processMtype;
		String stopStateMType;
		String errorStateMType;
		
		new (ProcessStatusExpression processStatusExpression) {
			this.active = processStatusExpression.active;
			this.inactive = processStatusExpression.inactive;
			this.stop = processStatusExpression.stop;
			this.programName = CurrentContext.curProgram.shortName;
			this.processName = processStatusExpression.process.name;
			PostConstructContext.register(this);
		}
		
		override void postConstruct(){
			val process = PromelaContext.context.allProcesses
				.findFirst[
					p | p.programName.equals(programName)
					&& p.shortName.equals(processName)
				];
			this.processMtype = process.nameMType;
			this.stopStateMType = process.stopStateMType;
			this.errorStateMType = process.errorStateMtype;
		}
		
		override toText() {
			val pMType = NamespaceContext.getName(processMtype);
			val sMType = NamespaceContext.getName(stopStateMType);
			val eMType = NamespaceContext.getName(errorStateMType);
			return active ? '''(«pMType» != «sMType» & «pMType» != «eMType»)'''
				: inactive ? '''(«pMType» == «sMType» || «pMType» == «eMType»)'''
				: stop ? '''«pMType» == «sMType»'''
				: '''«pMType» == «eMType»''';
		}
	}
	
	static class ArrayVar extends PromelaExpression {
		String name;
		PromelaExpression indexExpr;
		boolean indexIsVarOrConst;
		int firstArrayIndex;
		
		new (PromelaVar.Array arrayVar, PromelaExpression indexExpr) {
			this.name = arrayVar.name;
			this.indexExpr = indexExpr;
			this.firstArrayIndex = arrayVar.firstIndex;
			this.indexIsVarOrConst = indexExpr instanceof Var || indexExpr instanceof Constant;
		}
		
		override toText() {
			'''«NamespaceContext.getName(name)»[«
				IF firstArrayIndex != 0 && !indexIsVarOrConst
					»(«
				ENDIF
				»«indexExpr.toText()»«
				IF firstArrayIndex != 0»«
					IF !indexIsVarOrConst
						»)«
					ENDIF
					» - «firstArrayIndex»«
				ENDIF
			»]''';
		}
	}
	
}