package com.draga;

public interface PlayServices
{
    void signIn();

    boolean isSignedIn();

    void signOut();

    void showLeaderboards();

    void submitScore(int highScore, String leaderboardID);

    void updateLeaderboard(String leaderboardID, int score);

    void showLeaderboard(String leaderboardID);

    void unlockAchievement(String string);

    void showAchievements();

    void rateApp();
}
