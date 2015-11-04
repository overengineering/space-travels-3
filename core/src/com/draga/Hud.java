package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.draga.entity.Ship;
import com.draga.event.FuelChangeEvent;
import com.draga.event.SpeedChangedEvent;
import com.draga.event.StarCollectedEvent;
import com.draga.manager.GravityManager;
import com.draga.manager.InputManager;
import com.draga.manager.asset.AssMan;
import com.google.common.eventbus.Subscribe;

import java.util.Stack;

public class Hud implements Screen
{
    private float FORCE_INDICATOR_SCALE = 0.25f;

    private Stage stage;

    private ProgressBar fuelProgressBar;
    private ProgressBar speedProgressBar;

    private Stack<Image>       grayStars;
    private Table              starsTable;
    private Ship               ship;
    private ShapeRenderer      shapeRenderer;
    private OrthographicCamera orthographicCamera;

    public Hud(OrthographicCamera orthographicCamera)
    {
        this.orthographicCamera = orthographicCamera;
        shapeRenderer = new ShapeRenderer();
        Constants.EVENT_BUS.register(this);
        this.grayStars = new Stack<>();
        stage = new Stage();

        Table table = new Table();
        table.setFillParent(true);
        table.pad(((stage.getHeight() + stage.getWidth()) / 2f) / 50f);
        stage.addActor(table);

        float progressBarsHeight = stage.getHeight() / 30f;
        fuelProgressBar = getFuelProgressBar((int) progressBarsHeight);
        table
            .add(fuelProgressBar)
            .height(progressBarsHeight)
            .width(stage.getWidth() / 3f)
            .top()
            .left();
        table
            .add()
            .expandX();
        speedProgressBar = getSpeedLabel((int) progressBarsHeight);
        table
            .add(speedProgressBar)
            .height(progressBarsHeight)
            .width(stage.getWidth() / 3f)
            .top()
            .right();

        // Add an empty row with an expanded cell to fill the gap in the middle.
        table.row();
        table.add().expand();

        table.row();
        table.add();
        table.add();
        starsTable = new Table();
        starsTable
            .defaults()
            .width(stage.getViewport().getScreenWidth() / 30f)
            .height(stage.getViewport().getScreenWidth() / 30f);
        table
            .add(starsTable)
            .bottom()
            .right();

        stage.setDebugAll(Constants.DEBUG_DRAW);
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

    private ProgressBar getSpeedLabel(int height)
    {
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(
            1, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        ProgressBar.ProgressBarStyle speedProgressBarStyle = new ProgressBar.ProgressBarStyle(
            skin.newDrawable("white", Color.DARK_GRAY), null);
        speedProgressBarStyle.knobBefore = skin.newDrawable("white", Color.WHITE);

        ProgressBar speedProgressBar = new ProgressBar(
            0, 120, 1f, false, speedProgressBarStyle);

        return speedProgressBar;
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

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(orthographicCamera.combined);

        if (Constants.HUD_DRAW_VELOCITY_INDICATORS)
        {
            DrawGravityIndicator();
            DrawVelocityIndicator();
            DrawInputIndicator();
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void DrawGravityIndicator()
    {
        Vector2 gravVector = GravityManager.getForceActingOn(ship.getBody());

        shapeRenderer.setColor(new Color(1, 0, 0, 0.5f));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(
            ship.getX() + gravVector.x * FORCE_INDICATOR_SCALE,
            ship.getY() + gravVector.y * FORCE_INDICATOR_SCALE,
            0.5f);
        shapeRenderer.end();
    }
    
    private void DrawVelocityIndicator()
    {
        shapeRenderer.setColor(new Color(1, 1, 1, 0.5f));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(
            ship.getX() + ship.getBody().getLinearVelocity().x * FORCE_INDICATOR_SCALE,
            ship.getY() + ship.getBody().getLinearVelocity().y * FORCE_INDICATOR_SCALE,
            0.5f);
        shapeRenderer.end();
    }
    
    private void DrawInputIndicator()
    {
        Vector2 inputVec = InputManager.getInputForce();

        shapeRenderer.setColor(new Color(0, 1, 0, 0.5f));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(
            ship.getX() + inputVec.x / FORCE_INDICATOR_SCALE,
            ship.getY() + inputVec.y / FORCE_INDICATOR_SCALE,
            0.5f);
        shapeRenderer.end();
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
        shapeRenderer.dispose();
    }

    @Subscribe
    public void FuelChanged(FuelChangeEvent fuelChangeEvent)
    {
        fuelProgressBar.setRange(0, fuelChangeEvent.maxFuel);
        fuelProgressBar.setValue(fuelChangeEvent.newFuel);
    }

    public void addStar()
    {
        Texture starTexture = AssMan.getAssMan().get(AssMan.getAssList().starGray);
        Image starImage = new Image(starTexture);

        grayStars.add(starImage);

        starsTable.add(starImage);
    }

    @Subscribe
    public void starCollected(StarCollectedEvent starCollectedEvent)
    {
        Image firstGrayStar = grayStars.pop();

        Texture goldStarTexture = AssMan.getAssMan().get(AssMan.getAssList().starGold);

        firstGrayStar.setDrawable(new TextureRegionDrawable(new TextureRegion(goldStarTexture)));
    }

    @Subscribe
    public void SpeedChanged(SpeedChangedEvent speedChangedEvent)
    {
        speedProgressBar.setValue(speedChangedEvent.speed);
    }

    public void setShip(Ship ship)
    {
        this.ship = ship;
    }
}
