package com.ersin.legitbridge.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class RotationUtil {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Random random = new Random();

    /**
     * Calculates the Greatest Common Divisor (GCD) for mouse rotations in Minecraft.
     * This ensures the server sees rotation increments that match the user's mouse sensitivity,
     * making the rotations appear indistinguishable from human mouse movements.
     */
    public static float getGCD() {
//? if >1.19.2 {
        float f = (float) (client.options.getMouseSensitivity().getValue().floatValue()) * 0.6F + 0.2F;
//?} else {
        /*float f = (float) ((float)client.options.mouseSensitivity) * 0.6F + 0.2F;*///?}
        float f1 = f * f * f * 8.0F;
        return f1 * 0.15F;
    }

    /**
     * Applies the GCD fix to a given delta rotation (yaw or pitch).
     */
    public static float applyGCD(float delta) {
        float gcd = getGCD();
        return Math.round(delta / gcd) * gcd;
    }

    /**
     * Calculates a fixed yaw/pitch to reach the target, respecting GCD.
     */
    public static float[] getFixedRotations(float currentYaw, float currentPitch, float targetYaw, float targetPitch, float speed) {
        float deltaYaw = MathHelper.wrapDegrees(targetYaw - currentYaw);
        float deltaPitch = MathHelper.wrapDegrees(targetPitch - currentPitch);
        
        // Apply speed limit
        if (deltaYaw > speed) deltaYaw = speed;
        if (deltaYaw < -speed) deltaYaw = -speed;
        if (deltaPitch > speed) deltaPitch = speed;
        if (deltaPitch < -speed) deltaPitch = -speed;

        // Apply Jitter (micro human movements)
        deltaYaw += (random.nextFloat() - 0.5f) * 0.5f;
        deltaPitch += (random.nextFloat() - 0.5f) * 0.5f;

        // Apply GCD fix
        deltaYaw = applyGCD(deltaYaw);
        deltaPitch = applyGCD(deltaPitch);

        return new float[]{currentYaw + deltaYaw, currentPitch + deltaPitch};
    }
}
