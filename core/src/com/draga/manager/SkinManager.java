package com.draga.manager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

        //Create a font
        BitmapFont font = FontManager.getBigFont();
        skin.add("default", font);

        //Create a texture
        Pixmap pixmap = new Pixmap(0, 0, Pixmap.Format.RGB888);
        pixmap.setColor(Color.CLEAR);
        pixmap.fill();
        skin.add("background", new Texture(pixmap));

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        //Create check text button style
        TextButton.TextButtonStyle checkTextButtonStyle = new TextButton.TextButtonStyle();
        checkTextButtonStyle.font = FontManager.getBigFont();
        checkTextButtonStyle.checkedFontColor = Color.GREEN;
        checkTextButtonStyle.fontColor = Color.WHITE;
        skin.add("checkTextButton", checkTextButtonStyle);

        return skin;
    }
}
