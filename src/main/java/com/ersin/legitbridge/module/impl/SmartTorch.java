package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.LightType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class SmartTorch extends Module {

    private int tickDelay = 0;

    public SmartTorch() {
        super("SmartTorch", "Karanlık yerlerde (ışık seviyesi 0) kazı yaparken otomatik meşale koyar.", Category.LEGIT);
    }

    @Override
    public void onTick() {
        if (client.player == null || client.world == null) return;

        if (tickDelay > 0) {
            tickDelay--;
            return;
        }

        BlockPos pos = client.player.getBlockPos();
        // Check block light level (not sky light)
        int light = client.world.getLightLevel(LightType.BLOCK, pos);

        if (light == 0) {
            int torchSlot = findTorch();
            if (torchSlot != -1 && client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                // Sadece etrafta karanlık varsa ve bir bloğa bakıyorsak
                int oldSlot = com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot;
                
                // Switch
                com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = torchSlot;
                
                // Place
                //? if <=1.19.2 {
                /*client.interactionManager.interactBlock(client.player, client.world, Hand.MAIN_HAND, (net.minecraft.util.hit.BlockHitResult) client.crosshairTarget);
*///?} else {
                client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, (net.minecraft.util.hit.BlockHitResult) client.crosshairTarget);
//?}
                
                // Switch back
                com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = oldSlot;
                
                tickDelay = 20; // Wait 1 second before doing it again
            }
        }
    }

    private int findTorch() {
        for (int i = 0; i < 9; i++) {
            if (com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i).getItem() == Items.TORCH) {
                return i;
            }
        }
        return -1;
    }
}
