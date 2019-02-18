package com.ofnicon.stopdrinking.core;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Calendar;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    static String TAG = "NOTIFICATION_WORKER_TAG";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (currentHour < 23 && currentHour >= 7) {
            Core.displayNotification(getApplicationContext());
        }
        return Worker.Result.success();
    }
}
