package com.github.nxkoo.nxgeckolib;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class NxMathHelper {

    @OnlyIn(Dist.CLIENT)
    public static float interpolateAngle(float f1, float f2, float f3) {
        return f2 + f1 * wrapDegrees(f3 - f2);
    }

    /**
     * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
     */
    public static float wrapDegrees(float value) {
        float f = value % 360.0F;
        if (f >= 180.0F) {
            f -= 360.0F;
        }

        if (f < -180.0F) {
            f += 360.0F;
        }

        return f;
    }
}
