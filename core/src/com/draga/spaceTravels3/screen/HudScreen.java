package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.draga.joystick.Joystick;
import com.draga.spaceTravels3.*;
import com.draga.spaceTravels3.event.InputTypeChangedEvent;
import com.draga.spaceTravels3.event.PickupCollectedEvent;
import com.draga.spaceTravels3.gameEntity.Ship;
import com.draga.spaceTravels3.level.Level;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.ui.Screen;
import com.draga.utils.GraphicsUtils;
import com.google.common.eventbus.Subscribe;

import java.util.Stack;

public class HudScreen extends Screen
{
    public static String s =
        "*6DIPaZ5ZTA2EeF1s%9v5HtlVuVOkElG8MargXavrc1j94k6u0JR6CDcdWT9FrWC5BX3NRGnGvusdXHMg2";

    private final Label                 scoreLabel;
    private final Level                 level;
    private final GameScreen            gameScreen;
    private       Container<Image>      joystickOverlayContainer;
    private       Stack<Image>          grayPickups;
    private       Table                 pickupTable;
    private       Ship                  ship;
    private       MiniMap               miniMap;
    private       TextureRegionDrawable collectedPickupDrawable;

    public HudScreen(Level level, GameScreen gameScreen)
    {
        super(true, false);

        this.level = level;
        this.gameScreen = gameScreen;
        this.ship = level.getShip();

        Constants.General.EVENT_BUS.register(this);

        this.grayPickups = new Stack<>();
        Texture pickupTexture = AssMan.getGameAssMan().get(AssMan.getAssList().pickupTexture);
        this.collectedPickupDrawable = new TextureRegionDrawable(new TextureRegion(pickupTexture));

        this.miniMap = new MiniMap(level);

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);

        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Top row left column
        float fuelIndicatorWidth = this.stage.getWidth() / 3f;
        Actor fuelIndicator = getFuelIndicator(fuelIndicatorWidth);
        table
            .add(fuelIndicator)
            .width(fuelIndicatorWidth)
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

        this.joystickOverlayContainer = new Container<>();
        this.joystickOverlayContainer.setFillParent(true);
        this.joystickOverlayContainer.center();
        this.stage.addActor(this.joystickOverlayContainer);

        if (SettingsManager.getSettings().getInputType() == InputType.TOUCH
            || Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            addJoystickOverlay();
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
                    fuelProgressBar.setValue(HudScreen.this.ship.getCurrentFuel());
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

        Texture pickupTexture = AssMan.getGameAssMan().get(AssMan.getAssList().pickupGreyTexture);

        for (int i = 0; i < this.level.getPickups().size(); i++)
        {
            Image pickupImage = new Image(pickupTexture);

            pickupImage.setScaling(Scaling.fit);

            this.grayPickups.add(pickupImage);

            this.pickupTable.add(pickupImage);
        }

        return this.pickupTable;
    }

    private void addJoystickOverlay()
    {
        Joystick joystickTexture =
            AssMan.getGameAssMan().get(Constants.Visual.HUD.JOYSTICK_ASSET_DESCRIPTOR);
        Image joystickOverlayImage = new Image(joystickTexture);
        joystickOverlayImage.setScaling(Scaling.fit);

        this.joystickOverlayContainer.setActor(joystickOverlayImage);
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
        this.gameScreen.show();
    }

    @Override
    public void render(float delta)
    {
        Score score = this.level.getScore();
        setScoreLabel(score.getTotalScore());

        this.stage.getViewport().apply();

        this.stage.act(delta);
        this.stage.draw();

        GraphicsUtils.enableBlending();

        SpaceTravels3.shapeRenderer.begin();

        if (GameEntityManager.getGameEntities().contains(this.ship))
        {
            SpaceTravels3.shapeRenderer.setProjectionMatrix(SpaceTravels3.gameViewport.getCamera().combined);
            drawApproachSpeedIndicator();
        }

        this.miniMap.update();
        this.miniMap.draw();
        SpaceTravels3.shapeRenderer.end();
        GraphicsUtils.disableBlending();
    }

    private void drawApproachSpeedIndicator()
    {
        if (this.level.getDestinationPlanet() == null)
        {
            return;
        }

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
    public void dispose()
    {
        Constants.General.EVENT_BUS.unregister(this);
        super.dispose();
    }

    @Subscribe
    public void pickupCollected(PickupCollectedEvent pickupCollectedEvent)
    {
        // During the tutorial the hud doesn't show the pickups.
        if (!this.grayPickups.empty())
        {
            Image firstPickup = this.grayPickups.pop();

            firstPickup.setDrawable(this.collectedPickupDrawable);
        }
    }

    @Subscribe
    public void inputTypeChanged(InputTypeChangedEvent inputTypeChangedEvent)
    {
        if (SettingsManager.getSettings().getInputType() == InputType.TOUCH
            || Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            addJoystickOverlay();
        }
        else
        {
            this.joystickOverlayContainer.setActor(null);
        }
    }
}
