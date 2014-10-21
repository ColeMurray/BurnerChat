package com.burnerchat.chat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.burnerchat.R;

public class ChatInfoFragment extends ChatBaseFragment {
	
	private BootstrapButton mBurnButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.chat_info_fragment, container, false);
        
		mBurnButton = (BootstrapButton) rootView.findViewById(R.id.button_burn);
        
		setListeners();
		
        return rootView;
	}
	
	private void setListeners() {
		mBurnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mBurnButton.setBootstrapButtonEnabled(false);
				if (getCallback() != null) {
					// Send callback to activity
					// containing this fragment
					getCallback().onBurnClick();
				}
			}
			
		});
		
	}
	
	
}
