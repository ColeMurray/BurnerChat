package com.burnerchat.chat.fragments;

import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.burnerchat.R;
import com.burnerchat.chat.chatroom.message.MessageListAdapter;
import com.burnerchat.chat.fragments.ui.MessageListView;
import com.burnerchat.sdk.models.BurnerMessage;
import com.burnerchat.sdk.models.BurnerRoom;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

public class ChatRoomFragment extends ChatBaseFragment {
	
	private BurnerRoom mRoom;
	private BootstrapButton mSend;
	private BootstrapEditText mMessage;
	private MessageListView mListView;
	
	private AnimationAdapter mAnimAdapter;
	private MessageListAdapter mAdapter = null;
	
	private int myUserId;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.chat_room_fragment, container, false);
        
		mSend	 	= (BootstrapButton) rootView.findViewById(R.id.chat_room_send);
		mMessage 	= (BootstrapEditText) rootView.findViewById(R.id.chat_room_message);
		mListView 	= (MessageListView) rootView.findViewById(R.id.chat_room_listview);
		
		myUserId = getUser().getUserId();
		
		setListView();
        setOnClickListeners();
        
        return rootView;
	}

	private void setListView() {
		if (mRoom == null) return;
		
		mAdapter = new MessageListAdapter(getActivity(), mRoom.getMessages(), mRoom, myUserId);
		mAnimAdapter = new SwingBottomInAnimationAdapter(mAdapter);
        mAnimAdapter.setAbsListView(mListView);
        mListView.setAdapter(mAnimAdapter);
		mListView.setSelectionFromTop(mRoom.getMessages().size()-1, 0);
	}

	private void setOnClickListeners() {
		mSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mRoom == null) return;
				final String msg = mMessage.getText().toString();
				// Clear input message from edit text
				mMessage.setText("");
				
				if (msg != null && msg.length() > 0) {
					BurnerMessage bm = new BurnerMessage(msg, "Me", myUserId,  mRoom.getRoomID());
					mRoom.addMessage(bm);
					notifyChange();
					getCallback().onMessageSend(bm);
				}
				
			}
		});
		
	}

	public void setChatRoom(BurnerRoom chatroom) {
		this.mRoom = chatroom;
		if (mAdapter == null) {
			setListView();
		}
		setListView();
	}
	
	
	public void notifyChange() {
		mAnimAdapter.notifyDataSetChanged(); 
		mListView.smoothScrollToPosition(mRoom.getMessages().size()-1);
	}
	
	public int getCurrentRoomId() {
		if (mRoom != null) {
			return mRoom.getRoomID();
		}
		return -1;
	}
	
	public String getCurrentRoomName() {
		if (mRoom != null) {
			return mRoom.getName();
		}
		return null;
	}
	
	public IBinder getWindowToken() {
		return mMessage.getWindowToken();
	}
	
}
