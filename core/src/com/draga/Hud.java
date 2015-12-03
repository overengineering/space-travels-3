package com.draga;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.draga.event.FuelChangeEvent;
import com.draga.event.PickupCollectedEvent;
import com.draga.event.ScoreEvent;
import com.draga.gameEntity.Ship;
import com.draga.manager.GameEntityManager;
import com.draga.manager.InputManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.UIManager;
import com.draga.manager.asset.AssMan;
import com.draga.physic.PhysicsEngine;
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

    private Ship ship;

    private final OrthographicCamera worldCamera;
    private final OrthographicCamera joystickCamera =
        new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    private MiniMap miniMap;
    
    public Hud(OrthographicCamera worldCamera, int worldWidth, int worldHeight)
    {
        this.worldCamera = worldCamera;
        Constants.EVENT_BUS.register(this);
        this.grayPickups = new Stack<>();
        this.miniMap = new MiniMap(worldWidth, worldHeight);

        stage = new Stage();

        Table table = UIManager.addDefaultTableToStage(stage);

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

        stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private ProgressBar getFuelProgressBar()
    {
        ProgressBar fuelProgressBar = new ProgressBar(
            0, 1, 0.0001f, false, UIManager.skin);

        return fuelProgressBar;
    }

    private Label getScoreLabel()
    {
        Label scoreLabel = new Label("", UIManager.skin.get(Label.LabelStyle.class));

        return scoreLabel;
    }

    @Subscribe
    public void setScoreLabel(ScoreEvent scoreEvent)
    {
        scoreLabel.setText(String.valueOf(scoreEvent.getScore()));
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

        if (Constants.HUD_DRAW_VELOCITY_INDICATORS
            && GameEntityManager.getGameEntities().contains(ship))
        {
            SpaceTravels3.shapeRenderer.begin();

            if (SettingsManager.getSettings().inputType == InputType.TOUCH
                || Gdx.app.getType() == Application.ApplicationType.Desktop)
            {
                SpaceTravels3.shapeRenderer.setProjectionMatrix(joystickCamera.combined);
                drawJoystick();
            }

            SpaceTravels3.shapeRenderer.setProjectionMatrix(worldCamera.combined);
            drawGravityIndicator();
            drawVelocityIndicator();
            SpaceTravels3.shapeRenderer.end();
        }

        miniMap.draw();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
    
    private void drawJoystick()
    {
        float smallestDimension =
            Math.min(joystickCamera.viewportWidth, joystickCamera.viewportHeight);
        float deadZoneHeight = smallestDimension * InputManager.DEAD_ZONE / 2f;

        SpaceTravels3.shapeRenderer.setColor(new Color(1, 1, 1, 0.5f));

        ShapeRendererUtility.dashedCircle(SpaceTravels3.shapeRenderer, 0, 0, deadZoneHeight, 4, 30, 0, 12, 2);

        int numArcs = 8;

        ShapeRendererUtility.dashedCircle(
            SpaceTravels3.shapeRenderer,
            0,
            0,
            smallestDimension / 2,
            numArcs,
            15,
            360 / numArcs / 2,
            24,
            2);
    }

    private void drawGravityIndicator()
    {
        Vector2 gravityVector = PhysicsEngine.getGravityForceActingOn(ship);

        SpaceTravels3.shapeRenderer.setColor(Color.BLUE);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        SpaceTravels3.shapeRenderer.circle(
            ship.physicsComponent.getPosition().x + gravityVector.x * FORCE_INDICATOR_SCALE,
            ship.physicsComponent.getPosition().y + gravityVector.y * FORCE_INDICATOR_SCALE,
            0.5f);

        SpaceTravels3.shapeRenderer.setColor(new Color(0, 0, 1f, 0.4f));
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        SpaceTravels3.shapeRenderer.circle(
            ship.physicsComponent.getPosition().x,
            ship.physicsComponent.getPosition().y,
            gravityVector.len() * FORCE_INDICATOR_SCALE,
            24);
    }

    private void drawVelocityIndicator()
    {
        SpaceTravels3.shapeRenderer.setColor(Color.WHITE);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        SpaceTravels3.shapeRenderer.circle(
            ship.physicsComponent.getPosition().x
                + ship.physicsComponent.getVelocity().x * FORCE_INDICATOR_SCALE,
            ship.physicsComponent.getPosition().y
                + ship.physicsComponent.getVelocity().y * FORCE_INDICATOR_SCALE,
            0.5f);

        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        SpaceTravels3.shapeRenderer.setColor(new Color(1, 1, 1, 0.4f));
        SpaceTravels3.shapeRenderer.circle(
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
        miniMap.addShip(ship);
    }
}
