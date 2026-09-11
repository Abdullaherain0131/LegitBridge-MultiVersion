package com.ersin.legitbridge.mixin;

import com.ersin.legitbridge.CombatTracker;
import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
//? if <=1.19.2 {
/*import net.minecraft.text.LiteralText;
*///?}
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class MixinChatHud {

    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At("HEAD"), argsOnly = true)
    private Text modifyChatMessage(Text message) {
        if (ModConfig.streamerMode) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                String myName = client.player.getName().getString();
                String original = message.getString();
                if (original.contains(myName)) {
                    //? if <=1.19.2 {
                    /*return new net.minecraft.text.LiteralText(original.replace(myName, "Sen"));
                    *///?} else {
                    return com.ersin.legitbridge.utils.VersionHelper.literalText(original.replace(myName, "Sen"));
                    //?}
                }
            }
        }
        return message;
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At("HEAD"))
    private void onChat(Text message, int messageId, int timestamp, boolean refresh, CallbackInfo ci) {
        String msg = message.getString().toLowerCase();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        
        String myName = client.player.getName().getString().toLowerCase();
        
        // Basic kill detection logic
        if (msg.contains(myName)) {
            if ((msg.contains("killed by") || msg.contains("tarafından") || msg.contains("slain by") || msg.contains("öldürdü")) && msg.endsWith(myName)) {
                CombatTracker.addKill();
            } else if (msg.contains("killed by " + myName) || msg.contains(myName + " öldürdü") || msg.contains(myName + " katletti")) {
                CombatTracker.addKill();
            } else if (msg.startsWith(myName) && (msg.contains("was killed by") || msg.contains("öldürüldü") || msg.contains("katledildi"))) {
                // If I am the one dying
                CombatTracker.onPlayerDeath();
            }
        }
    }
}
