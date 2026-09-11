package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class Tracers extends Module {
    public Tracers() {
        super("Tracers", "Etraftaki oyunculara çizgiler çeker (Yerlerini gösterir).", Category.VISUALS);
    }

    @Override
    public void onTick() {
        // Çizim işlemleri WorldRenderEvent / MixinWorldRenderer üzerinden yapılır.
    }
}
