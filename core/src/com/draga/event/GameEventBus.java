package com.draga.event;

import com.google.common.eventbus.EventBus;

/**
 * Created by Administrator on 19/09/2015.
 */
public abstract class GameEventBus
{
    public static final EventBus GRAVITY_EVENT_BUS = new EventBus();
}
