package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.Humanizer;
import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class LegitAutoRod extends Module {
    
    private int delay = 0;
    private int oldSlot = -1;
    private int state = 0; // 0: Idle, 1: Switching to Rod, 2: Throwing, 3: Switching Back
    
    public LegitAutoRod() {
        super("LegitAutoRod", "Komboya başlamadan önce oltayı otomatik atıp geri çeker.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.autoRod || client.player == null || client.world == null) {
            state = 0;
            return;
        }
        
        if (delay > 0) {
            delay--;
            return;
        }

        if (state == 0) {
            PlayerEntity target = getNearestTarget();
            if (target != null) {
                double dist = client.player.distanceTo(target);
                // 3.5 bloktan uzak, 7 bloktan yakın (Oltanın etkili olduğu combo mesafesi)
                if (dist > 3.5 && dist < 7.0 && isEntityInFOV(target, 45)) {
                    int rodSlot = findRod();
                    if (rodSlot != -1) {
                        oldSlot = com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot;
                        com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = rodSlot;
                        state = 1;
                        delay = Humanizer.getGaussianDelay(1, 3);
                    }
                }
            }
        } else if (state == 1) {
            // Oltayı At
            if (client.interactionManager != null) {
                com.ersin.legitbridge.utils.VersionHelper.interactItem(client, Hand.MAIN_HAND);
                state = 2;
                delay = Humanizer.getGaussianDelay(4, 7); // Havada kalma süresi
            }
        } else if (state == 2) {
            // Oltayı Geri Çek (Tekrar sağ tık)
            if (client.interactionManager != null) {
                com.ersin.legitbridge.utils.VersionHelper.interactItem(client, Hand.MAIN_HAND);
                state = 3;
                delay = Humanizer.getGaussianDelay(1, 2);
            }
        } else if (state == 3) {
            // Eski silaha dön
            if (oldSlot != -1) {
                com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = oldSlot;
            }
            state = 0;
            // Bir sonraki olta atışı için cooldown (Spam engelleme)
            delay = Humanizer.getGaussianDelay(20, 40); 
        }
    }

    private int findRod() {
        for (int i = 0; i < 9; i++) {
            if (com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i).getItem() == Items.FISHING_ROD) {
                return i;
            }
        }
        return -1;
    }

    private PlayerEntity getNearestTarget() {
        PlayerEntity nearest = null;
        double nearestDist = 999.0;
        for (PlayerEntity player : client.world.getPlayers()) {
            if (player == client.player || player.isDead()) continue;
            double dist = client.player.distanceTo(player);
            if (dist < nearestDist) {
                nearest = player;
                nearestDist = dist;
            }
        }
        return nearest;
    }

    private boolean isEntityInFOV(PlayerEntity entity, float fov) {
        net.minecraft.util.math.Vec3d lookVec = client.player.getRotationVec(1.0f);
        net.minecraft.util.math.Vec3d diffVec = entity.getPos().subtract(client.player.getCameraPosVec(1.0f)).normalize();
        double dotProduct = lookVec.dotProduct(diffVec);
        double angle = Math.toDegrees(Math.acos(dotProduct));
        return angle <= (fov / 2.0);
    }
}
