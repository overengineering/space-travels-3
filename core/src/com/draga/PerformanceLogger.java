package com.draga;

import com.badlogic.gdx.Gdx;

import java.sql.Timestamp;
import java.util.Date;

public class PerformanceLogger implements Runnable
{
    private static final String LOGGING_TAG = PerformanceLogger.class.getSimpleName();

    @Override public void run()
    {
        String formattedJavaHeap = Constants.COMMA_SEPARATED_THOUSANDS_FORMATTER.format(
            Gdx.app.getJavaHeap());
        String formattedNativeHeap =
            Constants.COMMA_SEPARATED_THOUSANDS_FORMATTER.format(Gdx.app.getNativeHeap());
        String log = String.format(
            "%-23s | FPS : %3d | Java heap : %12s | Java native heap : %12s",
            new Timestamp(new Date().getTime()).toString(),
            Gdx.graphics.getFramesPerSecond(),
            formattedJavaHeap,
            formattedNativeHeap);
        Gdx.app.log(LOGGING_TAG, log);
    }
}
