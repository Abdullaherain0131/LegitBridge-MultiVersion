package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;

public class AutoEat extends Module {
    private int oldSlot = -1;
    private boolean isEating = false;

    public AutoEat() {
        super("AutoEat", "Automatically eats food when hungry or low on health.", Category.LEGIT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.autoEat) {
            if (isEating) stopEating();
            return;
        }

        if (client.player == null || client.world == null) return;

        boolean needsFood = client.player.getHungerManager().getFoodLevel() <= 15 || client.player.getHealth() < client.player.getMaxHealth();

        if (needsFood && !isEating) {
            int foodSlot = findFoodInHotbar();
            if (foodSlot != -1) {
                oldSlot = com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot;
                com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = foodSlot;
//? if <=1.19.2 {
                /*com.ersin.legitbridge.utils.VersionHelper.setUseKeyPressed(client, true);
*///?} else {
                com.ersin.legitbridge.utils.VersionHelper.setUseKeyPressed(client, true);
//?}
                isEating = true;
            }
        } else if (!needsFood && isEating) {
            stopEating();
        } else if (isEating) {
            // Check if we are still holding food
            ItemStack stack = client.player.getMainHandStack();
            if (!stack.isFood()) {
                stopEating();
            } else {
                // Ensure use key is pressed
//? if <=1.19.2 {
                /*com.ersin.legitbridge.utils.VersionHelper.setUseKeyPressed(client, true);
*///?} else {
                com.ersin.legitbridge.utils.VersionHelper.setUseKeyPressed(client, true);
//?}
            }
        }
    }

    private void stopEating() {
//? if <=1.19.2 {
        /*com.ersin.legitbridge.utils.VersionHelper.setUseKeyPressed(client, false);
*///?} else {
        com.ersin.legitbridge.utils.VersionHelper.setUseKeyPressed(client, false);
//?}
        if (oldSlot != -1) {
            com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = oldSlot;
            oldSlot = -1;
        }
        isEating = false;
    }

    private int findFoodInHotbar() {
        int bestSlot = -1;
        float bestSaturation = -1.0f;
        
        for (int i = 0; i < 9; i++) {
            ItemStack stack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
            if (stack.isFood()) {
                FoodComponent food = stack.getItem().getFoodComponent();
                if (food != null) {
                    float saturation = food.getSaturationModifier();
                    if (saturation > bestSaturation) {
                        bestSaturation = saturation;
                        bestSlot = i;
                    }
                }
            }
        }
        return bestSlot;
    }
}
