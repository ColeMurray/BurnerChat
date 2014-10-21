package com.burnerchat.login.fragments;

import com.burnerchat.R;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class LoginBaseFragment extends Fragment {
	protected ImageView mIv;
	protected TextView mTv;
	protected TextView mDetailTv;

	@Override
	public abstract View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);

	protected ViewGroup inflateView(LayoutInflater inflater, ViewGroup container) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.login_swipes_logo, container, false);

		mIv = (ImageView) rootView.findViewById(R.id.logo_teaser_image);
		mTv = (TextView) rootView.findViewById(R.id.logo_teaser_text);
		
		return rootView;
	}
	
	protected void setText(String s) {
		mTv.setText(s);
	}

	protected void setBackgrondDrawable(Drawable d) {
		mIv.setImageDrawable(d);
	}
	
	protected void setBackground(int id) {
		mIv.setImageResource(id);
	}

	//public abstract void setGravity(int s);
}
