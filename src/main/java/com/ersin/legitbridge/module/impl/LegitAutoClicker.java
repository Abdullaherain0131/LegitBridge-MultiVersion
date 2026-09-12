package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.Humanizer;
import net.minecraft.client.option.KeyBinding;

import java.util.Random;

public class LegitAutoClicker extends Module {

    private int tickTimer = 0;
    private int nextClickDelay = 0;
    private int holdTicks = 0;
    private final Random random = new Random();
    private boolean isClicking = false;

    public LegitAutoClicker() {
        super("LegitAutoClicker", "Farenin sol tuşuna basılı tuttuğunuzda rastgele CPS ile tıklar.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.legitAutoClicker || client.player == null) return;

        // Farenin sol tuşuna basılı tutulup tutulmadığını kontrol et
//? if <=1.19.2 {
        /*if (com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client)) {
*///?} else {
        if (com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client)) {
//?}
            holdTicks++;
            if (!isClicking) {
                isClicking = true;
                nextClickDelay = generateDelay();
                tickTimer = 0;
            }

            tickTimer++;
            if (tickTimer >= nextClickDelay) {
                // Tıklama işlemini gerçekleştir
                doClick();
                
                // Bir sonraki tıklama için yeni bir rastgele gecikme ayarla
                nextClickDelay = generateDelay();
                tickTimer = 0;
            }
        } else {
            isClicking = false;
            holdTicks = 0;
        }
    }

    private int generateDelay() {
        // CPS yorulma (Fatigue) mekaniği: Ne kadar uzun basılı tutarsan CPS o kadar düşer
        int baseDelayMin = 1;
        int baseDelayMax = 3;
        
        // 50 tick (2.5 saniye) basılı tutulduğunda yorulma başlar
        if (holdTicks > 50) {
            baseDelayMin += (holdTicks / 100); // Gittikçe yavaşlat
            baseDelayMax += (holdTicks / 50);
            
            // Rastgele duraksama (Spike/Drop): İnsanın parmağı bazen 1-2 tick atlar
            if (random.nextInt(15) == 0) { // Artırılmış hata ihtimali (Anti-Cheat Bypass)
                return Humanizer.getGaussianDelay(4, 8); // Ani duraksama
            }
        }
        
        int delay = Humanizer.getGaussianDelay(baseDelayMin, Math.min(baseDelayMax, 6));
        
        // Bazen çok hızlı tıklamaları engelle (Mekanik mouse limitleri)
        if (delay == 1 && random.nextBoolean()) {
            delay = 2; 
        }
        
        return delay;
    }

    private void doClick() {
        if (client.player != null) {
            // Anti-cheat baypas için bazen bilerek tıklamayı kaçırıyoruz
            if (Humanizer.shouldDropClick()) return;
            
//? if >1.19.2 {
            client.options.attackKey.setPressed(true);
            net.minecraft.client.option.KeyBinding.onKeyPressed(client.options.attackKey.getDefaultKey());
//?} else {
            /*client.options.keyAttack.setPressed(true);
            net.minecraft.client.option.KeyBinding.onKeyPressed(client.options.keyAttack.getDefaultKey());*///?}
        }
    }
}
