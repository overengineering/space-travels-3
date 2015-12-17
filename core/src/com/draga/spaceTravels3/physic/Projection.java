package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.SpaceTravels3;
import com.draga.spaceTravels3.component.PhysicsComponent;
import com.draga.spaceTravels3.gameEntity.Pickup;
import com.draga.spaceTravels3.gameEntity.Planet;
import com.draga.utils.GraphicsUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Projection
{
    private final int                                      steps;
    private final float                                    stepTime;
    private final ArrayList<Vector2>                       positions;
    private       LinkedHashMap<PhysicsComponent, Integer> collisions;
//    private ArrayList<ProjectionPoint>

    public Projection(int steps, float stepTime)
    {
        this.steps = steps;
        this.stepTime = stepTime;

        this.positions = new ArrayList<>(steps);
        this.collisions = new LinkedHashMap<>();
    }

    public void addPosition(Vector2 position)
    {
        this.positions.add(position);
    }
    
    public void addCollision(PhysicsComponent physicsComponent, int step)
    {
        if (!this.collisions.containsKey(physicsComponent))
        {
            this.collisions.put(physicsComponent, step);
        }
    }

    public void draw()
    {
        GraphicsUtils.enableBlending();

        Iterator<PhysicsComponent> collidingPhysicComponentIterator =
            collisions.keySet().iterator();

        float fromAlpha = 1f;
        float toAlpha = 0.3f;

        PhysicsComponent nextCollidingPhysicComponent = collidingPhysicComponentIterator.hasNext()
            ? collidingPhysicComponentIterator.next()
            : null;
        Color color = getColor(nextCollidingPhysicComponent);

        SpaceTravels3.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 1; i < positions.size(); i += 2)
        {
            if (nextCollidingPhysicComponent != null
                && collisions.get(nextCollidingPhysicComponent) < i)
            {
                if (nextCollidingPhysicComponent.getOwnerClass().equals(Planet.class))
                {
                    break;
                }
                nextCollidingPhysicComponent = collidingPhysicComponentIterator.hasNext()
                    ? collidingPhysicComponentIterator.next()
                    : null;
                color = getColor(nextCollidingPhysicComponent);
            }
            float alpha = fromAlpha - ((fromAlpha - toAlpha) / positions.size() * i);
            SpaceTravels3.shapeRenderer.setColor(color.r, color.g, color.b, alpha);

            Vector2 projectionPointA = positions.get(i);
            Vector2 projectionPointB = positions.get(i - 1);

            SpaceTravels3.shapeRenderer.line(
                projectionPointA.x, projectionPointA.y,
                projectionPointB.x, projectionPointB.y);
        }
        SpaceTravels3.shapeRenderer.end();

        GraphicsUtils.disableBlending();
    }

    private Color getColor(PhysicsComponent physicsComponent)
    {
        if (physicsComponent == null)
        {
            return Constants.Visual.HUD_TRAJECTORY_LINE_COLOR_NEUTRAL;
        }
        if (physicsComponent.getOwnerClass().equals(Planet.class))
        {
            return Constants.Visual.HUD_TRAJECTORY_LINE_COLOR_PLANET;
        }
        if (physicsComponent.getOwnerClass().equals(Pickup.class))
        {
            return Color.GREEN;
        }

        return Constants.Visual.HUD_TRAJECTORY_LINE_COLOR_NEUTRAL;
    }
}
