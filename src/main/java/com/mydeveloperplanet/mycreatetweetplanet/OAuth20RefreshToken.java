package com.mydeveloperplanet.mycreatetweetplanet;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.twitter.clientlib.ApiClientCallback;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.TweetCreateRequest;
import com.twitter.clientlib.model.TweetCreateResponse;

public class OAuth20RefreshToken {
    public static void main(String[] args) {
        TwitterApi apiInstance = new TwitterApi(new TwitterCredentialsOAuth2(System.getenv("TWITTER_OAUTH2_CLIENT_ID"),
                System.getenv("TWITTER_OAUTH2_CLIENT_SECRET"),
                System.getenv("TWITTER_OAUTH2_ACCESS_TOKEN"),
                System.getenv("TWITTER_OAUTH2_REFRESH_TOKEN")));
        apiInstance.addCallback(new MaintainToken());

        try {
            apiInstance.refreshToken();
        } catch (Exception e) {
            System.err.println("Error while trying to refresh existing token : " + e);
            e.printStackTrace();
            return;
        }
        callApi(apiInstance);
    }

    private static void callApi(TwitterApi apiInstance) {

        TweetCreateRequest tweetCreateRequest = new TweetCreateRequest(); // TweetCreateRequest |
        tweetCreateRequest.setText("Hello World again!");
        try {
            TweetCreateResponse result = apiInstance.tweets().createTweet(tweetCreateRequest)
                    .execute();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling TweetsApi#createTweet");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}

class MaintainToken implements ApiClientCallback {
    @Override
    public void onAfterRefreshToken(OAuth2AccessToken accessToken) {
        System.out.println("access: " + accessToken.getAccessToken());
        System.out.println("refresh: " + accessToken.getRefreshToken());
    }
}
