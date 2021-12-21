package su.nsk.iae.post.dsm.server;

import static su.nsk.iae.post.dsm.AppLauncher.DSM_NAME;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.lsp4j.jsonrpc.Launcher;

public class ServerLauncher {
	private ServerThread serverThread;

	public void start(final String hostName, final int port) {
		serverThread = new ServerThread(hostName, port);
		serverThread.start();
	}

	public void shutdown() {
		if (serverThread != null) {
			serverThread.shutdown();
		}
	}

	private final class ServerThread extends Thread {
		private final String host;
		private final int port;

		private ExecutorService threadPool;
		private boolean shouldRun = true;
		private AsynchronousSocketChannel socketChannel;

		ServerThread(final String host, final int port) {
			super(DSM_NAME + " WrapperServer");
			this.host = host;
			this.port = port;
		}

		@Override
		public void run() {
			threadPool = Executors.newCachedThreadPool();
			try (AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open()
					.bind(new InetSocketAddress(host, port))) {
				while (shouldRun) {
					System.out.println("[" + DSM_NAME + "] Ready to accept client requests");
					socketChannel = serverSocket.accept().get();
					final WrappedServer server = new WrappedServer();
					final InputStream input = Channels.newInputStream(socketChannel);
					final OutputStream output = Channels.newOutputStream(socketChannel);
					final Launcher<IWrappedClient> launcher = Launcher.createIoLauncher(server, IWrappedClient.class,
							input, output, threadPool, msg -> msg);
					final IWrappedClient client = launcher.getRemoteProxy();
					server.connect(client);
					CompletableFuture.supplyAsync(() -> startLauncher(launcher)).thenRun(server::dispose);
					System.out.println("[" + DSM_NAME + "] Connected client " + socketChannel.getRemoteAddress());
				}
			} catch (IOException | InterruptedException | ExecutionException e) {
				System.err.println("[" + DSM_NAME + "] Encountered an error at accepting new client");
				e.printStackTrace();
			}

		}

		private Void startLauncher(final Launcher<IWrappedClient> launcher) {
			try {
				return launcher.startListening().get();
			} catch (InterruptedException | ExecutionException e) {
				System.err
						.println("[" + DSM_NAME + "] AST Wrapper Server encountered an error at accepting new client");
				e.printStackTrace();
			}
			return null;
		}

		private void shutdown() {
			this.shouldRun = false;
			if (threadPool != null) {
				threadPool.shutdownNow();
			}
			if (socketChannel != null) {
				try {
					socketChannel.close();
					System.out.println("[" + DSM_NAME + "] Shutdown");
				} catch (final IOException e) {
					System.err.println("[" + DSM_NAME + "] Encountered an error at shutdown");
					e.printStackTrace();
				}	
			}
		}
	}
}
