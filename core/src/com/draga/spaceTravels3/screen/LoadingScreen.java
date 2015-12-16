package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

public class LoadingScreen implements Screen
{
    private static final String LOGGING_TAG = LoadingScreen.class.getSimpleName();

    private final String            levelId;
    private       Stage             stage;
    private       ProgressBar       progressBar;
    private       SerialisableLevel serialisableLevel;
    private       Stopwatch         stopwatch;
    private Level level;
    
    public LoadingScreen(String levelId)
    {
        this.levelId = levelId;
    }

    @Override
    public void show()
    {
        stopwatch = Stopwatch.createStarted();

        this.serialisableLevel = LevelManager.getSerialisableLevel(levelId);

        loadAssets(this.serialisableLevel);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Actor headerLabel = getHeaderLabel();

        progressBar = getProgressBar();

        Table table = UIManager.addDefaultTableToStage(stage);

        table.add(headerLabel);
        table.row();
        table
            .add(progressBar)
            .width(stage.getWidth() * 0.75f);

        stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private void loadAssets(SerialisableLevel serialisableLevel)
    {
        // Loads sounds first 'cause of weird quirk of Android not loading them in time.
        AssMan.getAssMan().load(AssMan.getAssList().thrusterSound, Sound.class);
        AssMan.getAssMan().load(AssMan.getAssList().explosionSound, Sound.class);
        AssMan.getAssMan().load(AssMan.getAssList().pickupCollectSound, Sound.class);
        AssMan.getAssMan().load(AssMan.getAssList().loseSound, Sound.class);
        AssMan.getAssMan().load(AssMan.getAssList().winSound, Sound.class);

        AssMan.getAssMan().load(AssMan.getAssList().pickupGreyTexture, Texture.class);
        AssMan.getAssMan().load(serialisableLevel.serialisedBackground.texturePath, Texture.class);
        AssMan.getAssMan().load(
            AssMan.getAssList().shipTexture, Texture.class);
        AssMan.getAssMan().load(
            AssMan.getAssList().thrusterTextureAtlas, TextureAtlas.class);
        for (SerialisablePlanet serialisablePlanet : serialisableLevel.serialisedPlanets)
        {
            AssMan.getAssMan().load(serialisablePlanet.texturePath, Texture.class);
        }
        AssMan.getAssMan().load(AssMan.getAssList().explosionTextureAtlas, TextureAtlas.class);
        AssMan.getAssMan().load(AssMan.getAssList().pickupTexture, Texture.class);
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
            AssMan.getAssMan().getQueuedAssets() + AssMan.getAssMan().getLoadedAssets(),
            1,
            false,
            UIManager.skin);

        return progressBar;
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
                        stopwatch.elapsed(TimeUnit.NANOSECONDS) * Constants.General.NANO));
            }
            level = LevelManager.getLevel(serialisableLevel);
            GameScreen gameScreen = new GameScreen(level);
            SpaceTravels3.getGame().setScreen(gameScreen);
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
