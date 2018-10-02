package Closing;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

public class CompressProtocol implements Runnable {

	public static final int BUFSIZE = 1024;
	private Socket clntSock;
	private Logger logger;
	
	public CompressProtocol(Socket clntSock, Logger logger) {
		this.clntSock = clntSock;
		this.logger = logger;
	}
	
	public static void handleCompressClient(Socket clntSock, Logger logger) {
		try {
			InputStream in = clntSock.getInputStream();
			ZipOutputStream out = new ZipOutputStream(clntSock.getOutputStream());
			
			byte[] buffer = new byte[BUFSIZE];
			int bytesRead;
			
			while ((bytesRead = in.read(buffer)) != -1)
				out.write(buffer, 0, bytesRead);
			out.finish();
			
			logger.info("Client " + clntSock.getRemoteSocketAddress() + " finished");
		} catch (IOException e) {
			logger.log(Level.WARNING, "Exception in echo protocol", e);
		}
		
		try { // Close socket
			clntSock.close();
		} catch (IOException e) {
			logger.info("Exception = " + e.getMessage());
		}
	}
	
	@Override
	public void run() {
		handleCompressClient(this.clntSock, this.logger);
	}
}
