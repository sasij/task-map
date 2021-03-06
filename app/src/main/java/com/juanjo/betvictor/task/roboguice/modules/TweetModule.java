package com.juanjo.betvictor.task.roboguice.modules;

import android.app.Application;

import com.google.inject.AbstractModule;
import com.juanjo.betvictor.task.Interfaces.IMainActivityPresenter;
import com.juanjo.betvictor.task.presenters.MainActivityPresenter;

/**
 * Created by juanjo on 05/02/15.
 */
//Indicate to injector what class have to bind to what interface
public class TweetModule extends AbstractModule {
    private Application application;

    public TweetModule() {
    }

    public TweetModule(Application application) {
        this.application = application;
    }

    @Override
    protected void configure() {
        bind(IMainActivityPresenter.class).to(MainActivityPresenter.class);
    }
}