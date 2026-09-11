package com.ersin.legitbridge.mixin;

import com.ersin.legitbridge.CombatTracker;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {

    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (player instanceof ClientPlayerEntity) {
            // Estimate damage based on hitting a target.
            // Since we can't reliably get the server-side calculated damage here for a general hit, 
            // we will add a baseline value or read from the weapon.
            // For now, we add an estimated +1 or +5 per hit.
            // A typical sword hit is around 6-7 damage.
            int estimatedDamage = 5;
            
            // To make it slightly more accurate, we can check weapon damage, but for simplicity:
            CombatTracker.addDamage(estimatedDamage);
            CombatTracker.onHitEntity(target);
        }
    }
}
