package com.draga.manager.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.draga.BeepingClickListener;
import com.draga.Constants;
import com.draga.InputType;
import com.draga.manager.GameManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.UIManager;

public class SettingsMenuScreen implements Screen
{
    private Stage stage;

    public SettingsMenuScreen()
    {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = UIManager.addDefaultTableToStage(stage);

        Label headerLabel = getHeaderLabel();
        table.add(headerLabel);

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

        stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    public Label getHeaderLabel()
    {
        Label headerLabel = new Label("Settings", UIManager.skin);

        return headerLabel;
    }

    public ScrollPane GetButtonScrollPane()
    {
        Table table = UIManager.getDefaultTable();
        ScrollPane scrollPane = new ScrollPane(table);

        //noinspection PointlessBooleanExpression
        if (Constants.General.IS_DEBUGGING
            || Gdx.app.getType() == Application.ApplicationType.Android
            || Gdx.app.getType() == Application.ApplicationType.iOS)
        {
            addInputType(table);
            table.row();
        }

        addVolume(table);

        table.row();

        addHud(table);

        return scrollPane;
    }

    private void addHud(Table table)
    {
        Label hudLabel =
            new Label("Hud", UIManager.skin);
        table.add(hudLabel);
        TextButton hudForceIndicatorsTextButton =
            new TextButton("Force indicators", UIManager.skin);
        hudForceIndicatorsTextButton.setChecked(
            SettingsManager.getSettings().hudForceIndicators);
        hudForceIndicatorsTextButton.addListener(new BeepingClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);
                SettingsManager.getSettings().hudForceIndicators
                    = !SettingsManager.getSettings().hudForceIndicators;
            }
        });
        table.add(hudForceIndicatorsTextButton);
    }

    private void addVolume(Table table)
    {
        table.add(new Label("Volume", UIManager.skin));
        final Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, UIManager.skin);
        volumeSlider.setValue(SettingsManager.getSettings().volume);
        volumeSlider.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                SettingsManager.getSettings().volume = volumeSlider.getValue();
            }
        });

        // Fix a bug that makes the slider jump back to zero when dragging.
        // Ref. http://badlogicgames.com/forum/viewtopic.php?f=11&t=12612
        InputListener stopTouchDown = new InputListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                event.stop();
                return false;
            }
        };
        volumeSlider.addListener(stopTouchDown);
        table
            .add(volumeSlider)
            .width(stage.getWidth() / 2f);
    }

    private void addInputType(Table table)
    {
        Label inputTypeLabel = new Label("Input type", UIManager.skin);
        table.add(inputTypeLabel);
        table.add(getInputTypeSelector());
    }

    private TextButton getBackTextButton()
    {
        TextButton backTextButton = new TextButton("Back", UIManager.skin);
        backTextButton.addListener(new BeepingClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);
                GameManager.getGame().setScreen(new MenuScreen());
            }
        });

        return backTextButton;
    }

    private Table getInputTypeSelector()
    {
        Table table = UIManager.getDefaultTable();

        ButtonGroup<TextButton> buttonGroup = new ButtonGroup<>();
        buttonGroup.setMaxCheckCount(1);

        TextButton touchButton = new TextButton("Touch", UIManager.skin);
        touchButton.setChecked(SettingsManager.getSettings().inputType == InputType.TOUCH);
        touchButton.addListener(new BeepingClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);
                SettingsManager.getSettings().inputType = InputType.TOUCH;
            }
        });
        buttonGroup.add(touchButton);
        table.add(touchButton);

        TextButton accelerometerButton = new TextButton("Accelerometer", UIManager.skin);
        accelerometerButton.setChecked(SettingsManager.getSettings().inputType
            == InputType.ACCELEROMETER);
        accelerometerButton.addListener(new BeepingClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);
                SettingsManager.getSettings().inputType = InputType.ACCELEROMETER;
            }
        });
        buttonGroup.add(accelerometerButton);
        table.add(accelerometerButton);

        return table;
    }

    private TextButton getDebugDrawTextButton()
    {
        final TextButton debugDrawTextButton = new TextButton(
            "Debug draw",
            UIManager.skin.get(TextButton.TextButtonStyle.class));
        debugDrawTextButton.setChecked(SettingsManager.getDebugSettings().debugDraw);

        debugDrawTextButton.addListener(
            new BeepingClickListener()
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
