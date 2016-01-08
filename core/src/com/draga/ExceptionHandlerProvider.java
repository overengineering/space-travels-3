package com.draga;

import java.util.ArrayList;

public abstract class ExceptionHandlerProvider
{
    private static final ArrayList<ExceptionHandler> CUSTOM_EXCEPTION_HANDLERS =
        new ArrayList<>();

    private ExceptionHandlerProvider()
    {
    }

    public static void addCustomExceptionHandler(ExceptionHandler exceptionHandler)
    {
        CUSTOM_EXCEPTION_HANDLERS.add(exceptionHandler);
    }

    public static void handle(String tag, String message, Exception exception)
    {
        for (ExceptionHandler exceptionHandler : CUSTOM_EXCEPTION_HANDLERS)
        {
            exceptionHandler.handle(tag, exception, message);
        }
    }

    public static void handle(String tag, String message)
    {
        for (ExceptionHandler exceptionHandler : CUSTOM_EXCEPTION_HANDLERS)
        {
            exceptionHandler.handle(tag, message);
        }
    }
}
