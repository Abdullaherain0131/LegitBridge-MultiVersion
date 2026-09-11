package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class NoRender extends Module {
    public NoRender() {
        super("NoRender", "Gereksiz görselleri kapatarak devasa FPS artışı sağlar", Category.VISUALS);
    }

    @Override
    public void onTick() {
        // Asıl NoRender mantığı Mixin'ler aracılığıyla çalışır.
        // Bu sınıf sadece GUI'de görünmesi ve toggle durumunu tutması içindir.
    }
}
