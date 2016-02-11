package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.event.CountdownFinishedEvent;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.ui.Screen;

public class CountdownScreen extends Screen
{
    private Label timerLabel;
    private float secondsRemaining;
    private boolean countdownFinished = false;

    public CountdownScreen()
    {
        super(true, false);
        this.secondsRemaining = Constants.Game.COUNTDOWN_SECONDS;

        Table table = new Table();
        this.stage.addActor(table);
        table.setFillParent(true);

        this.timerLabel = getTimerLabel();
        table
            .add(this.timerLabel)
            .center();
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
            Constants.General.EVENT_BUS.post(new CountdownFinishedEvent());
            ScreenManager.removeScreen(this);
            return;
        }

        this.timerLabel.setText(getLabelText());

        super.render(delta);
    }
}
