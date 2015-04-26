package com.rhxp.hoppin.remote;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by rickyh on 4/25/15.
 *
 * Edited by xprimus on 4/25/15.
 * -added setter for checkin
 */
public class URLHelper {
    public static String CHECKINS_URL = "https://api.twitter.com/1.1/search/tweets.json?q=downtown&geocode=35.7804537,-78.6403943,10mi&result_type=recent&count=500";

    /**
     * Set the CHECKINS_URL to the span of the day before up to the next date, brings most recent tweets
     */
    public static void setCHECKINS_URL(){
        CHECKINS_URL = "https://api.twitter.com/1.1/search/tweets.json?q=-RT&geocode="+encode("35.7804537,-78.6403943,1mi")+"&result_type="+encode("recent")+"&count="+encode("500");
    }

    public static String encode(String str){
        String ret = "";
        try {
            ret = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
