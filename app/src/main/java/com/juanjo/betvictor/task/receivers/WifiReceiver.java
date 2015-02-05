package com.juanjo.betvictor.task.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.juanjo.betvictor.task.TweetApplication;
import com.juanjo.betvictor.task.events.InternetConnectionChangedEvent;

/**
 * Created by juanjo on 05/02/15.
 */
public class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isConnected = (wifi != null && wifi.isConnectedOrConnecting())
                || (mobile != null && mobile.isConnectedOrConnecting());

        InternetConnectionChangedEvent event = new InternetConnectionChangedEvent();
        event.setInternetConnection(isConnected);
        TweetApplication.getEventBus().post(event);

    }
}
