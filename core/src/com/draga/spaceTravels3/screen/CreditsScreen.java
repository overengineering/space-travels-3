package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.ui.Screen;

public class CreditsScreen extends Screen
{
    public CreditsScreen()
    {
        super(true, true);

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);

        Table table = UIManager.addDefaultTableToStage(this.stage);

        table.add("Credits", "large", Color.WHITE);
        table.row();

        table
            .add(getCreditsScrollPane())
            .center()
            .expand();
        table.row();

        table.add(getBackButton());

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private Actor getCreditsScrollPane()
    {
        Table table = UIManager.getDefaultTable();

        table
            .add("Developers", "large")
            .row();
        table
            .add("Stefano Chiodino\r\n"
                + "Lee Richardson")
            .row();

        table
            .add()
            .row();

        table
            .add("Earth, Jupiter, Venus, Mars", "large")
            .row();
        table
            .add("NASA")
            .row();

        table
            .add()
            .row();

        table
            .add("Star icon", "large")
            .row();
        table
            .add("Author: I, Ssolbergj\r\n"
                + "Changes: saturation, transparency\r\n"
                + "Licence: CC BY 3.0")
            .row();

        table
            .add()
            .row();

        table
            .add("Explosion sound", "large")
            .row();
        table
            .add("Author: Blender Foundation\r\n"
                + "Changes: normalised\r\n"
                + "Licence: CC BY 3.0")
            .row();

        table
            .add()
            .row();

        table
            .add("Thruster", "large")
            .row();
        Label thrusterLabel = new Label(
            "WrathGames Studio\r\n"
                + "http://wrathgames.com/blog\r\n"
                + "Licence: CC BY 3.0",
            UIManager.skin);
        thrusterLabel.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.net.openURI("http://wrathgames.com/blog");
            }
        });
        table
            .add(thrusterLabel)
            .row();

        table
            .add()
            .row();

        table
            .add("Thruster sound", "large")
            .row();
        Label thrusterSoundLabel = new Label(
            "Fire Loop by Iwan 'qubodup' Gabovitch\r\n"
                + "http://opengameart.org/users/qubodup\r\n"
                + "Licence: CC BY 3.0",
            UIManager.skin);
        thrusterSoundLabel.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.net.openURI("http://opengameart.org/users/qubodup");
            }
        });
        table
            .add(thrusterSoundLabel)
            .row();

        table
            .add()
            .row();

        table
            .add("Powered by", "large")
            .row();
        Label poweredByLabel = new Label(
            "libGDX & gdx-pay",
            UIManager.skin);
        poweredByLabel.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.net.openURI("http://libgdx.badlogicgames.com");
            }
        });
        table
            .add(poweredByLabel)
            .row();

        table
            .add("Licence links", "large")
            .row();
        Label ccby30Label = new Label(
            "Attribution 3.0 Unported (CC BY 3.0)\r\n"
                + "https://creativecommons.org/licenses/by/3.0/",
            UIManager.skin);
        ccby30Label.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.net.openURI("https://creativecommons.org/licenses/by/3.0/");
            }
        });
        table
            .add(ccby30Label)
            .row();


        ScrollPane scrollPane = new ScrollPane(table, UIManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        return scrollPane;
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(new InputMultiplexer(this.stage, getBackInputAdapter()));
    }
}
