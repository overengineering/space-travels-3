package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
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
        this.layers = new ArrayList<>();
        this.layerParallaxScale = new ArrayList<>();
    }

    public void addStarLayer(int starsCount)
    {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Texture texture = getStarLayer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
            starsCount);
        this.layers.add(texture);

        float parallaxScale = MathUtils.random(0.05f, 0.3f);
        this.layerParallaxScale.add(parallaxScale);

        Gdx.app.debug(LOGGING_TAG, "Generating star layer took " + stopwatch.elapsed(
            TimeUnit.NANOSECONDS) * Constants.General.NANO + "s");
    }

    private static Texture getStarLayer(int width, int height, int starsCount)
    {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Pixmap.setBlending(Pixmap.Blending.None);

        float maxDiameter = width * height * Constants.Visual.Background.STAR_MAX_DIAMETER_SCALE;

        for (int i = 0; i < starsCount; i++)
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
                Interpolation alphaInterpolation = Interpolation.pow4;
                for (int j = radius; j > 0; j--)
                {
                    pixmap.setColor(r, g, b, alphaInterpolation.apply(0f, 1f, (j + 1f) / radius));
                    pixmap.fillCircle(x, y, j);
                }
                pixmap.setColor(r, g, b, 1f);
                pixmap.drawPixel(x, y);
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

        for (int i = 0; i < this.layers.size(); i++)
        {
            float offsetX = camera.position.x * this.layerParallaxScale.get(i);
            float offsetY = camera.position.y * this.layerParallaxScale.get(i);
            Texture texture = this.layers.get(i);

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
        for (Texture layer : this.layers)
        {
            layer.dispose();
        }
    }

    public int getLayerCount()
    {
        return this.layers.size();
    }
}
