package com.juanjo.betvictor.task.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.google.inject.Inject;
import com.juanjo.betvictor.task.ddbb.TweetDDBB;
import com.juanjo.betvictor.task.models.Tweet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanjo on 4/2/15.
 */
public class DatabaseHelper {

    @Inject
    TweetDDBB tweetDDBB;

    private SQLiteDatabase database;
    private String[] allColumns = {TweetDDBB.COLUMN_ID,
            TweetDDBB.COLUMN_LATITUDE, TweetDDBB.COLUMN_LONGITUDE};


    @Inject
    public DatabaseHelper(Context context) {
//        tweetDDBB = new TweetDDBB(context);
    }

    public void open() throws SQLException {
        database = tweetDDBB.getWritableDatabase();
    }

    public void close() {
        tweetDDBB.close();
    }

    public synchronized long addTweet(Tweet tweet) {
        ContentValues values = new ContentValues();
        values.put(TweetDDBB.COLUMN_ID, tweet.getId());
        values.put(TweetDDBB.COLUMN_LATITUDE, tweet.getLatitude());
        values.put(TweetDDBB.COLUMN_LONGITUDE, tweet.getLongitude());

        long id = database.insert(TweetDDBB.TABLE_TWEETS, null,
                values);

        return id;
    }

    public synchronized void deleteTweet(Tweet tweet) {
        double id = tweet.getId();
        System.out.println("Tweet deleted with id: " + id);
        database.delete(TweetDDBB.TABLE_TWEETS, TweetDDBB.COLUMN_ID
                + " = " + id, null);
    }

    public void removeAllTweets() {
        database.delete(TweetDDBB.TABLE_TWEETS, null, null);
    }

    public List<Tweet> getAllTweets() {
        List<Tweet> tweets = new ArrayList<Tweet>();

        Cursor cursor = database.query(TweetDDBB.TABLE_TWEETS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Tweet tweet = cursorToTweet(cursor);
            tweets.add(tweet);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return tweets;
    }

    private Tweet cursorToTweet(Cursor cursor) {
        Tweet tweet = new Tweet();
        tweet.setId(cursor.getDouble(0));
        tweet.setLatitude(cursor.getDouble(1));
        tweet.setLongitude(cursor.getDouble(2));
        return tweet;
    }

    public static void backupDatabase() throws IOException {
        // Open your local db as the input stream
        String inFileName = "/data/data/com.juanjo.betvictor.task/databases/tweets.db";
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStorageDirectory()
                + "/tweets.db";

        System.out.println("=> " + outFileName);

        // Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        // Close the streams
        output.flush();
        output.close();
        fis.close();
    }


}
