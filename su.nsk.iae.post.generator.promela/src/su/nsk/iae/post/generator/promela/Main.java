package su.nsk.iae.post.generator.promela;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import com.google.inject.Injector;

import su.nsk.iae.post.PoSTStandaloneSetup;
import su.nsk.iae.post.generator.promela.model.PromelaModel;
import su.nsk.iae.post.poST.Model;

public class Main {
	private static String defaultInputPath = ".\\Sanitizer.post";
	private static String defaultOutputPath = "D:\\Promela programms\\sanitizer-gen.pml";
	
	public static void main(String[] args) throws IOException {
		String inputFile = getKeyValue(args, "-i").orElse(defaultInputPath);
		String outputFile = getKeyValue(args, "-o").orElse(defaultOutputPath);
		
        Model m = prepareAndParseModelFromResource(inputFile);
        var res = new PromelaModel(m).toText();

        System.out.println(res);
        printToFile(outputFile, res);
        System.out.println("Saved to \"" + outputFile + "\".");
    }
	
	private static Optional<String> getKeyValue(String[] args, String key) {
		int pathIndex = List.of(args).indexOf(key) + 1;
		return Optional.ofNullable(pathIndex > 0 && args.length > pathIndex ? args[pathIndex] : null);
	}

    private static Model prepareAndParseModelFromResource(String fileName) throws IOException {
        Injector i = new PoSTStandaloneSetup().createInjectorAndDoEMFRegistration();
        ResourceSet rs = i.getInstance(ResourceSet.class);

        Resource r = rs.getResource(URI.createFileURI(fileName), true);
        r.load(null);
        return  (Model)r.getContents().get(0);
    }
    
    private static void printToFile(String path, String text) throws IOException {
    	var output = new FileOutputStream(path);
        output.write(text.getBytes());
        output.close();
    }
}
