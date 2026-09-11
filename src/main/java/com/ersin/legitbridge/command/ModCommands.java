package com.ersin.legitbridge.command;

import com.ersin.legitbridge.config.ModConfig;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
//? if <=1.19.2 {
/*import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
*///?} else {
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//?}
//? if <=1.19.2
//import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class ModCommands {

    private static Text getText(String text) {
        //? if <=1.19.2 {
        /*return new net.minecraft.text.LiteralText(text);
        *///?} else {
        return com.ersin.legitbridge.utils.VersionHelper.literalText(text);
        //?}
    }

    public static void register() {
//? if <=1.19.2 {
        /*ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("legitbridge")
*///?} else {
        net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("legitbridge")
//?}
            .then(ClientCommandManager.literal("toggle").executes(context -> {
                ModConfig.enabled = !ModConfig.enabled;
                ModConfig.save();
                context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] Mod durumu: " + (ModConfig.enabled ? "§aAÇIK" : "§cKAPALI")));
                return 1;
            }))
            .then(ClientCommandManager.literal("autosupply").executes(context -> {
                ModConfig.autoSupply = !ModConfig.autoSupply;
                ModConfig.save();
                context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] Auto-Supply: " + (ModConfig.autoSupply ? "§aAÇIK" : "§cKAPALI")));
                return 1;
            }))
            .then(ClientCommandManager.literal("directional").executes(context -> {
                ModConfig.directionalAssist = !ModConfig.directionalAssist;
                ModConfig.save();
                context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] Directional Assist: " + (ModConfig.directionalAssist ? "§aAÇIK" : "§cKAPALI")));
                return 1;
            }))
            .then(ClientCommandManager.literal("safewalk").executes(context -> {
                ModConfig.safeWalk = !ModConfig.safeWalk;
                ModConfig.save();
                context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] SafeWalk: " + (ModConfig.safeWalk ? "§aAÇIK" : "§cKAPALI")));
                return 1;
            }))
            .then(ClientCommandManager.literal("jumpbridge").executes(context -> {
                ModConfig.jumpBridge = !ModConfig.jumpBridge;
                ModConfig.save();
                context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] Jump-Bridge (Zıplayarak Yol): " + (ModConfig.jumpBridge ? "§aAÇIK" : "§cKAPALI")));
                return 1;
            }))
            .then(ClientCommandManager.literal("staircase").executes(context -> {
                ModConfig.staircaseAssist = !ModConfig.staircaseAssist;
                ModConfig.save();
                context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] Merdiven Yol Asistanı: " + (ModConfig.staircaseAssist ? "§aAÇIK" : "§cKAPALI")));
                return 1;
            }))
            .then(ClientCommandManager.literal("fastclick").executes(context -> {
                ModConfig.fastClick = !ModConfig.fastClick;
                ModConfig.save();
                context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] FastClick: " + (ModConfig.fastClick ? "§aAÇIK" : "§cKAPALI")));
                return 1;
            }))
            .then(ClientCommandManager.literal("hud").executes(context -> {
                ModConfig.hudEnabled = !ModConfig.hudEnabled;
                ModConfig.save();
                context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] Ekran HUD Göstergesi: " + (ModConfig.hudEnabled ? "§aAÇIK" : "§cKAPALI")));
                return 1;
            }))
            .then(ClientCommandManager.literal("triggerbot").executes(context -> {
                ModConfig.triggerBot = !ModConfig.triggerBot;
                ModConfig.save();
                context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] TriggerBot: " + (ModConfig.triggerBot ? "§aAÇIK" : "§cKAPALI")));
                return 1;
            }))
            .then(ClientCommandManager.literal("autotool").executes(context -> {
                ModConfig.autoTool = !ModConfig.autoTool;
                ModConfig.save();
                context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] Auto-Tool / Weapon: " + (ModConfig.autoTool ? "§aAÇIK" : "§cKAPALI")));
                return 1;
            }))
            .then(ClientCommandManager.literal("slotrotate").executes(context -> {
                ModConfig.slotRotation = !ModConfig.slotRotation;
                ModConfig.save();
                context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] Hotbar Slot Rotasyonu: " + (ModConfig.slotRotation ? "§aAÇIK" : "§cKAPALI")));
                return 1;
            }))
            .then(ClientCommandManager.literal("reachguard").executes(context -> {
                ModConfig.reachGuard = !ModConfig.reachGuard;
                ModConfig.save();
                context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] Reach Guard (3.0 Max Reach): " + (ModConfig.reachGuard ? "§aAÇIK" : "§cKAPALI")));
                return 1;
            }))
            .then(ClientCommandManager.literal("profile")
                .then(ClientCommandManager.argument("name", StringArgumentType.word())
                    .executes(context -> {
                        String name = StringArgumentType.getString(context, "name");
                        if (ModConfig.applyProfile(name)) {
                            context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] Profil aktif edildi: §a" + name.toUpperCase()));
                        } else {
                            context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] Geçersiz profil! (§elegit§7, §efast§7, §erage§7)"));
                        }
                        return 1;
                    })
                )
            )
            .then(ClientCommandManager.literal("delay")
                .then(ClientCommandManager.argument("min", IntegerArgumentType.integer(0, 20))
                    .then(ClientCommandManager.argument("max", IntegerArgumentType.integer(0, 20))
                        .executes(context -> {
                            int min = IntegerArgumentType.getInteger(context, "min");
                            int max = IntegerArgumentType.getInteger(context, "max");
                            ModConfig.minDelayTick = min;
                            ModConfig.maxDelayTick = max;
                            ModConfig.save();
                            context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] Gecikme aralığı ayarlandı: §a" + min + " - " + max + " tick"));
                            return 1;
                        })
                    )
                )
            )
            .then(ClientCommandManager.literal("fastclickdelay")
                .then(ClientCommandManager.argument("min", IntegerArgumentType.integer(0, 20))
                    .then(ClientCommandManager.argument("max", IntegerArgumentType.integer(0, 20))
                        .executes(context -> {
                            int min = IntegerArgumentType.getInteger(context, "min");
                            int max = IntegerArgumentType.getInteger(context, "max");
                            ModConfig.minFastClickDelay = min;
                            ModConfig.maxFastClickDelay = max;
                            ModConfig.save();
                            context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] FastClick Gecikme aralığı ayarlandı: §a" + min + " - " + max + " tick"));
                            return 1;
                        })
                    )
                )
            )
            .then(ClientCommandManager.literal("edgeoffset")
                .then(ClientCommandManager.argument("min", DoubleArgumentType.doubleArg(0.01, 1.0))
                    .then(ClientCommandManager.argument("max", DoubleArgumentType.doubleArg(0.01, 1.0))
                        .executes(context -> {
                            double min = DoubleArgumentType.getDouble(context, "min");
                            double max = DoubleArgumentType.getDouble(context, "max");
                            ModConfig.minEdgeOffset = min;
                            ModConfig.maxEdgeOffset = max;
                            ModConfig.save();
                            context.getSource().sendFeedback(getText("§7[§bLegitBridge§7] SafeWalk Toleransı ayarlandı: §a" + min + " - " + max));
                            return 1;
                        })
                    )
                )
            )
        );
//? if >1.19.2 {
        });
//?}
    }
}
