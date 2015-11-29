package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.draga.BeepingClickListener;
import com.draga.manager.GameManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.SkinManager;

public class DebugMenuScreen implements Screen
{
    private Stage stage;

    public DebugMenuScreen()
    {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.pad(((stage.getHeight() + stage.getWidth()) / 2f) / 50f);

        table.row();
        table
            .add()
            .expand();

        table.row();
        table.add(GetButtonScrollPane());

        table.row();
        table
            .add()
            .expand();

        TextButton backTextButton = getBackTextButton();
        table.row();
        table
            .add(backTextButton)
            .bottom();

        stage.addActor(table);
        stage.setDebugAll(SettingsManager.getSettings().debugDraw);
    }

    private TextButton getBackTextButton()
    {
        TextButton backTextButton = new TextButton("Back", SkinManager.BasicSkin);
        backTextButton.addListener(new BeepingClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);
                GameManager.getGame().setScreen(new MenuScreen());
            }
        });

        return backTextButton;
    }

    public ScrollPane GetButtonScrollPane()
    {
        VerticalGroup verticalGroup = new VerticalGroup();

        verticalGroup.addActor(getDebugDrawTextButton());
        verticalGroup.addActor(getNoGravityTextButton());
        verticalGroup.addActor(getInfiniteFuelTextButton());

        ScrollPane scrollPane = new ScrollPane(verticalGroup);

        return scrollPane;
    }

    private TextButton getDebugDrawTextButton()
    {
        final TextButton debugDrawTextButton = new TextButton(
            "Debug draw",
            SkinManager.BasicSkin.get(TextButton.TextButtonStyle.class));
        debugDrawTextButton.setChecked(SettingsManager.getSettings().debugDraw);

        debugDrawTextButton.addListener(
            new BeepingClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    SettingsManager.getSettings().debugDraw = !SettingsManager.getSettings().debugDraw;
                    debugDrawTextButton.setChecked(SettingsManager.getSettings().debugDraw);
                }
            });
        return debugDrawTextButton;
    }

    private TextButton getInfiniteFuelTextButton()
    {
        final TextButton infiniteFuelTextButton = new TextButton(
            "Infinite fuel",
            SkinManager.BasicSkin.get(TextButton.TextButtonStyle.class));
        infiniteFuelTextButton.setChecked(SettingsManager.getSettings().infiniteFuel);

        infiniteFuelTextButton.addListener(
            new BeepingClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    SettingsManager.getSettings().infiniteFuel = !SettingsManager.getSettings().infiniteFuel;
                    infiniteFuelTextButton.setChecked(SettingsManager.getSettings().infiniteFuel);
                }
            });
        return infiniteFuelTextButton;
    }

    private TextButton getNoGravityTextButton()
    {
        final TextButton infiniteFuelTextButton = new TextButton(
            "No gravity",
            SkinManager.BasicSkin.get(TextButton.TextButtonStyle.class));
        infiniteFuelTextButton.setChecked(SettingsManager.getSettings().noGravity);

        infiniteFuelTextButton.addListener(
            new BeepingClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    SettingsManager.getSettings().noGravity = !SettingsManager.getSettings().noGravity;
                    infiniteFuelTextButton.setChecked(SettingsManager.getSettings().noGravity);
                }
            });
        return infiniteFuelTextButton;
    }

    @Override
    public void show()
    {
    }

    @Override
    public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            GameManager.getGame().setScreen(new MenuScreen());
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height);
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
        stage.dispose();
    }
}
