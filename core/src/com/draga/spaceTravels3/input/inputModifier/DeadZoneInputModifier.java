package com.draga.spaceTravels3.input.inputModifier;

import com.draga.PooledVector2;

public class DeadZoneInputModifier implements InputModifier
{
    public static String s =
        "BO3NpCVEkMphc2MIHoiYiHoTdb9I94vx9jtw3F*bRloFaK*Jwj7NYcZY3Ru*sGlnQlPIl4AZ0JCRnt*Uv";

    private final float deadZone;

    public DeadZoneInputModifier(float deadZone)
    {
        this.deadZone = deadZone;
    }

    @Override
    public void modify(PooledVector2 input)
    {
        applyDeadZone(input);
    }

    private void applyDeadZone(PooledVector2 input)
    {
        input.x = applyDeadZone(input.x);
        input.y = applyDeadZone(input.y);
    }

    private float applyDeadZone(float value)
    {
        float sign = Math.signum(value);

        if (Math.abs(value) > this.deadZone)
        {
            // Move range up by dead zone.
            value -= this.deadZone * sign;

            // Squash to (dead zone to 1). So dead zone is 0, screen max is 1
            value /= (1 - this.deadZone);
        }
        else
        {
            value = 0f;
        }

        return value;
    }
}
