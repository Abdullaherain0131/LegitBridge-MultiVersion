package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class MemoryCleaner extends Module {
    private int tickCounter = 0;

    public MemoryCleaner() {
        super("MemoryCleaner", "RAM kullanımını optimize eder (Oto Garbage Collection)", Category.VISUALS);
    }

    @Override
    public void onTick() {
        if (!ModConfig.memoryCleaner) return;

        tickCounter++;
        // Her 3 dakikada bir (3600 tick) GC cagir.
        if (tickCounter >= 3600) {
            Runtime.getRuntime().gc();
            tickCounter = 0;
        }
    }
}
