package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
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
import com.draga.spaceTravels3.level.Level;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.ui.BeepingTextButton;
import com.draga.spaceTravels3.ui.Screen;

public class TutorialScreen extends Screen
{
    private final Level             level;
    private final GameScreen        gameScreen;
    private final Dialog            dialog;
    private final Label             dialogHeader;
    private final Label             dialogMessage;
    private final BeepingTextButton dialogNextTextButton;
    private final float             labelsWidth;
    private       Stage             stage;
    private       InputType         originalInputType;
    private       ClickListener     nextTextButtonListener;

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

        this.dialogNextTextButton = new BeepingTextButton("Next", UIManager.skin);
        table
            .add(this.dialogNextTextButton)
            .right();


        this.level.startTutorial();
        step1();
    }

    private void step1()
    {
        this.originalInputType = SettingsManager.getSettings().getInputType();

        SettingsManager.getSettings().setInputType(InputType.ACCELEROMETER);

        this.dialogHeader.setText("Movement");

        this.dialogMessage.setText(
            "There are 2 ways of moving the spaceship.\r\n"
                + "Try using the accelerometer: place the "
                + "device facing up and tilt it slightly where you want to go.\r\n"
                + "The more you tilt the faster the ship will accelerate (and consume fuel).\r\n"
                + "Move away using the accelerometer.");

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
                    public boolean act(float delta)
                    {
                        if (isTriggered())
                        {
                            step2();
                            return true;
                        }

                        return false;
                    }
                });
                TutorialScreen.this.level.endTutorial();
            }
        };
        this.dialogNextTextButton.addListener(this.nextTextButtonListener);

        this.dialog.show(this.stage);
    }

    private void step2()
    {
        this.level.getShip().physicsComponent.getVelocity().setZero();
        SettingsManager.getSettings().setInputType(InputType.TOUCH);

        this.dialogMessage.setText("Now try using the touch screen: 2 dashed circles will appear "
            + "at the center of the screen. touch between them to make the ship accelerate in "
            + "that direction.\r\n"
            + "The further away you touch from the ship the the faster the ship will accelerate "
            + "(and consume fuel).\r\n"
            + "Move away using the touch screen.");
        this.dialogNextTextButton.removeListener(this.nextTextButtonListener);
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
                    public boolean act(float delta)
                    {
                        if (isTriggered())
                        {
                            step3();
                            return true;
                        }

                        return false;
                    }
                });
                TutorialScreen.this.level.endTutorial();
            }
        };
        this.dialogNextTextButton.addListener(this.nextTextButtonListener);
        this.dialog.show(this.stage);
    }

    private void step3()
    {
        PhysicsComponent shipPhysicsComponent = this.level.getShip().physicsComponent;
        shipPhysicsComponent.getVelocity().setZero();
        SettingsManager.getSettings().setInputType(this.originalInputType);
/*
        Pickup pickup = new Pickup(shipPhysicsComponent.getPosition().x + Constants.Visual.VIEWPORT_WIDTH * 0.4f, shipPhysicsComponent.getPosition().y,
            AssMan.getAssList().pickupTexture);
        GameEntityManager.addGameEntity(pickup);*/

        this.dialogHeader.setText("Pickup");

        this.dialogMessage.setText("Provides " + Constants.Game.PICKUP_POINTS + " points.\r\n"
            + "Collect the pickup!");
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
            ScreenManager.removeScreen(TutorialScreen.this);
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
        SettingsManager.getSettings().setInputType(this.originalInputType);
        this.stage.dispose();
    }

    private abstract class PhysicsComponentMovementAction extends Action
    {
        private final float            distanceToTrigger;
        private final PhysicsComponent physicsComponent;
        private       Vector2          initialPosition;

        public PhysicsComponentMovementAction(
            PhysicsComponent physicsComponent,
            float distanceToTrigger)
        {
            this.physicsComponent = physicsComponent;
            this.distanceToTrigger = distanceToTrigger;
            this.initialPosition = new Vector2(physicsComponent.getPosition());
        }

        protected boolean isTriggered()
        {
            float distance = this.physicsComponent.getPosition().dst(this.initialPosition);

            return distance >= this.distanceToTrigger;
        }
    }
}
