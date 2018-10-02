package Closing;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

// Can deadlock if a large file is sent

public class CompressClient {

	public static final int BUFSIZE = 256; // Size of reader buffer
	
	public static void main(String[] args) throws IOException {
		if (args.length != 3)
			throw new IllegalArgumentException("<Server> <Port> <File>");
		
		String server = args[0];
		int port = Integer.parseInt(args[1]);
		String filename = args[2];
		
		// Open input and output file
		FileInputStream fileIn = new FileInputStream(filename);
		FileOutputStream fileOut = new FileOutputStream(filename + ".txt");
		
		// Socket connection to server
		Socket socket = new Socket(server, port);
		
		// Send uncompressed byte stream to server
		sendBytes(socket, fileIn);
		
		// Receive compressed byte stream from server
		InputStream sockIn = socket.getInputStream();
		int bytesRead; // Number of bytes read
		byte[] buffer = new byte[BUFSIZE];
		
		while ((bytesRead = sockIn.read(buffer)) != -1) {
			fileOut.write(buffer, 0, bytesRead);
			System.out.println("R"); // Reading progress indicator
		}
		
		System.out.println();
		
		socket.close();
		fileIn.close();
		fileOut.close();
	}

	private static void sendBytes(Socket socket, FileInputStream fileIn) throws IOException {
		OutputStream sockOut = socket.getOutputStream();
		int bytesRead;
		byte[] buffer = new byte[BUFSIZE];
		
		while((bytesRead = fileIn.read(buffer)) != -1) {
			sockOut.write(buffer, 0, bytesRead);
			System.out.println("W"); // Writing progress indicator
		}
		socket.shutdownOutput();
	}
}
