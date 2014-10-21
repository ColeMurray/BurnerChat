package com.burnerchat.chat.fragments;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.burnerchat.R;
import com.burnerchat.app.BurnerAppUser;
import com.burnerchat.app.BurnerBaseActivity;
import com.burnerchat.chat.ChatManager;
import com.burnerchat.chat.fragments.ui.MessageListItem;
import com.burnerchat.sdk.BurnerChat;
import com.burnerchat.sdk.BurnerListener;
import com.burnerchat.sdk.errors.BurnerError;
import com.burnerchat.sdk.exceptions.BurnerAppUserNotSetException;
import com.burnerchat.sdk.models.BurnerRoom;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify.IconValue;

public class ChatListFragment extends ChatBaseFragment {
	
	
	
	private ListView mListView;
	private ChatRoomAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.chat_list_fragment, container, false);
		
		setHasOptionsMenu(true);
		mListView = (ListView) rootView.findViewById(R.id.chat_list_view);
		
		makeListView();
        return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.chat_list_menu, menu);
		menu.findItem(R.id.chat_list_add).setIcon(
				   new IconDrawable(getActivity(), IconValue.fa_plus_square)
				   .colorRes(R.color.bbutton_danger).actionBarSize());
		
		menu.findItem(R.id.chat_list_refresh).setIcon(
				   new IconDrawable(getActivity(), IconValue.fa_refresh)
				   .colorRes(R.color.bbutton_danger).actionBarSize());
		
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.chat_list_add:
				addChatDialog();
				break;
			
			case R.id.chat_list_refresh:
				notifiyListChanged();
				break;
			default:
				break;
			
		}
		return super.onOptionsItemSelected(item);
	}


	private void makeListView() {
		
	    
	    mAdapter = new ChatRoomAdapter(getActivity(), ChatManager.getInstance(getActivity()).getChatRooms());
	    
	    
	    mListView.setAdapter(mAdapter);

	    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		    @Override
		    public void onItemClick(AdapterView<?> parent, final View view,
		      int position, long id) {
				BurnerRoom chatRoom = ChatManager.getInstance(getActivity()).getRoomById(id);
				if (getCallback() != null) {
					// Notify activity a room was clicked
					getCallback().onRoomSelected(chatRoom);
				}
		    }

	    });
		
	}
	
	private class ChatRoomAdapter extends ArrayAdapter<BurnerRoom> {
		
		public ChatRoomAdapter(Context context, ArrayList<BurnerRoom> objects) {
			super(context, 0, objects);
		}

		@Override
		public long getItemId(int position) {
			return getItem(position).getRoomID();
		}

		@Override
	    public View getView(final int position, View convertView, final ViewGroup parent) {
			MessageListItem tv = (MessageListItem) convertView;
	        if (tv == null) {
	            tv = new MessageListItem(getContext());
	        }
	        BurnerRoom cr = getItem(position);
	        tv.setAsReceived("", cr.getName());
	        return tv;
	    }

	}
	
	
	private Dialog createDialog() {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.chat_list_fragment_add, null);
	    // Find the text input stuff in the view
	    final BootstrapEditText roomName = (BootstrapEditText) rootView.findViewById(R.id.chat_room_name);
	    final BootstrapEditText roomPass = (BootstrapEditText) rootView.findViewById(R.id.chat_room_pass);

	    builder.setView(rootView)
	    // Add action buttons
	           .setPositiveButton(R.string.enter_room, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(final DialogInterface dialog, int id) {
	            	   String rName = roomName.getText().toString();
	            	   String rPass = roomPass.getText().toString();
	            	   // Validate input
	                   if( ((BurnerBaseActivity)getActivity()).isNameValid(rName, "Room Name") &&
	                	   ((BurnerBaseActivity)getActivity()).isNameValid(rPass, "Password")) {
	                	   
	                	   try {
							BurnerChat.getInstance(getActivity())
							   			 .enterRoom(rName, rPass, new BurnerListener.BurnerRoomResultListener() {

											@Override
											public void onSuccess(BurnerRoom room) {
												getChatManager().addRoom(room);
												BurnerAppUser.getInstance(getActivity())
												   .addRoom(room.getRoomID());
												mHandler.post(new Runnable() {

													@Override
													public void run() {
														notifiyListChanged();
														getCallback().onRoomAdded();
													}
													
												});
												dialog.dismiss();
											}

											@Override
											public void onError(final BurnerError e) {
												mHandler.post(new Runnable() {

													@Override
													public void run() {
														handleError(e.getMessage());
													}
													
												});
												
											}

							});
						} catch (BurnerAppUserNotSetException e) {
							
							e.printStackTrace();
						}
	                   }
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   dialog.dismiss();
	               }
	           });      
	    return builder.create();
	}
	
	private void addChatDialog() {
		createDialog().show();
	}
	
	private void handleError(String error) {
		mCallback.onError(error);
	}
	
	private void notifiyListChanged() {
		mAdapter.notifyDataSetChanged();
	}
}
