package com.microsoft.band.monitor;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.ListView;
import android.widget.ArrayAdapter;

public class StatsActivity extends Activity {
    TabHost tabHost;
    ListView dates;
    ListView duration;

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
        dates = (ListView) findViewById(R.id.dates);
        duration = (ListView) findViewById(R.id.duration);

        String[] blah = new String[]{"I'm on my period", "Such bad cramps", "I want chocolate", "I'm sad"};
        String[] plah = new String[]{"1","2","3","4"};

        ArrayAdapter<String> datesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, blah){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                if(position %2 == 1)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#843c96"));
                }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#C8A2C8"));
                }
                return view;
            }
        };

        ArrayAdapter<String> durationAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, plah){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                if(position %2 == 1)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#843c96"));

                }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#C8A2C8"));
                }
                return view;
            }
        };

        dates.setAdapter(datesAdapter);
        duration.setAdapter(durationAdapter);

    }
}
