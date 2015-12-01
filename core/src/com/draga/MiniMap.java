package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.draga.gameEntity.GameEntity;
import com.draga.gameEntity.Ship;
import com.draga.manager.GameEntityManager;
import com.draga.physic.shape.Circle;

public class MiniMap
{
    private static final float MINIMAP_SCALE = 0.25f;
    private Ship               ship;
    private OrthographicCamera orthographicCamera;
    private ShapeRenderer      shapeRenderer;

    public MiniMap(int worldWidth, int worldHeight)
    {
        orthographicCamera = new OrthographicCamera(worldWidth, worldHeight);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }

    public void draw()
    {
        updateMiniMap();

        shapeRenderer.begin();
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.miniMapGraphicComponent.draw(shapeRenderer);
        }

        shapeRenderer.end();
    }

    public void updateMiniMap()
    {
        Rectangle shipRect = new Rectangle(
            ship.physicsComponent.getPosition().x
                - ((Circle) ship.physicsComponent.getShape()).radius,
            ship.physicsComponent.getPosition().y
                - ((Circle) ship.physicsComponent.getShape()).radius,
            ((Circle) ship.physicsComponent.getShape()).radius * 2,
            ((Circle) ship.physicsComponent.getShape()).radius * 2);

        Rectangle worldRect = new Rectangle(
            0,
            0,
            orthographicCamera.viewportWidth,
            orthographicCamera.viewportHeight);
        update(shipRect, worldRect);
    }

    private void update(Rectangle... keepInView)
    {
        // Lee said that when we'll came to refactor this we ain't going to be too upset about it.

        // Create a rectangle encompassing all the rectangles that needs to be kept into view.
        Rectangle newCameraBounds = new Rectangle();
        for (Rectangle mergeRectangle : keepInView)
        {
            newCameraBounds.merge(mergeRectangle);
        }

        // Draw a background and border.
        drawBackground();

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

    private void drawBackground()
    {
        orthographicCamera.zoom = 1 / MINIMAP_SCALE;
        orthographicCamera.position.set(
            (orthographicCamera.viewportWidth / 2f) * orthographicCamera.zoom,
            (orthographicCamera.viewportHeight / 2f) * orthographicCamera.zoom,
            0);
        orthographicCamera.update();
        shapeRenderer.setProjectionMatrix(orthographicCamera.combined);

        shapeRenderer.begin();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Color minimapBackgroundColor = new Color(0, 0.17f, 0, 0.5f);
        shapeRenderer.setColor(minimapBackgroundColor);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(
            0,
            0,
            orthographicCamera.viewportWidth,
            orthographicCamera.viewportHeight);

        Color minimapBorderColor = new Color(0, 0.4f, 0, 1);
        shapeRenderer.setColor(minimapBorderColor);
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(
            0,
            0,
            orthographicCamera.viewportWidth,
            orthographicCamera.viewportHeight);

        Gdx.gl.glDisable(GL20.GL_BLEND);
        shapeRenderer.end();
    }

    public void addShip(Ship ship)
    {
        this.ship = ship;
    }
}
