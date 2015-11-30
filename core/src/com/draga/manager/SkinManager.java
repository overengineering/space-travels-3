package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class SkinManager
{
    private static final float FONT_SCALE       = 0.05f;
    private static final float DEBUG_FONT_SCALE = 0.02f;

    public static Skin skin = getSkin();

    private static Skin getSkin()
    {
        Skin skin = new Skin();

        BitmapFont defaultFont = getBitmapFont();
        skin.add("default", defaultFont);

        BitmapFont debugFont = getDebugFont();
        skin.add("debugFont", debugFont);

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

    private static BitmapFont getBitmapFont()
    {
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("font/Akashi.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size =
            (int) (Math.sqrt(Gdx.graphics.getWidth() * Gdx.graphics.getHeight()) * FONT_SCALE);
        BitmapFont bitmapFont = generator.generateFont(parameter);

        return bitmapFont;
    }

    private static BitmapFont getDebugFont()
    {
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("font/DroidSansMono.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size =
            (int) (
                Math.sqrt(Gdx.graphics.getWidth() * Gdx.graphics.getHeight())
                    * DEBUG_FONT_SCALE);
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
}
