package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.draga.manager.SettingsManager;
import com.draga.manager.SkinManager;

import java.sql.Timestamp;
import java.util.Date;

public class DebugOverlay implements Screen
{
    private final Label messageLabel;

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

        messageLabel = getMessageLabel();
        table
            .add(messageLabel)
            .bottom();

        stage.setDebugAll(SettingsManager.debugDraw);
    }

    private Label getMessageLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = SkinManager.BasicSkin.getFont("debugFont");
        labelStyle.background =
            SkinManager.BasicSkin.newDrawable("background", new Color(0, 0, 0, 0.3f));

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
        setMessageLabel();
        stage.act(delta);
        stage.draw();
    }

    public void setMessageLabel()
    {
        String formattedJavaHeap = Constants.COMMA_SEPARATED_THOUSANDS_FORMATTER.format(
            Gdx.app.getJavaHeap());
        String formattedNativeHeap =
            Constants.COMMA_SEPARATED_THOUSANDS_FORMATTER.format(Gdx.app.getNativeHeap());
        String message = String.format(
            "FPS : %3d | Java heap : %12s | Java native heap : %12s",
            Gdx.graphics.getFramesPerSecond(),
            formattedJavaHeap,
            formattedNativeHeap);
        messageLabel.setText(message);
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
