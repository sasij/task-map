package com.juanjo.betvictor.task.views;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.inject.Inject;
import com.juanjo.betvictor.task.Interfaces.IMainActivity;
import com.juanjo.betvictor.task.R;
import com.juanjo.betvictor.task.Util.DatabaseHelper;
import com.juanjo.betvictor.task.models.Tweet;
import com.juanjo.betvictor.task.presenters.MainActivityPresenter;

import java.io.IOException;
import java.util.Random;

import roboguice.activity.RoboFragmentActivity;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends RoboFragmentActivity implements
        IMainActivity, GoogleMap.OnMapLoadedCallback {

    final static int LIFESPAN = 5000;

    @Inject
    MainActivityPresenter presenter;

    GoogleMap map;
    boolean startTask;
    StreamTweetTask streamTweetTask;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter.onCreate(this);

        startTask = false;

        databaseHelper = new DatabaseHelper(this);


        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
//        map.setOnMapLoadedCallback(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseHelper.open();
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseHelper.close();
//        streamTweetTask.cancel(true);

        try {
            DatabaseHelper.backupDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showPin(final Tweet tweet) {

        final Marker marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(tweet.getLatitude(), tweet.getLongitude())));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("=> " + tweet.getId());
                marker.remove();
            }
        }, LIFESPAN);
    }

    @Override
    public void onMapLoaded() {
        streamTweetTask = new StreamTweetTask();
        streamTweetTask.execute();
    }

    public class StreamTweetTask extends AsyncTask<Context, Tweet, Boolean> {


        protected Boolean doInBackground(Context... arg0) {
            int rangeMin = 1;
            int rangeMax = 100;
            Random r;
            Tweet tweet;
            try {
                for (int i = 0; i < 100000; i++) {
                    Thread.sleep(50);
                    r = new Random();
                    double lat = rangeMin + (rangeMax - rangeMin)
                            * r.nextDouble();
                    double lng = rangeMin + (rangeMax - rangeMin)
                            * r.nextDouble();
                    tweet = new Tweet(i, lat, lng);
                    handleTweets(tweet);
                }
            } catch (Exception e) {
                Log.e("SampleTwitter", "doInBackground_" + e.toString());
            }

//			getTweet();

            return true;
        }

        private void handleTweets(Tweet tweet) {
            try {
                //save tweet on database
                publishProgress(tweet);
            } catch (Exception e) {
                Log.e("SampleTwitter", "handleTweets_" + e.toString());
            }
        }

        protected void onProgressUpdate(Tweet... tweets) {
            super.onProgressUpdate(tweets);
            showPin(tweets[0]);
            Log.d("SampleTwitter", "Im in onProgressUpdate()");
        }

        private void getTweet() {

            String consumerKey = "qyJvc190UWX9k7eriUs30oN3P";
            String consumerSecret = "EUJ0CIO1tJC0VjWmW9pHHNnzJdFcbRTdJmRenMmJ4qtJfjCx3n";
            String token = "143474171-aJqjg1XOVMgF9pV1dgD82AEsWeT1JwtTE7YJ1sjc";
            String secret = "lj3DybpelluVuS7ctGFVBe97rm1R1BmvGFOCUKD3uO91w";

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
                    .setOAuthConsumerSecret(consumerSecret)
                    .setOAuthAccessToken(token)
                    .setOAuthAccessTokenSecret(secret);

            TwitterStream ts = new TwitterStreamFactory(cb.build())
                    .getInstance();

            StatusListener listener = new StatusListener() {
                @Override
                public void onStatus(twitter4j.Status status) {
                    System.out.println("=>" + status.getText());

                }

                @Override
                public void onDeletionNotice(
                        StatusDeletionNotice statusDeletionNotice) {

                }

                @Override
                public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

                }

                @Override
                public void onScrubGeo(long userId, long upToStatusId) {

                }

                @Override
                public void onStallWarning(StallWarning warning) {

                }

                @Override
                public void onException(Exception ex) {

                }
            };

            ts.addListener(listener);
            FilterQuery filtre = new FilterQuery();
            String[] keywordsArray = {"iphone"};
            filtre.track(keywordsArray);
            ts.filter(filtre);
        }
    }
}