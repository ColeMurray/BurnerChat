package com.burnerchat.chat;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;

import com.burnerchat.sdk.models.BurnerMessage;
import com.burnerchat.sdk.models.BurnerRoom;

public class ChatManager {
	private static ChatManager instance = null;
	private ArrayList<BurnerRoom> chatRooms;
	
	private ChatManager() {
		chatRooms = new ArrayList<BurnerRoom>();
	}

	public static ChatManager getInstance(Context context) {
		if (instance == null) {
			synchronized (ChatManager.class) {
				if (instance == null) {
					instance = new ChatManager();
				}
			}
		}
		return instance;
	}

	public synchronized ArrayList<BurnerRoom> getChatRooms() {
		return this.chatRooms;
	}
	
	public synchronized BurnerRoom getLastRoom() {
		if (this.chatRooms.isEmpty()) {
			return null;
		}
		return this.chatRooms.get(this.chatRooms.size()-1);
	}
	
	public synchronized BurnerRoom getRoomById(long id) {
		if (this.chatRooms == null) return null;
		for (BurnerRoom br : this.chatRooms) {
			if (br.getRoomID() == id) {
				return br;
			}
		}
		return null;
	}
	
	public synchronized void addRoom(BurnerRoom room) {
		if (this.getRoomById(room.getRoomID()) == null) {
			chatRooms.add(room);
		}
	}
	
	public synchronized void addMessage(BurnerMessage msg) {
		BurnerRoom room = this.getRoomById(msg.getRoomId());
		if (room != null) {
			room.addMessage(msg);
		}
	}
	
	protected synchronized static void clear() {
		ChatManager.instance = null;
	}
	
	protected synchronized void burnUser(final int uid) {
		if (this.chatRooms == null) return;
		for (BurnerRoom br : this.chatRooms) {
			Iterator<BurnerMessage> it = br.getMessages().iterator();
			while (it.hasNext()) {
			    BurnerMessage bm = it.next();
			    if (bm.getSenderId() == uid){
			    	it.remove();
			    }
			}
		}
	}

}
