package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.Constants;
import com.draga.manager.FontManager;
import com.draga.manager.ScreenManager;

public class MenuScreen implements Screen
{
    private Stage stage;

    public MenuScreen()
    {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.pad(((stage.getHeight() + stage.getWidth()) / 2f) / 50f);
        stage.addActor(table);

        Actor headerLabel = getHeaderLabel();
        Actor playButton = getPlayButton();


        table
            .add(headerLabel)
            .top();

        // Add a row with an expanded cell to fill the gap.
        table.row();
        table
            .add()
            .expand();

        table.row();
        table
            .add(playButton)
            .bottom();

        stage.setDebugAll(Constants.IS_DEBUGGING);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float deltaTime)
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

    public Actor getPlayButton()
    {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = FontManager.getBigFont();

        Button playButton = new TextButton("Play", textButtonStyle);

        playButton.setX((stage.getWidth() / 2) - (playButton.getWidth() / 2));
        playButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    StartGameScreen();
                    super.clicked(event, x, y);
                }
            });
        return playButton;
    }

    private void StartGameScreen()
    {
        ScreenManager.setActiveScreen(new LoadingScreen("level1.json"));
    }

    public Actor getHeaderLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        BitmapFont bigFont = FontManager.getBigFont();
        labelStyle.font = bigFont;

        Label headerLabel = new Label("Space Travels 3", labelStyle);
        float height = bigFont.getLineHeight() * 2;
        headerLabel.sizeBy(stage.getWidth(), height);
        headerLabel.setPosition(
            stage.getWidth() - headerLabel.getWidth() / 2f, stage.getHeight() - height);

        return headerLabel;
    }
}
