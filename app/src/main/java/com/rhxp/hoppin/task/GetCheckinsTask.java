package com.rhxp.hoppin.task;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.rhxp.hoppin.async.AsyncListener;
import com.rhxp.hoppin.async.TaskCode;
import com.rhxp.hoppin.model.Checkin;
import com.rhxp.hoppin.db.CheckinHelper;
import com.rhxp.hoppin.model.CheckinList;
import com.rhxp.hoppin.remote.RemoteApiHelper;
import com.rhxp.hoppin.remote.URLHelper;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by Ricky on 11/17/14.
 */
public class GetCheckinsTask extends AsyncTask<Void, Void, String> {
    private final TaskCode taskCode = TaskCode.CHECKINS_GET;
    private final String TAG = getClass().getSimpleName();

    private String url = null;
    private List<NameValuePair> params;
    private List<NameValuePair> headers;

    private AsyncListener listener;

    public GetCheckinsTask(AsyncListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... arg0) {
        URLHelper.setCHECKINS_URL();
        url = URLHelper.CHECKINS_URL;
        params = RemoteApiHelper.prepareEmptyParams();
        headers = RemoteApiHelper.prepareAuthHeader();
        return RemoteApiHelper.makeGetRequest(url, params, headers);
    }

    @Override
    protected void onPostExecute(String responseString) {
        if (responseString == null) {
            Log.i(TAG, "GetListTask returned null.");
            //send failure to listener
            listener.onTaskComplete(taskCode, false, null);
            return;
        }
        Log.i(TAG, responseString);
        if (responseString.length() > 0) {
            Log.i(TAG, "The list was gotten.");
            Log.i(TAG, responseString);
            List<Checkin> checkins = CheckinList.parse(responseString);

            //send result to listener
            listener.onTaskComplete(taskCode, true, checkins);
        } else {
            Log.i(TAG, "The list was not received, returning null.");
            //send failure to listener
            listener.onTaskComplete(taskCode, false, null);
        }
    }
}
