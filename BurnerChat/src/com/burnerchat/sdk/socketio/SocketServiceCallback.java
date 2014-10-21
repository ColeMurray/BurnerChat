package com.burnerchat.sdk.socketio;

import org.json.JSONArray;

import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;

public class SocketServiceCallback implements ConnectCallback {
	protected interface SocketClientListener {
		public void onClientConnected(SocketIOClient client);
		public void onIncomingMessage(JSONArray message);
		public void onBurnEvent(JSONArray burned);
		public void onIncoming();
		//public void onAccountBurned();
	}
	
	private SocketClientListener mCallback;
	
	public SocketServiceCallback(SocketClientListener listener) {
		mCallback = listener;
	}
	
	@Override
	public void onConnectCompleted(Exception ex, SocketIOClient client) {
		
		mCallback.onClientConnected(client);
		
		client.on("message", new EventCallback() {
            @Override
            public void onEvent(JSONArray message, Acknowledge acknowledge) {
                mCallback.onIncomingMessage(message);
            }
        });
		
		client.on("burnn", new EventCallback() {
            @Override
            public void onEvent(JSONArray message, Acknowledge acknowledge) {
            	if (message == null) return;
                mCallback.onBurnEvent(message);
            }
        });
		
		client.on("incoming", new EventCallback() {
            @Override
            public void onEvent(JSONArray message, Acknowledge acknowledge) {
                mCallback.onIncoming();
            }
        });
	}
	
}
