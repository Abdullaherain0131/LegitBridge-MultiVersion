package com.ersin.legitbridge.module;

public enum Category {
    COMBAT("Savaş / PvP"),
    MOVEMENT("Hareket / Parkour"),
    LEGIT("Survival / Legit"),
    VISUALS("Görsel / HUD");

    public final String name;

    Category(String name) {
        this.name = name;
    }
}
