package com.burnerchat.login.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.burnerchat.R;

public class LoginInfoFragment extends LoginBaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup rootView = inflateView(inflater, container);

		setText("End-to-End Encryption");
		setBackground(R.drawable.locked_edit);
		return rootView;
	}

}
