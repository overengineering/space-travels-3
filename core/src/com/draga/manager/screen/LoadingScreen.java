package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.draga.Constants;
import com.draga.manager.AssMan;
import com.draga.manager.ScreenManager;
import com.draga.manager.level.LevelManager;
import com.draga.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.manager.level.serialisableEntities.SerialisablePlanet;
import com.google.common.base.Joiner;

public class LoadingScreen implements Screen
{
    private static final String LOGGING_TAG = LoadingScreen.class.getSimpleName();
    private final Stage                 stage;
    private final FreeTypeFontGenerator freeTypeFontGenerator;
    private final BitmapFont            pDark24Font;
    private final long                  startTime;
    private       ProgressBar           progressBar;
    private       SerialisableLevel     serialisableLevel;
    
    public LoadingScreen(String levelName)
    {
        startTime = System.nanoTime();
        
        serialisableLevel = LevelManager.getSerialisedLevelFromName(levelName);
        
        AssMan.DisposeAllAndClear();
        AssMan.getAssetManager().load(
            serialisableLevel.serialisedBackground.getTexturePath(), Texture.class);
        AssMan.getAssetManager().load(
            serialisableLevel.serialisedShip.getShipTexturePath(), Texture.class);
        AssMan.getAssetManager().load(
            serialisableLevel.serialisedShip.getThrusterTextureAtlasPath(), TextureAtlas.class);
        for (SerialisablePlanet serialisablePlanet : serialisableLevel.serialisedPlanets)
        {
            AssMan.getAssetManager().load(serialisablePlanet.getTexturePath(), Texture.class);
        }
        AssMan.getAssetManager().load("explosion/explosion.atlas", TextureAtlas.class);
        AssMan.getAssetManager().load("star/starGold64.png", Texture.class);
        AssMan.getAssetManager().load("star/starGray64.png", Texture.class);


        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/pdark.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 64;
        pDark24Font = freeTypeFontGenerator.generateFont(parameter);

        stage = new Stage();

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
        if (AssMan.getAssetManager().update())
        {
            if (Constants.IS_DEBUGGING)
            {
                Gdx.app.debug(
                    LOGGING_TAG, Joiner.on(", ").join(AssMan.getAssetManager().getAssetNames()));
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
            AssMan.getAssetManager().getQueuedAssets() + AssMan.getAssetManager()
                .getLoadedAssets());
        progressBar.setValue(AssMan.getAssetManager().getLoadedAssets());
    }

    @Override
    public void dispose()
    {
        freeTypeFontGenerator.dispose();
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
        labelStyle.font = pDark24Font;

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
            AssMan.getAssetManager().getQueuedAssets() + AssMan.getAssetManager().getLoadedAssets(),
            1,
            false,
            progressBarStyle);

        return progressBar;
    }
}
