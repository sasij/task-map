package com.juanjo.betvictor.task.presenters;

import com.juanjo.betvictor.task.Interfaces.IMainActivity;

import java.util.Random;

/**
 * Created by juanjo on 3/2/15.
 */
public class MainActivityPresenter {

    IMainActivity view;

    int rangeMin = 1;
    int rangeMax = 100;

    public void onCreate(IMainActivity mainView) {
        view = mainView;

        // TODO remove

        // init service to download tweet
        new Thread(new Runnable() {
            @Override
            public void run() {
                // createPins();
            }
        }).start();
    }

    private void createPins() {
        Random r;

        for (int i = 0; i < 100000; i++) {

            r = new Random();
            double lat = rangeMin + (rangeMax - rangeMin) * r.nextDouble();

            double lng = rangeMin + (rangeMax - rangeMin) * r.nextDouble();

//            view.showPin(i, lat, lng);
        }
    }

}
