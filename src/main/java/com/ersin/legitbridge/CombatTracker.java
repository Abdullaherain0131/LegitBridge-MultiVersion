package com.ersin.legitbridge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class CombatTracker {
    public static int kills = 0;
    public static int killstreak = 0;
    public static int highestKillstreak = 0;
    public static int totalDamageDealt = 0;

    public static long lastHitTime = 0;
    public static Entity lastTarget = null;

    public static void onHitEntity(Entity entity) {
        lastHitTime = System.currentTimeMillis();
        lastTarget = entity;
    }

    public static void addDamage(int damage) {
        totalDamageDealt += damage;
    }

    public static void addKill() {
        kills++;
        killstreak++;
        if (killstreak > highestKillstreak) {
            highestKillstreak = killstreak;
        }
    }

    public static void onPlayerDeath() {
        killstreak = 0;
    }

    public static void reset() {
        kills = 0;
        killstreak = 0;
        highestKillstreak = 0;
        totalDamageDealt = 0;
    }
}
