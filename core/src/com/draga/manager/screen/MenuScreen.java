package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.draga.BeepingClickListener;
import com.draga.Constants;
import com.draga.manager.GameManager;
import com.draga.manager.ScoreManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.UIManager;
import com.draga.manager.level.LevelManager;
import com.draga.manager.level.serialisableEntities.SerialisableLevel;

public class MenuScreen implements Screen
{
    private final TextButton              playButton;
    private       Stage                   stage;
    private       ButtonGroup<TextButton> buttonGroup;

    public MenuScreen()
    {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = UIManager.addDefaultTableToStage(stage);

        Label headerLabel = getHeaderLabel();
        playButton = getPlayButton();

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


        if (Constants.General.IS_DEBUGGING)
        {
            Actor debugButton = getDebugButton();

            table.row();
            table
                .add(debugButton)
                .bottom();
        }

        table.row();
        TextButton settingsTextButton = getSettingsTextButton();
        table
            .add(settingsTextButton)
            .bottom();

        table.row();
        table
            .add(playButton)
            .bottom();

        stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }
    
    public Label getHeaderLabel()
    {
        Label headerLabel = new Label("Space Travels 3", UIManager.skin);

        return headerLabel;
    }
    
    public TextButton getPlayButton()
    {
        TextButton playButton = new TextButton("Play", UIManager.skin);

        playButton.addListener(
            new BeepingClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    StartGameScreen();
                }
            });

        return playButton;
    }

    private ScrollPane getLevelList()
    {
        java.util.List<SerialisableLevel> levels = LevelManager.getSerialisableLevels();

        buttonGroup = new ButtonGroup<>();

        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        buttonGroup.setUncheckLast(true);

        Table table = UIManager.getDefaultTable();

        for (SerialisableLevel level : levels)
        {
            String buttonText = level.name + " (" + ScoreManager.getScore(level.name) + ")";
            TextButton textButton =
                new TextButton(buttonText, UIManager.skin);
            textButton.setName(level.id);
            textButton.addListener(new BeepingClickListener());
            buttonGroup.add(textButton);
            table.add(textButton);
            table.row();
        }

        ScrollPane scrollPane = new ScrollPane(table);

        return scrollPane;
    }

    public Actor getDebugButton()
    {
        TextButton debugButton = new TextButton("Debug", UIManager.skin);

        debugButton.addListener(
            new BeepingClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    GameManager.getGame().setScreen(new DebugMenuScreen());
                }
            });
        return debugButton;
    }

    private TextButton getSettingsTextButton()
    {
        TextButton settingsTextButton = new TextButton("Settings", UIManager.skin);

        settingsTextButton.addListener(
            new BeepingClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    GameManager.getGame().setScreen(new SettingsMenuScreen());
                }
            });

        return settingsTextButton;
    }

    private void StartGameScreen()
    {
        String levelId = buttonGroup.getChecked().getName();
        LoadingScreen loadingScreen = new LoadingScreen(levelId);
        GameManager.getGame().setScreen(loadingScreen);
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
