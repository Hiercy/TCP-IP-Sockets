package JMS.JMS.src.main.java.com.mike.JMS.TomcatExample;

import java.io.IOException;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MySender", urlPatterns = "/send")
public class MySender extends HttpServlet {
	private static final long serialVersionUID = -1615148573229856664L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String parameter = getTextParameter(request);
		sendMessage(parameter);
		resp(response, parameter);
	}

	private void resp(HttpServletResponse response, String msg) throws IOException {
		response.setContentType("text/plain");
		response.getWriter().write(String.format("Sent message %s", msg));
	}

	private String getTextParameter(HttpServletRequest request) {
		String parameter = request.getParameter("text");
		if(parameter == null) {
			parameter = (new Date()).toString();
		}
		return parameter;
	}

	private void sendMessage(String text) {
		try {
			InitialContext ctx = new InitialContext();
			ConnectionFactory connectionFactory = (ConnectionFactory) ctx.lookup("java:comp/env/jms/ConnectionFactory");
			Connection connection = connectionFactory.createConnection();
			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			MessageProducer producer = session.createProducer((Destination) ctx.lookup("java:comp/env/jms/queue/MyQueue"));

			TextMessage msg = session.createTextMessage();
			msg.setText(text);
			msg.setStringProperty("aKey", "someRandomTestValue");
			producer.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
