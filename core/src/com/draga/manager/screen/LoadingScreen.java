package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.draga.Constants;
import com.draga.manager.GameManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.SkinManager;
import com.draga.manager.asset.AssMan;
import com.draga.manager.level.LevelManager;
import com.draga.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.manager.level.serialisableEntities.SerialisablePlanet;
import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

public class LoadingScreen implements Screen
{
    private static final String LOGGING_TAG = LoadingScreen.class.getSimpleName();
    private final String            levelName;
    private       Stage             stage;
    private       ProgressBar       progressBar;
    private       SerialisableLevel serialisableLevel;
    private       Stopwatch         stopwatch;
    
    public LoadingScreen(String levelName)
    {
        this.levelName = levelName;
    }

    @Override
    public void show()
    {
        stopwatch = Stopwatch.createStarted();

        this.serialisableLevel = LevelManager.getLevel(levelName);

        loadAssets();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Actor headerLabel = getHeaderLabel();

        progressBar = getProgressBar();


        Table table = new Table();
        stage.addActor(table);
        table.setFillParent(true);

        table.add(headerLabel);
        table.row();
        table
            .add(progressBar)
            .width(stage.getWidth() * 0.75f);

        stage.setDebugAll(SettingsManager.getSettings().debugDraw);
    }

    private void loadAssets()
    {
        // Loads sounds first 'cause of weird quirk of Android not loading them in time.
        AssMan.getAssMan().load(AssMan.getAssList().thrusterSound, Sound.class);
        AssMan.getAssMan().load(AssMan.getAssList().explosionSound, Sound.class);
        AssMan.getAssMan().load(AssMan.getAssList().pickupCollectSound, Sound.class);
        AssMan.getAssMan().load(AssMan.getAssList().loseSound, Sound.class);
        AssMan.getAssMan().load(AssMan.getAssList().winSound, Sound.class);

        AssMan.getAssMan().load(AssMan.getAssList().pickupGrey, Texture.class);
        AssMan.getAssMan().load(
            this.serialisableLevel.serialisedBackground.texturePath, Texture.class);
        AssMan.getAssMan().load(
            AssMan.getAssList().ship, Texture.class);
        AssMan.getAssMan().load(
            AssMan.getAssList().thruster, TextureAtlas.class);
        for (SerialisablePlanet serialisablePlanet : this.serialisableLevel.serialisedPlanets)
        {
            AssMan.getAssMan().load(serialisablePlanet.texturePath, Texture.class);
        }
        AssMan.getAssMan().load(AssMan.getAssList().explosion, TextureAtlas.class);
        AssMan.getAssMan().load(AssMan.getAssList().pickup, Texture.class);
    }

    public Label getHeaderLabel()
    {
        Label.LabelStyle labelStyle = SkinManager.BasicSkin.get(Label.LabelStyle.class);

        Label headerLabel = new Label("Loading", labelStyle);

        return headerLabel;
    }

    private ProgressBar getProgressBar()
    {
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle(
            SkinManager.BasicSkin.newDrawable("progressbar", Color.DARK_GRAY), null);
        progressBarStyle.knobBefore = SkinManager.BasicSkin.newDrawable("progressbar", Color.WHITE);


        ProgressBar progressBar = new ProgressBar(
            0,
            AssMan.getAssMan().getQueuedAssets() + AssMan.getAssMan().getLoadedAssets(),
            1,
            false,
            progressBarStyle);

        return progressBar;
    }

    @Override
    public void render(float deltaTime)
    {
        if (AssMan.getAssMan().update())
        {
            if (Constants.IS_DEBUGGING)
            {
                Gdx.app.debug(
                    LOGGING_TAG,
                    "Assets loaded: " + Joiner.on(", ").join(AssMan.getAssMan().getAssetNames()));
                Gdx.app.debug(
                    LOGGING_TAG,
                    String.format(
                        "Loading time: %fs",
                        stopwatch.elapsed(TimeUnit.NANOSECONDS) * Constants.NANO));
            }
            GameScreen gameScreen = LevelManager.getLevelGameScreen(
                serialisableLevel, new SpriteBatch());
            GameManager.getGame().setScreen(gameScreen);
        }
        updateProgressBar();

        stage.act(deltaTime);
        stage.draw();
    }

    private void updateProgressBar()
    {
        progressBar.setRange(
            0,
            AssMan.getAssMan().getQueuedAssets() + AssMan.getAssMan()
                .getLoadedAssets());
        progressBar.setValue(AssMan.getAssMan().getLoadedAssets());
    }

    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height);
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
        this.dispose();
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}
