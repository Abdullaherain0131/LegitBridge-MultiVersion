package com.ersin.legitbridge.module;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    private final java.util.Map<String, Module> moduleMap = new java.util.HashMap<>();

    public ModuleManager() {
        // We will register all modules here later
    }

    public void registerModule(Module module) {
        modules.add(module);
        moduleMap.put(module.getName().toLowerCase(), module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Category category) {
        List<Module> categoryModules = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory() == category) {
                categoryModules.add(m);
            }
        }
        return categoryModules;
    }

    public Module getModuleByName(String name) {
        return moduleMap.get(name.toLowerCase());
    }

    public void syncWithConfig() {
        if (getModuleByName("AutoElytra") != null) getModuleByName("AutoElytra").setEnabled(com.ersin.legitbridge.config.ModConfig.autoElytra);
        if (getModuleByName("AutoSupply") != null) getModuleByName("AutoSupply").setEnabled(com.ersin.legitbridge.config.ModConfig.autoSupply);
        if (getModuleByName("AutoTool") != null) getModuleByName("AutoTool").setEnabled(com.ersin.legitbridge.config.ModConfig.autoTool);
        if (getModuleByName("AutoTotemModule") != null) getModuleByName("AutoTotemModule").setEnabled(com.ersin.legitbridge.config.ModConfig.autoTotem);
        if (getModuleByName("CombatAssist") != null) getModuleByName("CombatAssist").setEnabled(com.ersin.legitbridge.config.ModConfig.combatAssist);
        if (getModuleByName("AutoWeapon") != null) getModuleByName("AutoWeapon").setEnabled(com.ersin.legitbridge.config.ModConfig.autoWeapon);
        if (getModuleByName("AutoPot") != null) getModuleByName("AutoPot").setEnabled(com.ersin.legitbridge.config.ModConfig.autoPot);
        if (getModuleByName("PerfectCrit") != null) getModuleByName("PerfectCrit").setEnabled(com.ersin.legitbridge.config.ModConfig.perfectCrit);
        if (getModuleByName("BowAimbot") != null) getModuleByName("BowAimbot").setEnabled(com.ersin.legitbridge.config.ModConfig.bowAimbot);
        if (getModuleByName("Dodge") != null) getModuleByName("Dodge").setEnabled(com.ersin.legitbridge.config.ModConfig.autoDodge);
        if (getModuleByName("FireballAssist") != null) getModuleByName("FireballAssist").setEnabled(com.ersin.legitbridge.config.ModConfig.fireballAssist);
        if (getModuleByName("Freecam") != null) getModuleByName("Freecam").setEnabled(com.ersin.legitbridge.config.ModConfig.freecam);
        if (getModuleByName("JumpBridge") != null) getModuleByName("JumpBridge").setEnabled(com.ersin.legitbridge.config.ModConfig.jumpBridge);
        if (getModuleByName("JumpReset") != null) getModuleByName("JumpReset").setEnabled(com.ersin.legitbridge.config.ModConfig.jumpReset);
        if (getModuleByName("LegitScaffold") != null) getModuleByName("LegitScaffold").setEnabled(com.ersin.legitbridge.config.ModConfig.legitScaffold);
        if (getModuleByName("Scaffold") != null) getModuleByName("Scaffold").setEnabled(com.ersin.legitbridge.config.ModConfig.scaffold);
        if (getModuleByName("NinjaBridge") != null) getModuleByName("NinjaBridge").setEnabled(com.ersin.legitbridge.config.ModConfig.ninjaBridge);
        if (getModuleByName("SlotRotation") != null) getModuleByName("SlotRotation").setEnabled(com.ersin.legitbridge.config.ModConfig.slotRotation);
        if (getModuleByName("StructureFinder") != null) getModuleByName("StructureFinder").setEnabled(com.ersin.legitbridge.config.ModConfig.showStructureFinder);
        if (getModuleByName("TriggerBot") != null) getModuleByName("TriggerBot").setEnabled(com.ersin.legitbridge.config.ModConfig.triggerBot);
        if (getModuleByName("WorldRender") != null) getModuleByName("WorldRender").setEnabled(true);
        if (getModuleByName("AutoBed") != null) getModuleByName("AutoBed").setEnabled(com.ersin.legitbridge.config.ModConfig.autoBed);
        if (getModuleByName("FakeLag") != null) getModuleByName("FakeLag").setEnabled(com.ersin.legitbridge.config.ModConfig.fakeLag);
        if (getModuleByName("Derp") != null) getModuleByName("Derp").setEnabled(com.ersin.legitbridge.config.ModConfig.derp);
        if (getModuleByName("InstaHouse") != null) getModuleByName("InstaHouse").setEnabled(com.ersin.legitbridge.config.ModConfig.instaHouse);
        if (getModuleByName("VoidSave") != null) getModuleByName("VoidSave").setEnabled(com.ersin.legitbridge.config.ModConfig.voidSave);
        
        // Hardcore & Survival
        if (getModuleByName("AutoEat") != null) getModuleByName("AutoEat").setEnabled(com.ersin.legitbridge.config.ModConfig.autoEat);
        if (getModuleByName("AutoFish") != null) getModuleByName("AutoFish").setEnabled(com.ersin.legitbridge.config.ModConfig.autoFish);
        if (getModuleByName("DurabilityWarning") != null) getModuleByName("DurabilityWarning").setEnabled(com.ersin.legitbridge.config.ModConfig.durabilityWarning || com.ersin.legitbridge.config.ModConfig.autoElytraSwap);
        if (getModuleByName("AutoMlg") != null) getModuleByName("AutoMlg").setEnabled(com.ersin.legitbridge.config.ModConfig.autoMlg);
        if (getModuleByName("SmartTorch") != null) getModuleByName("SmartTorch").setEnabled(com.ersin.legitbridge.config.ModConfig.smartTorch);
        if (getModuleByName("LavaEvader") != null) getModuleByName("LavaEvader").setEnabled(com.ersin.legitbridge.config.ModConfig.lavaEvader);
        if (getModuleByName("InvMove") != null) getModuleByName("InvMove").setEnabled(com.ersin.legitbridge.config.ModConfig.invMove);
        if (getModuleByName("AutoArmor") != null) getModuleByName("AutoArmor").setEnabled(com.ersin.legitbridge.config.ModConfig.autoArmor);
        if (getModuleByName("NoHurtCam") != null) getModuleByName("NoHurtCam").setEnabled(com.ersin.legitbridge.config.ModConfig.noHurtCam);
        if (getModuleByName("TrueSight") != null) getModuleByName("TrueSight").setEnabled(com.ersin.legitbridge.config.ModConfig.trueSight);
        if (getModuleByName("CleanScreen") != null) getModuleByName("CleanScreen").setEnabled(com.ersin.legitbridge.config.ModConfig.cleanScreen);
        if (getModuleByName("Fullbright") != null) getModuleByName("Fullbright").setEnabled(com.ersin.legitbridge.config.ModConfig.fullbright);
        
        if (getModuleByName("PlayerESP") != null) getModuleByName("PlayerESP").setEnabled(com.ersin.legitbridge.config.ModConfig.playerESP);
        if (getModuleByName("Killaura") != null) getModuleByName("Killaura").setEnabled(com.ersin.legitbridge.config.ModConfig.killaura);
        if (getModuleByName("Velocity") != null) getModuleByName("Velocity").setEnabled(com.ersin.legitbridge.config.ModConfig.velocity);
        if (getModuleByName("Step") != null) getModuleByName("Step").setEnabled(com.ersin.legitbridge.config.ModConfig.step);
        if (getModuleByName("HitBoxExpander") != null) getModuleByName("HitBoxExpander").setEnabled(com.ersin.legitbridge.config.ModConfig.hitBoxExpander);
        if (getModuleByName("AutoBlockDefense") != null) getModuleByName("AutoBlockDefense").setEnabled(com.ersin.legitbridge.config.ModConfig.autoBlockDefense);
        if (getModuleByName("ProjectileWarning") != null) getModuleByName("ProjectileWarning").setEnabled(com.ersin.legitbridge.config.ModConfig.projectileWarning);
        if (getModuleByName("StreamerMode") != null) getModuleByName("StreamerMode").setEnabled(com.ersin.legitbridge.config.ModConfig.streamerMode);
        if (getModuleByName("FPSBooster") != null) getModuleByName("FPSBooster").setEnabled(com.ersin.legitbridge.config.ModConfig.fpsBooster);
        if (getModuleByName("MemoryCleaner") != null) getModuleByName("MemoryCleaner").setEnabled(com.ersin.legitbridge.config.ModConfig.memoryCleaner);
        if (getModuleByName("AutoBuilder") != null) getModuleByName("AutoBuilder").setEnabled(com.ersin.legitbridge.config.ModConfig.autoBuilder);
        if (getModuleByName("NoRender") != null) getModuleByName("NoRender").setEnabled(com.ersin.legitbridge.config.ModConfig.noRender);
    }

    public void onTick() {
        syncWithConfig();
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onTick();
            }
        }
    }
}
