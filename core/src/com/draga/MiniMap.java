package com.draga;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class MiniMap
{
    private static final float              MINIMAP_SCALE      = 0.25f;
    private static final OrthographicCamera orthographicCamera = new OrthographicCamera(300, 150);
    private static       ShapeRenderer      shapeRenderer      = new ShapeRenderer();

    static
    {
        shapeRenderer.setAutoShapeType(true);
    }

    public static ShapeRenderer getShapeRenderer()
    {
        return shapeRenderer;
    }

    public static void update(Rectangle... keepInView)
    {
        Rectangle newCameraBounds = new Rectangle();
        for (Rectangle mergeRectangle : keepInView)
        {
            newCameraBounds.merge(mergeRectangle);
        }

        //        float screenAspectRatio = (float) 300 / 150;
        //
        //        if (newCameraBounds.getAspectRatio() > screenAspectRatio)
        //        {
        //            newCameraBounds.height *= newCameraBounds.getAspectRatio() / screenAspectRatio;
        //        }
        //        else if (newCameraBounds.getAspectRatio() < screenAspectRatio)
        //        {
        //            newCameraBounds.width *= screenAspectRatio / newCameraBounds.getAspectRatio();
        //        }
        //        //
        //        newCameraBounds.width = 300;
        //        newCameraBounds.height = 150;


        //        if (newCameraBounds.x - (newCameraBounds.width / 2f))

        //        orthographicCamera.setToOrtho(false, newCameraBounds.width, newCameraBounds.height);
        //        orthographicCamera.setToOrtho(false, 300, 150);
        orthographicCamera.zoom =
            Math.max(newCameraBounds.width / 300, newCameraBounds.height / 150);
        orthographicCamera.position.set(
            newCameraBounds.x + 150,
            newCameraBounds.y + 75,
            0);
        //        orthographicCamera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2,0);
        //        orthographicCamera.zoom = 1 / MINIMAP_SCALE;
        orthographicCamera.update();
        shapeRenderer.setProjectionMatrix(orthographicCamera.combined);
    }
}
