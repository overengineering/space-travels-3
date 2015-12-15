package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Pools;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.event.CountdownFinishedEvent;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;

public class CountdownScreen implements Screen
{
    private Stage stage;
    private Label timerLabel;
    private float secondsRemaining;
    private boolean countdownFinished = false;

    public CountdownScreen()
    {
        this.secondsRemaining = Constants.Game.COUNTDOWN_SECONDS;

        this.stage = new Stage();

        Table table = new Table();
        stage.addActor(table);
        table.setFillParent(true);

        timerLabel = getTimerLabel();
        table
            .add(timerLabel)
            .center();

        stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private Label getTimerLabel()
    {
        Label.LabelStyle labelStyle = UIManager.skin.get(Label.LabelStyle.class);
        Label label = new Label(getLabelText(), labelStyle);
        return label;
    }

    private String getLabelText()
    {
        return String.format("%.1f", this.secondsRemaining);
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
            secondsRemaining = 0;
            countdownFinished = true;
            CountdownFinishedEvent countdownFinishedEvent =
                Pools.obtain(CountdownFinishedEvent.class);
            Constants.General.EVENT_BUS.post(countdownFinishedEvent);
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
