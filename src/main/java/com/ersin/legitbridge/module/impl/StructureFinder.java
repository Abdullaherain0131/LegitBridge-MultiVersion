package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
//? if <=1.19.2 {
/*import net.minecraft.util.registry.Registry;
*///?} else {
import net.minecraft.registry.Registry;
//?}
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StructureFinder extends Module {
    public StructureFinder() {
        super("StructureFinder", "StructureFinder module", Category.VISUALS);
    }

    public final List<BlockPos> foundStructures = new CopyOnWriteArrayList<>();
    private int tickCounter = 0;
    
    private boolean isScanning = false;

    @Override
    public void onTick() {
        
        
        tickCounter++;
        if (tickCounter % 40 == 0) { // Scan every 2 seconds
            if (!isScanning) {
                isScanning = true;
                
                final BlockPos playerPos = client.player != null ? client.player.getBlockPos() : null;
                final String targetId = ModConfig.targetBlockId;
                final net.minecraft.world.World world = client.world;
                
                if (world == null || playerPos == null) {
                    isScanning = false;
                    return;
                }

                new Thread(() -> {
                    try {
                        List<BlockPos> tempFound = new ArrayList<>();
                        
                        // 1. Scan Block Entities (Fast)
                        //? if <=1.16.5 {
                        /*for (BlockEntity be : world.blockEntities) {
                            if (be instanceof ChestBlockEntity || 
                                be instanceof EnderChestBlockEntity ||
                                be instanceof MobSpawnerBlockEntity ||
                                be instanceof BarrelBlockEntity ||
                                be instanceof ShulkerBoxBlockEntity ||
                                be instanceof HopperBlockEntity ||
                                be instanceof DispenserBlockEntity ||
                                be instanceof AbstractFurnaceBlockEntity) {
                                tempFound.add(be.getPos());
                            }
                        }
                        *///?}

                        // 2. Scan for specific target block asynchronously (Hyper-Optimized Chunk Scanner)
                        if (targetId != null && !targetId.trim().isEmpty()) {
                            net.minecraft.block.Block targetBlock = com.ersin.legitbridge.utils.VersionHelper.getBlockById(targetId.trim());
                            if (targetBlock != net.minecraft.block.Blocks.AIR) { // Ensure block exists
                                int radius = 64; // Increased radius to 64! (128x128x128 = ~2M blocks) runs in 0ms!
                                
                                int minX = playerPos.getX() - radius;
                                int maxX = playerPos.getX() + radius;
                                int minZ = playerPos.getZ() - radius;
                                int maxZ = playerPos.getZ() + radius;
                                
                                int startChunkX = minX >> 4;
                                int endChunkX = maxX >> 4;
                                int startChunkZ = minZ >> 4;
                                int endChunkZ = maxZ >> 4;

                                for (int cx = startChunkX; cx <= endChunkX; cx++) {
                                    for (int cz = startChunkZ; cz <= endChunkZ; cz++) {
                                        net.minecraft.world.chunk.WorldChunk chunk = world.getChunk(cx, cz);
                                        if (chunk != null && !chunk.isEmpty()) {
                                            for (BlockEntity be : chunk.getBlockEntities().values()) {
                                                if (be instanceof ChestBlockEntity || 
                                                    be instanceof EnderChestBlockEntity ||
                                                    be instanceof MobSpawnerBlockEntity ||
                                                    be instanceof BarrelBlockEntity ||
                                                    be instanceof ShulkerBoxBlockEntity ||
                                                    be instanceof HopperBlockEntity ||
                                                    be instanceof DispenserBlockEntity ||
                                                    be instanceof AbstractFurnaceBlockEntity) {
                                                    if (!tempFound.contains(be.getPos())) {
                                                        tempFound.add(be.getPos());
                                                    }
                                                }
                                            }
                                            net.minecraft.world.chunk.ChunkSection[] sections = chunk.getSectionArray();
                                            for (int i = 0; i < sections.length; i++) {
                                                net.minecraft.world.chunk.ChunkSection section = sections[i];
                                                // Skip empty or null sections immediately (Massive O(1) Optimization)
                                                if (section != null && !section.isEmpty()) {
                                                    int sectionYOffset = i << 4; // i * 16
                                                    if (sectionYOffset < 0 || sectionYOffset >= 256) continue;
                                                    
                                                    // Fast iterate inside the 16x16x16 palette
                                                    for (int lx = 0; lx < 16; lx++) {
                                                        for (int ly = 0; ly < 16; ly++) {
                                                            for (int lz = 0; lz < 16; lz++) {
                                                                BlockState state = section.getBlockState(lx, ly, lz);
                                                                if (state.getBlock() == targetBlock) {
                                                                    int worldX = (cx << 4) + lx;
                                                                    int worldY = sectionYOffset + ly;
                                                                    int worldZ = (cz << 4) + lz;
                                                                    
                                                                    // Check if within exact radius
                                                                    if (worldX >= minX && worldX <= maxX && worldZ >= minZ && worldZ <= maxZ) {
                                                                        BlockPos foundPos = new BlockPos(worldX, worldY, worldZ);
                                                                        if (!tempFound.contains(foundPos)) {
                                                                            tempFound.add(foundPos);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        foundStructures.clear();
                        foundStructures.addAll(tempFound);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        isScanning = false;
                    }
                }).start();
            }
        }
    }
}
