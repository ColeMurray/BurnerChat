package com.burnerchat.sdk;

import java.util.ArrayList;

import com.burnerchat.sdk.errors.BurnerError;
import com.burnerchat.sdk.models.BurnerMessage;
import com.burnerchat.sdk.models.BurnerRoom;
import com.burnerchat.sdk.models.BurnerUser;

public class BurnerListener {
	public interface BurnerMessageResultListener {
		public void onSuccess(ArrayList<BurnerMessage> messages);
		public void onError(BurnerError e);
	}
	
	public interface BurnerRoomResultListener {
		public void onSuccess(BurnerRoom room);
		public void onError(BurnerError e);
	}
	
	public interface BurnerUserInfoListener {
		public void onSuccess(BurnerUser user);
		public void onError(BurnerError e);
	}
	
	public interface BurnerHTTPListener {
		public void onSuccess();
		public void onError(BurnerError e);
	}
}
