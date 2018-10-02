package MultiTasking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPEchoServerPool {
	public static void main(String[] args) throws IOException {
		if (args.length != 2)
			throw new IllegalArgumentException("<Port> <Threads>");
		
		int port = Integer.parseInt(args[0]);
		int threadPoolSize = Integer.parseInt(args[1]); // Количество потоков, которое будет создано (= макс кол-во клиентов)
		
		final ServerSocket server = new ServerSocket(port);
		
		final Logger logger = Logger.getLogger("practical");
		
		for (int i = 0; i < threadPoolSize; i++) {
			Thread thread = new Thread() {
				public void run() {
					while (true) {
						try {
							Socket socket = server.accept();
							EchoProtocol.handleEchoClient(socket, logger);
						} catch (IOException e) {
							logger.log(Level.WARNING, "Client accept failed", e);
						}
					}
				}
			};
			thread.start();
			logger.info("Creadted and started Thread = " + thread.getName());
		}
	}
}
