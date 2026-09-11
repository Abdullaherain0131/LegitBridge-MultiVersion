package com.ersin.legitbridge.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    public static boolean enabled = false;
    public static boolean panicMode = false;
    public static int minDelayTick = 1;
    public static int maxDelayTick = 3;
    public static double minEdgeOffset = 0.05;
    public static double maxEdgeOffset = 0.15;
    public static boolean autoSupply = true;
    public static boolean directionalAssist = true;
    public static boolean safeWalk = true;
    public static boolean instaHouse = false;
    public static boolean voidSave = true;
    public static int voidSaveSpeed = 3; // Blocks per tick
    
    // FastClick settings
    public static boolean fastClick = true;
    public static int minFastClickDelay = 1;
    public static int maxFastClickDelay = 3;
    public static boolean ninjaBridge = false;

    public static boolean autoBed = false;
    public static boolean fakeLag = false;
    public static boolean derp = false;
    
    // Advanced Settings
    public static boolean hudEnabled = true;
    public static boolean blockCounter = true;
    public static boolean triggerBot = false;
    public static boolean autoTool = true;
    public static boolean noRender = false;
    public static boolean entityCulling = true; // Varsayılan açık, çünkü çok FPS artırır
    public static boolean slotRotation = false;
    public static boolean reachGuard = true;
    public static String currentProfile = "legit";

    // Jump Bridge & Staircase Settings
    public static boolean jumpBridge = true;
    public static boolean staircaseAssist = true;
    public static boolean legitScaffold = false;

    // Advanced Combat
    public static boolean combatAssist = false;
    public static boolean autoLock = false;
    public static boolean autoWeapon = false;
    public static boolean autoPot = false;
    public static boolean perfectCrit = false;
    public static boolean bowAimbot = false;
    public static boolean legitWTap = false;
    public static boolean autoRod = false;
    public static boolean killaura = false;
    public static boolean velocity = false;
    public static boolean fly = false;
    public static boolean noFall = false;
    public static boolean speed = false;
    public static boolean fastPlace = false;
    public static boolean tracers = false;
    public static boolean xray = false;
    public static boolean fpsBooster = false;
    public static boolean memoryCleaner = false;
    public static boolean autoBuilder = false;
    
    // AutoCrystal Settings
    public static boolean legitAutoCrystal = false;
    public static int crystalPlaceDelayMin = 1;
    public static int crystalPlaceDelayMax = 3;
    public static int crystalBreakDelayMin = 1;
    public static int crystalBreakDelayMax = 4;
    
    // Movement
    public static boolean hitBoxExpander = false;
    public static boolean step = false;
    public static float hitBoxExpanderSize = 0.1f;
    public static boolean fireballAssist = true;
    public static boolean autoFireballAngle = true;
    public static boolean autoBlockDefense = false;
    
    // Survival & Movement
    public static boolean autoEat = false;
    public static boolean autoFish = false;
    public static boolean invMove = false;
    public static boolean autoArmor = false;
    public static boolean legitChestStealer = false;
    public static boolean autoRefill = false;
    public static boolean legitAutoClicker = false;
    
    // Visuals & QoL
    public static boolean fullbright = false;
    public static boolean noHurtCam = false;
    public static boolean trueSight = false;
    public static boolean cleanScreen = false;
    public static boolean smartESP = false;
    public static boolean playerESP = false;
    public static boolean blockOverlay = false;
    public static boolean streamerMode = false;
    public static boolean waypoints = false;
    
    // Keybinds (Map of Feature Name -> GLFW Key Code)
    public static java.util.Map<String, Integer> keyBinds = new java.util.HashMap<>();

    // QoL, Themes, Audio & Positions
    public static boolean soundEnabled = true;
    public static String soundType = "CLICK"; // CLICK, PLING, LEVELUP
    public static boolean showCps = true;
    public static boolean showCombatPanel = true;
    public static boolean showFireballGuide = true;
    
    // Ultimate PvP Features
    public static boolean showPearlTrajectory = true;
    public static boolean showBedBreakMeter = true;
    public static boolean invisTracker = true;
    public static boolean showBlockHitSync = true;
    public static boolean showKbTrajectory = true;
    public static boolean showMlgCountdown = true;
    public static boolean showReachMeter = true;
    public static boolean showKnockbackVector = false;
    public static boolean showHitboxCenter = false;
    public static boolean reachBorderAlert = true;
    public static boolean showStructureFinder = true;
    public static boolean storageESP = false;
    
    // Survival & Hardcore
    public static boolean autoDisconnect = false;
    public static boolean autoTotem = false;
    public static boolean jumpReset = false;
    public static boolean autoElytra = false;
    public static boolean durabilityWarning = true;
    public static boolean autoElytraSwap = false;
    public static boolean autoMlg = false;
    public static boolean smartTorch = false;
    public static boolean lavaEvader = false;
    public static float autoDisconnectHealth = 4.0f; // 2 kalps
    public static boolean wtapIndicator = false;
    public static boolean comboDistance = false;
    public static boolean projectileWarning = true;
    public static boolean autoDodge = false;
    public static boolean freecam = false;
    public static boolean fireballPredictor = false;
    public static String targetBlockId = "";
    
    public static int hudX = 10;
    public static int hudY = 10;
    public static String theme = "CYAN"; // CYAN, EMERALD, DARK, ORANGE
    public static int toggleKeyCode = GLFW.GLFW_KEY_B;
    public static int menuKeyCode = GLFW.GLFW_KEY_RIGHT_SHIFT;

    private static final File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "legitbridge.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static int getThemePrimaryColor() {
        switch (theme) {
            case "EMERALD": return 0x00E676;
            case "DARK": return 0xAAAAAA;
            case "ORANGE": return 0xFF9100;
            case "CYAN":
            default: return 0x00E5FF;
        }
    }

    public static String getThemePrefix() {
        switch (theme) {
            case "EMERALD": return "§a";
            case "DARK": return "§7";
            case "ORANGE": return "§6";
            case "CYAN":
            default: return "§b";
        }
    }

    public static void load() {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                ConfigData data = gson.fromJson(reader, ConfigData.class);
                if (data != null) {
                    if (data.panicMode != null) panicMode = data.panicMode;
                    if (panicMode) enabled = false; // Panic modundaysa açılmasın
                    else if (data.enabled != null) enabled = data.enabled;
                    
                    if (data.minDelayTick != null) minDelayTick = data.minDelayTick;
                    if (data.maxDelayTick != null) maxDelayTick = data.maxDelayTick;
                    if (data.minEdgeOffset != null) minEdgeOffset = data.minEdgeOffset;
                    if (data.maxEdgeOffset != null) maxEdgeOffset = data.maxEdgeOffset;
                    if (data.autoSupply != null) autoSupply = data.autoSupply;
                    if (data.directionalAssist != null) directionalAssist = data.directionalAssist;
                    if (data.safeWalk != null) safeWalk = data.safeWalk;
                    if (data.instaHouse != null) instaHouse = data.instaHouse;
                    if (data.voidSave != null) voidSave = data.voidSave;
                    if (data.voidSaveSpeed != null) voidSaveSpeed = data.voidSaveSpeed;
                    if (data.fastClick != null) fastClick = data.fastClick;
                    if (data.minFastClickDelay != null) minFastClickDelay = data.minFastClickDelay;
                    if (data.maxFastClickDelay != null) maxFastClickDelay = data.maxFastClickDelay;
                    if (data.hudEnabled != null) hudEnabled = data.hudEnabled;
                    if (data.blockCounter != null) blockCounter = data.blockCounter;
                    if (data.triggerBot != null) triggerBot = data.triggerBot;
                    if (data.autoTool != null) autoTool = data.autoTool;
                    if (data.slotRotation != null) slotRotation = data.slotRotation;
                    if (data.reachGuard != null) reachGuard = data.reachGuard;
                    if (data.jumpBridge != null) jumpBridge = data.jumpBridge;
                    if (data.staircaseAssist != null) staircaseAssist = data.staircaseAssist;
                    if (data.legitScaffold != null) legitScaffold = data.legitScaffold;
                    if (data.ninjaBridge != null) ninjaBridge = data.ninjaBridge;
                    if (data.combatAssist != null) combatAssist = data.combatAssist;
                    if (data.fireballAssist != null) fireballAssist = data.fireballAssist;
                    if (data.autoFireballAngle != null) autoFireballAngle = data.autoFireballAngle;
                    if (data.autoBlockDefense != null) autoBlockDefense = data.autoBlockDefense;
                    if (data.autoTotem != null) autoTotem = data.autoTotem;
                    if (data.jumpReset != null) jumpReset = data.jumpReset;
                    if (data.autoElytra != null) autoElytra = data.autoElytra;
                    if (data.soundEnabled != null) soundEnabled = data.soundEnabled;
                    if (data.soundType != null) soundType = data.soundType;
                    if (data.showCps != null) showCps = data.showCps;
                    if (data.showCombatPanel != null) showCombatPanel = data.showCombatPanel;
                    if (data.showFireballGuide != null) showFireballGuide = data.showFireballGuide;
                    if (data.showPearlTrajectory != null) showPearlTrajectory = data.showPearlTrajectory;
                    if (data.showBedBreakMeter != null) showBedBreakMeter = data.showBedBreakMeter;
                    if (data.invisTracker != null) invisTracker = data.invisTracker;
                    if (data.showBlockHitSync != null) showBlockHitSync = data.showBlockHitSync;
                    if (data.showKbTrajectory != null) showKbTrajectory = data.showKbTrajectory;
                    if (data.showMlgCountdown != null) showMlgCountdown = data.showMlgCountdown;
                    if (data.showReachMeter != null) showReachMeter = data.showReachMeter;
                    if (data.showKnockbackVector != null) showKnockbackVector = data.showKnockbackVector;
                    if (data.showHitboxCenter != null) showHitboxCenter = data.showHitboxCenter;
                    if (data.reachBorderAlert != null) reachBorderAlert = data.reachBorderAlert;
                    if (data.wtapIndicator != null) wtapIndicator = data.wtapIndicator;
                    if (data.comboDistance != null) comboDistance = data.comboDistance;
                    if (data.projectileWarning != null) projectileWarning = data.projectileWarning;
                    if (data.autoDodge != null) autoDodge = data.autoDodge;
                    if (data.fireballPredictor != null) fireballPredictor = data.fireballPredictor;
                    if (data.showStructureFinder != null) showStructureFinder = data.showStructureFinder;
                    if (data.fullbright != null) fullbright = data.fullbright;
                    if (data.storageESP != null) storageESP = data.storageESP;
                    if (data.autoEat != null) autoEat = data.autoEat;
                    if (data.autoFish != null) autoFish = data.autoFish;
                    if (data.invMove != null) invMove = data.invMove;
                    if (data.autoArmor != null) autoArmor = data.autoArmor;
                    if (data.legitChestStealer != null) legitChestStealer = data.legitChestStealer;
                    if (data.autoRefill != null) autoRefill = data.autoRefill;
                    if (data.legitAutoClicker != null) legitAutoClicker = data.legitAutoClicker;
                    if (data.noHurtCam != null) noHurtCam = data.noHurtCam;
                    if (data.trueSight != null) trueSight = data.trueSight;
                    if (data.cleanScreen != null) cleanScreen = data.cleanScreen;
                    if (data.smartESP != null) smartESP = data.smartESP;
                    if (data.playerESP != null) playerESP = data.playerESP;
                    if (data.blockOverlay != null) blockOverlay = data.blockOverlay;
                    if (data.streamerMode != null) streamerMode = data.streamerMode;
                    if (data.waypoints != null) waypoints = data.waypoints;
                    if (data.durabilityWarning != null) durabilityWarning = data.durabilityWarning;
                    if (data.autoElytraSwap != null) autoElytraSwap = data.autoElytraSwap;
                    if (data.autoMlg != null) autoMlg = data.autoMlg;
                    if (data.smartTorch != null) smartTorch = data.smartTorch;
                    if (data.lavaEvader != null) lavaEvader = data.lavaEvader;
                    if (data.autoDisconnectHealth != null) autoDisconnectHealth = data.autoDisconnectHealth;
                    if (data.freecam != null) freecam = data.freecam;
                    if (data.hudX != null) hudX = data.hudX;
                    if (data.hudY != null) hudY = data.hudY;
                    if (data.theme != null) theme = data.theme;
                    if (data.toggleKeyCode != null && data.toggleKeyCode != 0) toggleKeyCode = data.toggleKeyCode;
                    if (data.menuKeyCode != null && data.menuKeyCode != 0) menuKeyCode = data.menuKeyCode;
                    if (data.currentProfile != null) currentProfile = data.currentProfile;
                    if (data.keyBinds != null) {
                        keyBinds = data.keyBinds;
                    }
                    if (!keyBinds.containsKey("freecam")) {
                        keyBinds.put("freecam", GLFW.GLFW_KEY_V);
                    }
                    if (data.autoLock != null) autoLock = data.autoLock;
                    if (data.autoWeapon != null) autoWeapon = data.autoWeapon;
                    if (data.autoPot != null) autoPot = data.autoPot;
                    if (data.perfectCrit != null) perfectCrit = data.perfectCrit;
                    if (data.bowAimbot != null) bowAimbot = data.bowAimbot;
                    if (data.legitWTap != null) legitWTap = data.legitWTap;
                    if (data.autoRod != null) autoRod = data.autoRod;
                    if (data.killaura != null) killaura = data.killaura;
                    if (data.velocity != null) velocity = data.velocity;
                    if (data.fly != null) fly = data.fly;
                    if (data.noFall != null) noFall = data.noFall;
                    if (data.speed != null) speed = data.speed;
                    if (data.fastPlace != null) fastPlace = data.fastPlace;
                    if (data.tracers != null) tracers = data.tracers;
                    if (data.xray != null) xray = data.xray;
                    if (data.fpsBooster != null) fpsBooster = data.fpsBooster;
                    if (data.memoryCleaner != null) memoryCleaner = data.memoryCleaner;
                    if (data.autoBuilder != null) autoBuilder = data.autoBuilder;
                    if (data.noRender != null) noRender = data.noRender;
                    if (data.entityCulling != null) entityCulling = data.entityCulling;

                    if (data.legitAutoCrystal != null) legitAutoCrystal = data.legitAutoCrystal;
                    if (data.crystalPlaceDelayMin != null) crystalPlaceDelayMin = data.crystalPlaceDelayMin;
                    if (data.crystalPlaceDelayMax != null) crystalPlaceDelayMax = data.crystalPlaceDelayMax;
                    if (data.crystalBreakDelayMin != null) crystalBreakDelayMin = data.crystalBreakDelayMin;
                    if (data.crystalBreakDelayMax != null) crystalBreakDelayMax = data.crystalBreakDelayMax;

                    if (data.hitBoxExpander != null) hitBoxExpander = data.hitBoxExpander;
                    if (data.step != null) step = data.step;
                    if (data.hitBoxExpanderSize != null) hitBoxExpanderSize = data.hitBoxExpanderSize;
                    if (data.targetBlockId != null) targetBlockId = data.targetBlockId;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            ConfigData data = new ConfigData();
            data.enabled = enabled && !panicMode;
            data.panicMode = panicMode;
            data.minDelayTick = minDelayTick;
            data.maxDelayTick = maxDelayTick;
            data.minEdgeOffset = minEdgeOffset;
            data.maxEdgeOffset = maxEdgeOffset;
            data.autoSupply = autoSupply;
            data.directionalAssist = directionalAssist;
            data.safeWalk = safeWalk;
            data.instaHouse = instaHouse;
            data.voidSave = voidSave;
            data.voidSaveSpeed = voidSaveSpeed;
            data.fastClick = fastClick;
            data.minFastClickDelay = minFastClickDelay;
            data.maxFastClickDelay = maxFastClickDelay;
            data.hudEnabled = hudEnabled;
            data.blockCounter = blockCounter;
            data.triggerBot = triggerBot;
            data.autoTool = autoTool;
            data.slotRotation = slotRotation;
            data.reachGuard = reachGuard;
            data.jumpBridge = jumpBridge;
            data.staircaseAssist = staircaseAssist;
            data.legitScaffold = legitScaffold;
            data.ninjaBridge = ninjaBridge;
            data.combatAssist = combatAssist;
            data.fireballAssist = fireballAssist;
            data.autoFireballAngle = autoFireballAngle;
            data.autoBlockDefense = autoBlockDefense;
            data.autoTotem = autoTotem;
            data.jumpReset = jumpReset;
            data.autoElytra = autoElytra;
            data.soundEnabled = soundEnabled;
            data.soundType = soundType;
            data.showCps = showCps;
            data.showCombatPanel = showCombatPanel;
            data.showFireballGuide = showFireballGuide;
            data.showPearlTrajectory = showPearlTrajectory;
            data.showBedBreakMeter = showBedBreakMeter;
            data.invisTracker = invisTracker;
            data.showBlockHitSync = showBlockHitSync;
            data.showKbTrajectory = showKbTrajectory;
            data.showMlgCountdown = showMlgCountdown;
            data.showReachMeter = showReachMeter;
            data.showKnockbackVector = showKnockbackVector;
            data.showHitboxCenter = showHitboxCenter;
            data.reachBorderAlert = reachBorderAlert;
            data.wtapIndicator = wtapIndicator;
            data.comboDistance = comboDistance;
            data.projectileWarning = projectileWarning;
            data.autoDodge = autoDodge;
            data.fireballPredictor = fireballPredictor;
            data.showStructureFinder = showStructureFinder;
            data.fullbright = fullbright;
            data.storageESP = storageESP;
            data.autoEat = autoEat;
            data.autoFish = autoFish;
            data.invMove = invMove;
            data.autoArmor = autoArmor;
            data.legitChestStealer = legitChestStealer;
            data.autoRefill = autoRefill;
            data.legitAutoClicker = legitAutoClicker;
            data.noHurtCam = noHurtCam;
            data.trueSight = trueSight;
            data.cleanScreen = cleanScreen;
            data.smartESP = smartESP;
            data.playerESP = playerESP;
            data.blockOverlay = blockOverlay;
            data.streamerMode = streamerMode;
            data.waypoints = waypoints;
            data.durabilityWarning = durabilityWarning;
            data.autoElytraSwap = autoElytraSwap;
            data.autoMlg = autoMlg;
            data.smartTorch = smartTorch;
            data.lavaEvader = lavaEvader;
            data.autoDisconnectHealth = autoDisconnectHealth;
            data.hudX = hudX;
            data.hudY = hudY;
            data.theme = theme;
            data.toggleKeyCode = toggleKeyCode;
            data.menuKeyCode = menuKeyCode;
            data.currentProfile = currentProfile;
            data.keyBinds = keyBinds;
            data.autoLock = autoLock;
            data.autoWeapon = autoWeapon;
            data.autoPot = autoPot;
            data.perfectCrit = perfectCrit;
            data.bowAimbot = bowAimbot;
            data.legitWTap = legitWTap;
            data.autoRod = autoRod;
            data.killaura = killaura;
            data.velocity = velocity;
            data.fly = fly;
            data.noFall = noFall;
            data.speed = speed;
            data.fastPlace = fastPlace;
            data.tracers = tracers;
            data.xray = xray;
            data.fpsBooster = fpsBooster;
            data.memoryCleaner = memoryCleaner;
            data.autoBuilder = autoBuilder;
            data.noRender = noRender;
            data.entityCulling = entityCulling;
            
            data.legitAutoCrystal = legitAutoCrystal;
            data.crystalPlaceDelayMin = crystalPlaceDelayMin;
            data.crystalPlaceDelayMax = crystalPlaceDelayMax;
            data.crystalBreakDelayMin = crystalBreakDelayMin;
            data.crystalBreakDelayMax = crystalBreakDelayMax;

            data.hitBoxExpander = hitBoxExpander;
            data.step = step;
            data.hitBoxExpanderSize = hitBoxExpanderSize;
            data.targetBlockId = targetBlockId;
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean applyProfile(String profileName) {
        if (profileName.equalsIgnoreCase("legit") || profileName.equalsIgnoreCase("casual")) {
            currentProfile = "casual";
            minDelayTick = 2;
            maxDelayTick = 4;
            minFastClickDelay = 2;
            maxFastClickDelay = 4;
            minEdgeOffset = 0.08;
            maxEdgeOffset = 0.15;
            reachGuard = true;
            triggerBot = false;
            jumpBridge = true;
            staircaseAssist = true;
            save();
            return true;
        } else if (profileName.equalsIgnoreCase("bedwars")) {
            currentProfile = "bedwars";
            minDelayTick = 1;
            maxDelayTick = 2;
            minFastClickDelay = 1;
            maxFastClickDelay = 2;
            minEdgeOffset = 0.05;
            maxEdgeOffset = 0.12;
            reachGuard = true;
            triggerBot = false;
            jumpBridge = true;
            staircaseAssist = true;
            save();
            return true;
        } else if (profileName.equalsIgnoreCase("skywars")) {
            currentProfile = "skywars";
            minDelayTick = 1;
            maxDelayTick = 2;
            minFastClickDelay = 1;
            maxFastClickDelay = 2;
            minEdgeOffset = 0.04;
            maxEdgeOffset = 0.10;
            reachGuard = true;
            triggerBot = true;
            jumpBridge = true;
            staircaseAssist = true;
            save();
            return true;
        }
        return false;
    }

    private static class ConfigData {
        public Boolean enabled;
        public Boolean panicMode;
        public Integer minDelayTick;
        public Integer maxDelayTick;
        public Double minEdgeOffset;
        public Double maxEdgeOffset;
        public Boolean autoSupply;
        public Boolean directionalAssist;
        public Boolean safeWalk;
        public Boolean instaHouse;
        public Boolean voidSave;
        public Integer voidSaveSpeed;
        public Boolean fastClick;
        public Integer minFastClickDelay;
        public Integer maxFastClickDelay;
        public Boolean ninjaBridge;
        public Boolean hudEnabled;
        public Boolean blockCounter;
        public Boolean triggerBot;
        public Boolean autoTool;
        public Boolean slotRotation;
        public Boolean reachGuard;
        public String currentProfile;
        public Boolean jumpBridge;
        public Boolean staircaseAssist;
        public Boolean legitScaffold;
        public Boolean autoLock;
        public Boolean autoWeapon;
        public Boolean autoPot;
        public Boolean perfectCrit;
        public Boolean bowAimbot;
        public Boolean legitWTap;
        public Boolean autoRod;
        public Boolean killaura;
        public Boolean velocity;
        public Boolean fly;
        public Boolean noFall;
        public Boolean speed;
        public Boolean fastPlace;
        public Boolean tracers;
        public Boolean xray;
        public Boolean fpsBooster;
        public Boolean memoryCleaner;
        public Boolean autoBuilder;
        public Boolean noRender;
        public Boolean entityCulling;
        
        public Boolean legitAutoCrystal;
        public Integer crystalPlaceDelayMin;
        public Integer crystalPlaceDelayMax;
        public Integer crystalBreakDelayMin;
        public Integer crystalBreakDelayMax;

        public Boolean hitBoxExpander;
        public Boolean step;
        public Float hitBoxExpanderSize;
        public Boolean combatAssist;
        public Boolean fireballAssist;
        public Boolean autoFireballAngle;
        public Boolean autoBlockDefense;
        public Boolean autoTotem;
        public Boolean jumpReset;
        public Boolean autoElytra;
        public java.util.Map<String, Integer> keyBinds;
        public Boolean soundEnabled;
        public String soundType;
        public Boolean showCps;
        public Boolean showCombatPanel;
        public Boolean showFireballGuide;
        public Boolean showPearlTrajectory;
        public Boolean showBedBreakMeter;
        public Boolean invisTracker;
        public Boolean showBlockHitSync;
        public Boolean showKbTrajectory;
        public Boolean showMlgCountdown;
        public Boolean showReachMeter;
        public Boolean showKnockbackVector;
        public Boolean showHitboxCenter;
        public Boolean reachBorderAlert;
        public Boolean wtapIndicator;
        public Boolean comboDistance;
        public Boolean projectileWarning;
        public Boolean autoDodge;
        public Boolean showStructureFinder;
        public Boolean freecam;
        public Boolean fireballPredictor;
        public Boolean fullbright;
        public Boolean storageESP;
        public Boolean autoEat;
        public Boolean autoFish;
        public Boolean invMove;
        public Boolean autoArmor;
        public Boolean legitChestStealer;
        public Boolean autoRefill;
        public Boolean legitAutoClicker;
        public Boolean noHurtCam;
        public Boolean trueSight;
        public Boolean cleanScreen;
        public Boolean smartESP;
        public Boolean playerESP;
        public Boolean blockOverlay;
        public Boolean streamerMode;
        public Boolean waypoints;
        public Boolean durabilityWarning;
        public Boolean autoElytraSwap;
        public Boolean autoMlg;
        public Boolean smartTorch;
        public Boolean lavaEvader;
        public Float autoDisconnectHealth;
        public Integer hudX;
        public Integer hudY;
        public String theme;
        public Integer toggleKeyCode;
        public Integer menuKeyCode;
        public String targetBlockId;
    }
}
