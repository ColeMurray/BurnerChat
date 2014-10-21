package com.burnerchat.chat.chatroom.message;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.burnerchat.chat.fragments.ui.MessageListItem;
import com.burnerchat.sdk.models.BurnerMessage;
import com.burnerchat.sdk.models.BurnerRoom;

public class MessageListAdapter extends ArrayAdapter<BurnerMessage> {
	private BurnerRoom room;
	private int myId;
	
	public MessageListAdapter(Context context, ArrayList<BurnerMessage> objects, BurnerRoom room, int me) {
		super(context, 0, objects);
		this.room = room;
		this.myId = me;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		
		MessageListItem mli = (MessageListItem) convertView;
		if (mli == null) {
			mli = new MessageListItem(getContext());
		}
		
		BurnerMessage message = getItem(position);

		if (message.getSenderId() == this.myId) {
			mli.setAsSender("Me", message.getMessage());
		} else {
			String from = room.getChatUser(message.getSenderId()).getUsername();
			if (from == null) from = "User";
			mli.setAsReceived(from, message.getMessage());
		}
		return mli;
	}

}
