package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.draga.entity.GameEntity;
import com.draga.manager.GameEntityManager;
import com.draga.physic.PhysicsEngine;
import com.draga.entity.Ship;
import com.draga.event.FuelChangeEvent;
import com.draga.event.PickupCollectedEvent;
import com.draga.manager.SettingsManager;
import com.draga.manager.SkinManager;
import com.draga.manager.asset.AssMan;
import com.draga.event.ScoreEvent;
import com.google.common.eventbus.Subscribe;

import java.util.Stack;

public class Hud implements Screen
{
    private final Label scoreLabel;

    private float FORCE_INDICATOR_SCALE = 0.25f;

    private Stage stage;

    private ProgressBar fuelProgressBar;

    private Stack<Image> grayPickups;
    private Table        pickupTable;

    private Ship         ship;

    private ShapeRenderer      shapeRenderer;
    private OrthographicCamera orthographicCamera;

    public Hud(OrthographicCamera orthographicCamera)
    {
        this.orthographicCamera = orthographicCamera;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        Constants.EVENT_BUS.register(this);
        this.grayPickups = new Stack<>();
        stage = new Stage();

        Table table = new Table();
        table.setFillParent(true);
        table.pad(((stage.getHeight() + stage.getWidth()) / 2f) / 50f);
        stage.addActor(table);

        // Top row left column
        fuelProgressBar = getFuelProgressBar();
        table
            .add(fuelProgressBar)
            .width(stage.getWidth() / 3f)
            .top()
            .left();

        // Top row right column.
        scoreLabel = getScoreLabel();
        table
            .add(scoreLabel)
            .top()
            .right();

        // Add an empty row with an expanded cell to fill the gap in the middle.
        table.row();
        table.add().expand();

        table.row();
        // Bottom row left column
        table.add();

        // Bottom row right column;
        pickupTable = new Table();
        pickupTable
            .defaults()
            .height(stage.getViewport().getScreenWidth() / 30f);
        table
            .add(pickupTable)
            .bottom()
            .right();

        stage.setDebugAll(SettingsManager.debugDraw);
    }

    private Label getScoreLabel()
    {
        Label scoreLabel = new Label("", SkinManager.BasicSkin.get(Label.LabelStyle.class));

        return scoreLabel;
    }

    private ProgressBar getFuelProgressBar()
    {
        ProgressBar.ProgressBarStyle fuelBarStyle = new ProgressBar.ProgressBarStyle(
            SkinManager.BasicSkin.newDrawable("progressbar", Color.DARK_GRAY), null);
        fuelBarStyle.knobBefore = SkinManager.BasicSkin.newDrawable("progressbar", Color.WHITE);

        ProgressBar fuelProgressBar = new ProgressBar(
            0, 1, 0.0001f, false, fuelBarStyle);

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

        if (Constants.HUD_DRAW_VELOCITY_INDICATORS)
           // && ship.getBody().isActive())
        {
            shapeRenderer.setProjectionMatrix(orthographicCamera.combined);
            shapeRenderer.begin();
            DrawGravityIndicator();
            DrawVelocityIndicator();
            shapeRenderer.end();
        }

        MiniMap.getShapeRenderer().begin();
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.miniMapGraphicComponent.draw(MiniMap.getShapeRenderer());
        }

        MiniMap.getShapeRenderer().end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void DrawGravityIndicator()
    {
        Vector2 gravityVector = PhysicsEngine.getForceActingOn(ship);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(
            ship.physicsComponent.getPosition().x + gravityVector.x * FORCE_INDICATOR_SCALE,
            ship.physicsComponent.getPosition().y + gravityVector.y * FORCE_INDICATOR_SCALE,
            0.5f);

        shapeRenderer.setColor(new Color(0, 0, 1f, 0.4f));
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.circle(
            ship.physicsComponent.getPosition().x,
            ship.physicsComponent.getPosition().y,
            gravityVector.len() * FORCE_INDICATOR_SCALE,
            24);
    }
    
    private void DrawVelocityIndicator()
    {
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(
            ship.physicsComponent.getPosition().x + ship.physicsComponent.getVelocity().x * FORCE_INDICATOR_SCALE,
            ship.physicsComponent.getPosition().y + ship.physicsComponent.getVelocity().y * FORCE_INDICATOR_SCALE,
            0.5f);

        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(1, 1, 1, 0.4f));
        shapeRenderer.circle(
            ship.physicsComponent.getPosition().x,
            ship.physicsComponent.getPosition().y,
            ship.physicsComponent.getVelocity().len() * FORCE_INDICATOR_SCALE,
            24);
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

    public void addPickup()
    {
        Texture pickupTexture = AssMan.getAssMan().get(AssMan.getAssList().pickupGrey);
        Image pickupImage = new Image(pickupTexture);

        pickupImage.setScaling(Scaling.fit);

        grayPickups.add(pickupImage);

        pickupTable.add(pickupImage);
    }

    @Subscribe
    public void pickupCollected(PickupCollectedEvent pickupCollectedEvent)
    {
        Image firstPickup = grayPickups.pop();

        Texture pickupTexture = AssMan.getAssMan().get(AssMan.getAssList().pickup);

        firstPickup.setDrawable(new TextureRegionDrawable(new TextureRegion(pickupTexture)));
    }

    public void setShip(Ship ship)
    {
        this.ship = ship;
    }

    @Subscribe
    public void setScoreLabel(ScoreEvent scoreEvent)
    {
        scoreLabel.setText(String.valueOf(scoreEvent.getScore()));
    }
}
