package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.ScoreManager;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableDifficulty;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.BeepingTextButton;

import java.util.LinkedHashMap;

public class LevelScreen extends com.draga.spaceTravels3.ui.Screen
{
    private final SerialisableLevel serialisableLevel;
    private       Stage             stage;

    public LevelScreen(SerialisableLevel serialisableLevel)
    {
        super(true, true);

        this.serialisableLevel = serialisableLevel;

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
        table.add(getDifficultiesList(this.serialisableLevel));

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
                    ScreenManager.addScreen(loadingScreen);
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
    }
}
