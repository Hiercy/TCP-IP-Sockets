package FileSharing;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSharingServer {
	
	private static final int LENGTH = 256;
	
	public static void main(String[] args) throws IOException {
		if (args.length != 1) 
			throw new IllegalArgumentException("<Port>");
		int port = Integer.parseInt(args[0]);
		
		try {
			ServerSocket server = new ServerSocket(port);
			Socket socket = server.accept();
			
			File file = new File("C:/Users/volk1/Desktop/qwe.txt");
			FileInputStream fileInput = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fileInput);
			
			byte[] b = new byte[(int)file.length()];
			bis.read(b, 0, b.length);
			
			OutputStream os = socket.getOutputStream();
			System.out.println("File sending");
			
			os.write(b, 0, b.length);
			os.flush();
			socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
