package server.message;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	private String from;
	private String to;
	private String text;
	private Date sent;

	public Message(String from, String to, String text, Date sent) {
		this.from = from;
		this.to = to;
		this.text = text;
		this.sent = sent;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getSent() {
		return sent;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}

}
