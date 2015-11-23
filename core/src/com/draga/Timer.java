package com.draga;

import com.badlogic.gdx.Gdx;

public class Timer
{
    private long startTime;

    /**
     * Start the timer.
     * @return The timer for chaining
     */
    public Timer start()
    {
        this.startTime = System.nanoTime();
        return this;
    }

    /**
     * Stop the timer and log in GDX the time taken.
     * @param loggingTag The tag to log the message under.
     * @param message The message to log, must contain a "%f" to print the time taken.
     */
    public void stop(String loggingTag, String message)
    {
        long elapsedNanoTime = System.nanoTime() - startTime;
        Gdx.app.log(loggingTag, String.format(message, elapsedNanoTime * Constants.NANO));
    }
}
