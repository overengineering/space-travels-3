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
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.BeepingClickListener;
import com.draga.spaceTravels3.ui.BeepingImageTextButton;
import com.draga.spaceTravels3.ui.BeepingTextButton;
import com.draga.spaceTravels3.ui.Screen;

public class MenuScreen extends Screen
{
    private Stage stage;
    private float levelIconsSize;

    public MenuScreen()
    {
        super(true, true);

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);
        this.levelIconsSize = this.stage.getWidth() / 10f;

        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Header label.
        Label headerLabel = getHeaderLabel();
        table
            .add(headerLabel)
            .top();

        // Level list.
        table.row();
        table
            .add(getLevelList())
            .expand()
            .center();

        // Buttons.
        table.row();
        Table buttonsTable = UIManager.getDefaultTable();

        buttonsTable.add(getSettingsTextButton());
        buttonsTable.add(getTutorialButton());
        buttonsTable.add(getCreditsButton());
        buttonsTable.add(getShareButton());
        buttonsTable.row();
        buttonsTable.add(getRateButton());
        buttonsTable.add(getAchievementsButton());
        buttonsTable.add(getLeaderboardsButton());
        if (!SpaceTravels3.services.hasFullVersion())
        {
            buttonsTable.add(getPurchaseButton());
        }

        table.add(buttonsTable);

        // Debug button.
        if (Constants.General.IS_DEBUGGING)
        {
            this.stage.addActor(getDebugButton());
        }

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

        final Table outerTable = UIManager.getDefaultTable();

        for (final SerialisableLevel serialisableLevel : serialisableLevels)
        {
            Table innerTable = UIManager.getDefaultTable();

            // If the level icon is not loaded in the ass man then add to an map to load them async.
            Image image = loadTextureAsync(
                serialisableLevel.serialisedDestinationPlanet.texturePath,
                AssMan.getAssMan());

            innerTable
                .add(image)
                .size(this.levelIconsSize);
            innerTable.row();

            innerTable.add(serialisableLevel.name);
            innerTable.row();

            innerTable.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
            innerTable.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    LevelScreen levelScreen = new LevelScreen(serialisableLevel);
                    ScreenManager.addScreen(levelScreen);
                }
            });

            outerTable.add(innerTable);
        }

        ScrollPane scrollPane = new ScrollPane(outerTable, UIManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(false, true);

        return scrollPane;
    }

    private Actor getSettingsTextButton()
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Settings", UIManager.skin, "settings");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    ScreenManager.addScreen(new SettingsScreen());
                }
            });

        return button;
    }

    private Actor getTutorialButton()
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Tutorial", UIManager.skin, "tutorial");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    ScreenManager.addScreen(new TutorialScreen());
                }
            });

        return button;
    }

    private Actor getCreditsButton()
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Credits", UIManager.skin, "credits");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    ScreenManager.addScreen(new CreditsScreen());
                }
            });

        return button;
    }

    private Actor getShareButton()
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Share", UIManager.skin, "share");
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ScreenManager.addScreen(new ShareScreen());
            }
        });

        return button;
    }

    private Actor getRateButton()
    {
        BeepingImageTextButton
            button = new BeepingImageTextButton("Rate", UIManager.skin, "rate");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    SpaceTravels3.services.rateApp();
                }
            });

        return button;
    }

    private Actor getAchievementsButton()
    {
        BeepingImageTextButton
            button = new BeepingImageTextButton("Achievements", UIManager.skin, "achievement");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    SpaceTravels3.services.googleShowAchievements();
                }
            });

        return button;
    }
    
    private Actor getLeaderboardsButton()
    {
        BeepingImageTextButton
            button = new BeepingImageTextButton("Leaderboards", UIManager.skin, "leaderboard");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    SpaceTravels3.services.googleShowLeaderboards();
                }
            });

        return button;
    }
    
    private Actor getPurchaseButton()
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Tutorial", UIManager.skin, "tutorial");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    SpaceTravels3.services.purchaseFullVersion();
                }
            });

        return button;
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
                    ScreenManager.addScreen(new DebugScreen());
                }
            });
        return debugButton;
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float deltaTime)
    {
        loadAsyncImages(AssMan.getAssMan());

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            Gdx.app.exit();
        }

        this.stage.getViewport().apply();

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

    }

    @Override
    public void dispose()
    {
        this.stage.dispose();
    }
}
