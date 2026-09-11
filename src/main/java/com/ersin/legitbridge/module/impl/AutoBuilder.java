package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoBuilder extends Module {
    private int buildDelay = 0;

    public AutoBuilder() {
        super("AutoBuilder", "Hızlı yapı ve duvar inşası sağlar. (Eğilip sağ tıkladığınızda tetiklenir)", Category.LEGIT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.autoBuilder || client.player == null || client.world == null) return;

        ItemStack mainHand = client.player.getMainHandStack();
        if (!(mainHand.getItem() instanceof BlockItem)) return;

//? if <=1.19.2 {
        /*if (com.ersin.legitbridge.utils.VersionHelper.isUseKeyPressed(client) && client.player.isSneaking()) {
*///?} else {
        if (com.ersin.legitbridge.utils.VersionHelper.isUseKeyPressed(client) && client.player.isSneaking()) {
//?}
            buildDelay++;
            if (buildDelay >= 2) { 
                HitResult hit = client.crosshairTarget;
                if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHit = (BlockHitResult) hit;
                    Direction side = blockHit.getSide();
                    BlockPos targetPos = blockHit.getBlockPos().offset(side);
                    
                    // Sağa, sola veya yukarı ekstra blok koy (Duvar örme efekti)
                    if (side.getAxis().isHorizontal()) {
                        BlockPos upPos = targetPos.up();
                        if (client.world.getBlockState(upPos).isAir()) {
                            BlockHitResult upHit = new BlockHitResult(blockHit.getPos().add(0, 1, 0), side, blockHit.getBlockPos().up(), blockHit.isInsideBlock());
//? if <=1.19.2 {
                            /*client.interactionManager.interactBlock(client.player, client.world, Hand.MAIN_HAND, upHit);
*///?} else {
                            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, upHit);
//?}
                        }
                    }
                }
                buildDelay = 0;
            }
        } else {
            buildDelay = 0;
        }
    }
}
