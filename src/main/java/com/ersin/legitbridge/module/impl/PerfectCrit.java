package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class PerfectCrit extends Module {

    public PerfectCrit() {
        super("PerfectCrit", "Saldırı bekleme süresini (1.16) hesaplar ve sadece vururken zıplayarak kritik vurmanı sağlar.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (client.player == null || client.world == null) return;

        // Vurma tuşuna basılıyorsa ve bir hedefe bakıyorsak
//? if <=1.19.2 {
        /*if (com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client) && client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
*///?} else {
        if (com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client) && client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
//?}
            
            // Eğer saldırı gücü barı tam doluysa (1.16+ mekaniği)
            if (client.player.getAttackCooldownProgress(0.5f) == 1.0f) {
                
                // Zıplamıyorsak ve yerdeysek ufak bir zıplama yap (Kritik vurmak için yere düşüyor olmak lazım)
                if (client.player.isOnGround()) {
                    client.player.jump();
                }
            } else {
                // Eğer bar dolu değilse, vurmayı engellemek için tuşu bırakmış gibi davran (Opsiyonel ama Legit değil)
                // Bu yüzden sadece zıplatmayı yapıyoruz.
            }
        }
    }
}
