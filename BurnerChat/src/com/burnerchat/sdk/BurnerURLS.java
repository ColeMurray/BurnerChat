package com.burnerchat.sdk;

public interface BurnerURLS {
	public interface SocketIO {
		public static final String URL = "http://162.219.7.18:17584";
		//public static final String URL = "http://199.58.85.73:17584";
		//public static final String URL = "http://192.168.1.103:17584";
	}
	
	public interface HTTP {
		public static final String URL = BurnerURLS.SocketIO.URL + "/";
	}
}
