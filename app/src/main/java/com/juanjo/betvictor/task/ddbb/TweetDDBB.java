package com.juanjo.betvictor.task.ddbb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.inject.Inject;

/**
 * Created by juanjo on 4/2/15.
 */
public class TweetDDBB extends SQLiteOpenHelper {

    public static final String TABLE_TWEETS = "tweets";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    private static final String DATABASE_NAME = "tweets.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TWEETS +
            "(" +
            COLUMN_ID + " double unique not null, " +
            COLUMN_LATITUDE + " double not null, " +
            COLUMN_LONGITUDE + " double not null );";

    @Inject
    public TweetDDBB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TweetDDBB.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TWEETS);
        onCreate(db);
    }

}

