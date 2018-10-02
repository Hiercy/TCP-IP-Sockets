package Selectors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class TCPServerSelector {

	private static final int BUFSIZE = 256; // Buffer Size (bytes)
	private static final int TIMEOUT = 3000; // Wait timeout
	
	public static void main(String[] args) throws IOException {
		if (args.length < 1)
			throw new IllegalArgumentException("<Port>");
		
		// Create a selector to multiplex listening sockets and connections
		Selector selector = Selector.open();
		
		// Create listening socket channel for each port and register selector
		for (String arg : args) {
			ServerSocketChannel listChannel = ServerSocketChannel.open();
			listChannel.socket().bind(new InetSocketAddress(Integer.parseInt(arg)));
			listChannel.configureBlocking(false); // only nonblocking channel can register selectors
			
			// Register selector with channel. The returned key is ignored
			listChannel.register(selector, SelectionKey.OP_ACCEPT);
		}
		
		// Create a handle that will implement the protocol
		TCPProtocol protocol = new EchoSelectorProtocol(BUFSIZE);
		
		while(true) {
			// Wait for some channel to be ready (or timeout)
			if (selector.select(TIMEOUT) == 0) {
				System.out.println(".");
				continue;
			}
			
			// Get iterator on set of keys with I/O to process
			Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
			
			while(keyIter.hasNext()) {
				SelectionKey key = keyIter.next();
				
				// Server socket channel has pending connection request?
				if (key.isAcceptable())
					protocol.handleAccept(key);
				
				// Client socket channel has pending data?
				if (key.isReadable()) 
					protocol.handleRead(key);
				
				// Client socket channel is available for writing and
				// key is valid (channel not closed)?
				if (key.isValid() && key.isWritable())
					protocol.handleWrite(key);
				
				keyIter.remove();
			}
		}
	}
}
