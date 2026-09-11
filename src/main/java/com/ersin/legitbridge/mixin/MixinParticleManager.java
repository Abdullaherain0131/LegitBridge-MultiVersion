package com.ersin.legitbridge.mixin;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ParticleManager.class)
public class MixinParticleManager {
    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"))
    private void onAddParticle(Particle particle, CallbackInfo ci) {
        if (!ModConfig.enabled || !ModConfig.invisTracker || particle == null) return;
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;
        
        // Simple check: is there an invisible player nearby?
        Box searchBox = new Box(
            particle.getBoundingBox().minX - 1.0,
            particle.getBoundingBox().minY - 2.0,
            particle.getBoundingBox().minZ - 1.0,
            particle.getBoundingBox().maxX + 1.0,
            particle.getBoundingBox().maxY + 2.0,
            particle.getBoundingBox().maxZ + 1.0
        );
        
        List<PlayerEntity> players = client.world.getEntitiesByClass(PlayerEntity.class, searchBox, Entity::isInvisible);
        if (!players.isEmpty()) {
            // Make the particle bright red and large
            particle.setColor(1.0f, 0.0f, 0.0f);
            particle.scale(3.0f);
        }
    }
}
