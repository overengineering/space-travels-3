package com.draga.spaceTravels3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
        this.label1 = getLabel();
        this.label1.setText(
            "mean   ");
        this.label2 = getLabel();
        this.label3 = getLabel();
        this.label4 = getLabel();


        this.stage = new Stage();

        Table table = new Table();
        table.setFillParent(true);
        this.stage.addActor(table);

        table
            .add()
            .expand();

        table.row();
        table
            .add(this.label1)
            .bottom()
            .right();

        table.row();
        table
            .add(this.label2)
            .bottom()
            .right();

        table.row();
        table
            .add(this.label3)
            .bottom()
            .right();

        table.row();
        table
            .add(this.label4)
            .bottom()
            .right();
    }

    private Label getLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = UIManager.skin.getFont("debug");
        labelStyle.background = UIManager.getTiledDrawable(new Color(0, 0, 0, 0.3f));

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
        this.stage.act(delta);
        this.stage.draw();
    }

    private void updateLabel2()
    {
        String message = String.format(
            "Engine :%9f ",
            PhysicsEngine.getStepPerformanceCounter().time.mean.getMean());
        this.label2.setText(message);

    }

    public void updateLabel3()
    {
        String message = String.format(
            "Projection :%9f ",
            PhysicsEngine.getGravityProjectionPerformanceCounter().time.mean.getMean());
        this.label3.setText(message);
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
        this.label4.setText(message);

        if (Gdx.graphics.getFramesPerSecond() < 50)
        {
            this.label4.setColor(Color.RED);
        }
        else if (Gdx.graphics.getFramesPerSecond() < 60)
        {
            this.label4.setColor(Color.YELLOW);
        }
        else
        {
            this.label4.setColor(Color.WHITE);
        }
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
