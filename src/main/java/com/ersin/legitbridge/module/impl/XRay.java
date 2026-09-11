package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class XRay extends Module {
    public XRay() {
        super("XRay", "Değerli madenleri blokların arkasından görmenizi sağlar.", Category.VISUALS);
    }

    @Override
    public void onTick() {
        // Blok render işlemlerine müdahale edilecek.
    }
}
