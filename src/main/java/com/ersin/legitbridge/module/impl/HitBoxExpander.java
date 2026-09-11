package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class HitBoxExpander extends Module {
    public HitBoxExpander() {
        super("HitBoxExpander", "Düşmanların hitbox'larını genişleterek daha kolay vurmanı sağlar. (Mixin üzerinden çalışır)", Category.COMBAT);
    }

    @Override
    public void onTick() {
        // Asıl işlem Mixin (örn. MixinEntity) içerisinde yapılacak. 
        // ModConfig.hitBoxExpander veya isEnabled() üzerinden kontrol edilebilir.
    }
}
