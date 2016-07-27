package com.microsoft.band.monitor;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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
            for (int i = 0; i < rawData.size(); i++) {
                String dateStr = rawData.get(i).date;
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
                    Log.d("INSIGHTS", dateStr);
                    date = formatter.parse(dateStr);
                    Log.d("INSIGHTS", date.toString());
                } catch (ParseException e) {
                    Log.d("ERRORREKT", e.getMessage());
                }
                dataPoints[i] = new DataPoint(date, rawData.get(i).mood);
            }

            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(dataPoints);
            //graph.addSeries(series2);
            series2.setTitle("bar");

//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });

        // generate Dates
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        Log.d("INSIGHTS", d1.toString());
        Log.d("INSIGHTS", d2.toString());
        Log.d("INSIGHTS", d3.toString());


// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3)
        });

        graph.addSeries(series);


        //graph.addSeries(series);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.VERTICAL);// It will remove the background grids
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space


        graph.getViewport().setMinX(20);
        graph.getViewport().setMaxX(40);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(5.0);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        // legend
        //series.setTitle("foo");

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        //series.setColor(Color.RED);
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