package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class LavaEvader extends Module {

    private boolean evaded = false;

    public LavaEvader() {
        super("LavaEvader", "Lavın içine düştüğünde otomatik pearl atarak seni kurtarır.", Category.LEGIT);
    }

    @Override
    public void onTick() {
        if (client.player == null || client.world == null) return;

        if (!client.player.isInLava()) {
            evaded = false;
            return;
        }

        if (client.player.isInLava() && !evaded) {
            int pearlSlot = findEnderPearl();
            if (pearlSlot != -1) {
                // Look up!
//? if <=1.19.2 {
/*client.player.pitch = -90f;
*///?} else {
com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, -90f);;
//?}
                
                // Switch and throw
                com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = pearlSlot;
                if (client.interactionManager != null) {
                    com.ersin.legitbridge.utils.VersionHelper.interactItem(client, Hand.MAIN_HAND);
                }
                evaded = true;
            }
        }
    }

    private int findEnderPearl() {
        for (int i = 0; i < 9; i++) {
            if (com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i).getItem() == Items.ENDER_PEARL) {
                return i;
            }
        }
        return -1;
    }
}
