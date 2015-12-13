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
    private final Label generalLabel;
    private final Label physicEngineLabel;

    private Stage stage;

    public DebugOverlay()
    {
        stage = new Stage();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table
            .add()
            .expand();

        physicEngineLabel = getLabel();
        table.row();
        table
            .add(physicEngineLabel)
            .bottom()
            .right();

        generalLabel = getLabel();
        table.row();
        table
            .add(generalLabel)
            .bottom()
            .right();

        stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private Label getLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = UIManager.skin.getFont("debugFont");
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
        updateGeneralLabel();
        updatePhysicEngineLabel();
        stage.act(delta);
        stage.draw();
    }

    public void updateGeneralLabel()
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
        generalLabel.setText(message);
    }

    public void updatePhysicEngineLabel()
    {
        String message = String.format(
            "Engine update time: %9f | Steps: %d\r\n",
            PhysicsEngine.getUpdateTime(),
            PhysicsEngine.getCurrentSteps());
        physicEngineLabel.setText(message);
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
