package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class Velocity extends Module {
    
    // Config values
    public static double horizontal = 0.0;
    public static double vertical = 0.0;

    public Velocity() {
        super("Velocity", "Aldığın knockback'i (geri sekmeyi) azaltır veya iptal eder.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        // Implementation is usually handled via Mixin into network packets (EntityVelocityUpdateS2CPacket, ExplosionS2CPacket)
        // or inside LivingEntity for generic knockback handling.
    }
}
