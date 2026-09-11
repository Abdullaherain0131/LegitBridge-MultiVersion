package com.ersin.legitbridge.mixin;

import com.ersin.legitbridge.Humanizer;
import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinEntitySafeWalk {

    @Unique
    private double currentJitter = 0.05D;

    @Inject(method = "clipAtLedge", at = @At("HEAD"), cancellable = true)
    private void onClipAtLedge(CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (com.ersin.legitbridge.utils.VersionHelper.getWorld(player).isClient && ModConfig.enabled && ModConfig.safeWalk) {
            boolean nearSurfaceLanding = ModConfig.jumpBridge && !player.isOnGround() && player.getVelocity().y <= 0.0D && player.fallDistance < 1.5F;
            if (player.isOnGround() || nearSurfaceLanding) {
                cir.setReturnValue(true);
            }
        }
    }

    @ModifyConstant(method = "adjustMovementForSneaking", constant = @Constant(doubleValue = 0.05D))
    private double modifyEdgeOffset(double originalOffset) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (com.ersin.legitbridge.utils.VersionHelper.getWorld(player).isClient && ModConfig.enabled && ModConfig.safeWalk) {
            boolean nearSurfaceLanding = ModConfig.jumpBridge && !player.isOnGround() && player.getVelocity().y <= 0.0D && player.fallDistance < 1.5F;
            if (player.isOnGround() || nearSurfaceLanding) {
                // Eğilerek/SafeWalk ile yol yaparken (Bridging), oyuncunun blok yüzeyini görebilmesi için 
                // offset değerinin orijinal 0.05 veya daha düşük olması gerekir. 
                // -0.15D vererek oyuncunun kenardan çok hafif sarkmasını sağlıyoruz, böylece düşmeden bloğun yanına tıklayabilir!
                boolean isBridging = com.ersin.legitbridge.utils.VersionHelper.getPitch(player) > 60.0f && player.getMainHandStack().getItem() instanceof net.minecraft.item.BlockItem;
                
                if (isBridging) {
                    return -0.15D; // Kenardan 0.15 blok dışarı sarkmasına izin ver (Asla düşmez, sadece hitbox açığa çıkar)
                }

                currentJitter = Humanizer.getSmoothedJitter(currentJitter, ModConfig.minEdgeOffset, ModConfig.maxEdgeOffset);
                return currentJitter;
            }
        }
        return originalOffset;
    }
}

