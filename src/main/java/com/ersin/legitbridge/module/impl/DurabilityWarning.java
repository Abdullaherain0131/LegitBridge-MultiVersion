package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
//? if <=1.19.2 {
/*import net.minecraft.text.LiteralText;
*///?}

public class DurabilityWarning extends Module {
    
    public static boolean shouldWarn = false;
    public static String warningText = "";
    private long lastSwapTime = 0;

    public DurabilityWarning() {
        super("DurabilityWarning", "Warns you when armor/elytra is about to break and swaps it.", Category.LEGIT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.durabilityWarning && !ModConfig.autoElytraSwap) {
            shouldWarn = false;
            return;
        }

        if (client.player == null || client.world == null) return;

        shouldWarn = false;
        warningText = "";

        // Check Armor & Elytra
        for (int i = 0; i < 4; i++) { // 0: Boots, 1: Leggings, 2: Chestplate, 3: Helmet
            ItemStack armorPiece = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
            
            if (!armorPiece.isEmpty() && armorPiece.isDamageable()) {
                int maxDamage = armorPiece.getMaxDamage();
                int currentDamage = armorPiece.getDamage();
                int remaining = maxDamage - currentDamage;
                float percent = (float) remaining / maxDamage * 100.0f;

                if (percent < 5.0f || remaining < 10) {
                    shouldWarn = true;
                    if (armorPiece.getItem() == Items.ELYTRA) {
                        warningText = "DİKKAT: ELYTRA KIRILMAK ÜZERE! (" + remaining + ")";
                        if (ModConfig.autoElytraSwap) {
                            swapElytra(remaining);
                        }
                    } else {
                        warningText = "DİKKAT: ZIRH KIRILMAK ÜZERE! (" + remaining + ")";
                    }
                }
            }
        }
    }

    private void swapElytra(int currentRemaining) {
        long now = System.currentTimeMillis();
        if (now - lastSwapTime < 1000) return; // Prevent spam swapping

        // Find a fresh elytra in inventory
        for (int i = 9; i < 36; i++) { // Main inventory
            ItemStack stack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
            if (!stack.isEmpty() && stack.getItem() == Items.ELYTRA) {
                int rem = stack.getMaxDamage() - stack.getDamage();
                if (rem > currentRemaining + 50) { // Ensure it's actually better
                    // Perform swap
                    if (client.interactionManager != null) {
                        // Click chestplate slot (Slot 6 in player container)
                        client.interactionManager.clickSlot(client.player.playerScreenHandler.syncId, 6, i, SlotActionType.SWAP, client.player);
                        //? if <=1.19.2 {
/*client.player.sendMessage(new net.minecraft.text.LiteralText("§a[LegitBridge] Elytra otomatik değiştirildi!"), true);
*///?} else {
client.player.sendMessage(com.ersin.legitbridge.utils.VersionHelper.literalText("§a[LegitBridge] Elytra otomatik değiştirildi!"), true);
//?}
lastSwapTime = now;
                        return;
                    }
                }
            }
        }
    }
}
