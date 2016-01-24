package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.draga.spaceTravels3.ui.BeepingImageTextButton;
import com.draga.spaceTravels3.ui.Screen;

public class ShareScreen extends Screen
{
    private Stage stage;

    public ShareScreen()
    {
        super(true, false);

        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.overlaySpriteBath);


        this.stage.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (!event.isStopped())
                {
                    ScreenManager.removeScreen(ShareScreen.this);
                }
            }
        });


        Table table = UIManager.addDefaultTableToStage(this.stage);

        table.setBackground(UIManager.getTiledDrawable(Constants.Visual.SCREEN_FADE_COLOUR));
        table.addAction(Actions.sequence(
            Actions.fadeOut(0),
            Actions.fadeIn(Constants.Visual.SCREEN_FADE_DURATION, Interpolation.pow2In)));


        table
            .add("Share", "large", Color.WHITE)
            .row();
        table
            .add(getMessageButton())
            .row();
        table
            .add(getFacebookShareButton())
            .row();
        table
            .add(getBackTextButton())
            .bottom();

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);
    }

    private Actor getMessageButton()
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("email/SMS", UIManager.skin, "message");
        button.addListener(new ClickListener()
                           {
                               @Override
                               public void clicked(InputEvent event, float x, float y)
                               {
                                   SpaceTravels3.services.googleInvite();
                                   event.stop();
                               }
                           }
        );

        return button;
    }

    private Actor getFacebookShareButton()
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Share on Facebook", UIManager.skin, "facebook");
        button.addListener(new ClickListener()
                           {
                               @Override
                               public void clicked(InputEvent event, float x, float y)
                               {
                                   SpaceTravels3.services.facebookShare();
                                   event.stop();
                               }
                           }
        );

        return button;
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            ScreenManager.removeScreen(ShareScreen.this);
        }

        this.stage.getViewport().apply();

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
