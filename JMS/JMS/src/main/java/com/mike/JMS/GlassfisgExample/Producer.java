package JMS.JMS.src.main.java.com.mike.JMS.GlassfisgExample;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.BasicConfigurator;

public class Producer {
	public Producer() throws NamingException, JMSException {
		// JNDI Connection
		InitialContext context = new InitialContext();

		// Look up a JMS connecion
		ConnectionFactory factory = (ConnectionFactory)context.lookup("jdbc://localhost:3306/testo");
		Connection connection;

		// Getting JMS connection from the server and starting it
		connection = factory.createConnection();
		try {
			connection.start();

			// JMS messages are sent and received using a Session. We will
			// create here a non-transactional session object. If you want
			// to use transactions you should set the first parameter to 'true'
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination destination = (Destination)context.lookup("MyQueue"); // Destenation

			// MessageProducer is used for sending messages (as opposed
			// to MessageConsumer which is used for receiving them)
			MessageProducer producer = session.createProducer(destination);
			
			// Will send small text message saying "Hallo World";
			TextMessage message = session.createTextMessage("Hallo World");
			
			// Sending the message
			producer.send(message);
			System.out.println("Sent message " + message.getText());
		} finally {
			connection.close();
		}
	}
	
	public static void main(String[] args) throws JMSException {
		try {
			BasicConfigurator.configure();
			new Producer();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
