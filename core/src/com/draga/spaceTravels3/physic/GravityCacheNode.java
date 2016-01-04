package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

import java.util.ArrayList;

public class GravityCacheNode
{
    private static final String LOGGING_TAG = GravityCacheNode.class.getSimpleName();

    private static final float SPLIT_THRESHOLD   = 10f;
    private static final float SPLIT_THRESHOLD_2 = SPLIT_THRESHOLD * SPLIT_THRESHOLD;
    private static final float MIN_SIZE          = 1f;

    private final GravityCacheNode topLeftNode;
    private final GravityCacheNode topRightNode;
    private final GravityCacheNode bottomLeftNode;
    private final GravityCacheNode bottomRightNode;

    private final Vector2 centre;

    private final Rectangle bounds;

    private final Vector2 topLeftGravity;
    private final Vector2 topRightGravity;
    private final Vector2 bottomLeftGravity;
    private final Vector2 bottomRightGravity;

    private final float   topLeftGravityLen2;
    private final float   topRightGravityLen2;
    private final float   bottomLeftGravityLen2;
    private final float   bottomRightGravityLen2;
    private final boolean hasChildren;

    public GravityCacheNode(
        Rectangle bounds,
        ArrayList<PhysicsComponent> staticPhysicsComponentsWithMass)
    {
        this.bounds = bounds;

        this.topLeftGravity = new Vector2();
        this.topRightGravity = new Vector2();
        this.bottomLeftGravity = new Vector2();
        this.bottomRightGravity = new Vector2();

        Vector2 centreGravity = new Vector2();
        Vector2 topGravity = new Vector2();
        Vector2 leftGravity = new Vector2();
        Vector2 rightGravity = new Vector2();
        Vector2 bottomGravity = new Vector2();

        float halfWidth = bounds.width / 2f;
        float halfHeight = bounds.height / 2f;
        this.centre = PooledVector2.newVector2(0f, 0f);
        bounds.getCenter(this.centre);

        for (PhysicsComponent physicsComponent : staticPhysicsComponentsWithMass)
        {
            float leftX = physicsComponent.getPosition().x - bounds.x;
            float centreX = physicsComponent.getPosition().x - (bounds.x + halfWidth);
            float rightX = physicsComponent.getPosition().x - (bounds.x + bounds.width);

            float topY = physicsComponent.getPosition().y - (bounds.y + bounds.height);
            float centreY = physicsComponent.getPosition().y - (bounds.y + halfHeight);
            float bottomY = physicsComponent.getPosition().y - bounds.y;

            addGravity(
                physicsComponent,
                leftX,
                topY,
                this.topLeftGravity);

            addGravity(
                physicsComponent,
                centreX,
                topY,
                topGravity);

            addGravity(
                physicsComponent,
                rightX,
                topY,
                this.topRightGravity);


            addGravity(
                physicsComponent,
                leftX,
                centreY,
                leftGravity);

            addGravity(
                physicsComponent,
                centreX,
                centreY,
                centreGravity);

            addGravity(
                physicsComponent,
                rightX,
                centreY,
                rightGravity);


            addGravity(
                physicsComponent,
                leftX,
                bottomY,
                this.bottomLeftGravity);

            addGravity(
                physicsComponent,
                centreX,
                bottomY,
                bottomGravity);

            addGravity(
                physicsComponent,
                rightX,
                bottomY,
                this.bottomRightGravity);
        }


        float centreGravityLen2 = centreGravity.len2();
        float topGravityLen2 = centreGravity.len2();
        float leftGravityLen2 = centreGravity.len2();
        float rightGravityLen2 = centreGravity.len2();
        float bottomGravityLen2 = centreGravity.len2();
        this.topLeftGravityLen2 = this.topLeftGravity.len2();
        this.topRightGravityLen2 = this.topRightGravity.len2();
        this.bottomLeftGravityLen2 = this.bottomLeftGravity.len2();
        this.bottomRightGravityLen2 = this.bottomRightGravity.len2();

        if ((
            this.bounds.width > MIN_SIZE * 2f
                && this.bounds.height > MIN_SIZE * 2f)
            && (
            centreGravityLen2 - topLeftGravityLen2 > SPLIT_THRESHOLD_2
                || centreGravityLen2 - topRightGravityLen2 > SPLIT_THRESHOLD_2
                || centreGravityLen2 - bottomLeftGravityLen2 > SPLIT_THRESHOLD_2
                || centreGravityLen2 - bottomRightGravityLen2 > SPLIT_THRESHOLD_2
                || centreGravityLen2 - topLeftGravityLen2 < -SPLIT_THRESHOLD_2
                || centreGravityLen2 - topRightGravityLen2 < -SPLIT_THRESHOLD_2
                || centreGravityLen2 - bottomLeftGravityLen2 < -SPLIT_THRESHOLD_2
                || centreGravityLen2 - bottomRightGravityLen2 < -SPLIT_THRESHOLD_2))
        {
            this.hasChildren = true;
            Rectangle boundsTopLeft = new Rectangle(
                this.bounds.x,
                this.bounds.y + halfHeight,
                halfWidth,
                halfHeight);
            Rectangle boundsTopRight = new Rectangle(
                this.bounds.x + halfWidth,
                this.bounds.y + halfHeight,
                halfWidth,
                halfHeight);
            Rectangle boundsBottomLeft = new Rectangle(
                this.bounds.x,
                this.bounds.y,
                halfWidth,
                halfHeight);
            Rectangle boundsBottomRight = new Rectangle(
                this.bounds.x + halfWidth,
                this.bounds.y,
                halfWidth,
                halfHeight);
            this.topLeftNode = new GravityCacheNode(
                boundsTopLeft,
                this.topLeftGravity,
                topGravity,
                leftGravity,
                centreGravity,
                this.topLeftGravityLen2,
                topGravityLen2,
                leftGravityLen2,
                centreGravityLen2,
                staticPhysicsComponentsWithMass);
            this.topRightNode = new GravityCacheNode(
                boundsTopRight,
                topGravity,
                this.topRightGravity,
                centreGravity,
                rightGravity,
                topGravityLen2,
                this.topRightGravityLen2,
                centreGravityLen2,
                rightGravityLen2,
                staticPhysicsComponentsWithMass);
            this.bottomLeftNode = new GravityCacheNode(
                boundsBottomLeft,
                leftGravity,
                centreGravity,
                this.bottomLeftGravity,
                bottomGravity,
                leftGravityLen2,
                centreGravityLen2,
                this.bottomLeftGravityLen2,
                bottomGravityLen2,
                staticPhysicsComponentsWithMass);
            this.bottomRightNode = new GravityCacheNode(
                boundsBottomRight,
                centreGravity,
                rightGravity,
                bottomGravity,
                this.bottomRightGravity,
                centreGravityLen2,
                rightGravityLen2,
                bottomGravityLen2,
                this.bottomRightGravityLen2,
                staticPhysicsComponentsWithMass);
        }
        else
        {
            this.hasChildren = false;

            this.topLeftNode = null;
            this.topRightNode = null;
            this.bottomLeftNode = null;
            this.bottomRightNode = null;
        }
    }

    private void addGravity(
        PhysicsComponent physicsComponent,
        float x,
        float y,
        Vector2 pooledVector2)
    {
        float len2 = x * x + y * y;
        double len = Math.sqrt(len2);

        float scale = physicsComponent.getMass() / len2;

        x *= scale / len;
        y *= scale / len;

        pooledVector2.add(x, y);
    }

    public GravityCacheNode(
        Rectangle bounds,
        Vector2 topLeftGravity,
        Vector2 topRightGravity,
        Vector2 bottomLeftGravity,
        Vector2 bottomRightGravity,
        float topLeftGravityLen2,
        float topRightGravityLen2,
        float bottomLeftGravityLen2,
        float bottomRightGravityLen2,
        ArrayList<PhysicsComponent> staticPhysicsComponentsWithMass)
    {
        this.bounds = bounds;

        this.topLeftGravity = topLeftGravity;
        this.topRightGravity = topRightGravity;
        this.bottomLeftGravity = bottomLeftGravity;
        this.bottomRightGravity = bottomRightGravity;

        Vector2 centreGravity = new Vector2();
        Vector2 topGravity = new Vector2();
        Vector2 leftGravity = new Vector2();
        Vector2 rightGravity = new Vector2();
        Vector2 bottomGravity = new Vector2();

        float halfWidth = bounds.width / 2f;
        float halfHeight = bounds.height / 2f;
        this.centre = PooledVector2.newVector2(0f, 0f);
        bounds.getCenter(this.centre);

        for (PhysicsComponent physicsComponent : staticPhysicsComponentsWithMass)
        {
            float leftX = physicsComponent.getPosition().x - bounds.x;
            float centreX = physicsComponent.getPosition().x - (bounds.x + halfWidth);
            float rightX = physicsComponent.getPosition().x - (bounds.x + bounds.width);

            float topY = physicsComponent.getPosition().y - (bounds.y + bounds.height);
            float centreY = physicsComponent.getPosition().y - (bounds.y + halfHeight);
            float bottomY = physicsComponent.getPosition().y - bounds.y;

            addGravity(
                physicsComponent,
                centreX,
                topY,
                topGravity);

            addGravity(
                physicsComponent,
                leftX,
                centreY,
                leftGravity);

            addGravity(
                physicsComponent,
                centreX,
                centreY,
                centreGravity);

            addGravity(
                physicsComponent,
                rightX,
                centreY,
                rightGravity);

            addGravity(
                physicsComponent,
                centreX,
                bottomY,
                bottomGravity);
        }


        float centreGravityLen2 = centreGravity.len2();
        float topGravityLen2 = centreGravity.len2();
        float leftGravityLen2 = centreGravity.len2();
        float rightGravityLen2 = centreGravity.len2();
        float bottomGravityLen2 = centreGravity.len2();
        this.topLeftGravityLen2 = this.topLeftGravity.len2();
        this.topRightGravityLen2 = this.topRightGravity.len2();
        this.bottomLeftGravityLen2 = this.bottomLeftGravity.len2();
        this.bottomRightGravityLen2 = this.bottomRightGravity.len2();

        if ((
            this.bounds.width > MIN_SIZE * 2f
                && this.bounds.height > MIN_SIZE * 2f)
            && (
            centreGravityLen2 - topLeftGravityLen2 > SPLIT_THRESHOLD_2
                || centreGravityLen2 - topRightGravityLen2 > SPLIT_THRESHOLD_2
                || centreGravityLen2 - bottomLeftGravityLen2 > SPLIT_THRESHOLD_2
                || centreGravityLen2 - bottomRightGravityLen2 > SPLIT_THRESHOLD_2
                || centreGravityLen2 - topLeftGravityLen2 < -SPLIT_THRESHOLD_2
                || centreGravityLen2 - topRightGravityLen2 < -SPLIT_THRESHOLD_2
                || centreGravityLen2 - bottomLeftGravityLen2 < -SPLIT_THRESHOLD_2
                || centreGravityLen2 - bottomRightGravityLen2 < -SPLIT_THRESHOLD_2))
        {
            this.hasChildren = true;

            Rectangle boundsTopLeft = new Rectangle(
                this.bounds.x,
                this.bounds.y + halfHeight,
                halfWidth,
                halfHeight);
            Rectangle boundsTopRight = new Rectangle(
                this.bounds.x + halfWidth,
                this.bounds.y + halfHeight,
                halfWidth,
                halfHeight);
            Rectangle boundsBottomLeft = new Rectangle(
                this.bounds.x,
                this.bounds.y,
                halfWidth,
                halfHeight);
            Rectangle boundsBottomRight = new Rectangle(
                this.bounds.x + halfWidth,
                this.bounds.y,
                halfWidth,
                halfHeight);
            this.topLeftNode = new GravityCacheNode(
                boundsTopLeft,
                this.topLeftGravity,
                topGravity,
                leftGravity,
                centreGravity,
                this.topLeftGravityLen2,
                topGravityLen2,
                leftGravityLen2,
                centreGravityLen2,
                staticPhysicsComponentsWithMass);
            this.topRightNode = new GravityCacheNode(
                boundsTopRight,
                topGravity,
                this.topRightGravity,
                centreGravity,
                rightGravity,
                topGravityLen2,
                this.topRightGravityLen2,
                centreGravityLen2,
                rightGravityLen2,
                staticPhysicsComponentsWithMass);
            this.bottomLeftNode = new GravityCacheNode(
                boundsBottomLeft,
                leftGravity,
                centreGravity,
                this.bottomLeftGravity,
                bottomGravity,
                leftGravityLen2,
                centreGravityLen2,
                this.bottomLeftGravityLen2,
                bottomGravityLen2,
                staticPhysicsComponentsWithMass);
            this.bottomRightNode = new GravityCacheNode(
                boundsBottomRight,
                centreGravity,
                rightGravity,
                bottomGravity,
                this.bottomRightGravity,
                centreGravityLen2,
                rightGravityLen2,
                bottomGravityLen2,
                this.bottomRightGravityLen2,
                staticPhysicsComponentsWithMass);
        }
        else
        {
            this.hasChildren = false;

            this.topLeftNode = null;
            this.topRightNode = null;
            this.bottomLeftNode = null;
            this.bottomRightNode = null;
        }

    }

    public Rectangle getBounds()
    {
        return bounds;
    }

    public GravityCacheNode getTopLeftNode()
    {
        return topLeftNode;
    }

    public GravityCacheNode getTopRightNode()
    {
        return topRightNode;
    }

    public GravityCacheNode getBottomLeftNode()
    {
        return bottomLeftNode;
    }

    public GravityCacheNode getBottomRightNode()
    {
        return bottomRightNode;
    }

    public PooledVector2 getGravity(Vector2 position)
    {
        if (this.hasChildren)
        {
            if (position.x < centre.x)
            {
                if (position.y < centre.y)
                {
                    return this.bottomLeftNode.getGravity(position);
                }
                else
                {
                    return this.topLeftNode.getGravity(position);
                }
            }
            else
            {
                if (position.y < centre.y)
                {
                    return this.bottomRightNode.getGravity(position);
                }
                else
                {
                    return this.topRightNode.getGravity(position);
                }
            }
        }

        // In this node.
        float leftXWeight = (position.x - this.bounds.x) / this.bounds.width / 2f;
        float bottomYWeight = (position.y - this.bounds.y) / this.bounds.height / 2f;
        float rightXWeight = 0.5f - leftXWeight;
        float topYWeight = 0.5f - bottomYWeight;

        PooledVector2 gravity = PooledVector2.newVector2(
            topLeftGravity.x * leftXWeight
                + topRightGravity.x * rightXWeight
                + bottomLeftGravity.x * leftXWeight
                + bottomRightGravity.x + rightXWeight,
            topLeftGravity.y * topYWeight
                + topRightGravity.y * topYWeight
                + bottomLeftGravity.y * bottomYWeight
                + bottomRightGravity.y + bottomYWeight);

        return gravity;
    }
}
