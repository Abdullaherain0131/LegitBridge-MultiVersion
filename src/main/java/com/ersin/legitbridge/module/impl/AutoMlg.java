package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.math.Vec3d;

public class AutoMlg extends Module {

    private boolean mlgAttempted = false;

    public AutoMlg() {
        super("AutoMLG", "Yüksekten düşerken otomatik su koyarak ölümü engeller.", Category.LEGIT);
    }

    @Override
    public void onTick() {
        if (client.player == null || client.world == null) return;

        // Reset if we are on the ground or in water
        if (client.player.isOnGround() || client.player.isSubmergedInWater()) {
            mlgAttempted = false;
            return;
        }

        // Check if falling dangerously
        if (client.player.fallDistance > 3.5f && client.player.getVelocity().y < -0.5) {
            
            // Raycast down to predict impact
            Vec3d pos = client.player.getPos();
            Vec3d down = pos.add(0, client.player.getVelocity().y * 3 - 2, 0);
            
            BlockHitResult hit = client.world.raycast(new RaycastContext(
                pos, down, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, client.player
            ));

            if (hit != null && hit.getType() == HitResult.Type.BLOCK && !mlgAttempted) {
                // We are about to hit the ground!
                int bucketSlot = findWaterBucket();
                if (bucketSlot != -1) {
                    com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = bucketSlot;
                    
                    // Use item
                    //? if <=1.19.2 {
                    /*client.interactionManager.interactItem(client.player, client.world, Hand.MAIN_HAND);
*///?} else {
                    com.ersin.legitbridge.utils.VersionHelper.interactItem(client, Hand.MAIN_HAND);
//?}
                    mlgAttempted = true;
                }
            }
        }
    }

    private int findWaterBucket() {
        for (int i = 0; i < 9; i++) {
            if (com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i).getItem() == Items.WATER_BUCKET) {
                return i;
            }
        }
        return -1;
    }
}
