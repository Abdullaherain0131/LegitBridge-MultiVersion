package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class FastPlace extends Module {
    public FastPlace() {
        super("FastPlace", "Blok koyma gecikmesini (delay) sıfırlar.", Category.LEGIT);
    }

    @Override
    public void onTick() {
        // Asıl işlem MixinMinecraftClientFastPlace içinde yapılıyor (itemUseCooldown).
        // ModConfig veya isEnabled() üzerinden kontrol edilir.
    }
}
