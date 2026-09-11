package com.ersin.legitbridge.mixin;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(Entity entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!ModConfig.enabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // NoRender (Itemları gizle)
        if (ModConfig.noRender) {
            if (entity instanceof ItemEntity) {
                // FPS artışı için düşen eşyaları renderlama (Çok op FPS verir)
                ci.cancel();
                return;
            }
        }

        // Entity Culling
        if (ModConfig.entityCulling && entity != client.player) {
            // Mesafe kontrolü (Uzak entityleri çizme)
            double distSq = client.player.squaredDistanceTo(entity);
            if (distSq > 64 * 64) { // 64 bloktan uzaksa çizme
                ci.cancel();
                return;
            }
            
            // Eğer player değilse ve görüş açısının dışındaysa (arkanda kalıyorsa) çizme
            if (!(entity instanceof PlayerEntity)) {
                net.minecraft.util.math.Vec3d lookVec = client.player.getRotationVec(1.0F).normalize();
                net.minecraft.util.math.Vec3d targetVec = entity.getPos().subtract(client.player.getPos()).normalize();
                double angle = Math.toDegrees(Math.acos(lookVec.dotProduct(targetVec)));
                if (angle > 100.0) { // 100 derece dışındakileri (arkandakileri) çizme
                    ci.cancel();
                    return;
                }
            }
        }
    }
}
