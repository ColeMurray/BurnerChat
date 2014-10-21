package com.burnerchat.chat.fragments;

import android.app.Fragment;
import android.os.Handler;

import com.burnerchat.app.BurnerAppUser;
import com.burnerchat.chat.ChatManager;
import com.burnerchat.sdk.models.BurnerMessage;
import com.burnerchat.sdk.models.BurnerRoom;

public abstract class ChatBaseFragment extends Fragment {
	public interface ChatCallback {
		public void onBurnClick();
		public void onRoomSelected(BurnerRoom chatroom);
		void onMessageSend(BurnerMessage message);
		void onError(String error);
		void onRoomAdded();
	}
	
	protected ChatCallback mCallback = null;
	protected final Handler mHandler = new Handler();
	
	public void setChatCallbackListener(ChatCallback callback) {
		this.mCallback = callback;
	}
	
	protected ChatCallback getCallback() {
		return this.mCallback;
	}
	
	protected ChatManager getChatManager() {
		return ChatManager.getInstance(getActivity());
	}
	
	protected BurnerAppUser getUser() {
		return BurnerAppUser.getInstance(getActivity());
	}

}
