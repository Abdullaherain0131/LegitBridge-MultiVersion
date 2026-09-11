package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;

public class AutoShield extends Module {

    private int blockTicks = 0;
    private boolean isBlocking = false;

    public AutoShield() {
        super("AutoShield", "Düşman size kılıç savurduğunda otomatik kalkan açar (Legit).", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.enabled || client.player == null || client.world == null) return;
        
        // Elimizde kalkan var mı kontrol et (Offhand veya Mainhand)
        boolean hasShield = client.player.getOffHandStack().getItem() instanceof ShieldItem 
                         || client.player.getMainHandStack().getItem() instanceof ShieldItem;
                         
        if (!hasShield) {
            stopBlocking();
            return;
        }

        boolean incomingAttack = false;

        // Etraftaki oyuncuları kontrol et
        for (Entity entity : client.world.getPlayers()) {
            if (entity == client.player) continue;
            
            PlayerEntity enemy = (PlayerEntity) entity;
            double dist = client.player.distanceTo(enemy);
            
            // Eğer düşman 5 bloktan yakınsa ve bize bakıyorsa
            if (dist < 5.0 && enemy.canSee(client.player)) {
                // Eğer düşman kolunu sallıyorsa (kılıç vurma animasyonu)
                if (enemy.handSwinging) {
                    incomingAttack = true;
                    break;
                }
            }
        }

        if (incomingAttack) {
            // Saldırı geliyorsa kalkanı aç
            if (!isBlocking) {
//? if <=1.19.2 {
                /*com.ersin.legitbridge.utils.VersionHelper.setUseKeyPressed(client, true);
*///?} else {
                com.ersin.legitbridge.utils.VersionHelper.setUseKeyPressed(client, true);
//?}
                isBlocking = true;
                blockTicks = 5; // En az 5 tick boyunca kalkanı açık tut
            }
        } else {
            if (isBlocking) {
                if (blockTicks > 0) {
                    blockTicks--;
                } else {
                    stopBlocking();
                }
            }
        }
    }
    
    private void stopBlocking() {
        if (isBlocking) {
            // Eğer biz manuel olarak kalkanı kullan tuşuna basmıyorsak bırak
//? if <=1.19.2 {
            /*com.ersin.legitbridge.utils.VersionHelper.setUseKeyPressed(client, false);
*///?} else {
            com.ersin.legitbridge.utils.VersionHelper.setUseKeyPressed(client, false);
//?}
            isBlocking = false;
        }
    }
}
