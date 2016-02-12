package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.SliderFixInputListener;
import com.draga.spaceTravels3.InputType;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.SoundManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.ui.BeepingImageTextButton;
import com.draga.spaceTravels3.ui.Screen;

public class SettingsScreen extends Screen
{
    public SettingsScreen()
    {
        super(true, true);

        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Header label.
        table.add("Settings", "large");

        // Setting buttons
        table.row();
        table
            .add(GetButtonScrollPane())
            .expand()
            .center();

        // Back button.
        table.row();
        table
            .add(getBackButton());
    }

    public ScrollPane GetButtonScrollPane()
    {
        Table table = UIManager.getDefaultTable();
        ScrollPane scrollPane = new ScrollPane(table);

        addInputType(table);
        table.row();

        addVolumeFX(table);

        table.row();

        addVolumeMusic(table);

        return scrollPane;
    }

    private void addInputType(Table table)
    {
        Label inputTypeLabel = new Label("Input type", UIManager.skin);
        table.add(inputTypeLabel);
        table.add(getInputTypeSelector());
    }

    private void addVolumeFX(Table table)
    {
        table.add(new Label("Effects volume", UIManager.skin));
        final Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, UIManager.skin);
        volumeSlider.setValue((float) Math.sqrt(SettingsManager.getSettings().volumeFX));
        volumeSlider.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                SettingsManager.getSettings().volumeFX =
                    (float) Math.pow(volumeSlider.getValue(), 2);
            }
        });
        volumeSlider.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SoundManager.buttonSound.play(SettingsManager.getSettings().volumeFX);
            }
        });

        volumeSlider.addListener(new SliderFixInputListener());
        table
            .add(volumeSlider)
            .width(this.stage.getWidth() / 2f);
    }

    private void addVolumeMusic(Table table)
    {
        table.add(new Label("Music volume", UIManager.skin));
        final Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, UIManager.skin);
        volumeSlider.setValue((float) Math.sqrt(SettingsManager.getSettings().getVolumeMusic()));
        volumeSlider.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                float volume = (float) Math.pow(volumeSlider.getValue(), 2);
                SettingsManager.getSettings().setVolumeMusic(volume);
            }
        });

        volumeSlider.addListener(new SliderFixInputListener());
        table
            .add(volumeSlider)
            .width(this.stage.getWidth() / 2f);
    }

    private Table getInputTypeSelector()
    {
        Table table = UIManager.getDefaultButtonsTable();

        ButtonGroup<ImageTextButton> buttonGroup = new ButtonGroup<>();
        buttonGroup.setMaxCheckCount(1);

        BeepingImageTextButton
            touchButton = new BeepingImageTextButton("Touch", UIManager.skin, "touch");
        touchButton.setChecked(SettingsManager.getSettings().getInputType() == InputType.TOUCH);
        touchButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SettingsManager.getSettings().setInputType(InputType.TOUCH);
            }
        });

        buttonGroup.add(touchButton);
        table
            .add(touchButton);

        BeepingImageTextButton accelerometerButton =
            new BeepingImageTextButton("Tilt", UIManager.skin, "accelerometer");
        accelerometerButton.setChecked(
            SettingsManager.getSettings().getInputType() == InputType.ACCELEROMETER);
        accelerometerButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SettingsManager.getSettings().setInputType(InputType.ACCELEROMETER);
            }
        });

        buttonGroup.add(accelerometerButton);
        table
            .add(accelerometerButton);

        return table;
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(new InputMultiplexer(this.stage, getBackInputAdapter()));
    }
}
