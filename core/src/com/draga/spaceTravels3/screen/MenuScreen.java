package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.BeepingClickListener;
import com.draga.spaceTravels3.ui.BeepingTextButton;
import com.draga.spaceTravels3.ui.Screen;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuScreen extends Screen
{
    private Stage                             stage;
    private HashMap<SerialisableLevel, Image> asyncLevelIcons;

    public MenuScreen()
    {
        super(true, true);

        this.asyncLevelIcons = new HashMap<>();

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);

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
            Image image;
            if (AssMan.getMenuAssMan().isLoaded(serialisableLevel.iconPath))
            {
                Texture texture = AssMan.getMenuAssMan().get(serialisableLevel.iconPath);
                image = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
            }
            else
            {
                image = new Image();
                this.asyncLevelIcons.put(serialisableLevel, image);
            }

            innerTable
                .add(image)
                .size(this.stage.getWidth() / 10f);
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
    
    private TextButton getSettingsTextButton()
    {
        TextButton settingsTextButton = new BeepingTextButton("Settings", UIManager.skin);

        settingsTextButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    ScreenManager.addScreen(new SettingsScreen());
                }
            });

        return settingsTextButton;
    }
    
    private Actor getTutorialButton()
    {
        TextButton tutorialTextButton = new BeepingTextButton("Tutorial", UIManager.skin);

        tutorialTextButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    ScreenManager.addScreen(new TutorialScreen());
                }
            });

        return tutorialTextButton;
    }

    private Actor getCreditsButton()
    {
        TextButton settingsTextButton = new BeepingTextButton("Credits", UIManager.skin);

        settingsTextButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    ScreenManager.addScreen(new CreditsScreen());
                }
            });

        return settingsTextButton;
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
        // Check if we need to load level icons and if they are loaded show them.
        if (!this.asyncLevelIcons.isEmpty())
        {
            ArrayList<SerialisableLevel> serialisableLevels =
                new ArrayList<>(this.asyncLevelIcons.keySet());
            for (SerialisableLevel serialisableLevel : serialisableLevels)
            {
                if (AssMan.getMenuAssMan().update()
                    || AssMan.getMenuAssMan().isLoaded(serialisableLevel.iconPath))
                {
                    Texture texture = AssMan.getMenuAssMan().get(serialisableLevel.iconPath);
                    this.asyncLevelIcons.get(serialisableLevel)
                        .setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));

                    this.asyncLevelIcons.remove(serialisableLevel);
                }
            }
        }


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
