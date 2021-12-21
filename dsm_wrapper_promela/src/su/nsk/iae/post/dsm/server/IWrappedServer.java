package su.nsk.iae.post.dsm.server;

import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;

public interface IWrappedServer {
	@JsonRequest
    CompletableFuture<String> generate(String ast);
}
