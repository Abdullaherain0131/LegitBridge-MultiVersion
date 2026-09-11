package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;

public class LegitScaffold extends Module {
    public LegitScaffold() {
        super("LegitScaffold", "LegitScaffold module", Category.LEGIT);
    }

    
    private boolean wasSneaking = false;
    
    @Override
    public void onTick() {
        if (!ModConfig.enabled || !ModConfig.legitScaffold) {
            if (wasSneaking) {
//? if <=1.19.2 {
                /*client.options.keySneak.setPressed(false);
*///?} else {
                client.options.sneakKey.setPressed(false);
//?}
                wasSneaking = false;
            }
            return;
        }
        
        if (client.player == null || client.world == null) return;
        
        // Sadece elinde blok varken ve yere değiyorken çalışsın
        if (!client.player.isOnGround()) {
            if (wasSneaking) {
//? if <=1.19.2 {
                /*client.options.keySneak.setPressed(false);
*///?} else {
                client.options.sneakKey.setPressed(false);
//?}
                wasSneaking = false;
            }
            return;
        }
        
        if (client.player.getMainHandStack().isEmpty() || !(client.player.getMainHandStack().getItem() instanceof BlockItem)) {
            if (wasSneaking) {
//? if <=1.19.2 {
                /*client.options.keySneak.setPressed(false);
*///?} else {
                client.options.sneakKey.setPressed(false);
//?}
                wasSneaking = false;
            }
            return;
        }

        // Oyuncunun geriye veya yanlara doğru hareket edip etmediğini kontrol et
//? if <=1.19.2 {
        /*boolean isMoving = client.options.keyBack.isPressed() || client.options.keyLeft.isPressed() || client.options.keyRight.isPressed();
*///?} else {
        boolean isMoving = client.options.backKey.isPressed() || client.options.leftKey.isPressed() || client.options.rightKey.isPressed();
//?}
        
        // Shift tuşuna zaten kendi basıyorsa bizim müdahale etmemize gerek yok
//? if <=1.19.2 {
        /*if (client.options.keySneak.isPressed() && !wasSneaking) {
*///?} else {
        if (client.options.sneakKey.isPressed() && !wasSneaking) {
//?}
            return; 
        }

        if (isMoving) {
            // Tam bloğun ucunda mı? 
            double x = client.player.getX() - Math.floor(client.player.getX());
            double z = client.player.getZ() - Math.floor(client.player.getZ());
            
            boolean atEdgeX = x < ModConfig.maxEdgeOffset || x > (1.0 - ModConfig.maxEdgeOffset);
            boolean atEdgeZ = z < ModConfig.maxEdgeOffset || z > (1.0 - ModConfig.maxEdgeOffset);
            
//? if <=1.19.2 {
                        /*if ((atEdgeX || atEdgeZ) && client.player.pitch > 60.0f) {
*///?} else {
                        if ((atEdgeX || atEdgeZ) && com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) > 60.0f) {
//?}
                if (!wasSneaking) {
//? if <=1.19.2 {
                    /*client.options.keySneak.setPressed(true); // Bloğun ucuna geldiğinde eğil
*///?} else {
                    client.options.sneakKey.setPressed(true); // Bloğun ucuna geldiğinde eğil
//?}
                    wasSneaking = true;
                }
                
                // Ve otomatik olarak sağ tık yaparak bloğu koy
                if (client.player.getItemUseTime() == 0) { // Spam'ı önlemek için basit kontrol (veya direkt bas)
                    ((com.ersin.legitbridge.mixin.MinecraftClientAccessor) client).invokeDoItemUse();
                }
            } else {
                if (wasSneaking) {
                    // Blok koyulduktan sonra / bloğun ortasına gelindiğinde kalk
//? if <=1.19.2 {
                    /*client.options.keySneak.setPressed(false); 
*///?} else {
                    client.options.sneakKey.setPressed(false); 
//?}
                    wasSneaking = false;
                }
            }
        } else {
            if (wasSneaking) {
//? if <=1.19.2 {
                /*client.options.keySneak.setPressed(false);
*///?} else {
                client.options.sneakKey.setPressed(false);
//?}
                wasSneaking = false;
            }
        }
    }
}
