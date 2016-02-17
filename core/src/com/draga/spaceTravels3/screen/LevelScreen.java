package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.event.ChangeDifficultyEvent;
import com.draga.spaceTravels3.event.ChangeLevelEvent;
import com.draga.spaceTravels3.event.ChangeLevelPackEvent;
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

public class LevelScreen extends Screen
{
    private final LevelPack                           levelPack;
    private       SerialisableLevel                   serialisableLevel;
    private       Cell                                difficultyDetailsCell;
    private       Button                              playButton;
    private       Button                              leaderboardButton;
    private       String                              selectedDifficulty;
    private       ButtonGroup<BeepingImageTextButton> difficultyButtonGroup;

    public LevelScreen(LevelPack levelPack, final SerialisableLevel serialisableLevel)
    {
        super(true, true);

        this.levelPack = levelPack;
        this.serialisableLevel = serialisableLevel;

        addTable();

        Constants.General.EVENT_BUS.register(this);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(new InputMultiplexer(this.stage, getBackInputAdapter()));

        // Refreshes the difficulty list to update score if necessary.
        if (this.selectedDifficulty != null)
        {
            this.difficultyDetailsCell.setActor(getDifficultyDetails(this.selectedDifficulty));
        }
    }

    private Actor getDifficultyDetails(String difficulty)
    {
        SerialisableDifficulty serialisableDifficulty =
            this.serialisableLevel.serialisedDifficulties.get(difficulty);

        Table table = UIManager.getDefaultTable();

        Integer score = ScoreManager.getScore(this.serialisableLevel.id, difficulty);
        if (score != null)
        {
            table
                .add("High score: ")
                .right();
            table
                .add(String.valueOf(score))
                .right()
                .row();
        }
        else
        {
            table
                .add("")
                .row();
        }

        table
            .add("Projection line: ")
            .right();
        table
            .add(String.valueOf(serialisableDifficulty.trajectorySeconds) + " sec")
            .right();
        table.row();

        table
            .add("Maximum landing speed: ")
            .right();
        table
            .add(String.valueOf(serialisableDifficulty.maxLandingSpeed))
            .right();
        table.row();

        table.add("Fuel: ")
            .right();
        table
            .add(serialisableDifficulty.infiniteFuel
                ? "infinite"
                : String.valueOf(serialisableDifficulty.fuel))
            .right();

        return table;
    }

    @Override
    public void dispose()
    {
        Constants.General.EVENT_BUS.unregister(this);
        super.dispose();
    }

    @Subscribe
    public void levelChanged(ChangeLevelEvent changeLevelEvent)
    {
        this.serialisableLevel = changeLevelEvent.serialisableLevel;

        this.stage.clear();
        addTable();
    }

    private void addTable()
    {
        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Header label.
        table
            .add(getHeader())
            .row();

        table
            .add("Select difficulty")
            .row();

        // Level selection
        Table innerTable = UIManager.getHorizontalPaddingTable();

        innerTable
            .add(getDifficultySelection())
            .left();

        this.difficultyDetailsCell = innerTable
            .add()
            .expand()
            .center();

        Table buttonsTable = UIManager.getHorizontalPaddingTable();
        this.leaderboardButton = getLeaderboardButton();
        buttonsTable.add(this.leaderboardButton);

        this.playButton = getPlayButton();
        buttonsTable.add(this.playButton);

        table
            .add(innerTable)
            .expand()
            .row();

        table
            .add(buttonsTable)
            .row();

        // Back button.
        table
            .add(getBackButton());

        selectDifficulty(this.serialisableLevel.serialisedDifficulties.keySet().iterator().next());
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

    private Actor getDifficultySelection()
    {
        Table table = UIManager.getVerticalPaddingTable();
        this.difficultyButtonGroup = new ButtonGroup<>();
        this.difficultyButtonGroup.setMaxCheckCount(1);
        this.difficultyButtonGroup.setMinCheckCount(1);

        int i = 0;
        for (final String difficulty : this.serialisableLevel.serialisedDifficulties.keySet())
        {
            BeepingImageTextButton button =
                new BeepingImageTextButton(difficulty, UIManager.skin, "difficulty" + i++);
            button.setName(difficulty);
            button.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    selectDifficulty(difficulty);
                }
            });

            this.difficultyButtonGroup.add(button);
            table
                .add(button)
                .row();
        }

        return table;
    }

    private Button getLeaderboardButton()
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Leaderboard", UIManager.skin, "leaderboard");
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SpaceTravels3.services.googleShowLeaderboard(LevelScreen.this.serialisableLevel.serialisedDifficulties
                    .get(LevelScreen.this.selectedDifficulty).playLeaderboardID);
            }
        });
        button.setVisible(false);

        return button;
    }

    private Button getPlayButton()
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Play", UIManager.skin, "play");
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (LevelScreen.this.levelPack.isFree()
                    || SpaceTravels3.services.hasPurchasedSku(LevelScreen.this.levelPack.getGoogleSku()))
                {
                    if (!SettingsManager.getSettings().tutorialPlayed)
                    {
                        offerTutorial(
                            LevelScreen.this.serialisableLevel,
                            LevelScreen.this.selectedDifficulty);
                    }
                    else
                    {
                        launchLevel(
                            LevelScreen.this.serialisableLevel,
                            LevelScreen.this.selectedDifficulty);
                    }
                }
                else
                {
                    SpaceTravels3.services.purchaseSku(LevelScreen.this.levelPack.getGoogleSku());
                }
            }
        });
        button.setVisible(false);

        return button;
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

    @Subscribe
    public void levelPackChanged(ChangeLevelPackEvent changeLevelPackEvent)
    {
        ScreenManager.removeScreen(this);
    }

    @Subscribe
    public void levelPackChanged(ChangeDifficultyEvent changeDifficultyEvent)
    {
        selectDifficulty(changeDifficultyEvent.difficulty);
        this.difficultyButtonGroup.setChecked(changeDifficultyEvent.difficulty);
    }

    private void selectDifficulty(String difficulty)
    {
        LevelScreen.this.difficultyDetailsCell.setActor(getDifficultyDetails(difficulty));
        LevelScreen.this.playButton.setVisible(true);
        LevelScreen.this.leaderboardButton.setVisible(true);
        LevelScreen.this.selectedDifficulty = difficulty;
    }
}
