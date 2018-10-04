package SimpleTCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class TCPEchoServer {

	private static final int BUFSIZE = 32;
	
	public static void main(String [] args) throws IOException, InterruptedException {
		System.out.println("This is server side");
	
		if (args.length != 1)
			throw new IllegalArgumentException("Parameter(s): <Port>");
		
		int servPort = Integer.parseInt(args[0]);
		
		// Create a server socket to accept client connection requests
		ServerSocket servSock = new ServerSocket(servPort);
		
		int recvMsgSize; // Size of received message
		byte[] receiveBuf = new byte[BUFSIZE]; // Receive buffer
		
		while (true) { // Run forever, accepting and servicing connections
			/*
			 * The sole purpose of a ServerSocket instance is to supply a new, 
			 * connected Socket instance for each new incoming TCP connection. 
			 * When the server is ready to handle a client, it calls accept(), 
			 * which blocks until an incoming connection is made to the ServerSocket’s port. 
			 * (If a connection arrives between the time the server socket is constructed and the call to accept(), 
			 * the new connection is queued, and in that case accept() returns immediately. 
			 * The accept() method of ServerSocket returns an instance of Socket that is already 
			 * connected to the client’s remote socket and ready for reading and writing. 
			 */
			Socket clntSock = servSock.accept(); // Get client connection
			
			/*
			 * We can query the newly created Socket instance for the address and port of the connecting client. 
			 * The getRemoteSocketAddress() method of Socket returns an instance of InetSocketAddress that contains the address and port of the client. 
			 * The toString() method of InetSocketAddress prints the information in the form "/(address):(port)". 
			 * (The name part is empty because the instance was created from the address information only.) 
			 */
			SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
			System.out.println("Handling client at " + clientAddress);
			
			/*
			 * Bytes written to this socket’s OutputStream will be read from the client’s socket’s InputStream,
			 * and bytes written to the client’s OutputStream will be read from this socket’s InputStream.
			 */
			InputStream in = clntSock.getInputStream();
			OutputStream out = clntSock.getOutputStream();

			/*
			 *  Receive until client closes connection, indicated by -1 return
			 *  
			 *  The while loop repeatedly reads bytes (when available) from the input stream and
			 *  immediately writes the same bytes back to the output stream until the client closes the connection.
			 *  The read() method of InputStream fetches up to the maximum number of bytes the array can hold(in this case,BUFSIZE bytes) in
			 *  to the byte array(receiveBuf)and returns the number of bytes read. 
			 *  read() blocks until data is available and returns−1 if there is no more data available, 
			 *  indicating that the client closed its socket. In the echo protocol, 
			 *  the client closes the connection when it has received the number of bytes that it sent, 
			 *  so in the server we expect to eventually receive a −1 from read(). 
			 *  (Recall that in the client, receiving a −1 from read() indicates a protocol error, 
			 *  because it can only happen if the server prematurely closed the connection.) As previously mentioned, 
			 *  read() does not have to fill the entire byte array to return. In fact, it can return after having read only a single byte. 
			 *  This write() method of OutputStream writes recvMsgSize bytes from receiveBuf to the socket. 
			 *  The second parameter indicates the offset into the byte array of the first byte to send. In this case, 
			 *  0 indicates to take bytes starting from the front of data. If we had used the form of write() 
			 *  that takes only the buffer argument, all the bytes in the buffer array would have been transmitted, 
			 *  possibly including bytes that were not received from the client. 
			 */
			while ((recvMsgSize = in.read(receiveBuf)) != -1) {
				out.write(receiveBuf, 0, recvMsgSize);
			}
			clntSock.close();
		}
	}
}
