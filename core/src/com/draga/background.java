package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Background implements Disposable
{
    private static final String LOGGING_TAG = Background.class.getSimpleName();

    private ArrayList<Texture> layers;
    private ArrayList<Float>   layerParallaxScale;

    public Background()
    {
        layers = new ArrayList<>();
        layerParallaxScale = new ArrayList<>();

        addNebulaeLayers(Constants.Visual.BACKGROUND_NEBULAE_LAYER_COUNT);

        addStarLayers(Constants.Visual.BACKGROUND_STAR_LAYER_COUNT);
    }

    private void addNebulaeLayers(int count)
    {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < count; i++)
        {
            Texture texture = getNebulaLayer();
            layers.add(texture);

            float parallaxScale = MathUtils.random(0.05f, 0.3f);
            layerParallaxScale.add(parallaxScale);
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
            layers.add(texture);

            float parallaxScale = MathUtils.random(0.05f, 0.3f);
            layerParallaxScale.add(parallaxScale);
        }
        Gdx.app.debug(LOGGING_TAG, Constants.Visual.BACKGROUND_STAR_LAYER_COUNT
            + " layers of stars took " + stopwatch.elapsed(
            TimeUnit.NANOSECONDS) * Constants.General.NANO + "s");
    }

    private Texture getNebulaLayer()
    {
        float r = MathUtils.random(0f, 1f);
        float g = MathUtils.random(0f, 1f);
        float b = MathUtils.random(0f, 1f);
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        float[][] pixels = PerlinNoiseGenerator.generatePerlinNoise(
            width,
            height,
            8);
        Pixmap pixmap = new Pixmap(
            width,
            height,
            Pixmap.Format.RGBA8888);

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                float a = Math.max(0, (pixels[x][y] - 0.6f) / 0.4f);
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
        texture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        return texture;
    }

    private static Texture getStarLayer(int width, int height)
    {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Pixmap.setBlending(Pixmap.Blending.None);

        float maxDiameter = width * height * Constants.Visual.BACKGROUND_STAR_MAX_DIAMETER_SCALE;

        for (int i = 0; i < Constants.Visual.BACKGROUND_STAR_COUNT
            / Constants.Visual.BACKGROUND_STAR_LAYER_COUNT; i++)
        {
            float diameter = MathUtils.randomTriangular(0.1f, maxDiameter, 0.1f);
            int radius = MathUtils.round(diameter / 2f);
            int x = MathUtils.random(radius, width - 1 - radius);
            int y = MathUtils.random(radius, height - 1 - radius);

            float r = MathUtils.random(0.8f, 1f);
            float g = MathUtils.random(0.8f, 1f);
            float b = MathUtils.random(0.8f, 1f);

            if (diameter <= 1f)
            {
                pixmap.setColor(r, g, b, diameter);
                pixmap.drawPixel(x, y);
            }
            else
            {
                float alphaStep = 1f / (radius + 1);
                float alpha = alphaStep;
                for (int j = radius; j >= 0; j--)
                {
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

        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        return texture;
    }

    public void draw(Camera camera)
    {
        float x = camera.position.x - camera.viewportWidth / 2f;
        float y = camera.position.y - camera.viewportHeight / 2f;

        for (int i = 0; i < layers.size(); i++)
        {
            float offsetX = camera.position.x * layerParallaxScale.get(i);
            float offsetY = camera.position.y * layerParallaxScale.get(i);
            Texture texture = layers.get(i);

            float u = offsetX / camera.viewportWidth;
            float v = offsetY / camera.viewportHeight;

            SpaceTravels3.spriteBatch.draw(
                texture,
                x,
                y,
                camera.viewportWidth,
                camera.viewportHeight,
                u,
                v,
                u + 1f,
                v + 1f);
        }
    }

    public void dispose()
    {
        for (int i = 0; i < layers.size(); i++)
        {
            layers.get(i).dispose();
        }
    }
}
