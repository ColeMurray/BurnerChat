package com.burnerchat.app;

import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BurnerApplication extends Application {

	private static Bus mEventBus;

	public static Bus getEventBus() {
		return mEventBus;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mEventBus = new Bus(ThreadEnforcer.ANY);
		
	}

}
