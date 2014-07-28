package com.compus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartServiceBroadcastReceiver extends BroadcastReceiver {

	private static final String ACTION = "android.intent.action.BOOT_COMPLETED";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION)){
			Intent i = new Intent(Intent.ACTION_RUN);
			i.setClass(context, MainService.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(i);
		}
	}

}
