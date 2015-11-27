package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class MiniMap
{
    private static final float              MINIMAP_SCALE      = 0.25f;
    private static final OrthographicCamera orthographicCamera = new OrthographicCamera();
    private static       ShapeRenderer      shapeRenderer      = new ShapeRenderer();

    static
    {
        shapeRenderer.setAutoShapeType(true);
    }

    public static void setWorldSize(float width, float height)
    {
        orthographicCamera.viewportWidth = width;
        orthographicCamera.viewportHeight = height;
    }

    public static void update(Rectangle... keepInView)
    {
        // Lee said that when we'll came to refactor this we ain't going to be too upset about it.

        // Create a rectangle encompassing all the rectangles that needs to be kept into view.
        Rectangle newCameraBounds = new Rectangle();
        for (Rectangle mergeRectangle : keepInView)
        {
            newCameraBounds.merge(mergeRectangle);
        }

        // Draw a background and border.
        MiniMap.drawBackground();

        // Zooms out enough to see the entire width and height of the new camera bounds, while
        // keeping the aspect ratio.
        orthographicCamera.zoom =
            Math.max(
                newCameraBounds.width / orthographicCamera.viewportWidth,
                newCameraBounds.height / orthographicCamera.viewportHeight);
        orthographicCamera.zoom /= MINIMAP_SCALE;

        // Moves the camera so that the bottom left corner of the screen corresponds to the
        // bottom left corner of the new camera bounds.
        orthographicCamera.position.x =
            newCameraBounds.x
                + ((orthographicCamera.viewportWidth / 2f) * orthographicCamera.zoom);
        orthographicCamera.position.y =
            newCameraBounds.y
                + ((orthographicCamera.viewportHeight / 2f) * orthographicCamera.zoom);

        orthographicCamera.update();
        shapeRenderer.setProjectionMatrix(orthographicCamera.combined);
    }

    public static void drawBackground()
    {
        orthographicCamera.zoom = 1 / MINIMAP_SCALE;
        orthographicCamera.position.set(
            (orthographicCamera.viewportWidth / 2f) * orthographicCamera.zoom,
            (orthographicCamera.viewportHeight / 2f) * orthographicCamera.zoom,
            0);
        orthographicCamera.update();
        shapeRenderer.setProjectionMatrix(orthographicCamera.combined);

        MiniMap.getShapeRenderer().begin();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Color minimapBackgroundColor = new Color(0, 0.17f, 0, 0.5f);
        MiniMap.getShapeRenderer().setColor(minimapBackgroundColor);
        MiniMap.getShapeRenderer().set(ShapeRenderer.ShapeType.Filled);
        MiniMap.getShapeRenderer()
            .rect(0, 0, orthographicCamera.viewportWidth, orthographicCamera.viewportHeight);

        Color minimapBorderColor = new Color(0, 0.4f, 0, 1);
        MiniMap.getShapeRenderer().setColor(minimapBorderColor);
        MiniMap.getShapeRenderer().set(ShapeRenderer.ShapeType.Line);
        MiniMap.getShapeRenderer()
            .rect(0, 0, orthographicCamera.viewportWidth, orthographicCamera.viewportHeight);

        Gdx.gl.glDisable(GL20.GL_BLEND);
        MiniMap.getShapeRenderer().end();
    }

    public static ShapeRenderer getShapeRenderer()
    {
        return shapeRenderer;
    }
}
