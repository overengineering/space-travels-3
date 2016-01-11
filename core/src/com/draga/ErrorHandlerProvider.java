package com.draga;

import java.util.ArrayList;

public abstract class ErrorHandlerProvider
{
    public static final GuavaSubscriberExceptionHandler GUAVA_SUBSCRIBER_EXCEPTION_HANDLER =
        new GuavaSubscriberExceptionHandler();

    private static final ArrayList<ErrorHandler> CUSTOM_EXCEPTION_HANDLERS =
        new ArrayList<>();

    private ErrorHandlerProvider()
    {
    }

    public static void addCustomExceptionHandler(ErrorHandler errorHandler)
    {
        CUSTOM_EXCEPTION_HANDLERS.add(errorHandler);
    }

    public static void handle(String tag, String message, Throwable throwable)
    {
        for (ErrorHandler errorHandler : CUSTOM_EXCEPTION_HANDLERS)
        {
            errorHandler.handle(tag, throwable, message);
        }
    }

    public static void handle(String tag, String message)
    {
        for (ErrorHandler errorHandler : CUSTOM_EXCEPTION_HANDLERS)
        {
            errorHandler.handle(tag, message);
        }
    }
}
