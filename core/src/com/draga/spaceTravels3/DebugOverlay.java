package com.draga.spaceTravels3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.physic.PhysicsEngine;

public class DebugOverlay implements Screen
{
    private final Label label1;
    private final Label label2;
    private final Label label3;
    private final Label label4;

    private Stage stage;

    public DebugOverlay()
    {
        label1 = getLabel();
        label1.setText(
            "Time    mean  |   std   |   min   |   max   ");
        label2 = getLabel();
        label3 = getLabel();
        label4 = getLabel();


        stage = new Stage();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table
            .add()
            .expand();

        table.row();
        table
            .add(label1)
            .bottom()
            .right();

        table.row();
        table
            .add(label2)
            .bottom()
            .right();

        table.row();
        table
            .add(label3)
            .bottom()
            .right();

        table.row();
        table
            .add(label4)
            .bottom()
            .right();

        stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private Label getLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = UIManager.skin.getFont("debug");
        labelStyle.background =
            UIManager.skin.newDrawable("background", new Color(0, 0, 0, 0.3f));

        Label scoreLabel = new Label("", labelStyle);

        return scoreLabel;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        updateLabel2();
        updateLabel3();
        updateLabel4();
        stage.act(delta);
        stage.draw();
    }

    private void updateLabel2()
    {
        String message = String.format(
            "Engine : %9f|%9f|%9f|%9f",
            PhysicsEngine.getStepPerformanceCounter().time.mean.getMean(),
            PhysicsEngine.getStepPerformanceCounter().time.mean.standardDeviation(),
            PhysicsEngine.getStepPerformanceCounter().time.min,
            PhysicsEngine.getStepPerformanceCounter().time.max);
        label2.setText(message);

    }

    public void updateLabel4()
    {
        String formattedJavaHeap = Constants.General.COMMA_SEPARATED_THOUSANDS_FORMATTER.format(
            Gdx.app.getJavaHeap());
        String formattedNativeHeap =
            Constants.General.COMMA_SEPARATED_THOUSANDS_FORMATTER.format(Gdx.app.getNativeHeap());
        String message = String.format(
            "FPS : %3d | Java heap : %12s | Java native heap : %12s",
            Gdx.graphics.getFramesPerSecond(),
            formattedJavaHeap,
            formattedNativeHeap);
        label4.setText(message);
    }

    public void updateLabel3()
    {
        String message = String.format(
            "Projection : %9f|%9f|%9f|%9f",
            PhysicsEngine.getGravityProjectionPerformanceCounter().time.mean.getMean(),
            PhysicsEngine.getGravityProjectionPerformanceCounter().time.mean.standardDeviation(),
            PhysicsEngine.getGravityProjectionPerformanceCounter().time.min,
            PhysicsEngine.getGravityProjectionPerformanceCounter().time.max);
        label3.setText(message);
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
        stage.dispose();
    }
}
