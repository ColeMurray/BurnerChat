package com.burnerchat.login.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.burnerchat.R;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify.IconValue;

public class LoginAnonFragment extends LoginBaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup rootView = inflateView(inflater, container);
		
		setText("Chat Anonymously");
		setBackgrondDrawable(new IconDrawable(getActivity(), IconValue.fa_comments)
											 .colorRes(R.color.bbutton_danger).sizeDp(400));
		return rootView;
	}

}
