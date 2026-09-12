package com.ersin.legitbridge.mixin;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntityHitbox {
    
    @Shadow public abstract Box getBoundingBox();

    @Inject(method = "getBoundingBox", at = @At("RETURN"), cancellable = true)
    private void expandHitbox(CallbackInfoReturnable<Box> cir) {
        if (ModConfig.enabled && ModConfig.hitBoxExpander) {
            Entity entity = (Entity) (Object) this;
            // Sadece oyuncuları büyüt (kendimiz hariç)
            if (entity instanceof net.minecraft.entity.player.PlayerEntity) {
                net.minecraft.client.network.ClientPlayerEntity clientPlayer = net.minecraft.client.MinecraftClient.getInstance().player;
                if (clientPlayer != null && !entity.equals(clientPlayer)) {
                    Box originalBox = cir.getReturnValue();
                    // Hitbox'u her yöne 0.2 blok genişlet (çok abartmadan)
                    cir.setReturnValue(originalBox.expand(ModConfig.hitBoxExpanderSize));
                }
            }
        }
    }
}
