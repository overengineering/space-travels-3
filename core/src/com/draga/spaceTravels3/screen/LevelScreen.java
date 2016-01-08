package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.ScoreManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.ui.BeepingTextButton;

public class LevelScreen implements Screen
{
    private final SerialisableLevel serialisableLevel;
    private       Stage             stage;

    public LevelScreen(SerialisableLevel serialisableLevel)
    {
        this.serialisableLevel = serialisableLevel;
    }

    @Override
    public void show()
    {
        this.stage = new Stage();
        Gdx.input.setInputProcessor(this.stage);

        Table table = UIManager.addDefaultTableToStage(this.stage);

        table
            .add(getHeader())
            .top()
            .center();
        table.row();

        Table centreTable = new Table();
        centreTable
            .add()
            .expand();
        centreTable.row();

        centreTable
            .add()
            .expand();
        centreTable.add(getHighScores());
        centreTable
            .add()
            .expand();
        centreTable.add(getPlayButtons());
        centreTable
            .add()
            .expand();
        centreTable.row();

        centreTable
            .add()
            .expand();

        table.add(centreTable);

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    @Override
    public void render(float delta)
    {
        this.stage.act(delta);
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        this.stage.getViewport().update(width, height);
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
        this.stage.dispose();
    }

    public Actor getHeader()
    {
        Label headerLabel = new Label(this.serialisableLevel.name, UIManager.skin);

        return headerLabel;
    }

    private Actor getHighScores()
    {
        Table mainTable = new Table();
        mainTable
            .add(new Label("High scores", UIManager.skin))
            .center();

        Table table = new Table();
        table.row();
        for (String difficulty : this.serialisableLevel.serialisedDifficulties.keySet())
        {
            table
                .add(new Label(difficulty, UIManager.skin))
                .right();
            int score = ScoreManager.getScore(this.serialisableLevel.id, difficulty);
            table
                .add(new Label(String.valueOf(score), UIManager.skin))
                .right();
            table.row();
        }

        mainTable.add(table);

        ScrollPane scrollPane = new ScrollPane(table, UIManager.skin);

        return scrollPane;
    }

    private Actor getPlayButtons()
    {
        Table table = new Table();
        for (final String difficulty : this.serialisableLevel.serialisedDifficulties.keySet())
        {
            BeepingTextButton beepingTextButton = new BeepingTextButton(difficulty, UIManager.skin);
            beepingTextButton.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    LoadingScreen loadingScreen = new LoadingScreen(
                        LevelScreen.this.serialisableLevel,
                        difficulty);
                    SpaceTravels3.getGame().setScreen(loadingScreen);
                    super.clicked(event, x, y);
                }
            });
            table.add(beepingTextButton);
            table.row();
        }

        ScrollPane scrollPane = new ScrollPane(table, UIManager.skin);

        return scrollPane;
    }
}
