package com.draga;

public interface ErrorHandler
{
    void handle(String tag, Exception exception, String message);

    void handle(String tag, String message);
}
