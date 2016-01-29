package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.manager.asset.AssMan;

public abstract class UIManager
{
    private static final String LOGGING_TAG = UIManager.class.getSimpleName();

    private static final Color CHECKED_COLOR = Color.GREEN;

    public static Skin skin;

    private UIManager()
    {
    }

    public static Table addDefaultTableToStage(Stage stage)
    {
        Table table = getDefaultTable();
        table.setFillParent(true);
        stage.addActor(table);

        return table;
    }

    public static Table getDefaultTable()
    {
        Table table = new Table(skin);
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

        skin.add("button", getNinePatch());

        skin.add("default", getTextButtonStyle(skin));

        skin.add("default", getImageTextButtonStyle(skin));
        skin.add("settings", getImageTextButtonStyle(skin, AssMan.getAssList().iconSettings));
        skin.add(
            "achievement",
            getImageTextButtonStyle(skin, AssMan.getAssList().iconAchievement));
        skin.add("credits", getImageTextButtonStyle(skin, AssMan.getAssList().iconCredits));
        skin.add(
            "leaderboard",
            getImageTextButtonStyle(skin, AssMan.getAssList().iconLeaderboard));
        skin.add("rate", getImageTextButtonStyle(skin, AssMan.getAssList().iconRate));
        skin.add("tutorial", getImageTextButtonStyle(skin, AssMan.getAssList().iconTutorial));
        skin.add("play", getImageTextButtonStyle(skin, AssMan.getAssList().iconPlay));
        skin.add("exit", getImageTextButtonStyle(skin, AssMan.getAssList().iconExit));
        skin.add("share", getImageTextButtonStyle(skin, AssMan.getAssList().iconShare));
        skin.add("unlock", getImageTextButtonStyle(skin, AssMan.getAssList().iconUnlock));
        skin.add(
            "touch",
            getCheckableImageTextButtonStyles(
                skin,
                AssMan.getAssList().iconTouch,
                AssMan.getAssList().iconTouchChecked));
        skin.add(
            "accelerometer",
            getCheckableImageTextButtonStyles(
                skin,
                AssMan.getAssList().iconAccelerometer,
                AssMan.getAssList().iconAccelerometerChecked));
        skin.add("retry", getImageTextButtonStyle(skin, AssMan.getAssList().iconRetry));
        skin.add(
            "unlockOverlay",
            new Sprite(new Texture(AssMan.getAssList().iconUnlockOverlay)));

        skin.add("checkable", getCheckableTextButtonStyle(skin));

        skin.add("default", getLabelStyle(skin, "default"), Label.LabelStyle.class);
        skin.add("large", getLabelStyle(skin, "large"), Label.LabelStyle.class);

        skin.add("default-horizontal", getProgressBarStyle());

        skin.add("default-horizontal", getSliderStyle());

        skin.add("default", getScrollPaneStyle());

        skin.add("default", getWindowStyle(skin));

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
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.down = skin.getDrawable("button");
        textButtonStyle.up = skin.getDrawable("button");

        return textButtonStyle;
    }

    private static ImageTextButton.ImageTextButtonStyle getImageTextButtonStyle(Skin skin)
    {
        ImageTextButton.ImageTextButtonStyle imageTextButtonStyle =
            new ImageTextButton.ImageTextButtonStyle();
        imageTextButtonStyle.fontColor = Color.BLACK;
        imageTextButtonStyle.font = skin.getFont("default");
        imageTextButtonStyle.down = skin.getDrawable("button");
        imageTextButtonStyle.up = skin.getDrawable("button");

        return imageTextButtonStyle;
    }

    private static ImageTextButton.ImageTextButtonStyle getImageTextButtonStyle(
        Skin skin, String imagePath)
    {
        ImageTextButton.ImageTextButtonStyle imageTextButtonStyle =
            new ImageTextButton.ImageTextButtonStyle(skin.get(
                ImageTextButton.ImageTextButtonStyle.class));
        Drawable settingsDrawable =
            new SpriteDrawable(new Sprite(new Texture(imagePath)));
        imageTextButtonStyle.imageUp = settingsDrawable;
        imageTextButtonStyle.imageDown = settingsDrawable;

        return imageTextButtonStyle;
    }

    private static ImageTextButton.ImageTextButtonStyle getCheckableImageTextButtonStyles(
        Skin skin,
        String imagePath,
        String checkedImagePath)
    {
        ImageTextButton.ImageTextButtonStyle imageTextButtonStyle =
            getImageTextButtonStyle(skin, imagePath);
        imageTextButtonStyle.checkedFontColor = CHECKED_COLOR;
        imageTextButtonStyle.imageChecked =
            new SpriteDrawable(new Sprite(new Texture(checkedImagePath)));

        return imageTextButtonStyle;
    }

    private static TextButton.TextButtonStyle getCheckableTextButtonStyle(Skin skin)
    {
        TextButton.TextButtonStyle checkableTextButtonStyle = new TextButton.TextButtonStyle();
        checkableTextButtonStyle.checkedFontColor = CHECKED_COLOR;
        checkableTextButtonStyle.fontColor = Color.BLACK;
        checkableTextButtonStyle.font = skin.getFont("default");
        checkableTextButtonStyle.down = skin.getDrawable("button");
        checkableTextButtonStyle.up = skin.getDrawable("button");

        return checkableTextButtonStyle;
    }

    private static Label.LabelStyle getLabelStyle(Skin skin, String fontName)
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont(fontName);

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

        sliderStyle.background.setMinWidth(0f);
        sliderStyle.knobBefore.setMinWidth(0f);

        return sliderStyle;
    }

    private static ScrollPane.ScrollPaneStyle getScrollPaneStyle()
    {
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();

        TiledDrawable scrollKnob = getTiledDrawable(Color.WHITE);
        TiledDrawable scrollBackground = getTiledDrawable(Color.DARK_GRAY);

        float size = Constants.Visual.UI.SQRT_PIXELS * 0.01f;
        scrollKnob.setMinWidth(size);
        scrollBackground.setMinWidth(size);
        scrollKnob.setMinHeight(size);
        scrollBackground.setMinHeight(size);

        scrollPaneStyle.hScrollKnob = scrollKnob;
        scrollPaneStyle.vScrollKnob = scrollKnob;
        scrollPaneStyle.hScroll = scrollBackground;
        scrollPaneStyle.vScroll = scrollBackground;

        return scrollPaneStyle;
    }

    private static Window.WindowStyle getWindowStyle(Skin skin)
    {
        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = skin.getFont("large");
        windowStyle.titleFontColor = Color.WHITE;
        windowStyle.background = getTiledDrawable(Constants.Visual.SCREEN_FADE_COLOUR);

        return windowStyle;
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

            progressBarStyle.knobBefore.setMinWidth(0);

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

        TextureRegion textureRegion = new TextureRegion(texture);

        TiledDrawable tiledDrawable = new TiledDrawable(textureRegion);

        return tiledDrawable;
    }
}
