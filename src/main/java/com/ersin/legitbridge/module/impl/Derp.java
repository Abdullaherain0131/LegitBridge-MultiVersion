package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
//? if <=1.19.2 {
/*import net.minecraft.network.Packet;
*///?} else {
import net.minecraft.network.packet.Packet;
//?}
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Random;

public class Derp extends Module {

    private final Random random = new Random();
    private boolean sendingFake = false;

    public Derp() {
        super("Derp", "Sunucuya rastgele bakış açıları (yaw/pitch) göndererek rakipleri şaşırtır.", Category.COMBAT);
    }

    public boolean onSendPacket(Packet<?> packet) {
        if (!ModConfig.enabled || !ModConfig.derp || sendingFake) return false;

        // Karakterin dönüş paketlerini yakala ve değiştir
        if (packet instanceof PlayerMoveC2SPacket) {
            PlayerMoveC2SPacket p = (PlayerMoveC2SPacket) packet;
            
            //? if >1.19.2 {
            boolean changesLook = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
            //?} else {
            /*boolean changesLook = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookOnly || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Both;*///?}
            //? if >1.19.2 {
            boolean changesPosition = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
            //?} else {
            /*boolean changesPosition = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionOnly || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Both;*///?}

            if (changesLook) {
                float newYaw = (float) (Math.random() * 360.0);
                float newPitch = (float) (Math.random() * 180.0 - 90.0);

                double x = changesPosition ? p.getX(client.player.getX()) : client.player.getX();
                double y = changesPosition ? p.getY(client.player.getY()) : client.player.getY();
                double z = changesPosition ? p.getZ(client.player.getZ()) : client.player.getZ();

                Packet<?> newPacket;
                
                if (changesPosition) {
                    //? if >1.19.2 {
                    newPacket = new PlayerMoveC2SPacket.Full(x, y, z, newYaw, newPitch, p.isOnGround());
                    //?} else {
                    /*newPacket = new PlayerMoveC2SPacket.Both(x, y, z, newYaw, newPitch, p.isOnGround());*///?}
                } else {
                    //? if >1.19.2 {
                    newPacket = new PlayerMoveC2SPacket.LookAndOnGround(newYaw, newPitch, p.isOnGround());
                    //?} else {
                    /*newPacket = new PlayerMoveC2SPacket.LookOnly(newYaw, newPitch, p.isOnGround());*///?}
                }

                sendingFake = true;
                client.getNetworkHandler().sendPacket(newPacket);
                sendingFake = false;
                
                return true; // Orijinal paketi iptal et
            }
        }

        return false;
    }
}
