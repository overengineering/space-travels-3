package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.ui.BeepingTextButton;
import com.draga.spaceTravels3.ui.Screen;

import java.util.HashMap;

public class TutorialScreen extends Screen
{
    private final float                  labelsWidth;
    private final float                  imageSize;
    private final AssetManager           assMan;
    private       Stage                  stage;
    private       HashMap<String, Image> asyncImages;

    public TutorialScreen()
    {
        super(true, true);

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);

        this.labelsWidth = this.stage.getWidth() * 0.8f;
        this.imageSize = this.stage.getWidth() * 0.1f;

        this.assMan = new AssetManager();
        this.asyncImages = new HashMap<>();

        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Header label.
        table.add("Tutorial", "large", Color.WHITE);
        table.row();

        // Tutorial slides.
        table
            .add(getTutorial())
            .expand()
            .center();
        table.row();

        // Back button.
        table.row();
        table.add(getBackTextButton());

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private Actor getTutorial()
    {
        Table table = UIManager.getDefaultTable();

        table.add(getMovementTutorial());
        table.row();
        table.add(getGoalTutorial());
        table.row();
        table.add(getElementsTutorial());

        ScrollPane scrollPane = new ScrollPane(table, UIManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        return scrollPane;
    }

    private TextButton getBackTextButton()
    {
        TextButton backTextButton = new BeepingTextButton("Back", UIManager.skin);
        backTextButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ScreenManager.removeScreen(TutorialScreen.this);
            }
        });

        return backTextButton;
    }

    private Actor getMovementTutorial()
    {
        Table table = new Table(UIManager.skin);

        table.add("Movement", "large", Color.WHITE);
        table.row();

        String movementText =
            "There are 2 ways of moving your spaceship, these can be changed in the settings.\r\n"
                + "Using the accelerometer you should keep your device facing up and tilt it "
                + "slightly where you want to go.\r\n"
                + "When using the touch screen 2 dashed circles will appear on the screen, touch "
                + "within them to move the spaceship in that direction. The further away you touch from "
                + "the Spaceship the greater the thrust produced (and fuel consumed).\r\n"
                + "Being in space nothing but the planets' gravity will slow you down! Use the "
                + "inertia of the spaceship and the gravity of the planets in your favor.";

        Label movementLabel = new Label(movementText, UIManager.skin);
        table
            .add(movementLabel)
            .width(this.labelsWidth);
        movementLabel.setWrap(true);

        return table;
    }

    private Actor getGoalTutorial()
    {
        Table table = new Table(UIManager.skin);

        table.add("Goal", "large", Color.WHITE);
        table.row();

        String goalText =
            "The goal is to land safely on the destination planet. This will be shown in blue in "
                + "the minimap and will have an overlay to indicate how fast are you going .The "
                + "trajectory line colliding with it will appear blue.";
        Label goalLabel = new Label(goalText, UIManager.skin);
        goalLabel.setWrap(true);
        table
            .add(goalLabel)
            .width(this.labelsWidth);

        return table;
    }

    private Actor getElementsTutorial()
    {
        Table table = new Table();

        table.add(new Label("Elements", UIManager.skin, "large", Color.WHITE));
        table.row();

        table.add(getShipTutorial());
        table.row();

        table.add(getTrajectoryLineTutorial());
        table.row();

        table.add(getLandingSpeedIndicatorTutorial());
        table.row();

        table.add(getPickupTutorial());
        table.row();

        table.add(getFuelTutorial());
        table.row();

        table.add(getMinimapTutorial());

        return table;
    }

    private Table getShipTutorial()
    {
        Table table = UIManager.getDefaultTable();

        table.add("Your ship");
        table.row();

        Image shipImage = loadTextureAsync(AssMan.getAssList().shipTexture, this.assMan);
        table
            .add(shipImage)
            .size(this.imageSize);

        return table;
    }

    private Table getTrajectoryLineTutorial()
    {
        Table table = UIManager.getDefaultTable();
        table.add("Trajectory line");
        table.row();

        String trajectoryLineText =
            "Shows where the ship is heading accounting for the planets gravity. Changes color "
                + "depending on what it's going to collide with:";
        Label trajectoryLineLabel = new Label(trajectoryLineText, UIManager.skin);
        table
            .add(trajectoryLineLabel)
            .width(this.labelsWidth);
        trajectoryLineLabel.setWrap(true);
        table.row();

        table.add("No collisions", "default", Constants.Visual.HUD.TrajectoryLine.COLOR_NEUTRAL);
        table.row();
        table.add(
            "Destination planet",
            "default",
            Constants.Visual.HUD.TrajectoryLine.COLOR_PLANET_DESTINATION);
        table.row();
        table.add("Wrong planet", "default", Constants.Visual.HUD.TrajectoryLine.COLOR_PLANET_LOSE);
        table.row();
        table.add("Pickup", "default", Constants.Visual.HUD.TrajectoryLine.COLOR_PICKUP);

        return table;
    }

    private Table getLandingSpeedIndicatorTutorial()
    {
        Table table = UIManager.getDefaultTable();
        table.add("Landing speed indicator");
        table.row();

        Image aboveLandingSpeedImage =
            loadTextureAsync(AssMan.getAssList().tutorialAboveLandingSpeedTexture, this.assMan);
        Image belowLandingSpeedImage =
            loadTextureAsync(AssMan.getAssList().tutorialBelowLandingSpeedTexture, this.assMan);

        Table imageTable = new Table();
        imageTable
            .add(belowLandingSpeedImage)
            .size(this.imageSize);
        imageTable
            .add()
            .size(this.imageSize);
        imageTable
            .add(aboveLandingSpeedImage)
            .size(this.imageSize);
        table.add(imageTable);
        table.row();

        String landingSpeedIndicatorText =
            "Grows from the center of the planet in color green when your ship speed is within the "
                + "speed that you can approach the destination planet. Above that speed starts "
                + "shrinking and becomes red. Remember that the destination planet's gravity will "
                + "pull your ship towards it the closer you are.";
        Label landingSpeedIndicatorLabel = new Label(landingSpeedIndicatorText, UIManager.skin);
        table
            .add(landingSpeedIndicatorLabel)
            .width(this.labelsWidth);
        landingSpeedIndicatorLabel.setWrap(true);

        return table;
    }

    private Table getPickupTutorial()
    {
        Table table = UIManager.getDefaultTable();
        table.add("Pickup");
        table.row();

        Image pickupImage =
            loadTextureAsync(AssMan.getAssList().tutorialPickupTexture, this.assMan);
        table
            .add(pickupImage)
            .size(this.imageSize);
        table.row();

        String pickupText = "Provides "
            + Constants.Game.PICKUP_POINTS
            + " points. Refer to the minimap and the hud to see how many have you collected and how"
            + " many are in the map.";
        Label pickupLabel = new Label(pickupText, UIManager.skin);
        table
            .add(pickupLabel)
            .width(this.labelsWidth);
        pickupLabel.setWrap(true);

        return table;
    }

    private Table getFuelTutorial()
    {
        Table table = UIManager.getDefaultTable();
        table.add("Fuel");
        table.row();

        ProgressBar delimitedProgressBar =
            UIManager.getDelimitedProgressBar(3f, this.stage.getWidth() * 0.25f);

        table
            .add(delimitedProgressBar)
            .width(this.stage.getWidth() * 0.25f);
        table.row();

        String fuelText =
            "Each notch represent a unit of fuel, exhaust it and you will be adrift! Fuel is "
                + "precious so you will be rewarded for how much you can save, up to "
                + Constants.Game.FUEL_POINTS + " points. Some maps have infinite fuel.";
        Label fuelLabel = new Label(fuelText, UIManager.skin);
        table
            .add(fuelLabel)
            .width(this.labelsWidth);
        fuelLabel.setWrap(true);

        return table;
    }

    private Table getMinimapTutorial()
    {
        Table table = UIManager.getDefaultTable();
        table.add("Minimap");
        table.row();

        Image minimapImage =
            loadTextureAsync(AssMan.getAssList().tutorialMinimapTexture, this.assMan);
        table
            .add(minimapImage)
            .height(this.imageSize);
        table.row();

        String minimapText =
            "The ship is represented by a triangle. Planets with circles in red or blue if it's "
                + "your destination. Pickups with stars.";
        Label minimapLabel = new Label(minimapText, UIManager.skin);
        table
            .add(minimapLabel)
            .width(this.labelsWidth);
        minimapLabel.setWrap(true);

        return table;
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta)
    {
        loadAsyncImages(this.assMan);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            ScreenManager.removeScreen(this);
        }

        this.stage.getViewport().apply();
        this.stage.act(delta);
        this.stage.draw();
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
        this.stage.dispose();
        this.assMan.dispose();
    }
}
