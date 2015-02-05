package com.juanjo.betvictor.task.presenters;

import android.content.Context;

import com.google.android.gms.maps.model.Marker;
import com.google.inject.Inject;
import com.juanjo.betvictor.task.Interfaces.IMainActivity;
import com.juanjo.betvictor.task.Interfaces.IMainActivityPresenter;
import com.juanjo.betvictor.task.R;
import com.juanjo.betvictor.task.Task.StreamTweetTask;
import com.juanjo.betvictor.task.TweetApplication;
import com.juanjo.betvictor.task.Util.ConnectionHelper;
import com.juanjo.betvictor.task.Util.DatabaseHelper;
import com.juanjo.betvictor.task.events.InternetConnectionChangedEvent;
import com.juanjo.betvictor.task.models.Tweet;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.List;

import roboguice.inject.InjectResource;

/**
 * Created by juanjo on 3/2/15.
 */
public class MainActivityPresenter implements IMainActivityPresenter {

    @Inject
    ConnectionHelper connectionHelper;
    @Inject
    Context context;
    @Inject
    DatabaseHelper databaseHelper;
    @InjectResource(R.string.with_connection)
    String withConnection;
    @InjectResource(R.string.without_connection)
    String withoutConnection;

    StreamTweetTask streamTweetTask;

    IMainActivity view;
    boolean taskIsRunning;

    @Override
    public void onCreate(IMainActivity mainView) {
        view = mainView;
    }

    @Override
    public void onStart() {
        TweetApplication.getEventBus().register(this);
    }

    @Override
    public void onResume() {
        databaseHelper.open();
        taskIsRunning = false;
        view.loadMap();
        init();
    }

    @Override
    public void init() {
        if (connectionHelper.isConnected(context) && !taskIsRunning) {
            view.showMessage(withConnection);
            view.cleanMap();
            databaseHelper.removeAllTweets();
            startStreamTask();
        } else {
            view.showMessage(withoutConnection);
            List<Tweet> tweets = databaseHelper.getAllTweets();
            for (Tweet tweet : tweets)
                view.addPinToMap(tweet);
        }
    }

    @Override
    public void startStreamTask() {
        streamTweetTask = new StreamTweetTask();
        streamTweetTask.setListener(this);
        taskIsRunning = true;
        streamTweetTask.execute();
    }

    @Override
    public void showPinOnMap(Tweet tweet) {
        view.addPinToMap(tweet);
    }

    @Override
    public void onPause() {
        stopStreamTask();
    }

    @Override
    public void stopStreamTask() {
        if (streamTweetTask != null) {
            taskIsRunning = false;
            streamTweetTask.stopStream();
            streamTweetTask.cancel(true);
            streamTweetTask = null;
        }
    }

    @Override
    public void onStop() {
        databaseHelper.close();
        TweetApplication.getEventBus().unregister(this);

        //TODO remove
        try {
            DatabaseHelper.backupDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveTweetOnDatabase(final Tweet tweet) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (taskIsRunning)
                    databaseHelper.addTweet(tweet);
            }
        }).start();
    }

    @Override
    public void removeTweetFromDatabase(final Tweet tweet) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (canRemoveTweet())
                    databaseHelper.deleteTweet(tweet);
            }
        }).start();
    }

    @Override
    public void removeTweetFromMap(Marker marker) {
        if (canRemoveTweet())
            marker.remove();
    }

    private boolean canRemoveTweet() {
        return taskIsRunning && connectionHelper.isConnected(context);
    }

    /**
     * Here the app receives events when the internet connection change.
     * The events are sent by WifiReceivers.
     */
    @Subscribe
    public void onConnectionEvent(InternetConnectionChangedEvent event) {

        if (event.withInternetConnection())
            init();
        else
            stopStreamTask();
    }
}
