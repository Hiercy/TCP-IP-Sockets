package DateServerAClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DateServer {
	public static void main(String[] args) throws IOException {
		try (
				ServerSocket server = new ServerSocket(8080);
				Socket socket = server.accept();
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				){
			while (!server.isClosed()) {
				out.println(new Date().toString());
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
