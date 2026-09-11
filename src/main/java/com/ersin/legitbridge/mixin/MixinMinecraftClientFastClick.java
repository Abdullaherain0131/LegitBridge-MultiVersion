package com.ersin.legitbridge.mixin;

import com.ersin.legitbridge.Humanizer;
import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClientFastClick implements MinecraftClientAccessor {

    @Shadow private int attackCooldown;
    @Shadow public ClientPlayerEntity player;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTickFastClick(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        
//? if <=1.19.2 {
        /*if (ModConfig.enabled && ModConfig.fastClick && this.player != null && com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client)) {
*///?} else {
        if (ModConfig.enabled && ModConfig.fastClick && this.player != null && com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client)) {
//?}
            
            // Kasıtlı olarak tıklama atlama (Drop) - İnsan simülasyonu
            if (Humanizer.shouldDropClick()) {
                return; // Bu tick'te basmayı atla
            }

            // Vanilla cooldown maksimum değeri aştığında devreye gir
            if (this.attackCooldown > ModConfig.maxFastClickDelay) {
                
                // Doğrudan vanilla saldırı metodunu çağırıyoruz (Hack bayrağı yememek için)
                this.invokeDoAttack();
                
                // Dinamik ve rastgele Gauss gecikmesi üretimi
                this.attackCooldown = Humanizer.getGaussianDelay(ModConfig.minFastClickDelay, ModConfig.maxFastClickDelay);
            }
        }
    }
}
