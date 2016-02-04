package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.InputType;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.level.Level;
import com.draga.spaceTravels3.level.LevelParameters;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisablePlanet;
import com.draga.spaceTravels3.physic.collisionCache.CollisionCache;
import com.draga.spaceTravels3.physic.collisionCache.CollisionCacheParameters;
import com.draga.spaceTravels3.ui.BeepingTextButton;
import com.draga.spaceTravels3.ui.Screen;
import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

public class LoadingScreen extends Screen
{
    private static final String LOGGING_TAG = LoadingScreen.class.getSimpleName();

    private final SerialisableLevel serialisableLevel;
    private final String            difficulty;

    private Stage       stage;
    private ProgressBar progressBar;

    private Stopwatch              stopwatch;
    private GameScreen             gameScreen;
    private AssetDescriptor<Level> levelAssetDescriptor;

    private boolean waitingForWarning = false;

    public LoadingScreen(String levelId, String difficulty)
    {
        this(LevelManager.getSerialisableLevel(levelId), difficulty);
    }

    public LoadingScreen(SerialisableLevel serialisableLevel, String difficulty)
    {
        super(true, true);

        this.difficulty = difficulty;
        this.serialisableLevel = serialisableLevel;
        this.stopwatch = Stopwatch.createStarted();

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);

        Table table = UIManager.addDefaultTableToStage(this.stage);

        table.add(new Label("Loading", UIManager.skin, "large", Color.WHITE));
        table.row();

        this.progressBar = getProgressBar();
        table
            .add(this.progressBar)
            .width(this.stage.getWidth() * 0.75f);

        if (!SettingsManager.getSettings().disableFaceUpWarning
            && SettingsManager.getSettings().inputType == InputType.ACCELEROMETER)
        {
            showFaceUpWarning();
            this.waitingForWarning = true;
        }

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    public void showFaceUpWarning()
    {
        final Dialog dialog = new Dialog("", UIManager.skin);

        TextButton dismissButton = new BeepingTextButton("Dismiss", UIManager.skin);

        ClickListener disableWarningListener = new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                SettingsManager.getSettings().disableFaceUpWarning = true;
                dialog.hide();
                LoadingScreen.this.waitingForWarning = false;
            }
        };

        dismissButton.addListener(disableWarningListener);

        Table table = UIManager.getDefaultTable();

        dialog.add(table);

        table
            .add("Face up!", "large")
            .center()
            .row();

        Image image = loadTextureAsync(AssMan.getAssList().iconFaceUp, AssMan.getAssMan());
        float iconSize = this.stage.getHeight() / 5f;
        table
            .add(image)
            .size(iconSize)
            .row();

        table
            .add("Tilting the device controls the spaceship\r\n"
                + "movements. Please turn your device face\r\n"
                + "up or change the input in the settings")
            .center()
            .row();

        table
            .add(dismissButton)
            .center();

        dialog.show(this.stage);
    }

    private ProgressBar getProgressBar()
    {
        ProgressBar progressBar = new ProgressBar(
            0,
            1,
            0.01f,
            false,
            UIManager.skin);
        progressBar.setAnimateDuration(0.05f);

        return progressBar;
    }

    @Override
    public void show()
    {
        loadAssets(this.serialisableLevel);

        Gdx.input.setInputProcessor(this.stage);
    }

    private void loadAssets(SerialisableLevel serialisableLevel)
    {
        AssetManager assMan = AssMan.getGameAssMan();
        LevelParameters levelParameters = new LevelParameters(serialisableLevel, this.difficulty);
        this.levelAssetDescriptor =
            new AssetDescriptor<>(Constants.Game.LEVEL_ASSET_FILENAME, Level.class, levelParameters);

        // Loads sounds first 'cause of weird quirk of Android not loading them in time.
        loadLevelAsset(assMan, levelParameters, AssMan.getAssList().thrusterSound, Sound.class);
        loadLevelAsset(assMan, levelParameters, AssMan.getAssList().explosionSound, Sound.class);
        loadLevelAsset(
            assMan,
            levelParameters,
            AssMan.getAssList().pickupCollectSound,
            Sound.class);
        loadLevelAsset(assMan, levelParameters, AssMan.getAssList().loseSound, Sound.class);
        loadLevelAsset(assMan, levelParameters, AssMan.getAssList().winSound, Sound.class);

        loadLevelAsset(
            assMan,
            levelParameters,
            AssMan.getAssList().pickupGreyTexture,
            Texture.class);
        loadLevelAsset(assMan, levelParameters, AssMan.getAssList().shipTexture, Texture.class);
        loadLevelAsset(
            assMan,
            levelParameters,
            AssMan.getAssList().thrusterTextureAtlas,
            TextureAtlas.class);
        for (SerialisablePlanet serialisablePlanet : serialisableLevel.serialisedPlanets)
        {
            loadLevelAsset(assMan, levelParameters, serialisablePlanet.texturePath, Texture.class);
        }
        loadLevelAsset(
            assMan,
            levelParameters,
            AssMan.getAssList().explosionTextureAtlas,
            TextureAtlas.class);
        loadLevelAsset(assMan, levelParameters, AssMan.getAssList().pickupTexture, Texture.class);

        assMan.load(this.levelAssetDescriptor);

        CollisionCacheParameters collisionCacheParameters = new CollisionCacheParameters();
        collisionCacheParameters.dependencies.add(this.levelAssetDescriptor);
        AssetDescriptor<CollisionCache> collisionCacheAssetDescriptor =
            new AssetDescriptor<>(Constants.Game.COLLISION_CACHE_ASSET_FILENAME,
                CollisionCache.class,
                collisionCacheParameters);
        assMan.load(collisionCacheAssetDescriptor);

        assMan.load(Constants.Visual.HUD.JOYSTICK_ASSET_DESCRIPTOR);

        assMan.update();
    }

    private void loadLevelAsset(
        AssetManager assMan,
        LevelParameters levelParameters,
        String assetPath,
        Class assetClass)
    {
        AssetDescriptor assetDescriptor =
            new AssetDescriptor(assetPath, assetClass);
        assMan.load(assetDescriptor);
        levelParameters.dependencies.add(assetDescriptor);
    }

    @Override
    public void render(float deltaTime)
    {
        loadAsyncImages(AssMan.getAssMan());

        // If GameScreen has been generated on the last step add it to the stack and remove itself.
        if (this.gameScreen != null)
        {
            ScreenManager.addScreen(this.gameScreen);
            ScreenManager.removeScreen(this);
            return;
        }

        // If loaded creates the game screen but doesn't add it to the stack yet to skip this frame
        // which will be long (ref. #77).
        if (AssMan.getGameAssMan().update()
            && !this.waitingForWarning)
        {
            if (Constants.General.IS_DEBUGGING)
            {
                Gdx.app.debug(
                    LOGGING_TAG,
                    "Assets loaded: " + Joiner.on(", ")
                        .join(AssMan.getGameAssMan().getAssetNames()));
                Gdx.app.debug(
                    LOGGING_TAG,
                    String.format(
                        "Loading time: %fs",
                        this.stopwatch.elapsed(TimeUnit.NANOSECONDS) * MathUtils.nanoToSec));
            }
            Level level = AssMan.getGameAssMan().get(this.levelAssetDescriptor);
            this.gameScreen = new GameScreen(level);
            return;
        }

        updateProgressBar();

        this.stage.getViewport().apply();

        this.stage.act(deltaTime);
        this.stage.draw();
    }

    private void updateProgressBar()
    {
        this.progressBar.setValue(AssMan.getGameAssMan().getProgress());
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
