package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.ui.BeepingTextButton;
import com.draga.spaceTravels3.ui.Screen;

public class CreditsScreen extends Screen
{
    private final Stage stage;

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

        table.add(getBackTextButton());

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private Actor getCreditsScrollPane()
    {
        Table table = UIManager.getDefaultTable();

        table.add("Developers");
        table.add("Stefano Chiodino\r\n"
            + "Lee Richardson");

        table.row();
        table.add();

        table.row();
        table.add("Earth\r\nJupiter\r\nVenus\r\nMars");
        table.add("NASA");

        table.row();
        table.add();

        table.row();
        table.add("Star\r\nicon");
        table.add("Author: I, Ssolbergj\r\n"
            + "Changes: saturation, transparency\r\n"
            + "Licence: CC BY 3.0");

        table.row();
        table.add();

        table.row();
        table.add("Explosion\r\nsound");
        table.add("Author: Blender Foundation\r\n"
            + "Changes: normalised\r\n"
            + "Licence: CC BY 3.0");

        table.row();
        table.add();

        table.row();
        table.add("Thruster");
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
        table.add(thrusterLabel);

        table.row();
        table.add();

        table.row();
        table.add("Thruster\r\nsound");
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
        table.add(thrusterSoundLabel);

        table.row();
        table.add();

        table.row();
        table.add("Licence\r\nlinks");
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
        table.add(ccby30Label);


        ScrollPane scrollPane = new ScrollPane(table, UIManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        return scrollPane;
    }

    private TextButton getBackTextButton()
    {
        TextButton backTextButton = new BeepingTextButton("Back", UIManager.skin);
        backTextButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ScreenManager.removeScreen(CreditsScreen.this);
            }
        });

        return backTextButton;
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float deltaTime)
    {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            ScreenManager.removeScreen(this);
        }

        this.stage.getViewport().apply();

        this.stage.act(deltaTime);
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        this.stage.getViewport().update(width, height);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        this.stage.dispose();
    }
}
