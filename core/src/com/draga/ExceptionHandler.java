package com.draga;

public interface ExceptionHandler
{
    void handle(String tag, Exception exception, String message);

    void handle(String tag, String message);
}
