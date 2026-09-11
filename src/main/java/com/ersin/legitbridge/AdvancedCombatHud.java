package com.ersin.legitbridge;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.LegitBridgeMod;
import com.ersin.legitbridge.module.impl.Dodge;
import com.ersin.legitbridge.mixin.ClientPlayerInteractionManagerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import com.ersin.legitbridge.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class AdvancedCombatHud {
    public static long lastLeftClickTime = 0;
    public static long lastRightClickTime = 0;
    
    // Performance Cache
    private static long lastTickTime = -1;
    private static final java.util.List<Entity> cachedProjectiles = new java.util.ArrayList<>();

    public static void onMouseClick(int button) {
        if (button == 0) lastLeftClickTime = System.currentTimeMillis();
        if (button == 1) lastRightClickTime = System.currentTimeMillis();
    }

    public static void render(Object context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;
        
        TextRenderer font = client.textRenderer;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        if (ModConfig.showReachMeter) {
            renderReachMeter(client, context, font, screenWidth, screenHeight, tickDelta);
        }
        
        if (ModConfig.showMlgCountdown) {
            renderMlgCountdown(client, context, font, screenWidth, screenHeight);
        }
        
        if (ModConfig.showBedBreakMeter) {
            renderBedBreakMeter(client, context, font, screenWidth, screenHeight);
        }

        if (ModConfig.comboDistance || ModConfig.reachBorderAlert) {
            renderComboDistance(client, context, font, screenWidth, screenHeight, tickDelta);
        }

        if (ModConfig.wtapIndicator) {
            renderWTapIndicator(client, context, font, screenWidth, screenHeight);
        }

        if (ModConfig.projectileWarning || ModConfig.autoDodge) {
            renderProjectileWarning(client, context, font, screenWidth, screenHeight);
        }
    }

    private static void renderReachMeter(MinecraftClient client, Object context, TextRenderer font, int width, int height, float tickDelta) {
        ClientPlayerEntity player = client.player;
        double maxReach = 6.0;
        Vec3d cameraPos = player.getCameraPosVec(tickDelta);
        Vec3d rotationVec = player.getRotationVec(tickDelta);
        Vec3d endPos = cameraPos.add(rotationVec.x * maxReach, rotationVec.y * maxReach, rotationVec.z * maxReach);
        
        Box box = player.getBoundingBox().stretch(rotationVec.multiply(maxReach)).expand(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHit = ProjectileUtil.raycast(player, cameraPos, endPos, box, entity -> !entity.isSpectator() && com.ersin.legitbridge.utils.VersionHelper.isAttackable(entity), maxReach * maxReach);
        
        if (entityHit != null && entityHit.getEntity() != null) {
            double distance = cameraPos.distanceTo(entityHit.getPos());
            String text = String.format("%.2fm", distance);
            int color;
            if (distance > 3.0) {
                color = 0xFF5555; // Red
            } else if (distance >= 2.7) {
                color = 0x55FF55; // Green (Sweet Spot)
            } else {
                color = 0x5555FF; // Blue (Close Combo)
            }
            int textWidth = font.getWidth(text);
            RenderUtils.drawTextWithShadow(context, font, text, (width / 2f) - (textWidth / 2f), (height / 2f) - 15, color);
        }
    }

    private static void renderMlgCountdown(MinecraftClient client, Object context, TextRenderer font, int width, int height) {
        ClientPlayerEntity player = client.player;
        if (player.getVelocity().y < -0.6 && !player.isOnGround() && !player.isFallFlying()) {
            // Raycast down to find ground
            Vec3d pos = player.getPos();
            Vec3d down = pos.add(0, -256, 0);
            BlockHitResult hit = client.world.raycast(new RaycastContext(pos, down, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
            
            if (hit.getType() == HitResult.Type.BLOCK) {
                double distanceY = pos.y - hit.getPos().y;
                if (distanceY > 3.0) {
                    // Approximate impact time based on current fall velocity and gravity
                    double v0 = player.getVelocity().y;
                    double gravity = -0.08;
                    // Solving for t: d = v0*t + 0.5*g*t^2 -> 0.5*g*t^2 + v0*t - d = 0 (where d is negative distanceY)
                    // quadratic formula: t = (-v0 - sqrt(v0^2 - 2*g*d)) / g
                    double discriminant = (v0 * v0) - (2 * gravity * (-distanceY));
                    if (discriminant >= 0) {
                        double timeTicks = (-v0 - Math.sqrt(discriminant)) / gravity;
                        double timeSeconds = timeTicks / 20.0;
                        
                        String text;
                        int color;
                        if (timeSeconds <= 0.3) {
                            text = "ŞİMDİ!";
                            color = 0x55FF55;
                        } else {
                            text = String.format("%.1fs", timeSeconds);
                            color = 0xFFFF55;
                        }
                        
                        int textWidth = font.getWidth(text);
                        RenderUtils.drawTextWithShadow(context, font, "MLG: " + text, (width / 2f) - (textWidth / 2f), (height / 2f) + 30, color);
                    }
                }
            }
        }
    }

    private static void renderBedBreakMeter(MinecraftClient client, Object context, TextRenderer font, int width, int height) {
        if (client.interactionManager instanceof ClientPlayerInteractionManagerAccessor) {
            ClientPlayerInteractionManagerAccessor accessor = (ClientPlayerInteractionManagerAccessor) client.interactionManager;
            float progress = accessor.getCurrentBreakingProgress();
            BlockPos pos = accessor.getCurrentBreakingPos();
            
            if (progress > 0.0f && pos != null) {
                String text = String.format("Kırılıyor: %d%%", (int)(progress * 100));
                int textWidth = font.getWidth(text);
                RenderUtils.drawTextWithShadow(context, font, text, (width / 2f) - (textWidth / 2f), (height / 2f) + 45, 0x00E5FF);
                
                // Draw a simple progress bar
                int barWidth = 100;
                int barX = (width / 2) - (barWidth / 2);
                int barY = (height / 2) + 55;
                RenderUtils.fill(context, barX, barY, barX + barWidth, barY + 4, 0x55000000);
                RenderUtils.fill(context, barX, barY, barX + (int)(barWidth * progress), barY + 4, 0xFF00E5FF);
            }
        }
    }
    
    public static void renderBlockHitSync(Object context, TextRenderer font, int x, int y) {
        long now = System.currentTimeMillis();
        // If both were clicked within the last 500ms
        if (now - lastLeftClickTime < 500 && now - lastRightClickTime < 500) {
            long diff = Math.abs(lastLeftClickTime - lastRightClickTime);
            String text;
            int color;
            if (diff < 50) {
                text = "Perfect Sync!";
                color = 0x55FF55;
            } else if (diff < 150) {
                text = "Good Sync";
                color = 0xFFFF55;
            } else {
                text = "Poor Sync";
                color = 0xFF5555;
            }
            RenderUtils.drawTextWithShadow(context, font, "Block-Hit: " + text + " (" + diff + "ms)", x, y, color);
        } else {
            RenderUtils.drawTextWithShadow(context, font, "Block-Hit: -", x, y, 0xAAAAAA);
        }
    }

    private static void renderComboDistance(MinecraftClient client, Object context, TextRenderer font, int width, int height, float tickDelta) {
        ClientPlayerEntity player = client.player;
        double maxReach = 6.0;
        Vec3d cameraPos = player.getCameraPosVec(tickDelta);
        Vec3d rotationVec = player.getRotationVec(tickDelta);
        Vec3d endPos = cameraPos.add(rotationVec.x * maxReach, rotationVec.y * maxReach, rotationVec.z * maxReach);
        
        Box box = player.getBoundingBox().stretch(rotationVec.multiply(maxReach)).expand(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHit = ProjectileUtil.raycast(player, cameraPos, endPos, box, entity -> !entity.isSpectator() && com.ersin.legitbridge.utils.VersionHelper.isAttackable(entity), maxReach * maxReach);
        
        if (entityHit != null && entityHit.getEntity() != null) {
            double distance = cameraPos.distanceTo(entityHit.getPos());
            
            // Reach Border Alert (Crosshair color/indicator)
            if (ModConfig.reachBorderAlert && distance <= 3.0) {
                // Draw a subtle border around the crosshair
                RenderUtils.fill(context, width/2 - 6, height/2 - 6, width/2 + 6, height/2 - 5, 0x8800FF00);
                RenderUtils.fill(context, width/2 - 6, height/2 + 5, width/2 + 6, height/2 + 6, 0x8800FF00);
                RenderUtils.fill(context, width/2 - 6, height/2 - 5, width/2 - 5, height/2 + 5, 0x8800FF00);
                RenderUtils.fill(context, width/2 + 5, height/2 - 5, width/2 + 6, height/2 + 5, 0x8800FF00);
            }
            
            // Combo Distance text
            if (ModConfig.comboDistance) {
                String distText = String.format("%.2f m", distance);
                RenderUtils.drawTextWithShadow(context, font, distText, (width / 2f) + 10, (height / 2f) + 10, distance <= 3.0 ? 0x55FF55 : 0xFF5555);
            }
        }
    }

    private static void renderWTapIndicator(MinecraftClient client, Object context, TextRenderer font, int width, int height) {
        long now = System.currentTimeMillis();
        long hitDiff = now - CombatTracker.lastHitTime;
        
        // W-Tap is most effective around 0.4s to 0.5s after a hit, before hitting again.
        // Let's flash a visual cue for W-Tap when 350-500ms have passed since the last hit.
        if (hitDiff >= 350 && hitDiff <= 550) {
            String wtap = "W-TAP";
            int tw = font.getWidth(wtap);
            RenderUtils.drawTextWithShadow(context, font, wtap, (width / 2f) - (tw / 2f), (height / 2f) + 20, 0xFFFF55);
        }
    }

    private static void renderProjectileWarning(MinecraftClient client, Object context, TextRenderer font, int width, int height) {
        ClientPlayerEntity player = client.player;
        
        long currentTick = client.world.getTime();
        if (currentTick != lastTickTime) {
            lastTickTime = currentTick;
            cachedProjectiles.clear();
            cachedProjectiles.addAll(client.world.getOtherEntities(player, player.getBoundingBox().expand(25.0), 
                entity -> entity instanceof net.minecraft.entity.projectile.ProjectileEntity || entity instanceof net.minecraft.entity.projectile.ExplosiveProjectileEntity));
        }
        
        java.util.List<Entity> projectiles = cachedProjectiles;
        
        boolean incomingLeft = false;
        boolean incomingRight = false;
        boolean incomingFront = false;

        for (Entity p : projectiles) {
            Vec3d pPos = p.getPos();
            Vec3d pVel = p.getVelocity();
            
            if (pVel.lengthSquared() > 0.05) {
                // Raycast projectile path
                Vec3d end = pPos.add(pVel.multiply(20.0)); // Look 1 second ahead
                Box playerBox = player.getBoundingBox().expand(0.5);
                java.util.Optional<Vec3d> intersect = playerBox.raycast(pPos, end);
                
                if (intersect.isPresent()) {
                    // It's coming right at us! Determine direction relative to player look
                    Vec3d toProj = pPos.subtract(player.getPos()).normalize();
                    Vec3d look = player.getRotationVector();
                    
                    // Simple dot product for front/back, cross product for left/right
                    double dot = look.dotProduct(toProj);
                    Vec3d cross = look.crossProduct(new Vec3d(0, 1, 0)); // Right vector
                    double rightDot = cross.dotProduct(toProj);
                    
                    if (dot > 0.5) incomingFront = true;
                    else if (rightDot > 0) incomingRight = true;
                    else incomingLeft = true;

                    // Signal Dodge Module
                    if (ModConfig.autoDodge || ModConfig.autoBlockDefense) {
                        Dodge dodge = (Dodge) LegitBridgeMod.moduleManager.getModuleByName("Dodge");
                        if (dodge != null) dodge.setPendingProjectile(toProj, incomingFront);
                    }
                }
            }
        }
        
        if (!incomingFront && !incomingLeft && !incomingRight) {
            Dodge dodge = (Dodge) LegitBridgeMod.moduleManager.getModuleByName("Dodge");
            if (dodge != null) dodge.resetDetection();
        }
        
        if (ModConfig.projectileWarning) {
            if (incomingFront) RenderUtils.drawTextWithShadow(context, font, "↑ DİKKAT (ÖN) ↑", (width / 2f) - 40, (height / 2f) - 30, 0xFF5555);
            if (incomingLeft) RenderUtils.drawTextWithShadow(context, font, "← Mermİ", (width / 2f) - 60, (height / 2f), 0xFF5555);
            if (incomingRight) RenderUtils.drawTextWithShadow(context, font, "Mermİ →", (width / 2f) + 30, (height / 2f), 0xFF5555);
        }
    }
}
