package com.microsoft.band.monitor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.sensors.BandSkinTemperatureEvent;
import com.microsoft.band.sensors.BandSkinTemperatureEventListener;
import com.microsoft.band.tiles.BandTile;
import com.microsoft.band.tiles.TileButtonEvent;
import com.microsoft.band.tiles.TileEvent;
import com.microsoft.band.tiles.pages.FlowPanel;
import com.microsoft.band.tiles.pages.FlowPanelOrientation;
import com.microsoft.band.tiles.pages.HorizontalAlignment;
import com.microsoft.band.tiles.pages.PageData;
import com.microsoft.band.tiles.pages.PageLayout;
import com.microsoft.band.tiles.pages.PageRect;
import com.microsoft.band.tiles.pages.ScrollFlowPanel;
import com.microsoft.band.tiles.pages.TextButton;
import com.microsoft.band.tiles.pages.TextButtonData;
import com.microsoft.band.tiles.pages.WrappedTextBlock;
import com.microsoft.band.tiles.pages.WrappedTextBlockData;
import com.microsoft.band.tiles.pages.WrappedTextBlockFont;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ProfileFragment.OnFragmentInteractionListener,
        DebugFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        CalendarFragment.OnFragmentInteractionListener,
        InsightsFragment.OnFragmentInteractionListener
{

    Fragment fragment = null;
    private String username = "";
    private boolean onPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Handle navigation view item clicks here.
        fragment = null;
        Class fragmentClass = HomeFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        navigationView.setCheckedItem(R.id.nav_home);

        SharedPreferences prefs = getSharedPreferences("Monitor", MODE_PRIVATE);
        username = prefs.getString("username", "UNKNOWN");
        Log.d("LOGIN", "From MainActivity: " + username);
        onPeriod = ServerCom.status(username);

        new StartTask().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            new StopTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void goToBand(View view) {
        Intent intent = new Intent(this, BandTileEventAppActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        fragment = null;
        final Class fragmentClass;
        switch(item.getItemId()) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_calendar:
                fragmentClass = CalendarFragment.class;
                break;
            case R.id.nav_insights:
                fragmentClass = InsightsFragment.class;
                break;
            case R.id.nav_alarms:
                fragmentClass = ProfileFragment.class;
                break;
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class;
                break;
            default:
                fragmentClass = DebugFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .commit();
            }
        }, 250);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void onFragmentInteraction(Uri uri) {
        //do nothing

    }

    // For HomeFragment
    // Called when you start or end a period
    public void startEndPeriod(View view) { ((HomeFragment)fragment).startEndPeriod(view);}

    public void enterHealthInfo(View view) {
        ((HomeFragment)fragment).enterHealthInfo(view);
    }

    // BAND STUFF

    private BandClient client = null;
    private static final UUID tileId = UUID.fromString("cc0D508F-70A3-47D4-BBA3-812BADB1F8Aa");
    private static final UUID pageId1 = UUID.fromString("b1234567-89ab-cdef-0123-456789abcd00");
    private static final UUID pageId2 = UUID.fromString("c1234567-89ab-cdef-0123-456789abcd00");

    private float temp = 0;

    BandSkinTemperatureEventListener mTempListener = new BandSkinTemperatureEventListener() {
        @Override
        public void onBandSkinTemperatureChanged(BandSkinTemperatureEvent event) {
            temp = event.getTemperature();
        }
    };



    @Override
    protected void onNewIntent(Intent intent) {
        try {
            processIntent(intent);
        } catch (BandIOException e) {
            e.printStackTrace();
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent() != null && getIntent().getExtras() != null){
            try {
                processIntent(getIntent());
            } catch (BandIOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDestroy() {
        new StopTask().execute();
        if (client != null) {
            try {
                client.disconnect().await();
            } catch (InterruptedException e) {
                // Do nothing as this is happening during destroy
            } catch (BandException e) {
                // Do nothing as this is happening during destroy
            }
        }
        super.onDestroy();
    }

    //method for seeing what is going on with the band
    private void processIntent(Intent intent) throws BandIOException {
        String extraString = intent.getStringExtra(getString(R.string.intent_key));

        if(extraString != null && extraString.equals(getString(R.string.intent_value))){
            if (intent.getAction() == TileEvent.ACTION_TILE_OPENED) {
                TileEvent tileOpenData = intent.getParcelableExtra(TileEvent.TILE_EVENT_DATA);
                onPeriod = ServerCom.status(username);
                updatePages();
            } else if (intent.getAction() == TileEvent.ACTION_TILE_BUTTON_PRESSED) {
                TileButtonEvent buttonData = intent.getParcelableExtra(TileEvent.TILE_EVENT_DATA);
                try {
//                  if(temp != 0) {
//                      sendMessage("" + temp);
//                  }

                    if (onPeriod)
                        sendMessage("You made it!");
                    else
                        sendMessage(ServerCom.tip(username));
                    togglePeriod();
                } catch (BandException e) {
                    handleBandException(e);
                } catch (Exception e) {
                }
            } else if (intent.getAction() == TileEvent.ACTION_TILE_CLOSED) {
                TileEvent tileCloseData = intent.getParcelableExtra(TileEvent.TILE_EVENT_DATA);
            }
        }
    }

    private class StartTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (getConnectedBandClient()) {
                    if (addTile()) {
                        updatePages();
                    }
                } else {
                    return false;
                }
                client.getSensorManager().registerSkinTemperatureEventListener(mTempListener);
            } catch (BandException e) {
                handleBandException(e);
                return false;
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }
    }

    private class StopTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (getConnectedBandClient()) {
                    removeTile();
                } else {
                }
                client.getSensorManager().unregisterSkinTemperatureEventListener(mTempListener);
            } catch (BandException e) {
                handleBandException(e);
                return false;
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }
    }





    private void removeTile() throws BandIOException, InterruptedException, BandException {
        if (doesTileExist()) {
            client.getTileManager().removeTile(tileId).await();
        }
    }


    private boolean doesTileExist() throws BandIOException, InterruptedException, BandException {
        List<BandTile> tiles = client.getTileManager().getTiles().await();
        for (BandTile tile : tiles) {
            if (tile.getTileId().equals(tileId)) {
                return true;
            }
        }
        return false;
    }

    private boolean addTile() throws Exception {
        if (doesTileExist()) {
            return true;
        }

        /* Set the options */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap tileIcon = BitmapFactory.decodeResource(getBaseContext().getResources(), R.raw.m_logo, options);

        BandTile tile = new BandTile.Builder(tileId, "monitor.", tileIcon)
                .setPageLayouts(createButtonLayout(), createInsightLayout()) // add the layouts here
                .build();
        if (client.getTileManager().addTile(this, tile).await()) {
            return true;
        } else {
            return false;
        }
    }

    // layout for page where you can toggle period on/off
    private PageLayout createButtonLayout() {
        return new PageLayout(
                new FlowPanel(15, 0, 260, 120, FlowPanelOrientation.VERTICAL)
                        .addElements(new WrappedTextBlock(new PageRect(0, 0, 245, 70), WrappedTextBlockFont.SMALL).setMargins(0, 0, 0, 0).setAutoHeightEnabled(false).setId(2))
                        .addElements(new TextButton(0, 0, 195, 43).setMargins(0, 0, 0, 0).setPressedColor(0xFF8B61F2).setHorizontalAlignment(HorizontalAlignment.CENTER).setId(3)));
    }

    // layout for page with insights
    private PageLayout createInsightLayout() {
        return new PageLayout(
                new ScrollFlowPanel(new PageRect(0, 0, 260, 120), FlowPanelOrientation.VERTICAL)
                        .addElements(new WrappedTextBlock(new PageRect(0, 0, 245, 30), WrappedTextBlockFont.SMALL).setMargins(0, 0, 0, 0).setColor(0xFF8B61F2).setId(4))
                        .addElements(new WrappedTextBlock(new PageRect(0, 0, 245, 120), WrappedTextBlockFont.SMALL).setAutoHeightEnabled(true).setMargins(0, 0, 0, 0).setId(5)));

    }


    // set up the content of the pages
    // the order here matters
    private void updatePages() throws BandIOException {
        PageData togglePeriodPage = null;
        if (onPeriod) {
            // make server request here
            togglePeriodPage = new PageData(pageId1, 0)
                    .update(new WrappedTextBlockData(2, "Day " + Integer.toString(ServerCom.day(username))))
                    .update(new TextButtonData(3, "Tap to End"));
        } else {
            togglePeriodPage = new PageData(pageId1, 0)
                    .update(new WrappedTextBlockData(2, "Click below if your period has started."))
                    .update(new TextButtonData(3, "Tap to Start"));
        }
        client.getTileManager().setPages(tileId,
                new PageData(pageId2, 1)
                        .update(new WrappedTextBlockData(4, "Insights"))
                        .update(new WrappedTextBlockData(5, "Last Period: "+ ServerCom.prev(username)
                                + "\nNext Predicted Period: "+ ServerCom.predict(username))),
                togglePeriodPage);
    }

    private void sendMessage(String message) throws BandIOException {
        client.getNotificationManager().showDialog(tileId, "monitor.", message);
    }

    private void togglePeriod() throws BandIOException {
        ServerCom.toggle(username);
        onPeriod = ServerCom.status(username);
        updatePages();

        try {
            fragment = (Fragment) (HomeFragment.class).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .commit();

    }

    private boolean getConnectedBandClient() throws InterruptedException, BandException {
        if (client == null) {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if (devices.length == 0) {
                return false;
            }
            client = BandClientManager.getInstance().create(getBaseContext(), devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }

        return ConnectionState.CONNECTED == client.connect().await();
    }

    private void handleBandException(BandException e) {
        String exceptionMessage = "";
        switch (e.getErrorType()) {
            case DEVICE_ERROR:
                exceptionMessage = "Please make sure bluetooth is on and the band is in range.\n";
                break;
            case UNSUPPORTED_SDK_VERSION_ERROR:
                exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                break;
            case SERVICE_ERROR:
                exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                break;
            case BAND_FULL_ERROR:
                exceptionMessage = "Band is full. Please use Microsoft Health to remove a tile.\n";
                break;
            default:
                exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                break;
        }
    }

    public void onCheckboxClicked(View view) {

    }

}