package com.draga;

public interface ErrorHandler
{
    void handle(String tag, Throwable throwable, String message);

    void handle(String tag, String message);
}
