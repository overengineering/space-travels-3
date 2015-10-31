package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.draga.Constants;
import com.draga.manager.AssMan;
import com.draga.manager.FontManager;
import com.draga.manager.ScreenManager;
import com.draga.manager.level.LevelManager;
import com.draga.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.manager.level.serialisableEntities.SerialisablePlanet;
import com.google.common.base.Joiner;

public class LoadingScreen implements Screen
{
    private static final String LOGGING_TAG = LoadingScreen.class.getSimpleName();
    private final Stage             stage;
    private final long              startTime;
    private       ProgressBar       progressBar;
    private       SerialisableLevel serialisableLevel;
    
    public LoadingScreen(String levelName)
    {
        startTime = System.nanoTime();
        
        serialisableLevel = LevelManager.getSerialisedLevelFromName(levelName);
        
        AssMan.DisposeAllAndClear();
        AssMan.getAssMan().load(
            serialisableLevel.serialisedBackground.texturePath, Texture.class);
        AssMan.getAssMan().load(
            AssMan.getAssList().ship, Texture.class);
        AssMan.getAssMan().load(
            AssMan.getAssList().thruster, TextureAtlas.class);
        for (SerialisablePlanet serialisablePlanet : serialisableLevel.serialisedPlanets)
        {
            AssMan.getAssMan().load(serialisablePlanet.texturePath, Texture.class);
        }
        AssMan.getAssMan().load(AssMan.getAssList().explosion, TextureAtlas.class);
        AssMan.getAssMan().load(AssMan.getAssList().starGold, Texture.class);
        AssMan.getAssMan().load(AssMan.getAssList().starGray, Texture.class);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Actor headerLabel = getHeaderLabel();

        float progressBarHeight = stage.getHeight() / 20f;
        progressBar = getProgressBar((int) progressBarHeight);


        Table table = new Table();
        stage.addActor(table);
        table.setFillParent(true);

        table.add(headerLabel);
        table.row();
        table
            .add(progressBar)
            .height(progressBarHeight)
            .width(stage.getWidth() * 0.75f);


        stage.setDebugAll(Constants.IS_DEBUGGING);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float deltaTime)
    {
        if (AssMan.getAssMan().update())
        {
            if (Constants.IS_DEBUGGING)
            {
                Gdx.app.debug(
                    LOGGING_TAG, Joiner.on(", ").join(AssMan.getAssMan().getAssetNames()));
            }

            GameScreen gameScreen = LevelManager.getLevelGameScreen(
                serialisableLevel, new SpriteBatch());
            long elapsedNanoTime = System.nanoTime() - startTime;
            Gdx.app.debug(LOGGING_TAG, "Loading time: " + elapsedNanoTime * Constants.NANO + "s");
            ScreenManager.setActiveScreen(gameScreen);
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
    public void dispose()
    {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height);
    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    public Label getHeaderLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontManager.getBigFont();

        Label headerLabel = new Label("Loading", labelStyle);

        return headerLabel;
    }

    private ProgressBar getProgressBar(int height)
    {
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(
            1, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle(
            skin.newDrawable("white", Color.DARK_GRAY), null);
        progressBarStyle.knobBefore = skin.newDrawable("white", Color.WHITE);

        ProgressBar progressBar = new ProgressBar(
            0,
            AssMan.getAssMan().getQueuedAssets() + AssMan.getAssMan().getLoadedAssets(),
            1,
            false,
            progressBarStyle);

        return progressBar;
    }
}
