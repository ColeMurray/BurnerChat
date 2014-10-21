package com.burnerchat.chat;

import java.util.ArrayList;

import com.burnerchat.sdk.models.BurnerMessage;
import com.burnerchat.sdk.models.BurnerUser;

public class ChatMessageEvent {
	
	public static class OutgoingMessage extends BurnerMessage {
		ArrayList<BurnerUser> recipients;
//		String session;
//		String token;
//		public OutgoingMessage(BurnerMessage msg, String session, String token, ArrayList<BurnerUser> recipients) {
//			super(msg);
//			this.recipients = recipients;
//			this.session = session;
//			this.token = token;
//		}
		
		public OutgoingMessage(BurnerMessage msg, ArrayList<BurnerUser> recipients) {
			super(msg);
			this.recipients = recipients;
		}
		
		public ArrayList<BurnerUser> getRecipients() {
			return this.recipients;
		}
		
//		public String getSession() {
//			return this.session;
//		}
//		
//		public String getToken() {
//			return this.token;
//		}
	}
	
	public static class IncomingMessage extends BurnerMessage {
		
		public IncomingMessage(BurnerMessage msg) {
			super(msg);
		}
	}
	
}
