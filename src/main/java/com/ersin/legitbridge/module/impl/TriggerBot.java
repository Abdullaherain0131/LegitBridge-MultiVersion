package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import com.ersin.legitbridge.mixin.MinecraftClientAccessor;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.Humanizer;

public class TriggerBot extends Module {
    public TriggerBot() {
        super("TriggerBot", "TriggerBot module", Category.COMBAT);
    }

    private int triggerCooldown = 0;

    @Override
    public void onTick() {
        if (client.player == null) {
            return;
        }

        if (triggerCooldown > 0) {
            triggerCooldown--;
            return;
        }

        HitResult target = client.crosshairTarget;
        if (target != null && target.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) target;
            Entity entity = entityHit.getEntity();

            if (entity instanceof LivingEntity && entity.isAlive() && entity != client.player) {
                double distanceSq = client.player.squaredDistanceTo(entity);
                double maxReach = ModConfig.reachGuard ? 9.0D : 16.0D; // 3.0 blocks vs 4.0 blocks

                if (distanceSq <= maxReach) {
                    if (Humanizer.shouldDropClick()) {
                        triggerCooldown = 1; // Miss click, try again next tick maybe
                        return; 
                    }

                    // En legit saldırı metodu: doğrudan oyunun kendi tıklama fonksiyonunu tetikliyoruz.
                    ((MinecraftClientAccessor)client).invokeDoAttack();
                    
                    // Delay'e tepki süresi (Reaction time) ekle
                    int delay = Humanizer.getGaussianDelay(ModConfig.minFastClickDelay, ModConfig.maxFastClickDelay);
                    if (Math.random() < 0.1) {
                        delay += Humanizer.getGaussianDelay(2, 5); // %10 ihtimalle yavaş tıklama
                    }
                    triggerCooldown = delay;
                }
            }
        }
    }
}
