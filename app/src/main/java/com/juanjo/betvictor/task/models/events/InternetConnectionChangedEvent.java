package com.juanjo.betvictor.task.models.events;

/**
 * Created by juanjo on 15/01/15.
 */
public class InternetConnectionChangedEvent {

    boolean internetConnection;

    public InternetConnectionChangedEvent() {
    }

    public boolean withInternetConnection() {
        return internetConnection;
    }

    public void setInternetConnection(boolean internetConnection) {
        this.internetConnection = internetConnection;
    }
}
