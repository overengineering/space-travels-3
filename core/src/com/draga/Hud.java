package com.draga;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.draga.event.FuelChangeEvent;
import com.draga.event.StarCollectedEvent;
import com.draga.manager.AssMan;
import com.google.common.eventbus.Subscribe;

import java.util.Stack;

public class Hud implements Screen
{
    private Stage        stage;
    private ProgressBar  fuelProgressBar;

    private int          totalStars;
    private Stack<Image> grayStars;

    private float        shipVelocity;

    public Hud()
    {
        Constants.EVENT_BUS.register(this);

        this.totalStars = 0;

        this.grayStars = new Stack<>();

        stage = new Stage();

        fuelProgressBar = getFuelProgressBar();
        stage.addActor(fuelProgressBar);

        stage.setDebugAll(Constants.IS_DEBUGGING);
    }

    private ProgressBar getFuelProgressBar()
    {
        float height = stage.getHeight() / 30f;
        float width = stage.getWidth() / 3f;
        float margin = ((stage.getHeight() + stage.getWidth()) / 2f) / 50f;

        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(
            1, (int) Math.ceil(height), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        ProgressBar.ProgressBarStyle fuelProgressBarStyle = new ProgressBar.ProgressBarStyle(
            skin.newDrawable("white", Color.DARK_GRAY), null);
        fuelProgressBarStyle.knobBefore = skin.newDrawable("white", Color.WHITE);

        ProgressBar fuelProgressBar = new ProgressBar(
            0, 1, 0.0001f, false, fuelProgressBarStyle);
        fuelProgressBar.setSize(width, height);
        fuelProgressBar.setPosition(
            margin, stage.getHeight() - margin - height);

        return fuelProgressBar;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        stage.act(delta);
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
        fuelProgressBar.setRange(0, fuelChangeEvent.maxFuel);
        fuelProgressBar.setValue(fuelChangeEvent.newFuel);
    }

    public void addStar()
    {
        totalStars++;

        float width = stage.getViewport().getScreenWidth() / 30f;
        float height = width;
        Texture starTexture = AssMan.getAssetManager().get("star/starGray64.png");
        Image starImage = new Image(starTexture);
        starImage.setPosition(stage.getViewport().getScreenWidth() - (width * totalStars), 0);
        starImage.setSize(width, height);

        grayStars.add(starImage);

        stage.addActor(starImage);
    }

    @Subscribe
    public void starCollected(StarCollectedEvent starCollectedEvent)
    {
        Image firstGrayStar = grayStars.pop();

        Texture goldStarTexture = AssMan.getAssetManager().get("star/starGold64.png");

        firstGrayStar.setDrawable(new TextureRegionDrawable(new TextureRegion(goldStarTexture)));
    }
}
