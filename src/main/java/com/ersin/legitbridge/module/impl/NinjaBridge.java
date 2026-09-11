package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;

public class NinjaBridge extends Module {
    public NinjaBridge() {
        super("NinjaBridge", "NinjaBridge module", Category.MOVEMENT);
    }

    
    private boolean isAutoSneaking = false;
    private int unsneakDelayTicks = 0;
    
    @Override
    public void onTick() {
        
        if (!ModConfig.enabled || !ModConfig.ninjaBridge || client.player == null || client.world == null) {
            if (isAutoSneaking) {
//? if <=1.19.2 {
                /*client.options.keySneak.setPressed(false);
*///?} else {
                client.options.sneakKey.setPressed(false);
//?}
                isAutoSneaking = false;
            }
            return;
        }
        
        // Sadece elde blok varken ve aşağı/çapraz aşağı bakarken çalışsın (Pitch > 50)
        ItemStack mainHand = client.player.getMainHandStack();
//? if <=1.19.2 {
                /*if (!(mainHand.getItem() instanceof BlockItem) || client.player.pitch < 50.0f) {
*///?} else {
                if (!(mainHand.getItem() instanceof BlockItem) || com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) < 50.0f) {
//?}
            if (isAutoSneaking) {
//? if <=1.19.2 {
                /*client.options.keySneak.setPressed(false);
*///?} else {
                client.options.sneakKey.setPressed(false);
//?}
                isAutoSneaking = false;
            }
            return;
        }

        // Oyuncu yerdeyse kontrol et
        if (client.player.isOnGround()) {
            
            // Eğer shift'i bırakmamız için bir delay (gecikme) atanmışsa, onu bekle
            if (unsneakDelayTicks > 0) {
                unsneakDelayTicks--;
                if (unsneakDelayTicks == 0) {
//? if <=1.19.2 {
                    /*client.options.keySneak.setPressed(false);
*///?} else {
                    client.options.sneakKey.setPressed(false);
//?}
                    isAutoSneaking = false;
                }
                return; // Gecikme bitene kadar aşağıdaki kenar kontrolünü atla
            }

            // Hızımızı ve yönümüzü alıp 2-3 tick sonrasını tahmin edelim
            double velX = client.player.getVelocity().x;
            double velZ = client.player.getVelocity().z;
            
            // Sabit bir offset ile kenar tespiti yapalım. (Oyuncunun gidiş yönüne doğru)
            double predictFactor = 3.5; 
            double nextX = client.player.getX() + (velX * predictFactor);
            double nextZ = client.player.getZ() + (velZ * predictFactor);
            
            // Eğer oyuncu duruyorsa ama kenardaysa da tetiklenmesi için minik bir manuel yön okuması:
            if (Math.abs(velX) < 0.01 && Math.abs(velZ) < 0.01) {
//? if <=1.19.2 {
                                /*float yaw = (float) Math.toRadians(client.player.yaw);
*///?} else {
                                float yaw = (float) Math.toRadians(com.ersin.legitbridge.utils.VersionHelper.getYaw(client.player));
//?}
                // Geriye basıyorsa
//? if <=1.19.2 {
                /*if (client.options.keyBack.isPressed()) {
*///?} else {
                if (client.options.backKey.isPressed()) {
//?}
                    nextX += Math.sin(yaw) * 0.2;
                    nextZ -= Math.cos(yaw) * 0.2;
                }
            }
            
            BlockPos underPos = new BlockPos((int) Math.floor(nextX), (int) Math.floor(client.player.getY() - 0.5), (int) Math.floor(nextZ));
            
            // Eğer önümüzdeki blok havaysa (kenara geldiysek)
            if (client.world.getBlockState(underPos).isAir()) {
                // Kenara yaklaşıyoruz, shift'e bas!
//? if <=1.19.2 {
                /*if (!client.options.keySneak.isPressed()) {
*///?} else {
                if (!client.options.sneakKey.isPressed()) {
//?}
//? if <=1.19.2 {
                    /*client.options.keySneak.setPressed(true);
*///?} else {
                    client.options.sneakKey.setPressed(true);
//?}
                    isAutoSneaking = true;
                }
            } else {
                // Önümüzde blok var (blok yeni konuldu veya henüz kenara gelmedik)
                // Hemen shift'i bırakmak makro gibi algılanır. Rastgele bir insan tepki süresi ekleyelim.
                if (isAutoSneaking && unsneakDelayTicks == 0) {
                    // Blok konulduktan sonra Shift'i bırakmak için 1 ile 3 tick arası bekle (Humanized Delay)
                    unsneakDelayTicks = com.ersin.legitbridge.Humanizer.getGaussianDelay(1, 3);
                }
            }
        } else {
            // Havadayken (zıplarken vs) oto-eğilmeyi bırak
            if (isAutoSneaking) {
//? if <=1.19.2 {
                /*client.options.keySneak.setPressed(false);
*///?} else {
                client.options.sneakKey.setPressed(false);
//?}
                isAutoSneaking = false;
                unsneakDelayTicks = 0;
            }
        }
    }
}
