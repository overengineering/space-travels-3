package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

import java.util.ArrayList;

public class GravityCacheNode
{
    private static final String LOGGING_TAG = GravityCacheNode.class.getSimpleName();

    private static final float SPLIT_THRESHOLD_2 = 100F;
    private static final float MIN_AREA          = 1f;
    private final Rectangle bounds;
    private       boolean hasChildren;
    private       GravityCacheNode topLeftNode;
    private       GravityCacheNode topRightNode;
    private       GravityCacheNode bottomLeftNode;
    private       GravityCacheNode bottomRightNode;
    private       Vector2 centre;
    private       float halfHeight;
    private       float halfWidth;

    private Vector2 topLeftGravity;
    private Vector2 topRightGravity;
    private Vector2 bottomLeftGravity;
    private Vector2 bottomRightGravity;

    private float topLeftGravityLen2;
    private float topRightGravityLen2;
    private float bottomLeftGravityLen2;
    private float bottomRightGravityLen2;

    public GravityCacheNode(
        Rectangle bounds,
        ArrayList<PhysicsComponent> staticPhysicsComponentsWithMass)
    {
        this.bounds = bounds;

        calculateCentreAndHalfMeasures(bounds);

        calculateCornerGravity(staticPhysicsComponentsWithMass);

        calculateChildren(staticPhysicsComponentsWithMass);
    }

    private void calculateCentreAndHalfMeasures(Rectangle bounds)
    {
        this.centre = bounds.getCenter(new Vector2());
        this.halfWidth = bounds.width / 2f;
        this.halfHeight = bounds.height / 2f;
    }

    private void calculateCornerGravity(ArrayList<PhysicsComponent> staticPhysicsComponentsWithMass)
    {
        this.topLeftGravity = new Vector2();
        this.topRightGravity = new Vector2();
        this.bottomLeftGravity = new Vector2();
        this.bottomRightGravity = new Vector2();

        for (PhysicsComponent physicsComponent : staticPhysicsComponentsWithMass)
        {
            float leftX = physicsComponent.getPosition().x - bounds.x;
            float rightX = physicsComponent.getPosition().x - (bounds.x + bounds.width);

            float topY = physicsComponent.getPosition().y - (bounds.y + bounds.height);
            float bottomY = physicsComponent.getPosition().y - bounds.y;

            addGravity(
                physicsComponent,
                leftX,
                topY,
                this.topLeftGravity);

            addGravity(
                physicsComponent,
                rightX,
                topY,
                this.topRightGravity);

            addGravity(
                physicsComponent,
                leftX,
                bottomY,
                this.bottomLeftGravity);

            addGravity(
                physicsComponent,
                rightX,
                bottomY,
                this.bottomRightGravity);
        }

        this.topLeftGravityLen2 = this.topLeftGravity.len2();
        this.topRightGravityLen2 = this.topRightGravity.len2();
        this.bottomLeftGravityLen2 = this.bottomLeftGravity.len2();
        this.bottomRightGravityLen2 = this.bottomRightGravity.len2();
    }

    private void calculateChildren(ArrayList<PhysicsComponent> staticPhysicsComponentsWithMass)
    {
        Vector2 centreGravity = new Vector2();
        Vector2 topGravity = new Vector2();
        Vector2 leftGravity = new Vector2();
        Vector2 rightGravity = new Vector2();
        Vector2 bottomGravity = new Vector2();

        calculateSideAndCentreGravity(
            staticPhysicsComponentsWithMass,
            centreGravity,
            topGravity,
            leftGravity,
            rightGravity,
            bottomGravity);

        float centreGravityLen2 = centreGravity.len2();
        float topGravityLen2 = topGravity.len2();
        float leftGravityLen2 = leftGravity.len2();
        float rightGravityLen2 = rightGravity.len2();
        float bottomGravityLen2 = bottomGravity.len2();

        if (this.bounds.width * this.bounds.height > MIN_AREA
            && (
            Math.abs(centreGravityLen2 - topLeftGravityLen2) > SPLIT_THRESHOLD_2
                || Math.abs(centreGravityLen2 - topRightGravityLen2) > SPLIT_THRESHOLD_2
                || Math.abs(centreGravityLen2 - bottomLeftGravityLen2) > SPLIT_THRESHOLD_2
                || Math.abs(centreGravityLen2 - bottomRightGravityLen2) > SPLIT_THRESHOLD_2))
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
                staticPhysicsComponentsWithMass, this.topLeftGravity,
                topGravity,
                leftGravity,
                centreGravity,
                this.topLeftGravityLen2,
                topGravityLen2,
                leftGravityLen2,
                centreGravityLen2
            );
            this.topRightNode = new GravityCacheNode(
                boundsTopRight,
                staticPhysicsComponentsWithMass, topGravity,
                this.topRightGravity,
                centreGravity,
                rightGravity,
                topGravityLen2,
                this.topRightGravityLen2,
                centreGravityLen2,
                rightGravityLen2
            );
            this.bottomLeftNode = new GravityCacheNode(
                boundsBottomLeft,
                staticPhysicsComponentsWithMass, leftGravity,
                centreGravity,
                this.bottomLeftGravity,
                bottomGravity,
                leftGravityLen2,
                centreGravityLen2,
                this.bottomLeftGravityLen2,
                bottomGravityLen2
            );
            this.bottomRightNode = new GravityCacheNode(
                boundsBottomRight,
                staticPhysicsComponentsWithMass, centreGravity,
                rightGravity,
                bottomGravity,
                this.bottomRightGravity,
                centreGravityLen2,
                rightGravityLen2,
                bottomGravityLen2,
                this.bottomRightGravityLen2
            );
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
        Vector2 vector2)
    {
        float len2 = x * x + y * y;
        double len = Math.sqrt(len2);

        float scale = physicsComponent.getMass() / len2;

        x *= scale / len;
        y *= scale / len;

        vector2.add(x, y);
    }

    private void calculateSideAndCentreGravity(
        ArrayList<PhysicsComponent> staticPhysicsComponentsWithMass,
        Vector2 centreGravity,
        Vector2 topGravity,
        Vector2 leftGravity,
        Vector2 rightGravity,
        Vector2 bottomGravity)
    {
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
    }

    public GravityCacheNode(
        Rectangle bounds,
        ArrayList<PhysicsComponent> staticPhysicsComponentsWithMass,
        Vector2 topLeftGravity,
        Vector2 topRightGravity,
        Vector2 bottomLeftGravity,
        Vector2 bottomRightGravity,
        float topLeftGravityLen2,
        float topRightGravityLen2,
        float bottomLeftGravityLen2,
        float bottomRightGravityLen2)
    {
        this.bounds = bounds;

        calculateCentreAndHalfMeasures(bounds);

        this.topLeftGravity = topLeftGravity;
        this.topRightGravity = topRightGravity;
        this.bottomLeftGravity = bottomLeftGravity;
        this.bottomRightGravity = bottomRightGravity;

        this.topLeftGravityLen2 = topLeftGravityLen2;
        this.topRightGravityLen2 = topRightGravityLen2;
        this.bottomLeftGravityLen2 = bottomLeftGravityLen2;
        this.bottomRightGravityLen2 = bottomRightGravityLen2;

        calculateChildren(staticPhysicsComponentsWithMass);
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
                + bottomRightGravity.x * rightXWeight,
            topLeftGravity.y * topYWeight
                + topRightGravity.y * topYWeight
                + bottomLeftGravity.y * bottomYWeight
                + bottomRightGravity.y * bottomYWeight);
        return gravity;
    }
}
