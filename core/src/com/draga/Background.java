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
    private ArrayList<Pixmap>  pixmaps;

    public Background()
    {
        this.layers = new ArrayList<>();
        this.layerParallaxScale = new ArrayList<>();
        this.pixmaps = new ArrayList<>();
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

    public void addStarLayerPixmap(int starsCount, float minParallax, float maxParallax)
    {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Pixmap pixmap = getStarLayerPixmap(
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight(),
            starsCount);
        this.pixmaps.add(pixmap);

        float parallaxScale = MathUtils.random(
            minParallax,
            maxParallax);
        this.layerParallaxScale.add(parallaxScale);

        Gdx.app.debug(LOGGING_TAG, "Generating star layer took " + stopwatch.elapsed(
            TimeUnit.NANOSECONDS) * Constants.General.NANO + "s");

    }

    private Pixmap getStarLayerPixmap(int width, int height, int starsCount)
    {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Pixmap.setBlending(Pixmap.Blending.None);

        float maxDiameter = width * height * Constants.Visual.Background.STAR_MAX_DIAMETER_SCALE;
        Interpolation alphaInterpolation = Interpolation.pow4;

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
                for (int j = radius; j > 0; j--)
                {
                    pixmap.setColor(
                        r,
                        g,
                        b,
                        1f - alphaInterpolation.apply(j / (radius + 1f)));
                    pixmap.fillCircle(x, y, j);
                }
                pixmap.setColor(r, g, b, 1f);
                pixmap.drawPixel(x, y);
            }
        }

        return pixmap;
    }

    public void loadLayersFromPixmaps()
    {
        for (Pixmap pixmap : this.pixmaps)
        {
            Texture texture = new Texture(pixmap);
            pixmap.dispose();
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            this.layers.add(texture);
        }

        this.pixmaps.clear();
    }
}
