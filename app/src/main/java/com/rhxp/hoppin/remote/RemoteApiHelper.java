package com.rhxp.hoppin.remote;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricky on 10/25/14.
 */
public class RemoteApiHelper {

    /**
     * Makes an HTTP post request to the given URL with the parameters given.
     * Puts the result (as a json-encoded string) into RemoteDbHelper.result.
     * @param url
     * @param params
     */
    public static String makePostRequest(String url, List<NameValuePair> params) {
        HttpPost httppost;
        HttpClient client;
        InputStream inputStream = null;

        //make post request,
        try {
            client = new DefaultHttpClient();
            httppost = new HttpPost(url);

            //adding data to post request
            httppost.setEntity(new UrlEncodedFormEntity(params));

            //execute post request
            HttpResponse response = client.execute(httppost);
            inputStream = response.getEntity().getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Http connection encountered error.");
            e.printStackTrace();
            return null;
        }

        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");

            String line = "0";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            String ret = sb.toString();
            Log.d("log_tag", "String gotten from post: " + ret);
            return ret;
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result to string.");
            return null;
        }
    }

    /**
     * Makes an HTTP post request to the given URL with the parameters given.
     * Puts the result (as a json-encoded string) into RemoteDbHelper.result.
     * @param url
     * @param rawPayload
     */
    public static String makePostRequest(String url, String rawPayload, List<NameValuePair> headers) {
        HttpPost httppost;
        HttpClient client;
        InputStream inputStream = null;

        //make post request,
        try {
            client = new DefaultHttpClient();
            httppost = new HttpPost(url);

            //adding data to post request
            StringEntity entity = new StringEntity(rawPayload);
            entity.setContentType(new BasicHeader("Content-Type",
                    "application/json"));
            httppost.setEntity(entity);

            // add headers to request if needed
            if ((headers != null) && (headers.size() > 0)) {
                for (NameValuePair header : headers) {
                    httppost.addHeader(header.getName(), header.getValue());
                }
            }

            //execute post request
            HttpResponse response = client.execute(httppost);
            inputStream = response.getEntity().getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Http connection encountered error.");
            e.printStackTrace();
            return null;
        }

        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");

            String line = "0";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            String ret = sb.toString();
            Log.d("log_tag", "String gotten from post: " + ret);
            return ret;
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result to string.");
            return null;
        }
    }

    public static String makeGetRequest(String url, List<NameValuePair> params, List<NameValuePair> headers) {
        HttpGet httpget;
        HttpClient client;
        InputStream inputStream = null;

        //make get request
        try {
            client = new DefaultHttpClient();
            String newUrl = url;
            if (params.size() > 0) {
                newUrl = url + "?" + URLEncodedUtils.format(params, "utf-8"); //bind parameters to get request
            }
            Log.d("log_tag", "URL = " + newUrl);
            httpget = new HttpGet(newUrl);

            // add headers to request if needed
            if ((headers != null) && (headers.size() > 0)) {
                for (NameValuePair header : headers) {
                    httpget.addHeader(header.getName(), header.getValue());
                }
            }

            //execute get request
            HttpResponse response = client.execute(httpget);
            inputStream = response.getEntity().getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Http connection encountered error.");
            e.printStackTrace();
            return null;
        }

        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");

            String line = "0";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            String ret = sb.toString();
            Log.d("log_tag", "String gotten from post: " + ret);
            return ret;
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result to string.");
            return null;
        }
    }

    public static String makeGetRequest(String url, List<NameValuePair> params) {
        return makeGetRequest(url, params, null);
    }

    //TODO this might not work, make sure it does
    public static String makePutRequest(String url, String rawPayload) {
        HttpPut httpput;
        HttpClient client;
        InputStream inputStream = null;

        //make post request,
        try {
            client = new DefaultHttpClient();
            httpput = new HttpPut(url);

            //adding data to put request
            StringEntity entity = new StringEntity(rawPayload);
            entity.setContentType(new BasicHeader("Content-Type",
                    "application/json"));
            httpput.setEntity(entity);

            //execute put request
            HttpResponse response = client.execute(httpput);
            inputStream = response.getEntity().getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Http connection encountered error.");
            e.printStackTrace();
            return null;
        }

        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");

            String line = "0";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            String ret = sb.toString();
            Log.d("log_tag", "String gotten from post: " + ret);
            return ret;
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result to string.");
            return null;
        }
    }

    public static int makeDeleteRequest(String url, List<NameValuePair> params) {
        HttpDelete httpdelete;
        HttpClient client;
        InputStream inputStream = null;

        //make post request,
        try {
            client = new DefaultHttpClient();
            httpdelete = new HttpDelete(url);

            //adding data to post request
            //httpdelete.setEntity(new UrlEncodedFormEntity(params));

            //execute post request
            HttpResponse response = client.execute(httpdelete);
            //inputStream = response.getEntity().getContent();
            int code = response.getStatusLine().getStatusCode();
            Log.i("log_tag", "Delete request got response code " + code);
            return code;
        } catch (Exception e) {
            Log.e("log_tag", "Http connection encountered error.");
            e.printStackTrace();
            return -1;
        }
/*
        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");

            String line = "0";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            String ret = sb.toString();
            Log.d("log_tag", "String gotten from post: " + ret);
            return ret;
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result to string.");
            return null;
        }
*/
    }

    /*******************************************************************************
     * RAW PAYLOAD PREPARATION *****************************************************
     ******************************************************************************/

//    public static String prepareAddListItemPayload(ListItem item) {
//        return JsonEncodeHelper.encodeListItemAsJson(item);
//    }

    /*******************************************************************************
     * HEADER PREPARATION **********************************************************
     ******************************************************************************/

    public static List<NameValuePair> prepareGoogleUserHeader(String googleId) {
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new BasicNameValuePair("googleid", googleId));
        return headers;
    }

    public static List<NameValuePair> prepareUserHeader(int userId) {
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new BasicNameValuePair("userid", userId + ""));
        return headers;
    }

    public static List<NameValuePair> prepareAuthHeader() {
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new BasicNameValuePair("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAHVKbgAAAAAACFHrwwRdkrZIOu%2B5I5lWdwoKX%2BU%3Dj7vpK2YlUjlXlXV2MyCLBGME9kbwqTGy16qXB6OUdiaz1K8Nen"));
        return headers;
    }

    /*******************************************************************************
     * PARAMS PREPARATION **********************************************************
     ******************************************************************************/

    /**
     * Empty params are used for basic requests
     * @return empty param list
     */
    public static List<NameValuePair> prepareEmptyParams() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        return params;
    }
}
