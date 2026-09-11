package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class SmartESP extends Module {

    public SmartESP() {
        super("SmartESP", "Yalnızca değerli eşyaları ve tehlikeli yaratıkları (Creeper) duvar arkasından gösterir.", Category.VISUALS);
    }

    @Override
    public void onTick() {
        // Çizim mantığı MixinGameRenderer veya MixinEntityRenderer'da uygulanır.
    }
}
