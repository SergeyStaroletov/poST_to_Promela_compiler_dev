package su.nsk.iae.post.generator.promela;

import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import com.google.inject.Injector;

import su.nsk.iae.post.PoSTStandaloneSetup;
import su.nsk.iae.post.generator.promela.model.PromelaModel;
import su.nsk.iae.post.poST.Model;

public class Main {
	public static void main(String[] args) throws IOException {
        Model m = prepareAndParseModelFromResource(".\\Sanitizer.post");
        
        var res = new PromelaModel(m).toText();

        System.out.println(res);
        
        printToFile("D:\\Promela programms\\sanitizer-gen.pml", res);

        System.out.println("Saved to file sanitizer-gen.pml.");
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
