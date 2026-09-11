package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
//? if <=1.19.2 {
/*import net.minecraft.item.MushroomStewItem;
*///?}
import net.minecraft.util.Hand;

public class AutoPot extends Module {

    private int delay = 0;

    public AutoPot() {
        super("AutoPot", "Canın azaldığında otomatik olarak iksir atar veya çorba içer.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (client.player == null || client.world == null) return;

        if (delay > 0) {
            delay--;
            return;
        }

        // Can 4 kalbin (8.0f) altındaysa çalışır
        if (client.player.getHealth() <= 8.0f) {
            int healSlot = findHealingItem();
            
            if (healSlot != -1) {
                int oldSlot = com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot;
                
                // Aşağı bakarak iksiri fırlat
//? if <=1.19.2 {
                /*float oldPitch = client.player.pitch;
*///?} else {
                float oldPitch = com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player);
//?}
//? if <=1.19.2 {
                /*client.player.pitch = 90f;
*///?} else {
                com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, 90f);;
//?}
                
                // Eşyayı eline al ve kullan
                com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = healSlot;
                if (client.interactionManager != null) {
                    //? if <=1.19.2 {
                    /*client.interactionManager.interactItem(client.player, client.world, Hand.MAIN_HAND);
*///?} else {
                    com.ersin.legitbridge.utils.VersionHelper.interactItem(client, Hand.MAIN_HAND);
//?}
                }
                com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = oldSlot;
//? if <=1.19.2 {
                /*client.player.pitch = oldPitch;
*///?} else {
                com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, oldPitch);;
//?}
                
                delay = 10; // Spam yapmamak için yarım saniye bekle
            }
        }
    }

    private int findHealingItem() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
            Item item = stack.getItem();
            
            // Splash Potion of Healing veya Çorba arıyoruz
            if (item instanceof SplashPotionItem || //? if <=1.19.2 {
            /*item instanceof MushroomStewItem
*///?} else {
            item == net.minecraft.item.Items.MUSHROOM_STEW
//?}
            ) {
                // Not: Aslında Splash potion'ın tipine (Healing mi Zehir mi) bakmak daha güvenli, 
                // ama basitlik açısından doğrudan fırlatılabilir iksirleri kullanıyoruz.
                return i;
            }
        }
        return -1;
    }
}
