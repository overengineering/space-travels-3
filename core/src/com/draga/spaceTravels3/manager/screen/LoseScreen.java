package com.draga.spaceTravels3.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.ui.BeepingTextButton;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.manager.GameManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;

public class LoseScreen implements Screen
{
    private final Stage  stage;
    private final Sound  sound;
    private       String levelId;

    public LoseScreen(String levelId)
    {
        this.levelId = levelId;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        TextButton retryButton = getRetryTextButton();

        Table table = UIManager.addDefaultTableToStage(stage);
        table.setBackground(UIManager.skin.newDrawable(
            "background",
            Constants.Visual.SCREEN_FADE_COLOUR));
        table.addAction(Actions.sequence(
            Actions.fadeOut(0),
            Actions.fadeIn(Constants.Visual.SCREEN_FADE_DURATION, Interpolation.pow2In)));

        table
            .add(retryButton);

        TextButton mainMenuTextButton = getMainMenuTextButton();
        table.row();
        table.add(mainMenuTextButton);

        stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);

        sound = AssMan.getAssMan().get(AssMan.getAssList().loseSound);
        sound.play(SettingsManager.getSettings().volume);
    }

    public TextButton getRetryTextButton()
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
