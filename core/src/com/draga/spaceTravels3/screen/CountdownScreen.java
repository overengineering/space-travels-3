package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Pools;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.event.CountdownFinishedEvent;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;

public class CountdownScreen extends com.draga.spaceTravels3.ui.Screen
{
    private Stage stage;
    private Label timerLabel;
    private float secondsRemaining;
    private boolean countdownFinished = false;

    public CountdownScreen()
    {
        super(true, false);
        this.secondsRemaining = Constants.Game.COUNTDOWN_SECONDS;

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);

        Table table = new Table();
        this.stage.addActor(table);
        table.setFillParent(true);

        this.timerLabel = getTimerLabel();
        table
            .add(this.timerLabel)
            .center();

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
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
        if (this.countdownFinished)
        {
            return;
        }

        this.secondsRemaining -= delta;
        if (this.secondsRemaining <= 0)
        {
            this.secondsRemaining = 0;
            this.countdownFinished = true;
            CountdownFinishedEvent countdownFinishedEvent =
                Pools.obtain(CountdownFinishedEvent.class);
            Constants.General.EVENT_BUS.post(countdownFinishedEvent);
            Pools.free(countdownFinishedEvent);
            ScreenManager.removeScreen(this);
            return;
        }

        this.timerLabel.setText(getLabelText());

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
        this.stage.dispose();
    }
}
