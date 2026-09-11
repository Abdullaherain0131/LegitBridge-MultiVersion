package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class FPSBooster extends Module {
    public FPSBooster() {
        super("FPSBooster", "Sistem performansını artırır (Oyun içi animasyonları kısıtlar)", Category.VISUALS);
    }

    @Override
    public void onTick() {
        if (!ModConfig.fpsBooster || client.player == null) return;
        // Mixinler tarafindan ModConfig.fpsBooster okunarak gereksiz entity animasyonlari durdurulabilir.
    }
}
