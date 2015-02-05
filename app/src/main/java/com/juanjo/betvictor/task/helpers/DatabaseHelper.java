package com.juanjo.betvictor.task.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.inject.Inject;
import com.juanjo.betvictor.task.ddbb.TweetDDBB;
import com.juanjo.betvictor.task.models.Tweet;

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
    }

    public void open() throws SQLException {
        database = tweetDDBB.getWritableDatabase();
    }

    public void close() {
        tweetDDBB.close();
    }

    public synchronized long addTweetToDatabase(Tweet tweet) {
        ContentValues values = new ContentValues();
        values.put(TweetDDBB.COLUMN_ID, tweet.getId());
        values.put(TweetDDBB.COLUMN_LATITUDE, tweet.getLatitude());
        values.put(TweetDDBB.COLUMN_LONGITUDE, tweet.getLongitude());

        long id = database.insert(TweetDDBB.TABLE_TWEETS, null,
                values);

        return id;
    }

    public synchronized void deleteTweetFromDatabase(Tweet tweet) {
        double id = tweet.getId();
        database.delete(TweetDDBB.TABLE_TWEETS, TweetDDBB.COLUMN_ID
                + " = " + id, null);
    }

    public void removeAllTweetsFromDatabase() {
        database.delete(TweetDDBB.TABLE_TWEETS, null, null);
    }

    public List<Tweet> getAllTweetsFromDatabase() {
        List<Tweet> tweets = new ArrayList<Tweet>();

        Cursor cursor = database.query(TweetDDBB.TABLE_TWEETS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Tweet tweet = cursorToTweet(cursor);
            tweets.add(tweet);
            cursor.moveToNext();
        }

        cursor.close();
        return tweets;
    }

    /**
     * Get the tweet data from a row
     *
     * @param cursor
     * @return Tweet
     */
    private Tweet cursorToTweet(Cursor cursor) {
        Tweet tweet = new Tweet();
        tweet.setId(cursor.getDouble(0));
        tweet.setLatitude(cursor.getDouble(1));
        tweet.setLongitude(cursor.getDouble(2));
        return tweet;
    }

}
