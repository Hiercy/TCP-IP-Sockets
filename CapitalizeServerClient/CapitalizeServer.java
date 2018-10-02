package CapitalizeServerClient;

import java.io.IOException;
import java.net.ServerSocket;

public class CapitalizeServer {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) throw new IllegalArgumentException("<ServerName>");

		int clientNumber = 0;
		int port = Integer.parseInt(args[0]);
		ServerSocket server = new ServerSocket(port);

		try {
			while (true) {
				new Capitilizer(server.accept(), clientNumber++).start();
			}
		} finally {
			server.close();
		}
	}
}
