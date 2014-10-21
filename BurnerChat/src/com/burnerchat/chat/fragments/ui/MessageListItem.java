package com.burnerchat.chat.fragments.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.burnerchat.R;

public class MessageListItem extends RelativeLayout {
	private static final String GRAY = "#e6e6e6";
	private static final String WHITE = "#ffffff";

	RelativeLayout parentView;
	TextView name;
	TextView message;
	
	public MessageListItem(Context context) {
		super(context);
		
		parentView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.chat_room_fragment_list_item, this);
		name 	   = (TextView) parentView.findViewById(R.id.msg_list_item_person);
		message    = (TextView) parentView.findViewById(R.id.msg_list_item_person_msg);
	}
	

	public void setAsSender(String name, String message) {
		setNameMessage(name, message);
		setParentBackground(GRAY);
		setMessageGravity(Gravity.RIGHT);
	}
	
	public void setAsReceived(String name, String message) {
		setNameMessage(name, message);
		setParentBackground(WHITE);
		setMessageGravity(Gravity.LEFT);
	}
	
	private void setParentBackground(String color) {
		parentView.setBackgroundColor(Color.parseColor(color));
	}
	
	private void setNameMessage(String name, String message) {
		this.name.setText(name);
		this.message.setText(message);
	}
	
	private void setMessageGravity(int gravity) {
		this.name.setGravity(gravity);
		this.message.setGravity(gravity);
	}
	
}
