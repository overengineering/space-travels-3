package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.event.ScoreUpdatedEvent;
import com.draga.spaceTravels3.event.VerifyPurchaseEvent;
import com.draga.spaceTravels3.manager.ScoreManager;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableDifficulty;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.BeepingImageTextButton;
import com.draga.spaceTravels3.ui.Screen;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LevelScreen extends Screen
{
    private final SerialisableLevel          serialisableLevel;
    private       Actor                      purchaseButton;
    private       Actor                      difficultiesList;
    private       Stage                      stage;
    private       HashMap<String, Label>     difficultyHighScoreLabels;
    private       ArrayList<ImageTextButton> playButtons;

    public LevelScreen(SerialisableLevel serialisableLevel)
    {
        super(true, true);

        this.serialisableLevel = serialisableLevel;

        this.difficultyHighScoreLabels = new HashMap<>();
        this.playButtons = new ArrayList<>();

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);

        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Header label.
        table.add(getHeader());

        // Setting buttons
        table.row();
        this.difficultiesList = getDifficultiesList(this.serialisableLevel);
        table
            .add(this.difficultiesList)
            .expand()
            .center();

        if (this.serialisableLevel.requiresFullVersion)
        {
            // Purchase button.
            table.row();
            this.purchaseButton = getPurchaseButton();
            table
                .add(this.purchaseButton)
                .bottom();
        }

        // Back button.
        table.row();
        table
            .add(getBackTextButton())
            .bottom();

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);

        Constants.General.EVENT_BUS.register(this);
    }

    private Actor getHeader()
    {
        Table table = new Table();

        Label label =
            new Label(this.serialisableLevel.name + " ", UIManager.skin, "large", Color.WHITE);
        table.add(label);


        Image headerImage =
            loadTextureAsync(
                this.serialisableLevel.serialisedDestinationPlanet.texturePath,
                AssMan.getAssMan());

        table
            .add(headerImage)
            .size(label.getHeight());

        return table;
    }

    private Actor getDifficultiesList(final SerialisableLevel serialisableLevel)
    {
        Table table = UIManager.getDefaultTable();
        LinkedHashMap<String, SerialisableDifficulty> serialisedDifficulties =
            serialisableLevel.serialisedDifficulties;

        for (final String difficulty : serialisedDifficulties.keySet())
        {
            final SerialisableDifficulty serialisableDifficulty =
                serialisedDifficulties.get(difficulty);

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

            // Difficulty details.
            Table innerDifficultyTable = UIManager.getDefaultTable();

            innerDifficultyTable
                .add("Projection line: ")
                .right();
            innerDifficultyTable
                .add(String.valueOf(serialisableDifficulty.trajectorySeconds) + " sec")
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
            innerDifficultyTable.row();

            // Leaderboard button.
            BeepingImageTextButton leaderboardButton =
                new BeepingImageTextButton("Leaderboard", UIManager.skin, "leaderboard");
            leaderboardButton.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    SpaceTravels3.services.googleShowLeaderboard(serialisableDifficulty.playLeaderboardID);
                }
            });
            innerDifficultyTable.add(leaderboardButton);

            // Play button.
            BeepingImageTextButton playButton =
                new BeepingImageTextButton("Play", UIManager.skin, "play");
            this.playButtons.add(playButton);
            playButton.setVisible(!this.serialisableLevel.requiresFullVersion
                || SpaceTravels3.services.hasFullVersion());
            playButton.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    if (!LevelScreen.this.serialisableLevel.requiresFullVersion
                        || SpaceTravels3.services.hasFullVersion())
                    {
                        LoadingScreen loadingScreen = new LoadingScreen(
                            serialisableLevel,
                            difficulty);
                        ScreenManager.addScreen(loadingScreen);
                        super.clicked(event, x, y);
                    }
                    else
                    {
                        SpaceTravels3.services.purchaseFullVersion();
                    }
                }
            });

            innerDifficultyTable.add(playButton);

            difficultyTable.add(innerDifficultyTable);

            table.add(difficultyTable);
        }

        ScrollPane scrollPane = new ScrollPane(table, UIManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(false, true);

        return scrollPane;
    }

    private Actor getPurchaseButton()
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Unlock", UIManager.skin, "unlock");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    SpaceTravels3.services.purchaseFullVersion();
                }
            });

        button.setVisible(!SpaceTravels3.services.hasFullVersion());

        return button;
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
            Gdx.app.exit();
        }

        loadAsyncImages(AssMan.getAssMan());

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
    public void scoreUpdated(ScoreUpdatedEvent scoreUpdatedEvent)
    {
        if (scoreUpdatedEvent.levelID.equals(this.serialisableLevel.id))
        {
            this.difficultyHighScoreLabels.get(scoreUpdatedEvent.difficulty)
                .setText(getHighScoreText(scoreUpdatedEvent.score));
        }
    }

    @Subscribe
    public void purchaseVerified(VerifyPurchaseEvent verifyPurchaseEvent)
    {
        if (this.serialisableLevel.requiresFullVersion)
        {
            this.purchaseButton.setVisible(!verifyPurchaseEvent.hasFullVersion);
            for (ImageTextButton playButton : this.playButtons)
            {
                playButton.setVisible(verifyPurchaseEvent.hasFullVersion);
            }

        }
    }
}
