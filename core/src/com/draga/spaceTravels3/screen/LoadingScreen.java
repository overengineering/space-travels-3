package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.draga.spaceTravels3.Constants;
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
import com.draga.spaceTravels3.physic.gravityCache.GravityCache;
import com.draga.spaceTravels3.physic.gravityCache.GravityCacheParameters;
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

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private ProgressBar getProgressBar()
    {
        ProgressBar progressBar = new ProgressBar(
            0,
            1,
            0.01f,
            false,
            UIManager.skin);

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
        this.levelAssetDescriptor = new AssetDescriptor("level", Level.class, levelParameters);



        // Loads sounds first 'cause of weird quirk of Android not loading them in time.
        AssetDescriptor<Sound> thrusterSoundAssetDescriptor =
            new AssetDescriptor<>(AssMan.getAssList().thrusterSound, Sound.class);
        assMan.load(thrusterSoundAssetDescriptor);
        levelParameters.dependencies.add(thrusterSoundAssetDescriptor);

        AssetDescriptor<Sound> explosionSoundAssetDescriptor =
            new AssetDescriptor<>(AssMan.getAssList().explosionSound, Sound.class);
        assMan.load(explosionSoundAssetDescriptor);
        levelParameters.dependencies.add(explosionSoundAssetDescriptor);

        AssetDescriptor<Sound> pickupCollectSoundAssetDescriptor =
            new AssetDescriptor<>(AssMan.getAssList().pickupCollectSound, Sound.class);
        assMan.load(pickupCollectSoundAssetDescriptor);
        levelParameters.dependencies.add(pickupCollectSoundAssetDescriptor);

        AssetDescriptor<Sound> loseSoundAssetDescriptor =
            new AssetDescriptor<>(AssMan.getAssList().loseSound, Sound.class);
        assMan.load(loseSoundAssetDescriptor);
        levelParameters.dependencies.add(loseSoundAssetDescriptor);

        AssetDescriptor<Sound> winSoundAssetDescriptor =
            new AssetDescriptor<>(AssMan.getAssList().winSound, Sound.class);
        assMan.load(winSoundAssetDescriptor);
        levelParameters.dependencies.add(winSoundAssetDescriptor);


        AssetDescriptor<Texture> pickupGreyTextureAssetDescriptor =
            new AssetDescriptor<>(AssMan.getAssList().pickupGreyTexture, Texture.class);
        assMan.load(pickupGreyTextureAssetDescriptor);
        levelParameters.dependencies.add(pickupGreyTextureAssetDescriptor);

        AssetDescriptor<Texture> shipTextureAssetDescriptor = new AssetDescriptor<>(
            AssMan.getAssList().shipTexture, Texture.class);
        assMan.load(shipTextureAssetDescriptor);
        levelParameters.dependencies.add(shipTextureAssetDescriptor);

        AssetDescriptor<TextureAtlas> thrusterTextureAtlasAssetDescriptor = new AssetDescriptor<>(
            AssMan.getAssList().thrusterTextureAtlas, TextureAtlas.class);
        assMan.load(thrusterTextureAtlasAssetDescriptor);
        levelParameters.dependencies.add(thrusterTextureAtlasAssetDescriptor);

        for (SerialisablePlanet serialisablePlanet : serialisableLevel.serialisedPlanets)
        {
            AssetDescriptor<Texture> planetTextureAssetDescriptor =
                new AssetDescriptor<>(serialisablePlanet.texturePath, Texture.class);
            assMan.load(planetTextureAssetDescriptor);
            levelParameters.dependencies.add(planetTextureAssetDescriptor);
        }
        AssetDescriptor<TextureAtlas> explosionTextureAtlasAssetDescriptor =
            new AssetDescriptor<>(AssMan.getAssList().explosionTextureAtlas, TextureAtlas.class);
        assMan.load(explosionTextureAtlasAssetDescriptor);
        levelParameters.dependencies.add(explosionTextureAtlasAssetDescriptor);

        AssetDescriptor<Texture> pickupTexture =
            new AssetDescriptor<>(AssMan.getAssList().pickupTexture, Texture.class);
        assMan.load(pickupTexture);
        levelParameters.dependencies.add(pickupTexture);


        assMan.load(this.levelAssetDescriptor);

        GravityCacheParameters gravityCacheParameters = new GravityCacheParameters();
        gravityCacheParameters.dependencies.add(levelAssetDescriptor);
        AssetDescriptor<GravityCache> gravityCacheAssetDescriptor =
            new AssetDescriptor<>("gravityCache", GravityCache.class,
                gravityCacheParameters);
        assMan.load(gravityCacheAssetDescriptor);



        assMan.load(Constants.Visual.HUD.JOYSTICK_ASSET_DESCRIPTOR);

        assMan.update();
    }

    @Override
    public void render(float deltaTime)
    {
        // If GameScreen has been generated on the last step add it to the stack and remove itself.
        if (this.gameScreen != null)
        {
            ScreenManager.addScreen(this.gameScreen);
            ScreenManager.removeScreen(this);
            return;
        }

        // If loaded creates the game screen but doesn't add it to the stack yet to skip this frame
        // which will be long ref. #77
        if (AssMan.getGameAssMan().update())
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
