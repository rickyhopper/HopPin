package com.rhxp.hoppin;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.GpsLocationProvider;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.views.InfoWindow;
import com.mapbox.mapboxsdk.views.MapView;
import com.rhxp.hoppin.async.AsyncListener;
import com.rhxp.hoppin.async.TaskCode;
import com.rhxp.hoppin.db.CheckinHelper;
import com.rhxp.hoppin.model.Checkin;
import com.rhxp.hoppin.model.CheckinAdapter;
import com.rhxp.hoppin.task.GetCheckinsTask;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements AsyncListener {

    private MapView mMapView = null;
    private View hiddenPanel;
    private View showPanelButton;
    private CheckinHelper db;
    private ArrayList<Checkin> feed;
    private ArrayList<Marker> markers;
    private GpsLocationProvider provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set up map center
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.setCenter(new LatLng(35.777016, -78.63797));
        mMapView.setZoom(17);
        //set gps provided to get user location
        provider = new GpsLocationProvider(this);
        //initialize the feed, markers, and db
        feed = new ArrayList<Checkin>();
        markers = new ArrayList<Marker>();
        db = new CheckinHelper(this);
        db.clearLocalDB();

        GetCheckinsTask t = new GetCheckinsTask(this);
        t.execute();

        hiddenPanel = findViewById(R.id.hidden_panel);
        showPanelButton = findViewById(R.id.showPanelButton);
    }

    @Override
    protected void onResume() {
        db.getWritableDatabase();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }
        else if(id == R.id.set_location){
            UserLocationOverlay myLocationOverlay = new UserLocationOverlay(provider, mMapView);
            myLocationOverlay.enableMyLocation();
            myLocationOverlay.setDrawAccuracyEnabled(true);
            mMapView.getOverlays().add(myLocationOverlay);
            Toast.makeText(MainActivity.this, "Location Set", Toast.LENGTH_LONG).show();
        }
        else if(id == R.id.share_location){
            if(provider.getLastKnownLocation() != null){
                String message = "Let's get this HopPin at " + " http://maps.google.com/?q="+ provider.getLastKnownLocation().getLatitude() + "," + provider.getLastKnownLocation().getLongitude() + " \n #HopPin";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, "Get the party HopPin"));
            }
            else{
                Toast.makeText(MainActivity.this, "Please set location first", Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public void slideUpDown(final View view) {
        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
            showPanelButton.setVisibility(View.GONE);

        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);

            hiddenPanel.startAnimation(bottomDown);
            showPanelButton.setVisibility(View.VISIBLE);
            hiddenPanel.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }

    public void clearMap(){
        for(Marker m: markers){
            mMapView.removeMarker(m);
        }
    }

    public void addCheckinToMap(Checkin c) {
        LatLng loc = new LatLng(c.getLat(),c.getLon());
        Marker marker = new Marker(mMapView, c.getUser().getScreen_name(), c.getFormattedCreated_at() + "\n\n" + c.getText() + "\n\n link: " + c.getContentLink() , loc);
        markers.add(marker);
        Drawable d = marker.getMarker(16);
        d.setColorFilter(new LightingColorFilter(Checkin.COLOR_BLACK, c.getColor(feed)));
        marker.setMarker(d);
        mMapView.addMarker(marker);
    }

    @Override
    public void onTaskComplete(TaskCode taskCode, boolean success, Object result) {
        Log.i("tag", "Task complete");
        if(success){
            feed = (ArrayList<Checkin>) result;
            db.addListToDB((ArrayList<Checkin>) result);
            //add each checkin to map
            for(Checkin status: feed){
                addCheckinToMap(status);
            }
            CheckinAdapter adapter = new CheckinAdapter(this, feed);
            // Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.checkinItemList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View v, int pos,
                                        long itemId) {
                    Checkin c = feed.get(pos);
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("twitter://status?status_id=" + c.getId()));
                        startActivity(intent);

                    }catch (Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://twitter.com/"+ c.getUser().getScreen_name() + "/status/"+c.getId())));
                    }
                }

            });
            Toast.makeText(MainActivity.this, "Added Markers", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this, "Couldn't Retrieve Feed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause(){
        db.close();
        super.onPause();
    }
}
