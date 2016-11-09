package com.draga.errorHandler;

import java.util.ArrayList;

public abstract class ErrorHandlerProvider
{
    public static final GuavaSubscriberExceptionHandler GUAVA_SUBSCRIBER_EXCEPTION_HANDLER =
        new GuavaSubscriberExceptionHandler();

    private static final ArrayList<ErrorHandler> ERROR_HANDLERS =
        new ArrayList<>();

    private ErrorHandlerProvider()
    {
    }

    public static void addErrorHandler(ErrorHandler errorHandler)
    {
        ERROR_HANDLERS.add(errorHandler);
    }

    public static void handle(String tag, String message, Throwable throwable)
    {
        for (ErrorHandler errorHandler : ERROR_HANDLERS)
        {
            errorHandler.handle(tag, throwable, message);
        }
    }

    public static void handle(String tag, String message)
    {
        for (ErrorHandler errorHandler : ERROR_HANDLERS)
        {
            errorHandler.handle(tag, message);
        }
    }
}
