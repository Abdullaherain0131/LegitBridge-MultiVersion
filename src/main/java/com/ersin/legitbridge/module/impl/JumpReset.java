package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;

public class JumpReset extends Module {
    public JumpReset() {
        super("JumpReset", "JumpReset module", Category.MOVEMENT);
    }


    private boolean wasHurt = false;

    @Override
    public void onTick() {
        if (!ModConfig.jumpReset || client.player == null) {
            return;
        }

        // Detect if the player just got hurt
        // hurtTime is set to maxHurtTime (usually 10) when damage is taken
        boolean isHurt = client.player.hurtTime > 0;
        
        if (isHurt && !wasHurt) {
            // Player just took damage this tick
            if (client.player.isOnGround()) {
                // Trigger a jump to reduce knockback (Jump Resetting)
                client.player.jump();
            }
        }
        
        wasHurt = isHurt;
    }
}
