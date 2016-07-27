package com.microsoft.band.monitor;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.ListView;
import android.widget.ArrayAdapter;

public class StatsActivity extends Activity {
    TabHost tabHost;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Tabs
        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Calendar");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Calendar");
        host.addTab(spec);

        spec = host.newTabSpec("List");
        spec.setContent(R.id.tab2);
        spec.setIndicator("List");
        host.addTab(spec);

        // List View
        listView = (ListView) findViewById(R.id.list);
        String[] blah = new String[]{"I'm on my period", "Such bad cramps", "I want chocolate", "I'm sad"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, blah);

        listView.setAdapter(adapter);

    }
}
