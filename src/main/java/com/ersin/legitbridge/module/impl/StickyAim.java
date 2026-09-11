package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class StickyAim extends Module {

    // Ne kadar sürtünme uygulanacağı (0.0 hiç yavaşlama, 1.0 tam durma)
    // Legit olması için 0.4 ile 0.7 arası iyi bir değerdir
    private final double friction = 0.6;
    
    // Yavaşlamanın etki edeceği FOV (Görüş açısı toleransı)
    private final double toleranceFOV = 15.0;

    public StickyAim() {
        super("StickyAim", "Düşmanın üzerindeyken veya çok yakınındayken fare hızını yavaşlatır (Friction).", Category.COMBAT);
    }

    @Override
    public void onTick() {
        // Asıl olay Mixin'de (MouseMixin veya ClientPlayerEntityMixin) gerçekleşmeli
        // Çünkü bu modül sadece ayarları tutar, yaw/pitch hızı frame-by-frame Mixin'den kontrol edilmelidir.
        // Fabric'te Mouse.class içindeki updateMouse() veya cursorDelta'ya müdahale edilmelidir.
    }
}
