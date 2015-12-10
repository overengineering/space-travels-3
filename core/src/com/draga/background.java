package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

public class Background
{
    public static final int LAYER_COUNT = 10;
    public static final int STAR_COUNT = 300;
    ArrayList<Sprite> layers;
    ArrayList<Float>  layerParallaxScale;

    public Background()
    {
        layers = new ArrayList<>();
        layerParallaxScale = new ArrayList<>();

        for (int i = 0; i < LAYER_COUNT; i++)
        {
            Texture layer = generateStarLayer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            layer.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
            Sprite sprite = new Sprite(layer);
            layers.add(sprite);

            float parallaxScale = MathUtils.random(0f, 0.3f);
            layerParallaxScale.add(parallaxScale);
        }
    }

    private static Texture generateStarLayer(int width, int height)
    {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Pixmap.setBlending(Pixmap.Blending.None);

        for (int i = 0; i < STAR_COUNT / LAYER_COUNT; i++)
        {
            //            pixmap.setColor(
            //                MathUtils.random(0.8f, 1f),
            //                MathUtils.random(0.5f, 0.8f),
            //                MathUtils.random(0.8f, 1f),
            //                MathUtils.random(0.5f, 1f)
            //            );
            int x = MathUtils.random(1, width - 1);
            int y = MathUtils.random(1, height - 1);
            int radius = MathUtils.random(1, 3);
            float alphaStep = 1f / radius;
            float alpha = alphaStep;
            for (int j = radius; j > 0; j--)
            {
                pixmap.setColor(1f, 1f, 1f, alpha);
                pixmap.fillCircle(x, y, j);

                alpha += alphaStep;
            }
        }

        Texture texture = new Texture(pixmap);

        return texture;
    }

    public void draw(Camera camera)
    {
        for (int i = 0; i < layers.size(); i++)
        {
            float offsetX = camera.position.x * layerParallaxScale.get(i);
            float offsetY = camera.position.y * layerParallaxScale.get(i);
            Sprite sprite = layers.get(i);
            sprite.setU(offsetX / camera.viewportWidth);
            sprite.setU2((offsetX / camera.viewportWidth) + 1);

            sprite.setV((offsetY / camera.viewportHeight) + 1);
            sprite.setV2(offsetY / camera.viewportHeight);

            SpaceTravels3.spriteBatch.draw(
                sprite,
                camera.position.x - camera.viewportWidth / 2f,
                camera.position.y - camera.viewportHeight / 2f,
                camera.viewportWidth,
                camera.viewportHeight);
        }
    }
}
