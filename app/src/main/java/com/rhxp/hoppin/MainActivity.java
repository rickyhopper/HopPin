package com.rhxp.hoppin;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.views.MapView;
import com.rhxp.hoppin.async.AsyncListener;
import com.rhxp.hoppin.async.TaskCode;
import com.rhxp.hoppin.model.Checkin;
import com.rhxp.hoppin.task.GetCheckinsTask;


public class MainActivity extends ActionBarActivity implements AsyncListener {

    private MapView mMapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.setCenter(new LatLng(35.777016, -78.63797));
        mMapView.setZoom(17);

        GetCheckinsTask t = new GetCheckinsTask(this);
        t.execute();
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

    public void addCheckinToMap(Checkin c) {

    }

    @Override
    public void onTaskComplete(TaskCode taskCode, boolean success, Object result) {
        Log.i("tag", "Task complete");
        //TODO: get tweets from db (after parsing them in through GetCheckinsTask)
    }
}
