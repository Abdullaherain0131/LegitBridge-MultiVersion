package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;

public class AutoElytra extends Module {
    public AutoElytra() {
        super("AutoElytra", "AutoElytra module", Category.MOVEMENT);
    }


    private boolean wasFlying = false;

    @Override
    public void onTick() {
        if (client.player == null || client.world == null) {
            return;
        }

        // Check if player is falling
        if (client.player.getVelocity().y >= -0.1 || client.player.isOnGround()) {
            return;
        }

        // Check if wearing Elytra
        if (client.player.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
            return;
        }

        // Check if already flying with elytra
        if (client.player.isFallFlying()) {
            wasFlying = true;
            return;
        }

        // Not flying, but was flying recently? (prevent spam)
        if (wasFlying && client.player.fallDistance < 2.0f) {
            return;
        }

        // Calculate distance to ground (check up to 5 blocks below)
        BlockPos playerPos = client.player.getBlockPos();
        boolean danger = false;
        
        // If falling fast (fall distance > 3), check 5 blocks below
        if (client.player.fallDistance > 3.0f) {
            for (int i = 1; i <= 5; i++) {
                BlockPos checkPos = playerPos.down(i);
                if (checkPos.getY() < 0) break;
                if (!client.world.getBlockState(checkPos).isAir()) {
                    danger = true;
                    break;
                }
            }
        }

        if (danger) {
            // Trigger elytra deployment by simulating a jump packet or setting the flag
            // The client sends a ClientCommandC2SPacket to start flying, but the easiest way is 
            // to just tell the client that the jump key was pressed so vanilla handles the elytra packet.
//? if <=1.19.2 {
            /*client.options.keyJump.setPressed(true);
*///?} else {
            client.options.jumpKey.setPressed(true);
//?}
            wasFlying = true;
        } else {
            wasFlying = false;
        }
    }
}
