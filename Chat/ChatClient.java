package Chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {

	private String server;
	private String username;
	private int port;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	public ChatClient(String server, String username, int port) {
		this.server = server;
		this.username = username;
		this.port = port;
	}

	public boolean start() {
		try {
			socket = new Socket(server, port);
		} catch (IOException e) {
			System.out.print("Error connection ");
			e.printStackTrace();
			return false;
		}

		String connected = "Connection accept " + socket.getInetAddress() + ":" + socket.getPort();
		System.out.println(connected);

		try {
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		// Create Thread for listening server
		new ServerListener().start();


		try {
			output.writeObject(username);
		} catch(IOException e) {
			e.printStackTrace();
			close();
			return false;
		}
		return true;
	}

	private void close() {
		try {
			if (output != null) 
				output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (input != null) 
				input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (socket != null) 
				socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void sendMessage(ChatProtocol message) {
		try {
			output.writeObject(message);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		if (args.length < 3 || args.length > 3) 
			throw new IllegalArgumentException("<ServerName> <Username> <Port>");

		String host = args[0];
		String username = args[1];
		int port = Integer.parseInt(args[2]); // by default
//		String host = "localhost";
//		String username = "Mike";
//		int port = 8080;

		ChatClient client = new ChatClient(host, username, port);

		if(!client.start())
			return;

		Scanner scan = new Scanner(System.in);

		while(true) {
			System.out.print("> ");
			String msg = scan.nextLine();

			if(msg.equalsIgnoreCase("LOGOUT")) {
				client.sendMessage(new ChatProtocol(ChatProtocol.LOGOUT, ""));
				break;
			} else if(msg.equalsIgnoreCase("WHOITTHIS"))
				client.sendMessage(new ChatProtocol(ChatProtocol.WHOISTHIS, ""));
			else
				client.sendMessage(new ChatProtocol(ChatProtocol.MESSAGE, msg));
		}
		client.close();
	}

	class ServerListener extends Thread {
		public void run() {
			while(true) {
				try {
					String message;
					message = (String)input.readObject();
					System.out.println(message);
					System.out.print("> ");

				} catch (IOException e) {
					System.out.println("Server has close connection");
					e.printStackTrace();
					break;
				} catch (ClassNotFoundException cl) {
					cl.printStackTrace();
				}
			}
		}
	}
}
