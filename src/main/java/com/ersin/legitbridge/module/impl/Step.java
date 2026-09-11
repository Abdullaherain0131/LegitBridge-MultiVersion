package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class Step extends Module {

    private float previousStepHeight = 0.6f; // Vanilla step height
    private float stepHeight = 1.5f;

    public Step() {
        super("Step", "Blokların üzerine zıplamadan çıkmanı sağlar.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (client.player != null) {
            com.ersin.legitbridge.utils.VersionHelper.setStepHeight(client.player, stepHeight);
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (client.player != null) {
            com.ersin.legitbridge.utils.VersionHelper.setStepHeight(client.player, previousStepHeight);
        }
    }
}
