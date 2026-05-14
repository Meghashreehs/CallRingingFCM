package com.incomingcallfcm.incomingcallmodule;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.incomingcallfcm.MainActivity;
import com.incomingcallfcm.R;

public class CallFcmService extends FirebaseMessagingService {

    private static final int CALL_NOTIFICATION_ID = 2001;
    private static final long CALL_TIMEOUT = 30000L;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().containsKey("type")
                && "INCOMING_CALL".equals(remoteMessage.getData().get("type"))) {
            showIncomingCallNotification(this, remoteMessage);
        }
    }

    public static void showIncomingCallNotification(Context context, RemoteMessage remoteMessage) {
        String meetingId = remoteMessage.getData().get("callId");
        String callerName = remoteMessage.getData().get("callerName");
        String subject = remoteMessage.getData().get("subject");
        String meetingToken = remoteMessage.getData().get("meetingToken");
        String meetingUrl = remoteMessage.getData().get("meetingUrl");
        String callType = remoteMessage.getData().get("callType");
        showIncomingCallNotification(context, meetingId, callerName, subject, meetingToken, meetingUrl, callType);
    }

    public static void showIncomingCallNotification(
            Context context,
            String meetingId,
            String callerName,
            String subject,
            String meetingToken,
            String meetingUrl,
            String callType
    ) {

        Bundle extras = new Bundle();
        extras.putString("meetingId", meetingId);
        extras.putString("meetingToken", meetingToken);
        extras.putString("meetingUrl", meetingUrl);
        extras.putString("callType", callType);
        extras.putString("subject", subject);

        PendingIntent dummyIntent = PendingIntent.getBroadcast(
                context,
                0,
                new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent fullScreenIntent = new Intent(context, MainActivity.class);
        fullScreenIntent.putExtras(extras);
        fullScreenIntent.setAction("ACTION_ACCEPT_CALL");
        fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(
                context,
                0,
                fullScreenIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent acceptIntent = new Intent(context, MainActivity.class);
        acceptIntent.setAction("ACTION_ACCEPT_CALL");
        acceptIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        acceptIntent.putExtras(extras);

        PendingIntent acceptPendingIntent = PendingIntent.getActivity(
                context,
                100,
                acceptIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent declineIntent = new Intent(context, CallActionReceiver.class);
        declineIntent.setAction("ACTION_DECLINE_CALL");
        declineIntent.putExtra("meetingId", meetingId);
        declineIntent.putExtra("callType", callType);

        PendingIntent declinePendingIntent = PendingIntent.getBroadcast(
                context,
                101,
                declineIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Person caller = new Person.Builder()
                .setName(callerName != null ? callerName : "Unknown caller")
                .setImportant(true)
                .build();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, NotificationHelper.CALL_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(callerName != null ? callerName : "Incoming Call")
                        .setContentText(subject != null ? subject : "Tap answer to continue")
                        .setSubText("Incoming Video Call")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setOngoing(true)
                        .setAutoCancel(false)
                        .setStyle(NotificationCompat.CallStyle.forIncomingCall(
                                caller,
                                declinePendingIntent,
                                acceptPendingIntent
                        ))
                        .setContentIntent(dummyIntent)
                        .setFullScreenIntent(fullScreenPendingIntent, true)
                        .setColor(Color.parseColor("#0B5FFF"));

        NotificationManagerCompat.from(context).notify(CALL_NOTIFICATION_ID, builder.build());

        new Handler(Looper.getMainLooper()).postDelayed(
                () -> NotificationManagerCompat.from(context).cancel(CALL_NOTIFICATION_ID),
                CALL_TIMEOUT
        );
    }
}
