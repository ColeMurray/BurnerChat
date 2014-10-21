package com.burnerchat.sdk.exceptions;

public class BurnerAppUserNotSetException extends Exception {

	private static final long serialVersionUID = 3239864251119293887L;

	public BurnerAppUserNotSetException() {}
	
	public BurnerAppUserNotSetException(String message) {
		super(message);
	}

}
