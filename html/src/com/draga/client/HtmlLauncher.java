package com.draga.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.draga.SpaceTravels3;

public class HtmlLauncher extends GwtApplication
{

    @Override public GwtApplicationConfiguration getConfig()
    {
        return new GwtApplicationConfiguration(480, 320);
    }

    @Override public ApplicationListener getApplicationListener()
    {
        return new SpaceTravels3();
    }
}
