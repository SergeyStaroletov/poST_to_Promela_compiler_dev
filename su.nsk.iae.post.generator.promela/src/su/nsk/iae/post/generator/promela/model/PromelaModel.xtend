package su.nsk.iae.post.generator.promela.model

import su.nsk.iae.post.poST.Model
import su.nsk.iae.post.generator.promela.PromelaContext
import su.nsk.iae.post.generator.promela.context.PostConstructContext
import su.nsk.iae.post.generator.promela.expressions.PromelaExpression
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar
import su.nsk.iae.post.generator.promela.expressions.PromelaExpression.TimeConstant

class PromelaModel implements IPromelaElement {
	final PromelaElementList<PromelaProgram> programs = new PromelaElementList("\r\n");
	
	new(Model m) {
		m.programs.forEach[p | programs.add(new PromelaProgram(p))];
		
		setTimeValues();
		setTimeoutVars();
		setNextProcesses();
		
		PostConstructContext.postConstruct();
	}
	
	override toText() {
		var context = PromelaContext.getContext();
		'''
			//-----------------------------------------------------------------------------
			//-----------------------------------------------------------------------------
			//metadata && init
			//-----------------------------------------------------------------------------
			//-----------------------------------------------------------------------------
						
			«context.getMetadataAndInitText()»
			
			«programs.toText()»
		''';
	}
	
	private def long setTimeValues() {
		val timeVals = PromelaContext.getContext().getTimeVals();
		if (timeVals.isEmpty()) {
			val timeVars = PromelaContext.getContext().getTimeVars();
			timeVars.forEach[v | v.setIgnoredTrue()];
			return 0;
		}
		else {
			var divider = timeVals.get(0).value;
			for (tv : timeVals) {
				divider = gcd(divider, tv.value);
			}
			for (tv : timeVals) {
				tv.setValue(tv.value / divider);
			}
			return divider;
		}
	}
	
	private def setTimeoutVars() {
		val allProcesses = PromelaContext.getContext().allProcesses;
		allProcesses.forEach[p | {
			var maxTimeout = p.states
				.map[s | s.timeout]
				.filterNull()
				.map[t | t.timeoutValue]
				.map[expr | {
					if (expr instanceof PromelaExpression.TimeConstant) {
						return expr.value;
					}
					else if (expr instanceof PromelaExpression.Var) {
						val timeVarFullName = expr.name;
						val timeVar = PromelaContext.getContext().getTimeVars()
							.findFirst[v | v.name.equals(timeVarFullName)];
						var timeVarValue = timeVar.value;
						if (timeVarValue instanceof TimeConstant) {
							return timeVarValue.value;
						}
						else {
							throw new NotSupportedElementException();
						}
					}
					else {
						throw new NotSupportedElementException();
					}
				}]
				.reduce[t1, t2 | Math.max(t1, t2)];
			if (maxTimeout !== null && maxTimeout > 0) {
				var bits = 0;
				while (maxTimeout > 0) {
					maxTimeout = maxTimeout >> 1;
					bits++;
				}
				if (bits >= 32) {
					throw new NotSupportedElementException();
				}
				p.timeoutVar.setBits(bits);
			}
		}];
	}
	
	private def setNextProcesses() {
		val processes = PromelaContext.getContext().allProcesses;
		var prev = processes.get(processes.size - 1);
		for (cur : processes) {
			prev.nextMType = cur.nameMType;
			prev = cur;
		}
	}
	
	private def gcd(long v1, long v2) {
		var a = v1;
		var b = v2;
		while (b != 0) {
			val remainder = a % b;
			a = b;
			b = remainder;
		}
		return a;
	}
	
}