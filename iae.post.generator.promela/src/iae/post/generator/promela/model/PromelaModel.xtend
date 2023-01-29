package iae.post.generator.promela.model

import iae.post.poST.Model
import iae.post.generator.promela.context.PromelaContext
import iae.post.generator.promela.context.PostConstructContext
import iae.post.generator.promela.model.expressions.PromelaExpression
import iae.post.generator.promela.model.vars.PromelaVar
import iae.post.generator.promela.model.expressions.PromelaExpression.TimeConstant
import iae.post.generator.promela.context.NamespaceContext
import java.util.List
import java.util.ArrayList
import java.util.HashMap
import iae.post.generator.promela.exceptions.ConflictingOutputsOrInOutsException
import iae.post.poST.Configuration
import iae.post.generator.promela.context.CurrentContext
import iae.post.generator.promela.context.WarningsContext
import iae.post.generator.promela.exceptions.NotSupportedElementException

class PromelaModel implements IPromelaElement {
	static val promelaVerificationTaskName = "PromelaVerificationTask";
	
	final PromelaElementList<PromelaProgram> programs = new PromelaElementList("\r\n\r\n\r\n");
	final boolean addLtlMacrosesToEnd;
	
	new(Model m, boolean reduceTimeValues, boolean addLtlMacrosesToEnd) {
		this.addLtlMacrosesToEnd = addLtlMacrosesToEnd;
		
		m.programs.forEach[p | programs.add(new PromelaProgram(p, m.globVars))];
		NamespaceContext.addId("__currentProcess");
		
		val interval = getIntervalOfPromelaVerificationTask(m.conf);
		setTimeValues(interval, reduceTimeValues);
		setTimeoutVars();
		val varSettingProgram = defineGremlinVarsAndOutputToInputConnections();
		PromelaContext.getContext().setVarSettingProgram(varSettingProgram);
		setNextProcesses(varSettingProgram);
		
		PostConstructContext.postConstruct();
		
		NamespaceContext.prepareNamesMapping();
		
		checkProcessesLimit();
	}
	
	def getWarnings() {
		return WarningsContext.warningsText.toString().trim();
	}
	
	override toText() {
		var context = PromelaContext.getContext();
		val res = '''
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
			«IF addLtlMacrosesToEnd»
				
				
				«ltlHelpingMacroses()»
			«ENDIF»
		''';
		clearContexts();
		return res.toString();
	}
	
	private def getIntervalOfPromelaVerificationTask(Configuration config) {
		if (config !== null) {
			for (res : config.resources) {
				for (task : res.resStatement.tasks) {
					if (promelaVerificationTaskName.equals(task.name)) {
						if (task.init.interval !== null) {
							return new PromelaExpression.TimeConstant(task.init.interval.time.interval).value;
						}
						else {
							throw new NotSupportedElementException("Task with name \"promelaVerificationTaskName\" with no interval specified");
						}
					}
				}
			}
		}
		return 1l;
	}
	
	private def long setTimeValues(long interval, boolean reduceTimeValues) {
		val timeVals = PromelaContext.getContext().getTimeVals();
		if (timeVals.isEmpty()) {
			val timeVars = PromelaContext.getContext().getTimeVars();
			timeVars.forEach[v | v.setIgnoredTrue()];
			return 0;
		}
		else {
			for (tVar : PromelaContext.getContext().getTimeVars()) {
				tVar.setValueAfterInterval(interval);
			}
			for (tv : timeVals) {
				tv.setValue(tv.value / interval);
			}
			var long divider;
			if (reduceTimeValues) {
				divider = timeVals.get(0).value;
				for (tv : timeVals) {
					divider = gcd(divider, tv.value);
				}
				for (tv : timeVals) {
					tv.setValue(tv.value / divider);
				}
			}
			else {
				divider = 1;
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
							throw new NotSupportedElementException(timeVarValue.class.toString);
						}
					}
					else {
						throw new NotSupportedElementException(expr.class.toString);
					}
				}]
				.reduce[t1, t2 | Math.max(t1, t2)];
			if (maxTimeout !== null && maxTimeout >= 0) {
				maxTimeout = maxTimeout + 1;
				var bits = 0;
				while (maxTimeout > 0) {
					maxTimeout = maxTimeout >> 1;
					bits++;
				}
				if (bits >= 32) {
					throw new NotSupportedElementException("Timeout variable (" + p.timeoutVar.name + ") with size >= 32 bits");
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
			processes.get(processes.size - 1).nextMType = varSettingProgram.processMTypes.get(0);
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
					//throw new ConflictingOutputsOrInOutsException(prev.name, inOut.name);
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
	
	private def clearContexts() {
		CurrentContext.clearContext();
		NamespaceContext.clearContext();
		PostConstructContext.clearContext();
		WarningsContext.clearContext();
		PromelaContext.clearContext();
	}
	
	private def ltlHelpingMacroses() {
		'''
			//-----------------------------------------------------------------------------
			//-----------------------------------------------------------------------------
			//ltl
			//-----------------------------------------------------------------------------
			//-----------------------------------------------------------------------------
			
			#define apply1__ltl(f, arg) f(arg)
			#define apply2__ltl(f, arg) f(apply1__ltl(f, arg))
			#define apply3__ltl(f, arg) f(apply2__ltl(f, arg))
			#define apply4__ltl(f, arg) f(apply3__ltl(f, arg))
			#define apply5__ltl(f, arg) f(apply4__ltl(f, arg))
			#define apply6__ltl(f, arg) f(apply5__ltl(f, arg))
			#define apply7__ltl(f, arg) f(apply6__ltl(f, arg))
			#define apply8__ltl(f, arg) f(apply7__ltl(f, arg))
			#define apply9__ltl(f, arg) f(apply8__ltl(f, arg))
			#define apply10__ltl(f, arg) f(apply9__ltl(f, arg))
			#define apply__ltl(n, f, arg) apply##n##__ltl(f, arg)
			
			#define afterCycle__ltl(expr) «
				»(cycle__u U (!cycle__u W (cycle__u && (expr))))
			#define afterNCyclesWith__ltl(n, cond, expr) «
							»(apply__ltl(n, (cond) -> afterCycle__ltl, expr))
			#define afterNCyclesOrSoonerWith__ltl(n, cond, expr) «
							»afterNCyclesWith__ltl(n, (cond) && !(expr), expr)
			
			//-----------------------------------------------------------------------------
			//ltl between cycles
			//-----------------------------------------------------------------------------
			
			#define cltl(expr) (cycle__u -> (expr))
			#define G__cltl(expr) [](cycle__u -> (expr))
			#define F__cltl(expr) <>(cycle__u && (expr))
			#define U__cltl(expr1, expr2) (cycle__u -> (expr1)) U (cycle__u && (expr2))
			#define W__cltl(expr1, expr2) (cycle__u -> (expr1)) W (cycle__u && (expr2))
			#define V__cltl(expr1, expr2) (cycle__u && (expr1)) V (cycle__u -> (expr2))
			
			#define next__cltl(expr) (cycle__u -> afterCycle__ltl(expr))
			#define afterNWith__cltl(n, cond, expr) «
							»(cycle__u -> afterNCyclesWith__ltl(n, cond, expr))
			#define afterNOrSoonerWith__cltl(n, cond, expr) «
							»(cycle__u -> afterNCyclesOrSoonerWith__ltl(n, cond, expr))
		'''
	}
	
	private def checkProcessesLimit() {
		var mainProcesses = PromelaContext.context.allProcesses.size;
		var specialProcesses = 0;
		if (PromelaContext.context.varSettingProgram != null)
			specialProcesses = PromelaContext.context.varSettingProgram.processMTypes.size;
		var total = mainProcesses + specialProcesses + 1;
		if (total > 255) {
			WarningsContext.addWarning('''Spin can not handle more than 255 processes. «
					»Your model has «total» («mainProcesses» from poST program)''');
		}
	}
	
}