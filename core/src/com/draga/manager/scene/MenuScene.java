package com.draga.manager.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.draga.manager.SceneManager;
import com.draga.manager.level.LevelManager;

/**
 * Created by Administrator on 25/09/2015.
 */
public class MenuScene extends Scene
{
    private final FreeTypeFontGenerator freeTypeFontGenerator;
    private Stage stage;
    private BitmapFont pDark24Font;

    public MenuScene()
    {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/pdark.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 64;
        pDark24Font = freeTypeFontGenerator.generateFont(parameter);

        stage.addActor(getHeaderLabel());
        stage.addActor(getPlayButton());
    }

    @Override public void render(float deltaTime)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            Gdx.app.exit();
        }

        stage.act(deltaTime);
        stage.draw();
    }

    @Override public void dispose()
    {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
        freeTypeFontGenerator.dispose();
    }

    @Override public void pause()
    {

    }

    @Override public void resize(int width, int height)
    {
        stage.getViewport().update(width, height);
    }

    @Override public void resume()
    {

    }

    public Actor getPlayButton()
    {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = pDark24Font;

        Button playButton = new TextButton("Play", textButtonStyle);

        playButton.setX((stage.getWidth() / 2) - (playButton.getWidth() / 2));
        playButton.addListener(
            new ClickListener()
            {
                @Override public void clicked(InputEvent event, float x, float y)
                {
                    SceneManager.setActiveScene(
                        LevelManager.getLevelWorldFromFile(
                            "level1.json", new SpriteBatch()));
                    super.clicked(event, x, y);
                }
            });
        return playButton;
    }

    public Actor getHeaderLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = pDark24Font;

        Label headerLabel = new Label("Space Travels 3", labelStyle);
        float height = pDark24Font.getLineHeight() * 2;
        headerLabel.sizeBy(stage.getWidth(), height);
        headerLabel.setPosition(
            stage.getWidth() - headerLabel.getWidth() / 2f, stage.getHeight() - height);

        return headerLabel;
    }
}
