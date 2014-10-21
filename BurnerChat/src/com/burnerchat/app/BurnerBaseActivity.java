package com.burnerchat.app;

import static com.devspark.appmsg.AppMsg.LENGTH_SHORT;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.burnerchat.R;
import com.burnerchat.chat.ChatActivity;
import com.burnerchat.login.LoginActivity;
import com.devspark.appmsg.AppMsg;
import com.squareup.otto.Bus;

public abstract class BurnerBaseActivity extends Activity {
	protected final Handler mHandler = new Handler();

	protected void goLogin() {
		startActivity(getLoginIntent());
	}
	
	protected void goLoginBurned() {
		Intent i = getLoginIntent();
		i.putExtra(BurnerConstants.BURNED, true);
		
		startActivity(i);
	}

	protected void goChat() {
		startActivity(new Intent(this, ChatActivity.class));
	}
	
	protected void showAlert(String msg) {
		final AppMsg.Style style = new AppMsg.Style(LENGTH_SHORT, R.color.bbutton_danger);
		AppMsg.makeText(this, msg, style).show();
	}
	
	private Intent getLoginIntent() {
		return new Intent(this, LoginActivity.class);
	}
	
	protected BurnerAppUser getUser() {
		return BurnerAppUser.getInstance(this);
	}
	
	protected Bus getEventBus() {
		return BurnerApplication.getEventBus();
	}
	
	public boolean isNameValid(String name, String type) {
		if (name.length() < 4) {
			showAlert(type +" has to be at least 4 characters long");
			return false;
		}
		else if (!name.matches("[a-zA-Z0-9_]+")) {
			showAlert(type+" can only contain alphanumeric characters");
			return false;
		}
		return true;
	}
}
