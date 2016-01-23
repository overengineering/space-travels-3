package com.draga;

public interface PlayServices
{
    String FACEBOOK_SHARE_URI =
        "https://www.facebook.com/sharer/sharer.php?u=http://www.example.com";

    void shareFacebook();

    void invite();

    void signIn();

    boolean isSignedIn();

    void signOut();

    void showLeaderboards();

    void updateLeaderboard(String leaderboardID, int score);

    void showLeaderboard(String leaderboardID);

    void unlockAchievement(String string);

    void showAchievements();

    void rateApp();
}
