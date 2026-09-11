package com.ersin.legitbridge.mixin;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {
    
    @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
    private void onIsInvisible(CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.trueSight) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    private void onIsInvisibleTo(net.minecraft.entity.player.PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.trueSight) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        com.ersin.legitbridge.module.impl.PlayerESP esp = (com.ersin.legitbridge.module.impl.PlayerESP) com.ersin.legitbridge.LegitBoosterMod.moduleManager.getModuleByName("PlayerESP");
        if (esp != null && esp.isEnabled()) {
            if ((Object) this instanceof net.minecraft.entity.player.PlayerEntity) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    private void onGetName(CallbackInfoReturnable<String> cir) {
        if (ModConfig.streamerMode && (Object) this instanceof net.minecraft.entity.player.PlayerEntity) {
            cir.setReturnValue("Player" + com.ersin.legitbridge.utils.VersionHelper.getEntityId(((Entity)(Object)this)));
        }
    }

    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    private void onGetDisplayName(CallbackInfoReturnable<net.minecraft.text.Text> cir) {
        if (ModConfig.streamerMode && (Object) this instanceof net.minecraft.entity.player.PlayerEntity) {
            cir.setReturnValue(com.ersin.legitbridge.utils.VersionHelper.literalText("Player" + com.ersin.legitbridge.utils.VersionHelper.getEntityId(((Entity)(Object)this))));
        }
    }
}
