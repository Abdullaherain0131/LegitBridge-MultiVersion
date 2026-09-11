package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class Fullbright extends Module {
    private double originalGamma = -1;
    private boolean wasEnabled = false;

    public Fullbright() {
        super("Fullbright", "Makes everything bright like daytime.", Category.VISUALS);
    }

    @Override
    public void onTick() {
        if (client.options == null) return;

        boolean isEnabled = ModConfig.fullbright;

        if (isEnabled && !wasEnabled) {
            //? if >1.19.2 {
            originalGamma = client.options.getGamma().getValue();
//?} else {
            /*originalGamma = client.options.gamma;*///?}
            //? if <=1.19.2 {
/*client.options.gamma = 100.0;
*///?} else {
client.options.getGamma().setValue(100.0);
//?}
            wasEnabled = true;
        } else if (!isEnabled && wasEnabled) {
                        //? if >1.19.2 {
                        client.options.getGamma().setValue(originalGamma != -1 ? originalGamma : 1.0);
//?} else {
                        /*client.options.gamma = (originalGamma != -1 ? originalGamma : 1.0);*///?}
            wasEnabled = false;
        } else if (isEnabled && wasEnabled) {
            // Keep enforcing it just in case
//? if >1.19.2 {
            if (client.options.getGamma().getValue() < 100.0) {
//?} else {
            /*if (client.options.gamma < 100.0) {*///?}
                //? if <=1.19.2 {
/*client.options.gamma = 100.0;
*///?} else {
client.options.getGamma().setValue(100.0);
//?}
            }
        }
    }
}
