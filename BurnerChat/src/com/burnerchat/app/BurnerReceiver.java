package com.burnerchat.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.burnerchat.app.service.BurnerService;

public class BurnerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			if (BurnerAppUser.getInstance(context).isLoggedIn()) {
				Intent myIntent = new Intent(context.getApplicationContext(), BurnerService.class);
			    context.getApplicationContext().startService(myIntent);
			}
		}
	}

}
