package com.burnerchat.sdk.models;

import java.util.ArrayList;
import java.util.Hashtable;

import android.util.Log;


public class BurnerRoom {
	private String name;
	private int roomID;
	private Hashtable<Integer, BurnerUser> userTable;
	private ArrayList<BurnerMessage> messageList;

	public BurnerRoom(String roomName, int roomID) {
		this.name = roomName;
		this.roomID = roomID;
		
		userTable = new Hashtable<Integer, BurnerUser>();
		messageList = new ArrayList<BurnerMessage>();
	}

	public void addMessage(BurnerMessage msg) {
		messageList.add(msg);
	}
	
	public void addMessage(String message, String usr, int from) {
		this.addMessage(new BurnerMessage(message, usr, from, this.roomID));
	}

	public void addUser(BurnerUser user) {
		Log.v("BURNER USER ADD", user.getUsername());
		userTable.put(user.getUserId(), user);
	}
	
	public void addUser(String username, String publickey, int id) {
		this.addUser(new BurnerUser(id, username, publickey));
	}

	public String getName() {
		return name;
	}

	public ArrayList<BurnerUser> getUserList() {
		ArrayList<BurnerUser> list = new ArrayList<BurnerUser>();
		list.addAll(userTable.values());
		return list;
	}
	
	public ArrayList<BurnerMessage> getMessages() {
		return messageList;
	}

	public BurnerUser getChatUser(int id) {
		return userTable.get(id);
	}
	
	public boolean hasUser(int id) {
		return userTable.containsKey(id);
	}

	public int getRoomID() {
		return roomID;
	}

	public void addMessages(ArrayList<BurnerMessage> messages) {
		this.messageList.addAll(messages);
	}
	
	public void removeMessage(int index) {
		this.messageList.remove(index);
	}

}
