package com.ersin.legitbridge;

import com.ersin.legitbridge.command.ModCommands;
import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.gui.ModConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
//? if <=1.19.2
//import net.minecraft.text.LiteralText;
import com.ersin.legitbridge.module.ModuleManager;
import com.ersin.legitbridge.module.impl.*;
import com.ersin.legitbridge.helpers.InventoryMouseMover;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LegitBoosterMod implements ClientModInitializer {
    public static final String MOD_ID = "legitbooster";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    private static KeyBinding toggleKey;
    private static KeyBinding menuKey;
    private static final java.util.Map<Integer, Boolean> prevKeyState = new java.util.HashMap<>();
    
    public static ModuleManager moduleManager;

    @Override
    public void onInitializeClient() {
        System.out.println("LegitBooster Mod başlatıldı!");
        
        moduleManager = new ModuleManager();
        registerModules();
        
        ModConfig.load();
        ModCommands.register();
        ModHudRenderer.register();
        
        // WorldRender is a module now, we will handle it in the event loops if needed
        // but for now we keep the structure

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.legitbridge.toggle",
                InputUtil.Type.KEYSYM,
                ModConfig.toggleKeyCode,
                "category.legitbridge.main"
        ));

        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.legitbridge.menu",
                InputUtil.Type.KEYSYM,
                ModConfig.menuKeyCode,
                "category.legitbridge.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                ModConfig.enabled = !ModConfig.enabled;
                ModConfig.save();
                ModConfigScreen.playConfigSound();
                if (client.player != null && !ModConfig.streamerMode) {
                    //? if <=1.19.2 {
/*client.player.sendMessage(new net.minecraft.text.LiteralText("§7[" + ModConfig.getThemePrefix() + "LegitBridge§7] Durum: " + (ModConfig.enabled ? "§aAÇIK" : "§cKAPALI")), true);
*///?} else {
client.player.sendMessage(com.ersin.legitbridge.utils.VersionHelper.literalText("§7[" + ModConfig.getThemePrefix() + "LegitBridge§7] Durum: " + (ModConfig.enabled ? "§aAÇIK" : "§cKAPALI")), true);
//?}
}
            }

            while (menuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    ModConfigScreen.playConfigSound();
                    com.ersin.legitbridge.utils.VersionHelper.setScreen(client, new ModConfigScreen());
                }
            }
            
            // Dinamik Tuş Atamaları (Keybinds) Kontrolü
            if (client.currentScreen == null && client.getWindow() != null) {
                long handle = client.getWindow().getHandle();
                for (java.util.Map.Entry<String, Integer> entry : ModConfig.keyBinds.entrySet()) {
                    int keyCode = entry.getValue();
                    if (keyCode <= 0) continue;
                    
                    boolean isPressed = InputUtil.isKeyPressed(handle, keyCode);
                    boolean wasPressed = prevKeyState.getOrDefault(keyCode, false);
                    
                    if (isPressed && !wasPressed) {
                        // Tuşa yeni basıldı
                        try {
                            java.lang.reflect.Field field = ModConfig.class.getField(entry.getKey());
                            boolean currentValue = field.getBoolean(null);
                            field.setBoolean(null, !currentValue);
                            ModConfig.save();
                            ModConfigScreen.playConfigSound();
                            
                            if (client.player != null && !ModConfig.streamerMode) {
                                //? if <=1.19.2 {
/*client.player.sendMessage(new net.minecraft.text.LiteralText("§7[" + ModConfig.getThemePrefix() + "LegitBridge§7] " + entry.getKey() + ": " + (!currentValue ? "§aAÇIK" : "§cKAPALI")), true);
*///?} else {
client.player.sendMessage(com.ersin.legitbridge.utils.VersionHelper.literalText("§7[" + ModConfig.getThemePrefix() + "LegitBridge§7] " + entry.getKey() + ": " + (!currentValue ? "§aAÇIK" : "§cKAPALI")), true);
//?}
}
                        } catch (Exception e) {
                            LOGGER.error("Keybind error for field: " + entry.getKey(), e);
                        }
                    }
                    prevKeyState.put(keyCode, isPressed);
                }
            }

            // Removed old click tracking to allow MixinMouse to handle CPS accurately
            
            // Helper Tick Events (Migrated to Module System)
            if (ModConfig.enabled) {
                moduleManager.onTick();
            }
            
            // Survival & Mouse Sim
            InventoryMouseMover.tick();
        });
    }

    private void registerModules() {
        moduleManager.registerModule(new AutoElytra());
        moduleManager.registerModule(new AutoSupply());
        moduleManager.registerModule(new AutoTool());
        moduleManager.registerModule(new AutoTotemModule());
        moduleManager.registerModule(new CombatAssist());
        moduleManager.registerModule(new AutoWeapon());
        moduleManager.registerModule(new AutoPot());
        moduleManager.registerModule(new PerfectCrit());
        moduleManager.registerModule(new BowAimbot());
        moduleManager.registerModule(new FireballAssist());
        moduleManager.registerModule(new Freecam());
        moduleManager.registerModule(new JumpBridge());
        moduleManager.registerModule(new JumpReset());
        moduleManager.registerModule(new LegitScaffold());
        moduleManager.registerModule(new NinjaBridge());
        moduleManager.registerModule(new SlotRotation());
        moduleManager.registerModule(new StructureFinder());
        moduleManager.registerModule(new TriggerBot());
        moduleManager.registerModule(new LegitAutoCrystal());
        moduleManager.registerModule(new LegitWTap());
        moduleManager.registerModule(new LegitAutoPot());
        moduleManager.registerModule(new LegitAutoRod());
        moduleManager.registerModule(new WorldRender());
        moduleManager.registerModule(new AutoEat());
        moduleManager.registerModule(new AutoFish());
        moduleManager.registerModule(new AutoBed());
        moduleManager.registerModule(new FakeLag());
        moduleManager.registerModule(new Derp());
        moduleManager.registerModule(new DurabilityWarning());
        moduleManager.registerModule(new Fullbright());
        moduleManager.registerModule(new InstaHouse());
        moduleManager.registerModule(new VoidSave());
        moduleManager.registerModule(new AutoMlg());
        moduleManager.registerModule(new SmartTorch());
        moduleManager.registerModule(new LavaEvader());
        moduleManager.registerModule(new InvMove());
        moduleManager.registerModule(new AutoArmor());
        moduleManager.registerModule(new NoHurtCam());
        moduleManager.registerModule(new TrueSight());
        moduleManager.registerModule(new CleanScreen());
        moduleManager.registerModule(new LegitChestStealer());
        moduleManager.registerModule(new AutoRefill());
        moduleManager.registerModule(new LegitAutoClicker());
        moduleManager.registerModule(new AutoSprint());
        moduleManager.registerModule(new SmartESP());
        moduleManager.registerModule(new BlockOverlay());
        moduleManager.registerModule(new StreamerMode());
        moduleManager.registerModule(new Waypoints());
        moduleManager.registerModule(new Dodge());
        moduleManager.registerModule(new AutoBlockDefense());
        moduleManager.registerModule(new HitBoxExpander());
        moduleManager.registerModule(new ProjectileWarning());
        moduleManager.registerModule(new PlayerESP());
        moduleManager.registerModule(new Killaura());
        moduleManager.registerModule(new Velocity());
        moduleManager.registerModule(new Step());
        moduleManager.registerModule(new NoRender());
    }
}
