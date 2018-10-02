package FileSharing;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileSharingClient {
	
	private static final int LENGTH = 256;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		if (args.length < 2 || args.length > 3) 
			throw new IllegalArgumentException("<Adress> <Port>");
		
		String address = args[0];
		int port = Integer.parseInt(args[1]);
		int bytesRead, total;
		
		byte[] b = new byte[LENGTH];
		
		Socket socket = new Socket(address, port);
		InputStream input = socket.getInputStream();
		FileOutputStream fileOutput = new FileOutputStream("C:/Users/volk1/Desktop/asd.txt");
		BufferedOutputStream bos = new BufferedOutputStream(fileOutput);
		
		bytesRead = input.read(b, 0, b.length);
		total = bytesRead;
		do {
			bytesRead = input.read(b, 0, b.length-total);
			if (bytesRead >= 0)
				total += bytesRead;
		} while (bytesRead > -1);
		
		bos.write(b, 0, total);
		bos.flush();
		bos.close();
		socket.close();
	}
}
