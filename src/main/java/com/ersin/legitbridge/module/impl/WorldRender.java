package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
//? if <=1.19.2 {
/*import net.minecraft.util.math.Matrix4f;
*///?} else {
import org.joml.Matrix4f;
//?}
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.LegitBridgeMod;
import com.ersin.legitbridge.module.impl.StructureFinder;
import com.ersin.legitbridge.module.impl.Waypoints;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import java.util.Map;

public class WorldRender extends Module {
    private final java.util.List<Entity> cachedExplosives = new java.util.ArrayList<>();
    private final java.util.List<Entity> cachedCombatEntities = new java.util.ArrayList<>();
    private final java.util.List<Entity> cachedSmartESPEntities = new java.util.ArrayList<>();
    private int cacheTickCounter = 0;

    public WorldRender() {
        super("WorldRender", "WorldRender module", Category.VISUALS);
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null || !ModConfig.enabled) return;

        // Cache entities at 20 TPS instead of every render frame (100+ FPS) to massively boost performance
        cacheTickCounter++;
        
        cachedExplosives.clear();
        cachedCombatEntities.clear();

        if (ModConfig.showKbTrajectory) {
            cachedExplosives.addAll(client.world.getOtherEntities(client.player, client.player.getBoundingBox().expand(16.0), entity -> entity instanceof TntEntity || entity instanceof FireballEntity));
        }

        if (ModConfig.showHitboxCenter || ModConfig.showKnockbackVector) {
            cachedCombatEntities.addAll(client.world.getOtherEntities(client.player, client.player.getBoundingBox().expand(16.0), entity -> entity instanceof PlayerEntity || entity instanceof net.minecraft.entity.mob.MobEntity));
        }
        
        // Smart ESP Cache (Her 10 tick'te bir yani saniyede 2 kez güncellensin, performansı katlar)
        if (ModConfig.smartESP && cacheTickCounter >= 10) {
            cacheTickCounter = 0;
            cachedSmartESPEntities.clear();
            for (Entity entity : client.world.getEntities()) {
                if (entity.distanceTo(client.player) <= 64.0) {
                    if (entity instanceof CreeperEntity || entity instanceof ItemEntity) {
                        cachedSmartESPEntities.add(entity);
                    }
                }
            }
        }
    }


    public void register() {
        WorldRenderEvents.LAST.register(this::render);
    }

    private void render(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null || !ModConfig.enabled) return;

        MatrixStack matrixStack = context.matrixStack();
        Vec3d cameraPos = context.camera().getPos();

        matrixStack.push();
        matrixStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        //? if <=1.19.2 {
/*RenderSystem.disableTexture();
*///?}
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        if (ModConfig.showPearlTrajectory && client.player.getMainHandStack().getItem() == Items.ENDER_PEARL) {
            renderPearlTrajectory(client, matrixStack);
        }

        if (ModConfig.showKbTrajectory) {
            renderKbTrajectory(client, matrixStack);
        }

        if (ModConfig.showHitboxCenter || ModConfig.showKnockbackVector) {
            renderCombatVisualizers(client, matrixStack);
        }
        
        if (ModConfig.fireballPredictor && client.player.getMainHandStack().getItem() == Items.FIRE_CHARGE) {
            renderFireballPredictor(client, matrixStack);
        }

        if (ModConfig.showStructureFinder) {
            renderStructures(client, matrixStack);
        }

        if (ModConfig.storageESP) {
            renderStorageESP(client, matrixStack);
        }
        
        if (ModConfig.smartESP) {
            renderSmartESP(client, matrixStack);
        }
        
        if (ModConfig.waypoints) {
            renderWaypoints(client, matrixStack);
        }
        
        if (ModConfig.blockOverlay && client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            renderBlockOverlay(client, matrixStack, (BlockHitResult) client.crosshairTarget);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        //? if <=1.19.2 {
/*RenderSystem.enableTexture();
*///?}

        matrixStack.pop();
    }

    private Vec3d lastCachePos = Vec3d.ZERO;
    private float lastCachePitch = 0;
    private float lastCacheYaw = 0;
    private final java.util.List<Vec3d> cachedPath = new java.util.ArrayList<>();
    private BlockPos cachedHitPos = null;

    private void renderPearlTrajectory(MinecraftClient client, MatrixStack matrixStack) {
        PlayerEntity player = client.player;
        net.minecraft.item.Item heldItem = player.getMainHandStack().getItem();
        
        // Fırlatılabilir kontrolü
        boolean isBow = heldItem instanceof net.minecraft.item.BowItem;
        boolean isThrowable = heldItem == Items.ENDER_PEARL || heldItem == Items.SNOWBALL || heldItem == Items.EGG || heldItem instanceof net.minecraft.item.SplashPotionItem;
        
        if (!isBow && !isThrowable) return;

        //? if <=1.19.2 {
/*float pitch = player.pitch;
*///?} else {
float pitch = com.ersin.legitbridge.utils.VersionHelper.getPitch(player);
//?}
        //? if <=1.19.2 {
/*float yaw = player.yaw;
*///?} else {
float yaw = com.ersin.legitbridge.utils.VersionHelper.getYaw(player);
//?}
        Vec3d playerPos = player.getPos();
        
        // Cache kontrolü: Oyuncu hareket etmediyse veya açısı değişmediyse raycast yapma
        if (playerPos.squaredDistanceTo(lastCachePos) > 0.01 || Math.abs(pitch - lastCachePitch) > 0.1 || Math.abs(yaw - lastCacheYaw) > 0.1) {
            lastCachePos = playerPos;
            lastCachePitch = pitch;
            lastCacheYaw = yaw;
            cachedPath.clear();
            cachedHitPos = null;
            
            double x = player.getX();
            double y = player.getY() + player.getStandingEyeHeight() - 0.1;
            double z = player.getZ();
            
            double velX = (double)(-MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F));
            double velZ = (double)(MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F));
            double velY = (double)(-MathHelper.sin((pitch - (isBow ? 0 : 20)) * 0.017453292F)); 
            
            // Hız çarpanları
            double multiplier = isBow ? 3.0 : 1.5;
            // Yerçekimi
            double gravity = isBow ? 0.05 : 0.03;

            double length = Math.sqrt(velX * velX + velY * velY + velZ * velZ);
            velX /= length; velY /= length; velZ /= length;
            velX *= multiplier; velY *= multiplier; velZ *= multiplier;

            Vec3d pos = new Vec3d(x, y, z);
            cachedPath.add(pos);
            
            HitResult hit = null;
            for (int i = 0; i < 200; i++) {
                Vec3d nextPos = pos.add(velX, velY, velZ);
                hit = client.world.raycast(new RaycastContext(pos, nextPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
                
                if (hit != null && hit.getType() != HitResult.Type.MISS) {
                    cachedPath.add(hit.getPos());
                    if (hit.getType() == HitResult.Type.BLOCK) {
                        cachedHitPos = ((BlockHitResult)hit).getBlockPos();
                    }
                    break;
                }
                cachedPath.add(nextPos);
                pos = nextPos;
                velX *= 0.99; velY *= 0.99; velZ *= 0.99;
                velY -= gravity; // Gravity
            }
        }

        // Sadece önbelleğe alınmış noktaları çiz
        if (cachedPath.isEmpty()) return;
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(
//? if <=1.19.2 {
/*3*/
//?} else {
net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINE_STRIP
//?}
, net.minecraft.client.render.VertexFormats.POSITION_COLOR); // GL_LINE_STRIP
        
        for (Vec3d p : cachedPath) {
            buffer.vertex(
//? if <=1.19.2 {
/*matrixStack.peek().getModel()*/
//?} else {
matrixStack.peek().getPositionMatrix()
//?}
, (float)p.x, (float)p.y, (float)p.z).color(0, 255, 0, 255).next();
        }
        tessellator.draw();
        
        if (cachedHitPos != null) {
            drawBox(matrixStack, new Box(cachedHitPos).expand(0.01), 0, 255, 0, 100);
        }
    }

    private void renderKbTrajectory(MinecraftClient client, MatrixStack matrixStack) {
        PlayerEntity player = client.player;
        
        for (Entity entity : cachedExplosives) {
            //? if <=1.19.2 {
/*if (entity.removed) continue;
*///?} else {
if (entity.isRemoved()) continue;
//?}
            
            double distance = player.distanceTo(entity);
            if (distance < 16.0) {
                Vec3d diff = player.getPos().subtract(entity.getPos()).normalize();
                double power = (16.0 - distance) / 2.0; // Better estimation for TNT kb
                Vec3d kbVector = diff.multiply(power).add(0, power * 0.1, 0); // Upwards KB
                
                Vec3d start = player.getPos().add(0, player.getHeight() / 2, 0);
                Vec3d end = start.add(kbVector);
                
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                buffer.begin(
//? if <=1.19.2 {
/*1*/
//?} else {
net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINES
//?}
, net.minecraft.client.render.VertexFormats.POSITION_COLOR); // GL_LINES
                
                buffer.vertex(
//? if <=1.19.2 {
/*matrixStack.peek().getModel()*/
//?} else {
matrixStack.peek().getPositionMatrix()
//?}
, (float)start.x, (float)start.y, (float)start.z).color(255, 0, 0, 255).next();
                buffer.vertex(
//? if <=1.19.2 {
/*matrixStack.peek().getModel()*/
//?} else {
matrixStack.peek().getPositionMatrix()
//?}
, (float)end.x, (float)end.y, (float)end.z).color(255, 0, 0, 255).next();
                
                tessellator.draw();
            }
        }
    }

    private void renderCombatVisualizers(MinecraftClient client, MatrixStack matrixStack) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        for (Entity entity : cachedCombatEntities) {
            //? if <=1.19.2 {
/*if (entity.removed) continue;
*///?} else {
if (entity.isRemoved()) continue;
//?}
            
            if (ModConfig.showHitboxCenter) {
                Vec3d center = entity.getBoundingBox().getCenter();
                drawBox(matrixStack, new Box(center.x - 0.05, center.y - 0.05, center.z - 0.05, center.x + 0.05, center.y + 0.05, center.z + 0.05), 0, 255, 255, 150);
            }
            
            if (ModConfig.showKnockbackVector) {
                Vec3d center = entity.getBoundingBox().getCenter();
                Vec3d velocity = entity.getVelocity();
                if (velocity.lengthSquared() > 0.01) {
                    Vec3d end = center.add(velocity.multiply(10)); // 0.5s into future
                    
                    //? if <=1.19.2 {
/*RenderSystem.disableTexture();
*///?}
                    buffer.begin(
//? if <=1.19.2 {
/*1*/
//?} else {
net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINES
//?}
, net.minecraft.client.render.VertexFormats.POSITION_COLOR); // GL_LINES
                    buffer.vertex(
//? if <=1.19.2 {
/*matrixStack.peek().getModel()*/
//?} else {
matrixStack.peek().getPositionMatrix()
//?}
, (float)center.x, (float)center.y, (float)center.z).color(255, 100, 0, 255).next();
                    buffer.vertex(
//? if <=1.19.2 {
/*matrixStack.peek().getModel()*/
//?} else {
matrixStack.peek().getPositionMatrix()
//?}
, (float)end.x, (float)end.y, (float)end.z).color(255, 100, 0, 255).next();
                    tessellator.draw();
                }
            }
        }
    }

    private void renderFireballPredictor(MinecraftClient client, MatrixStack matrixStack) {
        PlayerEntity player = client.player;
        float idealPitch = 75.0f; // 75 degrees down is generally optimal for max height+distance jump
        //? if <=1.19.2 {
/*float yaw = player.yaw;
*///?} else {
float yaw = com.ersin.legitbridge.utils.VersionHelper.getYaw(player);
//?}
        
        double velX = (double)(-MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(idealPitch * 0.017453292F));
        double velZ = (double)(MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(idealPitch * 0.017453292F));
        double velY = (double)(-MathHelper.sin(idealPitch * 0.017453292F));
        
        Vec3d start = player.getCameraPosVec(1.0f);
        Vec3d end = start.add(velX * 4, velY * 4, velZ * 4);
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        //? if <=1.19.2 {
/*RenderSystem.disableTexture();
*///?}
        buffer.begin(
//? if <=1.19.2 {
/*1*/
//?} else {
net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINES
//?}
, net.minecraft.client.render.VertexFormats.POSITION_COLOR);
        buffer.vertex(
//? if <=1.19.2 {
/*matrixStack.peek().getModel()*/
//?} else {
matrixStack.peek().getPositionMatrix()
//?}
, (float)start.x, (float)start.y, (float)start.z).color(0, 255, 0, 255).next();
        buffer.vertex(
//? if <=1.19.2 {
/*matrixStack.peek().getModel()*/
//?} else {
matrixStack.peek().getPositionMatrix()
//?}
, (float)end.x, (float)end.y, (float)end.z).color(0, 255, 0, 255).next();
        tessellator.draw();
    }

    private void renderStructures(MinecraftClient client, MatrixStack matrixStack) {
        StructureFinder finder = (StructureFinder) LegitBridgeMod.moduleManager.getModuleByName("StructureFinder");
        if (finder == null || finder.foundStructures.isEmpty()) return;

        Vec3d start = client.player.getCameraPosVec(1.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        //? if <=1.19.2 {
/*RenderSystem.disableTexture();
*///?}
        buffer.begin(
//? if <=1.19.2 {
/*1*/
//?} else {
net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINES
//?}
, net.minecraft.client.render.VertexFormats.POSITION_COLOR);

        for (BlockPos pos : finder.foundStructures) {
            Vec3d end = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            buffer.vertex(
//? if <=1.19.2 {
/*matrixStack.peek().getModel()*/
//?} else {
matrixStack.peek().getPositionMatrix()
//?}
, (float)start.x, (float)start.y, (float)start.z).color(255, 0, 255, 255).next();
            buffer.vertex(
//? if <=1.19.2 {
/*matrixStack.peek().getModel()*/
//?} else {
matrixStack.peek().getPositionMatrix()
//?}
, (float)end.x, (float)end.y, (float)end.z).color(255, 0, 255, 255).next();
        }
        
        tessellator.draw();
        
        for (BlockPos pos : finder.foundStructures) {
            drawBox(matrixStack, new Box(pos).expand(0.05), 255, 0, 255, 100);
        }
    }

    private void renderStorageESP(MinecraftClient client, MatrixStack matrixStack) {
        Vec3d start = client.player.getCameraPosVec(1.0f);
        
//? if <=1.19.2 {
        /*for (net.minecraft.block.entity.BlockEntity be : client.world.blockEntities) {
            BlockPos pos = be.getPos();
            if (pos.isWithinDistance(start, 32.0)) {
                if (be instanceof net.minecraft.block.entity.ChestBlockEntity || be instanceof net.minecraft.block.entity.BarrelBlockEntity || be instanceof net.minecraft.block.entity.ShulkerBoxBlockEntity) {
                    drawBox(matrixStack, new Box(pos).expand(0.01), 255, 165, 0, 100); // Orange for chests
                } else if (be instanceof net.minecraft.block.entity.MobSpawnerBlockEntity) {
                    drawBox(matrixStack, new Box(pos).expand(0.01), 255, 0, 0, 150); // Red for spawners
                }
            }
        }
*///?}
    }
    
    private void renderSmartESP(MinecraftClient client, MatrixStack matrixStack) {
        Vec3d start = client.player.getCameraPosVec(1.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        //? if <=1.19.2 {
/*RenderSystem.disableTexture();
*///?}
        for (Entity entity : cachedSmartESPEntities) {
            //? if <=1.19.2 {
/*if (entity.removed) continue;
*///?} else {
if (entity.isRemoved()) continue;
//?}
            
            if (entity instanceof CreeperEntity) {
                // Kırmızı Outline for Creeper
                drawBox(matrixStack, entity.getBoundingBox().expand(0.05), 255, 0, 0, 100);
            } else if (entity instanceof ItemEntity) {
                ItemEntity itemEntity = (ItemEntity) entity;
                Item item = itemEntity.getStack().getItem();
                if (item == Items.DIAMOND || item == Items.NETHERITE_INGOT || item == Items.NETHERITE_SCRAP || item == Items.ANCIENT_DEBRIS || item == Items.ENCHANTED_GOLDEN_APPLE) {
                    // Mavi Outline for Rare Items
                    drawBox(matrixStack, entity.getBoundingBox().expand(0.05), 0, 200, 255, 100);
                }
            }
        }
    }
    
    private void renderWaypoints(MinecraftClient client, MatrixStack matrixStack) {
        Vec3d start = client.player.getCameraPosVec(1.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        //? if <=1.19.2 {
/*RenderSystem.disableTexture();
*///?}
        buffer.begin(
//? if <=1.19.2 {
/*1*/
//?} else {
net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINES
//?}
, net.minecraft.client.render.VertexFormats.POSITION_COLOR); // GL_LINES
        
        for (Map.Entry<String, Vec3d> entry : Waypoints.savedWaypoints.entrySet()) {
            Vec3d pos = entry.getValue();
            Vec3d end = new Vec3d(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);
            buffer.vertex(
//? if <=1.19.2 {
/*matrixStack.peek().getModel()*/
//?} else {
matrixStack.peek().getPositionMatrix()
//?}
, (float)start.x, (float)start.y, (float)start.z).color(0, 255, 255, 255).next();
            buffer.vertex(
//? if <=1.19.2 {
/*matrixStack.peek().getModel()*/
//?} else {
matrixStack.peek().getPositionMatrix()
//?}
, (float)end.x, (float)end.y, (float)end.z).color(0, 255, 255, 255).next();
        }
        tessellator.draw();
        
        for (Map.Entry<String, Vec3d> entry : Waypoints.savedWaypoints.entrySet()) {
            Vec3d pos = entry.getValue();
            drawBox(matrixStack, new Box(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 2, pos.z + 1), 0, 255, 255, 100);
        }
    }

    private void renderBlockOverlay(MinecraftClient client, MatrixStack matrixStack, BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        BlockState state = client.world.getBlockState(pos);
        if (state.isAir()) return;
        
        VoxelShape shape = state.getOutlineShape(client.world, pos);
        if (shape.isEmpty()) return;
        
        Box box = shape.getBoundingBox().offset(pos).expand(0.005);
        
        // Gökkuşağı (RGB) efekti hesaplama
        long time = System.currentTimeMillis();
        int r = (int) ((Math.sin(time * 0.002) + 1.0) * 127.5);
        int g = (int) ((Math.sin(time * 0.002 + 2.0) + 1.0) * 127.5);
        int b = (int) ((Math.sin(time * 0.002 + 4.0) + 1.0) * 127.5);
        
        // Sadece kenarları (çizgileri) çizmek için GL_LINES kullanıyoruz
        Matrix4f matrix = 
//? if <=1.19.2 {
/*matrixStack.peek().getModel()*/
//?} else {
matrixStack.peek().getPositionMatrix()
//?}
;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        //? if <=1.19.2 {
/*RenderSystem.disableTexture();
*///?}
        RenderSystem.lineWidth(3.0F); // Kalın çizgi
        
        buffer.begin(
//? if <=1.19.2 {
/*1*/
//?} else {
net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINES
//?}
, net.minecraft.client.render.VertexFormats.POSITION_COLOR); // GL_LINES
        
        float minX = (float)box.minX;
        float minY = (float)box.minY;
        float minZ = (float)box.minZ;
        float maxX = (float)box.maxX;
        float maxY = (float)box.maxY;
        float maxZ = (float)box.maxZ;
        
        // Alt Yüzey Kenarları
        buffer.vertex(matrix, minX, minY, minZ).color(r, g, b, 255).next(); buffer.vertex(matrix, maxX, minY, minZ).color(r, g, b, 255).next();
        buffer.vertex(matrix, maxX, minY, minZ).color(r, g, b, 255).next(); buffer.vertex(matrix, maxX, minY, maxZ).color(r, g, b, 255).next();
        buffer.vertex(matrix, maxX, minY, maxZ).color(r, g, b, 255).next(); buffer.vertex(matrix, minX, minY, maxZ).color(r, g, b, 255).next();
        buffer.vertex(matrix, minX, minY, maxZ).color(r, g, b, 255).next(); buffer.vertex(matrix, minX, minY, minZ).color(r, g, b, 255).next();
        
        // Üst Yüzey Kenarları
        buffer.vertex(matrix, minX, maxY, minZ).color(r, g, b, 255).next(); buffer.vertex(matrix, maxX, maxY, minZ).color(r, g, b, 255).next();
        buffer.vertex(matrix, maxX, maxY, minZ).color(r, g, b, 255).next(); buffer.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, 255).next();
        buffer.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, 255).next(); buffer.vertex(matrix, minX, maxY, maxZ).color(r, g, b, 255).next();
        buffer.vertex(matrix, minX, maxY, maxZ).color(r, g, b, 255).next(); buffer.vertex(matrix, minX, maxY, minZ).color(r, g, b, 255).next();
        
        // Dikey Kenarlar
        buffer.vertex(matrix, minX, minY, minZ).color(r, g, b, 255).next(); buffer.vertex(matrix, minX, maxY, minZ).color(r, g, b, 255).next();
        buffer.vertex(matrix, maxX, minY, minZ).color(r, g, b, 255).next(); buffer.vertex(matrix, maxX, maxY, minZ).color(r, g, b, 255).next();
        buffer.vertex(matrix, maxX, minY, maxZ).color(r, g, b, 255).next(); buffer.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, 255).next();
        buffer.vertex(matrix, minX, minY, maxZ).color(r, g, b, 255).next(); buffer.vertex(matrix, minX, maxY, maxZ).color(r, g, b, 255).next();

        tessellator.draw();
        RenderSystem.lineWidth(1.0F); // Geri yükle
    }
    
    private void drawBox(MatrixStack matrixStack, Box box, int r, int g, int b, int a) {
        Matrix4f matrix = 
//? if <=1.19.2 {
/*matrixStack.peek().getModel()*/
//?} else {
matrixStack.peek().getPositionMatrix()
//?}
;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        // Quads for faces
        //? if >1.19.2 {
        buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.QUADS, net.minecraft.client.render.VertexFormats.POSITION_COLOR); // GL_QUADS
//?} else {
        /*buffer.begin(7, net.minecraft.client.render.VertexFormats.POSITION_COLOR);*///?}
        
        float minX = (float)box.minX;
        float minY = (float)box.minY;
        float minZ = (float)box.minZ;
        float maxX = (float)box.maxX;
        float maxY = (float)box.maxY;
        float maxZ = (float)box.maxZ;
        
        // Bottom
        buffer.vertex(matrix, minX, minY, minZ).color(r, g, b, a).next();
        buffer.vertex(matrix, maxX, minY, minZ).color(r, g, b, a).next();
        buffer.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a).next();
        buffer.vertex(matrix, minX, minY, maxZ).color(r, g, b, a).next();
        
        // Top
        buffer.vertex(matrix, minX, maxY, minZ).color(r, g, b, a).next();
        buffer.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a).next();
        buffer.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a).next();
        buffer.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a).next();
        
        tessellator.draw();
    }
}
