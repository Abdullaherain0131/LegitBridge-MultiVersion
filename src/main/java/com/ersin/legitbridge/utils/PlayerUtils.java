package com.ersin.legitbridge.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class PlayerUtils {
    public static PlayerInventory getInventory(PlayerEntity player) {
        //? if <=1.19.2 {
        /*return player.inventory;
        *///?} else {
        return (PlayerInventory) com.ersin.legitbridge.utils.VersionHelper.getInventory(player);
        //?}
    }
}
