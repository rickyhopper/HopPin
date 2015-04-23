package com.rhxp.hoppin.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rhxp.hoppin.model.Checkin;

import java.util.List;

/**
 * Created by Ricky on 8/31/14.
 */
public class CheckinHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HopPin_checkins.db";

    public static final String TABLE_NAME = "checkins";
    public static final String KEY_ID = "_id";
    public static final String KEY_SERVICE_NAME = "serviceName";
    public static final String KEY_SERVICE_ID = "serviceId"; //id within twitter, etc.
    public static final String KEY_LAT = "lat";
    public static final String KEY_LON = "lon";
    public static final String KEY_USER = "userId";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_CONTENT_LINK = "contentLink";

    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_SERVICE_NAME + " TEXT, "
                    + KEY_SERVICE_ID + " INTEGER, " + KEY_LAT + " FLOAT, "
                    + KEY_LON + " FLOAT, " + KEY_USER + " INTEGER, "
                    + KEY_CONTENT + " TEXT, " + KEY_CONTENT_LINK + " TEXT );";

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
    public void addCheckin(Checkin c) {
        SQLiteDatabase db = this.getWritableDatabase(); //open writable db instance

        //checkin doesn't exist already
        //create database info from data
        ContentValues values = new ContentValues();
        values.put(KEY_ID, c.getId());
        values.put(KEY_SERVICE_NAME, c.getService());
        values.put(KEY_SERVICE_ID, c.getServiceId());
        values.put(KEY_LAT, c.getLat());
        values.put(KEY_LON, c.getLon());
        values.put(KEY_USER, c.getUser());
        values.put(KEY_CONTENT, c.getContent());
        values.put(KEY_CONTENT_LINK, c.getContentLink());

        //insert the row into the database
        db.insert(TABLE_NAME, null, values);
        db.close(); //close database
    }

    //get list checkin from database (by id)
    public Checkin getCheckin(int id) {
        SQLiteDatabase db = this.getReadableDatabase(); //open readable db instance

        //query DB instance for checkin matching given id
        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_SERVICE_NAME, KEY_SERVICE_ID, KEY_LAT, KEY_LON, KEY_USER, KEY_CONTENT, KEY_CONTENT_LINK },
                KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        //create checkin from query result
        Checkin c = new Checkin(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                Integer.parseInt(cursor.getString(2)), Double.parseDouble(cursor.getString(3)),
                Double.parseDouble(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
                cursor.getString(6), cursor.getString(7));

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
        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_SERVICE_NAME, KEY_SERVICE_ID, KEY_LAT, KEY_LON, KEY_USER, KEY_CONTENT, KEY_CONTENT_LINK },
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

    //delete a checkin
    public void deleteCheckin(Checkin c) {
        deleteCheckin(c.getId());
    }

    //clear db
    public void clearLocalDB() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "1 = 1", new String[0]);

        db.close();
    }

    //add a list of checkins to the db
    public void addListToDB(List<Checkin> list) {
        SQLiteDatabase db = this.getWritableDatabase(); //open writable db instance

        for (int i = 0; i < list.size(); i++) {
            //checkin doesn't exist already
            //create database info from data
            Checkin c = list.get(i);

            ContentValues values = new ContentValues();
            values.put(KEY_ID, c.getId());
            values.put(KEY_SERVICE_NAME, c.getService());
            values.put(KEY_SERVICE_ID, c.getServiceId());
            values.put(KEY_LAT, c.getLat());
            values.put(KEY_LON, c.getLon());
            values.put(KEY_USER, c.getUser());
            values.put(KEY_CONTENT, c.getContent());
            values.put(KEY_CONTENT_LINK, c.getContentLink());

            //insert the row into the database
            db.insert(TABLE_NAME, null, values);
        }

        db.close();
    }
}
