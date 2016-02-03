package com.draga.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.draga.utils.GraphicsUtils;

import java.util.ArrayList;

public class Background implements Disposable
{
    private static final String LOGGING_TAG = Background.class.getSimpleName();

    private final int layerWidth;
    private final int layerHeight;

    private final float u2Offset;
    private final float v2Offset;

    private ArrayList<Texture> textures;
    private ArrayList<Float>   layerParallaxScale;
    private ArrayList<Pixmap>  pixmaps;

    private Vector2 position;

    public Background()
    {
        this.textures = new ArrayList<>();
        this.layerParallaxScale = new ArrayList<>();
        this.pixmaps = new ArrayList<>();

        this.position = new Vector2();

        // uses a pow2 size because GL has problem with textures not being in pow2 format.
        this.layerWidth = GraphicsUtils.getClosestPowerOfTwo(Gdx.graphics.getWidth());
        this.layerHeight = GraphicsUtils.getClosestPowerOfTwo(Gdx.graphics.getHeight());

        this.u2Offset = (float) Gdx.graphics.getWidth() / this.layerWidth;
        this.v2Offset = (float) Gdx.graphics.getHeight() / this.layerHeight;
    }

    /**
     * Draw the texture layers with parallax.
     */
    public void draw(Camera camera, Batch batch)
    {
        float x = camera.position.x - (camera.viewportWidth / 2f);
        float y = camera.position.y - (camera.viewportHeight / 2f);

        for (int i = 0; i < this.textures.size(); i++)
        {
            Float parallax = this.layerParallaxScale.get(i);
            float offsetX = this.position.x * parallax;
            float offsetY = this.position.y * parallax;

            float u = offsetX / camera.viewportWidth;
            float v = offsetY / camera.viewportHeight;

            Texture texture = this.textures.get(i);

            batch.draw(
                texture,
                x,
                y,
                camera.viewportWidth,
                camera.viewportHeight,
                u,
                v,
                u + this.u2Offset,
                v + this.v2Offset);
        }
    }

    /**
     * Disposes any texture and pixmap previously generated.
     */
    public void dispose()
    {
        for (Pixmap pixmap : this.pixmaps)
        {
            pixmap.dispose();
        }

        for (Texture texture : this.textures)
        {
            texture.dispose();
        }
    }

    /**
     * Turns the generated pixmaps into textures and disposes them. Requires a GL context.
     * This has been separated to allow async loading in the {@link BackgroundLoader}.
     */
    public void loadLayersFromPixmaps()
    {
        for (Pixmap pixmap : this.pixmaps)
        {
            Texture texture = new Texture(pixmap);
            pixmap.dispose();
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            // TODO: 01/02/2016 set texture filter to linear? It decreases the FPS a bit too much...
            this.textures.add(texture);
        }

        this.pixmaps.clear();
    }

    /**
     * Generate pixmaps layers of stars to be then turn into textures. Doesn't require a GL context.
     * This has been separated to allow async loading in the {@link BackgroundLoader}.
     */
    public void generateStarLayersPixmap(
        int layerCount,
        int starsCount,
        float minParallax,
        float maxParallax,
        float starMaxDiameterScale)
    {
        for (int i = 0; i < layerCount; i++)
        {
            generateStarLayerPixmap(
                starsCount / layerCount
                    * (this.layerWidth * this.layerHeight)
                    / (Gdx.graphics.getWidth() * Gdx.graphics.getHeight()),
                minParallax,
                maxParallax,
                starMaxDiameterScale);
        }
    }

    private void generateStarLayerPixmap(
        int starsCount,
        float minParallax,
        float maxParallax,
        float starMaxDiameterScale)
    {
        Pixmap pixmap = getStarLayerPixmap(
            this.layerWidth,
            this.layerHeight,
            starsCount,
            starMaxDiameterScale);
        this.pixmaps.add(pixmap);

        float parallaxScale = MathUtils.random(
            minParallax,
            maxParallax);
        this.layerParallaxScale.add(parallaxScale);

    }

    private Pixmap getStarLayerPixmap(
        int width,
        int height,
        int starsCount,
        float starMaxDiameterScale)
    {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Pixmap.setBlending(Pixmap.Blending.None);

        float maxDiameter =
            Gdx.graphics.getWidth() * Gdx.graphics.getHeight() * starMaxDiameterScale;
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

    public void move(Vector2 vector2)
    {
        this.position.add(vector2);
    }

    public Vector2 getPosition()
    {
        return this.position;
    }
}
