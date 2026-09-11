package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;

public class Freecam extends Module {
    public Freecam() {
        super("Freecam", "Freecam module", Category.VISUALS);
    }

    private boolean wasEnabled = false;
    private OtherClientPlayerEntity dummyPlayer;
    private Vec3d originalPos;
    private float originalYaw;
    private float originalPitch;
    public boolean isFreecamActive = false;

    @Override
    public void onTick() {
        if (client.player == null || client.world == null) return;

        boolean isEnabled = ModConfig.enabled && ModConfig.freecam;

        if (isEnabled && !wasEnabled) {
            // Enable Freecam
            wasEnabled = true;
            isFreecamActive = true;
            originalPos = client.player.getPos();
//? if <=1.19.2 {
                        /*originalYaw = client.player.yaw;
*///?} else {
                        originalYaw = com.ersin.legitbridge.utils.VersionHelper.getYaw(client.player);
//?}
//? if <=1.19.2 {
            /*originalPitch = client.player.pitch;
*///?} else {
            originalPitch = com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player);
//?}

            dummyPlayer = new OtherClientPlayerEntity(client.world, client.player.getGameProfile());
            dummyPlayer.copyPositionAndRotation(client.player);
            com.ersin.legitbridge.utils.VersionHelper.getInventory(dummyPlayer).clone(com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player));
            
            client.world.addEntity(-1000, dummyPlayer);
            
            client.player.noClip = true;
            com.ersin.legitbridge.utils.VersionHelper.getAbilities(client.player).allowFlying = true;
            com.ersin.legitbridge.utils.VersionHelper.getAbilities(client.player).flying = true;
            
        } else if (isEnabled && wasEnabled) {
            // Enforce freecam state every tick to prevent game from resetting it
            client.player.noClip = true;
            com.ersin.legitbridge.utils.VersionHelper.getAbilities(client.player).allowFlying = true;
            com.ersin.legitbridge.utils.VersionHelper.getAbilities(client.player).flying = true;
        } else if (!isEnabled && wasEnabled) {
            // Disable Freecam
            wasEnabled = false;
            isFreecamActive = false;
            if (dummyPlayer != null) {
                //? if >1.19.2 {
                client.world.removeEntity(-1000, net.minecraft.entity.Entity.RemovalReason.DISCARDED);
                //?} else {
                /*client.world.removeEntity(-1000);*///?}
                dummyPlayer = null;
            }
            client.player.noClip = false;
            com.ersin.legitbridge.utils.VersionHelper.getAbilities(client.player).flying = false;
            com.ersin.legitbridge.utils.VersionHelper.getAbilities(client.player).allowFlying = client.player.isCreative();
            client.player.setPosition(originalPos.x, originalPos.y, originalPos.z);
//? if <=1.19.2 {
/*client.player.yaw = originalYaw;
*///?} else {
com.ersin.legitbridge.utils.VersionHelper.setYaw(client.player, originalYaw);;
//?}
//? if <=1.19.2 {
/*client.player.pitch = originalPitch;
*///?} else {
com.ersin.legitbridge.utils.VersionHelper.setPitch(client.player, originalPitch);;
//?}
            client.player.setVelocity(Vec3d.ZERO);
        }
    }
}
