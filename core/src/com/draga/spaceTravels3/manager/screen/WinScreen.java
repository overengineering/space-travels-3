package com.draga.spaceTravels3.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.ui.BeepingTextButton;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.manager.GameManager;
import com.draga.spaceTravels3.manager.ScoreManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;

public class WinScreen implements Screen
{
    private final Stage  stage;
    private final Sound  sound;
    private final String levelId;

    public WinScreen(String levelId, int score)
    {
        this.levelId = levelId;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        int previousBestScore = ScoreManager.getScore(levelId);

        Label headerLabel = getHeaderLabel();
        TextButton retryButton = getRetryButton();
        Label scoreLabel = getScoreLabel(score);

        Table table = UIManager.addDefaultTableToStage(stage);
        table.setBackground(UIManager.skin.newDrawable(
            "background",
            Constants.Visual.SCREEN_FADE_COLOUR));
        table.addAction(Actions.sequence(
            Actions.fadeOut(0),
            Actions.fadeIn(Constants.Visual.SCREEN_FADE_DURATION, Interpolation.pow2In)));

        table.add(headerLabel);

        ScoreManager.updateScore(levelId, score);
        table.row();
        Label newBestScoreLabel = getBestScoreLabel(score, previousBestScore);
        table.add(newBestScoreLabel);

        table.row();
        table.add(scoreLabel);

        table.row();
        table.add(retryButton);

        SerialisableLevel nextLevel = LevelManager.getNextLevel(levelId);

        if (nextLevel != null)
        {
            String nextLevelId = nextLevel.id;
            TextButton nextTextButton = getNextButton(nextLevelId);
            table.row();
            table.add(nextTextButton);
        }

        TextButton mainMenuTextButton = getMainMenuTextButton();
        table.row();
        table.add(mainMenuTextButton);

        stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);

        sound = AssMan.getAssMan().get(AssMan.getAssList().winSound);
        sound.play(SettingsManager.getSettings().volume);
    }

    public Label getHeaderLabel()
    {
        Label.LabelStyle labelStyle = UIManager.skin.get(Label.LabelStyle.class);

        Label headerLabel = new Label("You won!", labelStyle);

        return headerLabel;
    }

    public TextButton getRetryButton()
    {
        TextButton retryButton = new BeepingTextButton("Try Again?", UIManager.skin);

        retryButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    Retry();
                }
            });

        return retryButton;
    }

    private Label getScoreLabel(int score)
    {
        Label.LabelStyle labelStyle = UIManager.skin.get(Label.LabelStyle.class);

        Label scoreLabel = new Label("Score: " + score, labelStyle);

        return scoreLabel;
    }

    private Label getBestScoreLabel(int score, int previousBestScore)
    {
        Label.LabelStyle labelStyle = UIManager.skin.get(Label.LabelStyle.class);

        String text = score > previousBestScore
            ? "New best score! It was: " + previousBestScore
            : "Best score: " + previousBestScore;

        Label newBestScoreLabel =
            new Label(text, labelStyle);

        return newBestScoreLabel;
    }

    public TextButton getNextButton(final String levelId)
    {
        TextButton retryButton = new BeepingTextButton("Next level", UIManager.skin);

        retryButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    GameManager.getGame().setScreen(new LoadingScreen(levelId));
                }
            });

        return retryButton;
    }

    private TextButton getMainMenuTextButton()
    {
        TextButton mainMenuTextButton = new BeepingTextButton("Main menu", UIManager.skin);
        mainMenuTextButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                GameManager.getGame().setScreen(new MenuScreen());
            }
        });

        return mainMenuTextButton;
    }

    private void Retry()
    {
        GameManager.getGame().setScreen(new LoadingScreen(levelId));
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            GameManager.getGame().setScreen(new MenuScreen());
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            Retry();
            return;
        }

        update(delta);
        draw(delta);
    }

    private void update(float delta)
    {
        stage.act(delta);
    }

    private void draw(float delta)
    {
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
        sound.stop();
        sound.dispose();
        stage.dispose();
    }
}
