package com.rhxp.hoppin;

import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.views.InfoWindow;
import com.mapbox.mapboxsdk.views.MapView;
import com.rhxp.hoppin.async.AsyncListener;
import com.rhxp.hoppin.async.TaskCode;
import com.rhxp.hoppin.db.CheckinHelper;
import com.rhxp.hoppin.model.Checkin;
import com.rhxp.hoppin.task.GetCheckinsTask;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements AsyncListener {

    private MapView mMapView = null;
    private CheckinHelper db;
    private ArrayList<Checkin> feed;
    private ArrayList<Marker> markers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.setCenter(new LatLng(35.777016, -78.63797));
        mMapView.setZoom(17);

        feed = new ArrayList<Checkin>();
        markers = new ArrayList<Marker>();
        db = new CheckinHelper(this);
        db.clearLocalDB();

        GetCheckinsTask t = new GetCheckinsTask(this);
        t.execute();
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

        return super.onOptionsItemSelected(item);
    }

    public void clearMap(){
        for(Marker m: markers){
            mMapView.removeMarker(m);
        }
    }

    public void addCheckinToMap(Checkin c) {
        LatLng loc = new LatLng(c.getLat(),c.getLon());
        Marker marker = new Marker(mMapView, c.getUser().getScreen_name(), c.getText() + "\n link: " + c.getContentLink() , loc);
        markers.add(marker);
        Drawable d = marker.getMarker(16);
        d.setColorFilter(new LightingColorFilter(Checkin.COLOR_BLACK, Checkin.COLOR_RED));
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
