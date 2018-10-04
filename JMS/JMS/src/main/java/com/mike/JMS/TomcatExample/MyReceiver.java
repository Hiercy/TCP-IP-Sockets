package JMS.JMS.src.main.java.com.mike.JMS.TomcatExample;

import java.io.IOException;
import java.util.Optional;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MyReceiver", urlPatterns = "/receive")
public class MyReceiver extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Optional<String> text;
	
	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		Optional<String> text = receiveMessages();
		resp(httpServletResponse, text);
	}

	private void resp(HttpServletResponse response, Optional<String> text) throws IOException {
		response.setContentType("text/plain");
		
		if (text.isPresent()) 
			response.getWriter().write(String.format("Received message %s", text.get()));
		 else 
			response.getWriter().write("No message");
		
	}

	private Optional<String> receiveMessages() {
		try {
			InitialContext ctx = new InitialContext();
			
			QueueConnectionFactory factory = (QueueConnectionFactory)ctx.lookup("java:comp/env/jms/ConnectionFactory");
			QueueConnection connection = factory.createQueueConnection();
			
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = (Queue) ctx.lookup("java:comp/env/jms/queue/MyQueue");
			
			QueueReceiver receiver = session.createReceiver(queue);
			
			try {
				connection.start();
				
				Message msg = receiver.receive(1000);
				if (msg != null && msg instanceof TextMessage) {
					TextMessage textMsg = (TextMessage) msg;
					text = Optional.of(textMsg.getText());
					System.out.printf("Received TextMessage %s", text);
				} else {
					System.out.printf("No TextMessage received: %s", msg);
				}
			} finally {
				session.close();
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
}
