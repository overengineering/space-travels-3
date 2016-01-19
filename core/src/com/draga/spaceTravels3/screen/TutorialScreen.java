package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.draga.spaceTravels3.Level;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.ui.Screen;

public class TutorialScreen extends Screen
{
    private final Level level;
    private       Stage stage;

    public TutorialScreen(Level level)
    {
        super(true, true);
        this.level = level;

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);

        this.level.startTutorial();

        startTutorial();
    }

    private void startTutorial()
    {
        Dialog dialog = new Dialog("Movement", UIManager.skin);
        dialog.add("test dfggagasdfasdfasdf").center();

        this.stage.addActor(dialog);
        dialog.setFillParent(true);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            ScreenManager.removeScreen(TutorialScreen.this);
        }

        this.stage.getViewport().apply();
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

    }

    @Override
    public void dispose()
    {
    }
}
