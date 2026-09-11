package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.item.SwordItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;

public class AutoBlockDefense extends Module {
    private int delay = 0;
    
    public AutoBlockDefense() {
        super("AutoBlockDefense", "Hasar alınca otomatik önüne blok koyarak savunma yapar.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (client.player == null || client.world == null) return;

        if (delay > 0) {
            delay--;
            return;
        }

        // Hasar aldıysak (hurtTime > 0)
        if (client.player.hurtTime > 0) {
            int blockSlot = findBlockInHotbar();
            if (blockSlot != -1) {
                // Önündeki bloku hesapla (Ayak hizasının 1 blok önü)
                net.minecraft.util.math.Vec3d look = client.player.getRotationVector();
                net.minecraft.util.math.BlockPos playerPos = client.player.getBlockPos();
                
                // Baktığı yönde 1 blok ilerisi
                int dx = (int) Math.round(look.x);
                int dz = (int) Math.round(look.z);
                
                // Eğer oyuncu çapraz bakmıyorsa veya hiç bakmıyorsa varsayılan bir yön seçebiliriz
                // Ama genelde dx veya dz'den biri boş olmaz.
                net.minecraft.util.math.BlockPos targetPos = playerPos.add(dx, 0, dz);

                // Eğer o blok boşsa (Hava ise) yerleştir
                if (client.world.getBlockState(targetPos).isAir()) {
                    int oldSlot = com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot;
                    com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = blockSlot;
                    
                    BlockHitResult hitResult = new BlockHitResult(
                            new net.minecraft.util.math.Vec3d(targetPos.getX(), targetPos.getY(), targetPos.getZ()),
                            Direction.UP, targetPos, false
                    );
                        
                    if (client.interactionManager != null) {
                        //? if <=1.19.2 {
                        /*client.interactionManager.interactBlock(client.player, client.world, net.minecraft.util.Hand.MAIN_HAND, hitResult);
*///?} else {
                        client.interactionManager.interactBlock(client.player, net.minecraft.util.Hand.MAIN_HAND, hitResult);
//?}
                    }
                    
                    com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = oldSlot;
                    delay = 10; // Spam yapmayı önlemek için 10 tick bekle
                }
            }
        }
    }
    
    private int findBlockInHotbar() {
        for (int i = 0; i < 9; i++) {
            net.minecraft.item.ItemStack stack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
            if (stack.getItem() instanceof net.minecraft.item.BlockItem) {
                return i;
            }
        }
        return -1;
    }
}
