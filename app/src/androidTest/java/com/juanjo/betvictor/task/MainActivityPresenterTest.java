package com.juanjo.betvictor.task;

import com.juanjo.betvictor.task.Interfaces.IMainActivity;
import com.juanjo.betvictor.task.Util.ConnectionHelper;
import com.juanjo.betvictor.task.Util.DatabaseHelper;
import com.juanjo.betvictor.task.presenters.MainActivityPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class MainActivityPresenterTest {


    @Mock
    ConnectionHelper connectionHelperMock;
    @Mock
    DatabaseHelper databaseHelperMock;
    @Mock
    IMainActivity viewMock;
    @Spy
    MainActivityPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new MainActivityPresenter();
    }

    @Test
    public void testOnResume() {

        presenter.onResume();
        verify(databaseHelperMock, times(1)).open();
        verify(viewMock, times(1)).loadMap();
        verify(presenter, times(1)).init();

    }
}