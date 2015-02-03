package com.juanjo.betvictor.task.views;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.inject.Inject;
import com.juanjo.betvictor.task.Interfaces.IMainActivity;
import com.juanjo.betvictor.task.R;
import com.juanjo.betvictor.task.presenters.MainActivityPresenter;


public class MainActivity extends ActionBarActivity implements IMainActivity {

    @Inject
    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter.onCreate(this);
    }

}
