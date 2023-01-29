package iae.post.generator.promela.model.statements

import iae.post.poST.Statement
import iae.post.generator.promela.exceptions.UnknownElementException
import iae.post.generator.promela.model.statements.PromelaStatement
import iae.post.poST.StatementList
import iae.post.poST.impl.AssignmentStatementImpl
import iae.post.poST.impl.IfStatementImpl
import iae.post.poST.impl.CaseStatementImpl
import iae.post.poST.impl.StartProcessStatementImpl
import iae.post.poST.impl.StopProcessStatementImpl
import iae.post.poST.impl.ErrorProcessStatementImpl
import iae.post.poST.impl.SetStateStatementImpl
import iae.post.poST.impl.ResetTimerStatementImpl
import iae.post.poST.impl.TimeoutStatementImpl
import iae.post.poST.impl.WhileStatementImpl
import iae.post.poST.impl.RepeatStatementImpl
import iae.post.poST.impl.ForStatementImpl

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
			default: throw new UnknownElementException("Statement of type " + s.class.toString + " (" + s + ")")
		}
	}
}