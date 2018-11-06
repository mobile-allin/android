package br.com.allin.mobile.pushnotification.gcm;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

public class AllInJobService extends JobIntentService {
    static final int JOB_ID = 1000;

    public static void enqueueWork(Context context, Intent work) {
        AllInJobService.enqueueWork(context, AllInJobService.class, AllInJobService.JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        new AllInGcmNotification(this).showNotification(intent.getExtras());
    }
}
