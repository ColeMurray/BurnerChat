package com.burnerchat.sdk;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.burnerchat.sdk.models.BurnerMessage;
import com.burnerchat.sdk.models.BurnerRoom;
import com.burnerchat.sdk.models.BurnerUser;

public class BurnerParser {
	
	public static BurnerUser parseUser(JSONObject user) throws Exception, JSONException {
		String pubkey 	= user.getString(BurnerResultKeys.PUBKEY);
		String username = user.getString(BurnerResultKeys.USERNAME);
		int userId 		= user.getInt(BurnerResultKeys.ID);
		return new BurnerUser(userId, username, pubkey);
	}
	
	public static ArrayList<BurnerMessage> parseMessages(JSONObject result) throws Exception, JSONException {
		ArrayList<BurnerMessage> msgList = new ArrayList<BurnerMessage>();
		JSONArray messages = result.getJSONArray(BurnerResultKeys.MESSAGE);
		for (int i = 0; i < messages.length(); ++i) {
			JSONObject message = messages.getJSONObject(i);
			BurnerMessage bm = BurnerParser.getMessage(message);
			msgList.add(bm);
		}
		return msgList;
	}
	
	private static BurnerMessage getMessage(JSONObject message) throws Exception, JSONException {
		String msg = message.getString(BurnerResultKeys.MESSAGE);
		String usr = message.getString(BurnerResultKeys.USERNAME);
		int from   = message.getInt(BurnerResultKeys.FROMID);
		int room   = message.getInt(BurnerResultKeys.ROOMID);
		return new BurnerMessage(msg, usr, from, room);
	}
	
	public static BurnerRoom parseRoom(JSONObject result) throws Exception, JSONException {
		JSONArray users = result.getJSONArray("users");
		JSONObject room = result.getJSONObject("room");
		
		String roomName   = room.getString("name");
		final int roomId  = room.getInt("id");
		
		BurnerRoom chatRoom = new BurnerRoom(roomName, roomId);
		
		for (int i = 0; i < users.length(); ++i) {
			JSONObject user = users.getJSONObject(i);
			chatRoom.addUser(BurnerParser.parseUser(user));
		}
		
		return chatRoom;
	}
	
	public static BurnerMessage parseSocketMessage(JSONArray msg) throws Exception, JSONException {
		JSONObject message = msg.getJSONObject(0);
		return BurnerParser.getMessage(message);
	}
	
	public static int parseBurnedUser(JSONArray msg) throws Exception, JSONException {
		JSONObject message = msg.getJSONObject(0);
		return message.getInt("userid");
	}
}
