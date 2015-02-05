package com.juanjo.betvictor.task;

import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by juanjo on 5/2/15.
 */
public class TweetApplication extends Application {


    private static Bus eventBus;

    public static Bus getEventBus() {
        return eventBus;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        eventBus = new Bus(ThreadEnforcer.ANY);
    }
}
