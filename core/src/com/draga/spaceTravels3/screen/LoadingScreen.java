package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.Level;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisablePlanet;
import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

public class LoadingScreen extends com.draga.spaceTravels3.ui.Screen
{
    private static final String LOGGING_TAG = LoadingScreen.class.getSimpleName();
    private final SerialisableLevel serialisableLevel;
    private final String            difficulty;

    private Stage       stage;
    private ProgressBar progressBar;

    private Stopwatch stopwatch;

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

        loadAssets(this.serialisableLevel);

        this.stage = new Stage();

        Actor headerLabel = getHeaderLabel();

        Table table = UIManager.addDefaultTableToStage(this.stage);

        table.add(headerLabel);
        table.row();

        this.progressBar = getProgressBar();
        table
            .add(this.progressBar)
            .width(this.stage.getWidth() * 0.75f);

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private void loadAssets(SerialisableLevel serialisableLevel)
    {
        AssetManager assMan = AssMan.getAssMan();

        // Loads sounds first 'cause of weird quirk of Android not loading them in time.
        assMan.load(AssMan.getAssList().thrusterSound, Sound.class);
        assMan.load(AssMan.getAssList().explosionSound, Sound.class);
        assMan.load(AssMan.getAssList().pickupCollectSound, Sound.class);
        assMan.load(AssMan.getAssList().loseSound, Sound.class);
        assMan.load(AssMan.getAssList().winSound, Sound.class);

        assMan.load(AssMan.getAssList().pickupGreyTexture, Texture.class);
        assMan.load(
            AssMan.getAssList().shipTexture, Texture.class);
        assMan.load(
            AssMan.getAssList().thrusterTextureAtlas, TextureAtlas.class);
        for (SerialisablePlanet serialisablePlanet : serialisableLevel.serialisedPlanets)
        {
            assMan.load(serialisablePlanet.texturePath, Texture.class);
        }
        assMan.load(AssMan.getAssList().explosionTextureAtlas, TextureAtlas.class);
        assMan.load(AssMan.getAssList().pickupTexture, Texture.class);

        assMan.load(Constants.Visual.Background.BACKGROUND_ASSET_DESCRIPTOR);

        assMan.load(Constants.Visual.HUD.JOYSTICK_ASSET_DESCRIPTOR);

        assMan.update();
    }

    public Label getHeaderLabel()
    {
        Label.LabelStyle labelStyle = UIManager.skin.get(Label.LabelStyle.class);

        Label headerLabel = new Label("Loading", labelStyle);

        return headerLabel;
    }

    private ProgressBar getProgressBar()
    {
        ProgressBar progressBar = new ProgressBar(
            0,
            getTotalAssetsCount(),
            1,
            false,
            UIManager.skin);

        return progressBar;
    }

    private float getTotalAssetsCount()
    {
        return AssMan.getAssMan().getQueuedAssets()
            + AssMan.getAssMan().getLoadedAssets();
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float deltaTime)
    {
        if (AssMan.getAssMan().update())
        {
            if (Constants.General.IS_DEBUGGING)
            {
                Gdx.app.debug(
                    LOGGING_TAG,
                    "Assets loaded: " + Joiner.on(", ").join(AssMan.getAssMan().getAssetNames()));
                Gdx.app.debug(
                    LOGGING_TAG,
                    String.format(
                        "Loading time: %fs",
                        this.stopwatch.elapsed(TimeUnit.NANOSECONDS) * MathUtils.nanoToSec));
            }
            Level level =
                LevelManager.getLevel(this.serialisableLevel, this.difficulty);
            GameScreen gameScreen = new GameScreen(level);
            ScreenManager.addScreen(gameScreen);
            ScreenManager.removeScreen(this);
            return;
        }
        updateProgressBar();

        this.stage.getBatch().begin();
        SpaceTravels3.background.draw(this.stage.getCamera(), this.stage.getBatch());
        this.stage.getBatch().end();

        this.stage.act(deltaTime);
        this.stage.draw();
    }

    private void updateProgressBar()
    {
        this.progressBar.setRange(0, getTotalAssetsCount());
        this.progressBar.setValue(getLoadedAssetsCount());
    }

    private int getLoadedAssetsCount()
    {
        return AssMan.getAssMan().getLoadedAssets();
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
