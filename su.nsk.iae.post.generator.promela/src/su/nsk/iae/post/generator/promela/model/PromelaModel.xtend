package su.nsk.iae.post.generator.promela.model

import su.nsk.iae.post.poST.Model
import su.nsk.iae.post.generator.promela.context.PromelaContext
import su.nsk.iae.post.generator.promela.context.PostConstructContext
import su.nsk.iae.post.generator.promela.expressions.PromelaExpression
import su.nsk.iae.post.generator.promela.model.vars.PromelaVar
import su.nsk.iae.post.generator.promela.expressions.PromelaExpression.TimeConstant
import su.nsk.iae.post.generator.promela.context.NamespaceContext
import java.util.List
import java.util.ArrayList
import java.util.HashMap
import su.nsk.iae.post.generator.promela.exceptions.ConflictingOutputsOrInOutsException

class PromelaModel implements IPromelaElement {
	final PromelaElementList<PromelaProgram> programs = new PromelaElementList("\r\n\r\n\r\n");
	
	new(Model m) {
		m.programs.forEach[p | programs.add(new PromelaProgram(p))];
		NamespaceContext.addId("__currentProcess");
		
		setTimeValues();
		setTimeoutVars();
		val varSettingProgram = defineGremlinVarsAndOutputToInputConnections();
		PromelaContext.getContext().setVarSettingProgram(varSettingProgram);
		setNextProcesses(varSettingProgram);
		
		PostConstructContext.postConstruct();
		
		NamespaceContext.prepareNamesMapping();
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
			«IF context.varSettingProgram !== null»
				
				
				«context.varSettingProgram.toText()»
			«ENDIF»
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
	
	private def setNextProcesses(VarSettingProgram varSettingProgram) {
		val processes = PromelaContext.getContext().allProcesses;
		var prev = processes.get(processes.size - 1);
		for (cur : processes) {
			prev.nextMType = cur.nameMType;
			prev = cur;
		}
		if (varSettingProgram !== null) {
			varSettingProgram.setFirstProcess(processes.get(0).nameMType);
//			processes.get(processes.size - 1).nextMType = varSettingProgram.processMTypes.get(0);
		}
	}
	
	private def VarSettingProgram defineGremlinVarsAndOutputToInputConnections() {
		val gremlinVars = new ArrayList<PromelaVar>();
		val outputToInputVars = new HashMap<String, List<String>>();//ouputFullId -> List<inputFullId>
		
		val inputs = new HashMap<String, List<PromelaVar>>();//shortId -> List<promelaVar>
		val outputsAndInOuts = new HashMap<String, PromelaVar>();//shortId -> promelaVar
		for (pr : programs) {
			for (in : pr.inVars) {
				val fullIdParts = in.name.split("__");
				val shortId = fullIdParts.get(fullIdParts.length - 1);
				val inputsOfThisId = inputs.computeIfAbsent(shortId, [new ArrayList()]);
				inputsOfThisId.add(in);
			}
			for (out : pr.outVars) {
				val fullIdParts = out.name.split("__");
				val shortId = fullIdParts.get(fullIdParts.length - 1);
				val prev = outputsAndInOuts.putIfAbsent(shortId, out);
				if (prev !== null) {
					throw new ConflictingOutputsOrInOutsException(prev.name, out.name);
				}
			}
			for (inOut : pr.inOutVars) {
				val fullIdParts = inOut.name.split("__");
				val shortId = fullIdParts.get(fullIdParts.length - 1);
				val prev = outputsAndInOuts.putIfAbsent(shortId, inOut);
				if (prev !== null) {
					throw new ConflictingOutputsOrInOutsException(prev.name, inOut.name);
				}
			}
		}
		for (inShortIdAndVars : inputs.entrySet) {
			val shortId = inShortIdAndVars.key;
			val ins = inShortIdAndVars.value;
			val out = outputsAndInOuts.get(shortId);
			if (out === null) {
				ins.forEach[in | gremlinVars.add(in)];
			}
			else {
				outputToInputVars.put(out.name, ins.map[in | in.name]);
			}
		}
		
		if (gremlinVars.isEmpty() && outputToInputVars.isEmpty()) {
			return null;
		}
		else {
			val res = new VarSettingProgram();
			gremlinVars.forEach[v | res.addGremlinVar(v)];
			outputToInputVars.entrySet.forEach[outToInputs |
				res.addOutputToInputAssignments(outToInputs.key, outToInputs.value)
			];
			return res;
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