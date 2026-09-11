package com.ersin.legitbridge.module.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.Humanizer;

public class Dodge extends Module {
    public Dodge() {
        super("Dodge", "Dodge module", Category.COMBAT);
    }

    private long lastDodgeTime = 0;
    private long projectileDetectedTime = 0;
    private int currentReactionDelay = 200;

    private Vec3d pendingIncomingDir = null;
    private boolean pendingFront = false;

    // Defense State Machine
    private int defenseState = 0;
    private int defenseTicks = 0;
    private float originalPitch = 0;
    private long lastBlockDefenseTime = 0;

    public void setPendingProjectile(Vec3d dir, boolean front) {
        pendingIncomingDir = dir;
        pendingFront = front;
    }

    @Override
    public void onTick() {
        if (!com.ersin.legitbridge.config.ModConfig.enabled) return;

        handleDefenseStateMachine(client);

        if (pendingIncomingDir != null) {
            long now = System.currentTimeMillis();
            
            if (com.ersin.legitbridge.config.ModConfig.autoBlockDefense && pendingFront && client.player != null) {
                if (now - lastBlockDefenseTime > 2000 && defenseState == 0) {
                    startBlockDefense(client);
                    lastBlockDefenseTime = now;
                }
            }

            if (com.ersin.legitbridge.config.ModConfig.autoDodge && client.player != null) {
                if (executeDodge(client, client.player, pendingIncomingDir)) {
                    pendingIncomingDir = null; // Sadece dodge başarılıysa veya timeout olduysa sıfırla
                }
            } else {
                pendingIncomingDir = null; // Dodge kapalıysa sıfırla
            }
        }
    }

    private void startBlockDefense(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        
        boolean hasBlock = player.getMainHandStack().getItem() instanceof net.minecraft.item.BlockItem || 
                           player.getOffHandStack().getItem() instanceof net.minecraft.item.BlockItem;
        
        if (!hasBlock) {
            int blockSlot = -1;
            for (int i = 0; i < 9; i++) {
                if (com.ersin.legitbridge.utils.VersionHelper.getInventory(player).getStack(i).getItem() instanceof net.minecraft.item.BlockItem) {
                    blockSlot = i;
                    break;
                }
            }
            if (blockSlot != -1) {
                com.ersin.legitbridge.utils.VersionHelper.getInventory(player).selectedSlot = blockSlot;
            } else {
                return; 
            }
        }
        
        originalPitch = com.ersin.legitbridge.utils.VersionHelper.getPitch(player);
        defenseState = 1;
        defenseTicks = 0;
    }

    private void handleDefenseStateMachine(MinecraftClient client) {
        if (defenseState == 0) return;
        ClientPlayerEntity player = client.player;
        if (player == null) {
            defenseState = 0;
            return;
        }

        defenseTicks++;
        
        if (defenseState == 1) {
            // İleri-aşağı bakarak (yaklaşık 70 derece) bloğu yerleştir
            com.ersin.legitbridge.utils.VersionHelper.setPitch(player, 70.0f);;
            if (defenseTicks >= 2) {
                // 2 tick bekle ki sunucu rotasyonu algılasın
                ((com.ersin.legitbridge.mixin.MinecraftClientAccessor) client).invokeDoItemUse();
                defenseState = 2;
                defenseTicks = 0;
            }
        } else if (defenseState == 2) {
            // Geri dönmeden önce 1 tick daha bekle
            if (defenseTicks >= 1) {
                com.ersin.legitbridge.utils.VersionHelper.setPitch(player, originalPitch);;
                defenseState = 0;
            }
        }
    }

    public boolean executeDodge(MinecraftClient client, ClientPlayerEntity player, Vec3d incomingDir) {
        long now = System.currentTimeMillis();
        
        if (now - lastDodgeTime < 1000) {
            projectileDetectedTime = 0; 
            return true;
        }

        if (projectileDetectedTime == 0) {
            projectileDetectedTime = now;
            currentReactionDelay = Humanizer.getGaussianDelay(100, 250);
            return false; 
        }

        if (now - projectileDetectedTime < currentReactionDelay) {
            return false; // Henüz tepki süresi dolmadı
        }
        
        if (now - projectileDetectedTime > 1000) {
            // Çok eski, iptal et
            projectileDetectedTime = 0;
            return true;
        }

        Vec3d look = player.getRotationVector();
        Vec3d right = look.crossProduct(new Vec3d(0, 1, 0)).normalize();
        double rightDot = right.dotProduct(incomingDir);

        Vec3d dodgeDir;
        if (Math.abs(rightDot) < 0.2) {
            dodgeDir = (Math.random() > 0.5) ? right : right.multiply(-1);
        } else if (rightDot > 0) {
            dodgeDir = right.multiply(-1);
        } else {
            dodgeDir = right;
        }

        if (player.isOnGround()) {
            Vec3d currentVel = player.getVelocity();
            player.setVelocity(currentVel.x + dodgeDir.x * 0.45, currentVel.y + 0.1, currentVel.z + dodgeDir.z * 0.45);
            lastDodgeTime = now;
            projectileDetectedTime = 0; 
            return true;
        }
        
        return false;
    }
    
    public void resetDetection() {
        projectileDetectedTime = 0;
    }
}
