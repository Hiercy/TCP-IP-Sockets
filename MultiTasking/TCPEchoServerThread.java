package MultiTasking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class TCPEchoServerThread {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) 
			throw new IllegalArgumentException("<Port>");
		
		int port = Integer.parseInt(args[0]);
		
		ServerSocket server = new ServerSocket(port);
		
		Logger logger = Logger.getLogger("practical");
		
		while (true) {
			Socket clntSock = server.accept();
			
			Thread thread = new Thread(new EchoProtocol(clntSock, logger));
			thread.start();
			logger.info("Created and started Thread " + thread.getName());
		}
	}
}
