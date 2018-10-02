package Selectors;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class EchoSelectorProtocol implements TCPProtocol {

	private int bufSize;
	
	public EchoSelectorProtocol(int bufSize) {
		this.bufSize = bufSize;
	}
	
	@Override
	public void handleAccept(SelectionKey key) throws IOException {
		SocketChannel clntChan = ((ServerSocketChannel)key.channel()).accept();
		clntChan.configureBlocking(false);
		
		// Register the selector with new channel for read and attach byte buffer
		clntChan.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));
	}

	@Override
	public void handleRead(SelectionKey key) throws IOException {
		// Client socket channel has pending data
		SocketChannel clntChan = (SocketChannel)key.channel();
		ByteBuffer buf = (ByteBuffer)key.attachment();
		long bytesRead = clntChan.read(buf);
		
		if (bytesRead == -1) // Did the other end close?
			clntChan.close();
		else if (bytesRead > 0)
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE); // Indicate via key that reading/writing
	}

	@Override
	public void handleWrite(SelectionKey key) throws IOException {
		// Retrieve data read earlier
		ByteBuffer buf = (ByteBuffer)key.attachment();
		buf.flip(); // Prepare buffer for writing
		SocketChannel clntChan = (SocketChannel) key.channel();
		clntChan.write(buf);
		
		if (!buf.hasRemaining()) // Buffer completely written?
			key.interestOps(SelectionKey.OP_READ); // Nothing left, so no longer interested in writes
		
		buf.compact(); // Make room for more data to be read in
	}
}
