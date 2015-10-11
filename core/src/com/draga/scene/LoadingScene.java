package com.draga.scene;

import com.badlogic.gdx.Gdx;
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
import com.draga.manager.SceneManager;
import com.draga.manager.level.LevelManager;
import com.draga.manager.level.serialisableEntities.SerialisableGameScene;
import com.draga.manager.level.serialisableEntities.SerialisablePlanet;

public class LoadingScene extends Scene
{
    private static final String LOGGING_TAG = LoadingScene.class.getSimpleName();
    private final Stage                 stage;
    private final FreeTypeFontGenerator freeTypeFontGenerator;
    private final BitmapFont            pDark24Font;
    private final long                  startTime;
    private       Label                 progressLabel;
    private       SerialisableGameScene serialisableGameScene;

    public LoadingScene(String levelJsonPath)
    {
        startTime = System.nanoTime();

        serialisableGameScene = LevelManager.getSerialisedGameSceneFromFile(levelJsonPath);

        AssMan.getAssetManager().load(
            serialisableGameScene.serialisedBackground.getTexturePath(), Texture.class);
        AssMan.getAssetManager().load(
            serialisableGameScene.serialisedShip.getShipTexturePath(), Texture.class);
        AssMan.getAssetManager().load(
            serialisableGameScene.serialisedShip.getThrusterTextureAtlasPath(), TextureAtlas.class);
        for (SerialisablePlanet serialisablePlanet : serialisableGameScene.serialisedPlanets)
        {
            AssMan.getAssetManager().load(serialisablePlanet.getTexturePath(), Texture.class);
        }
        AssMan.getAssetManager().load("explosion/explosion.atlas", TextureAtlas.class);
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
    public void render(float deltaTime)
    {
        if (AssMan.getAssetManager().update())
        {
            GameScene gameScene = LevelManager.getLevelGameScene(
                serialisableGameScene, new SpriteBatch());
            long elapsedNanoTime = System.nanoTime() - startTime;
            Gdx.app.debug(LOGGING_TAG, "Loading time: " + elapsedNanoTime * Constants.NANO + "s");
            SceneManager.setActiveScene(gameScene);
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
