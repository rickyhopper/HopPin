package com.rhxp.hoppin.model;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xavierprimus on 4/26/15.
 */
public class CheckinList {
    private List<Checkin> statuses = new ArrayList<Checkin>();

    public CheckinList(ArrayList<Checkin> checkins){
        this.statuses = checkins;
    }

    public List<Checkin> getStatuses(){
        return this.statuses;
    }
    @Override
    public String toString() {
        String str = "CheckinList [";
        for(Checkin entity: statuses){
            str +="{" + entity.toString() + "}, ";
        }
        str= str.substring(0,str.length()-1);
        str +="]";
        return str;
    }

    public static List<Checkin> parse(String json) {
        Gson gson = new Gson();
        CheckinList list = null;

        BufferedReader bufferedReader = new BufferedReader(
                new StringReader(json));

        //convert the json string to list of checkins
        list = gson.fromJson(bufferedReader, CheckinList.class);

        Log.i("CheckinList", list.toString());

        return list.getStatuses();
    }

}
