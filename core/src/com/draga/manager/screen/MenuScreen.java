package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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
        ScrollPane levelsScrollPane = getLevelList();
        table.add(levelsScrollPane);

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

    private ScrollPane getLevelList()
    {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = FontManager.getBigFont();
        textButtonStyle.checkedFontColor = Color.GREEN;

        TextButton[] textButtons = new TextButton[] {
            new TextButton("test1", textButtonStyle),
            new TextButton("test2", textButtonStyle),
            new TextButton("test3", textButtonStyle),
            new TextButton("test4", textButtonStyle),
            new TextButton("test5", textButtonStyle),
            new TextButton("test6", textButtonStyle),
            new TextButton("test7", textButtonStyle),
            new TextButton("test8", textButtonStyle),
            new TextButton("test9", textButtonStyle),
            new TextButton("test10", textButtonStyle),
            new TextButton("test11", textButtonStyle),
            new TextButton("test12", textButtonStyle),
            new TextButton("test13", textButtonStyle),
            new TextButton("test14", textButtonStyle),
            new TextButton("test15", textButtonStyle),
            new TextButton("test16", textButtonStyle),
            new TextButton("test17", textButtonStyle),
            new TextButton("test18", textButtonStyle),
            new TextButton("test19", textButtonStyle),
            new TextButton("test20", textButtonStyle),
        };
        ButtonGroup<TextButton> buttonGroup = new ButtonGroup<>();

        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        buttonGroup.setUncheckLast(true);

        VerticalGroup verticalGroup = new VerticalGroup();

        for (TextButton textButton:textButtons){
            buttonGroup.add(textButton);
            verticalGroup.addActor(textButton);
        }

        ScrollPane scrollPane = new ScrollPane(verticalGroup);
        
        return scrollPane;
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

    public TextButton getPlayButton()
    {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = FontManager.getBigFont();

        TextButton playButton = new TextButton("Play", textButtonStyle);

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
        ScreenManager.setActiveScreen(new LoadingScreen("level/level1.json"));
    }

    public Actor getHeaderLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        BitmapFont bigFont = FontManager.getBigFont();
        labelStyle.font = bigFont;

        Label headerLabel = new Label("Space Travels 3", labelStyle);

        return headerLabel;
    }
}
