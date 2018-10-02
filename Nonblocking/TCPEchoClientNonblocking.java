package Nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class TCPEchoClientNonblocking {

	public static void main(String[] args) throws IOException {
		if ((args.length < 2) || (args.length > 3))
			throw new IllegalArgumentException("<Server> <Word> [<Port>]");
		
		String server = args[0];
		byte[] argument = args[1].getBytes();
		
		int port = (args.length == 3) ? Integer.parseInt(args[2]) : 7;
		
		// Create channel and set to nonblocking
		SocketChannel clntChan = SocketChannel.open();
		clntChan.configureBlocking(false);
		
		// Connect to server
		if (!clntChan.connect(new InetSocketAddress(server, port))) {
			while (!clntChan.finishConnect()) 
				System.out.println(".");
		}
		
		ByteBuffer writeBuf = ByteBuffer.wrap(argument);
		ByteBuffer readBuf = ByteBuffer.allocate(argument.length);
		int totalBytesRcvd = 0;
		int bytesRcvd;
		
		while (totalBytesRcvd < argument.length) {
			if (writeBuf.hasRemaining())
				clntChan.write(writeBuf);
			if ((bytesRcvd = clntChan.read(readBuf)) == -1) 
				throw new SocketException("Connection closed prematurely");
			
			totalBytesRcvd += bytesRcvd;
			System.out.println(".");
		}
		
		System.out.println("Received: " + new String(readBuf.array(), 0, totalBytesRcvd));
		clntChan.close();
	}
}
