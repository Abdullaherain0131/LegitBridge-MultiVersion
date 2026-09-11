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
import net.minecraft.util.math.Vec3d;

public class VoidSave extends Module {
    public VoidSave() {
        super("VoidSave", "Boşluğa düşerken en yakın adadan ayağının altına hızlıca köprü/blok çeker.", Category.LEGIT);
    }

    private boolean isSaving = false;

    @Override
    public void onTick() {
        if (!isEnabled() || !ModConfig.voidSave || client.player == null || client.world == null) {
            isSaving = false;
            return;
        }

        ItemStack mainHand = client.player.getMainHandStack();
        if (!(mainHand.getItem() instanceof BlockItem)) {
            isSaving = false;
            return;
        }

        if (client.player.isOnGround()) {
            isSaving = false;
            return;
        }

        // Yalnızca yeterince hızlı düşüyorsak tetikle
        if (client.player.getVelocity().y > -0.3) {
            return;
        }

        // Void kontrolü (Altımızda 0'a kadar katı blok var mı?)
        boolean isOverVoid = true;
        BlockPos playerPos = client.player.getBlockPos();
        
        // Çok derin kontrol performansı düşürebilir, 20 blok yeterli. Veya Y=0.
        int checkDepth = Math.max(0, playerPos.getY() - 40);
        for (int y = playerPos.getY() - 1; y >= checkDepth; y--) {
            BlockPos checkPos = new BlockPos(playerPos.getX(), y, playerPos.getZ());
            if (!client.world.getBlockState(checkPos).isAir()) {
                isOverVoid = false;
                break;
            }
        }

        if (isOverVoid) {
            isSaving = true;
        }

        if (isSaving) {
            // En yakın katı bloğu bul (Yarıçap 4, çünkü 4.5 blok yerleştirme menzilidir)
            BlockPos nearestSolid = null;
            double closestDist = 999.0;
            
            for (int x = -4; x <= 4; x++) {
                for (int y = -2; y <= 5; y++) { // Yukarımıza da bakabilir (adadan düştüğümüzde ada üstte kalır)
                    for (int z = -4; z <= 4; z++) {
                        BlockPos scanPos = playerPos.add(x, y, z);
                        if (!client.world.getBlockState(scanPos).isAir()) {
                            double dist = playerPos.getSquaredDistance(scanPos);
                            if (dist < closestDist) {
                                closestDist = dist;
                                nearestSolid = scanPos;
                            }
                        }
                    }
                }
            }

            if (nearestSolid != null) {
                // Oyuncunun 2 blok altını hedefle (Düşmeyi durdurmak için)
                BlockPos targetPos = playerPos.down(2);
                
                // En yakın katı bloktan hedefe (targetPos) doğru 1 blok adım at
                BlockPos placePos = nearestSolid;
                if (nearestSolid.getX() < targetPos.getX()) placePos = placePos.east();
                else if (nearestSolid.getX() > targetPos.getX()) placePos = placePos.west();
                else if (nearestSolid.getZ() < targetPos.getZ()) placePos = placePos.south();
                else if (nearestSolid.getZ() > targetPos.getZ()) placePos = placePos.north();
                else if (nearestSolid.getY() > targetPos.getY()) placePos = placePos.down();

                // Eğer adım attığımız yer boşsa, nearestSolid üzerine o yönde blok yerleştir
                if (client.world.getBlockState(placePos).isAir()) {
                    Direction face = Direction.UP;
                    if (placePos.getX() > nearestSolid.getX()) face = Direction.EAST;
                    else if (placePos.getX() < nearestSolid.getX()) face = Direction.WEST;
                    else if (placePos.getZ() > nearestSolid.getZ()) face = Direction.SOUTH;
                    else if (placePos.getZ() < nearestSolid.getZ()) face = Direction.NORTH;
                    else if (placePos.getY() < nearestSolid.getY()) face = Direction.DOWN;

                    BlockHitResult hitResult = new BlockHitResult(
                            new Vec3d(nearestSolid.getX() + 0.5 + face.getOffsetX() * 0.5, 
                                      nearestSolid.getY() + 0.5 + face.getOffsetY() * 0.5, 
                                      nearestSolid.getZ() + 0.5 + face.getOffsetZ() * 0.5),
                            face, nearestSolid, false);
                    
                    // Belirlenen hız (Speed) kadar paketi ardı ardına yolla (Çok hızlı örmesi için)
                    for(int i = 0; i < ModConfig.voidSaveSpeed; i++) {
//? if <=1.19.2 {
                    /*client.interactionManager.interactBlock(client.player, client.world, Hand.MAIN_HAND, hitResult);
*///?} else {
                    client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
//?}
                    }
                    
                    // Rotasyonu aniden çevirmemek için şimdilik sessiz paket kullanıyoruz (interactBlock kendisi hallediyor).
                }
            }
        }
    }
}
