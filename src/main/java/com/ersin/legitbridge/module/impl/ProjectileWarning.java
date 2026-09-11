package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
//? if <=1.19.2 {
/*import net.minecraft.text.LiteralText;
*///?}

public class ProjectileWarning extends Module {
    public ProjectileWarning() {
        super("ProjectileWarning", "Üzerinize gelen ok vb mermilerde uyarı verir.", Category.VISUALS);
    }

    @Override
    public void onTick() {
        if (client.world == null || client.player == null) return;

        for (Entity entity : client.world.getEntities()) {
            if (entity instanceof ProjectileEntity) {
                ProjectileEntity projectile = (ProjectileEntity) entity;
                // If it's heading towards us and close
                if (projectile.distanceTo(client.player) < 15.0f && projectile.getVelocity().lengthSquared() > 0.1) {
                    // Simple warning logic (could be improved with dot product to check if it's heading to us)
                    // We can just show a title or a toast
                }
            }
        }
    }
}
