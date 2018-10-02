package MultiTasking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimelimitEchoProtocol implements Runnable {

	private static final int BUFSIZE = 32; // Size (bytes) of buffer
	private static final String TIMELIMIT = "10000"; // Default limit (ms)
	private static final String TIMELIMITPROP = "Timelimit"; // Property

	private static int timelimit;
	private Socket socket;
	private Logger logger;

	public TimelimitEchoProtocol(Socket socket, Logger logger) {
		this.socket = socket;
		this.logger = logger;

		// Get the time limit from the System properties or take the default
		timelimit = Integer.parseInt(System.getProperty(TIMELIMITPROP, TIMELIMIT));
	}

	public static void handleEchoCLient(Socket socket, Logger logger) {
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			int rcvMsgSize;
			int totalBytesEchoed = 0;
			byte[] echoBuffer = new byte[BUFSIZE];
			long endTime = System.currentTimeMillis() + timelimit;
			int timeBoundMillis = timelimit;

			socket.setSoTimeout(timeBoundMillis);

			while ((timeBoundMillis > 0) && ((rcvMsgSize = in.read(echoBuffer)) != -1)) {
				out.write(echoBuffer, 0, rcvMsgSize);
				totalBytesEchoed += rcvMsgSize;
				timeBoundMillis = (int) (endTime - System.currentTimeMillis());
				socket.setSoTimeout(timeBoundMillis);
			}
			logger.info("Client " + socket.getRemoteSocketAddress() + ", encoded " + totalBytesEchoed + " bytes");
		} catch (IOException ex) {
			logger.log(Level.WARNING, "Exception in echo protocol ", ex);
		}
	}

	@Override
	public void run() {
		handleEchoCLient(this.socket, this.logger);
	}

}
