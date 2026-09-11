package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;

public class JumpBridge extends Module {
    public JumpBridge() {
        super("JumpBridge", "JumpBridge module", Category.MOVEMENT);
    }


    /**
     * Zıplama esnasında zirve noktada (Jump Peak) ayağın altına veya arkaya anında blok yerleştirme zamanlaması hesaplar.
     */
    public boolean shouldInstantPlaceOnJump(MinecraftClient client) {
        if (!ModConfig.enabled || !ModConfig.jumpBridge || client.player == null) {
            return false;
        }

        // Oyuncu havada zıplıyorsa ve elinde blok varsa
        if (!client.player.isOnGround() && client.player.getMainHandStack().getItem() instanceof BlockItem) {
            double velocityY = client.player.getVelocity().y;
            // Zıplamanın tepe noktasında veya düşüşe geçtiği an (peak)
            if (velocityY <= 0.15D && client.player.fallDistance < 2.0F) {
                return true;
            }
        }
        return false;
    }
}
