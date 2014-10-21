package com.burnerchat.sdk.socketio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.burnerchat.app.BurnerAppUser;
import com.burnerchat.app.BurnerApplication;
import com.burnerchat.sdk.BurnerParser;
import com.burnerchat.sdk.BurnerURLS;
import com.burnerchat.sdk.models.BurnerMessage;
import com.burnerchat.sdk.socketio.SocketServiceCallback.SocketClientListener;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.socketio.SocketIOClient;
import com.koushikdutta.async.http.socketio.SocketIORequest;
import com.squareup.otto.Bus;

public class BurnerSocketIO implements SocketClientListener {
	public interface BurnerMessageListener {
		public void onIncomingMessage(BurnerMessage msg);
	}
	
	private static final String ON_CONNECT = "onConnect";
	private static final String SEND = "send";
	private static final String BURN = "burnn";
	
	private static BurnerSocketIO instance = null;
	
	private BurnerMessageListener mListenerService = null;
	private BurnerMessageListener mListenerActivity = null;

	private SocketIOClient client = null;
	private Bus eBus = null;
	private Context mCtx;
	
	private boolean wasAuth = false;
	
	private BurnerSocketIO(Context ctx) {
		
		this.mCtx = ctx;
		this.wasAuth = false;
		eBus = BurnerApplication.getEventBus();
		eBus.register(this);
		
		SocketServiceCallback ssc = new SocketServiceCallback(this);
		
		SocketIORequest req = new SocketIORequest(BurnerURLS.SocketIO.URL);
		req.setLogging("Socket.IO", Log.VERBOSE);
		
		SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), req, ssc);

	}
	
	public static BurnerSocketIO getInstance(Context context) {
		if (instance == null) {
			synchronized (BurnerSocketIO.class) {
				if (instance == null) {
					instance = new BurnerSocketIO(context);
				}
			}
		}
		return instance;
	}


	public void setService(BurnerMessageListener s) {
		this.mListenerService = s;
	}
	
	public void stopServiceListener() {
		this.mListenerService = null;
	}
	
	public void setActivityListener(BurnerMessageListener a) {
		this.mListenerActivity = a;
	}
	
	public void stopActivityListener() {
		this.mListenerActivity = null;
	}
	
	@Override
	public void onClientConnected(SocketIOClient client) {
		this.client = client;
		doAuth();
	}

	@Override
	public void onIncomingMessage(JSONArray message) {
		Log.v("SOCKET.IO INCOMING MESSAGE", message.toString());
		try {
			BurnerMessage bm = BurnerParser.parseSocketMessage(message);
			
			if (mListenerActivity != null) {
				mListenerActivity.onIncomingMessage(bm);
			} else if (mListenerService != null) {
				mListenerService.onIncomingMessage(bm);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public synchronized void sendMessage(final JSONArray data) {
		checkAuth();
		Log.v("SOCKET.IO SENDING MESSAGE", data.toString());
		emit(SEND, data);
	}
	
	public synchronized void sendAuth(JSONArray data) {
		Log.v("SOCKETIO EMITTING AUTH", data.toString());
		emit(ON_CONNECT, data);
		this.wasAuth = true;
	}
	
	public synchronized void disconnect() {
		if (this.client != null) {
			if (this.client.isConnected()) {
				this.client.disconnect();
				Log.v("SOCKETIO DISCONNECTED", "DONE");
			}
		}
	}
	
	public static void stop() {
		Log.v("SOCKETIO STOPPING", "NULL");
		instance = null;
	}
	
	private void emit(final String name, JSONArray data) {
		this.client.emit(name, data);
	}
	
	@Override
	public void onBurnEvent(JSONArray burned) {
		Log.v("BURNEEEEEDDDDD ACCOUNT", burned.toString());
		try {
			int id = BurnerParser.parseBurnedUser(burned);
			if (this.myID == id || id == -1) {
				return;
			}
			else {
				eBus.post(new Integer(id));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int myID = -1;
	public void sendBurn(int id) {
		this.myID = id;
		try {
			JSONArray out = new JSONArray();
			JSONObject data = new JSONObject();
			data.put("userid", id);
			out.put(data);
			Log.v("SOCKET SENDING BURN", out.toString());
			emit(BURN, out);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void doAuth() {
		Log.v("SOCKETIO DOING AUTH", "START");
		BurnerAppUser bau = BurnerAppUser.getInstance(mCtx);
		if (bau.getSession() == null) return;
		if (bau.getToken() == null) return;
		
		Log.v("SOCKETIO DOING AUTH - session", bau.getSession());
		Log.v("SOCKETIO DOING AUTH - toekn", bau.getToken());

		try {
			final JSONArray args = new JSONArray();
			JSONObject jo = new JSONObject();
			jo.put("session", bau.getSession());
			jo.put("token", bau.getToken());
	        args.put(jo);
	        // Send auth data to socketio server
	        sendAuth(args);	
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkAuth() {
		Log.v("SOCKETIO CHECKING AUTH", "....");
		if (this.wasAuth == false) {
			doAuth();
		}
	}

	@Override
	public void onIncoming() {
		if (this.wasAuth == false) {
			doAuth();
		}
		
	}
	
}
