package com.burnerchat.sdk.errors;

public class BurnerError {
	private String message;
	
	public BurnerError(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
