package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.ScoreManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.BeepingTextButton;

public class MenuScreen implements Screen
{
    private Stage                   stage;
    private ButtonGroup<TextButton> buttonGroup;

    public MenuScreen()
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
        table
            .add(headerLabel)
            .top();

        // Add a row with an expanded cell to fill the gap.
        table.row();
        table
            .add()
            .expand();

        // Level list.
        table.row();
        ScrollPane levelsScrollPane = getLevelList();
        table.add(levelsScrollPane);

        // Add a row with an expanded cell to fill the gap.
        table.row();
        table
            .add()
            .expand();

        // Debug button.
        if (Constants.General.IS_DEBUGGING)
        {
            Actor debugButton = getDebugButton();

            table.row();
            table
                .add(debugButton)
                .bottom();
        }

        // Setting button.
        table.row();
        TextButton settingsTextButton = getSettingsTextButton();
        table
            .add(settingsTextButton)
            .bottom();

        // Play button.
        TextButton playButton = getPlayButton();
        table.row();
        table
            .add(playButton)
            .bottom();

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }
    
    public Label getHeaderLabel()
    {
        Label headerLabel = new Label("Space Travels 3", UIManager.skin, "large", Color.WHITE);

        return headerLabel;
    }

    private ScrollPane getLevelList()
    {
        java.util.List<SerialisableLevel> serialisableLevels = LevelManager.getSerialisableLevels();

        this.buttonGroup = new ButtonGroup<>();

        this.buttonGroup.setMaxCheckCount(1);
        this.buttonGroup.setMinCheckCount(1);
        this.buttonGroup.setUncheckLast(true);

        Table table = UIManager.getDefaultTable();

        for (SerialisableLevel serialisableLevel : serialisableLevels)
        {
            String buttonText =
                serialisableLevel.name + " (" + ScoreManager.getScore(serialisableLevel.id) + ")";
            TextButton textButton =
                new BeepingTextButton(buttonText, UIManager.skin);
            textButton.setName(serialisableLevel.id);
            this.buttonGroup.add(textButton);
            table.add(textButton);
            table.row();
        }

        ScrollPane scrollPane = new ScrollPane(table, UIManager.skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        return scrollPane;
    }

    public Actor getDebugButton()
    {
        TextButton debugButton = new BeepingTextButton("Debug", UIManager.skin);

        debugButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    SpaceTravels3.getGame().setScreen(new DebugMenuScreen());
                }
            });
        return debugButton;
    }

    private TextButton getSettingsTextButton()
    {
        TextButton settingsTextButton = new BeepingTextButton("Settings", UIManager.skin);

        settingsTextButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    SpaceTravels3.getGame().setScreen(new SettingsMenuScreen());
                }
            });

        return settingsTextButton;
    }

    public TextButton getPlayButton()
    {
        TextButton playButton = new BeepingTextButton("Play", UIManager.skin);

        playButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    StartGameScreen();
                }
            });

        return playButton;
    }

    private void StartGameScreen()
    {
        String levelId = this.buttonGroup.getChecked().getName();
        LoadingScreen loadingScreen = new LoadingScreen(levelId);
        SpaceTravels3.getGame().setScreen(loadingScreen);
    }

    @Override
    public void render(float deltaTime)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            StartGameScreen();
        }

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
        this.dispose();
    }

    @Override
    public void dispose()
    {
        this.stage.dispose();
    }
}
