package com.mydeveloperplanet.mycreatetweetplanet;

import java.util.Scanner;

public class OAuth2Token {

    public static void main(String[] args) {
        TwitterCredentialsOAuth2 credentials = new TwitterCredentialsOAuth2(System.getenv("TWITTER_OAUTH2_CLIENT_ID"),
                System.getenv("TWITTER_OAUTH2_CLIENT_SECRET"),
                System.getenv("TWITTER_OAUTH2_ACCESS_TOKEN"),
                System.getenv("TWITTER_OAUTH2_REFRESH_TOKEN"));

//        OAuth2AccessToken accessToken = getAccessToken(credentials);
//        if (accessToken == null) {
//            return;
//        }
//
//        // Setting the access & refresh tokens into TwitterCredentialsOAuth2
//        credentials.setTwitterOauth2AccessToken(accessToken.getAccessToken());
//        credentials.setTwitterOauth2RefreshToken(accessToken.getRefreshToken());
        callApi(credentials);
    }

    public static OAuth2AccessToken getAccessToken(TwitterCredentialsOAuth2 credentials) {
        TwitterOAuth20Service service = new TwitterOAuth20Service(
                credentials.getTwitterOauth2ClientId(),
                credentials.getTwitterOAuth2ClientSecret(),
                "https://www.mydeveloperplanet.com",
                "offline.access tweet.read users.read tweet.write");

        OAuth2AccessToken accessToken = null;
        try {
            final Scanner in = new Scanner(System.in, "UTF-8");
            System.out.println("Fetching the Authorization URL...");

            final String secretState = "state";
            PKCE pkce = new PKCE();
            pkce.setCodeChallenge("challenge");
            pkce.setCodeChallengeMethod(PKCECodeChallengeMethod.PLAIN);
            pkce.setCodeVerifier("challenge");
            String authorizationUrl = service.getAuthorizationUrl(pkce, secretState);

            System.out.println("Go to the Authorization URL and authorize your App:\n" +
                    authorizationUrl + "\nAfter that paste the authorization code here\n>>");
            final String code = in.nextLine();
            System.out.println("\nTrading the Authorization Code for an Access Token...");
            accessToken = service.getAccessToken(pkce, code);

            System.out.println("Access token: " + accessToken.getAccessToken());
            System.out.println("Refresh token: " + accessToken.getRefreshToken());
        } catch (Exception e) {
            System.err.println("Error while getting the access token:\n " + e);
            e.printStackTrace();
        }
        return accessToken;
    }

    public static void callApi(TwitterCredentialsOAuth2 credentials) {
        TwitterApi apiInstance = new TwitterApi(credentials);
        TweetCreateRequest tweetCreateRequest = new TweetCreateRequest(); // TweetCreateRequest |
        tweetCreateRequest.setText("Test");
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
