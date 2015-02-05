package com.juanjo.betvictor.task.Util;

import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by juanjo on 4/02/15.
 */
public class TwitterHelper {

    static final String consumerKey = "qyJvc190UWX9k7eriUs30oN3P";
    static final String consumerSecret = "EUJ0CIO1tJC0VjWmW9pHHNnzJdFcbRTdJmRenMmJ4qtJfjCx3n";
    static final String token = "143474171-aJqjg1XOVMgF9pV1dgD82AEsWeT1JwtTE7YJ1sjc";
    static final String secret = "lj3DybpelluVuS7ctGFVBe97rm1R1BmvGFOCUKD3uO91w";

    public static ConfigurationBuilder getConfigurationBuilder() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(token)
                .setOAuthAccessTokenSecret(secret);
        return configurationBuilder;
    }

}
