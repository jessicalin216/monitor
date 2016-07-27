package com.microsoft.band.monitor;

import android.app.IntentService;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by keazares on 7/27/2016.
 */
public class NotificationService extends IntentService {
    Context context;

    public NotificationService() {
        super("MyTestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        System.out.println("I like cats");
        // TODO: Get the band info and store the stats
    }
}
