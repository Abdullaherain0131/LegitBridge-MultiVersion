package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;

public class AutoTool extends Module {
    public AutoTool() {
        super("AutoTool", "AutoTool module", Category.LEGIT);
    }


    @Override
    public void onTick() {
        if (client.player == null || client.world == null) {
            return;
        }

        HitResult target = client.crosshairTarget;
        if (target == null) return;

        //? if <=1.19.2 {
        /*PlayerInventory inventory = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player);
        *///?} else {
        PlayerInventory inventory = (PlayerInventory) com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player);
        //?}
        // Auto Weapon on Entity Target
//? if <=1.19.2 {
        /*if (com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client) && target.getType() == HitResult.Type.ENTITY) {
*///?} else {
        if (com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client) && target.getType() == HitResult.Type.ENTITY) {
//?}
            int bestWeaponSlot = findBestWeaponSlot(inventory);
            if (bestWeaponSlot != -1 && bestWeaponSlot != inventory.selectedSlot) {
                inventory.selectedSlot = bestWeaponSlot;
            }
            return;
        }

        // Auto Tool on Block Target when mining
//? if <=1.19.2 {
        /*if (com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client) && target.getType() == HitResult.Type.BLOCK) {
*///?} else {
        if (com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client) && target.getType() == HitResult.Type.BLOCK) {
//?}
            BlockHitResult blockHit = (BlockHitResult) target;
            BlockState state = client.world.getBlockState(blockHit.getBlockPos());
            
            if (!state.isAir()) {
                int bestToolSlot = findBestToolSlot(inventory, state);
                if (bestToolSlot != -1 && bestToolSlot != inventory.selectedSlot) {
                    inventory.selectedSlot = bestToolSlot;
                }
            }
        }
    }

    private int findBestWeaponSlot(PlayerInventory inventory) {
        int bestSlot = -1;
        float bestDamage = 0;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                float damage = 0;
                if (item instanceof SwordItem) {
                    damage = ((SwordItem) item).getAttackDamage() + 4.0f;
                } else if (item instanceof MiningToolItem) {
                    damage = ((MiningToolItem) item).getAttackDamage() + 2.0f;
                }

                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestSlot = i;
                }
            }
        }
        return bestSlot;
    }

    private int findBestToolSlot(PlayerInventory inventory, BlockState state) {
        int bestSlot = -1;
        float bestSpeed = 1.0f;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                float speed = stack.getMiningSpeedMultiplier(state);
                if (speed > bestSpeed) {
                    bestSpeed = speed;
                    bestSlot = i;
                }
            }
        }
        return bestSlot;
    }
}
