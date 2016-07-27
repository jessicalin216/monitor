package com.microsoft.band.monitor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;

import com.roomorama.caldroid.CaldroidFragment;
import java.util.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView dates;
    ListView duration;

    boolean isLeftListEnabled = true;
    boolean isRightListEnabled = true;


    public CalendarFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Tabs
        TabHost host = (TabHost) view.findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Calendar");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Calendar");
        host.addTab(spec);

        spec = host.newTabSpec("History");
        spec.setContent(R.id.tab2);
        spec.setIndicator("History");
        host.addTab(spec);

        //Calendar View
        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.calendarView, caldroidFragment);
        t.commit();

        Resources res = getResources();
        Drawable d = res.getDrawable(R.drawable.purple_square_hi);

        for(int m=0; m <= 11; m++){
            Date start = new Date(116, m, 1);
            Date end = new Date(116, m, 6);

            caldroidFragment.setSelectedDates(start, end);

            for(int i = 1; i <= 6; i++) {
                Date hold = new Date(116, m, i);
                caldroidFragment.setBackgroundDrawableForDate(d, hold);
            }


        }


        // List View
        dates = (ListView)view.findViewById(R.id.dates);
        duration = (ListView) view.findViewById(R.id.duration);

        String[] blah = new String[]{"Average Length",
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
                                     "2016-12-1 to 2016-12-6"};
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
                    view.setBackgroundColor(Color.parseColor("#E9DEEE"));
                }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#ffffff"));
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
                    view.setBackgroundColor(Color.parseColor("#E9DEEE"));

                }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#ffffff"));
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
