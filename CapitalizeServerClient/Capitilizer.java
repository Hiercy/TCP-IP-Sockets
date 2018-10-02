package CapitalizeServerClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Capitilizer extends Thread {

	private Socket socket;
	private int clientNumber;
	
	public Capitilizer (Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		log("New connection with client #" + clientNumber + " at " + socket);
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			out.println("Hello, you are client #" + clientNumber + ".");
            
            while (true) {
            	String input = in.readLine();
            	
            	if (input == null || input.equalsIgnoreCase(".")) 
            		break;
            	
            	out.println(input.toUpperCase());
            }
		} catch (IOException e) {
			log("Error handling client# " + clientNumber + ": " + e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				log("Couldn't close a socket, what's going on?");
			}
			log("Connection with client# " + clientNumber + " closed");
		}
	}
	
	private void log(String string) {
		System.out.println(string);
	}
	
}
