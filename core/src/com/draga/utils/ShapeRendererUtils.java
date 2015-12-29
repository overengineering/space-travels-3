package com.draga.utils;

import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.draga.Vector2;

public final class ShapeRendererUtils
{
    private static final String LOGGING_TAG =
        ShapeRendererUtils.class.getSimpleName();

    private ShapeRendererUtils()
    {
    }

    public static void dashedCircle(
        ShapeRenderer shapeRenderer,
        float x,
        float y,
        float radius,
        int numArcs,
        float arcLengthDegrees,
        float startOffset,
        int segments,
        float lineWidth)
    {
        for (int i = 0; i < numArcs; i++)
        {
            float startDegrees = startOffset + (i * (360 / numArcs)) - (arcLengthDegrees / 2);
            arc(shapeRenderer, x, y, radius, startDegrees, arcLengthDegrees, segments, lineWidth);
        }
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
        float halfWidth = width * 0.5f;

        float colorBits = shapeRenderer.getColor().toFloatBits();

        ShapeRenderer.ShapeType oldShapeType = shapeRenderer.getCurrentType();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        ImmediateModeRenderer renderer = shapeRenderer.getRenderer();

        try (Vector2 perpendicular = Vector2.newVector2(0f, 0f))
        {
            float x2 = x + cx;
            float y2 = y + cy;
            for (int i = 0; i < segments; i++)
            {
                float x1 = x2;
                float y1 = y2;
                float temp = cx;
                cx = cos * cx - sin * cy;
                cy = sin * temp + cos * cy;
                x2 = x + cx;
                y2 = y + cy;

                perpendicular.set(y2 - y1, x1 - x2).nor();
                float tx = perpendicular.x * halfWidth;
                float ty = perpendicular.y * halfWidth;
                renderer.color(colorBits);
                renderer.vertex(x1 + tx, y1 + ty, 0);
                renderer.color(colorBits);
                renderer.vertex(x1 - tx, y1 - ty, 0);
                renderer.color(colorBits);
                renderer.vertex(x2 + tx, y2 + ty, 0);

                renderer.color(colorBits);
                renderer.vertex(x2 - tx, y2 - ty, 0);
                renderer.color(colorBits);
                renderer.vertex(x2 + tx, y2 + ty, 0);
                renderer.color(colorBits);
                renderer.vertex(x1 - tx, y1 - ty, 0);
            }
        }

        shapeRenderer.set(oldShapeType);
    }

    public static void dashedCircle(
        ShapeRenderer shapeRenderer,
        float x,
        float y,
        float radius,
        int numArcs,
        float arcLengthDegrees,
        float startOffset,
        float lineWidth)
    {
        for (int i = 0; i < numArcs; i++)
        {
            float startDegrees = startOffset + (i * (360 / numArcs)) - (arcLengthDegrees / 2);
            arc(shapeRenderer, x, y, radius, startDegrees, arcLengthDegrees, lineWidth);
        }
    }

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
}
