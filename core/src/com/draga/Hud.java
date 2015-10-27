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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.draga.event.FuelChangeEvent;
import com.draga.event.StarCollectedEvent;
import com.draga.manager.AssMan;
import com.google.common.eventbus.Subscribe;

import java.util.Stack;

public class Hud implements Screen
{
    private Stage stage;

    private ProgressBar fuelProgressBar;

    private Stack<Image> grayStars;
    private Table        starsTable;

    private float        shipVelocity;

    public Hud()
    {
        Constants.EVENT_BUS.register(this);
        this.grayStars = new Stack<>();
        stage = new Stage();

        Table table = new Table();
        table.setFillParent(true);
        table.pad(((stage.getHeight() + stage.getWidth()) / 2f) / 50f);
        stage.addActor(table);

        float fuelProgressBarHeight = stage.getHeight() / 30f;
        fuelProgressBar = getFuelProgressBar((int) fuelProgressBarHeight);
        table
            .add(fuelProgressBar)
            .height(fuelProgressBarHeight)
            .width(stage.getWidth() / 3f)
            .top()
            .left();

        // Add an empty row with an expanded cell to fill the gap in the middle.
        table.row();
        table.add().expand();

        table.row();
        starsTable = new Table();
        starsTable
            .defaults()
            .width(stage.getViewport().getScreenWidth() / 30f)
            .height(stage.getViewport().getScreenWidth() / 30f);
        table
            .add(starsTable)
            .bottom()
            .right();

        stage.setDebugAll(Constants.IS_DEBUGGING);
    }

    private ProgressBar getFuelProgressBar(int height)
    {
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(
            1, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        ProgressBar.ProgressBarStyle fuelProgressBarStyle = new ProgressBar.ProgressBarStyle(
            skin.newDrawable("white", Color.DARK_GRAY), null);
        fuelProgressBarStyle.knobBefore = skin.newDrawable("white", Color.WHITE);

        ProgressBar fuelProgressBar = new ProgressBar(
            0, 1, 0.0001f, false, fuelProgressBarStyle);

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
        stage.dispose();
    }

    @Subscribe
    public void FuelChanged(FuelChangeEvent fuelChangeEvent)
    {
        fuelProgressBar.setRange(0, fuelChangeEvent.maxFuel);
        fuelProgressBar.setValue(fuelChangeEvent.newFuel);
    }

    public void addStar()
    {
        Texture starTexture = AssMan.getAssetManager().get("star/starGray64.png");
        Image starImage = new Image(starTexture);

        grayStars.add(starImage);

        starsTable.add(starImage);
    }

    @Subscribe
    public void starCollected(StarCollectedEvent starCollectedEvent)
    {
        Image firstGrayStar = grayStars.pop();

        Texture goldStarTexture = AssMan.getAssetManager().get("star/starGold64.png");

        firstGrayStar.setDrawable(new TextureRegionDrawable(new TextureRegion(goldStarTexture)));
    }
}
