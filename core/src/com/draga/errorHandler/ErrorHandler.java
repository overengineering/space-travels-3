package com.draga.errorHandler;

public interface ErrorHandler
{
    void handle(String tag, Throwable throwable, String message);

    void handle(String tag, String message);
}
