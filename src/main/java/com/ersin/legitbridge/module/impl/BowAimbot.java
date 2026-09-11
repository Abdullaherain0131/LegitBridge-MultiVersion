package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BowAimbot extends Module {

    public BowAimbot() {
        super("BowAimbot", "Yayı çektiğinde hedefin koşma hızını hesaplayıp otomatik nişan alır.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.enabled || !ModConfig.bowAimbot || client.player == null || client.world == null) return;

        // Ok, kartopu veya pot fırlatırken
        net.minecraft.item.Item heldItem = client.player.getMainHandStack().getItem();
        boolean isProjectile = heldItem instanceof BowItem || 
                               heldItem instanceof net.minecraft.item.SnowballItem || 
                               heldItem instanceof net.minecraft.item.EggItem ||
                               heldItem instanceof net.minecraft.item.SplashPotionItem ||
                               heldItem instanceof net.minecraft.item.EnderPearlItem;

        if (client.player.isUsingItem() || isProjectile) {
            PlayerEntity target = getClosestPlayer(30.0); // 30 blok içindeki en yakın oyuncu
            
            if (target != null) {
                // Hız ve Süre Hesaplamaları
                double distance = client.player.distanceTo(target);
                
                // Ok için hız: 3.0, kartopu vb. için ~1.5
                double projectileSpeed = (heldItem instanceof BowItem) ? 3.0 : 1.5; 
                double gravity = (heldItem instanceof BowItem) ? 0.05 : 0.03;

                int ticksToHit = (int) (distance / projectileSpeed);
                
                // Hedefin şu anki hızı
                double vX = target.getX() - target.prevX;
                double vY = target.getY() - target.prevY;
                double vZ = target.getZ() - target.prevZ;
                
                // Hareket Tahmini (Motion Prediction)
                double predictX = target.getX() + (vX * ticksToHit);
                double predictZ = target.getZ() + (vZ * ticksToHit);
                // Gözünün biraz altı, artı zıplıyorsa y tahminine ekle
                double predictY = target.getY() + (target.getStandingEyeHeight() / 2.0) + (vY * ticksToHit); 
                
                // Yerçekimi Telafisi (Gravity Drop Compensation)
                // Uzağa atarken merminin düşüşünü hesaplayıp yayı yukarı kaldırmak
                double dropTime = distance / projectileSpeed;
                double dropCompensation = 0.5 * gravity * dropTime * dropTime;
                predictY += dropCompensation;

                // Bakış açılarını hesapla
                double dX = predictX - client.player.getX();
                double dY = predictY - (client.player.getY() + client.player.getEyeHeight(client.player.getPose()));
                double dZ = predictZ - client.player.getZ();
                
                double diffXZ = Math.sqrt(dX * dX + dZ * dZ);
                
                float targetYaw = (float) ((MathHelper.atan2(dZ, dX) * 180.0D / Math.PI) - 90.0F);
                float targetPitch = (float) -(MathHelper.atan2(dY, diffXZ) * 180.0D / Math.PI);
                
                // İnsancıl hata payı (Gaussian Noise - Tremor)
                targetYaw += (Math.random() - 0.5) * 1.5;
                targetPitch += (Math.random() - 0.5) * 1.5;
                
                // Smooth Aim (Yumuşak çevirme - aniden kitlenmesin)
//? if <=1.19.2 {
                /*float yawDiff = MathHelper.wrapDegrees(targetYaw - client.player.yaw);
*///?} else {
                float yawDiff = MathHelper.wrapDegrees(targetYaw - com.ersin.legitbridge.utils.VersionHelper.getYaw(client.player));
//?}
//? if <=1.19.2 {
                /*float pitchDiff = targetPitch - client.player.pitch;
*///?} else {
                float pitchDiff = targetPitch - com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player);
//?}
                
                float smooth = 0.15f; // Ne kadar düşükse o kadar legit
//? if <=1.19.2 {
/*client.player.yaw += yawDiff * smooth;
*///?} else {
com.ersin.legitbridge.utils.VersionHelper.setYaw(client.player, com.ersin.legitbridge.utils.VersionHelper.getYaw(client.player) + yawDiff * smooth);
//?}
//? if <=1.19.2 {
/*client.player.pitch += pitchDiff * smooth;
*///?} else {
com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) + pitchDiff * smooth);
//?}
            }
        }
    }
    
    private PlayerEntity getClosestPlayer(double maxDistance) {
        PlayerEntity closest = null;
        double closestDist = maxDistance;
        
        for (PlayerEntity p : client.world.getPlayers()) {
            if (p != client.player && !p.isSpectator() && p.isAlive()) {
                // Sadece görebildiğimiz hedeflere nişan al (RayTrace)
                if (!client.player.canSee(p)) continue;

                double dist = client.player.distanceTo(p);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = p;
                }
            }
        }
        return closest;
    }
}
