package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.draga.manager.ScreenManager;
import com.draga.manager.level.LevelManager;

public class MenuScreen implements Screen
{
    private final FreeTypeFontGenerator freeTypeFontGenerator;
    private Stage stage;
    private BitmapFont pDark24Font;

    public MenuScreen()
    {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/pdark.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 64;
        pDark24Font = freeTypeFontGenerator.generateFont(parameter);
        freeTypeFontGenerator.dispose();

        stage.addActor(getHeaderLabel());
        stage.addActor(getPlayButton());
    }

    @Override public void show()
    {

    }

    @Override public void render(float deltaTime)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            StartGameScreen();
        }

        stage.act(deltaTime);
        stage.draw();
    }

    @Override public void dispose()
    {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
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

    @Override public void hide()
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
                    StartGameScreen();
                    super.clicked(event, x, y);
                }
            });
        return playButton;
    }

    private void StartGameScreen()
    {
        ScreenManager.setActiveScreen(
            LevelManager.getLevelWorldFromFile(
                "level1.json", new SpriteBatch()));
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
