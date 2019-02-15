package com.ofnicon.stopdrinking.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.ofnicon.stopdrinking.R;
import com.ofnicon.stopdrinking.activities.MainActivity;
import com.ofnicon.stopdrinking.activities.NotificationActivity;

import java.util.Random;

public class NotifManager {

//    private static final long INTERVAL = 1000 * 60 * 60; // 1 час в продакшн
    private static final long INTERVAL = 600000; // 20 секунд для тестов
    private static final long FIRST_DISPLAY_DELAY = 5000; // first show after turning on


    private static final String APP_PREFERENCES = "StopDrinkingSettings";
    private static final String APP_PREFERENCES_SHOW_NOTIFICAIONS = "show_notifications";

    private static String getNotificationText(Context context) {
        String[] reasonsArray = context.getResources().getStringArray(R.array.reasons_list);
        Random r = new Random();
        int reasonNum = r.nextInt(reasonsArray.length);
        return reasonsArray[reasonNum];
    }

    public static void setNotificationAlarm(Context context) {

        long currentTimeInMillis = SystemClock.elapsedRealtime();
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // First "Welcome" notification
        alarmManager.set(
                android.app.AlarmManager.ELAPSED_REALTIME_WAKEUP,
                currentTimeInMillis + FIRST_DISPLAY_DELAY,
                getPendingIntent(context, 1));

        // Repeating notifications
        alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                currentTimeInMillis + INTERVAL,
                INTERVAL,
                getPendingIntent(context, 2));

    }

    private static PendingIntent getPendingIntent(Context context, int requestCode) {
        Intent intent = new Intent(context, MyAlarmReceiver.class);
        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    static boolean notificationsEnabled(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean result = false;
        if (preferences.contains(APP_PREFERENCES_SHOW_NOTIFICAIONS)) {
            result = preferences.getBoolean(APP_PREFERENCES_SHOW_NOTIFICAIONS, false);
        }
        return result;
    }

    private static void setShowNotifications(Context context, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(APP_PREFERENCES_SHOW_NOTIFICAIONS, value);
        editor.apply();
    }

    public static void disableNotifications(Context context) {
//        setShowNotifications(context, false);

        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context, 2));
    }

    public static void enableNotifications(Context context) {
        setShowNotifications(context, true);
    }

    public static void shareNotice(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Поделиться"));
    }

    static void displayNotification(Context context) {

        String text = NotifManager.getNotificationText(context);
        Intent notificationIntent = new Intent(context, NotificationActivity.class);
        notificationIntent.putExtra("text", text);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        Intent chooser = Intent.createChooser(sendIntent, "Поделиться");
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
