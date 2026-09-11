package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class InvMove extends Module {

    public InvMove() {
        super("InvMove", "Envanter açıkken hareket etmeni sağlar", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.invMove || client.player == null || client.currentScreen == null) return;
        
        // Chat veya yazı yazılan ekranlardaysak çalışma
        if (client.currentScreen instanceof ChatScreen || 
            client.currentScreen instanceof SignEditScreen || 
            client.currentScreen instanceof AnvilScreen || 
            client.currentScreen instanceof BookEditScreen) {
            return;
        }

        long handle = client.getWindow().getHandle();
        
        KeyBinding[] keys = {
//? if <=1.19.2 {
            /*client.options.keyForward,
*///?} else {
            //? if >1.19.2 {
client.options.forwardKey,
//?} else {
/*client.options.keyForward,*///?}
//?}
            //? if >1.19.2 {
client.options.backKey,
//?} else {
/*client.options.keyBack,*///?}
            //? if >1.19.2 {
client.options.leftKey,
//?} else {
/*client.options.keyLeft,*///?}
            //? if >1.19.2 {
client.options.rightKey,
//?} else {
/*client.options.keyRight,*///?}
//? if <=1.19.2 {
            /*client.options.keyJump,
*///?} else {
            client.options.jumpKey,
//?}
//? if <=1.19.2 {
            /*client.options.keySprint
*///?} else {
            client.options.sprintKey
//?}
        };
        
        for (KeyBinding key : keys) {
            InputUtil.Key boundKey = key.getDefaultKey();
            if (boundKey != null && boundKey.getCategory() == InputUtil.Type.KEYSYM) {
                boolean isPressed = InputUtil.isKeyPressed(handle, boundKey.getCode());
                key.setPressed(isPressed);
            }
        }
    }
}
