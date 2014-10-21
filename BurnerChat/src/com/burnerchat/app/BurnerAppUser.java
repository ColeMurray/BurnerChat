package com.burnerchat.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class BurnerAppUser {
	public static final String LOGGED_IN = "LOGGED_IN";
	public static final String PRIVATE 	 = "private";
	public static final String PUBLIC 	 = "public";
	public static final String SESSION 	 = "session";
	public static final String TOKEN 	 = "token";
	public static final String USER_ID 	 = "userid";
	public static final String ROOMS 	 = "rooms";

	
	private Context mContext;
	private static BurnerAppUser instance = null;
	
	private BurnerAppUser(Context context) {
		this.mContext = context;
	}
	
	public static BurnerAppUser getInstance(Context context) {
		if (instance == null) {
			synchronized (BurnerAppUser.class) {
				if (instance == null) {
					instance = new BurnerAppUser(context);
				}
			}
		}
		return instance;
	}
	
	public synchronized boolean isLoggedIn() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		return sp.getBoolean(LOGGED_IN, false);
	}
	
	public synchronized String getSession() {
		return getFromPrefs(SESSION);
	}
	
	public synchronized String getToken() {
		return getFromPrefs(TOKEN);
	}
	
	public synchronized String getPublicKey() {
		return getFromPrefs(PUBLIC);
	}
	
	public synchronized String getPrivateKey() {
		return getFromPrefs(PRIVATE);
	}
	
	public synchronized int getUserId() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		return sp.getInt(USER_ID, -1);
	}
	
	public synchronized void setUserId(int id) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		// Save user is logged in
        Editor editor = sp.edit();
        editor.putInt(USER_ID, id);
        editor.commit();
	}
	
	public synchronized void setUser(String session, String token, String key, int id) {
		setSession(session);
		setToken(token);
		setPublicKey(key);
		setPrivateKey(key);
		setUserId(id);
		setLoggedIn(true);
	}
	
	public synchronized void clearUser() {
		setSession(null);
		setToken(null);
		setPublicKey(null);
		setPrivateKey(null);
		setRoomsEmpty();
		setUserId(-1);
		setLoggedIn(false);
	}
	
	public synchronized void addRoom(int id) {
		String rid = null;
		try {
			 rid = String.valueOf(id);
		} catch (Exception e) {
			return;
		}
		
		String current = getFromPrefs(ROOMS);
		if(current == null) {
			saveInPrefs(ROOMS, rid);
		} else {
			saveInPrefs(ROOMS, current + " " + rid);
		}
	}
	
	public synchronized String[] getRooms() {
		String rooms = getFromPrefs(ROOMS);
		if(rooms == null) {
			return null;
		}
		return rooms.split(" ");
	}
	
	public synchronized boolean isInRoom(int room) {
		for(String r : getRooms()) {
			if (r.equals(String.valueOf(room))) {
				return true;
			}
		}
		return false;
	}
	
	private void setRoomsEmpty() {
		saveInPrefs(ROOMS, null);
	}
	private void setSession(String s) {
        saveInPrefs(SESSION, s);
	}
	
	private void setToken(String s) {
		saveInPrefs(TOKEN, s);
	}
	
	private void setPublicKey(String s) {
		saveInPrefs(PUBLIC, s);
	}
	
	private void setPrivateKey(String s) {
		saveInPrefs(PRIVATE, s);
	}
	
	private void setLoggedIn(boolean status) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		// Save user is logged in
        Editor editor = sp.edit();
        editor.putBoolean(LOGGED_IN, status);
        editor.commit();
	}
	
	private void saveInPrefs(final String KEY, String value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        Editor editor = sp.edit();
        editor.putString(KEY, value);
        editor.commit();
	}
	
	private String getFromPrefs(final String KEY) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		return sp.getString(KEY, null);
	}
}
