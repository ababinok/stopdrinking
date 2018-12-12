package com.ofnicon.stopdrinking.core;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.ofnicon.stopdrinking.R;

import java.util.Random;

public class NotifManager {

    static final long INTERVAL = 1000 * 60 * 60; // 1 час в продакшн
    //    static final long INTERVAL = 10000; // 10 секунд для тестов
    private static final String APP_PREFERENCES = "StopDrinkingSettings";
    private static final String APP_PREFERENCES_SHOW_NOTIFICAIONS = "show_notifications";

    private static String getNotificationText(Context context) {
        String[] reasonsArray = context.getResources().getStringArray(R.array.reasons_list);
        Random r = new Random();
        int reasonNum = r.nextInt(reasonsArray.length);
        return reasonsArray[reasonNum];
    }

    public static void setNotificationAlarm(Context context, long delayInMillis, boolean first) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("text", getNotificationText(context));
        intent.putExtra("first", first);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delayInMillis;
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(android.app.AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
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
        setShowNotifications(context, false);
    }

    public static void enableNotifications(Context context) {
        setShowNotifications(context, true);
    }

}
