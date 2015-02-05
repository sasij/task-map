package com.juanjo.betvictor.task.listeners;

import com.juanjo.betvictor.task.tasks.StreamTweetTask;
import com.juanjo.betvictor.task.models.Tweet;

import twitter4j.StallWarning;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

/**
 * Created by juanjo on 5/2/15.
 */
public class TweetListener implements StatusListener {

    StreamTweetTask task;

    public TweetListener(StreamTweetTask task) {
        this.task = task;
    }

    @Override
    public void onStatus(twitter4j.Status status) {
        Tweet tweet = new Tweet(System.currentTimeMillis(),
                status.getGeoLocation().getLatitude(),
                status.getGeoLocation().getLongitude());
        task.handleTweet(tweet);
    }

    @Override
    public void onDeletionNotice(
            StatusDeletionNotice statusDeletionNotice) {

    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {

    }

    @Override
    public void onStallWarning(StallWarning warning) {

    }

    @Override
    public void onException(Exception ex) {

    }

}
