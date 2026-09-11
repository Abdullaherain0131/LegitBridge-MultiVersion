package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class BlockOverlay extends Module {

    public BlockOverlay() {
        super("BlockOverlay", "Baktığınız bloğun seçim çerçevesini RGB renkli ve kalın yapar.", Category.VISUALS);
    }

    @Override
    public void onTick() {
        // Çizim (Rendering) işlemi MixinWorldRenderer içerisinde yapılır
    }
}
