package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoFish extends Module {
    private int reelTimer = -1;
    private int recastTimer = -1;

    public AutoFish() {
        super("AutoFish", "Automatically catches fish when they bite.", Category.LEGIT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.autoFish) return;
        if (client.player == null || client.world == null) return;

        boolean holdingRod = client.player.getMainHandStack().getItem() == Items.FISHING_ROD || client.player.getOffHandStack().getItem() == Items.FISHING_ROD;
        if (!holdingRod) return;

        FishingBobberEntity bobber = client.player.fishHook;

        if (bobber != null) {
            // Fish bit the hook (bobber plunges down)
            if (bobber.getVelocity().y < -0.04 && bobber.isTouchingWater()) {
                if (reelTimer == -1) {
                    reelTimer = 10; // wait 10 ticks to look legit (0.5s reaction)
                }
            }
        }

        if (reelTimer > 0) {
            reelTimer--;
            if (reelTimer == 0) {
                // Reel in
//? if <=1.19.2 {
/*client.interactionManager.interactItem(client.player, client.world, net.minecraft.util.Hand.MAIN_HAND);
*///?} else {
client.interactionManager.interactItem(client.player, net.minecraft.util.Hand.MAIN_HAND);
//?}
                reelTimer = -1;
                recastTimer = 20; // wait 1 second before recasting
            }
        }

        if (recastTimer > 0) {
            recastTimer--;
            if (recastTimer == 0) {
                // Cast again
//? if <=1.19.2 {
/*client.interactionManager.interactItem(client.player, client.world, net.minecraft.util.Hand.MAIN_HAND);
*///?} else {
client.interactionManager.interactItem(client.player, net.minecraft.util.Hand.MAIN_HAND);
//?}
                recastTimer = -1;
            }
        }
    }
}
