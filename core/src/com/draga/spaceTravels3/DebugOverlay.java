package com.draga.spaceTravels3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.physic.PhysicsEngine;

public class DebugOverlay implements Screen
{
    private final Label label;

    private Stage stage;

    public DebugOverlay()
    {
        this.label = getLabel();

        this.stage = new Stage();

        Table table = new Table();
        table.setFillParent(true);
        this.stage.addActor(table);

        table
            .add(this.label)
            .expand()
            .bottom()
            .right();
    }

    private Label getLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = UIManager.skin.getFont("debug");

        Label label = new Label("", labelStyle);
        label.setAlignment(Align.right);

        return label;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        updateLabel();

        this.stage.act(delta);
        this.stage.draw();
    }

    private void updateLabel()
    {
        String formattedJavaHeap = Constants.General.COMMA_SEPARATED_THOUSANDS_FORMATTER.format(
            Gdx.app.getJavaHeap() / 1024);
        String formattedNativeHeap = Constants.General.COMMA_SEPARATED_THOUSANDS_FORMATTER.format(
            Gdx.app.getNativeHeap() / 1024);

        String message = String.format(
            "Load:%3f\r\n"
                + "Engine:%9f\r\n"
                + "Projection:%9f\r\n"
                + "FPS:%3d|Heap:%7s|Nat. heap:%7s",
            SpaceTravels3.getPerformanceCounter().load.latest,
            PhysicsEngine.getStepPerformanceCounter().time.mean.getMean(),
            PhysicsEngine.getGravityProjectionPerformanceCounter().time.mean.getMean(),
            Gdx.graphics.getFramesPerSecond(),
            formattedJavaHeap,
            formattedNativeHeap);

        this.label.setText(message);

        if (Gdx.graphics.getFramesPerSecond() < 45)
        {
            this.label.setColor(Color.RED);
        }
        else if (Gdx.graphics.getFramesPerSecond() < 55)
        {
            this.label.setColor(Color.YELLOW);
        }
        else
        {
            this.label.setColor(Color.WHITE);
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
