package com.ersin.legitbridge;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.gui.ModConfigScreen;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import com.ersin.legitbridge.utils.RenderUtils;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.hit.HitResult;

public class ModHudRenderer implements HudRenderCallback {

    private static int clickCounter = 0;
    private static long lastTime = System.currentTimeMillis();
    private static int liveCps = 0;

    public static void register() {
        HudRenderCallback.EVENT.register(new ModHudRenderer());
    }

    public static void onClientClick() {
        clickCounter++;
    }

    @Override

//? if <=1.19.2 {
/*        public void onHudRender(net.minecraft.client.util.math.MatrixStack context, float tickDelta) {*/
//?} else {
        public void onHudRender(net.minecraft.client.gui.DrawContext context, float tickDelta) {
//?}

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) {
            return;
        }

        long now = System.currentTimeMillis();
        if (now - lastTime >= 1000) {
            liveCps = clickCounter;
            clickCounter = 0;
            lastTime = now;
            SessionTracker.checkCps(liveCps);
        }

        TextRenderer font = client.textRenderer;
        int x = ModConfig.hudX;
        int y = ModConfig.hudY;
        String colorPrefix = ModConfig.getThemePrefix();
        int primaryColor = ModConfig.getThemePrimaryColor();

        // Calculate total HUD height dynamically to adjust the drag box
        int hudHeight = 15; // padding
        if (ModConfig.hudEnabled) hudHeight += 11 * 3;
        if (ModConfig.enabled && ModConfig.blockCounter) hudHeight += 11;
        if (ModConfig.enabled && ModConfig.showCps) hudHeight += 11;
        if (ModConfig.enabled) hudHeight += 11; // session time
        if (ModConfig.enabled && ModConfig.showCombatPanel) {
            hudHeight += 5 + (11 * 4); // Combat panel basics
            if (ModConfig.showBlockHitSync) hudHeight += 11;
        }

        // Highlight box when Config GUI is open for drag preview
        if (client.currentScreen instanceof ModConfigScreen) {
            RenderUtils.fill(context, x - 4, y - 4, x + 200, y + hudHeight, 0x55000000);
            RenderUtils.fill(context, x - 4, y - 4, x + 200, y - 3, primaryColor);
            RenderUtils.drawTextWithShadow(context, font, "§e[Sürükle & Bırak (Drag)]", x + 5, y - 14, 0xFFFF55);
        }

        // Top Status Badge
        if (ModConfig.hudEnabled && !ModConfig.streamerMode) {
            String statusText = colorPrefix + "[LegitBridge] " + (ModConfig.enabled ? "§aAÇIK" : "§cKAPALI") + " §7(" + ModConfig.currentProfile.toUpperCase() + ")";
            RenderUtils.drawTextWithShadow(context, font, statusText, x, y, primaryColor);
            y += 11;

            if (ModConfig.enabled) {
                String modules = "§7FastPlace: " + (ModConfig.enabled ? "§aON" : "§cOFF") +
                                 " §7| FastClick: " + (ModConfig.fastClick ? "§aON" : "§cOFF") +
                                 " §7| SafeWalk: " + (ModConfig.safeWalk ? "§aON" : "§cOFF");
                RenderUtils.drawTextWithShadow(context, font, modules, x, y, 0xE0E0E0);
                y += 11;

                if (ModConfig.triggerBot || ModConfig.autoTool) {
                    String subModules = "§7TriggerBot: " + (ModConfig.triggerBot ? "§aON" : "§cOFF") +
                                        " §7| AutoTool: " + (ModConfig.autoTool ? "§aON" : "§cOFF") +
                                        " §7| ReachGuard: " + (ModConfig.reachGuard ? "§aON" : "§cOFF");
                    RenderUtils.drawTextWithShadow(context, font, subModules, x, y, 0xE0E0E0);
                    y += 11;
                }
            }
        }

        // Live Block & CPS Counter
        if (ModConfig.enabled) {
            if (ModConfig.blockCounter) {
                int totalBlocks = 0;
                ItemStack mainHand = client.player.getMainHandStack();
                String currentBlockName = "Yok";

                if (!mainHand.isEmpty() && mainHand.getItem() instanceof BlockItem) {
                    currentBlockName = mainHand.getName().getString();
                }

                for (int i = 0; i < 36; i++) {
                    ItemStack stack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
                    if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                        totalBlocks += stack.getCount();
                    }
                }

                String blockText = "§eEldeki Blok: §f" + totalBlocks + " §7(" + currentBlockName + ")";
                RenderUtils.drawTextWithShadow(context, font, blockText, x, y, 0xFFD700);
                y += 11;
            }

            if (ModConfig.showCps) {
                String cpsText = colorPrefix + "CPS: §f" + liveCps + " §7(Max: " + SessionTracker.maxCps + ")";
                RenderUtils.drawTextWithShadow(context, font, cpsText, x, y, primaryColor);
                y += 11;
            }

            // Session Analytics Overlay
            String sessionTimeText = "§7Oturum: §f" + SessionTracker.getFormattedSessionTime() + " §7| Koyulan Blok: §e" + SessionTracker.totalBlocksPlaced;
            RenderUtils.drawTextWithShadow(context, font, sessionTimeText, x, y, 0xAAAAAA);
            y += 11;

            if (ModConfig.showCombatPanel) {
                y += 5;
                RenderUtils.drawTextWithShadow(context, font, colorPrefix + "=== Savaş Paneli ===", x, y, primaryColor);
                y += 11;
                RenderUtils.drawTextWithShadow(context, font, "§7Skor (Kill): §c" + CombatTracker.kills, x, y, 0xFF5555);
                y += 11;
                RenderUtils.drawTextWithShadow(context, font, "§7Seri (Streak): §6" + CombatTracker.killstreak + " §8(Max: " + CombatTracker.highestKillstreak + ")", x, y, 0xFFAA00);
                y += 11;
                RenderUtils.drawTextWithShadow(context, font, "§7Verilen Hasar: §e" + CombatTracker.totalDamageDealt, x, y, 0xFFFF55);
                y += 11;
                if (ModConfig.showBlockHitSync) {
                    AdvancedCombatHud.renderBlockHitSync(context, font, x, y);
                    y += 11;
                }
            }
        }
        
        // Advanced HUD (Reach, Bed Break, MLG)
        AdvancedCombatHud.render(context, tickDelta);
        
        // Durability Warning (Hardcore)
        if (com.ersin.legitbridge.module.impl.DurabilityWarning.shouldWarn) {
            String wText = com.ersin.legitbridge.module.impl.DurabilityWarning.warningText;
            int textWidth = font.getWidth(wText);
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();
            RenderUtils.drawTextWithShadow(context, font, wText, (screenWidth / 2f) - (textWidth / 2f), (screenHeight / 2f) - 45, 0xFF0000);
        }

        // Fireball Jump Guide
        if (ModConfig.enabled && ModConfig.showFireballGuide) {
            ItemStack mainHand = client.player.getMainHandStack();
            if (!mainHand.isEmpty() && mainHand.getItem() == net.minecraft.item.Items.FIRE_CHARGE) {
                float pitch = client.player.getPitch(tickDelta);
                float yaw = client.player.getYaw(tickDelta);
                Vec3d eyePos = client.player.getCameraPosVec(tickDelta);
                
                // Yatay eksende (ileri ve geri) adayı tespit etmek için raycast
                float f = yaw * 0.017453292F;
                Vec3d lookFwd = new Vec3d(-MathHelper.sin(f), 0, MathHelper.cos(f));
                Vec3d lookBwd = new Vec3d(MathHelper.sin(f), 0, -MathHelper.cos(f));

                Vec3d endFwd = eyePos.add(lookFwd.multiply(30.0));
                Vec3d endBwd = eyePos.add(lookBwd.multiply(30.0));

                BlockHitResult hitFwd = client.world.raycast(new RaycastContext(eyePos, endFwd, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, client.player));
                BlockHitResult hitBwd = client.world.raycast(new RaycastContext(eyePos, endBwd, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, client.player));

                double dist = -1;
                if (hitFwd.getType() == HitResult.Type.BLOCK) dist = eyePos.distanceTo(hitFwd.getPos());
                if (hitBwd.getType() == HitResult.Type.BLOCK) {
                    double dBwd = eyePos.distanceTo(hitBwd.getPos());
                    if (dist == -1 || dBwd < dist) dist = dBwd; // Hangisi daha yakınsa
                }

                float targetPitch = 75.0f; // Hedef bulunamazsa varsayılan max jump açısı
                String targetDesc = "Açık Alan (Max Zıplama)";

                if (dist != -1 && dist > 1.0) {
                    // Uzaklığa göre dinamik açı hesabı
                    // 25 blok için ~70 derece, 3 blok için ~85 derece
                    targetPitch = Math.max(70.0f, Math.min(85.0f, 85.0f - (float)(dist * 0.6f)));
                    targetDesc = String.format("Uzaklık: %.1fm", dist);
                }

                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();
                
                int centerX = screenWidth / 2;
                int centerY = screenHeight / 2;
                
                String guideText;
                int guideColor;
                
                if (Math.abs(pitch - targetPitch) <= 2.0f) {
                    guideText = "MÜKEMMEL AÇI (" + String.format("%.1f", pitch) + "°)";
                    guideColor = 0x55FF55; // Green
                } else if (pitch > targetPitch) {
                    guideText = "Çok Dik! Kalk ↑ (Hedef: " + String.format("%.1f", targetPitch) + "°)";
                    guideColor = 0xFF5555; // Red
                } else {
                    guideText = "Çok Yatay! Eğil ↓ (Hedef: " + String.format("%.1f", targetPitch) + "°)";
                    guideColor = 0xFFFF55; // Yellow
                }
                
                String infoText = "§7[Hedef: " + targetDesc + "]";
                
                int textWidth = font.getWidth(guideText);
                int infoWidth = font.getWidth(infoText);
                
                RenderUtils.drawTextWithShadow(context, font, guideText, centerX - textWidth / 2f, centerY + 15, guideColor);
                RenderUtils.drawTextWithShadow(context, font, infoText, centerX - infoWidth / 2f, centerY + 25, 0xFFFFFF);
            }
        }
    }
}
