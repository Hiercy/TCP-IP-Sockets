package SimpleTCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class TCPEchoClient {
	private static String helloMessage = "Hello Client side";
	
	public static void main(String[] args) throws IOException {
		System.out.println("Client side");
		// Application setup and parameter parsing: line 11-20
	    if ((args.length < 2) || (args.length > 3)) // Test for correct # of args
	    	throw new IllegalArgumentException("Parameter(s): <Server> <Word> [<Port>]");
	    
	    String server = args[0]; // Server name or IP address
	    
	    /*
	     * Convert arg String to bytes using the default character encoding
	     * 
	     * TCP sockets send and receive sequences of bytes. The getBytes() method of String
	     * returns a byte array representation of the string
	     */
	    
	    byte[] data = args[1].getBytes(); // Convert the echo strubstring
	    
	    /*
	     * Determine the port of the echo server
	     * 
	     * The default echo port is 7. If we specify a third parameter, 
	     * Integer.parseInt() takes the string and returns the equivalent value
	     */
//	    int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;

	    /*
	     * Create socket that is connected to server on specified port
	     * 
	     * The socket constructor creates a socket and connects it to the specified server, 
	     * identified either by name or IP address, before returning. Note that the underlying 
	     * TCP deals only with IP addresses; If a name is given, 
	     * the implementation resolves it to the corresponding address.
	     * If the connection attempt fails for any reason, the constructor throws an IOException
	     */
	    Socket socket = new Socket(server, 8080);
	    System.out.println("Connected to server...sending echo string");
	    socket.close();
	    /*
	     * Get socket input and output streams
	     * 
	     * Associated with each connected Socket instance is an InputStream and an OutputStream.
	     * We send data over the socket by writing bytes to the OutputStream just as we would any
	     * other stream, and we receive by reading from the InputStream
	     */
	    InputStream in = socket.getInputStream();
	    OutputStream out = socket.getOutputStream();
	   
	    /*
	     * Send the string to echo server
	     * 
	     * The write() method of OutputStream transmits the given bytes array over the connection to the server.
	     */
	    out.write(data); // Send the encoded string to the server
	    out.write(helloMessage.getBytes());
	    out.flush();
	    
	    // Receive the same string back from the server
	    int totalBytesRcvd = 0; // Total bytes received so far
	    int bytesRcvd;			// Bytes received in last read
	    
	    /*
	     * The loop simply fills up data until we receive as many bytes as we sent. 
	     * If the TCP connection is closed by the other end,
	     * read() returns −1. For the client, this indicates that the server prematurely closed the socket. 
	     */
	    while(totalBytesRcvd < data.length) {
	    	if((bytesRcvd = in.read(data, totalBytesRcvd, data.length - totalBytesRcvd)) == -1)
	    		throw new SocketException("Connected closed prematurely");
	    	
	    	totalBytesRcvd += bytesRcvd;
	    } // data array is full
	    
	    System.out.println("Hello message: " + new String(data));
	    System.out.println("Received: " + new String(data));
	    
//	    socket.close();
	}
}
