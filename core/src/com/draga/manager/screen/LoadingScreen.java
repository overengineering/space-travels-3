package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
    private       Label                 progressLabel;
    private       SerialisableLevel     serialisableLevel;

    public LoadingScreen(String levelJsonPath)
    {
        startTime = System.nanoTime();

        serialisableLevel = LevelManager.getSerialisedGameSceneFromFile(levelJsonPath);

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
        stage = new Stage();


        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/pdark.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 64;
        pDark24Font = freeTypeFontGenerator.generateFont(parameter);

        Actor headerLabel = getHeaderLabel();
        progressLabel = getProgressLabel();
        stage.addActor(headerLabel);
        stage.addActor(progressLabel);
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
                Gdx.app.debug(LOGGING_TAG, Joiner.on(", ").join(AssMan.getAssetManager().getAssetNames()));
            }

            GameScreen gameScene = LevelManager.getLevelGameScene(
                serialisableLevel, new SpriteBatch());
            long elapsedNanoTime = System.nanoTime() - startTime;
            Gdx.app.debug(LOGGING_TAG, "Loading time: " + elapsedNanoTime * Constants.NANO + "s");
            ScreenManager.setActiveScreen(gameScene);
        }
        setProgressLabelText();

        stage.act(deltaTime);
        stage.draw();
    }

    private void setProgressLabelText()
    {
        progressLabel.setText(
            AssMan.getAssetManager().getLoadedAssets() + " of " + (
                AssMan.getAssetManager().getQueuedAssets() + AssMan.getAssetManager()
                    .getLoadedAssets()));
        progressLabel.setX(stage.getWidth() - progressLabel.getWidth() / 2f);
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
        float height = pDark24Font.getLineHeight() * 2;
        headerLabel.sizeBy(stage.getWidth(), height);
        headerLabel.setPosition(
            stage.getWidth() - headerLabel.getWidth() / 2f, stage.getHeight() - height);

        return headerLabel;
    }

    public Label getProgressLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = pDark24Font;

        Label progressLabel = new Label("Loading", labelStyle);
        float height = pDark24Font.getLineHeight() * 2;
        progressLabel.sizeBy(stage.getWidth(), height);
        progressLabel.setHeight(stage.getHeight() / 10);
        progressLabel.setX((stage.getWidth() / 2) - (progressLabel.getWidth() / 2));

        return progressLabel;
    }
}
