package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;

public class PlayerESP extends Module {

    public PlayerESP() {
        super("PlayerESP", "Oyuncuları duvar arkasından görmeni sağlar.", Category.VISUALS);
    }

    @Override
    public void onTick() {
        // Render mixins check this module's isEnabled() state.
    }
}
