package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Hand;

public class LegitAutoPot extends Module {

    private int state = 0; // 0: Idle, 1: Aiming Down, 2: Throwing, 3: Aiming Back, 4: Soup Wait, 5: Drop Bowl
    private float originalPitch;
    private int oldSlot = -1;
    private int soupTimeout = 0;
    
    public LegitAutoPot() {
        super("LegitAutoPot", "Can azaldığında pot atar veya çorba (Soup) içer. Kaseyi otomatik atar.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.autoPot || client.player == null || client.world == null) {
            state = 0;
            return;
        }
        
        // Auto Refill Soup logic
        refillSoup();

        if (state == 0) {
            // Can %40'ın altına düştüğünde (8 sağlık puanı = 4 kalp)
            if (client.player.getHealth() <= 8.0f) {
                int[] healingItem = findHealingItem(); // [slot, type(0=pot, 1=soup)]
                if (healingItem[0] != -1) {
                    oldSlot = com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot;
                    com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = healingItem[0];
                    if (healingItem[1] == 0) {
                        // Pot -> Aşağı bak ve at
//? if <=1.19.2 {
                        /*originalPitch = client.player.pitch;
*///?} else {
                        originalPitch = com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player);
//?}
//? if <=1.19.2 {
                        /*client.player.pitch = 90.0f;
*///?} else {
                        com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, 90.0f);;
//?}
                        state = 1;
                    } else {
                        // Soup -> Direk iç
                        if (client.interactionManager != null) {
                            com.ersin.legitbridge.utils.VersionHelper.interactItem(client, Hand.MAIN_HAND);
                        }
                        soupTimeout = 40; // 2 saniye bekleme süresi max
                        state = 4;
                    }
                }
            }
        } else if (state == 1) {
            // İksiri fırlatmak için yavaşça eğil
//? if <=1.19.2 {
            /*if (client.player.pitch < 85.0f) {
*///?} else {
            if (com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) < 85.0f) {
//?}
//? if <=1.19.2 {
                /*client.player.pitch += 12.0f;
*///?} else {
                com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) + 12.0f);
//?}
            } else {
//? if <=1.19.2 {
                /*client.player.pitch = 90.0f;
*///?} else {
                com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, 90.0f);;
//?}
                state = 2;
            }
        } else if (state == 2) {
            // İksiri fırlat
            if (client.interactionManager != null) {
                com.ersin.legitbridge.utils.VersionHelper.interactItem(client, Hand.MAIN_HAND);
            }
            state = 3;
        } else if (state == 3) {
            // Kafayı geri kaldır
//? if <=1.19.2 {
            /*if (client.player.pitch > originalPitch + 5.0f) {
*///?} else {
            if (com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) > originalPitch + 5.0f) {
//?}
//? if <=1.19.2 {
                /*client.player.pitch -= 12.0f;
*///?} else {
                com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player) - 12.0f);
//?}
            } else {
//? if <=1.19.2 {
                /*client.player.pitch = originalPitch;
*///?} else {
                com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, originalPitch);;
//?}
                restoreSlot();
            }
        } else if (state == 4) {
            // Çorba içildi mi kontrol et (Elde boş kase var mı)
            ItemStack mainHand = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(oldSlot);
            if (mainHand.getItem() == Items.BOWL) {
                state = 5;
            } else {
                soupTimeout--;
                if (soupTimeout <= 0) {
                    restoreSlot(); // İçemedi veya kase gelmedi, iptal et
                }
            }
        } else if (state == 5) {
            // Boş kaseyi (Q) yere at
            if (client.interactionManager != null) {
                //? if >1.19.2 {
                client.interactionManager.dropCreativeStack(net.minecraft.item.ItemStack.EMPTY);
                client.player.dropSelectedItem(true);
                //?} else {
                /*client.player.dropSelectedItem(true);*///?}
            }
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
            if (isSoup(stack)) return;
        }
        
        // Find soup in inventory (slots 9 to 35)
        for (int i = 9; i < 36; i++) {
            ItemStack stack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
            if (isSoup(stack)) {
                int targetHotbarSlot = 8;
                for (int j = 0; j < 9; j++) {
                    ItemStack hStack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(j);
                    if (hStack.isEmpty() || hStack.getItem() == Items.BOWL) {
                        targetHotbarSlot = j;
                        break;
                    }
                }
                //? if >1.19.2 {
                client.interactionManager.clickSlot(0, i, targetHotbarSlot, net.minecraft.screen.slot.SlotActionType.SWAP, client.player);
                //?} else {
                /*client.interactionManager.clickSlot(0, i, targetHotbarSlot, net.minecraft.screen.slot.SlotActionType.SWAP, client.player);*///?}
                break;
            }
        }
    }
}
