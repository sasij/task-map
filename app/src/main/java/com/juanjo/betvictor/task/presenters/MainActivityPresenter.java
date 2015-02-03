package com.juanjo.betvictor.task.presenters;

import com.juanjo.betvictor.task.Interfaces.IMainActivity;

/**
 * Created by juanjo on 3/2/15.
 */
public class MainActivityPresenter {

    IMainActivity view;

    public void onCreate(IMainActivity mainView) {
        view = mainView;
    }

}
