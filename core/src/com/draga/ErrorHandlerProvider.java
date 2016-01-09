package com.draga;

import java.util.ArrayList;

public abstract class ErrorHandlerProvider
{
    private static final ArrayList<ErrorHandler> CUSTOM_EXCEPTION_HANDLERS =
        new ArrayList<>();

    private ErrorHandlerProvider()
    {
    }

    public static void addCustomExceptionHandler(ErrorHandler errorHandler)
    {
        CUSTOM_EXCEPTION_HANDLERS.add(errorHandler);
    }

    public static void handle(String tag, String message, Exception exception)
    {
        for (ErrorHandler errorHandler : CUSTOM_EXCEPTION_HANDLERS)
        {
            errorHandler.handle(tag, exception, message);
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
