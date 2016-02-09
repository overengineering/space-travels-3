package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.InputType;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.event.ShipPlanetCollisionEvent;
import com.draga.spaceTravels3.gameEntity.Pickup;
import com.draga.spaceTravels3.gameEntity.Planet;
import com.draga.spaceTravels3.level.Level;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.manager.level.LevelManager;
import com.draga.spaceTravels3.manager.level.LevelPack;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisablePlanet;
import com.draga.spaceTravels3.physic.PhysicsEngine;
import com.draga.spaceTravels3.tutorial.OrbitAction;
import com.draga.spaceTravels3.tutorial.PhysicsComponentMovementAction;
import com.draga.spaceTravels3.tutorial.PhysicsComponentVelocityAction;
import com.draga.spaceTravels3.tutorial.PickupCollectedAction;
import com.draga.spaceTravels3.ui.BeepingTextButton;
import com.draga.spaceTravels3.ui.Screen;
import com.google.common.eventbus.Subscribe;

public class TutorialScreen extends Screen
{
    private final Level             level;
    private final GameScreen        gameScreen;
    private final Dialog            dialog;
    private final Label             dialogHeader;
    private final Label             dialogMessage;
    private final BeepingTextButton dialogButton;
    private final float             labelsWidth;
    private       Stage             stage;
    private       InputType         originalInputType;
    private       ClickListener     nextTextButtonListener;
    private       Planet            planet;

    public TutorialScreen(Level level, GameScreen gameScreen)
    {
        super(false, false);
        this.level = level;
        this.gameScreen = gameScreen;

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);
        this.labelsWidth = this.stage.getWidth() * 0.8f;


        this.dialog = new Dialog("", UIManager.skin);

        Table table = UIManager.getDefaultTable();
        this.dialog.add(table);

        this.dialogHeader = new Label("", UIManager.skin, "large", Color.WHITE);
        table
            .add(this.dialogHeader)
            .center();
        table.row();

        this.dialogMessage = new Label("", UIManager.skin);
        table
            .add(this.dialogMessage)
            .center()
            .width(this.labelsWidth);
        this.dialogMessage.setWrap(true);
        table.row();

        // TODO: next button skin
        this.dialogButton = new BeepingTextButton("Next", UIManager.skin);
        table
            .add(this.dialogButton)
            .right();

        Constants.General.EVENT_BUS.register(this);

        moveTilt();
    }

    private void moveTilt()
    {
        this.level.startTutorial();
        this.originalInputType = SettingsManager.getSettings().getInputType();

        SettingsManager.getSettings().setInputType(InputType.ACCELEROMETER);

        this.dialogHeader.setText("Movement");

        this.dialogMessage.setText(
            "There are 2 ways of moving the spaceship.\r\n"
                + "Try using the tilt: place the "
                + "device facing up and tilt it slightly where you want to go.\r\n"
                + "The more you tilt the faster the ship will accelerate (and consume fuel).\r\n"
                + "Try to move away using the accelerometer.");

        this.nextTextButtonListener = new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                TutorialScreen.this.dialog.hide();
                TutorialScreen.this.stage.addAction(new PhysicsComponentMovementAction(
                    TutorialScreen.this.level.getShip().physicsComponent,
                    50f)
                {
                    @Override
                    protected void onTriggered()
                    {
                        stopTilt();
                    }
                });
                TutorialScreen.this.level.endTutorial();
            }
        };
        this.dialogButton.addListener(this.nextTextButtonListener);

        this.dialog.show(this.stage);
    }

    private void stopTilt()
    {
        this.level.startTutorial();
        this.dialogMessage.setText("Now try to slow down the ship!");
        this.dialogButton.removeListener(this.nextTextButtonListener);
        this.nextTextButtonListener = new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                TutorialScreen.this.dialog.hide();
                TutorialScreen.this.stage.addAction(new PhysicsComponentVelocityAction(
                    TutorialScreen.this.level.getShip().physicsComponent,
                    1f,
                    false)
                {
                    @Override
                    protected void onTriggered()
                    {
                        moveTouch();
                    }
                });
                TutorialScreen.this.level.endTutorial();
            }
        };
        this.dialogButton.addListener(this.nextTextButtonListener);
        this.dialog.show(this.stage);
    }

    private void moveTouch()
    {
        this.level.startTutorial();
        SettingsManager.getSettings().setInputType(InputType.TOUCH);

        this.dialogMessage.setText("Try using the touch screen: 2 dashed circles will appear "
            + "at the center of the screen. touch between them to make the ship accelerate in "
            + "that direction.\r\n"
            + "The further away you touch from the ship the the faster the ship will accelerate "
            + "(and consume fuel).\r\n"
            + "Move away using the touch screen.");
        this.dialogButton.removeListener(this.nextTextButtonListener);
        this.nextTextButtonListener = new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                TutorialScreen.this.dialog.hide();
                TutorialScreen.this.stage.addAction(new PhysicsComponentMovementAction(
                    TutorialScreen.this.level.getShip().physicsComponent,
                    50f)
                {
                    @Override
                    protected void onTriggered()
                    {
                        stopTouch();
                    }
                });
                TutorialScreen.this.level.endTutorial();
            }
        };
        this.dialogButton.addListener(this.nextTextButtonListener);
        this.dialog.show(this.stage);
    }

    private void stopTouch()
    {
        this.level.startTutorial();
        this.dialogMessage.setText("Now try to slow down the ship!");
        this.dialogButton.removeListener(this.nextTextButtonListener);
        this.nextTextButtonListener = new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                TutorialScreen.this.dialog.hide();
                TutorialScreen.this.stage.addAction(new PhysicsComponentVelocityAction(
                    TutorialScreen.this.level.getShip().physicsComponent,
                    1f,
                    false)
                {
                    @Override
                    protected void onTriggered()
                    {
                        if (TutorialScreen.this.originalInputType != null)
                        {
                            SettingsManager.getSettings()
                                .setInputType(TutorialScreen.this.originalInputType);
                            TutorialScreen.this.originalInputType = null;
                        }
                        pickup();
                    }
                });
                TutorialScreen.this.level.endTutorial();
            }
        };
        this.dialogButton.addListener(this.nextTextButtonListener);
        this.dialog.show(this.stage);
    }

    private void pickup()
    {
        this.level.startTutorial();

        PhysicsComponent shipPhysicsComponent = this.level.getShip().physicsComponent;
        shipPhysicsComponent.getVelocity().setZero();

        final Pickup pickup = new Pickup(
            shipPhysicsComponent.getPosition().x
                + Constants.Visual.VIEWPORT_WIDTH * 0.4f,
            shipPhysicsComponent.getPosition().y,
            AssMan.getGameAssMan().get(AssMan.getAssList().pickupTexture, Texture.class));
        GameEntityManager.addGameEntity(pickup);
        GameEntityManager.update();

        PhysicsEngine.recacheCollisions(shipPhysicsComponent);
        this.level.calculateBounds();

        this.dialogHeader.setText("Pickup");

        this.dialogMessage.setText("Provides " + Constants.Game.PICKUP_POINTS + " points.\r\n"
            + "Collect the pickup!");

        this.dialogButton.removeListener(this.nextTextButtonListener);
        this.nextTextButtonListener = new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                TutorialScreen.this.dialog.hide();
                TutorialScreen.this.stage.addAction(new PickupCollectedAction(pickup)
                {
                    @Override
                    protected void onTriggered()
                    {
                        planet(false);
                    }
                });
                TutorialScreen.this.level.endTutorial();
            }
        };
        this.dialogButton.addListener(this.nextTextButtonListener);
        this.dialog.show(this.stage);
    }

    private void planet(boolean retry)
    {
        this.level.startTutorial();

        final PhysicsComponent shipPhysicsComponent = this.level.getShip().physicsComponent;
        shipPhysicsComponent.getVelocity().setZero();

        LevelPack firstLevelPack = LevelManager.getLevelPacks()
            .get(0);
        SerialisableLevel firstSerialisableLevel = firstLevelPack
            .getSerialisableLevels()
            .get(0);
        SerialisablePlanet firstSerialisablePlanet =
            firstSerialisableLevel.serialisedPlanets.get(0);
        // TODO: dispose this texture
        Texture texture = new Texture(firstSerialisablePlanet.texturePath);
        this.planet = new Planet(
            10000f,
            5f,
            shipPhysicsComponent.getPosition().x
                + Constants.Visual.VIEWPORT_WIDTH * 0.4f,
            shipPhysicsComponent.getPosition().y,
            texture,
            false);
        GameEntityManager.addGameEntity(this.planet);
        GameEntityManager.update();

        PhysicsEngine.recacheCollisions(shipPhysicsComponent);
        this.level.calculateBounds();

        this.dialogHeader.setText("Planet");

        final int numberOfOrbits = 3;
        String text = retry
            ? "You crashed in the planet, try again!\r\n"
            : "Planets attracts you with their gravity.\r\n"
                + "This planet is not your destination and you will lose if you land on it.\r\n";
        text += "Try to make " + numberOfOrbits + " orbits around it!";
        this.dialogMessage.setText(text);

        this.dialogButton.removeListener(this.nextTextButtonListener);
        this.nextTextButtonListener = new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                TutorialScreen.this.dialog.hide();
                TutorialScreen.this.stage.addAction(new OrbitAction(
                    shipPhysicsComponent,
                    TutorialScreen.this.planet.physicsComponent,
                    numberOfOrbits)
                {
                    @Override
                    protected void onTriggered()
                    {
                        end();
                    }
                });
                TutorialScreen.this.level.endTutorial();
            }
        };
        this.dialogButton.addListener(this.nextTextButtonListener);
        this.dialog.show(this.stage);
    }

    private void end()
    {
        this.level.startTutorial();

        this.dialogHeader.setText("Done");

        this.dialogMessage.setText("Good job! You have completed the tutorial!");

        this.dialogButton.removeListener(this.nextTextButtonListener);
        this.nextTextButtonListener = new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ScreenManager.removeScreen(TutorialScreen.this);
                ScreenManager.removeScreen(TutorialScreen.this.gameScreen);
            }
        };
        this.dialogButton.addListener(this.nextTextButtonListener);

        this.dialogButton.setText("Exit");
        this.dialog.show(this.stage);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta)
    {
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
        if (this.originalInputType != null)
        {
            SettingsManager.getSettings().setInputType(this.originalInputType);
        }
        this.stage.dispose();
        Constants.General.EVENT_BUS.unregister(this);
    }

    @Subscribe
    public void shipPlanetCollision(ShipPlanetCollisionEvent shipPlanetCollisionEvent)
    {
        GameEntityManager.removeGameEntity(this.planet);
        planet(true);
    }
}
