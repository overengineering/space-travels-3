package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

import java.util.ArrayList;

public class GravityCacheNode
{
    public static final  GravityCacheNode NULL_GRAVITY_CACHE_NODE =
        new GravityCacheNode(new Rectangle(), new ArrayList<PhysicsComponent>())
        {
            @Override
            public GravityCacheNode getParentNode()
            {
                return null;
            }

            @Override
            public boolean contains(Vector2 vector2)
            {
                return true;
            }

            @Override
            public PooledVector2 getGravity(Vector2 position)
            {
                return PooledVector2.newVector2(0f, 0f);
            }

            @Override
            public boolean hasChildren()
            {
                return false;
            }
        };

    private static final String           LOGGING_TAG             =
        GravityCacheNode.class.getSimpleName();

    private static final float            SPLIT_THRESHOLD         = 1f;
    private static final float            MIN_AREA                = 1f;

    private final Rectangle bounds;

    private Vector2 centre;
    private float   halfHeight;
    private float   halfWidth;

    private GravityCacheNode parentNode;
    private boolean          hasChildren;

    private GravityCacheNode topLeftNode;
    private GravityCacheNode topRightNode;
    private GravityCacheNode bottomLeftNode;
    private GravityCacheNode bottomRightNode;

    private Vector2 topLeftGravity;
    private Vector2 topRightGravity;
    private Vector2 bottomLeftGravity;
    private Vector2 bottomRightGravity;

    private float topLeftGravityLen;
    private float topRightGravityLen;
    private float bottomLeftGravityLen;
    private float bottomRightGravityLen;

    public GravityCacheNode(
        Rectangle bounds,
        ArrayList<PhysicsComponent> staticPhysicsComponentsWithMass)
    {
        this.parentNode = NULL_GRAVITY_CACHE_NODE;

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
            float leftX = physicsComponent.getPosition().x - this.bounds.x;
            float rightX = physicsComponent.getPosition().x - (this.bounds.x + this.bounds.width);

            float topY = physicsComponent.getPosition().y - (this.bounds.y + this.bounds.height);
            float bottomY = physicsComponent.getPosition().y - this.bounds.y;

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

        this.topLeftGravityLen = this.topLeftGravity.len();
        this.topRightGravityLen = this.topRightGravity.len();
        this.bottomLeftGravityLen = this.bottomLeftGravity.len();
        this.bottomRightGravityLen = this.bottomRightGravity.len();
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

        float centreGravityLen = centreGravity.len();
        float topGravityLen = topGravity.len();
        float leftGravityLen = leftGravity.len();
        float rightGravityLen = rightGravity.len();
        float bottomGravityLen = bottomGravity.len();

        if (this.bounds.width * this.bounds.height > MIN_AREA
            && (
            Math.abs(centreGravityLen - this.topLeftGravityLen) > SPLIT_THRESHOLD
                || Math.abs(centreGravityLen - this.topRightGravityLen) > SPLIT_THRESHOLD
                || Math.abs(centreGravityLen - this.bottomLeftGravityLen) > SPLIT_THRESHOLD
                || Math.abs(centreGravityLen - this.bottomRightGravityLen) > SPLIT_THRESHOLD))
        {
            this.hasChildren = true;

            Rectangle boundsTopLeft = new Rectangle(
                this.bounds.x,
                this.bounds.y + this.halfHeight,
                this.halfWidth,
                this.halfHeight);
            Rectangle boundsTopRight = new Rectangle(
                this.bounds.x + this.halfWidth,
                this.bounds.y + this.halfHeight,
                this.halfWidth,
                this.halfHeight);
            Rectangle boundsBottomLeft = new Rectangle(
                this.bounds.x,
                this.bounds.y,
                this.halfWidth,
                this.halfHeight);
            Rectangle boundsBottomRight = new Rectangle(
                this.bounds.x + this.halfWidth,
                this.bounds.y,
                this.halfWidth,
                this.halfHeight);
            this.topLeftNode = new GravityCacheNode(
                this,
                boundsTopLeft,
                staticPhysicsComponentsWithMass, this.topLeftGravity,
                topGravity,
                leftGravity,
                centreGravity,
                this.topLeftGravityLen,
                topGravityLen,
                leftGravityLen,
                centreGravityLen);
            this.topRightNode = new GravityCacheNode(
                this,
                boundsTopRight,
                staticPhysicsComponentsWithMass, topGravity,
                this.topRightGravity,
                centreGravity,
                rightGravity,
                topGravityLen,
                this.topRightGravityLen,
                centreGravityLen,
                rightGravityLen);
            this.bottomLeftNode = new GravityCacheNode(
                this,
                boundsBottomLeft,
                staticPhysicsComponentsWithMass, leftGravity,
                centreGravity,
                this.bottomLeftGravity,
                bottomGravity,
                leftGravityLen,
                centreGravityLen,
                this.bottomLeftGravityLen,
                bottomGravityLen);
            this.bottomRightNode = new GravityCacheNode(
                this,
                boundsBottomRight,
                staticPhysicsComponentsWithMass, centreGravity,
                rightGravity,
                bottomGravity,
                this.bottomRightGravity,
                centreGravityLen,
                rightGravityLen,
                bottomGravityLen,
                this.bottomRightGravityLen);
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
        if (len2 != 0)
        {
            double len = Math.sqrt(len2);

            float scale = physicsComponent.getMass() / len2;

            x *= scale / len;
            y *= scale / len;
        }
        else
        {
            x = Float.MAX_VALUE;
            y = Float.MAX_VALUE;
        }
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
            float leftX = physicsComponent.getPosition().x - this.bounds.x;
            float centreX = physicsComponent.getPosition().x - (this.bounds.x + this.halfWidth);
            float rightX = physicsComponent.getPosition().x - (this.bounds.x + this.bounds.width);

            float topY = physicsComponent.getPosition().y - (this.bounds.y + this.bounds.height);
            float centreY = physicsComponent.getPosition().y - (this.bounds.y + this.halfHeight);
            float bottomY = physicsComponent.getPosition().y - this.bounds.y;

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
        GravityCacheNode parentNode,
        Rectangle bounds,
        ArrayList<PhysicsComponent> staticPhysicsComponentsWithMass,
        Vector2 topLeftGravity,
        Vector2 topRightGravity,
        Vector2 bottomLeftGravity,
        Vector2 bottomRightGravity,
        float topLeftGravityLen,
        float topRightGravityLen,
        float bottomLeftGravityLen,
        float bottomRightGravityLen)
    {
        this.parentNode = parentNode;

        this.bounds = bounds;

        this.calculateCentreAndHalfMeasures(bounds);

        this.topLeftGravity = topLeftGravity;
        this.topRightGravity = topRightGravity;
        this.bottomLeftGravity = bottomLeftGravity;
        this.bottomRightGravity = bottomRightGravity;

        this.topLeftGravityLen = topLeftGravityLen;
        this.topRightGravityLen = topRightGravityLen;
        this.bottomLeftGravityLen = bottomLeftGravityLen;
        this.bottomRightGravityLen = bottomRightGravityLen;

        this.calculateChildren(staticPhysicsComponentsWithMass);
    }

    public GravityCacheNode getParentNode()
    {
        return this.parentNode;
    }

    public boolean contains(Vector2 vector2)
    {
        return this.bounds.contains(vector2.x, vector2.y);
    }

    public GravityCacheNode getTopLeftNode()
    {
        return this.topLeftNode;
    }

    public GravityCacheNode getTopRightNode()
    {
        return this.topRightNode;
    }

    public GravityCacheNode getBottomLeftNode()
    {
        return this.bottomLeftNode;
    }

    public GravityCacheNode getBottomRightNode()
    {
        return this.bottomRightNode;
    }

    public PooledVector2 getGravity(Vector2 position)
    {
        float leftXWeight = (position.x - this.bounds.x) / this.bounds.width / 2f;
        float bottomYWeight = (position.y - this.bounds.y) / this.bounds.height / 2f;
        float rightXWeight = 0.5f - leftXWeight;
        float topYWeight = 0.5f - bottomYWeight;

        PooledVector2 gravity = PooledVector2.newVector2(
            this.topLeftGravity.x * leftXWeight
                + this.topRightGravity.x * rightXWeight
                + this.bottomLeftGravity.x * leftXWeight
                + this.bottomRightGravity.x * rightXWeight,
            this.topLeftGravity.y * topYWeight
                + this.topRightGravity.y * topYWeight
                + this.bottomLeftGravity.y * bottomYWeight
                + this.bottomRightGravity.y * bottomYWeight);
        return gravity;
    }

    public boolean hasChildren()
    {
        return this.hasChildren;
    }
    
    public Vector2 getCentre()
    {
        return this.centre;
    }

    public float getY()
    {
        return this.bounds.y;
    }

    public float getX()
    {
        return this.bounds.x;
    }

    public float getWidth()
    {
        return this.bounds.width;
    }

    public float getHeight()
    {
        return this.bounds.height;
    }
}
