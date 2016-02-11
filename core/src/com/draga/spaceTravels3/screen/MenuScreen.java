package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.event.PurchasedEvent;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.draga.spaceTravels3.manager.level.LevelPack;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.*;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

public class MenuScreen extends Screen
{
    private final Cell levelPackListCell;

    protected final float buttonHeight = Gdx.graphics.getHeight() * 0.12f;

    public MenuScreen()
    {
        super(true, true);
        Constants.General.EVENT_BUS.register(this);

        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Header label.
        table
            .add("Space Travels 3", "large")
            .top();

        // Level list.
        table.row();

        // Firstly create the cell so that if the event comes through it will generate the level
        // pack list again.
        this.levelPackListCell = table
            .add()
            .expand()
            .center();
        this.levelPackListCell.setActor(getLevelPackList());

        // Buttons.
        table.row();
        Table buttonsTable = UIManager.getDefaultTable();
        buttonsTable
            .defaults()
            .size(this.buttonHeight);

        // Tutorial button.
        buttonsTable
            .add(getSettingsButton(false));
        buttonsTable
            .add(getGuideButton());
        buttonsTable
            .add(getTutorialButton());
        buttonsTable
            .add(getCreditsButton());
        buttonsTable
            .add(getShareButton());
        buttonsTable
            .add(getRateButton());
        buttonsTable
            .add(getAchievementsButton());
        buttonsTable
            .add(getLeaderboardsButton());

        table.add(buttonsTable);

        if (Constants.General.IS_DEBUGGING)
        {
            this.stage.addActor(getDebugButton());
        }
    }

    private ScrollPane getLevelPackList()
    {
        java.util.List<LevelPack> levelPacks =
            LevelManager.getLevelPacks();

        final Table outerTable = UIManager.getDefaultTable();

        for (final LevelPack levelPack : levelPacks)
        {
            Table innerTable = UIManager.getDefaultTable();

            ArrayList<Image> levelImages = new ArrayList<>();

            // Overlaps the images of the destination planets leaving a 1/3 offset from each other.
            // Starts from right and the last planet because the last drawn will be on top.
            WidgetGroup imageGroup = new WidgetGroup();
            ArrayList<SerialisableLevel> serialisableLevels = levelPack.getSerialisableLevels();
            for (int i = serialisableLevels.size() - 1; i >= 0; i--)
            {
                SerialisableLevel serialisableLevel = serialisableLevels.get(i);
                Image image = loadTextureAsync(
                    serialisableLevel.serialisedDestinationPlanet.texturePath
                );
                image.sizeBy(Constants.Visual.LEVEL_ICON_SIZE);
                image.setX(i * (Constants.Visual.LEVEL_ICON_OVERLAP_DISTANCE));
                imageGroup.addActor(image);
                levelImages.add(image);
            }

            float imageGroupWidth = Constants.Visual.LEVEL_ICON_SIZE
                + (
                (levelPack.getSerialisableLevels().size() - 1)
                    * Constants.Visual.LEVEL_ICON_OVERLAP_DISTANCE);

            if (!levelPack.isFree()
                && !SpaceTravels3.services.hasPurchasedSku(levelPack.getGoogleSku()))
            {
                for (Image levelImage : levelImages)
                {
                    levelImage.setColor(Constants.Visual.FADE_TINT_COLOUR);
                }

                Image unlockImage = new Image(UIManager.skin, "unlockOverlay");
                unlockImage.setX(imageGroupWidth / 2f - unlockImage.getWidth() / 2f);
                unlockImage.setY(Constants.Visual.LEVEL_ICON_SIZE / 2f
                    - unlockImage.getHeight() / 2f);
                imageGroup.addActor(unlockImage);
            }

            innerTable
                .add(imageGroup)
                .height(Constants.Visual.LEVEL_ICON_SIZE)
                .width(imageGroupWidth);
            innerTable.row();

            innerTable.add(levelPack.getName());
            innerTable.row();

            innerTable.addListener(BeepingClickListener.BEEPING_CLICK_LISTENER);
            innerTable.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    if (levelPack.isFree()
                        || SpaceTravels3.services.hasPurchasedSku(levelPack.getGoogleSku()))
                    {
                        LevelPackScreen levelPackScreen = new LevelPackScreen(levelPack);
                        ScreenManager.addScreen(levelPackScreen);
                    }
                    else
                    {
                        SpaceTravels3.services.purchaseSku(levelPack.getGoogleSku());
                    }
                }
            });

            outerTable.add(innerTable);
        }

        ScrollPane scrollPane = new ScrollPane(outerTable, UIManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(false, true);

        return scrollPane;
    }
    
    private Actor getGuideButton()
    {
        BeepingImageButton
            button = new BeepingImageButton(UIManager.skin, "guide");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    ScreenManager.addScreen(new GuideScreen());
                }
            });

        return button;
    }

    private Actor getTutorialButton()
    {
        BeepingImageButton button =
            new BeepingImageButton(UIManager.skin, "tutorial");

        button.addListener(
            new ClickListener()
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
                }
            });

        return button;
    }

    private Actor getCreditsButton()
    {
        BeepingImageButton button =
            new BeepingImageButton(UIManager.skin, "credits");

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
        BeepingImageButton button =
            new BeepingImageButton(UIManager.skin, "share");
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SpaceTravels3.services.share();
            }
        });

        return button;
    }

    private Actor getRateButton()
    {
        BeepingImageButton
            button = new BeepingImageButton(UIManager.skin, "rate");

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
        BeepingImageButton
            button = new BeepingImageButton(UIManager.skin, "achievement");

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
        BeepingImageButton
            button = new BeepingImageButton(UIManager.skin, "leaderboard");

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

    public static String s(String s, int j)
    {
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            char c = chars[i];
            if (Character.isDigit(c))
            {
                int newValue = Integer.parseInt(String.valueOf(c)) + j;
                newValue += 10;
                char[] charArray = String.valueOf(newValue).toCharArray();
                chars[i] = charArray[charArray.length - 1];
            }
        }

        return new String(chars);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(new InputMultiplexer(this.stage, new InputAdapter()
        {
            @Override
            public boolean keyUp(int keycode)
            {
                switch (keycode)
                {
                    case Input.Keys.ESCAPE:
                    case Input.Keys.BACK:
                    {
                        Gdx.app.exit();
                        return true;
                    }
                }
                return false;
            }
        }));
    }

    @Override
    public void dispose()
    {
        Constants.General.EVENT_BUS.unregister(this);
        super.dispose();
    }

    @Subscribe
    public void purchased(PurchasedEvent purchasedEvent)
    {
        // If it doesn't exists it means it has not been created yet and it will taken care of.
        if (this.levelPackListCell != null)
        {
            this.levelPackListCell.clearActor();
            this.levelPackListCell.setActor(getLevelPackList());
        }
    }
}
