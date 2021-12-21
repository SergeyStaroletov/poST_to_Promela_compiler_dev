package su.nsk.iae.post.dsm.server;

import static su.nsk.iae.post.dsm.AppLauncher.DSM_DIRECTORY;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.lsp4j.jsonrpc.CompletableFutures;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;

import com.google.inject.Injector;

import su.nsk.iae.post.PoSTStandaloneSetup;
import su.nsk.iae.post.deserialization.ModelDeserializer;
import su.nsk.iae.post.dsm.data.DSMRequestBody;
import su.nsk.iae.post.dsm.data.NullGeneratorContext;
import su.nsk.iae.post.generator.promela.PromelaGenerator;

public class WrappedServer implements IWrappedServer {

	@Override
	public CompletableFuture<String> generate(String body) {
		return CompletableFutures.computeAsync(cancelChecker -> doGenerate(body));
	}

	public void connect(IWrappedClient client) {
		// nothing to do -- no need to communicate to client for now
	}

	public void dispose() {
		// nothing to do
	}

	private String doGenerate(String body) {
		final DSMRequestBody requestBody = new DSMRequestBody(body);
		return doGenerate(requestBody.getAST(), requestBody.getRoot(), requestBody.getFileName());
	}

	private String doGenerate(String serializedAst, String directory, String fileName) {
		final Injector injector = PoSTStandaloneSetup.getInjector();
		final Resource resource = ModelDeserializer.deserializeFromXMI(serializedAst);

		final PromelaGenerator generator = new PromelaGenerator();
		final JavaIoFileSystemAccess fsa = injector.getInstance(JavaIoFileSystemAccess.class);

		final String generated = directory + File.separator + DSM_DIRECTORY + File.separator + fileName;
		fsa.setOutputPath(generated);

		final IGeneratorContext context = new NullGeneratorContext();
		generator.beforeGenerate(resource, fsa, context);
		generator.doGenerate(resource, fsa, context);
		generator.afterGenerate(resource, fsa, context);

		return "Files generated in " + generated;
	}

}
