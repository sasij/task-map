package com.juanjo.betvictor.task.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;
import com.juanjo.betvictor.task.Interfaces.IMainActivityPresenter;
import com.juanjo.betvictor.task.helpers.TwitterHelper;
import com.juanjo.betvictor.task.listeners.TweetListener;
import com.juanjo.betvictor.task.models.Tweet;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * Created by juanjo on 5/2/15.
 */
public class StreamTweetTask extends AsyncTask<Context, Tweet, Boolean> {

    IMainActivityPresenter presenter;
    TwitterStream twitterStream;

    @Inject
    public StreamTweetTask() {

    }

    public void setListener(IMainActivityPresenter presenter) {
        this.presenter = presenter;
    }

    protected Boolean doInBackground(Context... arg0) {
        getTweet();
        return true;
    }

    private void getTweet() {

        twitterStream = new TwitterStreamFactory(TwitterHelper.getConfigurationBuilder().build())
                .getInstance();

        twitterStream.addListener(new TweetListener(this));

        FilterQuery filter = new FilterQuery();
        String[] keywordsArray = {"me"};
        filter.track(keywordsArray);
        twitterStream.filter(filter);
    }

    public void handleTweet(Tweet tweet) {
        presenter.saveTweetOnDatabase(tweet);
        publishProgress(tweet);
    }

    public void stopStream() {
        twitterStream.shutdown();
    }

    protected void onProgressUpdate(Tweet... tweets) {
        super.onProgressUpdate(tweets);
        presenter.showPinOnMap(tweets[0]);
    }

}



