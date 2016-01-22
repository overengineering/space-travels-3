package com.draga;

public interface PlayServices
{
    void signIn();

    void signOut();

    void rateGame();

    void unlockAchievement(String string);

    void showAchievement(String achievementID);

    void submitScore(int highScore, String leaderboardID);

    void showScore(String leaderboardID);

    boolean isSignedIn();
}
