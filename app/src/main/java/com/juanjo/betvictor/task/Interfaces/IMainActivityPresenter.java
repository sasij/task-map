package com.juanjo.betvictor.task.Interfaces;

import com.google.android.gms.maps.model.Marker;
import com.juanjo.betvictor.task.tasks.StreamTweetTask;
import com.juanjo.betvictor.task.models.Tweet;

/**
 * Created by juanjo on 5/2/15.
 */
public interface IMainActivityPresenter {

    public void onCreate(IMainActivity mainView);

    public void onStart();

    public void onResume();

    public void onPause();

    public void onStop();

    public void initProcess();

    public void startStreamTask(StreamTweetTask task);

    public void stopStreamTask(StreamTweetTask task);

    public void showPinOnMap(Tweet tweet);

    public void saveTweetOnDatabase(final Tweet tweet);

    public void removeTweetFromDatabase(final Tweet tweet);

    public void removeTweetFromMap(Marker marker);
}