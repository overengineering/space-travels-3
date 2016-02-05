package com.draga.spaceTravels3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.level.Level;
import com.draga.spaceTravels3.manager.ScreenManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.UIManager;
import com.draga.spaceTravels3.ui.BeepingImageTextButton;
import com.draga.spaceTravels3.ui.Screen;

public abstract class IngameMenuScreen extends Screen
{
    public static String s = "gTAKsCqx0NeZVO9igYzMNjolg61Y6KLwLQaulfOZzuI2WLhPNr*mAmEy3T%VP08ZzWILHaHhKDHXeGP";

    protected final Level  level;
    protected final Cell   centreCell;
    private final   Stage  stage;
    protected       Screen gameScreen;

    public IngameMenuScreen(Screen gameScreen, Level level)
    {
        super(true, false);
        this.gameScreen = gameScreen;
        this.level = level;
        this.stage = new Stage(SpaceTravels3.menuViewport, SpaceTravels3.spriteBatch);

        Table table = UIManager.addDefaultTableToStage(this.stage);

        table.setBackground(UIManager.getTiledDrawable(Constants.Visual.SCREEN_FADE_COLOUR));
        table.addAction(Actions.sequence(
            Actions.fadeOut(0),
            Actions.fadeIn(Constants.Visual.SCREEN_FADE_DURATION, Interpolation.pow2In)));

        // Header label.
        Actor headerLabel = getHeaderLabel();
        table.add(headerLabel);
        table.row();

        // Gap between header and rest.
        table
            .add()
            .expand();
        table.row();

        this.centreCell = table.add();
        table.row();

        // Retry button.
        table.add(getRetryButton());
        table.row();

        // Main menu button.
        table.add(getMainMenuTextButton());
        table.row();

        // Gap between the centre and the end of the screen.
        table
            .add()
            .expand();

        this.stage.setDebugAll(SettingsManager.getDebugSettings().debugDraw);

    }

    public Actor getHeaderLabel()
    {
        Table table = new Table();

        Label label = new Label(this.level.getName() + " ", UIManager.skin, "large", Color.WHITE);
        table.add(label);

        Image headerImage =
            new Image(this.level.getDestinationPlanet().graphicComponent.getTexture());

        table
            .add(headerImage)
            .size(label.getHeight());

        table.add(new Label(" " + this.level.getDifficulty(), UIManager.skin));

        return table;
    }

    public Actor getRetryButton()
    {
        BeepingImageTextButton button =
            new BeepingImageTextButton("Try Again?", UIManager.skin, "retry");

        button.addListener(
            new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    Retry();
                }
            });

        return button;
    }

    protected Actor getMainMenuTextButton()
    {
        BeepingImageTextButton
            button = new BeepingImageTextButton("Main menu", UIManager.skin, "exit");
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ScreenManager.removeScreen(IngameMenuScreen.this);
                ScreenManager.removeScreen(IngameMenuScreen.this.gameScreen);
            }
        });

        return button;
    }

    private void Retry()
    {
        ScreenManager.removeScreen(this);
        ScreenManager.removeScreen(this.gameScreen);
        ScreenManager.addScreen(new LoadingScreen(this.level.getId(), this.level.getDifficulty()));
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
            ScreenManager.removeScreen(IngameMenuScreen.this);
            ScreenManager.removeScreen(IngameMenuScreen.this.gameScreen);
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            Retry();
            return;
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
