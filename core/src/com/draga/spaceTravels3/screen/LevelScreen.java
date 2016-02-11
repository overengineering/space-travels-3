package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.event.PurchasedEvent;
import com.draga.spaceTravels3.event.ScoreUpdatedEvent;
import com.draga.spaceTravels3.manager.ScoreManager;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.draga.spaceTravels3.manager.level.LevelPack;
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
    private final LevelPack                  levelPack;
    private final SerialisableLevel          serialisableLevel;
    private       Actor                      difficultiesList;
    private       HashMap<String, Label>     difficultyHighScoreLabels;
    private       ArrayList<ImageTextButton> playButtons;

    public LevelScreen(LevelPack levelPack, SerialisableLevel serialisableLevel)
    {
        super(true, true);

        this.levelPack = levelPack;
        this.serialisableLevel = serialisableLevel;

        this.difficultyHighScoreLabels = new HashMap<>();
        this.playButtons = new ArrayList<>();

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

        // Back button.
        table.row();
        table
            .add(getBackButton());

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
                this.serialisableLevel.serialisedDestinationPlanet.texturePath
            );

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
            playButton.setVisible(this.levelPack.isFree()
                || SpaceTravels3.services.hasPurchasedSku(this.levelPack.getGoogleSku()));
            playButton.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    if (LevelScreen.this.levelPack.isFree()
                        || SpaceTravels3.services.hasPurchasedSku(LevelScreen.this.levelPack.getGoogleSku()))
                    {
                        if (!SettingsManager.getSettings().tutorialPlayed)
                        {
                            offerTutorial(serialisableLevel, difficulty);
                        }
                        else
                        {
                            launchLevel(serialisableLevel, difficulty);
                        }
                    }
                    else
                    {
                        SpaceTravels3.services.purchaseSku(LevelScreen.this.levelPack.getGoogleSku());
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

    public void offerTutorial(final SerialisableLevel serialisableLevel, final String difficulty)
    {
        final Dialog dialog = new Dialog("", UIManager.skin);

        Table table = UIManager.getDefaultTable();

        dialog.add(table);

        table
            .add("Tutorial", "large")
            .center()
            .row();

        table
            .add("Space Travels 3 can be very challenging,\r\n"
                + "would you like to play a tutorial?")
            .center()
            .row();

        Table buttonsTable = UIManager.getDefaultTable();

        BeepingImageTextButton tutorialButton =
            new BeepingImageTextButton("Tutorial", UIManager.skin, "tutorial");

        tutorialButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SerialisableLevel tutorialSerialisableLevel =
                    LevelManager.getTutorialSerialisableLevel();
                ScreenManager.addScreen(new LoadingScreen(
                    tutorialSerialisableLevel,
                    "Tutorial",
                    true));
                dialog.hide();
            }
        });

        buttonsTable
            .add(tutorialButton)
            .left();

        BeepingImageTextButton dismissButton =
            new BeepingImageTextButton("Dismiss", UIManager.skin, "play");

        dismissButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                launchLevel(serialisableLevel, difficulty);
                SettingsManager.getSettings().tutorialPlayed = true;
                dialog.hide();
            }
        });

        buttonsTable
            .add(dismissButton)
            .right();

        table.add(buttonsTable);

        dialog.show(this.stage);
    }

    private void launchLevel(SerialisableLevel serialisableLevel, String difficulty)
    {
        LoadingScreen loadingScreen = new LoadingScreen(
            serialisableLevel,
            difficulty,
            false);
        ScreenManager.addScreen(loadingScreen);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(new InputMultiplexer(this.stage, getBackInputAdapter()));
    }

    @Override
    public void dispose()
    {
        Constants.General.EVENT_BUS.unregister(this);
        super.dispose();
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
    public void purchased(PurchasedEvent purchasedEvent)
    {
        if (!this.levelPack.isFree()
            && this.levelPack.getGoogleSku().equals(purchasedEvent.sku))
        {
            for (ImageTextButton playButton : this.playButtons)
            {
                playButton.setVisible(true);
            }
        }
    }
}
