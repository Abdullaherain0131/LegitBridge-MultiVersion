package com.ersin.legitbridge.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.screen.Screen;

//? if >1.19.2 {
import net.minecraft.registry.Registries;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.network.packet.Packet;
//?} else {
/*import net.minecraft.util.registry.Registry;
import net.minecraft.network.Packet;
*///?}

public class VersionHelper {

    public static PlayerInventory getInventory(PlayerEntity player) {
//? if >1.19.2 {
        return player.getInventory();
//?} else {
        /*return player.inventory;*///?}
    }

    public static float getPitch(Entity entity) {
//? if >1.19.2 {
        return entity.getPitch();
//?} else {
        /*return entity.pitch;*///?}
    }

    public static float getYaw(Entity entity) {
//? if >1.19.2 {
        return entity.getYaw();
//?} else {
        /*return entity.yaw;*///?}
    }

    public static void setPitch(Entity entity, float pitch) {
//? if >1.19.2 {
        entity.setPitch(pitch);
//?} else {
        /*entity.pitch = pitch;*///?}
    }

    public static void setYaw(Entity entity, float yaw) {
//? if >1.19.2 {
        entity.setYaw(yaw);
//?} else {
        /*entity.yaw = yaw;*///?}
    }

    public static PlayerAbilities getAbilities(PlayerEntity player) {
//? if >1.19.2 {
        return player.getAbilities();
//?} else {
        /*return player.abilities;*///?}
    }

    public static int getWidgetX(ClickableWidget widget) {
//? if >1.19.2 {
        return widget.getX();
//?} else {
        /*return widget.x;*///?}
    }

    public static int getWidgetY(ClickableWidget widget) {
//? if >1.19.2 {
        return widget.getY();
//?} else {
        /*return widget.y;*///?}
    }

    public static void setWidgetY(ClickableWidget widget, int y) {
//? if >1.19.2 {
        widget.setY(y);
//?} else {
        /*widget.y = y;*///?}
    }

    public static boolean isUseKeyPressed(MinecraftClient client) {
//? if >1.19.2 {
        return client.options.useKey.isPressed();
//?} else {
        /*return client.options.keyUse.isPressed();*///?}
    }

    public static void setUseKeyPressed(MinecraftClient client, boolean pressed) {
//? if >1.19.2 {
        client.options.useKey.setPressed(pressed);
//?} else {
        /*client.options.keyUse.setPressed(pressed);*///?}
    }

    public static boolean isAttackKeyPressed(MinecraftClient client) {
//? if >1.19.2 {
        return client.options.attackKey.isPressed();
//?} else {
        /*return client.options.keyAttack.isPressed();*///?}
    }

    public static Text literalText(String text) {
//? if >1.19.2 {
        return Text.literal(text);
//?} else {
        /*return new net.minecraft.text.LiteralText(text);*///?}
    }

    public static void setScreen(MinecraftClient client, Screen screen) {
//? if >1.19.2 {
        client.setScreen(screen);
//?} else {
        /*client.openScreen(screen);*///?}
    }

    public static Identifier getItemId(Item item) {
//? if >1.19.2 {
        return Registries.ITEM.getId(item);
//?} else {
        /*return Registry.ITEM.getId(item);*///?}
    }

    public static double getGamma(MinecraftClient client) {
//? if >1.19.2 {
        return client.options.getGamma().getValue();
//?} else {
        /*return client.options.gamma;*///?}
    }
    
    public static void setGamma(MinecraftClient client, double gamma) {
//? if >1.19.2 {
        client.options.getGamma().setValue(gamma);
//?} else {
        /*client.options.gamma = gamma;*///?}
    }



    public static net.minecraft.world.World getWorld(Entity entity) {
//? if >1.19.2 {
        return entity.getWorld();
//?} else {
        /*return entity.getEntityWorld();*///?}
    }

    public static void removeEntity(MinecraftClient client, int id) {
//? if >1.19.2 {
        client.world.removeEntity(id, Entity.RemovalReason.DISCARDED);
//?} else {
        /*client.world.removeEntity(id);*///?}
    }

    public static boolean isAttackable(Entity entity) {
//? if >1.19.2 {
        return entity.canHit();
//?} else {
        /*return entity.isAttackable();*///?}
    }
    public static int getEntityId(Entity entity) {
//? if >1.19.2 {
        return entity.getId();
//?} else {
        /*return entity.getEntityId();*///?}
    }


    public static void setStepHeight(net.minecraft.client.network.ClientPlayerEntity player, float height) {
//? if >1.19.2 {
        player.setStepHeight(height);
//?} else {
        /*player.stepHeight = height;*///?}
    }
    
    public static net.minecraft.block.Block getBlockById(String id) {
//? if >1.19.2 {
        return net.minecraft.registry.Registries.BLOCK.get(new Identifier(id));
//?} else {
        /*return net.minecraft.util.registry.Registry.BLOCK.get(new Identifier(id));*///?}
    }

    public static void interactItem(MinecraftClient client, net.minecraft.util.Hand hand) {
//? if >1.19.2 {
        client.interactionManager.interactItem(client.player, hand);
//?} else {
        /*client.interactionManager.interactItem(client.player, client.world, hand);*///?}
    }
}
