package com.burnerchat.app.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.burnerchat.R;
import com.burnerchat.app.BurnerAppUser;
import com.burnerchat.app.BurnerApplication;
import com.burnerchat.app.MainActivity;
import com.burnerchat.chat.ChatManager;
import com.burnerchat.sdk.models.BurnerMessage;
import com.burnerchat.sdk.socketio.BurnerSocketIO;
import com.burnerchat.sdk.socketio.BurnerSocketIO.BurnerMessageListener;

public class BurnerService extends Service implements BurnerMessageListener {
//	private final IBinder mBinder = new BurnerBinder();
	private final Handler mHandler = new Handler();

	private boolean bound = false;
	
	@Override
	public IBinder onBind(Intent intent) {
		//bound = true;
		return null;
	}

	@Override
	public void onCreate() {
		showToast("created");
		if (!isLoggedIn()) {
			showToast("stopped");
			// Stop service if user is not logged in
			stopSelf();
			// Disconnect if already connected
			BurnerSocketIO.getInstance(this).disconnect();
			BurnerSocketIO.stop();
		} else {
			showToast("running");
			
			// Register service to event bus
			BurnerApplication.getEventBus().register(this);
			// Start and register for notifications to Socket server
			BurnerSocketIO.getInstance(this).setService(this);

		}
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }


	private boolean isLoggedIn() {
		return BurnerAppUser.getInstance(this).isLoggedIn();
	}

	private void showToast(String m) {
		Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
	}

	private void makeNotification(String message, String from, BurnerMessage msg) {
		
		if (this.bound) {
			return;
		}
		
		ChatManager.getInstance(this).addMessage(msg);
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(from)
		        .setContentText(message);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MainActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(0, mBuilder.build());
	}

	@Override
	public void onIncomingMessage(final BurnerMessage msg) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				makeNotification(msg.getMessage(), msg.getName(), msg);
			}
			
		});
		
	}

}
