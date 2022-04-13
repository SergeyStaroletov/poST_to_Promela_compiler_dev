package su.nsk.iae.post.generator.promela.statements

import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.generator.promela.model.UnknownElementException
import su.nsk.iae.post.generator.promela.statements.PromelaStatement
import su.nsk.iae.post.poST.StatementList
import su.nsk.iae.post.poST.impl.AssignmentStatementImpl
import su.nsk.iae.post.poST.impl.IfStatementImpl
import su.nsk.iae.post.poST.impl.CaseStatementImpl
import su.nsk.iae.post.poST.impl.StartProcessStatementImpl
import su.nsk.iae.post.poST.impl.StopProcessStatementImpl
import su.nsk.iae.post.poST.impl.ErrorProcessStatementImpl
import su.nsk.iae.post.poST.impl.SetStateStatementImpl
import su.nsk.iae.post.poST.impl.ResetTimerStatementImpl
import su.nsk.iae.post.poST.impl.TimeoutStatementImpl
import su.nsk.iae.post.poST.impl.WhileStatementImpl
import su.nsk.iae.post.poST.impl.RepeatStatementImpl
import su.nsk.iae.post.poST.impl.ForStatementImpl

class PromelaStatementsHelper {
	static def getStatementList(StatementList statements) {
		return statements.statements.map[s | getStatement(s)];
	}
	
	static def getStatement(Statement s) {
		switch (s.class) {
			case AssignmentStatementImpl: return new PromelaStatement.Assign(s as AssignmentStatementImpl)
			case IfStatementImpl: return new PromelaStatement.If(s as IfStatementImpl)
			case CaseStatementImpl: return new PromelaStatement.Case(s as CaseStatementImpl)
			case StartProcessStatementImpl: return new PromelaStatement.StartProcess(s as StartProcessStatementImpl)
			case StopProcessStatementImpl: return new PromelaStatement.StopProcess(s as StopProcessStatementImpl)
			case ErrorProcessStatementImpl: return new PromelaStatement.ErrorProcess(s as ErrorProcessStatementImpl)
			case SetStateStatementImpl: return new PromelaStatement.SetState(s as SetStateStatementImpl)
			case ResetTimerStatementImpl: return new PromelaStatement.ResetTimer()
			case TimeoutStatementImpl: return new PromelaStatement.Timeout(s as TimeoutStatementImpl)
			case WhileStatementImpl: return new PromelaStatement.While(s as WhileStatementImpl)
			case RepeatStatementImpl: return new PromelaStatement.Repeat(s as RepeatStatementImpl)
			case ForStatementImpl: return new PromelaStatement.For(s as ForStatementImpl)
			default: throw new UnknownElementException()
		}
	}
}