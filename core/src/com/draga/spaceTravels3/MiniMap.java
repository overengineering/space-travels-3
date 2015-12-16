package com.draga.spaceTravels3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.draga.shape.Circle;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.gameEntity.Ship;
import com.draga.spaceTravels3.manager.GameEntityManager;

public class MiniMap
{
    private final OrthographicCamera orthographicCamera;
    private final Matrix4            backgroundProjectionMatrix;
    private       Ship               ship;
    private       float              worldWidth;
    private       float              worldHeight;
    private       float              miniMapAspectRatio;

    public MiniMap(Ship ship, float worldWidth, float worldHeight)
    {
        this.ship = ship;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.orthographicCamera =
            new OrthographicCamera(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        this.backgroundProjectionMatrix = getBackgroundProjectionMatrix();
        this.miniMapAspectRatio =
            orthographicCamera.viewportWidth / orthographicCamera.viewportHeight;
    }

    private Matrix4 getBackgroundProjectionMatrix()
    {
        OrthographicCamera backgroundCamera = new OrthographicCamera(worldWidth, worldHeight);
        backgroundCamera.zoom = 1f / Constants.Visual.MINIMAP_SCALE;
        backgroundCamera.position.set(
            (backgroundCamera.viewportWidth / 2f) * backgroundCamera.zoom,
            (backgroundCamera.viewportHeight / 2f) * backgroundCamera.zoom,
            0);
        backgroundCamera.update();

        return backgroundCamera.combined;
    }

    public void draw()
    {
        // Draw a background and border.
        SpaceTravels3.shapeRenderer.setProjectionMatrix(backgroundProjectionMatrix);
        drawBackground();

        SpaceTravels3.shapeRenderer.setProjectionMatrix(orthographicCamera.combined);
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.miniMapGraphicComponent.draw();
        }
    }

    private void drawBackground()
    {
        GraphicsUtils.SetBlending(true);

        Color minimapBackgroundColor = Constants.Visual.MINIMAP_BACKGROUND_COLOR;
        SpaceTravels3.shapeRenderer.setColor(minimapBackgroundColor);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        SpaceTravels3.shapeRenderer.rect(
            0,
            0,
            worldWidth,
            worldHeight);

        Color minimapBorderColor = Constants.Visual.MINIMAP_BORDER_COLOR;
        SpaceTravels3.shapeRenderer.setColor(minimapBorderColor);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        SpaceTravels3.shapeRenderer.rect(
            0,
            0,
            worldWidth,
            worldHeight);

        GraphicsUtils.SetBlending(false);
    }

    public void update()
    {
        Circle shipCircle = (Circle) ship.physicsComponent.getShape();

        Rectangle shipRect = new Rectangle(
            ship.physicsComponent.getPosition().x
                - shipCircle.radius,
            ship.physicsComponent.getPosition().y
                - shipCircle.radius,
            shipCircle.radius * 2,
            shipCircle.radius * 2);

        Rectangle worldRect = new Rectangle(
            0,
            0,
            worldWidth,
            worldHeight);

        keepInView(shipRect, worldRect);
    }

    // Lee said that when we'll came to refactor this we ain't going to be too upset about it.
    private void keepInView(Rectangle... keepInView)
    {
        // Create a rectangle encompassing all the rectangles that needs to be kept into view.
        Rectangle newCameraBounds = new Rectangle();
        for (Rectangle mergeRectangle : keepInView)
        {
            newCameraBounds.merge(mergeRectangle);
        }

        // Keep the minimap aspect ratio so that the rectangle will be centered in the minimap.
        float newCameraBoundsAspectRatio = newCameraBounds.getAspectRatio();
        if (newCameraBoundsAspectRatio < miniMapAspectRatio)
        {
            float newWidth = newCameraBounds.height * miniMapAspectRatio;
            float widthDifference = newWidth - newCameraBounds.width;
            newCameraBounds.x -= widthDifference / 2f;
            newCameraBounds.width = newWidth;
        }
        else
        {
            float newHeight = newCameraBounds.width / miniMapAspectRatio;
            float heightDifference = newHeight - newCameraBounds.height;
            newCameraBounds.y -= heightDifference / 2f;
            newCameraBounds.height = newHeight;
        }

        // Zoom out to see the new camera bounds.
        orthographicCamera.zoom = Math.max(
            newCameraBounds.width / orthographicCamera.viewportWidth,
            newCameraBounds.height / orthographicCamera.viewportHeight);
        // Zoom out to make this world as big as the minimap.
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
    }
}
