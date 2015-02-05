package com.juanjo.betvictor.task.views;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.inject.Inject;
import com.juanjo.betvictor.task.Interfaces.IMainActivity;
import com.juanjo.betvictor.task.R;
import com.juanjo.betvictor.task.models.Tweet;
import com.juanjo.betvictor.task.presenters.MainActivityPresenter;

import roboguice.activity.RoboFragmentActivity;

public class MainActivity extends RoboFragmentActivity implements
        IMainActivity, GoogleMap.OnMapLoadedCallback {

    final static int LIFESPAN = 5000;

    @Inject
    MainActivityPresenter presenter;

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter.onCreate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void loadMap() {
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        map.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded() {
        System.out.println("=> map loaded");
    }

    @Override
    public void addPinToMap(final Tweet tweet) {

        final Marker marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(tweet.getLatitude(), tweet.getLongitude())));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.removeTweetFromMap(marker);
                presenter.removeTweetFromDatabase(tweet);
            }
        }, LIFESPAN);
    }

    @Override
    public void cleanMap() {
        map.clear();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }
}