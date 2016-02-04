package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.event.PurchasedEvent;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.draga.spaceTravels3.manager.level.LevelPack;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.BeepingClickListener;
import com.draga.spaceTravels3.ui.BeepingImageTextButton;
import com.draga.spaceTravels3.ui.BeepingTextButton;
import com.draga.spaceTravels3.ui.Screen;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

public class MenuScreen extends Screen
{
    private final Cell  levelPackListCell;
    private       Stage stage;

    public MenuScreen()
    {
        super(true, true);
        Constants.General.EVENT_BUS.register(this);

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);

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

        buttonsTable.add(getSettingsTextButton(false));
        buttonsTable.add(getTutorialButton());
        buttonsTable.add(getCreditsButton());
        buttonsTable.add(getShareButton());
        buttonsTable.add(getRateButton());
        buttonsTable.add(getAchievementsButton());
        buttonsTable.add(getLeaderboardsButton());

        table.add(buttonsTable);

        if (Constants.General.IS_DEBUGGING)
        {
            this.stage.addActor(getDebugButton());
        }

        if (!SettingsManager.getSettings().disableFaceUpWarning)
        {
            addFaceUpWarning(this.stage);
        }

//        this.stage.setDebugAll(true);
                this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
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
                    serialisableLevel.serialisedDestinationPlanet.texturePath,
                    AssMan.getAssMan());
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

    private Button getSettingsTextButton(boolean useText)
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton(useText ? "Settings" : "", UIManager.skin, "settings");

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
            new BeepingImageTextButton("", UIManager.skin, "tutorial");

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
            new BeepingImageTextButton("", UIManager.skin, "credits");

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
            new BeepingImageTextButton("", UIManager.skin, "share");
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
        BeepingImageTextButton
            button = new BeepingImageTextButton("", UIManager.skin, "rate");

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
            button = new BeepingImageTextButton("", UIManager.skin, "achievement");

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
            button = new BeepingImageTextButton("", UIManager.skin, "leaderboard");

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

    private void addFaceUpWarning(Stage stage)
    {
        Dialog dialog = new Dialog("", UIManager.skin);

        TextButton dismissButton = new BeepingTextButton("Dismiss", UIManager.skin);
        Button settingsButton = getSettingsTextButton(true);

        ClickListener disableWarningListener = new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SettingsManager.getSettings().disableFaceUpWarning = true;
            }
        };

        dismissButton.addListener(disableWarningListener);
        settingsButton.addListener(disableWarningListener);

        dialog.getContentTable()
            .add("Face up!", "large")
            .center()
            .row();
        dialog.getContentTable()
            .add("Tilting the device moves the spaceship when playing.\r\n"
                + "Make sure to start your games orienting you device\n"
                + "face up or change the input in the settings")
            .center();
        dialog
            .button(settingsButton)
            .button(dismissButton)
            .center();

        dialog.show(stage);
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
        Constants.General.EVENT_BUS.unregister(this);
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
