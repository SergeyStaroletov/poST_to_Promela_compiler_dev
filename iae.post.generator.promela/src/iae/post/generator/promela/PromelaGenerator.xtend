package iae.post.generator.promela

import iae.post.generator.IPoSTGenerator
import iae.post.poST.Model
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import iae.post.generator.promela.model.PromelaProgram
import iae.post.generator.promela.model.PromelaModel

class PromelaGenerator implements IPoSTGenerator {
	
	override setModel(Model model) {
		System.out.println(new PromelaModel(model, false, false).toText());
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