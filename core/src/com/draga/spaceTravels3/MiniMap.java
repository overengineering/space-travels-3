package com.draga.spaceTravels3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.draga.shape.Circle;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.gameEntity.Ship;
import com.draga.spaceTravels3.manager.GameEntityManager;

public class MiniMap
{
    private Ship               ship;
    private OrthographicCamera orthographicCamera;

    public MiniMap(float worldWidth, float worldHeight)
    {
        orthographicCamera = new OrthographicCamera(worldWidth, worldHeight);
    }

    public void draw()
    {
        updateMiniMap();

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.miniMapGraphicComponent.draw();
        }
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
        orthographicCamera.zoom /= Constants.Visual.MINIMAP_SCALE;

        // Moves the camera so that the bottom left corner of the screen corresponds to the
        // bottom left corner of the new camera bounds.
        orthographicCamera.position.x =
            newCameraBounds.x
                + ((orthographicCamera.viewportWidth / 2f) * orthographicCamera.zoom);
        orthographicCamera.position.y =
            newCameraBounds.y
                + ((orthographicCamera.viewportHeight / 2f) * orthographicCamera.zoom);

        orthographicCamera.update();
        SpaceTravels3.shapeRenderer.setProjectionMatrix(orthographicCamera.combined);
    }

    private void drawBackground()
    {
        orthographicCamera.zoom = 1 / Constants.Visual.MINIMAP_SCALE;
        orthographicCamera.position.set(
            (orthographicCamera.viewportWidth / 2f) * orthographicCamera.zoom,
            (orthographicCamera.viewportHeight / 2f) * orthographicCamera.zoom,
            0);
        orthographicCamera.update();
        SpaceTravels3.shapeRenderer.setProjectionMatrix(orthographicCamera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Color minimapBackgroundColor = new Color(0, 0.17f, 0, 0.5f);
        SpaceTravels3.shapeRenderer.setColor(minimapBackgroundColor);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        SpaceTravels3.shapeRenderer.rect(
            0,
            0,
            orthographicCamera.viewportWidth,
            orthographicCamera.viewportHeight);

        Color minimapBorderColor = new Color(0, 0.4f, 0, 1);
        SpaceTravels3.shapeRenderer.setColor(minimapBorderColor);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        SpaceTravels3.shapeRenderer.rect(
            0,
            0,
            orthographicCamera.viewportWidth,
            orthographicCamera.viewportHeight);

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void addShip(Ship ship)
    {
        this.ship = ship;
    }
}
