package CapitalizeServerClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class CapitalizeClient {

	private static BufferedReader in;
	private static PrintWriter out;


	public static void main(String[] args) throws Exception {
		if (args.length != 1) throw new IllegalArgumentException("<ServerName>");
		String serverAddress = args[0];

		Socket socket = new Socket(serverAddress, 8080);
		System.out.println("Connected to server...");

		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());

		out.println("Connected successfully");
		
		String response;
		while(!socket.isClosed()) {
			try {
			response = in.readLine();
			if (response.equalsIgnoreCase("quit")) System.exit(-1);
			} catch (IOException e) {
				response = "Error: " + e;
			}
		}
	}
}
