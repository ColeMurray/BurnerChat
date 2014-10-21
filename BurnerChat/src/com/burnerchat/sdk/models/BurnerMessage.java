package com.burnerchat.sdk.models;


/* This class is an object used to store message to and from the server
 */

public class BurnerMessage {
	private String message;
	private String username;
	private int roomId;
	private int sender;

	public BurnerMessage(String msg, String username, int sender, int roomId ) {
		this.message = msg;
		this.username = username;
		this.sender = sender;
		this.roomId = roomId;
	}

	public BurnerMessage(BurnerMessage msg) {
		this(msg.getMessage(),
			 msg.getName(),
			 msg.getSenderId(),
			 msg.getRoomId());
	}

	public String getMessage() {
		return message;
	}
	
	public String getName() {
		return this.username;
	}
	
	public int getSenderId() {
		return this.sender;
	}
	
	public int getRoomId() {
		return this.roomId;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
