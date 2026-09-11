package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.util.math.Vec3d;

public class Speed extends Module {
    public Speed() {
        super("Speed", "Hızlı koşmanızı ve zıplamanızı sağlar (BunnyHop).", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (client.player == null) return;

        if (client.player.forwardSpeed != 0 || client.player.sidewaysSpeed != 0) {
            if (client.player.isOnGround()) {
                client.player.jump();
                Vec3d velocity = client.player.getVelocity();
                client.player.setVelocity(velocity.x * 1.5, velocity.y, velocity.z * 1.5);
            } else {
                Vec3d velocity = client.player.getVelocity();
                client.player.setVelocity(velocity.x * 1.02, velocity.y, velocity.z * 1.02);
            }
        }
    }
}
