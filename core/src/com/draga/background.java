package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Background implements Disposable
{
    private static final String LOGGING_TAG = Background.class.getSimpleName();

    private ArrayList<Sprite> layers;
    private ArrayList<Float>  layerParallaxScale;

    public Background()
    {
        layers = new ArrayList<>();
        layerParallaxScale = new ArrayList<>();

        addNebulaeLayers(Constants.Visual.BACKGROUND_NEBULAE_LAYER_COUNT);

        addStarLayers(Constants.Visual.BACKGROUND_STAR_LAYER_COUNT);
    }

    private void addNebulaeLayers(int count)
    {
        float r = MathUtils.random(0f, 1f);
        float g = MathUtils.random(0f, 1f);
        float b = MathUtils.random(0f, 1f);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < count; i++)
        {
            Texture texture = getNebulaLayer(r, g, b);
            Sprite sprite = getRepeatingSprite(texture, Texture.TextureWrap.MirroredRepeat);
            layers.add(sprite);

            float parallaxScale = MathUtils.random(0.05f, 0.3f);
            layerParallaxScale.add(parallaxScale);

            //            PixmapIO.writePNG(FileManager.getFileHandle(Constants.General.FOLDER, "nebula"+i+".png"), pixmap);
        }
        Gdx.app.debug(
            LOGGING_TAG,
            Constants.Visual.BACKGROUND_NEBULAE_LAYER_COUNT
                + " layers of nebulae took "
                + stopwatch.elapsed(
                TimeUnit.NANOSECONDS) * Constants.General.NANO
                + "s");
    }

    private void addStarLayers(int count)
    {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < count; i++)
        {
            Texture texture = getStarLayer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Sprite sprite = getRepeatingSprite(texture, Texture.TextureWrap.Repeat);
            layers.add(sprite);

            float parallaxScale = MathUtils.random(0.05f, 0.3f);
            layerParallaxScale.add(parallaxScale);

            //            PixmapIO.writePNG(FileManager.getFileHandle(Constants.General.FOLDER, "stars"+i+".png"), pixmap);
        }
        Gdx.app.debug(LOGGING_TAG, Constants.Visual.BACKGROUND_STAR_LAYER_COUNT
            + " layers of stars took " + stopwatch.elapsed(
            TimeUnit.NANOSECONDS) * Constants.General.NANO + "s");
    }

    private Texture getNebulaLayer(float r, float g, float b)
    {
        int size = 1024;
        float[][] pixels = PerlinNoiseGenerator.generatePerlinNoise(
            size,
            size,
            8);
        Pixmap pixmap = new Pixmap(
            size,
            size,
            Pixmap.Format.RGBA8888);

        for (int x = 0; x < size; x++)
        {
            for (int y = 0; y < size; y++)
            {
                float a = Math.max(0, (pixels[x][y] - 0.6f) / 0.4f);
                //float a = pixels[x][y];
                pixmap.setColor(
                    r,
                    g,
                    b,
                    a);
                pixmap.drawPixel(x, y);
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Sprite getRepeatingSprite(Texture texture, Texture.TextureWrap textureWrap)
    {
        texture.setWrap(textureWrap, textureWrap);
        Sprite sprite = new Sprite(texture);

        return sprite;
    }

    private static Texture getStarLayer(int width, int height)
    {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Pixmap.setBlending(Pixmap.Blending.None);

        for (int i = 0; i < Constants.Visual.BACKGROUND_STAR_COUNT
            / Constants.Visual.BACKGROUND_STAR_LAYER_COUNT; i++)
        {
            int radius = Math.round(MathUtils.randomTriangular(0, 1, 0));
            int x = MathUtils.random(radius, width - 1 - radius);
            int y = MathUtils.random(radius, height - 1 - radius);
            float alphaStep = 1f / (radius + 1);
            float alpha = alphaStep;
            for (int j = radius; j >= 0; j--)
            {
                float r = MathUtils.random(0.8f, 1f);
                float g = MathUtils.random(0.8f, 1f);
                float b = MathUtils.random(0.8f, 1f);
                pixmap.setColor(r, g, b, alpha);
                if (j == 0)
                {
                    pixmap.drawPixel(x, y);
                }
                else
                {
                    pixmap.fillCircle(x, y, j);
                }

                alpha += alphaStep;
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
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

    public void dispose()
    {
        for (int i = 0; i < layers.size(); i++)
        {
            layers.get(i).getTexture().dispose();
        }
    }
}
