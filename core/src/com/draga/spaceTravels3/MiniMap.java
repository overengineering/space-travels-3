package com.draga.spaceTravels3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.draga.shape.Circle;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.level.Level;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.physic.Projection;
import com.draga.utils.GraphicsUtils;

import static com.draga.spaceTravels3.Constants.Visual.HUD.Minimap.SCALE;

public class MiniMap
{
    private static final int SCISSOR_WIDTH = MathUtils.round(Gdx.graphics.getWidth() * SCALE) + 1;
    private static final int SCISSOR_HEIGHT = MathUtils.round(Gdx.graphics.getHeight() * SCALE) + 1;

    private final OrthographicCamera orthographicCamera;
    private final Matrix4            backgroundProjectionMatrix;
    private final Level              level;
    private       float              miniMapAspectRatio;

    private Projection shipProjection;

    public MiniMap(Level level)
    {
        this.level = level;
        this.orthographicCamera =
            new OrthographicCamera(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());

        this.backgroundProjectionMatrix = getBackgroundProjectionMatrix();
        this.miniMapAspectRatio =
            this.orthographicCamera.viewportWidth / this.orthographicCamera.viewportHeight;
    }

    private Matrix4 getBackgroundProjectionMatrix()
    {
        OrthographicCamera backgroundCamera =
            new OrthographicCamera(this.level.getWidth(), this.level.getHeight());
        backgroundCamera.zoom = 1f / SCALE;
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
        SpaceTravels3.shapeRenderer.setProjectionMatrix(this.backgroundProjectionMatrix);
        drawBackground();

        SpaceTravels3.shapeRenderer.setProjectionMatrix(this.orthographicCamera.combined);
        Gdx.gl20.glEnable(GL20.GL_SCISSOR_TEST);
        // Crop out everything outside of the minimap (note the +1 to include the last pixel)
        HdpiUtils.glScissor(
            0,
            0,
            SCISSOR_WIDTH,
            SCISSOR_HEIGHT);

        if (this.shipProjection != null)
        {
            this.shipProjection.draw();
        }

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.miniMapGraphicComponent.draw();
        }

        Gdx.gl20.glDisable(GL20.GL_SCISSOR_TEST);
    }

    private void drawBackground()
    {
        GraphicsUtils.enableBlending();

        Color minimapBackgroundColor = Constants.Visual.HUD.Minimap.BACKGROUND_COLOR;
        SpaceTravels3.shapeRenderer.setColor(minimapBackgroundColor);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        SpaceTravels3.shapeRenderer.rect(
            0,
            0,
            this.level.getWidth(),
            this.level.getHeight());

        Color minimapBorderColor = Constants.Visual.HUD.Minimap.BORDER_COLOR;
        SpaceTravels3.shapeRenderer.setColor(minimapBorderColor);
        SpaceTravels3.shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        SpaceTravels3.shapeRenderer.rect(
            0,
            0,
            this.level.getWidth(),
            this.level.getHeight());

        GraphicsUtils.disableBlending();
    }

    public void update()
    {
        Circle shipBoundsCircle = this.level.getShip().physicsComponent.getBoundsCircle();

        Rectangle shipRect = new Rectangle(
            this.level.getShip().physicsComponent.getPosition().x
                - shipBoundsCircle.radius,
            this.level.getShip().physicsComponent.getPosition().y
                - shipBoundsCircle.radius,
            shipBoundsCircle.radius * 2,
            shipBoundsCircle.radius * 2);

        Rectangle worldRect = this.level.getBounds();

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
        if (newCameraBoundsAspectRatio < this.miniMapAspectRatio)
        {
            float newWidth = newCameraBounds.height * this.miniMapAspectRatio;
            float widthDifference = newWidth - newCameraBounds.width;
            newCameraBounds.x -= widthDifference / 2f;
            newCameraBounds.width = newWidth;
        }
        else
        {
            float newHeight = newCameraBounds.width / this.miniMapAspectRatio;
            float heightDifference = newHeight - newCameraBounds.height;
            newCameraBounds.y -= heightDifference / 2f;
            newCameraBounds.height = newHeight;
        }

        // Zoom out to see the new camera bounds.
        this.orthographicCamera.zoom = Math.max(
            newCameraBounds.width / this.orthographicCamera.viewportWidth,
            newCameraBounds.height / this.orthographicCamera.viewportHeight);
        // Zoom out to make this world as big as the minimap.
        this.orthographicCamera.zoom /= SCALE;

        // Moves the camera so that the bottom left corner of the screen corresponds to the
        // bottom left corner of the new camera bounds.
        this.orthographicCamera.position.x =
            newCameraBounds.x
                + ((this.orthographicCamera.viewportWidth / 2f) * this.orthographicCamera.zoom);
        this.orthographicCamera.position.y =
            newCameraBounds.y
                + ((this.orthographicCamera.viewportHeight / 2f) * this.orthographicCamera.zoom);
        this.orthographicCamera.update();
    }

    public void setShipProjection(Projection shipProjection)
    {
        this.shipProjection = shipProjection;
    }
}
