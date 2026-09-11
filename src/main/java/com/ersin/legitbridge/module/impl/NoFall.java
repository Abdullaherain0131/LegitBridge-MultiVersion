package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", "Düşme hasarını engeller.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (client.player == null) return;
        
        if (client.player.fallDistance > 2.5f) {
            //? if >1.19.2 {
            client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
            //?} else {
            /*client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket(true));*///?}
        }
    }
}
