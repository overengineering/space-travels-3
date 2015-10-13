package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.google.common.eventbus.Subscribe;

public class Hud implements Screen
{
    public static final float WORLD_HEIGHT = 1000f;
    public static final float WORLD_WIDTH  = 1000f;
    private final FreeTypeFontGenerator freeTypeFontGenerator;
    private final BitmapFont            pDark24Font;
    private       Stage                 stage;
    private       Label                 fuelLabel;

    public Hud()
    {
        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/pdark.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        pDark24Font = freeTypeFontGenerator.generateFont(parameter);

        StretchViewport viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT);
        stage = new Stage(viewport);

        fuelLabel = getFuelLabel();
        stage.addActor(fuelLabel);

        Constants.EVENT_BUS.register(this);
    }

    public Label getFuelLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = pDark24Font;

        Label fuelLabel = new Label(null, labelStyle);
        float height = pDark24Font.getLineHeight() * 2;
        fuelLabel.setHeight(height);
        fuelLabel.setPosition(0, WORLD_HEIGHT - height);

        return fuelLabel;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
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

    }

    @Subscribe
    public void FuelChanged(FuelChangeEvent fuelChangeEvent)
    {
        updateFuelLabel(fuelChangeEvent.fuel);
    }

    private void updateFuelLabel(float fuel)
    {
        String label = String.format("%.2f of %.2f", fuel, Constants.MAX_FUEL);
        fuelLabel.setText(label);
    }
}
