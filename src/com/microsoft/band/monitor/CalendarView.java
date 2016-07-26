package com.microsoft.band.monitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.*;

public class CalendarView extends LinearLayout {
    private LinearLayout header;
    private TextView txtDate;
    private GridView grid;

    public CalendarView(Context context) {
        super(context);
        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.control_calendar, this);

        // layout is inflated, assign local variables to components
        header = (LinearLayout)findViewById(R.id.calendar_header);
        txtDate = (TextView)findViewById(R.id.calendar_date_display);
        grid = (GridView)findViewById(R.id.calendar_grid);
    }
}
