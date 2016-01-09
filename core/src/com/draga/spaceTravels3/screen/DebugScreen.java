package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.ErrorHandlerProvider;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.ui.BeepingTextButton;

public class DebugScreen extends com.draga.spaceTravels3.ui.Screen
{
    private static final String LOGGING_TAG = DebugScreen.class.getSimpleName();
    private Stage stage;

    public DebugScreen()
    {
        super(true, true);

        this.stage = new Stage();

        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Empty expanded top cell to keep the menu centered
        table.row();
        table
            .add()
            .expand();

        table.row();
        table.add(getButtonScrollPane());
        table.row();
        table.add(getForceCrashButton());
        table.row();
        table.add(getErrorButton());

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

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private ScrollPane getButtonScrollPane()
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

    private TextButton getForceCrashButton()
    {
        TextButton forceCrashTextButton = new BeepingTextButton("Force crash", UIManager.skin);
        forceCrashTextButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                throw new RuntimeException("This is a crash");
            }
        });

        return forceCrashTextButton;
    }

    private Actor getErrorButton()
    {
        TextButton errorTextButton = new BeepingTextButton("Force error", UIManager.skin);
        errorTextButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ErrorHandlerProvider.handle(LOGGING_TAG, "test");
            }
        });

        return errorTextButton;
    }

    private TextButton getBackTextButton()
    {
        TextButton backTextButton = new BeepingTextButton("Back", UIManager.skin);
        backTextButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ScreenManager.removeScreen(DebugScreen.this);
            }
        });

        return backTextButton;
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
                    SettingsManager.getDebugSettings().debugDraw =
                        !SettingsManager.getDebugSettings().debugDraw;
                    debugDrawTextButton.setChecked(SettingsManager.getDebugSettings().debugDraw);
                }
            });

        return debugDrawTextButton;
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
                    SettingsManager.getDebugSettings().noGravity =
                        !SettingsManager.getDebugSettings().noGravity;
                    infiniteFuelTextButton.setChecked(SettingsManager.getDebugSettings().noGravity);
                }
            });

        return infiniteFuelTextButton;
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
                    SettingsManager.getDebugSettings().infiniteFuel =
                        !SettingsManager.getDebugSettings().infiniteFuel;
                    infiniteFuelTextButton.setChecked(SettingsManager.getDebugSettings().infiniteFuel);
                }
            });

        return infiniteFuelTextButton;
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            ScreenManager.removeScreen(DebugScreen.this);
        }

        this.stage.act(delta);
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
