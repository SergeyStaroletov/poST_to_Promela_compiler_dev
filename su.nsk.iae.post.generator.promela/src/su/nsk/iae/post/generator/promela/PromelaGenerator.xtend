package su.nsk.iae.post.generator.promela

import su.nsk.iae.post.generator.IPoSTGenerator
import su.nsk.iae.post.poST.Model
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import su.nsk.iae.post.generator.promela.model.PromelaProgram

class PromelaGenerator implements IPoSTGenerator {
	
	override setModel(Model model) {
		System.out.println('''
		---------
		
		«new PromelaProgram(model.programs.get(0)).toText()»
		
		---------
		''');
	}
	
	override beforeGenerate(
		Resource input,
		IFileSystemAccess2 fsa,
		IGeneratorContext context
	) {
		
	}
	
	override doGenerate(
		Resource input,
		IFileSystemAccess2 fsa,
		IGeneratorContext context
	) {
		
	}
	
	override afterGenerate(
		Resource input,
		IFileSystemAccess2 fsa,
		IGeneratorContext context
	) {
		
	}
	
}