package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.event.ChangeDifficultyEvent;
import com.draga.spaceTravels3.event.ChangeLevelEvent;
import com.draga.spaceTravels3.event.ChangeLevelPackEvent;
import com.draga.spaceTravels3.level.Level;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.draga.spaceTravels3.manager.level.LevelPack;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.BeepingImageTextButton;
import com.draga.spaceTravels3.ui.Screen;

public abstract class IngameMenuScreen extends Screen
{
    public static String s =
        "gTAKsCqx0NeZVO9igYzMNjolg61Y6KLwLQaulfOZzuI2WLhPNr*mAmEy3T%VP08ZzWILHaHhKDHXeGP";

    protected final Level  level;
    protected final Cell   centreCell;
    protected       Screen gameScreen;

    public IngameMenuScreen(Screen gameScreen, Level level)
    {
        super(true, false);
        this.gameScreen = gameScreen;
        this.level = level;

        Table table = UIManager.addDefaultTableToStage(this.stage);

        table.setBackground(UIManager.getTiledDrawable(Constants.Visual.LIGHT_DARK));
        table.addAction(Actions.sequence(
            Actions.fadeOut(0),
            Actions.fadeIn(Constants.Visual.SCREEN_FADE_DURATION, Interpolation.pow2In)));

        // Header label.
        Actor headerLabel = getHeaderLabel();
        table.add(headerLabel);
        table.row();

        this.centreCell = table
            .add()
            .expand();
        table.row();

        Actor nextLevelOrPackButton = getNextLevelOrPackButton();
        String nextDifficulty =
            LevelManager.getNextDifficulty(this.level.getId(), this.level.getDifficulty());

        if (nextLevelOrPackButton != null
            || nextDifficulty != null)
        {
            Table innerTable = UIManager.getHorizontalPaddingTable();

            table
                .add(innerTable)
                .row();
            if (nextLevelOrPackButton != null)
            {
                innerTable
                    .add(nextLevelOrPackButton);
            }

            if (nextDifficulty != null)
            {
                innerTable
                    .add(getNextDifficultyButton(nextDifficulty));
            }
        }

        Table innerTable = UIManager.getHorizontalPaddingTable();
        innerTable
            .add(getRetryButton());
        innerTable
            .add(getSettingsButton(true));
        innerTable
            .add(getMainMenuTextButton());
        table
            .add(innerTable);
    }

    public Actor getHeaderLabel()
    {
        Table table = new Table();

        Label label = new Label(this.level.getName() + " ", UIManager.skin, "large");
        table.add(label);

        Image headerImage = this.level.getDestinationPlanet() != null
            ? new Image(this.level.getDestinationPlanet().graphicComponent.getTexture())
            : new Image();

        table
            .add(headerImage)
            .size(label.getHeight());

        table.add(new Label(" " + this.level.getDifficulty(), UIManager.skin));

        return table;
    }

    private Actor getNextLevelOrPackButton()
    {
        SerialisableLevel nextSerialisableLevel =
            LevelManager.getNextSerialisableLevel(this.level.getId());
        if (nextSerialisableLevel != null)
        {
            return getNextLevelButton(nextSerialisableLevel);
        }

        LevelPack nextLevelPack = LevelManager.getNextLevelPack(this.level.getId());
        if (nextLevelPack != null)
        {
            return getNextLevelPackButton(nextLevelPack);
        }

        return null;
    }

    private Actor getNextDifficultyButton(final String difficulty)
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton(difficulty + " difficulty", UIManager.skin, "next");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    ChangeDifficultyEvent changeDifficultyEvent =
                        new ChangeDifficultyEvent(difficulty);
                    Constants.General.EVENT_BUS.post(changeDifficultyEvent);
                    play(
                        IngameMenuScreen.this.level.getId(),
                        difficulty);
                }
            });

        return button;
    }

    public Actor getRetryButton()
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Try Again?", UIManager.skin, "retry");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    play(
                        IngameMenuScreen.this.level.getId(),
                        IngameMenuScreen.this.level.getDifficulty());
                }
            });

        return button;
    }

    protected Actor getMainMenuTextButton()
    {
        BeepingImageTextButton
            button = new BeepingImageTextButton("Main menu", UIManager.skin, "exit");
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ScreenManager.removeScreen(IngameMenuScreen.this);
                ScreenManager.removeScreen(IngameMenuScreen.this.gameScreen);
            }
        });

        return button;
    }

    private Actor getNextLevelButton(final SerialisableLevel nextSerialisableLevel)
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Next level", UIManager.skin, "next");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    ChangeLevelEvent changeLevelEvent =
                        new ChangeLevelEvent(nextSerialisableLevel);
                    Constants.General.EVENT_BUS.post(changeLevelEvent);
                    play(
                        nextSerialisableLevel.id,
                        nextSerialisableLevel.serialisedDifficulties.keySet().iterator().next());
                }
            });

        return button;
    }

    private Actor getNextLevelPackButton(final LevelPack levelPack)
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Next level pack", UIManager.skin, "next");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    ScreenManager.removeScreen(IngameMenuScreen.this);
                    ScreenManager.removeScreen(IngameMenuScreen.this.gameScreen);
                    ChangeLevelPackEvent changeLevelPackEvent =
                        new ChangeLevelPackEvent(levelPack);
                    Constants.General.EVENT_BUS.post(changeLevelPackEvent);
                }
            });

        return button;
    }

    private void play(String levelId, String difficulty)
    {
        ScreenManager.removeScreen(this);
        ScreenManager.removeScreen(this.gameScreen);
        ScreenManager.addScreen(new LoadingScreen(
            levelId,
            difficulty,
            false));
    }

    @Override
    public void show()
    {
        InputAdapter enterInputAdapter = new InputAdapter()
        {
            @Override
            public boolean keyUp(int keycode)
            {
                switch (keycode)
                {
                    case Input.Keys.ENTER:
                    {
                        play(
                            IngameMenuScreen.this.level.getId(),
                            IngameMenuScreen.this.level.getDifficulty());
                        return true;
                    }
                }
                return false;
            }
        };

        Gdx.input.setInputProcessor(new InputMultiplexer(
            this.stage,
            enterInputAdapter));
    }

    protected InputAdapter getCloseInputAdapter()
    {
        return new InputAdapter()
        {
            @Override
            public boolean keyUp(int keycode)
            {
                switch (keycode)
                {
                    case Input.Keys.ESCAPE:
                    case Input.Keys.BACK:
                    {
                        ScreenManager.removeScreen(IngameMenuScreen.this);
                        ScreenManager.removeScreen(IngameMenuScreen.this.gameScreen);
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
