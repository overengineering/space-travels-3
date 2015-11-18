package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.draga.manager.asset.AssMan;
import com.draga.manager.asset.FontManager;

public class SkinManager
{
    public static Skin BasicSkin;

    public static void create()
    {
        BasicSkin = createBasicSkin();
    }

    private static Skin createBasicSkin()
    {
        Skin skin = new Skin();

        // Create a font
        BitmapFont font = FontManager.getBigFont();
        skin.add("bigFont", font);

        // Create a texture
        Pixmap pixmap = new Pixmap(0, 0, Pixmap.Format.RGB888);
        pixmap.setColor(Color.CLEAR);
        pixmap.fill();
        skin.add("background", new Texture(pixmap));

        // Create a button 9 patch
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("button/button.pack"));
        NinePatch buttonNinePatch = textureAtlas.createPatch("button");
        skin.add("button", buttonNinePatch);

        // Create a text button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.checkedFontColor = Color.GREEN;
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.font = skin.getFont("bigFont");
        textButtonStyle.down = skin.getDrawable("button");
        textButtonStyle.up = skin.getDrawable("button");
        skin.add("default", textButtonStyle);

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("bigFont");
        skin.add("default", labelStyle, Label.LabelStyle.class);

        // Progress bar texture
        Pixmap progressBarPixmap =
            new Pixmap(1, (int) (Gdx.graphics.getHeight() / 30f), Pixmap.Format.RGBA8888);
        progressBarPixmap.setColor(Color.WHITE);
        progressBarPixmap.fill();
        skin.add("progressbar", new Texture(progressBarPixmap));


        return skin;
    }
}
