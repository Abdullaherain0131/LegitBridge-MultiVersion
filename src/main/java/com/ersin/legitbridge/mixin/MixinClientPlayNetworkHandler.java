package com.ersin.legitbridge.mixin;

import com.ersin.legitbridge.LegitBridgeMod;
import com.ersin.legitbridge.module.impl.Velocity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Shadow private MinecraftClient client;

    @Inject(method = "onVelocityUpdate", at = @At("HEAD"), cancellable = true)
    private void onVelocityUpdate(EntityVelocityUpdateS2CPacket packet, CallbackInfo ci) {
        Velocity velocityModule = (Velocity) LegitBridgeMod.moduleManager.getModuleByName("Velocity");
        if (velocityModule != null && velocityModule.isEnabled()) {
            //? if >1.19.2 {
            if (client.player != null && packet.getId() == client.player.getId()) {
            //?} else {
            /*if (client.player != null && packet.getId() == client.player.getEntityId()) {
            *///?}
                if (Velocity.horizontal == 0.0 && Velocity.vertical == 0.0) {
                    ci.cancel();
                } else {
                    // In a more complex implementation, we'd scale the packet values here,
                    // but vanilla packets might not have setters. Usually we just cancel
                    // and apply the scaled velocity manually if it's not 0%.
                    // For now, 0% knockback is standard.
                }
            }
        }
    }

    @Inject(method = "onExplosion", at = @At("HEAD"), cancellable = true)
    private void onExplosion(ExplosionS2CPacket packet, CallbackInfo ci) {
        Velocity velocityModule = (Velocity) LegitBridgeMod.moduleManager.getModuleByName("Velocity");
        if (velocityModule != null && velocityModule.isEnabled()) {
            if (Velocity.horizontal == 0.0 && Velocity.vertical == 0.0) {
                // If 0% knockback, we can just let it process but we'd need to modify the packet's player velocity values
                // Since ExplosionS2CPacket gives player velocity X, Y, Z.
                // It's usually easier to cancel it and spawn explosion particles manually, but that's complex.
                // We'll leave explosion as is or just cancel for full velocity reduction.
            }
        }
    }
}
