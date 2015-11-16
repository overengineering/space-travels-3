package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.*;

public class MiniMap
{
    private static ShapeRenderer shapeRenderer = new ShapeRenderer();
    private static Viewport viewport;
    private static final float MINIMAP_SCALE = 0.25f;

    private static final OrthographicCamera orthographicCamera = new OrthographicCamera();

    static
    {
        orthographicCamera.viewportWidth =  Gdx.graphics.getWidth() / MINIMAP_SCALE;
        orthographicCamera.viewportHeight =  Gdx.graphics.getHeight() / MINIMAP_SCALE;
        orthographicCamera.update();

        shapeRenderer.setAutoShapeType(true);
    }

    public static ShapeRenderer getShapeRenderer()
    {
        return shapeRenderer;
    }
}
