package com.draga.spaceTravels3;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.event.PickupCollectedEvent;
import com.draga.spaceTravels3.gameEntity.Ship;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.physic.PhysicsEngine;
import com.draga.utils.GraphicsUtils;
import com.draga.utils.PixmapUtils;
import com.google.common.eventbus.Subscribe;

import java.util.Stack;

public class Hud implements Screen
{
    private final Label                 scoreLabel;
    private final Camera                worldCamera;
    private final Level                 level;
    private       Stage                 stage;
    private       Actor                 fuelIndicator;
    private       Stack<Image>          grayPickups;
    private       Table                 pickupTable;
    private       Ship                  ship;
    private       MiniMap               miniMap;
    private       TextureRegionDrawable collectedPickupDrawable;

    public Hud(Camera worldCamera, Level level)
    {
        this.level = level;
        this.ship = level.getShip();
        this.worldCamera = worldCamera;

        Constants.General.EVENT_BUS.register(this);

        this.grayPickups = new Stack<>();
        Texture pickupTexture = AssMan.getAssMan().get(AssMan.getAssList().pickupTexture);
        this.collectedPickupDrawable = new TextureRegionDrawable(new TextureRegion(pickupTexture));

        this.miniMap = new MiniMap(level);

        this.stage = new Stage();

        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Top row left column
        float width = this.stage.getWidth() / 3f;
        this.fuelIndicator = getFuelIndicator(width);
        table
            .add(this.fuelIndicator)
            .top()
            .left();

        // Top row right column.
        this.scoreLabel = getScoreLabel();
        table
            .add(this.scoreLabel)
            .top()
            .right();

        // Add an empty row with an expanded cell to fill the gap in the middle.
        table.row();
        table.add().expand();

        // Bottom row left column
        table.row();
        table.add();

        // Bottom row right column;
        this.pickupTable = createPickupTable();
        table
            .add(this.pickupTable)
            .bottom()
            .right();

        if (SettingsManager.getSettings().inputType == InputType.TOUCH
            || Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            Image joystickOverlayImage = createJoystickOverlay();
            joystickOverlayImage.setScaling(Scaling.fit);

            Container<Image> joystickOverlayContainer = new Container<>(joystickOverlayImage);
            joystickOverlayContainer.setFillParent(true);
            joystickOverlayContainer.center();

            this.stage.addActor(joystickOverlayContainer);
        }

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private Actor getFuelIndicator(float width)
    {
        if (this.ship.isInfiniteFuel())
        {
            Label infiniteFuelLabel = new Label("inf", UIManager.skin);
            return infiniteFuelLabel;
        }
        else
        {
            final ProgressBar fuelProgressBar =
                UIManager.getDelimitedProgressBar(this.ship.getMaxFuel(), width);

            fuelProgressBar.addAction(new Action()
            {
                @Override
                public boolean act(float delta)
                {
                    fuelProgressBar.setValue(Hud.this.ship.getCurrentFuel());
                    return false;
                }
            });
            return fuelProgressBar;
        }
    }

    private Label getScoreLabel()
    {
        Label scoreLabel = new Label("", UIManager.skin);

        return scoreLabel;
    }

    private Table createPickupTable()
    {
        this.pickupTable = new Table();
        this.pickupTable
            .defaults()
            .height(this.stage.getViewport().getScreenWidth() / 30f);

        Texture pickupTexture = AssMan.getAssMan().get(AssMan.getAssList().pickupGreyTexture);

        for (int i = 0; i < this.level.getPickups().size(); i++)
        {
            Image pickupImage = new Image(pickupTexture);

            pickupImage.setScaling(Scaling.fit);

            this.grayPickups.add(pickupImage);

            this.pickupTable.add(pickupImage);
        }

        return this.pickupTable;
    }

    private Image createJoystickOverlay()
    {
        float smallestDimension = Math.min(this.stage.getWidth(), this.stage.getHeight());

        Pixmap pixmap =
            new Pixmap(
                (int) smallestDimension,
                (int) smallestDimension,
                Pixmap.Format.RGBA8888);
        pixmap.setColor(Constants.Visual.HUD.JOYSTICK_OVERLAY_COLOR);

        int numOuterArcs = 8;
        float halfSmallestDimension = smallestDimension / 2f;

        PixmapUtils.dashedCircle(
            pixmap,
            halfSmallestDimension,
            halfSmallestDimension,
            halfSmallestDimension,
            numOuterArcs,
            15,
            360 / numOuterArcs / 2,
            100,
            Constants.Visual.HUD.JOYSTICK_OVERLAY_WIDTH);
        PixmapUtils.dashedCircle(
            pixmap,
            halfSmallestDimension,
            halfSmallestDimension,
            halfSmallestDimension * Constants.Game.DEAD_ZONE,
            4,
            30,
            0,
            100,
            Constants.Visual.HUD.JOYSTICK_OVERLAY_WIDTH);

        return new Image(new Texture(pixmap));
    }

    private void setScoreLabel(int score)
    {
        this.scoreLabel.setText(String.valueOf(score));
    }

    public MiniMap getMiniMap()
    {
        return this.miniMap;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        setScoreLabel(this.level.getScore());

        this.stage.act(delta);
        this.stage.draw();

        GraphicsUtils.enableBlending();

        SpaceTravels3.shapeRenderer.begin();

        if (GameEntityManager.getGameEntities().contains(this.ship))
        {
            SpaceTravels3.shapeRenderer.setProjectionMatrix(this.worldCamera.combined);
            if (SettingsManager.getSettings().hudForceIndicators)
            {
                drawGravityIndicator();
                drawVelocityIndicator();
            }
            drawApproachSpeedIndicator();
        }


        this.miniMap.update();
        this.miniMap.draw();
        SpaceTravels3.shapeRenderer.end();
        GraphicsUtils.disableBlending();
    }

    private void drawGravityIndicator()
    {
        try (PooledVector2 gravityVector = PhysicsEngine.calculateGravityForce(this.ship.physicsComponent))
        {

            SpaceTravels3.shapeRenderer.setColor(Color.BLUE);
            SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            SpaceTravels3.shapeRenderer.circle(
                this.ship.physicsComponent.getPosition().x
                    + gravityVector.x * Constants.Visual.HUD.FORCE_INDICATOR_SCALE,
                this.ship.physicsComponent.getPosition().y
                    + gravityVector.y * Constants.Visual.HUD.FORCE_INDICATOR_SCALE,
                0.5f);

            SpaceTravels3.shapeRenderer.setColor(new Color(0, 0, 1f, 0.4f));
            SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
            SpaceTravels3.shapeRenderer.circle(
                this.ship.physicsComponent.getPosition().x,
                this.ship.physicsComponent.getPosition().y,
                gravityVector.len() * Constants.Visual.HUD.FORCE_INDICATOR_SCALE,
                24);
        }
    }

    private void drawVelocityIndicator()
    {
        SpaceTravels3.shapeRenderer.setColor(Color.WHITE);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        SpaceTravels3.shapeRenderer.circle(
            this.ship.physicsComponent.getPosition().x
                + this.ship.physicsComponent.getVelocity().x
                * Constants.Visual.HUD.FORCE_INDICATOR_SCALE,
            this.ship.physicsComponent.getPosition().y
                + this.ship.physicsComponent.getVelocity().y
                * Constants.Visual.HUD.FORCE_INDICATOR_SCALE,
            0.5f);

        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        SpaceTravels3.shapeRenderer.setColor(new Color(1, 1, 1, 0.4f));
        SpaceTravels3.shapeRenderer.circle(
            this.ship.physicsComponent.getPosition().x,
            this.ship.physicsComponent.getPosition().y,
            this.ship.physicsComponent.getVelocity().len()
                * Constants.Visual.HUD.FORCE_INDICATOR_SCALE,
            24);
    }

    private void drawApproachSpeedIndicator()
    {
        float radius = this.level.getDestinationPlanet().physicsComponent.getBoundsCircle().radius;
        float shipSpeed = this.ship.physicsComponent.getVelocity().len();
        // "That is not going to be confusing at all" (cit. Lee)
        Color borderCollie;
        Color fillCollins;
        if (shipSpeed > this.level.getMaxLandingSpeed())
        {
            borderCollie = Constants.Visual.HUD.DESTINATION_PLANET_OVERLAY_LOSE_BORDER;
            fillCollins = Constants.Visual.HUD.DESTINATION_PLANET_OVERLAY_LOSE_FILL;
            radius *= this.level.getMaxLandingSpeed() / shipSpeed;
        }
        else
        {
            borderCollie = Constants.Visual.HUD.DESTINATION_PLANET_OVERLAY_WIN_BORDER;
            fillCollins = Constants.Visual.HUD.DESTINATION_PLANET_OVERLAY_WIN_FILL;
            radius *= shipSpeed / this.level.getMaxLandingSpeed();
        }

        SpaceTravels3.shapeRenderer.setColor(fillCollins);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        int segments = (int) Math.max(1, (12 * (float) Math.cbrt(radius)));
        SpaceTravels3.shapeRenderer.circle(
            this.level.getDestinationPlanet().physicsComponent.getPosition().x,
            this.level.getDestinationPlanet().physicsComponent.getPosition().y,
            radius,
            segments);

        SpaceTravels3.shapeRenderer.setColor(borderCollie);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        SpaceTravels3.shapeRenderer.circle(
            this.level.getDestinationPlanet().physicsComponent.getPosition().x,
            this.level.getDestinationPlanet().physicsComponent.getPosition().y,
            radius,
            segments);
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
        Constants.General.EVENT_BUS.unregister(this);
        this.stage.dispose();
    }

    @Subscribe
    public void pickupCollected(PickupCollectedEvent pickupCollectedEvent)
    {
        Image firstPickup = this.grayPickups.pop();

        firstPickup.setDrawable(this.collectedPickupDrawable);
    }
}
