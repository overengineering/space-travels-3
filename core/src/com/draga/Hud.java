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
import com.draga.entity.GameEntity;
import com.draga.entity.Ship;
import com.draga.event.FuelChangeEvent;
import com.draga.event.StarCollectedEvent;
import com.draga.manager.GameEntityManager;
import com.draga.manager.GravityManager;
import com.draga.manager.asset.AssMan;
import com.google.common.eventbus.Subscribe;

import java.util.Stack;

public class Hud implements Screen
{
    private float FORCE_INDICATOR_SCALE = 0.25f;

    private Stage stage;

    private ProgressBar fuelProgressBar;

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
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);

        MiniMap.getShapeRenderer().begin();
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.drawMiniMap();
        }
        MiniMap.getShapeRenderer().end();
    }

    private void DrawGravityIndicator()
    {
        Vector2 gravityVector = GravityManager.getForceActingOn(ship.getBody());

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(
            ship.getX() + gravityVector.x * FORCE_INDICATOR_SCALE,
            ship.getY() + gravityVector.y * FORCE_INDICATOR_SCALE,
            0.5f);
        shapeRenderer.end();

        shapeRenderer.setColor(new Color(1, 0, 0, 0.4f));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.circle(
            ship.getX(),
            ship.getY(),
            gravityVector.len() * FORCE_INDICATOR_SCALE,
            24);
        shapeRenderer.end();
    }
    
    private void DrawVelocityIndicator()
    {
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(
            ship.getX() + ship.getBody().getLinearVelocity().x * FORCE_INDICATOR_SCALE,
            ship.getY() + ship.getBody().getLinearVelocity().y * FORCE_INDICATOR_SCALE,
            0.5f);
        shapeRenderer.end();

        shapeRenderer.setColor(new Color(1, 1, 1, 0.2f));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.circle(
            ship.getX(),
            ship.getY(),
            ship.getBody().getLinearVelocity().len() * FORCE_INDICATOR_SCALE,
            24);
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

    public void setShip(Ship ship)
    {
        this.ship = ship;
    }
}
