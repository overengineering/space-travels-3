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
import com.draga.manager.SkinManager;

public class SettingsMenuScreen implements Screen
{
    private Stage stage;

    public SettingsMenuScreen()
    {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.pad(((stage.getHeight() + stage.getWidth()) / 2f) / 50f);

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

        stage.addActor(table);
        stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    public Label getHeaderLabel()
    {
        Label headerLabel = new Label("Settings", SkinManager.skin);

        return headerLabel;
    }

    public ScrollPane GetButtonScrollPane()
    {
        Table table = new Table();

        //noinspection PointlessBooleanExpression
        if (Constants.IS_DEBUGGING
            || Gdx.app.getType() == Application.ApplicationType.Android
            || Gdx.app.getType() == Application.ApplicationType.iOS)
        {
            Label inputTypeLabel = new Label("Input type", SkinManager.skin);
            table.add(inputTypeLabel);
            table.add(getInputTypeSelector());
            table.row();
        }
        table.add(new Label("Volume", SkinManager.skin));
        final Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, SkinManager.skin);
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

        ScrollPane scrollPane = new ScrollPane(table);

        return scrollPane;
    }

    private TextButton getBackTextButton()
    {
        TextButton backTextButton = new TextButton("Back", SkinManager.skin);
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

    private VerticalGroup getInputTypeSelector()
    {
        VerticalGroup verticalGroup = new VerticalGroup();
        ButtonGroup<TextButton> buttonGroup = new ButtonGroup<>();
        buttonGroup.setMaxCheckCount(1);

        TextButton touchButton = new TextButton("Touch", SkinManager.skin);
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
        verticalGroup.addActor(touchButton);



        TextButton accelerometerButton = new TextButton("Accelerometer", SkinManager.skin);
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
        verticalGroup.addActor(accelerometerButton);

        return verticalGroup;
    }

    private TextButton getDebugDrawTextButton()
    {
        final TextButton debugDrawTextButton = new TextButton(
            "Debug draw",
            SkinManager.skin.get(TextButton.TextButtonStyle.class));
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
