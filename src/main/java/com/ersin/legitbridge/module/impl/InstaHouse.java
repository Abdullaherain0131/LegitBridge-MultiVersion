package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class InstaHouse extends Module {
    private List<BlockPos> houseBlocks = new ArrayList<>();
    private int buildIndex = 0;
    private boolean isBuilding = false;

    public InstaHouse() {
        super("InstaHouse", "Otomatik olarak etrafınıza 5x5x4 bir ev inşa eder. Açtığınızda başlar.", Category.LEGIT);
    }

    @Override
    public void onTick() {
        if (!isEnabled() || client.player == null || client.world == null) {
            isBuilding = false;
            houseBlocks.clear();
            return;
        }

//? if <=1.19.2 {
        /*if (!isBuilding && client.options.keySneak.isPressed() && com.ersin.legitbridge.utils.VersionHelper.isUseKeyPressed(client)) {
*///?} else {
        if (!isBuilding && client.options.sneakKey.isPressed() && com.ersin.legitbridge.utils.VersionHelper.isUseKeyPressed(client)) {
//?}
            ItemStack mainHand = client.player.getMainHandStack();
            if (mainHand.getItem() instanceof BlockItem) {
                startBuilding();
            }
        }

        if (isBuilding) {
            ItemStack mainHand = client.player.getMainHandStack();
            if (!(mainHand.getItem() instanceof BlockItem)) {
                // Eğer blok biterse inşayı durdur
                isBuilding = false;
                client.player.sendMessage(com.ersin.legitbridge.utils.VersionHelper.literalText("§c[LegitBridge] InstaHouse: Blok Bitti!"), true);
                return;
            }

            int blocksPerTick = 3; // Her tick 3 blok koy
            for (int i = 0; i < blocksPerTick; i++) {
                if (buildIndex >= houseBlocks.size()) {
                    isBuilding = false;
                    client.player.sendMessage(com.ersin.legitbridge.utils.VersionHelper.literalText("§a[LegitBridge] InstaHouse: Ev Tamamlandı!"), true);
                    return;
                }

                BlockPos targetPos = houseBlocks.get(buildIndex);
                if (client.world.getBlockState(targetPos).isAir()) {
                    BlockPos supportPos = targetPos.down(); // Geçici olarak altına koyuyormuş gibi yap
                    BlockHitResult hitResult = new BlockHitResult(
                            new net.minecraft.util.math.Vec3d(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5),
                            Direction.UP, supportPos, false);
//? if <=1.19.2 {
                    /*client.interactionManager.interactBlock(client.player, client.world, Hand.MAIN_HAND, hitResult);
*///?} else {
                    client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
//?}
                }
                buildIndex++;
            }
        }
    }

    private void startBuilding() {
        houseBlocks.clear();
        buildIndex = 0;
        BlockPos center = client.player.getBlockPos();

        int radius = 2; // 5x5
        int height = 3; // 4 blok yüksekliğinde duvarlar (y, y+1, y+2, y+3 tavan)

        // Duvarları hesapla
        for (int y = 0; y <= height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    // Sadece kenarlar (duvarlar) ve en üst (tavan)
                    if (x == -radius || x == radius || z == -radius || z == radius || y == height) {
                        // Kapı boşluğu bırak (Ön taraf, z = -radius, x=0, y=0 ve 1)
                        if (z == -radius && x == 0 && (y == 0 || y == 1)) {
                            continue;
                        }
                        houseBlocks.add(center.add(x, y, z));
                    }
                }
            }
        }

        isBuilding = true;
        client.player.sendMessage(com.ersin.legitbridge.utils.VersionHelper.literalText("§a[LegitBridge] InstaHouse: Ev inşa ediliyor..."), true);
    }
}
