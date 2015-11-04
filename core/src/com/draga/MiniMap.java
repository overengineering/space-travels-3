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

    private static final OrthographicCamera orthographicCamera = new OrthographicCamera();

    static
    {
        orthographicCamera.viewportWidth =  Gdx.graphics.getWidth() * 4;
        orthographicCamera.viewportHeight =  Gdx.graphics.getHeight() * 4;
        orthographicCamera.update();

        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setProjectionMatrix(orthographicCamera.combined);
    }

    public static ShapeRenderer getShapeRenderer()
    {
        return shapeRenderer;
    }
}
