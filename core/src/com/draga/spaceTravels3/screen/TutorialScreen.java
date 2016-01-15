package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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

public class TutorialScreen extends Screen
{
    private final float labelsWidth;
    private       Stage stage;

    public TutorialScreen()
    {
        super(true, true);

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);

        this.labelsWidth = this.stage.getWidth() * 0.8f;

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

        table.add(getElemetsTutorial());
        table.row();
        table.add(getGoalTutorial());

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

    private Actor getElemetsTutorial()
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

    private Actor getGoalTutorial()
    {
        Table table = new Table(UIManager.skin);

        table.add("Goal", "large", Color.WHITE);
        table.row();

        String goalText =
            "The goal of the game is to land safely on the destination planet. This will be shown in blue in the minimap, will have an overlay to indicate how fast are you going and the trajectory line will appear blue.";
        Label goalLabel = new Label(goalText, UIManager.skin);
        goalLabel.setWrap(true);
        table
            .add(goalLabel)
            .width(this.labelsWidth);

        return table;
    }

    private Table getShipTutorial()
    {
        Table shipTable = UIManager.getDefaultTable();

        shipTable.add("Your ship");
        shipTable.row();

        shipTable.add(new Image(new Texture(AssMan.getAssList().shipTexture)));
        return shipTable;
    }

    private Table getTrajectoryLineTutorial()
    {
        Table trajectoryLineTable = UIManager.getDefaultTable();
        trajectoryLineTable.add("Trajectory line");
        trajectoryLineTable.row();

        trajectoryLineTable.add(new Image(new Texture(AssMan.getAssList().shipTexture)));
        trajectoryLineTable.row();

        String trajectoryLineText =
            "Shows where you are going accounting for the planets gravity. Turns red if you are going to collide with a planet, blue if the planet is your destination, and green if you are going to collect a pickup.";
        Label trajectoryLineLabel = new Label(trajectoryLineText, UIManager.skin);
        trajectoryLineTable
            .add(trajectoryLineLabel)
            .width(this.labelsWidth);
        trajectoryLineLabel.setWrap(true);
        return trajectoryLineTable;
    }

    private Table getLandingSpeedIndicatorTutorial()
    {
        Table landingSpeedIndicatorTable = UIManager.getDefaultTable();
        landingSpeedIndicatorTable.add("Landing speed indicator");
        landingSpeedIndicatorTable.row();

        landingSpeedIndicatorTable.add(new Image(new Texture(AssMan.getAssList().shipTexture)));
        landingSpeedIndicatorTable.row();

        String landingSpeedIndicatorText =
            "Grows from the center of the planet in color green when your ship speed is within the speed that you can approach the destination planet. Above that speed starts shrinking and becomes red.";
        Label landingSpeedIndicatorLabel = new Label(landingSpeedIndicatorText, UIManager.skin);
        landingSpeedIndicatorTable
            .add(landingSpeedIndicatorLabel)
            .width(this.labelsWidth);
        landingSpeedIndicatorLabel.setWrap(true);
        return landingSpeedIndicatorTable;
    }

    private Table getPickupTutorial()
    {
        Table pickupTable = UIManager.getDefaultTable();
        pickupTable.add("Pickup");
        pickupTable.row();

        pickupTable.add(new Image(new Texture(AssMan.getAssList().pickupTexture)));
        pickupTable.row();

        String pickupText = "Provides "
            + Constants.Game.PICKUP_POINTS
            + " points. Refer to the minimap and the hud to see how many have you collected and how many are in the map.";
        Label pickupLabel = new Label(pickupText, UIManager.skin);
        pickupTable
            .add(pickupLabel)
            .width(this.labelsWidth);
        pickupLabel.setWrap(true);
        return pickupTable;
    }

    private Table getFuelTutorial()
    {
        Table fuelTable = UIManager.getDefaultTable();
        fuelTable.add("Fuel");
        fuelTable.row();

        fuelTable.add(new Image(new Texture(AssMan.getAssList().shipTexture)));
        fuelTable.row();

        String fuelText =
            "Exhaust it and you will be adrift. Each notch represent a unit of fuel. Some maps have infinite fuel.";
        Label fuelLabel = new Label(fuelText, UIManager.skin);
        fuelTable
            .add(fuelLabel)
            .width(this.labelsWidth);
        fuelLabel.setWrap(true);
        return fuelTable;
    }

    private Table getMinimapTutorial()
    {
        Table minimapTable = UIManager.getDefaultTable();
        minimapTable.add("Minimap");
        minimapTable.row();

        minimapTable.add(new Image(new Texture(AssMan.getAssList().shipTexture)));
        minimapTable.row();

        String minimapText =
            "The minimap. The ship is represented by a triangle. Planets with circles in red or blue if it's your destination. Pickups with stars.";
        Label minimapLabel = new Label(minimapText, UIManager.skin);
        minimapTable
            .add(minimapLabel)
            .width(this.labelsWidth);
        minimapLabel.setWrap(true);
        return minimapTable;
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta)
    {
        {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
            {
                ScreenManager.removeScreen(TutorialScreen.this);
            }
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
    }
}
