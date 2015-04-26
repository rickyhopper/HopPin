package com.rhxp.hoppin.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rhxp.hoppin.model.Checkin;
import com.rhxp.hoppin.model.Geo;
import com.rhxp.hoppin.model.User;

import java.util.List;

/**
 * Created by Ricky on 8/31/14.
 */
public class CheckinHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "HopPin_checkins.db";

    public static final String TABLE_NAME = "checkins";
    public static final String KEY_ID = "_id";
    public static final String KEY_SERVICE_NAME = "serviceName";
    public static final String KEY_SERVICE_ID = "serviceId"; //id within twitter, etc.
    public static final String KEY_LAT = "lat";
    public static final String KEY_LON = "lon";
    public static final String KEY_USER = "userFullName";
    public static final String KEY_USER_HANDLE = "userHandle"; //screen_name/handle
    public static final String KEY_USER_PIC_URL = "pictureUrl";
    public static final String KEY_FOLLOWERS_COUNT = "followersCount";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_CONTENT_LINK = "contentLink";
    public static final String KEY_RETWEET_COUNT = "retweetCount";
    public static final String KEY_FAVORITE_COUNT = "favoriteCount";
    public static final String KEY_CREATED_AT = "createdAt";

    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_SERVICE_NAME + " TEXT, "
                    + KEY_SERVICE_ID + " INTEGER, " + KEY_LAT + " FLOAT, "
                    + KEY_LON + " FLOAT, " + KEY_USER + " TEXT, " + KEY_USER_HANDLE + " TEXT, "
                    + KEY_USER_PIC_URL + " TEXT, " + KEY_FOLLOWERS_COUNT + " INTEGER, "
                    + KEY_CONTENT + " TEXT, " + KEY_CONTENT_LINK + " TEXT," + KEY_RETWEET_COUNT + " INTEGER,"
                    + KEY_FAVORITE_COUNT + " INTEGER, " + KEY_CREATED_AT + " TEXT);";

    public CheckinHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        arg0.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CheckinHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";"); //delete old table
        onCreate(db); //recreate table
    }

    //add checkin to database
    public void addCheckin(int id, Checkin c) {
        SQLiteDatabase db = this.getWritableDatabase(); //open writable db instance

        //checkin doesn't exist already
        //create database info from data
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_SERVICE_NAME, c.getService());
        values.put(KEY_SERVICE_ID, c.getId());
        values.put(KEY_LAT, c.getLat());
        values.put(KEY_LON, c.getLon());
        values.put(KEY_USER, c.getUser().getName());
        values.put(KEY_USER_HANDLE, c.getUser().getScreen_name());
        values.put(KEY_USER_PIC_URL, c.getUser().getProfile_image_url());
        values.put(KEY_FOLLOWERS_COUNT, c.getUser().getFollowers_count());
        values.put(KEY_CONTENT, c.getText());
        values.put(KEY_CONTENT_LINK, c.getContentLink());
        values.put(KEY_RETWEET_COUNT, c.getRetweet_count());
        values.put(KEY_FAVORITE_COUNT, c.getFavorite_count());
        values.put(KEY_CREATED_AT, c.getCreated_at());

        //insert the row into the database
        db.insert(TABLE_NAME, null, values);
        db.close(); //close database
    }

    //get list checkin from database (by id)
    public Checkin getCheckin(int id) {
        SQLiteDatabase db = this.getReadableDatabase(); //open readable db instance

        //query DB instance for checkin matching given id
        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_SERVICE_NAME, KEY_SERVICE_ID, KEY_LAT, KEY_LON, KEY_USER, KEY_USER_HANDLE, KEY_USER_PIC_URL, KEY_FOLLOWERS_COUNT, KEY_CONTENT, KEY_CONTENT_LINK, KEY_RETWEET_COUNT, KEY_FAVORITE_COUNT,KEY_CREATED_AT },
                KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Checkin c = null;
        if(cursor != null){
            double[] coords = {cursor.getDouble(3),cursor.getDouble(4)};
            Geo g = new Geo(coords);
            User u = new User(cursor.getString(5), cursor.getString(6), cursor.getString(7), Integer.parseInt(cursor.getString(8)));
            //create checkin from query result
            c = new Checkin(Integer.parseInt(cursor.getString(2)), cursor.getString(1),
                    (g), (u),cursor.getString(9), Integer.parseInt(cursor.getString(11)), Integer.parseInt(cursor.getString(12)), cursor.getString(14));

        }
        //close all resources
        cursor.close();
        db.close();

        return c;
    }

    //get all checkins from db, return cursor object
    public Cursor getAllCheckinsAsCursor() {
        //get db instance
        SQLiteDatabase db = this.getReadableDatabase();

        //create query to get all checkins contained in database
        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_SERVICE_NAME, KEY_SERVICE_ID, KEY_LAT, KEY_LON, KEY_USER, KEY_USER_HANDLE, KEY_USER_PIC_URL, KEY_FOLLOWERS_COUNT, KEY_CONTENT, KEY_CONTENT_LINK, KEY_RETWEET_COUNT, KEY_FAVORITE_COUNT,KEY_CREATED_AT },
                null, null, null, null, null);

        //move cursor to first instance
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();

        return cursor;
    }


    //delete a checkin
    public void deleteCheckin(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(id) });

        db.close();
    }

    //delete a checkin by serviceId
    public void deleteCheckinByServiceId(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, KEY_SERVICE_ID + " = ?", new String[] { String.valueOf(id) });

        db.close();
    }

    //delete a checkin
    public void deleteCheckin(Checkin c) {
        deleteCheckinByServiceId(c.getId());
    }

    //clear db
    public void clearLocalDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("CheckinHelper", "Clearing db");
        db.delete(TABLE_NAME, null, null);

        db.close();
    }

    //check if entry exists by service id, checkin's id
    public boolean hasCheckin(Checkin checkin) {
        boolean present = false;
        SQLiteDatabase db = this.getReadableDatabase(); //open readable db instance

        //query DB instance for checkin matching given id
        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_SERVICE_NAME, KEY_SERVICE_ID, KEY_LAT, KEY_LON, KEY_USER, KEY_USER_HANDLE, KEY_USER_PIC_URL, KEY_FOLLOWERS_COUNT, KEY_CONTENT, KEY_CONTENT_LINK, KEY_RETWEET_COUNT, KEY_FAVORITE_COUNT,KEY_CREATED_AT },
                KEY_SERVICE_ID + "=?", new String[] { String.valueOf(checkin.getId()) }, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            present = true;
        }

        return present;
    }

    //add a list of checkins to the db
    public void addListToDB(List<Checkin> list) {
        SQLiteDatabase db = this.getWritableDatabase(); //open writable db instance
        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        Log.i("CheckinHelper", "Number of entries: "+numRows);
        int insertIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            //checkin doesn't exist already
            //create database info from data
            Checkin c = list.get(i);
            if(!hasCheckin(c)){
                ContentValues values = new ContentValues();
                values.put(KEY_ID, insertIndex + numRows);
                values.put(KEY_SERVICE_NAME, c.getService());
                values.put(KEY_SERVICE_ID, c.getId());
                values.put(KEY_LAT, c.getLat());
                values.put(KEY_LON, c.getLon());
                values.put(KEY_USER, c.getUser().getName());
                values.put(KEY_USER_HANDLE, c.getUser().getScreen_name());
                values.put(KEY_USER_PIC_URL, c.getUser().getProfile_image_url());
                values.put(KEY_FOLLOWERS_COUNT, c.getUser().getFollowers_count());
                values.put(KEY_CONTENT, c.getText());
                values.put(KEY_CONTENT_LINK, c.getContentLink());
                values.put(KEY_RETWEET_COUNT, c.getRetweet_count());
                values.put(KEY_FAVORITE_COUNT, c.getFavorite_count());
                values.put(KEY_CREATED_AT, c.getCreated_at());
                Log.i("CheckinHelper", "Inserting at"  +  (insertIndex + numRows));
                //insert the row into the database
                db.insert(TABLE_NAME, null, values);
                insertIndex++;
            }
            else{
                Log.i("CheckinHelper", "Checkin already exists: "+ c.getId());
            }
        }
        numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        Log.i("CheckinHelper", "Number of entries: "+numRows);
        db.close();
    }
}
