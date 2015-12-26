package com.draga.spaceTravels3;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Scaling;
import com.draga.utils.PixmapUtils;
import com.draga.spaceTravels3.event.PickupCollectedEvent;
import com.draga.spaceTravels3.gameEntity.Ship;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.physic.PhysicsEngine;
import com.draga.utils.GraphicsUtils;
import com.google.common.eventbus.Subscribe;

import java.util.Stack;

public class Hud implements Screen
{
    private final Label        scoreLabel;
    private final Camera       worldCamera;
    private final Level        level;
    private       Stage        stage;
    private       Actor        fuelIndicator;
    private       Stack<Image> grayPickups;
    private       Table        pickupTable;
    private       Ship         ship;

    public MiniMap getMiniMap()
    {
        return miniMap;
    }

    private MiniMap               miniMap;
    private TextureRegionDrawable collectedPickupDrawable;


    public Hud(Camera worldCamera, Level level)
    {
        this.level = level;
        this.ship = level.getShip();
        this.worldCamera = worldCamera;

        Constants.General.EVENT_BUS.register(this);

        this.grayPickups = new Stack<>();
        Texture pickupTexture = AssMan.getAssMan().get(AssMan.getAssList().pickupTexture);
        collectedPickupDrawable = new TextureRegionDrawable(new TextureRegion(pickupTexture));

        this.miniMap = new MiniMap(ship, level.getWidth(), level.getHeight());

        stage = new Stage();

        Table table = UIManager.addDefaultTableToStage(stage);

        // Top row left column
        float width = stage.getWidth() / 3f;
        fuelIndicator = getFuelIndicator(width);
        table
            .add(fuelIndicator)
            .width(width)
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

        // Bottom row left column
        table.row();
        table.add();

        // Bottom row right column;
        pickupTable = createPickupTable();
        table
            .add(pickupTable)
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

            stage.addActor(joystickOverlayContainer);
        }

        stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private Actor getFuelIndicator(float width)
    {
        if (!ship.isInfiniteFuel())
        {
            /*Pixmap pixmap = new Pixmap(
                Math.round(width),
                Math.round((Gdx.graphics.getHeight() / 30f)),
                Pixmap.Format.RGBA8888);

            pixmap.setColor(Color.RED);
            pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());

            float chunkSize = width / ship.getMaxFuel();
            for (int i = 0; i < ship.getMaxFuel(); i++)
            {
                pixmap.setColor(Color.BLUE);
                pixmap.fillRectangle(Math.round(chunkSize * i) - 1, 0, 1, pixmap.getHeight());
            }
            Texture texture = new Texture(pixmap);
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

            final ProgressBar fuelProgressBar = new ProgressBar(
                0, ship.getMaxFuel(), 0.001f, false, UIManager.skin);


            Pixmap pixmap2 = new Pixmap(
                Math.round(width),
                Math.round((Gdx.graphics.getHeight() / 30f)),
                Pixmap.Format.RGBA8888);

            pixmap2.setColor(Color.GREEN);
            pixmap2.fillRectangle(0, 0, pixmap2.getWidth(), pixmap2.getHeight());

            for (int i = 0; i < ship.getMaxFuel(); i++)
            {
                pixmap2.setColor(Color.BLUE);
                pixmap2.fillRectangle(Math.round(chunkSize * i) - 1, 0, 1, pixmap2.getHeight());
            }
            Texture texture2 = new Texture(pixmap2);
            texture2.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

            Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(texture));
            fuelProgressBar.getStyle().background = backgroundDrawable;
            fuelProgressBar.getStyle().knobBefore = null;
            fuelProgressBar.getStyle().knobAfter =
                new SpriteDrawable(new Sprite(new Texture(pixmap2)));*/

            final ProgressBar fuelProgressBar = UIManager.getDelimitedProgressBar(ship.getMaxFuel(), width);
            fuelProgressBar.addAction(new Action()
            {
                @Override
                public boolean act(float delta)
                {
                    fuelProgressBar.setValue(ship.getCurrentFuel());
                    return false;
                }
            });
            return fuelProgressBar;
        }
        else
        {
            Label infiniteFuelLabel = new Label("inf", UIManager.skin);
            return infiniteFuelLabel;
        }
    }

    private Label getScoreLabel()
    {
        Label scoreLabel = new Label("", UIManager.skin);

        return scoreLabel;
    }

    private Table createPickupTable()
    {
        pickupTable = new Table();
        pickupTable
            .defaults()
            .height(stage.getViewport().getScreenWidth() / 30f);

        Texture pickupTexture = AssMan.getAssMan().get(AssMan.getAssList().pickupGreyTexture);

        for (int i = 0; i < level.getPickups().size(); i++)
        {
            Image pickupImage = new Image(pickupTexture);

            pickupImage.setScaling(Scaling.fit);

            grayPickups.add(pickupImage);

            pickupTable.add(pickupImage);
        }

        return pickupTable;
    }

    private Image createJoystickOverlay()
    {
        float smallestDimension = Math.min(stage.getWidth(), stage.getHeight());

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
            2);
        PixmapUtils.dashedCircle(
            pixmap,
            halfSmallestDimension,
            halfSmallestDimension,
            halfSmallestDimension * Constants.Game.DEAD_ZONE,
            4,
            30,
            0,
            100,
            2);

        return new Image(new Texture(pixmap));
    }

    private void setScoreLabel(int score)
    {
        scoreLabel.setText(String.valueOf(score));
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        setScoreLabel(level.getScore());

        stage.act(delta);
        stage.draw();

        GraphicsUtils.enableBlending();

        SpaceTravels3.shapeRenderer.begin();

        if (SettingsManager.getSettings().hudForceIndicators
            && GameEntityManager.getGameEntities().contains(ship))
        {
            SpaceTravels3.shapeRenderer.setProjectionMatrix(worldCamera.combined);
            drawGravityIndicator();
            drawVelocityIndicator();
        }

        drawSpeedIndicator();

        miniMap.update();
        miniMap.draw();
        SpaceTravels3.shapeRenderer.end();
        GraphicsUtils.disableBlending();
    }

    private void drawSpeedIndicator()
    {
        float radius = level.getDestinationPlanet().physicsComponent.getBoundsCircle().radius;
        float shipSpeed = ship.physicsComponent.getVelocity().len();
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
            level.getDestinationPlanet().physicsComponent.getPosition().x,
            level.getDestinationPlanet().physicsComponent.getPosition().y,
            radius,
            segments);

        SpaceTravels3.shapeRenderer.setColor(borderCollie);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        SpaceTravels3.shapeRenderer.circle(
            level.getDestinationPlanet().physicsComponent.getPosition().x,
            level.getDestinationPlanet().physicsComponent.getPosition().y,
            radius,
            segments);
    }

    private void drawGravityIndicator()
    {
        Vector2 gravityVector = PhysicsEngine.calculateGravityForce(ship.physicsComponent);

        SpaceTravels3.shapeRenderer.setColor(Color.BLUE);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        SpaceTravels3.shapeRenderer.circle(
            ship.physicsComponent.getPosition().x
                + gravityVector.x * Constants.Visual.HUD.FORCE_INDICATOR_SCALE,
            ship.physicsComponent.getPosition().y
                + gravityVector.y * Constants.Visual.HUD.FORCE_INDICATOR_SCALE,
            0.5f);

        SpaceTravels3.shapeRenderer.setColor(new Color(0, 0, 1f, 0.4f));
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        SpaceTravels3.shapeRenderer.circle(
            ship.physicsComponent.getPosition().x,
            ship.physicsComponent.getPosition().y,
            gravityVector.len() * Constants.Visual.HUD.FORCE_INDICATOR_SCALE,
            24);
    }

    private void drawVelocityIndicator()
    {
        SpaceTravels3.shapeRenderer.setColor(Color.WHITE);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        SpaceTravels3.shapeRenderer.circle(
            ship.physicsComponent.getPosition().x
                + ship.physicsComponent.getVelocity().x
                * Constants.Visual.HUD.FORCE_INDICATOR_SCALE,
            ship.physicsComponent.getPosition().y
                + ship.physicsComponent.getVelocity().y
                * Constants.Visual.HUD.FORCE_INDICATOR_SCALE,
            0.5f);

        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        SpaceTravels3.shapeRenderer.setColor(new Color(1, 1, 1, 0.4f));
        SpaceTravels3.shapeRenderer.circle(
            ship.physicsComponent.getPosition().x,
            ship.physicsComponent.getPosition().y,
            ship.physicsComponent.getVelocity().len() * Constants.Visual.HUD.FORCE_INDICATOR_SCALE,
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

        firstPickup.setDrawable(collectedPickupDrawable);
    }
}
