package org.ndywicki.chat.json;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("message")
public class Message extends Event {

	private User user;
	private int h;
	private int m;
	private String message;

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public int getM() {
		return m;
	}
	public void setM(int m) {
		this.m = m;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Message [event=").append(event).append(", user=")
				.append(user).append(", h=").append(h).append(", m=").append(m)
				.append(", message=").append(message).append("]");
		return builder.toString();
	}
		
}
