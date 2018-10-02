package MultiTasking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EchoProtocol implements Runnable {

	private static final int BUFSIZE = 32;
	private Socket clntSock;
	private Logger logger;
	
	public EchoProtocol (Socket clntSock, Logger logger) {
		this.clntSock = clntSock;
		this.logger = logger;
	}
	
	public static void handleEchoClient(Socket socket, Logger logger) {
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			int recvMsgSize;
			int totalBytesEchoed = 0;
			byte[] echoBuffer = new byte[BUFSIZE];
			
			while ((recvMsgSize = in.read(echoBuffer)) != -1) {
				out.write(echoBuffer, 0, recvMsgSize);
				totalBytesEchoed += recvMsgSize;
			}
			logger.info("Client " + socket.getRemoteSocketAddress() + ", echoed " + totalBytesEchoed + " bytes");
		} catch (IOException ex) {
			logger.log(Level.WARNING, "Exception in echo protocol", ex);
		} finally {
			try {
			socket.close();
			} catch (IOException e) {
			}
		} 
	}
	
	@Override
	public void run() {
		handleEchoClient(clntSock, logger);
	}

}
