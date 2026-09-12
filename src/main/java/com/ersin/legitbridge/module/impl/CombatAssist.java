package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.Humanizer;

import java.util.Comparator;
import java.util.List;

public class CombatAssist extends Module {
    public CombatAssist() {
        super("CombatAssist", "CombatAssist module", Category.COMBAT);
    }


    private Entity currentTarget = null;
    private int tickCounter = 0;

    @Override
    public void onTick() {
        if (!ModConfig.enabled || !ModConfig.combatAssist) return;
        if (client.player == null || client.world == null) return;

        // Sadece sol tıka basılı tutarken (Saldırı) veya sağ tıka basılı tutarken (Blok Koyma/Engelleme) çalışsın
//? if <=1.19.2 {
        /*boolean isAttacking = com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client);
*///?} else {
        boolean isAttacking = com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client);
//?}
//? if <=1.19.2 {
        /*boolean isBlocking = com.ersin.legitbridge.utils.VersionHelper.isUseKeyPressed(client) && client.player.getMainHandStack().getItem() instanceof net.minecraft.item.BlockItem;
*///?} else {
        boolean isBlocking = com.ersin.legitbridge.utils.VersionHelper.isUseKeyPressed(client) && client.player.getMainHandStack().getItem() instanceof net.minecraft.item.BlockItem;
//?}
        
        if (!isAttacking && !isBlocking) {
            currentTarget = null; // Tuşu bırakınca hedefi sıfırla
            return;
        }

        double range = 4.0;
        
        // Eğer mevcut hedef hala geçerliyse, ona odaklanmaya devam et (Persistence/Stickiness)
        if (currentTarget != null && currentTarget.isAlive() && client.player.distanceTo(currentTarget) <= range + 1.0 && client.player.canSee(currentTarget)) {
            // Hedefi koru
        } else {
            currentTarget = null;
            tickCounter++;
            
            // İşlem Dağıtımı (Tick Spreading)
            if (tickCounter % 5 != 0) {
                return;
            }

            Vec3d eyePos = client.player.getCameraPosVec(1.0F);
            Box searchBox = new Box(
                    eyePos.x - range, eyePos.y - range, eyePos.z - range,
                    eyePos.x + range, eyePos.y + range, eyePos.z + range
            );

            List<Entity> targets = client.world.getOtherEntities(client.player, searchBox, entity -> {
                if (!(entity instanceof LivingEntity)) return false;
                if (entity.isSpectator()) return false;
                if (!entity.isAlive()) return false;
                if (client.player.distanceTo(entity) > range) return false;
                // Raytracing (Duvar arkası koruması) - Sadece görebildiğimiz hedeflere kilitlen
                if (!client.player.canSee(entity)) return false;
                return true;
            });

            if (targets.isEmpty()) {
                currentTarget = null;
                return;
            }

            // Hedefleri en düşük cana, canlar eşitse mesafeye göre sırala
            targets.sort((e1, e2) -> {
                float h1 = ((LivingEntity) e1).getHealth();
                float h2 = ((LivingEntity) e2).getHealth();
                if (h1 != h2) {
                    return Float.compare(h1, h2);
                }
                return Double.compare(client.player.distanceTo(e1), client.player.distanceTo(e2));
            });

            currentTarget = targets.get(0);
        }

        Entity target = currentTarget;
        Vec3d eyePos = client.player.getCameraPosVec(1.0F);

        Vec3d targetPos;
        if (isBlocking) {
            // Hareket Tahmini (Prediction): Rakibin hızını al
            double velX = target.getX() - target.prevX;
            double velZ = target.getZ() - target.prevZ;
            double speed = Math.sqrt(velX * velX + velZ * velZ);
            
            // Eğer koşuyorsa, olduğu yere değil, koştuğu yönün ilerisine (tam suratına/ayak ucuna) blok koy
            double predictFactor = (speed > 0.1) ? 6.0 : 0.0; // 6 tick (yaklaşık 0.3 sn) geleceği tahmin et
            
            // Sağ tık (Blok Koyma): Rakibin ayaklarının tam önüne blok koyması için aşağıya (ayak hizasına) nişan al
            targetPos = new Vec3d(target.getX() + velX * predictFactor, target.getY() - 0.5, target.getZ() + velZ * predictFactor);
        } else {
            // Sol tık (Saldırı): Göğüs hizasına nişan al
            targetPos = new Vec3d(target.getX(), target.getBodyY(0.5D), target.getZ());
        }

        // İnsancıl hata payı (Humanized Tremor / Noise)
        // Hedef noktaya ufak rastgele sapmalar ekle
        double noiseX = Humanizer.getGaussianDelay(-5, 5) / 100.0; // -0.05 to +0.05 blok sapma
        double noiseY = Humanizer.getGaussianDelay(-5, 5) / 100.0;
        double noiseZ = Humanizer.getGaussianDelay(-5, 5) / 100.0;
        
        targetPos = targetPos.add(noiseX, noiseY, noiseZ);
        
        double diffX = targetPos.x - eyePos.x;
        double diffY = targetPos.y - eyePos.y;
        double diffZ = targetPos.z - eyePos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float targetYaw = (float) (MathHelper.atan2(diffZ, diffX) * 57.29577951308232D) - 90.0F;
        float targetPitch = (float) (-(MathHelper.atan2(diffY, diffXZ) * 57.29577951308232D));

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

        float smoothing = isBlocking ? 1.5f : 3.0f; // Blok koyarken biraz daha hızlı tepki versin
        
        if (ModConfig.autoLock) {
            smoothing = 1.0f; // Oto-kilitlenme aktifse yumuşatma yok, anında kilitlen (Aimbot)
        }
        
        // Bezier Eğrisi (Smooth Kavisli Hedefleme)
        // Eğer yaw ve pitch farkı çok büyükse, sadece dümdüz gitmek yerine kavisli gitmesi için
        // smoothing'i uzaklığa göre parabolik olarak değiştiriyoruz
        float distanceToTargetAngle = (float) Math.sqrt(yawDiff * yawDiff + pitchDiff * pitchDiff);
        if (distanceToTargetAngle > 20.0f) {
             smoothing *= (distanceToTargetAngle / 15.0f); // Uzakken daha kavisli/yavaş
        }
        
        // Rastgele yumuşatma dalgalanması (smoothing dalgalanması eli titreyen insan hissi verir)
        if (!ModConfig.autoLock) {
            smoothing += Humanizer.getSmoothedJitter(smoothing, smoothing - 0.5, smoothing + 1.0) - smoothing;
        }

        if (Math.abs(yawDiff) > 1.0f || ModConfig.autoLock) {
            // Anti-cheat (GCD) baypası: Mouse hareketlerini sensitivity ile oranla
            float targetRawYaw = com.ersin.legitbridge.utils.VersionHelper.getYaw(client.player) + yawDiff / smoothing;
            float safeYaw = com.ersin.legitbridge.utils.GCDFix.applyGCD(targetRawYaw, com.ersin.legitbridge.utils.VersionHelper.getYaw(client.player), client);
            com.ersin.legitbridge.utils.VersionHelper.setYaw(client.player, safeYaw);
        }
        if (Math.abs(pitchDiff) > 1.0f || ModConfig.autoLock) {
            float targetRawPitch = com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) + pitchDiff / (smoothing * 0.9f);
            float safePitch = com.ersin.legitbridge.utils.GCDFix.applyGCD(targetRawPitch, com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player), client);
            com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, safePitch);
        }
    }
}
