package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.Humanizer;
import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class LegitAutoCrystal extends Module {

    private int actionDelay = 0;
    
    public LegitAutoCrystal() {
        super("LegitAutoCrystal", "İnsancıl tepki hızıyla tam otomatik Kristal PvP.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.legitAutoCrystal || client.player == null || client.world == null) return;

        if (actionDelay > 0) {
            actionDelay--;
            return;
        }

        // 1. Kristal kırma kontrolü (Öncelik her zaman patlatmakta)
        if (breakCrystalIfPossible()) {
            actionDelay = Humanizer.getGaussianDelay(ModConfig.crystalBreakDelayMin, ModConfig.crystalBreakDelayMax);
            return;
        }

        // 2. Eğer kırılamadıysa ve elimizde kristal varsa koymayı dene
        if (client.player.getMainHandStack().getItem() == Items.END_CRYSTAL || client.player.getOffHandStack().getItem() == Items.END_CRYSTAL) {
            if (placeCrystalIfPossible()) {
                actionDelay = Humanizer.getGaussianDelay(ModConfig.crystalPlaceDelayMin, ModConfig.crystalPlaceDelayMax);
            }
        }
    }

    private boolean breakCrystalIfPossible() {
        for (Entity entity : client.world.getEntities()) {
            if (entity instanceof EndCrystalEntity) {
                if (client.player.distanceTo(entity) <= 5.0f) {
                    
                    // FOV Kontrolü (Sadece kameranın önündeki kristallere vur - Silent aim için zorunlu)
                    if (!isEntityInFOV(entity, 120)) continue; 

                    // Kendi canımızı tehlikeye atacak kadar yakınsak patlatma (Suicide check)
                    if (client.player.distanceTo(entity) < 3.5f) {
                        continue;
                    }

                    // Kristale vur
                    if (client.interactionManager != null) {
                        client.interactionManager.attackEntity(client.player, entity);
                        client.player.swingHand(Hand.MAIN_HAND);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean placeCrystalIfPossible() {
        PlayerEntity target = getNearestTarget();
        if (target == null) return false;

        List<BlockPos> validBlocks = getValidObsidianBlocks(target);
        if (validBlocks.isEmpty()) return false;

        // En mantıklı bloğu seç (Hedefe en yakın, bize intihar sınırından uzak)
        BlockPos bestBlock = null;
        double bestDist = 999.0;

        for (BlockPos pos : validBlocks) {
            double distToTarget = target.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
            double distToPlayer = client.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);

            // Bize 4 bloktan daha yakınsa koyma (Kendi canımızı koru)
            if (distToPlayer < 16.0) continue; 
            
            // Hedefe çok uzaksa (6 blok) koyma
            if (distToTarget > 36.0) continue;

            if (distToTarget < bestDist) {
                bestDist = distToTarget;
                bestBlock = pos;
            }
        }

        if (bestBlock != null) {
            Hand hand = (client.player.getOffHandStack().getItem() == Items.END_CRYSTAL) ? Hand.OFF_HAND : Hand.MAIN_HAND;
            
            BlockHitResult hitResult = new BlockHitResult(
                    new Vec3d(bestBlock.getX() + 0.5, bestBlock.getY() + 1.0, bestBlock.getZ() + 0.5), 
                    Direction.UP, 
                    bestBlock, 
                    false
            );

            if (client.interactionManager != null) {
                //? if <=1.19.2 {
                /*client.interactionManager.interactBlock(client.player, client.world, hand, hitResult);
*///?} else {
                client.interactionManager.interactBlock(client.player, hand, hitResult);
//?}
                client.player.swingHand(hand);
                return true;
            }
        }

        return false;
    }

    private PlayerEntity getNearestTarget() {
        PlayerEntity nearest = null;
        double nearestDist = 999.0;

        for (PlayerEntity player : client.world.getPlayers()) {
            if (player == client.player || player.isDead()) continue;

            double dist = client.player.distanceTo(player);
            // 7 bloğa kadar tarama
            if (dist < 7.0 && dist < nearestDist) {
                nearest = player;
                nearestDist = dist;
            }
        }
        return nearest;
    }

    private List<BlockPos> getValidObsidianBlocks(PlayerEntity target) {
        List<BlockPos> blocks = new ArrayList<>();
        BlockPos playerPos = client.player.getBlockPos();
        World world = client.world;

        int radius = 5;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    
                    if (world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || world.getBlockState(pos).getBlock() == Blocks.BEDROCK) {
                        // Kristal yerleştirmek için üstteki iki bloğun hava (veya değiştirilebilir) olması gerekir
                        BlockPos up1 = pos.up();
                        BlockPos up2 = pos.up(2);
                        
                        if (world.isAir(up1) && world.isAir(up2)) {
                            // Üstünde başka entity (mesela hedefin kendisi veya başka kristal) olmamalı
                            Box box = new Box(
                                    pos.getX(), pos.getY() + 1, pos.getZ(), 
                                    pos.getX() + 1, pos.getY() + 3, pos.getZ() + 1
                            );
                            
                            if (world.getOtherEntities(null, box).isEmpty()) {
                                // Sadece görüş alanındaki (FOV) bloklara koy
                                if (isBlockInFOV(pos, 120)) {
                                    blocks.add(pos);
                                }
                            }
                        }
                    }
                }
            }
        }
        return blocks;
    }

    private boolean isBlockInFOV(BlockPos pos, float fov) {
        Vec3d blockVec = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        return isVecInFOV(blockVec, fov);
    }
    
    private boolean isEntityInFOV(Entity entity, float fov) {
        return isVecInFOV(entity.getPos(), fov);
    }

    private boolean isVecInFOV(Vec3d targetVec, float fov) {
        Vec3d lookVec = client.player.getRotationVec(1.0f);
        Vec3d diffVec = targetVec.subtract(client.player.getCameraPosVec(1.0f)).normalize();
        
        double dotProduct = lookVec.dotProduct(diffVec);
        double angle = Math.toDegrees(Math.acos(dotProduct));
        
        return angle <= (fov / 2.0);
    }
}
