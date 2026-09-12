package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.item.BlockItem;
//? if <=1.19.2 {
/*import net.minecraft.network.Packet;
*///?} else {
import net.minecraft.network.packet.Packet;
//?}
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {

    private float serverYaw;
    private float serverPitch;
    private boolean isSpoofing = false;
    private int placeTimer = 0;

    private BlockPos targetBlock = null;
    private Direction targetFace = null;

    public Scaffold() {
        super("Scaffold", "İstediğiniz yere bakarken otomatik olarak altınıza blok koyar (Sunucu rotasyonu ile).", Category.LEGIT);
    }

    @Override
    public void onEnable() {
        if (!isSpoofing) {
            serverYaw = com.ersin.legitbridge.utils.VersionHelper.getYaw(client.player);
            serverPitch = com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player);
        }
        isSpoofing = false;
        targetBlock = null;
        targetFace = null;
    }

    @Override
    public void onTick() {
        if (!ModConfig.enabled || !ModConfig.scaffold) {
            isSpoofing = false;
            return;
        }

        if (client.player == null || client.world == null) return;

        // Check if holding a block
        if (client.player.getMainHandStack().isEmpty() || !(client.player.getMainHandStack().getItem() instanceof BlockItem)) {
            isSpoofing = false;
            return;
        }

        if (placeTimer > 0) {
            placeTimer--;
        }

        // Only place if Right Click is held
        //? if >1.19.2 {
        boolean using = client.options.useKey.isPressed();
        //?} else {
        /*boolean using = client.options.keyUse.isPressed();*///?}
        if (!using) {
            isSpoofing = false;
            return;
        }

        BlockPos playerPos = client.player.getBlockPos().down();
        
        // Find a block to attach to
        //? if >1.19.2 {
        boolean isReplaceable = client.world.getBlockState(playerPos).isReplaceable();
        //?} else {
        /*boolean isReplaceable = client.world.getBlockState(playerPos).getMaterial().isReplaceable();*///?}

        if (isReplaceable) {
            findTargetBlock(playerPos);
        } else {
            targetBlock = null;
            targetFace = null;
        }

        if (targetBlock != null && targetFace != null) {
            isSpoofing = true;
            
            // Calculate required rotation
            Vec3d targetHitVec = new Vec3d(
                    targetBlock.getX() + 0.5 + targetFace.getOffsetX() * 0.5,
                    targetBlock.getY() + 0.5 + targetFace.getOffsetY() * 0.5,
                    targetBlock.getZ() + 0.5 + targetFace.getOffsetZ() * 0.5
            );
            
            float[] rotations = getRotations(client.player.getPos().add(0, client.player.getEyeHeight(client.player.getPose()), 0), targetHitVec);
            
            // Smoothly interpolate rotations
            float speed = ModConfig.scaffoldRotationSpeed; // e.g., 20.0f
            serverYaw = interpolateRotation(serverYaw, rotations[0], speed);
            serverPitch = interpolateRotation(serverPitch, rotations[1], speed);

            // If we are looking closely enough, place the block
            float yawDiff = Math.abs(MathHelper.wrapDegrees(serverYaw - rotations[0]));
            float pitchDiff = Math.abs(MathHelper.wrapDegrees(serverPitch - rotations[1]));

            if (yawDiff < 20.0f && pitchDiff < 20.0f && placeTimer == 0) {
                placeBlock(targetBlock, targetFace, targetHitVec);
                placeTimer = 4; // Add a small delay between placements
            }
        } else {
            isSpoofing = false;
            // Slowly return to real rotations when not scaffolding
            float speed = ModConfig.scaffoldRotationSpeed;
            // Yumuşak dönüş
            serverYaw = interpolateRotation(serverYaw, com.ersin.legitbridge.utils.VersionHelper.getYaw(client.player), speed);
            serverPitch = interpolateRotation(serverPitch, com.ersin.legitbridge.utils.VersionHelper.getPitch(client.player), speed);
        }
    }

    private void findTargetBlock(BlockPos pos) {
        Direction[] offsets = {Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP};
        for (Direction offset : offsets) {
            BlockPos adjacent = pos.offset(offset);
            //? if >1.19.2 {
            boolean isReplaceable = client.world.getBlockState(adjacent).isReplaceable();
            //?} else {
            /*boolean isReplaceable = client.world.getBlockState(adjacent).getMaterial().isReplaceable();*///?}
            
            if (!isReplaceable) {
                targetBlock = adjacent;
                targetFace = offset.getOpposite();
                return;
            }
        }
        targetBlock = null;
        targetFace = null;
    }

    private void placeBlock(BlockPos pos, Direction face, Vec3d hitVec) {
        BlockHitResult hitResult = new BlockHitResult(hitVec, face, pos, false);
        
        // Bloğu yerleştirme paketini gönder
        //? if >1.19.2 {
        client.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, hitResult, 0));
        //?} else {
        /*client.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, hitResult));*///?}
        // Swing hand
        client.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
        // Also do client side action if needed to update inventory/swing animation
        client.player.swingHand(Hand.MAIN_HAND);
    }

    private float[] getRotations(Vec3d from, Vec3d to) {
        double diffX = to.x - from.x;
        double diffY = to.y - from.y;
        double diffZ = to.z - from.z;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0 / Math.PI);
        
        return new float[]{yaw, pitch};
    }

    private float interpolateRotation(float current, float target, float speed) {
        float diff = MathHelper.wrapDegrees(target - current);
        if (diff > speed) {
            diff = speed;
        }
        if (diff < -speed) {
            diff = -speed;
        }
        return current + diff;
    }

    private boolean sendingFake = false;

    public boolean onSendPacket(Packet<?> packet) {
        if (!ModConfig.enabled || !ModConfig.scaffold || !isSpoofing || sendingFake) return false;

        if (packet instanceof PlayerMoveC2SPacket) {
            PlayerMoveC2SPacket p = (PlayerMoveC2SPacket) packet;
            
            //? if >1.19.2 {
            boolean changesPosition = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
            boolean changesLook = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
            //?} else {
            /*boolean changesPosition = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionOnly || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Both;
            boolean changesLook = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookOnly || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Both;*///?}

            double x = changesPosition ? p.getX(client.player.getX()) : client.player.getX();
            double y = changesPosition ? p.getY(client.player.getY()) : client.player.getY();
            double z = changesPosition ? p.getZ(client.player.getZ()) : client.player.getZ();
            boolean onGround = p.isOnGround();
            
            Packet<?> newPacket;
            if (changesPosition) {
                //? if >1.19.2 {
                newPacket = new PlayerMoveC2SPacket.Full(x, y, z, serverYaw, serverPitch, onGround);
                //?} else {
                /*newPacket = new PlayerMoveC2SPacket.Both(x, y, z, serverYaw, serverPitch, onGround);*///?}
            } else {
                //? if >1.19.2 {
                newPacket = new PlayerMoveC2SPacket.LookAndOnGround(serverYaw, serverPitch, onGround);
                //?} else {
                /*newPacket = new PlayerMoveC2SPacket.LookOnly(serverYaw, serverPitch, onGround);*///?}
            }
            
            // Send the spoofed packet
            sendingFake = true;
            client.getNetworkHandler().sendPacket(newPacket);
            sendingFake = false;
            return true; // Cancel the original packet
        }

        return false;
    }
}
