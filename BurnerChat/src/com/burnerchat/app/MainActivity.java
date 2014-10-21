package com.burnerchat.app;

import android.os.Bundle;

public class MainActivity extends BurnerBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Check if user is logged in already
		if (getUser().isLoggedIn()) {
			goChat();
		} else {
			goLogin();
		}

		// Finish the activity so that you cant come
		// back to it
		finish();
	}

}
