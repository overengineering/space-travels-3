package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.ui.Screen;

public class GuideScreen extends Screen
{
    private final float                  labelsWidth;
    private final float                  imageSize;

    public GuideScreen()
    {
        super(true, true);

        this.labelsWidth = this.stage.getWidth() * 0.8f;
        this.imageSize = this.stage.getWidth() * 0.1f;

        Table table = UIManager.addDefaultTableToStage(this.stage);

        // Header label.
        table.add("Tutorial", "large", Color.WHITE);
        table.row();

        // Tutorial slides.
        table
            .add(getGuide())
            .expand()
            .center();
        table.row();

        // Back button.
        table
            .add(getBackButton())
            .height(this.buttonHeight);
    }

    private Actor getGuide()
    {
        Table table = UIManager.getDefaultTable();

        table.add(getMovementGuide());
        table.row();
        table.add(getGoalGuide());
        table.row();
        table.add(getElementsGuide());

        ScrollPane scrollPane = new ScrollPane(table, UIManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        return scrollPane;
    }

    private Actor getMovementGuide()
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
                + "Being in space, nothing but the planets' gravity will slow you down! Use the "
                + "inertia of the spaceship and the gravity of the planets in your favor.";

        Label movementLabel = new Label(movementText, UIManager.skin);
        table
            .add(movementLabel)
            .width(this.labelsWidth);
        movementLabel.setWrap(true);

        return table;
    }

    private Actor getGoalGuide()
    {
        Table table = new Table(UIManager.skin);

        table.add("Goal", "large", Color.WHITE);
        table.row();

        String goalText =
            "The goal is to land safely on the destination planet. This will be shown in blue in "
                + "the minimap and will have an overlay to indicate how fast are you going. The "
                + "trajectory line colliding with it will appear blue.";
        Label goalLabel = new Label(goalText, UIManager.skin);
        goalLabel.setWrap(true);
        table
            .add(goalLabel)
            .width(this.labelsWidth);

        return table;
    }

    private Actor getElementsGuide()
    {
        Table table = new Table();

        table.add(new Label("Elements", UIManager.skin, "large", Color.WHITE));
        table.row();

        table.add(getShipGuide());
        table.row();

        table.add(getTrajectoryLineGuide());
        table.row();

        table.add(getLandingSpeedIndicatorGuide());
        table.row();

        table.add(getPickupGuide());
        table.row();

        table.add(getFuelGuide());
        table.row();

        table.add(getMinimapGuide());

        return table;
    }

    private Table getShipGuide()
    {
        Table table = UIManager.getDefaultTable();

        table.add("Your ship");
        table.row();

        Image shipImage = loadTextureAsync(AssMan.getAssList().shipTexture);
        table
            .add(shipImage)
            .size(this.imageSize);

        return table;
    }

    private Table getTrajectoryLineGuide()
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

    private Table getLandingSpeedIndicatorGuide()
    {
        Table table = UIManager.getDefaultTable();
        table.add("Landing speed indicator");
        table.row();

        Image aboveLandingSpeedImage =
            loadTextureAsync(AssMan.getAssList().guideAboveLandingSpeedTexture);
        Image belowLandingSpeedImage =
            loadTextureAsync(AssMan.getAssList().guideBelowLandingSpeedTexture);

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
                + "speed that you can approach the destination planet. Above that speed it starts "
                + "shrinking and becomes red. Remember that the destination planet's gravity will "
                + "pull your ship more the closer you are.";
        Label landingSpeedIndicatorLabel = new Label(landingSpeedIndicatorText, UIManager.skin);
        table
            .add(landingSpeedIndicatorLabel)
            .width(this.labelsWidth);
        landingSpeedIndicatorLabel.setWrap(true);

        return table;
    }

    private Table getPickupGuide()
    {
        Table table = UIManager.getDefaultTable();
        table.add("Pickup");
        table.row();

        Image pickupImage =
            loadTextureAsync(AssMan.getAssList().guidePickupTexture);
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

    private Table getFuelGuide()
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

    private Table getMinimapGuide()
    {
        Table table = UIManager.getDefaultTable();
        table.add("Minimap");
        table.row();

        Image minimapImage =
            loadTextureAsync(AssMan.getAssList().guideMinimapTexture);
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
        Gdx.input.setInputProcessor(new InputMultiplexer(this.stage, getBackInputAdapter()));
    }
}
