package com.draga.spaceTravels3.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.ui.BeepingTextButton;

public class DebugMenuScreen implements Screen
{
    private Stage stage;

    public DebugMenuScreen()
    {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = UIManager.addDefaultTableToStage(stage);

        // Empty expanded top cell to keep the menu centered
        table.row();
        table
            .add()
            .expand();

        table.row();
        table.add(GetButtonScrollPane());

        // Empty expanded bottom cell to keep the menu centered
        table.row();
        table
            .add()
            .expand();

        // Back button
        TextButton backTextButton = getBackTextButton();
        table.row();
        table
            .add(backTextButton)
            .bottom();

        stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private TextButton getBackTextButton()
    {
        TextButton backTextButton = new BeepingTextButton("Back", UIManager.skin);
        backTextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SpaceTravels3.getGame().setScreen(new MenuScreen());
            }
        });

        return backTextButton;
    }

    public ScrollPane GetButtonScrollPane()
    {
        Table table = UIManager.getDefaultTable();

        table.add(getDebugDrawTextButton());
        table.row();
        table.add(getNoGravityTextButton());
        table.row();
        table.add(getInfiniteFuelTextButton());

        ScrollPane scrollPane = new ScrollPane(table);

        return scrollPane;
    }

    private TextButton getDebugDrawTextButton()
    {
        final TextButton debugDrawTextButton = new BeepingTextButton(
            "Debug draw",
            UIManager.skin.get(TextButton.TextButtonStyle.class));
        debugDrawTextButton.setChecked(SettingsManager.getDebugSettings().debugDraw);

        debugDrawTextButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    SettingsManager.getDebugSettings().debugDraw = !SettingsManager.getDebugSettings().debugDraw;
                    debugDrawTextButton.setChecked(SettingsManager.getDebugSettings().debugDraw);
                }
            });
        return debugDrawTextButton;
    }

    private TextButton getInfiniteFuelTextButton()
    {
        final TextButton infiniteFuelTextButton = new BeepingTextButton(
            "Infinite fuel",
            UIManager.skin.get(TextButton.TextButtonStyle.class));
        infiniteFuelTextButton.setChecked(SettingsManager.getDebugSettings().infiniteFuel);

        infiniteFuelTextButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    SettingsManager.getDebugSettings().infiniteFuel = !SettingsManager.getDebugSettings().infiniteFuel;
                    infiniteFuelTextButton.setChecked(SettingsManager.getDebugSettings().infiniteFuel);
                }
            });
        return infiniteFuelTextButton;
    }

    private TextButton getNoGravityTextButton()
    {
        final TextButton infiniteFuelTextButton = new BeepingTextButton(
            "No gravity",
            UIManager.skin.get(TextButton.TextButtonStyle.class));
        infiniteFuelTextButton.setChecked(SettingsManager.getDebugSettings().noGravity);

        infiniteFuelTextButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    SettingsManager.getDebugSettings().noGravity = !SettingsManager.getDebugSettings().noGravity;
                    infiniteFuelTextButton.setChecked(SettingsManager.getDebugSettings().noGravity);
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
            SpaceTravels3.getGame().setScreen(new MenuScreen());
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
