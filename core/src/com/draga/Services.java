package com.draga;

public interface Services
{
    void purchaseFullVersion();

    boolean hasFullVersion();

    void facebookShare();

    void facebookInvite();

    boolean facebookCanInvite();

    void googleInvite();

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
