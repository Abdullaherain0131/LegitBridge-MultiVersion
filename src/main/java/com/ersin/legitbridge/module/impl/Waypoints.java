package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public class Waypoints extends Module {

    public static final Map<String, Vec3d> savedWaypoints = new HashMap<>();

    public Waypoints() {
        super("Waypoints", "Kaydettiğiniz koordinatlara doğru ışın gösterir.", Category.VISUALS);
    }

    @Override
    public void onTick() {
        // Çizim mantığı MixinWorldRenderer'da.
    }
    
    public static void addWaypoint(String name, Vec3d pos) {
        savedWaypoints.put(name, pos);
    }
    
    public static void removeWaypoint(String name) {
        savedWaypoints.remove(name);
    }
}
