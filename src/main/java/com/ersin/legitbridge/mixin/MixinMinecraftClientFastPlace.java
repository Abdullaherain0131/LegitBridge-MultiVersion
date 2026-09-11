package com.ersin.legitbridge.mixin;

import com.ersin.legitbridge.Humanizer;
import com.ersin.legitbridge.LegitBoosterMod;
import com.ersin.legitbridge.module.impl.JumpBridge;
import com.ersin.legitbridge.module.impl.SlotRotation;
import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClientFastPlace {

    @Shadow private int itemUseCooldown;
    @Shadow public ClientPlayerEntity player;
    @Shadow public HitResult crosshairTarget;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        
        if (ModConfig.enabled && this.player != null &&
//? if <=1.19.2 {
            /*com.ersin.legitbridge.utils.VersionHelper.isUseKeyPressed(client)) {
*///?} else {
            com.ersin.legitbridge.utils.VersionHelper.isUseKeyPressed(client)) {
//?}
            
            // Eğer directionalAssist aktifse, sadece bir bloğa bakıyorsak paket gönder (Gereksiz boşluk paketlerini önle)
            boolean isValidTarget = true;
            if (ModConfig.directionalAssist) {
                isValidTarget = (this.crosshairTarget != null && this.crosshairTarget.getType() == HitResult.Type.BLOCK);
            }
            
            if (isValidTarget) {
                // Kasıtlı olarak tıklama atlama (Drop) - İnsan simülasyonu
                if (Humanizer.shouldDropClick()) {
                    return; // Bu tick'te basmayı atla
                }

                // Oyuncunun iki elinden birinde en az bir eşya var mı?
                if (!this.player.getMainHandStack().isEmpty() || !this.player.getOffHandStack().isEmpty()) {
                    
                    // JumpBridge Zıplayarak Merdiven Tipi Yol Yapma Desteği
                    JumpBridge jumpBridge = (JumpBridge) LegitBoosterMod.moduleManager.getModuleByName("JumpBridge");
                    if (jumpBridge != null && jumpBridge.shouldInstantPlaceOnJump(client)) {
                        this.itemUseCooldown = 0; // Zıplamanın zirve noktasında ayağın altına anında koy
                        
                        SlotRotation slotRotation = (SlotRotation) LegitBoosterMod.moduleManager.getModuleByName("SlotRotation");
                        if (slotRotation != null) slotRotation.onBlockPlaced(client);
                        
                        com.ersin.legitbridge.SessionTracker.onBlockPlaced();
                        return;
                    }

                    // Vanilla cooldown maksimum (4) değeri aştığında devreye gir
                    if (this.itemUseCooldown > ModConfig.maxDelayTick) {
                        
                        // Dinamik ve rastgele Gauss gecikmesi üretimi
                        this.itemUseCooldown = Humanizer.getGaussianDelay(ModConfig.minDelayTick, ModConfig.maxDelayTick);
                        
                        // Slot Rotasyonu Asistanını Tetikle
                        SlotRotation slotRotation = (SlotRotation) LegitBoosterMod.moduleManager.getModuleByName("SlotRotation");
                        if (slotRotation != null) slotRotation.onBlockPlaced(client);
                        
                        com.ersin.legitbridge.SessionTracker.onBlockPlaced();
                    }

                }
            }
        }
    }
}
