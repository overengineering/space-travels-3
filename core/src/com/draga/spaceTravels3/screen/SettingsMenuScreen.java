package com.draga.spaceTravels3.screen;

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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.InputType;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.ui.BeepingTextButton;

public class SettingsMenuScreen implements Screen
{
    private Stage stage;

    public SettingsMenuScreen()
    {
    }

    @Override
    public void show()
    {
        this.stage = new Stage();
        Gdx.input.setInputProcessor(this.stage);

        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Header label.
        Label headerLabel = getHeaderLabel();
        table.add(headerLabel);

        // Add a row with an expanded cell to fill the gap.
        table.row();
        table
            .add()
            .expand();

        // Setting buttons
        table.row();
        table.add(GetButtonScrollPane());

        // Add a row with an expanded cell to fill the gap.
        table.row();
        table
            .add()
            .expand();

        // Back button.
        TextButton backTextButton = getBackTextButton();
        table.row();
        table
            .add(backTextButton)
            .bottom();

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
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

        addVolumeFX(table);

        table.row();

        addVolumeMusic(table);

        table.row();

        addHudSettings(table);

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
                SpaceTravels3.getGame().setScreen(new MenuScreen());
            }
        });

        return backTextButton;
    }

    private void addInputType(Table table)
    {
        Label inputTypeLabel = new Label("Input type", UIManager.skin);
        table.add(inputTypeLabel);
        table.add(getInputTypeSelector());
    }

    private void addVolumeFX(Table table)
    {
        table.add(new Label("Volume FX", UIManager.skin));
        final Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, UIManager.skin);
        volumeSlider.setValue(SettingsManager.getSettings().volumeFX);
        volumeSlider.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                SettingsManager.getSettings().volumeFX = volumeSlider.getValue();
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
            .width(this.stage.getWidth() / 2f);
    }

    private void addVolumeMusic(Table table)
    {
        table.add(new Label("Volume music", UIManager.skin));
        final Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, UIManager.skin);
        volumeSlider.setValue(SettingsManager.getSettings().getVolumeMusic());
        volumeSlider.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                SettingsManager.getSettings().setVolumeMusic(volumeSlider.getValue());
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
            .width(this.stage.getWidth() / 2f);
    }

    private void addHudSettings(Table table)
    {
        Label hudLabel =
            new Label("Hud", UIManager.skin);
        table.add(hudLabel);

        TextButton hudForceIndicatorsTextButton =
            new BeepingTextButton("Force indicators", UIManager.skin);
        hudForceIndicatorsTextButton.setChecked(
            SettingsManager.getSettings().hudForceIndicators);
        hudForceIndicatorsTextButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SettingsManager.getSettings().hudForceIndicators
                    = !SettingsManager.getSettings().hudForceIndicators;
            }
        });

        table.add(hudForceIndicatorsTextButton);
    }

    private Table getInputTypeSelector()
    {
        Table table = UIManager.getDefaultTable();

        ButtonGroup<TextButton> buttonGroup = new ButtonGroup<>();
        buttonGroup.setMaxCheckCount(1);

        TextButton touchButton = new BeepingTextButton("Touch", UIManager.skin);
        touchButton.setChecked(SettingsManager.getSettings().inputType == InputType.TOUCH);
        touchButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SettingsManager.getSettings().inputType = InputType.TOUCH;
            }
        });

        buttonGroup.add(touchButton);
        table.add(touchButton);

        TextButton accelerometerButton = new BeepingTextButton("Accelerometer", UIManager.skin);
        accelerometerButton.setChecked(
            SettingsManager.getSettings().inputType == InputType.ACCELEROMETER);
        accelerometerButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SettingsManager.getSettings().inputType = InputType.ACCELEROMETER;
            }
        });

        buttonGroup.add(accelerometerButton);
        table.add(accelerometerButton);

        return table;
    }

    @Override
    public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            SpaceTravels3.getGame().setScreen(new MenuScreen());
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
        dispose();
    }

    @Override
    public void dispose()
    {
        this.stage.dispose();
    }
}
