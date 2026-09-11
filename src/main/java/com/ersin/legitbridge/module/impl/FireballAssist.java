package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.mixin.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;

public class FireballAssist extends Module {
    public FireballAssist() {
        super("FireballAssist", "FireballAssist module", Category.COMBAT);
    }

    private int assistTicks = 0;
    private float targetPitch = 0;
    private boolean isAssisting = false;

    @Override
    public void onTick() {
        if (!ModConfig.autoFireballAngle) return;

        if (client.player == null || client.world == null) return;

//? if <=1.19.2 {
        /*boolean isHoldingRightClick = com.ersin.legitbridge.utils.VersionHelper.isUseKeyPressed(client);
*///?} else {
        boolean isHoldingRightClick = com.ersin.legitbridge.utils.VersionHelper.isUseKeyPressed(client);
//?}
        boolean isHoldingFireball = client.player.getMainHandStack().getItem() == net.minecraft.item.Items.FIRE_CHARGE;

        if (isHoldingRightClick && isHoldingFireball) {
            if (!isAssisting) {
                // Sadece ilk basıldığında bir kere açıyı hesapla
                targetPitch = calculateTargetPitch(client);
                isAssisting = true;
                assistTicks = 0;
            }
        } else {
            isAssisting = false;
        }

        if (isAssisting) {
            assistTicks++;
//? if <=1.19.2 {
                        /*float currentPitch = client.player.pitch;
*///?} else {
                        float currentPitch = com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player);
//?}
            
            // İlk 2-3 tick içinde açıyı düzelt, oyuncu zaten sağ tıka basılı tuttuğu için
            // açı düzeldiğinde oyun otomatik olarak Fireball'ı fırlatacak.
            if (assistTicks <= 3 && Math.abs(currentPitch - targetPitch) > 1.0f) {
//? if <=1.19.2 {
                /*client.player.pitch += (targetPitch - currentPitch) / 2.0f;
*///?} else {
                com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) + (targetPitch - currentPitch) / 2.0f);
//?}
            } else if (assistTicks > 3) {
                // Açı düzeltildi, asistan devre dışı kalabilir. Sağ tık simülasyonuna GEREK YOK!
                // Oyun zaten fırlatma işlemini (ItemUse) kendi ağ paketiyle halledecek, 
                // biz sadece fırlatılmadan hemen önceki mikro saniyede açıyı kilitledik.
//? if <=1.19.2 {
                /*client.player.pitch = targetPitch;
*///?} else {
                com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, targetPitch);;
//?}
            }
        }
    }

    private float calculateTargetPitch(MinecraftClient client) {
        float yaw = client.player.getYaw(1.0F);
        Vec3d eyePos = client.player.getCameraPosVec(1.0F);
        
        float f = yaw * 0.017453292F;
        Vec3d lookFwd = new Vec3d(-MathHelper.sin(f), 0, MathHelper.cos(f));
        Vec3d lookBwd = new Vec3d(MathHelper.sin(f), 0, -MathHelper.cos(f));

        Vec3d endFwd = eyePos.add(lookFwd.multiply(30.0));
        Vec3d endBwd = eyePos.add(lookBwd.multiply(30.0));

        BlockHitResult hitFwd = client.world.raycast(new RaycastContext(eyePos, endFwd, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, client.player));
        BlockHitResult hitBwd = client.world.raycast(new RaycastContext(eyePos, endBwd, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, client.player));

        double dist = -1;
        if (hitFwd.getType() == HitResult.Type.BLOCK) dist = eyePos.distanceTo(hitFwd.getPos());
        if (hitBwd.getType() == HitResult.Type.BLOCK) {
            double dBwd = eyePos.distanceTo(hitBwd.getPos());
            if (dist == -1 || dBwd < dist) dist = dBwd;
        }

        if (dist != -1 && dist > 1.0) {
            return Math.max(70.0f, Math.min(85.0f, 85.0f - (float)(dist * 0.6f)));
        }
        return 75.0f;
    }
}
