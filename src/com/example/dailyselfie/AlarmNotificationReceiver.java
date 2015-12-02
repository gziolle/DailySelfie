package com.example.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmNotificationReceiver extends BroadcastReceiver {
	
	private Intent mNotificationIntent;
	private PendingIntent pendingIntent;
	private String mTakeASelfie = "It is time to take a Selfie";
	private String mNotificationContentText = "Tap to access DailySelfie";
	private String mNotificationContentTitle = "DailySelfie";
	private int MY_NOTIFICATION_ID = 1;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		mNotificationIntent = new Intent(context, MainActivity.class);
		pendingIntent = PendingIntent.getActivity(context, 0, mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Notification.Builder notificationBuilder = new Notification.Builder(context)
			.setSmallIcon(android.R.drawable.ic_menu_camera)
			.setContentText(mNotificationContentText)
			.setContentTitle(mNotificationContentTitle)
			.setTicker(mTakeASelfie)
			.setContentIntent(pendingIntent);
		
		NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());	
	}

}
