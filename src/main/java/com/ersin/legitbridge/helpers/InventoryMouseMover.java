package com.ersin.legitbridge.helpers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.lwjgl.glfw.GLFW;

public class InventoryMouseMover {

    private static boolean moving = false;
    private static double startX, startY;
    private static double targetX, targetY;
    private static long startTime;
    private static long durationMs;
    private static Runnable onComplete;

    public static void tick() {
        if (!moving) return;

        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed >= durationMs) {
            moving = false;
            MinecraftClient client = MinecraftClient.getInstance();
            GLFW.glfwSetCursorPos(client.getWindow().getHandle(), targetX, targetY);
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        // Calculate progress with a simple ease-in-out curve
        double progress = (double) elapsed / durationMs;
        double ease = progress < 0.5 ? 2 * progress * progress : -1 + (4 - 2 * progress) * progress;

        // Apply bezier/ease curve
        double currentX = startX + (targetX - startX) * ease;
        double currentY = startY + (targetY - startY) * ease;

        MinecraftClient client = MinecraftClient.getInstance();
        GLFW.glfwSetCursorPos(client.getWindow().getHandle(), currentX, currentY);
    }

    public static void moveToSlot(Slot slot, long duration, Runnable callback) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || !(client.currentScreen instanceof HandledScreen)) {
            if (callback != null) callback.run();
            return;
        }
        
        HandledScreen<?> screen = (HandledScreen<?>) client.currentScreen;
        
        // Approximate standard HandledScreen offsets since fields are protected without accessor
        // Default chest/inventory size
        int backgroundWidth = 176; 
        int backgroundHeight = 166;
        int guiLeft = (screen.width - backgroundWidth) / 2;
        int guiTop = (screen.height - backgroundHeight) / 2;

        // Slot positions are relative to guiLeft and guiTop
        int slotCenterX = guiLeft + slot.x + 8;
        int slotCenterY = guiTop + slot.y + 8;

        // Convert Minecraft GUI coordinates to window pixel coordinates
        double scale = client.getWindow().getScaleFactor();
        targetX = slotCenterX * scale;
        targetY = slotCenterY * scale;

        startX = client.mouse.getX();
        startY = client.mouse.getY();

        durationMs = duration;
        startTime = System.currentTimeMillis();
        onComplete = callback;
        moving = true;
    }
    
    public static boolean isMoving() {
        return moving;
    }
}
