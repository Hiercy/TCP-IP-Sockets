package Chat;

import java.io.Serializable;

public class ChatProtocol implements Serializable{
	private static final long serialVersionUID = -3550157993007949386L;
	
	static final int MESSAGE = 1, WHOISTHIS = 0, LOGOUT = 2; 
	private int type;
	private String message;
	
	public ChatProtocol(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public int getType() {
		return this.type;
	}
	
	public String getMessage() {
		return this.message;
	}
}
