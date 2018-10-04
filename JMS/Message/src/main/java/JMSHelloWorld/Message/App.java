package JMS.Message.src.main.java.JMSHelloWorld.Message;

import java.util.Scanner;

import javax.jms.JMSException;

public class App {
	public static void main(String[] args) throws JMSException {
		final Sender sender = new Sender();

		final Receiver receiver = new Receiver();
		receiver.startListener();
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			String m = sc.nextLine();
			sender.sendMessage(m);
			
			if(m.equalsIgnoreCase("quit")) {
				sender.destroy();
				receiver.destroy();
				
				break;
			}
		}
	}
}
