package com.microsoft.band.monitor;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

public class CalendarActivity extends Activity {
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Calendar");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Calendar");
        host.addTab(spec);

        spec = host.newTabSpec("List");
        spec.setContent(R.id.tab2);
        spec.setIndicator("List");
        host.addTab(spec);
    }
}
