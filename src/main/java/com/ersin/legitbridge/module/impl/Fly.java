package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class Fly extends Module {
    private boolean wasFlying = false;

    public Fly() {
        super("Fly", "Havada uçmanızı sağlar.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (client.player == null) return;
        
        com.ersin.legitbridge.utils.VersionHelper.getAbilities(client.player).flying = true;
        wasFlying = true;
    }

    @Override
    public void onDisable() {
        if (client.player != null && !client.player.isCreative() && !client.player.isSpectator()) {
            com.ersin.legitbridge.utils.VersionHelper.getAbilities(client.player).flying = false;
        }
    }
}
