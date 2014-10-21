package com.burnerchat.login;

import java.security.KeyPair;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.burnerchat.R;
import com.burnerchat.app.BurnerBaseActivity;
import com.burnerchat.app.BurnerConstants;
import com.burnerchat.app.service.BurnerService;
import com.burnerchat.enctypter.BurnerEncrypter;
import com.burnerchat.login.fragments.LoginAnonFragment;
import com.burnerchat.login.fragments.LoginBaseFragment;
import com.burnerchat.login.fragments.LoginInfoFragment;
import com.burnerchat.login.fragments.LoginLogoFragment;
import com.burnerchat.login.fragments.TeaserPagerAdapter;
import com.burnerchat.sdk.BurnerChat;
import com.burnerchat.sdk.BurnerListener;
import com.burnerchat.sdk.errors.BurnerError;
import com.viewpagerindicator.UnderlinePageIndicator;

public class LoginActivity extends BurnerBaseActivity {
	
	/*Index for adding elements into Fragment arraylist */
	private static int LOGIN_INTRO  = 0;
	private static int LOGIN_INSTRUCTIONS = 2;
	private static int LOGIN_BLANK = 1;

	private BootstrapButton mLogin;
	private BootstrapEditText mUsername;
	private ViewPager mPager;
	private UnderlinePageIndicator mIndicators;
	private ArrayList <LoginBaseFragment> mLoginSwipeFragments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	    getActionBar().hide();
		setContentView(R.layout.login_activity);
		
		mLogin 		= (BootstrapButton) 	findViewById(R.id.login_button);
		mUsername 	= (BootstrapEditText)   findViewById(R.id.login_username);
		mPager 		= (ViewPager) 		  	findViewById(R.id.login_pager);
		mIndicators = (UnderlinePageIndicator) findViewById(R.id.login_pager_indicator);
		
		/* Initialize swipe fragments */
		setLoginSwipeFragments();
		
		mPager.setAdapter(new TeaserPagerAdapter(getFragmentManager(),mLoginSwipeFragments));
		mIndicators.setViewPager(mPager);
		
		// Check if this activity was launched as the result 
		// of an account being burned
		Bundle extras = getIntent().getExtras();
		if(extras != null && extras.getBoolean(BurnerConstants.BURNED, false)) {
			showAlert("Account Sucessfully Burned");
		}
		
		getUser().clearUser();
		Intent intent = new Intent(getApplicationContext(), BurnerService.class);
		stopService(intent);
		setListeners();
	}



	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void setListeners() {
		mLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mLogin.setBootstrapButtonEnabled(false);
				String username = mUsername.getText().toString();
				if (isNameValid(username, "Username")) {
					final KeyPair keys = BurnerEncrypter.getInstance().generateKeys();
					
					BurnerChat.getInstance(v.getContext()).createUser(username, keys.getPublic().toString(), 
							new BurnerListener.BurnerHTTPListener() {
						
						@Override
						public void onSuccess() {
							mHandler.post(new Runnable() {
								
								@Override
								public void run() {
							        // Start chat activity
							        goChat();
							        // Kill activity so you cant go back to it
							        finish();
								}
							});
							
						}
						
						@Override
						public void onError(BurnerError e) {
							showAlert(e.getMessage());
							mLogin.setBootstrapButtonEnabled(true);
						}
					});

				}
				
			}
		});
		
	}
	
	public void setLoginSwipeFragments()
	{
		mLoginSwipeFragments = new ArrayList <LoginBaseFragment>(3);
		mLoginSwipeFragments.add(LOGIN_INTRO,new LoginLogoFragment());
		mLoginSwipeFragments.add(LOGIN_BLANK, new LoginAnonFragment());
		mLoginSwipeFragments.add (LOGIN_INSTRUCTIONS, new LoginInfoFragment());
		
		
	}
	
}
