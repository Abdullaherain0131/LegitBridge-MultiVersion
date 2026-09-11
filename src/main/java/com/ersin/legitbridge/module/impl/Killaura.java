package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Killaura extends Module {

    private int attackCooldown = 0;
    
    // Config values (could be moved to ModConfig later)
    private double reach = 4.0;
    private boolean legitMode = true; // LegitMode waits for hit cooldown
    private boolean mobs = false;
    private boolean players = true;

    public Killaura() {
        super("Killaura", "Etraftaki hedeflere otomatik olarak saldırır.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (client.player == null || client.world == null) return;
        
        if (attackCooldown > 0) {
            attackCooldown--;
        }

        // Check if player is dead
        if (!client.player.isAlive()) return;
        
        // Find target
        Entity target = getBestTarget();
        if (target != null) {
            // Legit Rotation - Hedefe doğru dön
            faceTarget(target);

            boolean canAttack = false;
            
            if (legitMode) {
                // Sadece tam cooldown dolduğunda vur (1.9+ combat)
                if (client.player.getAttackCooldownProgress(0.5f) >= 1.0f) {
                    canAttack = true;
                }
            } else {
                // Rage mode: Her X tick'te bir vur (1.8 spam mantığı)
                if (attackCooldown <= 0) {
                    canAttack = true;
                    // Rastgele CPS (Anti-Cheat Bypass)
                    attackCooldown = 2 + (int)(Math.random() * 2); // 2-3 tick arası bekle
                }
            }
            
            if (canAttack) {
                attack(target);
            }
        }
    }
    
    private Entity currentTarget = null;
    private int tickCounter = 0;
    
    private Entity getBestTarget() {
        // Eğer mevcut hedef hala geçerliyse, ona odaklanmaya devam et (Persistence/Stickiness)
        if (currentTarget != null && isValid(currentTarget, reach + 1.0)) { 
            // Menzilden ufak bir tık daha uzağa çıkana kadar bırakma (1.0 blok tolerans)
            return currentTarget;
        }

        currentTarget = null; // Hedef geçersizse sıfırla
        tickCounter++;
        
        // İşlem Dağıtımı (Tick Spreading): Sadece her 5 tickte bir etrafı tara.
        // Bu CPU kullanımını %80 oranında düşürür.
        if (tickCounter % 5 != 0) {
            return null;
        }

        Entity best = null;
        float lowestHealth = Float.MAX_VALUE;
        double bestDist = reach;

        for (Entity entity : client.world.getEntities()) {
            if (isValid(entity, reach)) {
                if (entity instanceof net.minecraft.entity.LivingEntity) {
                    float health = ((net.minecraft.entity.LivingEntity) entity).getHealth();
                    double dist = client.player.distanceTo(entity);
                    
                    // Öncelik: En düşük can. Canlar eşitse mesafe
                    if (health < lowestHealth || (health == lowestHealth && dist < bestDist)) {
                        lowestHealth = health;
                        bestDist = dist;
                        best = entity;
                    }
                }
            }
        }
        
        currentTarget = best;
        return best;
    }
    
    private boolean isValid(Entity entity, double currentReach) {
        if (entity == client.player) return false;
        if (!entity.isAlive()) return false;
        if (client.player.distanceTo(entity) > currentReach) return false;
        
        // RayTrace: Sadece görebildiğimiz hedeflere vur (Duvar arkası vurmayı engelle)
        if (!client.player.canSee(entity)) return false;

        // FOV (Görüş Açısı) Kontrolü: Sadece önümüzdeki 120 derece içindeki hedeflere vur
        Vec3d lookVec = client.player.getRotationVec(1.0F).normalize();
        Vec3d targetVec = entity.getPos().subtract(client.player.getPos()).normalize();
        double angle = Math.toDegrees(Math.acos(lookVec.dotProduct(targetVec)));
        if (angle > 60.0) return false; // Sağ ve sol 60 derece, toplam 120 derece
        
        if (entity instanceof PlayerEntity && players) return true;
        
        return false;
    }

    private void faceTarget(Entity target) {
        double diffX = target.getX() - client.player.getX();
        double diffY = target.getBodyY(0.5D) - client.player.getEyeY();
        double diffZ = target.getZ() - client.player.getZ();
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float targetYaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0F;
        float targetPitch = (float) -(Math.atan2(diffY, diffXZ) * 180.0 / Math.PI);

        // Rastgele Titreme (Humanizer/Gaussian Noise)
        targetYaw += (Math.random() - 0.5) * 4.0;
        targetPitch += (Math.random() - 0.5) * 4.0;

//? if <=1.19.2 {
        /*float yawDiff = net.minecraft.util.math.MathHelper.wrapDegrees(targetYaw - client.player.yaw);
*///?} else {
        float yawDiff = net.minecraft.util.math.MathHelper.wrapDegrees(targetYaw - com.ersin.legitbridge.utils.VersionHelper.getYaw(client.player));
//?}
//? if <=1.19.2 {
        /*float pitchDiff = targetPitch - client.player.pitch;
*///?} else {
        float pitchDiff = targetPitch - com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player);
//?}

        // Yumuşak Dönüş (Smooth Aim)
        float smoothing = 3.0f;
//? if <=1.19.2 {
/*client.player.yaw += yawDiff / smoothing;
*///?} else {
com.ersin.legitbridge.utils.VersionHelper.setYaw(client.player, com.ersin.legitbridge.utils.VersionHelper.getYaw(client.player) + yawDiff / smoothing);
//?}
//? if <=1.19.2 {
/*client.player.pitch += pitchDiff / smoothing;
*///?} else {
com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) + pitchDiff / smoothing);
//?}
    }
    
    private void attack(Entity target) {
        if (client.interactionManager != null) {
            client.interactionManager.attackEntity(client.player, target);
            client.player.swingHand(Hand.MAIN_HAND);
        }
    }
}
