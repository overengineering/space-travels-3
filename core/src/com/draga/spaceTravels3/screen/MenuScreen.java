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
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableDifficulty;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.BeepingTextButton;

import java.util.LinkedHashMap;

public class MenuScreen implements Screen
{
    private Stage stage;

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
            this.stage.addActor(getDebugButton());
        }

        // Setting button.
        table.row();
        TextButton settingsTextButton = getSettingsTextButton();
        table
            .add(settingsTextButton)
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

        final Table table = UIManager.getDefaultTable();

        for (final SerialisableLevel serialisableLevel : serialisableLevels)
        {
            table.add(new Label(serialisableLevel.name, UIManager.skin));
            table.row();
            Actor levelsScrollPane = getDifficultiesList(serialisableLevel);
            table.add(levelsScrollPane);
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

    private Actor getDifficultiesList(final SerialisableLevel serialisableLevel)
    {
        Table table = UIManager.getDefaultTable();
        LinkedHashMap<String, SerialisableDifficulty> serialisedDifficulties =
            serialisableLevel.serialisedDifficulties;

        for (final String difficulty : serialisedDifficulties.keySet())
        {
            SerialisableDifficulty serialisableDifficulty = serialisedDifficulties.get(difficulty);

            VerticalGroup verticalGroup = new VerticalGroup();

            verticalGroup.addActor(new Label(difficulty, UIManager.skin));
            int score = ScoreManager.getScore(
                serialisableLevel.id,
                difficulty);
            verticalGroup.addActor(new Label(
                "High score : " + String.valueOf(score),
                UIManager.skin));
            verticalGroup.addActor(new Label("", UIManager.skin));

            Table difficultyTable = UIManager.getDefaultTable();

            difficultyTable
                .add(new Label("Projection line: ", UIManager.skin))
                .right();
            difficultyTable
                .add(new Label(
                    String.valueOf(serialisableDifficulty.trajectorySeconds),
                    UIManager.skin))
                .right();
            difficultyTable.row();

            difficultyTable
                .add(new Label("Maximum landing speed: ", UIManager.skin))
                .right();
            difficultyTable
                .add(new Label(
                    String.valueOf(serialisableDifficulty.maxLandingSpeed),
                    UIManager.skin))
                .right();
            difficultyTable.row();

            difficultyTable
                .add(new Label("Fuel: ", UIManager.skin))
                .right();
            difficultyTable
                .add(new Label(
                    serialisableDifficulty.infiniteFuel
                        ? "infinite"
                        : String.valueOf(serialisableDifficulty.fuel),
                    UIManager.skin))
                .right();

            verticalGroup.addActor(difficultyTable);

            BeepingTextButton beepingTextButton = new BeepingTextButton("Play", UIManager.skin);
            beepingTextButton.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    LoadingScreen loadingScreen = new LoadingScreen(
                        serialisableLevel,
                        difficulty);
                    SpaceTravels3.getGame().setScreen(loadingScreen);
                    super.clicked(event, x, y);
                }
            });

            verticalGroup.addActor(beepingTextButton);

            table.add(verticalGroup);
        }

        ScrollPane scrollPane = new ScrollPane(table, UIManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(false, true);

        return scrollPane;
    }

    @Override
    public void render(float deltaTime)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            Gdx.app.exit();
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
