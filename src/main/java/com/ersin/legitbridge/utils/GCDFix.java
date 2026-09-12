package com.ersin.legitbridge.utils;

import net.minecraft.client.MinecraftClient;

public class GCDFix {

    /**
     * Applies the GCD (Greatest Common Divisor) fix to a target rotation angle.
     * Minecraft converts mouse pixel movements to float rotations using a specific multiplier based on the user's mouse sensitivity.
     * Anti-cheats check if rotations are valid multiples of this GCD.
     *
     * @param targetRot The target rotation (yaw or pitch) you want to achieve.
     * @param prevRot The previous rotation (yaw or pitch) before the change.
     * @param client The Minecraft client instance.
     * @return The anti-cheat safe rotation angle closest to the target rotation.
     */
    public static float applyGCD(float targetRot, float prevRot, MinecraftClient client) {
        if (client.options == null) return targetRot;
        
        float diff = targetRot - prevRot;
        
        // Calculate the exact GCD multiplier Minecraft uses internally
//? if <=1.19.2 {
        /*double f = client.options.mouseSensitivity * 0.6 + 0.2;
*///?} else {
        double f = client.options.getMouseSensitivity().getValue() * 0.6 + 0.2;
//?}
        double f1 = f * f * f * 8.0;
        double gcd = f1 * 0.15;
        
        // Round the raw difference to the nearest valid step
        int steps = (int) Math.round(diff / gcd);
        
        return prevRot + (float)(steps * gcd);
    }
}
