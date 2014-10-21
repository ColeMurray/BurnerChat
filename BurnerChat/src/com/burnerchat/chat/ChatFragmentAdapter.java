package com.burnerchat.chat;

import java.util.ArrayList;

import com.burnerchat.chat.fragments.ChatBaseFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

public class ChatFragmentAdapter extends FragmentStatePagerAdapter {
	private ArrayList<ChatBaseFragment> mFragments;
	
    public ChatFragmentAdapter(FragmentManager fm, ArrayList<ChatBaseFragment> mChatFragments) {
        super(fm);
        mFragments = mChatFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
    
	@Override
    public int getCount() {
        return mFragments.size();
    }
}
