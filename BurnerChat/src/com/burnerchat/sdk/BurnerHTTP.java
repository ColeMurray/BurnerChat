package com.burnerchat.sdk;

import org.json.JSONObject;

import android.util.Log;

import com.burnerchat.sdk.BurnerChat.HTTPRequestListener;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.Multimap;
import com.koushikdutta.async.http.body.UrlEncodedFormBody;

public class BurnerHTTP {
	protected static final String USER_CREATE 	= "users/create";
	protected static final String USER_BURN		= "users/burn";
	protected static final String ROOM_CREATE	= "rooms/create";
	protected static final String ROOM_DETAILS(String ID) {return "rooms/" + ID;}
	protected static final String ROOM_MESSAGES(int ID) {return String.format("rooms/%d/messages",  ID );}
	
	protected static final String USER_INFO(int ID) {return "users/" + String.valueOf(ID);}


	private static String makeURL(String path) {
		return BurnerURLS.HTTP.URL + path;
	}


	public BurnerHTTP(String path, Multimap mm, final HTTPRequestListener callback) {
		AsyncHttpPost post = new AsyncHttpPost(makeURL(path));
		post.setBody(new UrlEncodedFormBody(mm));
		AsyncHttpClient.getDefaultInstance().executeJSONObject(post, new JSONCallback(callback));
	}

	private class JSONCallback extends AsyncHttpClient.JSONObjectCallback {
		private HTTPRequestListener callback;
		
		public JSONCallback(HTTPRequestListener callback) {
			this.callback = callback;
		}
		
		@Override
		public void onCompleted(Exception e, AsyncHttpResponse source, JSONObject result) {
			if (e != null) {
				callback.onComplete(null);
				return;
			}
			Log.v("BURNER_HTTP", result.toString());
			callback.onComplete(result);
		}
		
	}
}
