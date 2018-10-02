package MultiTasking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class TCPEchoServerExecutor {
	public static void main(String[] args) throws IOException {
		if (args.length != 1)
			throw new IllegalArgumentException("<Port>");
		
		int port = Integer.parseInt(args[0]);
//		int threadSizePool = Integer.parseInt(args[1]);
		
		ServerSocket server = new ServerSocket(port);
		
		Logger logger = Logger.getLogger("practical");
		
		// Executor service = Executors.newCachedThreadPool(threadSizePool);
		Executor service = Executors.newCachedThreadPool();
		
		while (true) {
			Socket socket = server.accept();
			service.execute(new EchoProtocol(socket, logger));
		}
	}
}
