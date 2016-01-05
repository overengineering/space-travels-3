package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.manager.asset.AssMan;

public class UIManager
{
    public static Skin skin;

    public static Table addDefaultTableToStage(Stage stage)
    {
        Table table = getDefaultTable();
        table.setFillParent(true);
        stage.addActor(table);

        return table;
    }

    public static Table getDefaultTable()
    {
        Table table = new Table();
        table.defaults().pad(Constants.Visual.UI.BUTTON_PADDING);
        table.pad(Constants.Visual.UI.SQRT_PIXELS / 50f);

        return table;
    }

    public static void dispose()
    {
        skin.dispose();
    }

    public static void create()
    {
        skin = getSkin();
    }

    private static Skin getSkin()
    {
        Skin skin = new Skin();

        BitmapFont defaultFont = getBitmapFont(
            AssMan.getAssList().font,
            (int) (Constants.Visual.UI.SQRT_PIXELS * Constants.Visual.UI.FONT_SCALE));
        skin.add("default", defaultFont);

        if (Constants.General.IS_DEBUGGING)
        {
            BitmapFont debugFont = getBitmapFont(
                AssMan.getAssList().debugFont,
                (int) (Constants.Visual.UI.SQRT_PIXELS * Constants.Visual.UI.DEBUG_FONT_SCALE));
            skin.add("debug", debugFont);
        }

        // Create a texture
        Pixmap pixmap = getTexture();
        skin.add("background", new Texture(pixmap));

        // Create a button 9 patch
        NinePatch buttonNinePatch = getNinePatch();
        skin.add("button", buttonNinePatch);

        // Create a text button style
        TextButton.TextButtonStyle textButtonStyle = getTextButtonStyle(skin);
        skin.add("default", textButtonStyle);

        // Label style
        Label.LabelStyle labelStyle = getLabelStyle(skin);
        skin.add("default", labelStyle, Label.LabelStyle.class);

        // Progress bar texture
        Pixmap progressBarPixmap = getProgressBarTexture();
        skin.add("progressbar", new Texture(progressBarPixmap));

        // Progress bar texture
        Pixmap sliderPixmap = getSliderTexture();
        skin.add("slider", new Texture(sliderPixmap));

        ProgressBar.ProgressBarStyle progressBarStyle = getProgressBarStyle(skin);
        skin.add("default-horizontal", progressBarStyle);

        Slider.SliderStyle sliderStyle = getSliderStyle(skin);
        skin.add("default-horizontal", sliderStyle);

        return skin;
    }

    private static BitmapFont getBitmapFont(String path, int size)
    {
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size =
            size;
        BitmapFont bitmapFont = generator.generateFont(parameter);

        return bitmapFont;
    }

    private static Pixmap getTexture()
    {
        Pixmap pixmap = new Pixmap(0, 0, Pixmap.Format.RGB888);
        pixmap.setColor(Color.CLEAR);
        pixmap.fill();
        return pixmap;
    }

    private static NinePatch getNinePatch()
    {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("button/button.pack"));
        return textureAtlas.createPatch("button");
    }

    private static TextButton.TextButtonStyle getTextButtonStyle(Skin skin)
    {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.checkedFontColor = Color.GREEN;
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.down = skin.getDrawable("button");
        textButtonStyle.up = skin.getDrawable("button");
        return textButtonStyle;
    }

    private static Label.LabelStyle getLabelStyle(Skin skin)
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        return labelStyle;
    }

    private static Pixmap getProgressBarTexture()
    {
        Pixmap progressBarPixmap =
            new Pixmap(1, (int) (Gdx.graphics.getHeight() / 30f), Pixmap.Format.RGBA8888);
        progressBarPixmap.setColor(Color.WHITE);
        progressBarPixmap.fill();
        return progressBarPixmap;
    }

    private static Pixmap getSliderTexture()
    {
        Pixmap progressBarPixmap =
            new Pixmap(1, (int) (Gdx.graphics.getHeight() / 15f), Pixmap.Format.RGBA8888);
        progressBarPixmap.setColor(Color.WHITE);
        progressBarPixmap.fill();
        return progressBarPixmap;
    }

    private static ProgressBar.ProgressBarStyle getProgressBarStyle(Skin skin)
    {
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle(
            skin.newDrawable("progressbar", Color.DARK_GRAY), null);
        progressBarStyle.knobBefore = skin.newDrawable("progressbar", Color.WHITE);

        return progressBarStyle;
    }

    private static Slider.SliderStyle getSliderStyle(Skin skin)
    {
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.newDrawable("slider", Color.DARK_GRAY);
        sliderStyle.knobBefore = skin.newDrawable("slider", Color.LIGHT_GRAY);
        sliderStyle.knob = skin.newDrawable("slider", Color.WHITE);

        return sliderStyle;
    }

    public static ProgressBar getDelimitedProgressBar(float max, float width)
    {
        Pixmap pixmap = new Pixmap(
            Math.round(width / max),
            Math.round((Gdx.graphics.getHeight() / 30f)),
            Pixmap.Format.RGBA8888);

        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.fillRectangle(0, 0, pixmap.getWidth()-1, pixmap.getHeight());

        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(pixmap.getWidth() - 1, 0, 1, pixmap.getHeight());

        Texture texture = new Texture(pixmap);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        Sprite sprite = new Sprite(texture);
        sprite.setRegion(0f, 0f, /*pixmap.getWidth() / width*/4f, 1f);

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = new SpriteDrawable(sprite);

        progressBarStyle.knob = skin.newDrawable("progressbar", Color.BLUE);

        ProgressBar progressBar = new ProgressBar(0, max, 0.01f, false, progressBarStyle);

        return progressBar;
    }
}
