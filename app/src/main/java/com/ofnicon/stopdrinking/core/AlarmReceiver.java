package com.ofnicon.stopdrinking.core;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.ofnicon.stopdrinking.R;
import com.ofnicon.stopdrinking.activities.MainActivity;
import com.ofnicon.stopdrinking.activities.NotificationActivity;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!NotifManager.notificationsEnabled(context)) {
            return;
        }

        NotifManager.setNotificationAlarm(context, NotifManager.INTERVAL, false);

        boolean first = intent.getBooleanExtra("first", false);

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        if (first || currentHour < 23 && currentHour >= 8) {
            displayNotification(context, intent.getStringExtra("text"));
        }
    }

    private void displayNotification(Context context, String text) {

        Intent notificationIntent = new Intent(context, NotificationActivity.class);
        notificationIntent.putExtra("text", text);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        Intent chooser = Intent.createChooser(sendIntent,"Поделиться");
        PendingIntent pendingSendIntent = PendingIntent.getActivity(context, 1, chooser, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.header_text))
                .setContentText(text)
                .setColor(context.getResources().getColor(R.color.primary_dark))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_share_24dp, context.getString(R.string.share), pendingSendIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());
    }


}
