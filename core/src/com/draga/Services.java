package com.draga;

public interface Services
{
    void share();

    void purchaseFullVersion();

    boolean hasFullVersion();

    void googleSignIn();

    boolean googleIsSignedIn();

    void googleSignOut();

    void googleShowLeaderboards();

    void googleUpdateLeaderboard(String leaderboardID, int score);

    void googleShowLeaderboard(String leaderboardID);

    void googleUnlockAchievement(String string);

    void googleShowAchievements();

    void rateApp();
}
