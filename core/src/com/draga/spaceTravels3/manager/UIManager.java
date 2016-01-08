package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.manager.asset.AssMan;

public class UIManager
{
    private static final String LOGGING_TAG = UIManager.class.getSimpleName();

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

        BitmapFont largeFont = getBitmapFont(
            AssMan.getAssList().font,
            (int) (Constants.Visual.UI.SQRT_PIXELS * Constants.Visual.UI.LARGE_FONT_SCALE));
        skin.add("large", largeFont);

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

        // Create a button 9 patch
        NinePatch buttonNinePatch = getNinePatch();
        skin.add("button", buttonNinePatch);

        // Create a text button style
        TextButton.TextButtonStyle textButtonStyle = getTextButtonStyle(skin);
        skin.add("default", textButtonStyle);

        // Label style
        Label.LabelStyle labelStyle = getLabelStyle(skin);
        skin.add("default", labelStyle, Label.LabelStyle.class);

        ProgressBar.ProgressBarStyle progressBarStyle = getProgressBarStyle();
        skin.add("default-horizontal", progressBarStyle);

        Slider.SliderStyle sliderStyle = getSliderStyle();
        skin.add("default-horizontal", sliderStyle);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = getScrollPaneStyle();
        skin.add("default", scrollPaneStyle);

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

    private static ProgressBar.ProgressBarStyle getProgressBarStyle()
    {
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = getTiledDrawable(Color.DARK_GRAY);
        progressBarStyle.knobBefore = getTiledDrawable(Color.WHITE);

        float height = Gdx.graphics.getHeight() * 0.03f;

        progressBarStyle.background.setMinHeight(height);
        progressBarStyle.knobBefore.setMinHeight(height);

        return progressBarStyle;
    }

    private static Slider.SliderStyle getSliderStyle()
    {
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = getTiledDrawable(Color.DARK_GRAY);
        sliderStyle.knobBefore = getTiledDrawable(Color.WHITE);

        float height = Gdx.graphics.getHeight() * 0.05f;

        sliderStyle.background.setMinHeight(height);
        sliderStyle.knobBefore.setMinHeight(height);

        return sliderStyle;
    }

    private static ScrollPane.ScrollPaneStyle getScrollPaneStyle()
    {
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();

        scrollPaneStyle.hScrollKnob = getTiledDrawable(Color.WHITE);
        scrollPaneStyle.vScrollKnob = getTiledDrawable(Color.WHITE);
        scrollPaneStyle.hScroll = getTiledDrawable(Color.DARK_GRAY);
        scrollPaneStyle.vScroll = getTiledDrawable(Color.DARK_GRAY);

        float size = Constants.Visual.UI.SQRT_PIXELS * 0.01f;

        scrollPaneStyle.vScrollKnob.setMinWidth(size);
        scrollPaneStyle.vScroll.setMinWidth(size);
        scrollPaneStyle.hScrollKnob.setMinHeight(size);
        scrollPaneStyle.hScroll.setMinHeight(size);

        return scrollPaneStyle;
    }

    public static TiledDrawable getTiledDrawable(Color color)
    {
        int size = 100;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);

        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, size, size);
        TiledDrawable tiledDrawable = new TiledDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap.dispose();

        return tiledDrawable;
    }

    public static ProgressBar getDelimitedProgressBar(float max, float width)
    {
        int delimiterWidth = 1;

        int chunkWidth = Math.round(width / max);
        int height = Math.round((Gdx.graphics.getHeight() / 30f));

        String progressBarStyleName =
            String.format("delimitedProgressBar%d/%d/%d", delimiterWidth, chunkWidth, height);

        if (!skin.has(progressBarStyleName, ProgressBar.ProgressBarStyle.class))
        {
            TiledDrawable backgroundTiledDrawable =
                getDelimitedTiledDrawableChunk(
                    delimiterWidth,
                    chunkWidth,
                    height,
                    Constants.Visual.DELIMITED_PROGRESSBAR_BACKGROUND,
                    Constants.Visual.DELIMITED_PROGRESSBAR_DELIMITER);

            TiledDrawable knobBeforeTiledDrawable =
                getDelimitedTiledDrawableChunk(
                    delimiterWidth,
                    chunkWidth,
                    height,
                    Constants.Visual.DELIMITED_PROGRESSBAR_KNOB_BEFORE,
                    Constants.Visual.DELIMITED_PROGRESSBAR_DELIMITER);

            ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
            progressBarStyle.background = backgroundTiledDrawable;
            progressBarStyle.knobBefore = knobBeforeTiledDrawable;

            skin.add(progressBarStyleName, progressBarStyle);
        }

        ProgressBar progressBar = new ProgressBar(0, max, 0.01f, false, skin, progressBarStyleName);

        return progressBar;
    }

    private static TiledDrawable getDelimitedTiledDrawableChunk(
        int delimiterWidth,
        int width,
        int height, Color backgroundColor,
        Color delimiterColor)
    {
        Pixmap pixmap = new Pixmap(
            width,
            height,
            Pixmap.Format.RGBA8888);

        pixmap.setColor(backgroundColor);
        pixmap.fillRectangle(0, 0, pixmap.getWidth() - delimiterWidth, pixmap.getHeight());

        pixmap.setColor(delimiterColor);
        pixmap.fillRectangle(
            pixmap.getWidth() - delimiterWidth,
            0,
            delimiterWidth,
            pixmap.getHeight());

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        TextureRegion textureRegion = new TextureRegion(texture);

        TiledDrawable tiledDrawable = new TiledDrawable(textureRegion);

        return tiledDrawable;
    }
}
