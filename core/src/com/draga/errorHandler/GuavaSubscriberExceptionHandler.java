package com.draga.errorHandler;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

public class GuavaSubscriberExceptionHandler implements SubscriberExceptionHandler
{
    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context)
    {
        String message = "Event: "
            + context.getEvent()
            + "\r\nSubscriber: "
            + context.getSubscriber()
            + "\r\nSubscriber method:"
            + context.getSubscriberMethod();
        ErrorHandlerProvider.handle(context.getEventBus().toString(), message, exception);
    }
}
