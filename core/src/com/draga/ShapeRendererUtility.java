package com.draga;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class ShapeRendererUtility
{
    public static void arc(
        ShapeRenderer shapeRenderer,
        float x,
        float y,
        float radius,
        float start,
        float degrees,
        float width)
    {
        arc(
            shapeRenderer,
            x,
            y,
            radius,
            start,
            degrees,
            Math.max(1, (int) (6 * (float) Math.cbrt(radius) * (degrees / 360.0f))),
            width);
    }

    public static void arc(
        ShapeRenderer shapeRenderer,
        float x,
        float y,
        float radius,
        float start,
        float degrees,
        int segments,
        float width)
    {
        if (segments <= 0)
        {
            throw new IllegalArgumentException("segments must be > 0.");
        }

        float theta = (2 * MathUtils.PI * (degrees / 360.0f)) / segments;
        float cos = MathUtils.cos(theta);
        float sin = MathUtils.sin(theta);
        float cx = radius * MathUtils.cos(start * MathUtils.degreesToRadians);
        float cy = radius * MathUtils.sin(start * MathUtils.degreesToRadians);

        ShapeRenderer.ShapeType oldShapeType = shapeRenderer.getCurrentType();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < segments * 2; i += 2)
        {
            Vector2 startVertex = new Vector2(x + cx, y + cy);
            float temp = cx;
            cx = cos * cx - sin * cy;
            cy = sin * temp + cos * cy;
            Vector2 endVertex = new Vector2(x + cx, y + cy);
            shapeRenderer.rectLine(startVertex, endVertex, width);
        }

        shapeRenderer.set(oldShapeType);
    }

    public static void dashedCircle(
        ShapeRenderer shapeRenderer,
        float x,
        float y,
        float radius,
        int numArcs,
        float arcLengthDegs,
        float startOffset,
        int segments,
        float lineWidth)
    {

        for (int i = 0; i < numArcs; i++)
        {
            float startDegs = startOffset + (i * (360 / numArcs)) - (arcLengthDegs / 2);
            arc(shapeRenderer, x, y, radius, startDegs, arcLengthDegs, segments, lineWidth);
        }
    }
}
