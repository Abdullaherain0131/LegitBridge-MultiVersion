package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;

import java.util.Random;

public class SlotRotation extends Module {
    public SlotRotation() {
        super("SlotRotation", "SlotRotation module", Category.LEGIT);
    }

    private final Random random = new Random();
    private int blocksPlacedInSlot = 0;

    public void onBlockPlaced(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        blocksPlacedInSlot++;
        // Rotate hotbar slot every 3 to 8 placed blocks to humanize bridging pattern
        if (blocksPlacedInSlot >= (3 + random.nextInt(6))) {
            blocksPlacedInSlot = 0;
            rotateToBlockSlot(client);
        }
    }

    private void rotateToBlockSlot(MinecraftClient client) {
        //? if <=1.19.2 {
        /*PlayerInventory inventory = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player);
        *///?} else {
        PlayerInventory inventory = (PlayerInventory) com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player);
        //?} else {
/*com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = (com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot + 1) % 9;
*///?}
        int currentSlot = inventory.selectedSlot;

        for (int i = 0; i < 9; i++) {
            int slot = (currentSlot + i + 1) % 9;
            ItemStack stack = inventory.getStack(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                inventory.selectedSlot = slot;
                break;
            }
        }
    }
}
