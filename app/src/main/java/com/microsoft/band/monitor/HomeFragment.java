package com.microsoft.band.monitor;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private boolean isPeriodOn;
    private String username = "";
    private int days;

    private View mView;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        // Get all the info
        // TODO
        // Some serious bullshit to run all network tasks on the UI thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences prefs = getActivity().getSharedPreferences("Monitor", Context.MODE_PRIVATE);
        username = prefs.getString("username", "UNKNOWN");

        isPeriodOn = ServerCom.status(username);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        updateTextAndButtons();
        return mView;
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

    // Called when you start or end a period
    public void startEndPeriod(View view) {


        // TODO: uncomment if you want scheduled tasks
//        if(isPeriodOn)
//            scheduleAlarm();
//        else
//            cancelAlarm();

        ServerCom.toggle(username);
        isPeriodOn = ServerCom.status(username);
        updateTextAndButtons();


    }

    public void enterHealthInfo(View view) {
        buildDialog();
    }

    // Helper
    public void buildDialog() {
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.landing_healthinfo_dialog_title));

        // Inflate the view
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_health_alert, null));

        builder.setPositiveButton(R.string.ok, null);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        // Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        // Show the dialog
        dialog.show();
        Button posButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        posButton.setOnClickListener(new CustomListener(dialog));
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            RatingBar ratingBar = (RatingBar) ((AlertDialog) dialog).findViewById(R.id.ratingBar);
            int rating = (int) ratingBar.getRating();

            // Catch empty rating
            if (rating < 1) {
                Toast.makeText(getActivity(), getString(R.string.landing_healthinfo_dialog_error),
                        Toast.LENGTH_SHORT).show();
            }
            // Else save and dismiss
            else {
                dialog.dismiss();
            }
        }
    }

    // Helper to update the emotions
    public void updateEmotion() {
        ImageView monMon_backdrop = (ImageView) mView.findViewById(R.id.monMon_backdrop);
        ImageView monMon_face = (ImageView) mView.findViewById(R.id.monMon_face);
        ImageView monMon_symptom1 = (ImageView) mView.findViewById(R.id.monMon_symptom1);
        ImageView monMon_symptom2 = (ImageView) mView.findViewById(R.id.monMon_symptom2);
//        Map<String, Integer> map = new HashMap<String, Integer>();
//        map.put("blah", );

        // TODO: update for all emotions
        monMon_backdrop.setImageResource(isPeriodOn ? R.drawable.p_default : R.drawable.monitor_logo_full);
        monMon_face.setImageResource(isPeriodOn ? R.drawable.p_default : R.drawable.np_default);
        if (true) {
            monMon_symptom1.setVisibility(View.VISIBLE);
        }
        if (true) {
            monMon_symptom2.setVisibility(View.VISIBLE);
        }
    }

    // Helper
    public void updateTextAndButtons() {
        days = isPeriodOn ? ServerCom.day(username) : getNextPeriod(ServerCom.predict(username).replaceAll("\"",""));

        // Change visible text
        TextView periodStartText = (TextView) mView.findViewById(R.id.periodInfoDate);
        // Create today's date
        Date today = new Date();
        String startDate = today.getYear()+1900 + "-" + (today.getMonth() + 1) + "-" + (today.getDate() - days);
        String startHeader = isPeriodOn ?
                getString(R.string.landing_periodstart_prefix) + " " + getMonthDate(startDate) :
                getString(R.string.landing_projectedstart_prefix) + " " + getMonthDate(ServerCom.predict(username).replaceAll("\"",""));
        periodStartText.setText(startHeader);

        TextView periodInfoText = (TextView) mView.findViewById(R.id.periodInfoText);
        String infoHeader = isPeriodOn ?
                getString(R.string.landing_periodon_prefix) + " " + days :
                days + " " + getString(R.string.landing_periodoff_suffix);
        periodInfoText.setText(infoHeader);

        // Change button text
        Button startEndPeriodButton = (Button) mView.findViewById(R.id.startEndPeriodButton);
        String buttonText = !isPeriodOn ?
                getString(R.string.landing_periodon_button_text) :
                getString(R.string.landing_periodoff_button_text);
        startEndPeriodButton.setText(buttonText);

        // Set button visibility
        Button healthInfoButton = (Button) mView.findViewById(R.id.healthInfoButton);
//        healthInfoButton.setVisibility(isPeriodOn ? View.VISIBLE : View.INVISIBLE);

        // Update MonMon
        updateEmotion();
    }

    public long dateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public int getNextPeriod(String date) {
        // Change string into a date
        if(date.contains("na"))
            return 0;
        String[] splitStr = date.split("-");
        int year = Integer.parseInt(splitStr[0]) - 1900;
        int month = Integer.parseInt(splitStr[1]) - 1;
        int day = Integer.parseInt(splitStr[2]);
        Date nextDate = new Date(year, month, day);
        Date today = new Date();
        return (int) dateDiff(today, nextDate, TimeUnit.DAYS);
    }

    public String getMonthDate(String date) {
        // Change string into a date
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        if(date.contains("na"))
            return "";
        String[] splitStr = date.split("-");
        int year = Integer.parseInt(splitStr[0]) - 1900;
        int month = Integer.parseInt(splitStr[1]) - 1;
        int day = Integer.parseInt(splitStr[2]);
        Date nextDate = new Date(year, month, day);
        return sdf.format(nextDate).toUpperCase();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
