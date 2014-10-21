package com.burnerchat.chat;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.burnerchat.R;
import com.burnerchat.app.BurnerApplication;
import com.burnerchat.app.BurnerBaseActivity;
import com.burnerchat.app.service.BurnerService;
import com.burnerchat.chat.fragments.ChatBaseFragment;
import com.burnerchat.chat.fragments.ChatBaseFragment.ChatCallback;
import com.burnerchat.chat.fragments.ChatInfoFragment;
import com.burnerchat.chat.fragments.ChatListFragment;
import com.burnerchat.chat.fragments.ChatRoomFragment;
import com.burnerchat.sdk.BurnerChat;
import com.burnerchat.sdk.BurnerListener;
import com.burnerchat.sdk.errors.BurnerError;
import com.burnerchat.sdk.exceptions.BurnerAppUserNotSetException;
import com.burnerchat.sdk.models.BurnerMessage;
import com.burnerchat.sdk.models.BurnerRoom;
import com.burnerchat.sdk.models.BurnerUser;
import com.burnerchat.sdk.socketio.BurnerSocketIO;
import com.burnerchat.sdk.socketio.BurnerSocketIO.BurnerMessageListener;
import com.squareup.otto.Subscribe;

public class ChatActivity extends BurnerBaseActivity implements ChatCallback, OnPageChangeListener, BurnerMessageListener {
	private static final int CHAT_INFO = 0;
	private static final int CHAT_LIST = 1;
	private static final int CHAT_ROOM = 2;

	//private boolean mBound = false;
	
	private ViewPager mPager;
	//private BurnerService mBurnerService = null;
	private ArrayList<ChatBaseFragment> mChatFragments;
	private ChatRoomFragment mChatRoom = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity);
		BurnerApplication.getEventBus().register(this);
		BurnerSocketIO.getInstance(this).setActivityListener(this);
		
		Intent myIntent = new Intent(getApplicationContext(), BurnerService.class);
	    getApplicationContext().startService(myIntent);
		
		mChatFragments = new ArrayList<ChatBaseFragment>(3);
		mChatFragments.add(CHAT_INFO, new ChatInfoFragment());
		mChatFragments.add(CHAT_LIST, new ChatListFragment());

		for (ChatBaseFragment fragment : mChatFragments) {
			fragment.setChatCallbackListener(this);
		}
		
	    final String[] rooms = getUser().getRooms();
		if (rooms != null) {
			final Context context = this;
			
			// Load chat rooms into chat list when activity initially starts
			// will be used by chat list fragment
			for (String roomId : rooms) {
				try {
					BurnerChat.getInstance(this).roomDetails(roomId,
							new BurnerListener.BurnerRoomResultListener() {

								@Override
								public void onSuccess(BurnerRoom room) {
									ChatManager.getInstance(context)
											   .addRoom(room);
									
									mHandler.post(new Runnable() {

										@Override
										public void run() {
											// Set default room so you can swipe 
											BurnerRoom br = ChatManager.getInstance(context).getChatRooms().get(0);
											setChatRoom(br);
										}
										
									});
								}

								@Override
								public void onError(final BurnerError e) {
									if (e.getMessage().equalsIgnoreCase("Not Authorized")) {
										mHandler.post(new Runnable() {

											@Override
											public void run() {
												burnAccount();											
											}
											
										});
										
									}
									else {
										mHandler.post(new Runnable() {

											@Override
											public void run() {
												showAlert(e.getMessage());												
											}
											
										});
										
									}
								}
							});
				} catch (BurnerAppUserNotSetException e) {
					getUser().clearUser();
					goLoginBurned();
					finish();
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}

		mPager = (ViewPager) findViewById(R.id.chat_pager);
		mPager.setAdapter(new ChatFragmentAdapter(getFragmentManager(),
				mChatFragments));
		mPager.setCurrentItem(CHAT_LIST);
		mPager.setOnPageChangeListener(this);
		
	}

	@Override
	protected void onStop() {
		super.onStop();
		BurnerSocketIO.getInstance(this).stopActivityListener();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		BurnerSocketIO.getInstance(this).setActivityListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		BurnerSocketIO.getInstance(this).stopActivityListener();
	}
	
	@Override
	public void onBurnClick() {
		try {
			
			BurnerChat.getInstance(this).burnUser(new BurnerListener.BurnerHTTPListener() {
				
				@Override
				public void onSuccess() {
					
				}
				
				@Override
				public void onError(BurnerError e) {
					
				}
			});
			
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			burnAccount();
		}

	}
	
	private void burnAccount() {
		BurnerSocketIO.getInstance(this).sendBurn(getUser().getUserId());
		BurnerSocketIO.getInstance(this).disconnect();
		BurnerSocketIO.stop();
		
		Intent intent = new Intent(getApplicationContext(), BurnerService.class);
		stopService(intent);
		
		getUser().clearUser();
		// Clear instance of current chat manager
		ChatManager.clear();
		goLoginBurned();
		finish();
	}

	@Override
	public void onMessageSend(BurnerMessage msg) {
		try {
			JSONArray dataTo = new JSONArray();

			final JSONArray args = new JSONArray();
			JSONObject data = new JSONObject();
			data.put("session", getUser().getSession());
			data.put("token", getUser().getToken());
			data.put("from", msg.getSenderId());
			data.put("roomId", msg.getRoomId());
			data.put("message", msg.getMessage());
			data.put("to", (JSONArray)dataTo);
			args.put(data);
			Log.v("CHAT SENDING MESSAGE TO IO", args.toString());
			BurnerSocketIO.getInstance(this).sendMessage(args);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	@Override
	public void onRoomSelected(BurnerRoom chatroom) {
		setChatRoom(chatroom);
		mPager.setCurrentItem(CHAT_ROOM, true);
	}
	
	@Override
	public void onRoomAdded() {
		setChatRoom(getChatManager().getLastRoom());
		mPager.setCurrentItem(CHAT_ROOM, true);
	}
	
	@Override
	public void onError(String error) {
		if (error.equalsIgnoreCase("Not Authorized")) {
			burnAccount();
		}
		showAlert(error);
	}

	private void putMessageIntoRoom(BurnerMessage msg) {
		BurnerRoom br = getChatManager().getRoomById(msg.getRoomId());
		// Checks if user is in our list, adds user if not in there
		if (!br.hasUser(msg.getSenderId())) {
			Log.v("chatAct", "inhere");
			BurnerUser user = new BurnerUser(msg.getSenderId(), msg.getName(), "qwerjkwherkq");
			addUserToChatRoom(msg.getRoomId(), user);
		}
		addMessageToChatRoomFragment(msg);
		
	}
	
//	private ServiceConnection mConnection = new ServiceConnection() {
//
//		@Override
//		public void onServiceConnected(ComponentName className, IBinder binder) {
//			BurnerService.BurnerBinder b = (BurnerService.BurnerBinder) binder;
//			mBurnerService = b.getService();
//			mBound = true;
//		}
//
//		@Override
//		public void onServiceDisconnected(ComponentName className) {
//			mBurnerService = null;
//			mBound = false;
//		}
//	};
//	
	private void addUserToChatRoom(int roomId, BurnerUser user) {
		getChatManager().getRoomById(roomId).addUser(user);
	}
	
	private void addMessageToChatRoomFragment(BurnerMessage message) {
		getChatManager().addMessage(message);
		
		ChatRoomFragment crf = ((ChatRoomFragment) mChatFragments.get(CHAT_ROOM));
		if (crf.getCurrentRoomId() != -1 && crf.getCurrentRoomId() == message.getRoomId()) {
			crf.notifyChange();
		}
	}
	
	private void log(String s) {
		Log.v("CHAT ACTIVITY", s);
	}
	
	@Subscribe
	public void userBurn(final Integer uid) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				userBurner(uid);
			}
		});
	}
	
	private void userBurner(int uid) {
		Log.v("CHAT ACTIVITY", "Burned User");
		ChatManager.getInstance(this).burnUser(uid);
		mChatRoom.notifyChange();
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int page) {
		switch (page) {
			case CHAT_INFO:
				getActionBar().setTitle("Info");
				getActionBar().setDisplayHomeAsUpEnabled(false);
				//doAuthh();
				if(mChatRoom != null)
					closeKeyboard(this, mChatRoom.getWindowToken());
				break;
			case CHAT_LIST:
				getActionBar().setTitle("Rooms");
				getActionBar().setDisplayHomeAsUpEnabled(false);
				if(mChatRoom != null)
					closeKeyboard(this, mChatRoom.getWindowToken());
				break;
			case CHAT_ROOM:
				String name = mChatRoom.getCurrentRoomName();
				if (name == null) break;
				getActionBar().setTitle(name);
				getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	mPager.setCurrentItem(CHAT_LIST, true);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void setChatRoom(BurnerRoom room) {
		if (mChatRoom == null) {
			mChatRoom = new ChatRoomFragment();
			mChatRoom.setChatCallbackListener(this);
			mChatFragments.add(CHAT_ROOM, mChatRoom);
			mPager.getAdapter().notifyDataSetChanged();
		}
		mChatRoom.setChatRoom(room);
	}
	
	private ChatManager getChatManager() {
		return ChatManager.getInstance(this);
	}
	
	private static void closeKeyboard(final Context c, final IBinder windowToken) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
			    mgr.hideSoftInputFromWindow(windowToken, 0);
				
			}
			
		}).run();
	    
	}

	@Override
	public void onIncomingMessage(final BurnerMessage msg) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				log(msg.getMessage());
				putMessageIntoRoom(msg);
				
			}
			
		});
		
	}
}
