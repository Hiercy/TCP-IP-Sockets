package Chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChatServer {

	private static int ID; 
	private SimpleDateFormat dates;
	private ArrayList<Client> clntList;
	private boolean isAlive;
	private int port = 8080; // By default port = 8080

	public ChatServer(int port) {
		this.port = port;
		dates = new SimpleDateFormat("HH:mm:ss");
		clntList = new ArrayList<>();
	}

	public void start() {
		isAlive = true;

		try {
			ServerSocket server = new ServerSocket(port);
			while(isAlive) {
				System.out.println("Waiting to client connection");
				Socket socket = server.accept();

				if (!isAlive) 
					break;

				Client client = new Client(socket); // Make a thread for client
				clntList.add(client); // Add new client in ArrayList
				client.start(); // And start new thread for new client
			}

			try {
				server.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			System.out.println("Error closing server" + ex);
		}
	}

	private synchronized void send(String message) {
//		System.out.println("METHOD SEND");
		String time = dates.format(new Date());
		String fullMsg = time + " " + message + "\n";

//		if (clntList == null)
			System.out.println(fullMsg);

		for (int i = 0; i < clntList.size(); i++) {
			Client client = clntList.get(i);
		}
	}

	public static void main(String[] args) {
		if (args.length != 1) 
			throw new IllegalArgumentException("<Port>");

		int portNumber = Integer.parseInt(args[0]);

		ChatServer server = new ChatServer(portNumber);
		server.start();
	}

	class Client extends Thread {

		int id;
		Socket socket;
		//		InputStream input;
		ObjectInputStream input;
		//		OutputStream output;
		ObjectOutputStream output;
		String username;
		ChatProtocol messages;
		String date;

		// DONE
		public Client(Socket socket) {
			this.socket = socket;
			date = new Date().toString() + "\n";
			id = ID++;
			
			System.out.println("Thread trying to create Object Input/Output Streams");
			try {
//				System.out.println("IN TRY BLOCK");
				//				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				output = new ObjectOutputStream(socket.getOutputStream()); // OUTPUT MUST BE FIRST
				input = new ObjectInputStream(socket.getInputStream());

				// readLine() - deprecated
				username = (String)input.readObject();
				System.out.println(username + " connected");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
			date = new Date().toString() + "\n";
		}

		@Override
		public void run() {
			boolean isAlive = true;

			while(isAlive) {
				try { // Trying read string
					//					BufferedReader reader = new BufferedReader(new InputStreamReader(input));
					messages = (ChatProtocol)input.readObject();
//					System.out.println("RUN METHOD");
				} catch (IOException e) {
					e.printStackTrace();
					break;
				} catch (ClassNotFoundException cnfe) {
					cnfe.printStackTrace();
					break;
				}

				String message = messages.getMessage();
				
				switch(messages.getType()) {
				case ChatProtocol.MESSAGE:
//					System.out.println("IN SWITCH BLOCK");
					send(username + ": " + message);
					break;
				case ChatProtocol.WHOISTHIS:
					System.out.println("List of the users in" + dates.format(new Date()) + "\n");
					for (int i = 0; i < clntList.size(); i++) {
						Client client = clntList.get(i);
						System.out.println("#" + i + " " + client.username + " in " + client.date);
					}
					break;
				case ChatProtocol.LOGOUT:
					System.out.println(username + " disconnected");
					isAlive = false;
					break;
				default:
					break;
				}
			}
			close();
		}

		// Try to close any open connection
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
	}
}
