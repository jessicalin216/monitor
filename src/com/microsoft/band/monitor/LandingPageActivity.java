package com.microsoft.band.monitor;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class LandingPageActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private boolean isPeriodOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Get all the info
        // TODO
        isPeriodOn = false;
        // Change header text
        TextView periodInfoText = (TextView) findViewById(R.id.periodInfoText);
        int days = 2;
        String infoHeader = isPeriodOn ?
                getString(R.string.landing_periodon_prefix) + " " + days :
                days + " " + getString(R.string.landing_periodoff_suffix);
        periodInfoText.setText(infoHeader);

        // Change button text
        Button startEndPeriodButton = (Button) findViewById(R.id.startEndPeriodButton);
        String buttonText = !isPeriodOn ?
                getString(R.string.landing_periodon_button_text) :
                getString(R.string.landing_periodoff_button_text);
        startEndPeriodButton.setText(buttonText);

        // Set button visibility
        Button healthInfoButton = (Button) findViewById(R.id.healthInfoButton);
        healthInfoButton.setVisibility(isPeriodOn ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void startEndPeriod(View view) {
        // Change state
        isPeriodOn = !isPeriodOn;

        // Change visible text
        TextView periodInfoText = (TextView) findViewById(R.id.periodInfoText);
        int days = 2;
        String infoHeader = isPeriodOn ?
                getString(R.string.landing_periodon_prefix) + " " + days :
                days + " " + getString(R.string.landing_periodoff_suffix);
        periodInfoText.setText(infoHeader);

        // Change button text
        Button startEndPeriodButton = (Button) findViewById(R.id.startEndPeriodButton);
        String buttonText = !isPeriodOn ?
                getString(R.string.landing_periodon_button_text) :
                getString(R.string.landing_periodoff_button_text);
        startEndPeriodButton.setText(buttonText);

        // Set button visibility
        Button healthInfoButton = (Button) findViewById(R.id.healthInfoButton);
        healthInfoButton.setVisibility(isPeriodOn ? View.VISIBLE : View.GONE);

        // TODO call toggle
    }

    public void enterHealthInfo(View view) {
        buildDialog();
    }

    // Helper
    public void buildDialog() {
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.landing_healthinfo_dialog_title));

        // Inflate the view
        final LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.health_alert_dialog, null));

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
                Toast.makeText(LandingPageActivity.this, getString(R.string.landing_healthinfo_dialog_error),
                        Toast.LENGTH_SHORT).show();
            }
            // Else save and dismiss
            else {
                // TODO Call http code
                dialog.dismiss();
            }
        }
    }

    // Helper
    public void changeEmotion() {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_landing_page, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((LandingPageActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
