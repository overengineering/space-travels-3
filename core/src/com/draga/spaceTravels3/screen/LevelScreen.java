package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.event.ScoreUpdatedEvent;
import com.draga.spaceTravels3.manager.ScoreManager;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableDifficulty;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.BeepingTextButton;
import com.draga.spaceTravels3.ui.Screen;
import com.google.common.eventbus.Subscribe;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class LevelScreen extends Screen
{
    private final SerialisableLevel      serialisableLevel;
    private       Actor                  difficultiesList;
    private       Stage                  stage;
    private       HashMap<String, Label> difficultyHighScoreLabels;

    public LevelScreen(SerialisableLevel serialisableLevel)
    {
        super(true, true);

        this.serialisableLevel = serialisableLevel;

        this.difficultyHighScoreLabels = new HashMap<>();

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);

        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Header label.
        table.add(new Label(this.serialisableLevel.name, UIManager.skin));

        // Add a row with an expanded cell to fill the gap.
        table.row();
        table
            .add()
            .expand();

        // Setting buttons
        table.row();
        this.difficultiesList = getDifficultiesList(this.serialisableLevel);
        table.add(this.difficultiesList);

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

        Constants.General.EVENT_BUS.register(this);
    }

    private Actor getDifficultiesList(final SerialisableLevel serialisableLevel)
    {
        Table table = UIManager.getDefaultTable();
        LinkedHashMap<String, SerialisableDifficulty> serialisedDifficulties =
            serialisableLevel.serialisedDifficulties;

        for (final String difficulty : serialisedDifficulties.keySet())
        {
            SerialisableDifficulty serialisableDifficulty = serialisedDifficulties.get(difficulty);

            Table difficultyTable = new Table(UIManager.skin);

            // Header.
            difficultyTable.add(new Label(difficulty, UIManager.skin));
            difficultyTable.row();

            // Score.
            Integer score = ScoreManager.getScore(
                serialisableLevel.id,
                difficulty);

            Label difficultyHighScoreLabel = new Label(getHighScoreText(score), UIManager.skin);
            this.difficultyHighScoreLabels.put(difficulty, difficultyHighScoreLabel);

            difficultyTable.add(difficultyHighScoreLabel);
            difficultyTable.row();

            difficultyTable.add("");
            difficultyTable.row();

            // Difficulty details.
            Table innerDifficultyTable = UIManager.getDefaultTable();

            innerDifficultyTable
                .add("Projection line: ")
                .right();
            innerDifficultyTable
                .add(String.valueOf(serialisableDifficulty.trajectorySeconds))
                .right();
            innerDifficultyTable.row();

            innerDifficultyTable
                .add("Maximum landing speed: ")
                .right();
            innerDifficultyTable
                .add(String.valueOf(serialisableDifficulty.maxLandingSpeed))
                .right();
            innerDifficultyTable.row();

            innerDifficultyTable.add("Fuel: ")
                .right();
            innerDifficultyTable
                .add(serialisableDifficulty.infiniteFuel
                    ? "infinite"
                    : String.valueOf(serialisableDifficulty.fuel))
                .right();

            difficultyTable.add(innerDifficultyTable);
            difficultyTable.row();

            // Play button.
            BeepingTextButton beepingTextButton = new BeepingTextButton("Play", UIManager.skin);
            beepingTextButton.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    LoadingScreen loadingScreen = new LoadingScreen(
                        serialisableLevel,
                        difficulty);
                    ScreenManager.addScreen(loadingScreen);
                    super.clicked(event, x, y);
                }
            });

            difficultyTable.add(beepingTextButton);

            table.add(difficultyTable);
        }

        ScrollPane scrollPane = new ScrollPane(table, UIManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(false, true);

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
                ScreenManager.removeScreen(LevelScreen.this);
            }
        });

        return backTextButton;
    }

    private String getHighScoreText(Integer score)
    {
        if (score != null)
        {
            return "High score : " + String.valueOf(score);
        }
        else
        {
            return "";
        }
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
            ScreenManager.removeScreen(LevelScreen.this);
        }

        this.stage.getViewport().apply();
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
        Constants.General.EVENT_BUS.unregister(this);
    }

    @Subscribe
    public void ScoreUpdated(ScoreUpdatedEvent scoreUpdatedEvent)
    {
        if (scoreUpdatedEvent.levelID.equals(this.serialisableLevel.id))
        {
            this.difficultyHighScoreLabels.get(scoreUpdatedEvent.difficulty)
                .setText(getHighScoreText(scoreUpdatedEvent.score));
        }
    }
}
