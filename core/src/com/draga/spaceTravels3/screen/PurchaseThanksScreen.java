package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.ui.Screen;

public class PurchaseThanksScreen extends Screen
{
    public PurchaseThanksScreen()
    {
        super(true, false);

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);

        // If the stage is clicked then close this screen, unless the event was stopped.
        this.stage.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (!event.isStopped())
                {
                    ScreenManager.removeScreen(PurchaseThanksScreen.this);
                }
            }
        });

        final Table table = UIManager.addDefaultTableToStage(this.stage);

        // If the table is clicked stop the event propagating to the stage.
        table.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                event.stop();
            }
        });

        table.setBackground(UIManager.getTiledDrawable(Constants.Visual.LIGHT_DARK));
        table.addAction(Actions.sequence(
            Actions.fadeOut(0),
            Actions.fadeIn(Constants.Visual.SCREEN_FADE_DURATION, Interpolation.pow2In)));


        table
            .add("Thanks", "large")
            .row();
        table
            .add("Thanks for your purchase, hope you'll enjoy it!")
            .row();
        table
            .add(getBackButton())
            .bottom();

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(new InputMultiplexer(this.stage, getBackInputAdapter()));
    }
}
