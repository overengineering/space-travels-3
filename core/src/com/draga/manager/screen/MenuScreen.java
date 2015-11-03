package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.Constants;
import com.draga.manager.asset.FontManager;
import com.draga.manager.GameManager;
import com.draga.manager.level.LevelManager;

public class MenuScreen implements Screen
{
    private Stage stage;
    private ButtonGroup<TextButton> buttonGroup;

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
        textButtonStyle.fontColor = Color.WHITE;

        FileHandle[] levelFiles = LevelManager.getLevels();

        buttonGroup = new ButtonGroup<>();

        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        buttonGroup.setUncheckLast(true);

        VerticalGroup verticalGroup = new VerticalGroup();

        for (FileHandle levelFileHandle : levelFiles)
        {
            TextButton textButton =
                new TextButton(levelFileHandle.nameWithoutExtension(), textButtonStyle);
            // Set path as name so that it can be passed straight to the loading screen later on.
            textButton.
                setName(levelFileHandle.path());
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
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
        this.dispose();
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
        GameManager.getGame().setScreen(new LoadingScreen(buttonGroup.getChecked().getName()));
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
