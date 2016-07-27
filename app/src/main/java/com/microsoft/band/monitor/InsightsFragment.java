package com.microsoft.band.monitor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.roomorama.caldroid.CaldroidFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsightsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String username;

    ListView dates;
    ListView duration;

    boolean isLeftListEnabled = true;
    boolean isRightListEnabled = true;

    private OnFragmentInteractionListener mListener;

    public InsightsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InsightsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InsightsFragment newInstance(String param1, String param2) {
        InsightsFragment fragment = new InsightsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insights, container, false);
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        SharedPreferences prefs = getActivity().getSharedPreferences("Monitor", 0);
        username = prefs.getString("username", "UNKNOWN");
        Log.d("LOGIN", username);
        ArrayList<PeriodCalendarEntry> rawData = ServerCom.get_all(username);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));

        DataPoint[] dataPoints = new DataPoint[rawData.size()];
        DataPoint[] dataPoints2 = new DataPoint[rawData.size()];
        DataPoint[] dataPoints3 = new DataPoint[rawData.size()];
            for (int i = 0; i < rawData.size(); i++) {
                String dateStr = rawData.get(i).date;
                Log.d("INSIGHTS2", dateStr);
                dateStr = dateStr.replace("\"", "");
                String [] dateArr = dateStr.split("-", 0);
                if(dateArr.length != 3) {
                    Log.d("INSIGHTS", "Date not formatted properly");
                    for(int j = 0; j < dateArr.length; j++) {
                        Log.d("INSIGHTS", "\t"+dateArr[j]);
                    }
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                Date date = null;
                try {
                    date = formatter.parse(dateStr);
                } catch (ParseException e) {
                    Log.d("ERRORREKT", e.getMessage());
                }
                dataPoints[i] = new DataPoint(date, rawData.get(i).mood);
                dataPoints2[i] = new DataPoint(date, rawData.get(i).heart);
                dataPoints3[i] = new DataPoint(date, rawData.get(i).temp);
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(dataPoints2);
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(dataPoints3);
            //graph.addSeries(series2);
            series.setTitle("Mood");
        series2.setTitle("Heart Rate");
        series3.setTitle("Temperature");

//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });

        // generate Dates



// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(d1, 1),
//                new DataPoint(d2, 5),
//                new DataPoint(d3, 3)
//        });

        graph.addSeries(series);
        graph.addSeries(series2);
        graph.addSeries(series3);



        //graph.addSeries(series);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);// It will remove the background grids
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(50.0);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        // legend
        //series.setTitle("foo");

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        series.setColor(Color.DKGRAY);
        series2.setColor(Color.rgb(160,24,180));
        series3.setColor(Color.GREEN);







       // COPY "LIST" CODE HERE



        // Tabs
//        TabHost host = (TabHost) view.findViewById(R.id.tabHost);
//        host.setup();
//
//        TabHost.TabSpec spec = host.newTabSpec("List");
//        spec.setContent(R.id.tab1);
//        spec.setIndicator("List");
//        host.addTab(spec);

        // List View
        dates = (ListView)view.findViewById(R.id.dates);
        duration = (ListView) view.findViewById(R.id.duration);

        String[] blah = new String[]{"2016-01-1 to 2016-01-6",
                "2016-02-1 to 2016-02-6",
                "2016-03-1 to 2016-03-6",
                "2016-04-1 to 2016-04-6",
                "2016-05-1 to 2016-05-6",
                "2016-06-1 to 2016-06-6",
                "2016-07-1 to 2016-07-6",
                "2016-08-1 to 2016-08-6",
                "2016-09-1 to 2016-09-6",
                "2016-10-1 to 2016-10-6",
                "2016-11-1 to 2016-11-6",
                "2016-12-1 to 2016-12-6",};
        String[] plah = new String[]{"6 days","6 days","6 days","6 days","6 days",
                "6 days","6 days","6 days","6 days","6 days","6 days","6 days"};

        ArrayAdapter<String> datesAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, blah){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                if(position %2 == 1)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#d7bef9"));
                }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#c39ff4"));
                }
                return view;
            }
        };

        ArrayAdapter<String> durationAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, plah){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                if(position %2 == 1)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#d7bef9"));

                }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#c39ff4"));
                }
                return view;
            }
        };

        dates.setAdapter(datesAdapter);
        duration.setAdapter(durationAdapter);

        // IF YOU DO NOT OVERRIDE THIS
        // ONLY THE ONE THAT IS TOUCHED WILL SCROLL OVER
        dates.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        duration.setOverScrollMode(ListView.OVER_SCROLL_NEVER);

        dates.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // onScroll will be called and there will be an infinite loop.
                // That's why i set a boolean value
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isRightListEnabled = false;
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    isRightListEnabled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                View c = view.getChildAt(0);
                if (c != null && isLeftListEnabled) {
                    duration.setSelectionFromTop(firstVisibleItem, c.getTop());
                }
            }
        });

        duration.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isLeftListEnabled = false;
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    isLeftListEnabled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                View c = view.getChildAt(0);
                if (c != null && isRightListEnabled) {
                    dates.setSelectionFromTop(firstVisibleItem, c.getTop());
                }
            }
        });





        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}