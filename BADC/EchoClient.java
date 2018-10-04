package BADC;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class EchoClient {
	public static void main(String[] args) throws IOException {
		DatagramChannel channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(8080));
		
		String text = "New String to ... bla-bla. Time: " + System.currentTimeMillis();
		ByteBuffer buf = ByteBuffer.allocate(256);
		buf.clear();
		buf.put(text.getBytes());
		buf.flip();
		
		int bytesSend = channel.send(buf, new InetSocketAddress("localhost", 8080));
	}
}
