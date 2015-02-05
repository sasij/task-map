package com.juanjo.betvictor.task;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.juanjo.betvictor.task.Interfaces.IMainActivity;
import com.juanjo.betvictor.task.Task.StreamTweetTask;
import com.juanjo.betvictor.task.Util.ConnectionHelper;
import com.juanjo.betvictor.task.Util.DatabaseHelper;
import com.juanjo.betvictor.task.models.Tweet;
import com.juanjo.betvictor.task.presenters.MainActivityPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import roboguice.RoboGuice;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class MainActivityPresenterTest {

    @Inject
    MainActivityPresenter presenter;

    @Mock
    ConnectionHelper connectionHelperMock;
    @Mock
    DatabaseHelper databaseHelperMock;
    @Mock
    IMainActivity viewMock;
    @Mock
    StreamTweetTask streamTweetTaskMock;

    MainActivityPresenter spyPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        RoboGuice.overrideApplicationInjector(Robolectric.application,
                new MyTestModule());
        RoboGuice.getInjector(Robolectric.application).injectMembers(this);
    }

    @Test
    public void testOnResume() {
        spyPresenter = spy(presenter);

        spyPresenter.onCreate(viewMock);
        spyPresenter.onResume();

        verify(databaseHelperMock, times(1)).open();
        verify(viewMock, times(1)).loadMap();
        verify(spyPresenter, times(1)).init();
    }

    @Test
    public void testInitWithConnectionEnabled() {

        spyPresenter = spy(presenter);

        doReturn(true).when(spyPresenter).canInitTheTask();

        spyPresenter.onCreate(viewMock);
        spyPresenter.init();

        verify(viewMock, times(1)).showMessage(anyString());
        verify(viewMock, times(1)).cleanMap();
        verify(databaseHelperMock, times(1)).removeAllTweetsFromDatabase();
        verify(spyPresenter, times(1)).startStreamTask(any(StreamTweetTask.class));

    }


    @Test
    public void testInitWithConnectionDisabled() {
        List<Tweet> tweets = new ArrayList<Tweet>();
        Tweet tweet1 = new Tweet();
        Tweet tweet2 = new Tweet();
        tweets.add(tweet1);
        tweets.add(tweet2);

        spyPresenter = spy(presenter);

        doReturn(false).when(spyPresenter).canInitTheTask();
        when(databaseHelperMock.getAllTweetsFromDatabase()).thenReturn(tweets);

        spyPresenter.onCreate(viewMock);
        spyPresenter.init();

        verify(viewMock, times(1)).showMessage("Without connection");
        verify(viewMock, times(2)).addPinToMap(any(Tweet.class));
    }

    @Test
    public void testStartStreamTask() {
        presenter.startStreamTask(streamTweetTaskMock);

        verify(streamTweetTaskMock, times(1)).setListener(presenter);
        verify(streamTweetTaskMock, times(1)).execute();
    }

    @Test
    public void testShowPinOnMap() {
        presenter.onCreate(viewMock);
        presenter.showPinOnMap(new Tweet());

        verify(viewMock, times(1)).addPinToMap(any(Tweet.class));
    }

    @Test
    public void testOnPause() {
        spyPresenter = spy(presenter);
        spyPresenter.onPause();
        verify(spyPresenter, times(1)).stopStreamTask(any(StreamTweetTask.class));
    }

    @Test
    public void testStopStreamTask() {
        presenter.stopStreamTask(streamTweetTaskMock);
        verify(streamTweetTaskMock, times(1)).stopStream();
        verify(streamTweetTaskMock, times(1)).cancel(true);
    }

    public class MyTestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(ConnectionHelper.class).toInstance(connectionHelperMock);
            bind(DatabaseHelper.class).toInstance(databaseHelperMock);
        }
    }
}