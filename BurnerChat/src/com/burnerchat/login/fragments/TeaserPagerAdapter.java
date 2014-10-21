package com.burnerchat.login.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

public class TeaserPagerAdapter extends FragmentStatePagerAdapter {
	
	private ArrayList <LoginBaseFragment>mLoginSwipeFragments;
	
    public TeaserPagerAdapter(FragmentManager fm, 
    					ArrayList<LoginBaseFragment> mLoginSwipeFragments) {
        super(fm);
        this.mLoginSwipeFragments = mLoginSwipeFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mLoginSwipeFragments.get(position);
    }

    @Override
    public int getCount() {
        return mLoginSwipeFragments.size();
    }
}
