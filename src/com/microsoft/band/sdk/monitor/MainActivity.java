package com.microsoft.band.sdk.monitor;

import android.app.Activity;
import android.os.Bundle;

import com.microsoft.band.sdk.monitor.notification.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
