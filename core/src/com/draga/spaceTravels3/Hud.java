package com.draga.spaceTravels3;

import com.badlogic.gdx.Application;
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
import com.draga.PixmapUtility;
import com.draga.spaceTravels3.event.PickupCollectedEvent;
import com.draga.spaceTravels3.gameEntity.Ship;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.physic.PhysicsEngine;
import com.google.common.eventbus.Subscribe;

import java.util.Stack;

public class Hud implements Screen
{
    private final Label        scoreLabel;
    private final Camera       worldCamera;
    private final Level        level;
    private       Stage        stage;
    private       ProgressBar  fuelProgressBar;
    private       Stack<Image> grayPickups;
    private       Table        pickupTable;
    private       Ship         ship;
    private       MiniMap      miniMap;
    
    public Hud(Camera worldCamera, Level level)
    {
        this.level = level;

        this.worldCamera = worldCamera;
        Constants.General.EVENT_BUS.register(this);
        this.grayPickups = new Stack<>();
        this.miniMap = new MiniMap(level.getWidth(), level.getHeight());

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


        if (SettingsManager.getSettings().inputType == InputType.TOUCH
            || Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            float smallestDimension = Math.min(stage.getWidth(), stage.getHeight());

            Pixmap pixmap =
                new Pixmap(
                    (int) smallestDimension,
                    (int) smallestDimension,
                    Pixmap.Format.RGBA8888);
            pixmap.setColor(Constants.Visual.JOYSTICK_OVERLAY_COLOR);

            int numOuterArcs = 8;
            float halfSmallestDimension = smallestDimension / 2f;

            PixmapUtility.dashedCircle(
                pixmap,
                halfSmallestDimension,
                halfSmallestDimension,
                halfSmallestDimension,
                numOuterArcs,
                15,
                360 / numOuterArcs / 2,
                100,
                2);
            PixmapUtility.dashedCircle(
                pixmap,
                halfSmallestDimension,
                halfSmallestDimension,
                halfSmallestDimension * Constants.Game.DEAD_ZONE,
                4,
                30,
                0,
                100,
                2);

            Image joystickOverlayImage = new Image(new Texture(pixmap));
            joystickOverlayImage.setScaling(Scaling.fit);
            Table joystickOverlayTable = new Table();
            joystickOverlayTable.setFillParent(true);
            joystickOverlayTable
                .add(joystickOverlayImage)
                .center();

            stage.addActor(joystickOverlayTable);
        }

        this.ship = level.getShip();
        miniMap.addShip(this.ship);

        for (int i = 0; i < level.getPickups().size(); i++)
        {
            this.addPickup();
        }

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
        Label scoreLabel = new Label("", UIManager.skin);

        return scoreLabel;
    }

    private void setScoreLabel(int score)
    {
        scoreLabel.setText(String.valueOf(score));
    }

    public void addPickup()
    {
        Texture pickupTexture = AssMan.getAssMan().get(AssMan.getAssList().pickupGreyTexture);
        Image pickupImage = new Image(pickupTexture);

        pickupImage.setScaling(Scaling.fit);

        grayPickups.add(pickupImage);

        pickupTable.add(pickupImage);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        updateFuelProgressBar();

        stage.act(delta);
        stage.draw();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        SpaceTravels3.shapeRenderer.begin();

        if (SettingsManager.getSettings().hudForceIndicators
            && GameEntityManager.getGameEntities().contains(ship))
        {

            SpaceTravels3.shapeRenderer.setProjectionMatrix(worldCamera.combined);
            drawGravityIndicator();
            drawVelocityIndicator();

        }

        miniMap.draw();
        SpaceTravels3.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void updateFuelProgressBar()
    {
        fuelProgressBar.setRange(0, ship.getMaxFuel());
        fuelProgressBar.setValue(ship.getCurrentFuel());
    }

    private void drawGravityIndicator()
    {
        Vector2 gravityVector = PhysicsEngine.getGravityForceActingOn(ship);

        SpaceTravels3.shapeRenderer.setColor(Color.BLUE);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        SpaceTravels3.shapeRenderer.circle(
            ship.physicsComponent.getPosition().x
                + gravityVector.x * Constants.Visual.HUD_FORCE_INDICATOR_SCALE,
            ship.physicsComponent.getPosition().y
                + gravityVector.y * Constants.Visual.HUD_FORCE_INDICATOR_SCALE,
            0.5f);

        SpaceTravels3.shapeRenderer.setColor(new Color(0, 0, 1f, 0.4f));
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        SpaceTravels3.shapeRenderer.circle(
            ship.physicsComponent.getPosition().x,
            ship.physicsComponent.getPosition().y,
            gravityVector.len() * Constants.Visual.HUD_FORCE_INDICATOR_SCALE,
            24);
    }

    private void drawVelocityIndicator()
    {
        SpaceTravels3.shapeRenderer.setColor(Color.WHITE);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        SpaceTravels3.shapeRenderer.circle(
            ship.physicsComponent.getPosition().x
                + ship.physicsComponent.getVelocity().x
                * Constants.Visual.HUD_FORCE_INDICATOR_SCALE,
            ship.physicsComponent.getPosition().y
                + ship.physicsComponent.getVelocity().y
                * Constants.Visual.HUD_FORCE_INDICATOR_SCALE,
            0.5f);

        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        SpaceTravels3.shapeRenderer.setColor(new Color(1, 1, 1, 0.4f));
        SpaceTravels3.shapeRenderer.circle(
            ship.physicsComponent.getPosition().x,
            ship.physicsComponent.getPosition().y,
            ship.physicsComponent.getVelocity().len() * Constants.Visual.HUD_FORCE_INDICATOR_SCALE,
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
        Constants.General.EVENT_BUS.unregister(this);
        stage.dispose();
    }

    @Subscribe
    public void pickupCollected(PickupCollectedEvent pickupCollectedEvent)
    {
        Image firstPickup = grayPickups.pop();

        Texture pickupTexture = AssMan.getAssMan().get(AssMan.getAssList().pickupTexture);

        firstPickup.setDrawable(new TextureRegionDrawable(new TextureRegion(pickupTexture)));
    }

    public void update()
    {
        this.setScore(level.getScore());
    }

    public void setScore(int score)
    {
        setScoreLabel(score);
    }
}
