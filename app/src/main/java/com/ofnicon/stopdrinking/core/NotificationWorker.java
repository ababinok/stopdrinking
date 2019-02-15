package com.ofnicon.stopdrinking.core;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    public static String TAG = "NOTIFICATION_WORKER_TAG";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Core.displayNotification(getApplicationContext());
        return Worker.Result.success();
    }
}
