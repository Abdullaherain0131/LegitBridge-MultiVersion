package com.ersin.legitbridge.mixin;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.LegitBoosterMod;
import com.ersin.legitbridge.module.impl.Freecam;
import net.minecraft.network.ClientConnection;
//? if <=1.19.2 {
/*import net.minecraft.network.Packet;
*///?} else {
import net.minecraft.network.packet.Packet;
//?}
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class MixinClientConnection {
    @Inject(method = "send(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (LegitBoosterMod.moduleManager == null) return;
        
        Freecam freecam = (Freecam) LegitBoosterMod.moduleManager.getModuleByName("Freecam");
        if (freecam != null && freecam.isEnabled() && packet instanceof PlayerMoveC2SPacket) {
            ci.cancel();
            return;
        }
        
        // Derp modifies the packet values (or cancels and sends a new one)
        com.ersin.legitbridge.module.impl.Derp derp = (com.ersin.legitbridge.module.impl.Derp) LegitBoosterMod.moduleManager.getModuleByName("Derp");
        if (derp != null && derp.isEnabled()) {
            if (derp.onSendPacket(packet)) {
                ci.cancel();
                return;
            }
        }

        // FakeLag buffers the packet
        com.ersin.legitbridge.module.impl.FakeLag fakeLag = (com.ersin.legitbridge.module.impl.FakeLag) LegitBoosterMod.moduleManager.getModuleByName("FakeLag");
        if (fakeLag != null && fakeLag.isEnabled()) {
            if (fakeLag.onSendPacket(packet, (ClientConnection) (Object) this)) {
                ci.cancel();
                return;
            }
        }
        
        // Scaffold spoofs the rotation
        com.ersin.legitbridge.module.impl.Scaffold scaffold = (com.ersin.legitbridge.module.impl.Scaffold) LegitBoosterMod.moduleManager.getModuleByName("Scaffold");
        if (scaffold != null && scaffold.isEnabled()) {
            if (scaffold.onSendPacket(packet)) {
                ci.cancel();
                return;
            }
        }
    }
}
