package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class StreamerMode extends Module {

    public StreamerMode() {
        super("StreamerMode", "İsimleri gizleyerek sizi yayınlarda veya videolarda korur.", Category.VISUALS);
    }

    @Override
    public void onTick() {
        // İsim değiştirme mantığı Mixin'de çalışır.
    }
}
