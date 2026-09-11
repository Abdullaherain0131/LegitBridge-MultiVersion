package com.ersin.legitbridge.mixin;

import com.ersin.legitbridge.ModHudRenderer;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MixinMouse {

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void onMouseButtonClick(long window, int button, int action, int mods, CallbackInfo ci) {
        // action == 1 means button press (0 is release)
        // button == 0 is Left Click, button == 1 is Right Click
        if (action == 1 && (button == 0 || button == 1)) {
            ModHudRenderer.onClientClick();
            com.ersin.legitbridge.AdvancedCombatHud.onMouseClick(button);
        }
    }
}
