package o2a.java.serverless.ref.impl.model;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class Response {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Response [message=" + message + "]";
	}
	
}
