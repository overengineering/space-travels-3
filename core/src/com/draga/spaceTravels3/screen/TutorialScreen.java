package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.InputType;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.level.Level;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.tutorial.PhysicsComponentMovementAction;
import com.draga.spaceTravels3.tutorial.PhysicsComponentVelocityAction;
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
        this.dialogNextTextButton.addListener(this.nextTextButtonListener);

        this.dialog.show(this.stage);
    }

    private void stopTilt()
    {
        this.level.startTutorial();
        this.dialogMessage.setText("Now try to slow down the ship!");
        this.dialogNextTextButton.removeListener(this.nextTextButtonListener);
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
        this.dialogNextTextButton.addListener(this.nextTextButtonListener);
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
                    protected void onTriggered()
                    {
                        stopTouch();
                    }
                });
                TutorialScreen.this.level.endTutorial();
            }
        };
        this.dialogNextTextButton.addListener(this.nextTextButtonListener);
        this.dialog.show(this.stage);
    }

    private void stopTouch()
    {
        this.level.startTutorial();
        this.dialogMessage.setText("Now try to slow down the ship!");
        this.dialogNextTextButton.removeListener(this.nextTextButtonListener);
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
                        /*moveTouch();*/
                    }
                });
                TutorialScreen.this.level.endTutorial();
            }
        };
        this.dialogNextTextButton.addListener(this.nextTextButtonListener);
        this.dialog.show(this.stage);
    }

    //    private void moveTouch()
    //    {
    //        PhysicsComponent shipPhysicsComponent = this.level.getShip().physicsComponent;
    //        shipPhysicsComponent.getVelocity().setZero();
    //        SettingsManager.getSettings().setInputType(this.originalInputType);
    ///*
    //        Pickup pickup = new Pickup(shipPhysicsComponent.getPosition().x + Constants.Visual.VIEWPORT_WIDTH * 0.4f, shipPhysicsComponent.getPosition().y,
    //            AssMan.getAssList().pickupTexture);
    //        GameEntityManager.addGameEntity(pickup);*/
    //
    //        this.dialogHeader.setText("Pickup");
    //
    //        this.dialogMessage.setText("Provides " + Constants.Game.PICKUP_POINTS + " points.\r\n"
    //            + "Collect the pickup!");
    //    }

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
        SettingsManager.getSettings().setInputType(this.originalInputType);
        this.stage.dispose();
    }
}
