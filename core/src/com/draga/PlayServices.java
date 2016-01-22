package com.draga;

public interface PlayServices
{
    void signIn();

    void signOut();

    void rateGame();

    void unlockAchievement(String string);

    void showAchievements();

    void submitScore(int highScore, String leaderboardID);

    void showLeaderboard(String leaderboardID);

    boolean isSignedIn();

    void updateLeaderboard(String leaderboardID, int score);
}
