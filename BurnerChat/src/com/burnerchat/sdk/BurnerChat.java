package com.burnerchat.sdk;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.burnerchat.app.BurnerAppUser;
import com.burnerchat.sdk.BurnerListener.BurnerHTTPListener;
import com.burnerchat.sdk.BurnerListener.BurnerMessageResultListener;
import com.burnerchat.sdk.BurnerListener.BurnerRoomResultListener;
import com.burnerchat.sdk.BurnerListener.BurnerUserInfoListener;
import com.burnerchat.sdk.errors.BurnerError;
import com.burnerchat.sdk.exceptions.BurnerAppUserNotSetException;
import com.burnerchat.sdk.models.BurnerMessage;
import com.burnerchat.sdk.models.BurnerRoom;
import com.burnerchat.sdk.models.BurnerUser;
import com.koushikdutta.async.http.Multimap;

public class BurnerChat {

	public interface HTTPRequestListener {
		void onComplete(JSONObject result);
	}
	
	private Context context;
	private static BurnerChat instance = null;
	
	private BurnerChat(Context context) {
		this.context = context;
	}
	
	public static BurnerChat getInstance(Context context) {
		if (instance == null) {
			synchronized (BurnerChat.class) {
				if (instance == null) {
					instance = new BurnerChat(context);
				}
			}
		}
		return instance;
	}

	public void createUser(String username, String publicKey,
			final BurnerHTTPListener callback) {
		Multimap mm = new Multimap();
		mm.add("publicKey", publicKey);
		mm.add("username", username);
		new BurnerHTTP(BurnerHTTP.USER_CREATE, mm, new HTTPRequestListener() {
			
			@Override
			public void onComplete(JSONObject result) {
				BurnerError err = checkResultForErrors(result);
				final String key = "$-fSDF=DSFSdf=SD_F=S+_Df=-SDF+-=SDf=SDFsdf*&SDF6DS^%$";
				if (err != null) {
					callback.onError(err);
					return;
				}
				try {
					String rSession = result.getString(BurnerResultKeys.SESSION);
					String rToken = result.getString(BurnerResultKeys.TOKEN);
					int rId = result.getInt(BurnerResultKeys.USERID);
					
					BurnerAppUser.getInstance(context).setUser(rSession, rToken, key, rId);
					
					callback.onSuccess();
					
				} catch (JSONException e)  {
					e.printStackTrace();
					callback.onError(new BurnerError("Error Occured"));
				}
				
			}
		});
	}

	public void burnUser(final BurnerHTTPListener callback) throws BurnerAppUserNotSetException {
		Multimap mm = getMultiMap();
		new BurnerHTTP(BurnerHTTP.USER_BURN, mm, new HTTPRequestListener() {
			
			@Override
			public void onComplete(JSONObject result) {
				BurnerError err = checkResultForErrors(result);
				if (err != null) {
					callback.onError(err);
					return;
				}
				
				callback.onSuccess();
			}
		});
	}

	public void enterRoom(String name, String password, final BurnerRoomResultListener callback) throws BurnerAppUserNotSetException {
		Multimap mm = getMultiMap();
		mm.add("room_name", name);
		mm.add("room_pass", password);
		new BurnerHTTP(BurnerHTTP.ROOM_CREATE, mm, new HTTPRequestListener() {
			
			@Override
			public void onComplete(JSONObject result) {
				BurnerError err = checkResultForErrors(result);
				if (err != null) {
					callback.onError(err);
					return;
				}
				
				try {
					final BurnerRoom bRoom = BurnerParser.parseRoom(result);
					roomMessages(bRoom.getRoomID(), new BurnerMessageResultListener() {

						@Override
						public void onSuccess(ArrayList<BurnerMessage> messages) {
							bRoom.addMessages(messages);
						}

						@Override
						public void onError(BurnerError e) {
							
						}
						
					});
					callback.onSuccess(bRoom);
					
				} catch (Exception e) {
					callback.onError(new BurnerError("Some Error Occured - 02"));
					e.printStackTrace();
				}
			}
		});
	}

	public void roomDetails(String roomId,
			final BurnerRoomResultListener callback)
			throws BurnerAppUserNotSetException {
		Multimap mm = getMultiMap();
		new BurnerHTTP(BurnerHTTP.ROOM_DETAILS(roomId), mm, new HTTPRequestListener() {
			
			@Override
			public void onComplete(JSONObject result) {
				BurnerError err = checkResultForErrors(result);
				if (err != null) {
					callback.onError(err);
					return;
				}
				
				try {
					final BurnerRoom bRoom = BurnerParser.parseRoom(result);
					roomMessages(bRoom.getRoomID(), new BurnerMessageResultListener() {

						@Override
						public void onSuccess(ArrayList<BurnerMessage> messages) {
							bRoom.addMessages(messages);
							
						}

						@Override
						public void onError(BurnerError e) {
							
						}
						
					});
					
					callback.onSuccess(bRoom);
					
				} catch (Exception e) {
					callback.onError(new BurnerError("Some Error Occured - 02"));
					e.printStackTrace();
				}
				
			}
		});
	}

	public void roomMessages(int roomId,
			final BurnerMessageResultListener callback)
			throws BurnerAppUserNotSetException {
		Multimap mm = getMultiMap();
		
		new BurnerHTTP(BurnerHTTP.ROOM_MESSAGES(roomId), mm, new HTTPRequestListener() {
			
			@Override
			public void onComplete(JSONObject result) {
				//Do bunch of error checking here
				BurnerError err = checkResultForErrors(result);
				if (err != null) {
					callback.onError(err);
					return;
				}
				try {
					// Then return the message array in the callback
					callback.onSuccess(BurnerParser.parseMessages(result));

				} catch (Exception e) {
					e.printStackTrace();
					callback.onError(new BurnerError("Some Error Occured"));
				}
			}
		});
	}
	
	public void userInfo(int userId, final BurnerUserInfoListener callback) throws BurnerAppUserNotSetException {
		Multimap mm = getMultiMap();
		new BurnerHTTP(BurnerHTTP.USER_INFO(userId), mm, new HTTPRequestListener() {
			
			@Override
			public void onComplete(JSONObject result) {
				BurnerError err = checkResultForErrors(result);
				if (err != null) {
					callback.onError(err);
					return;
				}
				try {
					BurnerUser br = BurnerParser.parseUser(result);
					callback.onSuccess(br);
				} catch (Exception e) {
					callback.onError(new BurnerError("Some Error Occured"));
					e.printStackTrace();
				}
			}
			
		});
	}
	
	private Multimap getMultiMap() throws BurnerAppUserNotSetException {
		if (getSession() == null || getToken() == null) {
			throw new BurnerAppUserNotSetException("App User Not Set");
		} else {
			Multimap mm = new Multimap();
			mm.add(BurnerResultKeys.SESSION, getSession());
			mm.add(BurnerResultKeys.TOKEN, getToken());
			return mm;
		}
	}
	
	private BurnerError checkResultForErrors(JSONObject result) {
		try {
			if (result == null) {
				return new BurnerError("No Connection to Server");
			} else if (!result.getString(BurnerResultKeys.STATUS).equals(BurnerResultKeys.SUCCESS)) {
				String message = result.getString(BurnerResultKeys.MESSAGE);
				return new BurnerError(message);
			}
		} catch (Exception e) {
			return new BurnerError("Some Error Occured - 01");
		}
		return null;
	}
		

	private String getSession() {
		return BurnerAppUser.getInstance(this.context).getSession();
	}
	
	private String getToken() {
		return BurnerAppUser.getInstance(this.context).getToken();
	}

}
