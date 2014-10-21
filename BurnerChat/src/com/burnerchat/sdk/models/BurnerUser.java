package com.burnerchat.sdk.models;

public class BurnerUser {

	private String name;
	private String publicKey;
	private int userId;

	public BurnerUser() {
		name = "";
		userId = 0;
		publicKey = null;
	}

	public BurnerUser(int userID, String name, String publicKey) {
		this.name = name;
		this.userId = userID;
		this.publicKey = publicKey;
	}

	public String getUsername() {
		return name;
	}

	public int getUserId() {
		return userId;
	}

	public String getPublicKey() {
		return publicKey;
	}

}
