package com.incomingcallfcm.incomingcallmodule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

public class CallActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String meetingId = intent.getStringExtra("meetingId");
        String callType = intent.getStringExtra("callType");

        NotificationManagerCompat.from(context).cancel(2001);

        if ("ACTION_DECLINE_CALL".equals(action)) {
            Log.d(
                    "incomingcallfcm",
                    "Call declined for meetingId=" + meetingId + " callType=" + callType
            );
        }
    }
}
