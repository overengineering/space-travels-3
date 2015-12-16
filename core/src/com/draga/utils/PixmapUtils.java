package com.draga.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public final class PixmapUtils
{
    private static final String LOGGING_TAG =
        PixmapUtils.class.getSimpleName();

    private PixmapUtils()
    {
    }

    public static void dashedCircle(
        Pixmap pixmap,
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
            arc(pixmap, x, y, radius, startDegrees, arcLengthDegrees, segments, lineWidth);
        }
    }

    public static void arc(
        Pixmap pixmap,
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

        // Avoid the overlapping of triangles to increase the transparency
        Pixmap.setBlending(Pixmap.Blending.None);

        float theta = (2 * MathUtils.PI * (degrees / 360.0f)) / segments;
        float cos = MathUtils.cos(theta);
        float sin = MathUtils.sin(theta);
        float cx = radius * MathUtils.cos(start * MathUtils.degreesToRadians);
        float cy = radius * MathUtils.sin(start * MathUtils.degreesToRadians);
        float halfWidth = width * 0.5f;

        Vector2 perpendicular = new Vector2();
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

            int p1x = Math.round(x1 + tx);
            int p1y = Math.round(y1 + ty);
            int p2x = Math.round(x1 - tx);
            int p2y = Math.round(y1 - ty);
            int p3x = Math.round(x2 + tx);
            int p3y = Math.round(y2 + ty);
            int p4x = Math.round(x2 - tx);
            int p4y = Math.round(y2 - ty);

            // Two triangles to form a rectangle.
            pixmap.fillTriangle(
                p1x,
                p1y,
                p2x,
                p2y,
                p3x,
                p3y
            );

            pixmap.fillTriangle(
                p4x,
                p4y,
                p3x,
                p3y,
                p2x,
                p2y
            );
        }
    }
}
