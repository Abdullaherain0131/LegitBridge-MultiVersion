package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class AutoSprint extends Module {

    public AutoSprint() {
        super("AutoSprint", "Otomatik olarak koşmanızı sağlar.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.enabled || this.client.player == null) return;
        
        // Sadece ileri doğru hareket ederken ve açlık barı koşmaya uygunken çalışsın
        if (this.client.player.input.movementForward > 0 && !this.client.player.isSneaking() && !this.client.player.horizontalCollision) {
            //? if >1.19.2 {
            this.client.player.setSprinting(true);
            //?} else {
            /*this.client.player.setSprinting(true);*///?}
        }
    }
}
