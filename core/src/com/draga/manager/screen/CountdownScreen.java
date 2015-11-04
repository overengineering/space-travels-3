package com.draga.manager.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Pools;
import com.draga.Constants;
import com.draga.event.CountdownFinishedEvent;
import com.draga.manager.asset.FontManager;

public class CountdownScreen implements Screen
{
    private Stage   stage;
    private Label   timerLabel;
    private float   secondsRemaining;
    private boolean countdownFinished = false;

    public CountdownScreen()
    {
        this.secondsRemaining = Constants.COUNTDOWN_SECONDS;

        this.stage = new Stage();

        Table table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
        table.pad(((stage.getHeight() + stage.getWidth()) / 2f) / 50f);
        timerLabel = getTimerLabel();
        table
            .add(timerLabel)
            .center();

        stage.setDebugAll(Constants.DEBUG_DRAW);
    }

    private Label getTimerLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle(FontManager.getBigFont(), Color.WHITE);
        Label label = new Label(getLabelText(), labelStyle);
        return label;
    }

    private String getLabelText()
    {
        return String.format("%.1f", this.secondsRemaining);
    }

    public float getSecondsRemaining()
    {
        return secondsRemaining;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        if (countdownFinished)
        {
            return;
        }

        this.secondsRemaining -= delta;
        if (secondsRemaining <= 0)
        {
            countdownFinished = true;
            CountdownFinishedEvent countdownFinishedEvent =
                Pools.obtain(CountdownFinishedEvent.class);
            Constants.EVENT_BUS.post(countdownFinishedEvent);
            Pools.free(countdownFinishedEvent);
            return;
        }
        this.timerLabel.setText(getLabelText());
        stage.act(delta);
        stage.draw();
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
