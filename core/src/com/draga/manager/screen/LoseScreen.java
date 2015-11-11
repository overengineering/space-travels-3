package com.draga.manager.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.manager.DebugManager;
import com.draga.manager.GameManager;
import com.draga.manager.SkinManager;

public class LoseScreen implements Screen
{
    private final Stage      stage;
    private final Color fadeToColour     = new Color(0, 0, 0, 0.7f);
    private final ShapeRenderer shapeRenderer;
    private       String        levelName;

    public LoseScreen(String levelName)
    {
        this.levelName = levelName;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Actor retryButton = getRetryButton();


        Table table = new Table();
        table.setBackground(SkinManager.BasicSkin.newDrawable("background", fadeToColour));
        table.addAction(Actions.sequence(
            Actions.fadeOut(0),
            Actions.fadeIn(3, Interpolation.pow2In)));


        table.add(retryButton).size(retryButton.getWidth() * 2, retryButton.getHeight() * 3);
        table.setFillParent(true);
        stage.addActor(table);

        stage.setDebugAll(DebugManager.debugDraw);
        shapeRenderer = new ShapeRenderer();
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

    private void draw(float delta)
    {
        stage.draw();
    }

    private void update(float delta)
    {
        stage.act(delta);
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
        shapeRenderer.dispose();
        stage.dispose();
    }

    public Actor getRetryButton()
    {
        TextButton.TextButtonStyle buttonStyle =
            SkinManager.BasicSkin.get(TextButton.TextButtonStyle.class);

        TextButton retryButton = new TextButton("Try Again?", buttonStyle);

        retryButton.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    Retry();
                    super.clicked(event, x, y);
                }
            });

        return retryButton;
    }

    private void Retry()
    {
        GameManager.getGame().setScreen(new LoadingScreen(levelName));
    }
}
