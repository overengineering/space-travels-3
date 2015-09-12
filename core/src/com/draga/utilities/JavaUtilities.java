package com.draga.utilities;

/**
 * Created by Administrator on 12/09/2015.
 */
public class JavaUtilities
{
    /**
     * https://stackoverflow.com/questions/3776204/how-to-find-out-if-debug-mode-is-enabled
     *
     * @return If the application is being debugged
     */
    public static boolean isDebugMode()
    {
        boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().
            getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
        return isDebug;
    }
}
