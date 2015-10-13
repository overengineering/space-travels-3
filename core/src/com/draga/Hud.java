package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.google.common.eventbus.Subscribe;

public class Hud implements Screen
{
    private static final float WORLD_HEIGHT                  = 1000f;
    private static final float WORLD_WIDTH                   = 1000f;
    private static final float FUEL_PROGRESS_BAR_HEIGHT      = WORLD_HEIGHT / 20f;
    private static final float FUEL_PROGRESS_BAR_WIDTH       = WORLD_WIDTH / 3f;
    private static final float FUEL_PROGRESS_BAR_LEFT_MARGIN = FUEL_PROGRESS_BAR_HEIGHT;
    private static final float FUEL_PROGRESS_BAR_TOP_MARGIN  = FUEL_PROGRESS_BAR_LEFT_MARGIN;
    private       Stage                 stage;
    private       ProgressBar           fuelProgressBar;

    public Hud()
    {
        Constants.EVENT_BUS.register(this);

        StretchViewport viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT);
        stage = new Stage(viewport);

        fuelProgressBar = getFuelProgressBar();
        updateFuelProgressBar(Constants.MAX_FUEL);
        stage.addActor(fuelProgressBar);
        stage.setDebugAll(Constants.IS_DEBUGGING);
    }

    public ProgressBar getFuelProgressBar()
    {
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(
            1, (int) Math.ceil(FUEL_PROGRESS_BAR_HEIGHT), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        ProgressBar.ProgressBarStyle fuelProgressBarStyle = new ProgressBar.ProgressBarStyle(
            skin.newDrawable("white", Color.DARK_GRAY), null);
        fuelProgressBarStyle.knobBefore = skin.newDrawable("white", Color.WHITE);

        ProgressBar fuelProgressBar = new ProgressBar(
            0, Constants.MAX_FUEL, Constants.MAX_FUEL / 1000f, false, fuelProgressBarStyle);
        fuelProgressBar.setSize(FUEL_PROGRESS_BAR_WIDTH, FUEL_PROGRESS_BAR_HEIGHT);
        fuelProgressBar.setPosition(
            FUEL_PROGRESS_BAR_LEFT_MARGIN,
            WORLD_HEIGHT - FUEL_PROGRESS_BAR_TOP_MARGIN - FUEL_PROGRESS_BAR_HEIGHT);

        return fuelProgressBar;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
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

    }

    @Override
    public void dispose()
    {
        Constants.EVENT_BUS.unregister(this);
    }

    @Subscribe
    public void FuelChanged(FuelChangeEvent fuelChangeEvent)
    {
        updateFuelProgressBar(fuelChangeEvent.fuel);
    }

    private void updateFuelProgressBar(float fuel)
    {
        fuelProgressBar.setValue(fuel);
    }
}
