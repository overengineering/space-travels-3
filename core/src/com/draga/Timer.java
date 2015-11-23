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
     * Log in GDX the elapsed time.
     * @param loggingTag The tag to log the message under.
     * @param message The message to log, must contain a "%f" to print the time taken.
     */
    public void elapsed(String loggingTag, String message)
    {
        long elapsedNanoTime = System.nanoTime() - startTime;
        Gdx.app.log(loggingTag, String.format(message, elapsedNanoTime * Constants.NANO));
    }

    /**
     * @return the elapsed seconds.
     */
    public float elapsed()
    {
        float elapsedTime = (System.nanoTime() - startTime)  * Constants.NANO;
        return elapsedTime;
    }
}
