package su.nsk.iae.post.generator.promela;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.BasicEAnnotationValidator.ValidationContext;

import com.google.inject.Injector;

import su.nsk.iae.post.PoSTStandaloneSetup;
import su.nsk.iae.post.generator.promela.context.WarningsContext;
import su.nsk.iae.post.generator.promela.exceptions.ConflictingOutputsOrInOutsException;
import su.nsk.iae.post.generator.promela.exceptions.NotSupportedElementException;
import su.nsk.iae.post.generator.promela.exceptions.UnknownElementException;
import su.nsk.iae.post.generator.promela.exceptions.WrongModelStateException;
import su.nsk.iae.post.generator.promela.model.PromelaModel;
import su.nsk.iae.post.poST.Model;

public class Main {
	public static void main(String[] args) throws IOException {
		String inputFile = getKeyValue(args, "-i", "--input").orElse(null);
		String outputFile = getKeyValue(args, "-o", "--output").orElse(null);
		boolean reduceTimeValues = isKeyPresent(args, "-rt", "--reduceTime");
		boolean addLtlMacrosesToEnd = isKeyPresent(args, "-lm", "--ltlMacro");
		
		if (inputFile == null) {
			System.out.println("ERROR: Input .post file must be specified (-i).");
		}
		if (outputFile == null) {
			System.out.println("ERROR: Output .pml file must be specified (-o).");
		}
		if (inputFile == null || outputFile == null) {
			return;
		}
		
        Model m = prepareAndParseModelFromResource(inputFile);
        try {
        	var model = new PromelaModel(m, reduceTimeValues, addLtlMacrosesToEnd);
        	var warnings = model.getWarnings();
        	var res = model.toText().replace("\t", "    ");
        	printToFile(outputFile, res);
        	System.out.println("Saved to \"" + outputFile + "\".");
        	if (!warnings.isBlank()) {
        		System.out.println(warnings);
        	}
        }
        catch (
        		ConflictingOutputsOrInOutsException |
        		NotSupportedElementException |
        		UnknownElementException |
        		WrongModelStateException e
        ) {
        	System.out.println("Translation failed.");
        	System.out.println("ERROR: " + e.getMessage());
        	System.out.println("Exception:");
        	e.printStackTrace(System.out);
        }
    }
	
	private static Optional<String> getKeyValue(String[] args, String... keys) {
		int keyIndex = -1;
		for (String key : keys) {
			keyIndex = List.of(args).indexOf(key);
			if (keyIndex != -1) {
				break;
			}
		}
		return Optional.ofNullable(keyIndex != -1 && args.length > keyIndex + 1 ? args[keyIndex + 1] : null);
	}
	
	private static boolean isKeyPresent(String[] args, String... keys) {
		for (String key : keys) {
			if (List.of(args).indexOf(key) != -1) {
				return true;
			}
		}
		return false;
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
