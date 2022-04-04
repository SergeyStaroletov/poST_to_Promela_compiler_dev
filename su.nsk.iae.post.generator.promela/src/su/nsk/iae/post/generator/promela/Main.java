package su.nsk.iae.post.generator.promela;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import com.google.inject.Injector;

import su.nsk.iae.post.PoSTStandaloneSetup;
import su.nsk.iae.post.generator.promela.model.PromelaModel;
import su.nsk.iae.post.generator.promela.model.PromelaProgram;
import su.nsk.iae.post.poST.Model;

public class Main {
	public static void main(String[] args) throws IOException {
        System.out.println("Hello!\r\n");

        Model m = prepareAndParseModelFromResource(".\\Sanitizer.post");

        System.out.println(buildModel(m).toText().toString());

        System.out.println("Bye!");
    }

    private static PromelaModel buildModel(Model m) {
        return new PromelaModel(m);
    }

    private static Model prepareAndParseModelFromResource(String fileName) throws IOException {
        Injector i = new PoSTStandaloneSetup().createInjectorAndDoEMFRegistration();
        ResourceSet rs = i.getInstance(ResourceSet.class);

        Resource r = rs.getResource(URI.createFileURI(fileName), true);
        r.load(null);
        return  (Model)r.getContents().get(0);
    }
}
