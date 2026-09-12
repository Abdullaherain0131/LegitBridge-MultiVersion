package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.Humanizer;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Hand;

public class LegitAutoPot extends Module {

    private int state = 0; // 0: Idle, 1-3: Pot, 10-16: Soup
    private float originalPitch;
    private int oldSlot = -1;
    private int delayTimer = 0;
    private int refillDelayTimer = 0;
    private int targetSoupSlot = -1;
    
    public LegitAutoPot() {
        super("LegitAutoPot", "Can azaldığında pot atar veya çorba (Soup) içer. Kaseyi otomatik atar.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.autoPot || client.player == null || client.world == null) {
            state = 0;
            return;
        }
        
        // Timer counting down
        if (delayTimer > 0) {
            delayTimer--;
            return;
        }
        
        // Auto Refill Soup logic with delay
        if (refillDelayTimer > 0) {
            refillDelayTimer--;
        } else if (state == 0) {
            refillSoup();
            refillDelayTimer = Humanizer.getGaussianDelay(10, 30); // Refill every 0.5-1.5 seconds minimum to look legit
        }

        if (state == 0) {
            // Can %40'ın altına düştüğünde (8 sağlık puanı = 4 kalp)
            if (client.player.getHealth() <= 8.0f) {
                int[] healingItem = findHealingItem(); // [slot, type(0=pot, 1=soup)]
                if (healingItem[0] != -1) {
                    oldSlot = com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot;
                    
                    if (healingItem[1] == 0) {
                        // Potting
                        com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = healingItem[0];
//? if <=1.19.2 {
                        /*originalPitch = client.player.pitch;
*///?} else {
                        originalPitch = com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player);
//?}
                        state = 1;
                    } else {
                        // Soup eating
                        targetSoupSlot = healingItem[0];
                        delayTimer = Humanizer.getGaussianDelay(1, 3); // Wait 1-3 ticks before swapping
                        state = 10;
                    }
                }
            }
        } else if (state == 1) { // POT
//? if <=1.19.2 {
            /*if (client.player.pitch < 85.0f) {
                client.player.pitch = com.ersin.legitbridge.utils.GCDFix.applyGCD(client.player.pitch + 15.0f, client.player.pitch, client);
            } else {
                client.player.pitch = 90.0f;
                state = 2;
            }
*///?} else {
            if (com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) < 85.0f) {
                com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, com.ersin.legitbridge.utils.GCDFix.applyGCD(com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) + 15.0f, com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player), client));
            } else {
                com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, 90.0f);;
                state = 2;
            }
//?}
        } else if (state == 2) {
            if (client.interactionManager != null) {
                com.ersin.legitbridge.utils.VersionHelper.interactItem(client, Hand.MAIN_HAND);
            }
            delayTimer = Humanizer.getGaussianDelay(1, 2);
            state = 3;
        } else if (state == 3) {
//? if <=1.19.2 {
            /*if (client.player.pitch > originalPitch + 5.0f) {
                client.player.pitch = com.ersin.legitbridge.utils.GCDFix.applyGCD(client.player.pitch - 15.0f, client.player.pitch, client);
            } else {
                client.player.pitch = originalPitch;
                restoreSlot();
            }
*///?} else {
            if (com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) > originalPitch + 5.0f) {
                com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, com.ersin.legitbridge.utils.GCDFix.applyGCD(com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) - 15.0f, com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player), client));
            } else {
                com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, originalPitch);;
                restoreSlot();
            }
//?}
        } else if (state == 10) { // SOUP: Swap slot
            com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = targetSoupSlot;
            delayTimer = Humanizer.getGaussianDelay(1, 3); // Wait 1-3 ticks before interact
            state = 11;
        } else if (state == 11) { // SOUP: Interact
            if (client.interactionManager != null) {
                com.ersin.legitbridge.utils.VersionHelper.interactItem(client, Hand.MAIN_HAND);
            }
            delayTimer = 2; // Check bowl every 2 ticks
            state = 12;
            targetSoupSlot = 20; // 20 is just a max check counter here
        } else if (state == 12) { // SOUP: Wait for bowl
            ItemStack mainHand = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot);
            if (mainHand.getItem() == Items.BOWL) {
                delayTimer = Humanizer.getGaussianDelay(1, 3); // Wait 1-3 ticks before dropping
                state = 13;
            } else {
                targetSoupSlot--;
                if (targetSoupSlot <= 0) {
                    restoreSlot(); // Timeout
                } else {
                    delayTimer = 2; // Wait again
                }
            }
        } else if (state == 13) { // SOUP: Drop bowl
            if (client.interactionManager != null) {
                client.player.dropSelectedItem(true); // Q press
            }
            delayTimer = Humanizer.getGaussianDelay(2, 4); // Wait 2-4 ticks before swapping back
            state = 14;
        } else if (state == 14) { // SOUP: Swap back
            restoreSlot();
        }
    }

    private void restoreSlot() {
        if (oldSlot != -1) {
            com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = oldSlot;
            oldSlot = -1;
        }
        state = 0;
    }

    // Dönüş: int array [slot, tip(0: pot, 1: soup)]
    private int[] findHealingItem() {
        // Önce Soup Ara (SoupPvP'de çok daha etkilidir)
        for (int i = 0; i < 9; i++) {
            ItemStack stack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
            if (stack.getItem() == Items.MUSHROOM_STEW || 
                stack.getItem() == Items.RABBIT_STEW || 
                stack.getItem() == Items.BEETROOT_SOUP) {
                return new int[]{i, 1};
            }
        }
        
        // Soup yoksa Pot ara
        for (int i = 0; i < 9; i++) {
            ItemStack stack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
            if (stack.getItem() == Items.SPLASH_POTION) {
                java.util.List<net.minecraft.entity.effect.StatusEffectInstance> effects = PotionUtil.getPotionEffects(stack);
                boolean isHealing = false;
                for (net.minecraft.entity.effect.StatusEffectInstance e : effects) {
                    if (e.getEffectType() == net.minecraft.entity.effect.StatusEffects.INSTANT_HEALTH ||
                        e.getEffectType() == net.minecraft.entity.effect.StatusEffects.REGENERATION) {
                        isHealing = true;
                        break;
                    }
                }
                if (isHealing) {
                    return new int[]{i, 0};
                }
            }
        }
        return new int[]{-1, -1};
    }

    private boolean isSoup(ItemStack stack) {
        return stack.getItem() == Items.MUSHROOM_STEW || 
               stack.getItem() == Items.RABBIT_STEW || 
               stack.getItem() == Items.BEETROOT_SOUP;
    }

    private void refillSoup() {
        if (client.player == null || client.interactionManager == null) return;
        
        // Check if we already have soup in hotbar
        for (int i = 0; i < 9; i++) {
            ItemStack stack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
            if (isSoup(stack)) return; // Don't refill if there's already a soup in hotbar
        }
        
        // Find soup in inventory (slots 9 to 35)
        for (int i = 9; i < 36; i++) {
            ItemStack stack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
            if (isSoup(stack)) {
                int targetHotbarSlot = 8; // Default to last slot
                for (int j = 0; j < 9; j++) {
                    ItemStack hStack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(j);
                    if (hStack.isEmpty() || hStack.getItem() == Items.BOWL) {
                        targetHotbarSlot = j;
                        break;
                    }
                }
                // Simulate human delay: wait a bit before moving items around inside inventory
                //? if >1.19.2 {
                client.interactionManager.clickSlot(0, i, targetHotbarSlot, net.minecraft.screen.slot.SlotActionType.SWAP, client.player);
                //?} else {
                /*client.interactionManager.clickSlot(0, i, targetHotbarSlot, net.minecraft.screen.slot.SlotActionType.SWAP, client.player);*///?}
                break;
            }
        }
    }
}
