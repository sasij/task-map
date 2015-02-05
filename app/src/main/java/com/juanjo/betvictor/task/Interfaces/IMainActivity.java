package com.juanjo.betvictor.task.Interfaces;

import com.juanjo.betvictor.task.models.Tweet;

/**
 * Created by juanjo on 3/2/15.
 */
public interface IMainActivity {
    public void loadMap();

    public void addPinToMap(Tweet tweet);

    public void cleanMap();

    public void showMessage(String message);
}
