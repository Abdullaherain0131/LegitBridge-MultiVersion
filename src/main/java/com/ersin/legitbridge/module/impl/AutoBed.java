package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.block.BedBlock;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AutoBed extends Module {

    private int tickDelay = 0;

    public AutoBed() {
        super("AutoBed", "Görünür menzildeki düşman yataklarını otomatik olarak kırar.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.enabled || !ModConfig.autoBed || client.player == null || client.world == null) return;

        if (tickDelay > 0) {
            tickDelay--;
            return;
        }

        int radius = 4;
        BlockPos playerPos = client.player.getBlockPos();

        // 4 blokluk yarıçapta yatakları tara
        List<BlockPos> beds = BlockPos.stream(
                playerPos.add(-radius, -radius, -radius),
                playerPos.add(radius, radius, radius)
        ).filter(pos -> client.world.getBlockState(pos).getBlock() instanceof BedBlock)
         .map(BlockPos::toImmutable)
         .sorted(Comparator.comparingDouble(pos -> client.player.squaredDistanceTo(Vec3d.ofCenter(pos))))
         .collect(Collectors.toList());

        for (BlockPos targetBed : beds) {
            if (canSeeBlock(targetBed)) {
                // Görünür bir yatak bulduk, kırmaya başla
                client.interactionManager.updateBlockBreakingProgress(targetBed, Direction.UP);
                client.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
                
                // Kırma işlemi için insani bir bekleme (opsiyonel)
                tickDelay = 2; 
                break; // Sadece bir yatağa odaklan
            }
        }
    }

    private boolean canSeeBlock(BlockPos pos) {
        Vec3d eyePos = client.player.getCameraPosVec(1.0F);
        Vec3d blockCenter = Vec3d.ofCenter(pos);
        
        BlockHitResult result = client.world.raycast(new RaycastContext(
                eyePos, blockCenter,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                client.player
        ));

        // Raycast ya hedefe ulaşmalı ya da MISS olmalı (arada engel yok)
        return result.getType() == HitResult.Type.MISS || result.getBlockPos().equals(pos);
    }
}
