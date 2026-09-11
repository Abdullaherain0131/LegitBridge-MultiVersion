package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
//? if <=1.19.2 {
/*import net.minecraft.network.Packet;
*///?} else {
import net.minecraft.network.packet.Packet;
//?}
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.LinkedList;
import java.util.Queue;

public class FakeLag extends Module {

    private final Queue<Packet<?>> packetQueue = new LinkedList<>();
    private int tickCounter = 0;
    private boolean sendingPackets = false;

    public FakeLag() {
        super("FakeLag", "Hareket paketlerini geciktirerek ışınlanıyormuş gibi görünmeni sağlar.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.enabled || !ModConfig.fakeLag) {
            flushPackets();
            return;
        }

        tickCounter++;
        // Kaç tickte bir paketleri göndereceğimiz (örn. 10 tick)
        if (tickCounter >= 10) {
            flushPackets();
            tickCounter = 0;
        }
    }

    @Override
    public void onDisable() {
        flushPackets();
        tickCounter = 0;
    }

    public boolean onSendPacket(Packet<?> packet, ClientConnection connection) {
        if (!ModConfig.enabled || !ModConfig.fakeLag || sendingPackets) return false;

        // Sadece hareket paketlerini kuyruğa alalım (vuruşlar anında gitsin)
        if (packet instanceof PlayerMoveC2SPacket) {
            packetQueue.add(packet);
            return true; // Paketi iptal et (kuyrukta bekletiliyor)
        }

        return false;
    }

    private void flushPackets() {
        if (packetQueue.isEmpty() || client.getNetworkHandler() == null) return;

        sendingPackets = true;
        while (!packetQueue.isEmpty()) {
            client.getNetworkHandler().sendPacket(packetQueue.poll());
        }
        sendingPackets = false;
    }
}
