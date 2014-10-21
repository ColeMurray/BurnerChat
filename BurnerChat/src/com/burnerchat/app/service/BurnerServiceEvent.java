package com.burnerchat.app.service;

import com.burnerchat.sdk.models.BurnerMessage;

public class BurnerServiceEvent {
	public static class ShowNotifications {
		private boolean show;

		public ShowNotifications(boolean show) {
			this.show = show;
		}

		public boolean show() {
			return this.show;
		}
	}

	public static class NotifyMessage extends BurnerMessage {
		public NotifyMessage(BurnerMessage message) {
			super(message);
		}
	}
	
	public static class UpdateConnect {
		public UpdateConnect() {
			
		}
	}
}
