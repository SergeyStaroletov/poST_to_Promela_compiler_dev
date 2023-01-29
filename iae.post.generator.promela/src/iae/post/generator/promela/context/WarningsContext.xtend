package su.nsk.iae.post.generator.promela.context

import java.util.ArrayList

class WarningsContext {
	static val warningTexts = new ArrayList<String>();
	
	static def addWarning(String message) {
		warningTexts.add(message);
	}
	
	static def getWarningsText() {
		'''
			«FOR wt : warningTexts»
				WARNING: «wt»
			«ENDFOR»
		'''
	}
	
	static def clearContext() {
		warningTexts.clear();
	}
}