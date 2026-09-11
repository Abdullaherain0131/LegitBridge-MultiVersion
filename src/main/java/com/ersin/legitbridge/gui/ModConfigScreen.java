package com.ersin.legitbridge.gui;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.utils.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.systems.RenderSystem;


import java.util.function.Supplier;

public class ModConfigScreen extends Screen {
    //? if >1.19.2 {
    private static net.minecraft.text.Text text(String s) { return com.ersin.legitbridge.utils.VersionHelper.literalText(s); }
    //?} else {
    /*private static net.minecraft.text.Text text(String s) { return new net.minecraft.text.LiteralText(s); }
    *///?}


    private boolean draggingHud = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    
    private int currentTab = 0; // 0: Savaş, 1: Hareket, 2: Survival, 3: Görsel
    private String waitingForKeybind = null;
    private TextFieldWidget blockInputField;
    
    private double scrollTarget = 0;
    private double scrollCurrent = 0;
    private int maxScroll = 0;

    public ModConfigScreen() {
        super(text("LegitBridge Ayar Menüsü"));
    }

    public static int getThemeColorHex() {
        switch (ModConfig.theme) {
            case "EMERALD": return 0xFF55FF55;
            case "DARK": return 0xFFAAAAAA;
            case "ORANGE": return 0xFFFFAA00;
            default: return 0xFF55FFFF; // CYAN
        }
    }

    private void addCustomButton(ButtonWidget button) {
        //? if >1.19.2 {
        this.addDrawableChild(button);
        //?} else {
        /*this.addButton(button);
        *///?}
    }
    
    private Iterable<?> getWidgetList() {
        return this.children();

    }

    @Override
    protected void init() {
//? if <=1.19.2 {
/*        this.children.clear();
*///?} else {
        this.clearChildren();
//?}

        this.blockInputField = null;
        
        scrollTarget = 0;
        scrollCurrent = 0;
        
        int centerX = this.width / 2;
        int buttonWidth = 140;
        int buttonHeight = 22;
        int gap = 26;
        
        String color = ModConfig.getThemePrefix();

        // ------------------
        // TABS
        // ------------------
        int tabY = 25;
        int tabW = 85;
        this.addCustomButton(new TabButton(centerX - 215, tabY, tabW, 20, "Savaş/PvP", 0));
        this.addCustomButton(new TabButton(centerX - 125, tabY, tabW, 20, "Hareket/Parkour", 1));
        this.addCustomButton(new TabButton(centerX - 35, tabY, tabW, 20, "Survival", 2));
        this.addCustomButton(new TabButton(centerX + 55, tabY, tabW, 20, "Görsel/HUD", 3));
        this.addCustomButton(new TabButton(centerX + 145, tabY, tabW, 20, "QoL/Yardımcı", 4));

        // ------------------
        // TOP CONTROLS (Theme, Profile, Sound)
        // ------------------
        int topY = 55;
        this.addCustomButton(new SimpleButton(centerX - 215, topY, 100, 20, () -> "Mod: " + (ModConfig.enabled ? "§aAÇIK" : "§cKAPALI"), () -> {
            if (ModConfig.panicMode) return; // Panik modundayken mod açılamaz
            ModConfig.enabled = !ModConfig.enabled; ModConfig.save(); playConfigSound(); this.init();
        }));
        
        this.addCustomButton(new SimpleButton(centerX - 105, topY, 100, 20, () -> "Tema: " + color + ModConfig.theme, () -> {
            switch (ModConfig.theme) {
                case "CYAN": ModConfig.theme = "EMERALD"; break;
                case "EMERALD": ModConfig.theme = "DARK"; break;
                case "DARK": ModConfig.theme = "ORANGE"; break;
                default: ModConfig.theme = "CYAN"; break;
            }
            ModConfig.save(); playConfigSound(); this.init();
        }));
        
        this.addCustomButton(new SimpleButton(centerX + 5, topY, 100, 20, () -> "Profil: " + color + ModConfig.currentProfile.toUpperCase(), () -> {
            if (ModConfig.currentProfile.equalsIgnoreCase("casual")) ModConfig.applyProfile("bedwars");
            else if (ModConfig.currentProfile.equalsIgnoreCase("bedwars")) ModConfig.applyProfile("skywars");
            else ModConfig.applyProfile("casual");
            playConfigSound(); this.init();
        }));
        
        this.addCustomButton(new SimpleButton(centerX + 115, topY, 80, 20, () -> "Ses: " + color + ModConfig.soundType, () -> {
            switch (ModConfig.soundType) {
                case "CLICK": ModConfig.soundType = "PLING"; break;
                case "PLING": ModConfig.soundType = "LEVELUP"; break;
                default: ModConfig.soundType = "CLICK"; break;
            }
            ModConfig.save(); playConfigSound(); this.init();
        }));
        
        this.addCustomButton(new SimpleButton(centerX + 200, topY, 70, 20, () -> ModConfig.panicMode ? "§4§lPANİK" : "§cPanik Mod", () -> {
            ModConfig.panicMode = !ModConfig.panicMode;
            if (ModConfig.panicMode) ModConfig.enabled = false;
            ModConfig.save(); playConfigSound(); this.init();
        }));

        int startY = 85;
        int col1X = centerX - buttonWidth - 10;
        int col2X = centerX + 10;

        // TAB 0: Savaş/PvP
        if (currentTab == 0) {
            this.addCustomButton(new ConfigButton(col1X, startY, buttonWidth, buttonHeight, "combatAssist", "Oto Nişan", () -> ModConfig.combatAssist, () -> ModConfig.combatAssist = !ModConfig.combatAssist));
            this.addCustomButton(new ConfigButton(col1X, startY + gap, buttonWidth, buttonHeight, "autoLock", "Oto Kilitlenme", () -> ModConfig.autoLock, () -> ModConfig.autoLock = !ModConfig.autoLock));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 2, buttonWidth, buttonHeight, "autoWeapon", "Oto-Kılıç", () -> ModConfig.autoWeapon, () -> ModConfig.autoWeapon = !ModConfig.autoWeapon));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 3, buttonWidth, buttonHeight, "perfectCrit", "Oto-Kritik", () -> ModConfig.perfectCrit, () -> ModConfig.perfectCrit = !ModConfig.perfectCrit));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 4, buttonWidth, buttonHeight, "fireballAssist", "Fireball Destek", () -> ModConfig.fireballAssist, () -> ModConfig.fireballAssist = !ModConfig.fireballAssist));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 5, buttonWidth, buttonHeight, "autoFireballAngle", "Oto FB Açı", () -> ModConfig.autoFireballAngle, () -> ModConfig.autoFireballAngle = !ModConfig.autoFireballAngle));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 6, buttonWidth, buttonHeight, "legitAutoClicker", "Legit AutoClicker", () -> ModConfig.legitAutoClicker, () -> ModConfig.legitAutoClicker = !ModConfig.legitAutoClicker));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 7, buttonWidth, buttonHeight, "autoRod", "Oto-Olta", () -> ModConfig.autoRod, () -> ModConfig.autoRod = !ModConfig.autoRod));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 8, buttonWidth, buttonHeight, "killaura", "Killaura", () -> ModConfig.killaura, () -> ModConfig.killaura = !ModConfig.killaura));
            
            this.addCustomButton(new ConfigButton(col2X, startY, buttonWidth, buttonHeight, "hitBoxExpander", "Geniş HitBox (Ghost)", () -> ModConfig.hitBoxExpander, () -> ModConfig.hitBoxExpander = !ModConfig.hitBoxExpander));
            this.addCustomButton(new ConfigButton(col2X, startY + gap, buttonWidth, buttonHeight, "bowAimbot", "Okçu Aimbot", () -> ModConfig.bowAimbot, () -> ModConfig.bowAimbot = !ModConfig.bowAimbot));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 2, buttonWidth, buttonHeight, "autoPot", "Oto-Pot/Çorba", () -> ModConfig.autoPot, () -> ModConfig.autoPot = !ModConfig.autoPot));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 3, buttonWidth, buttonHeight, "autoBlockDefense", "Oto Blok", () -> ModConfig.autoBlockDefense, () -> ModConfig.autoBlockDefense = !ModConfig.autoBlockDefense));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 4, buttonWidth, buttonHeight, "triggerBot", "TriggerBot", () -> ModConfig.triggerBot, () -> ModConfig.triggerBot = !ModConfig.triggerBot));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 5, buttonWidth, buttonHeight, "legitWTap", "Otomatik W-Tap", () -> ModConfig.legitWTap, () -> ModConfig.legitWTap = !ModConfig.legitWTap));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 6, buttonWidth, buttonHeight, "legitAutoCrystal", "Legit AutoCrystal", () -> ModConfig.legitAutoCrystal, () -> ModConfig.legitAutoCrystal = !ModConfig.legitAutoCrystal));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 7, buttonWidth, buttonHeight, "velocity", "Velocity (AntiKB)", () -> ModConfig.velocity, () -> ModConfig.velocity = !ModConfig.velocity));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 8, buttonWidth, buttonHeight, "autoDodge", "Oto Dodge", () -> ModConfig.autoDodge, () -> ModConfig.autoDodge = !ModConfig.autoDodge));
            
            maxScroll = Math.max(0, (startY + gap * 9) - (this.height - 40));
        }
        
        // TAB 1: Hareket/Parkour
        else if (currentTab == 1) {
            this.addCustomButton(new ConfigButton(col1X, startY, buttonWidth, buttonHeight, "safeWalk", "SafeWalk", () -> ModConfig.safeWalk, () -> ModConfig.safeWalk = !ModConfig.safeWalk));
            this.addCustomButton(new ConfigButton(col1X, startY + gap, buttonWidth, buttonHeight, "jumpBridge", "Zıplama Destek", () -> ModConfig.jumpBridge, () -> ModConfig.jumpBridge = !ModConfig.jumpBridge));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 2, buttonWidth, buttonHeight, "legitScaffold", "Oto Köprü", () -> ModConfig.legitScaffold, () -> ModConfig.legitScaffold = !ModConfig.legitScaffold));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 3, buttonWidth, buttonHeight, "ninjaBridge", "Ninja Bridge", () -> ModConfig.ninjaBridge, () -> ModConfig.ninjaBridge = !ModConfig.ninjaBridge));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 4, buttonWidth, buttonHeight, "step", "Step (Örümcek)", () -> ModConfig.step, () -> ModConfig.step = !ModConfig.step));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 5, buttonWidth, buttonHeight, "fly", "Fly (Uçma)", () -> ModConfig.fly, () -> ModConfig.fly = !ModConfig.fly));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 6, buttonWidth, buttonHeight, "speed", "Speed (Hız)", () -> ModConfig.speed, () -> ModConfig.speed = !ModConfig.speed));
            
            this.addCustomButton(new ConfigButton(col2X, startY, buttonWidth, buttonHeight, "jumpReset", "Jump Reset (Anti-KB)", () -> ModConfig.jumpReset, () -> ModConfig.jumpReset = !ModConfig.jumpReset));
            this.addCustomButton(new ConfigButton(col2X, startY + gap, buttonWidth, buttonHeight, "autoElytra", "Oto-Elytra", () -> ModConfig.autoElytra, () -> ModConfig.autoElytra = !ModConfig.autoElytra));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 2, buttonWidth, buttonHeight, "noFall", "NoFall (Hasarsız)", () -> ModConfig.noFall, () -> ModConfig.noFall = !ModConfig.noFall));
            
            maxScroll = Math.max(0, (startY + gap * 7) - (this.height - 40));
        }

        // TAB 2: Survival/Legit
        else if (currentTab == 2) {
            this.addCustomButton(new ConfigButton(col1X, startY, buttonWidth, buttonHeight, "fastClick", "FastClick", () -> ModConfig.fastClick, () -> ModConfig.fastClick = !ModConfig.fastClick));
            this.addCustomButton(new ConfigButton(col1X, startY + gap, buttonWidth, buttonHeight, "autoTool", "AutoTool", () -> ModConfig.autoTool, () -> ModConfig.autoTool = !ModConfig.autoTool));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 2, buttonWidth, buttonHeight, "autoSupply", "AutoSupply", () -> ModConfig.autoSupply, () -> ModConfig.autoSupply = !ModConfig.autoSupply));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 3, buttonWidth, buttonHeight, "autoFish", "Oto-Balık", () -> ModConfig.autoFish, () -> ModConfig.autoFish = !ModConfig.autoFish));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 4, buttonWidth, buttonHeight, "durabilityWarning", "Zırh Uyarısı", () -> ModConfig.durabilityWarning, () -> ModConfig.durabilityWarning = !ModConfig.durabilityWarning));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 5, buttonWidth, buttonHeight, "autoElytraSwap", "Oto-Elytra", () -> ModConfig.autoElytraSwap, () -> ModConfig.autoElytraSwap = !ModConfig.autoElytraSwap));
            
            this.addCustomButton(new ConfigButton(col2X, startY, buttonWidth, buttonHeight, "autoTotem", "Oto-Totem", () -> ModConfig.autoTotem, () -> ModConfig.autoTotem = !ModConfig.autoTotem));
            this.addCustomButton(new ConfigButton(col2X, startY + gap, buttonWidth, buttonHeight, "autoEat", "Oto-Yemek", () -> ModConfig.autoEat, () -> ModConfig.autoEat = !ModConfig.autoEat));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 2, buttonWidth, buttonHeight, "autoMlg", "Oto-MLG", () -> ModConfig.autoMlg, () -> ModConfig.autoMlg = !ModConfig.autoMlg));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 3, buttonWidth, buttonHeight, "smartTorch", "Akıllı Meşale", () -> ModConfig.smartTorch, () -> ModConfig.smartTorch = !ModConfig.smartTorch));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 4, buttonWidth, buttonHeight, "lavaEvader", "Anti-Lav", () -> ModConfig.lavaEvader, () -> ModConfig.lavaEvader = !ModConfig.lavaEvader));
            
            maxScroll = Math.max(0, (startY + gap * 6) - (this.height - 40));
        }

        // TAB 3: Görsel/HUD
        else if (currentTab == 3) {
            this.addCustomButton(new ConfigButton(col1X, startY, buttonWidth, buttonHeight, "hudEnabled", "HUD Çerçevesi", () -> ModConfig.hudEnabled, () -> ModConfig.hudEnabled = !ModConfig.hudEnabled));
            this.addCustomButton(new ConfigButton(col1X, startY + gap, buttonWidth, buttonHeight, "showCps", "CPS Göstergesi", () -> ModConfig.showCps, () -> ModConfig.showCps = !ModConfig.showCps));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 2, buttonWidth, buttonHeight, "showCombatPanel", "Savaş Paneli", () -> ModConfig.showCombatPanel, () -> ModConfig.showCombatPanel = !ModConfig.showCombatPanel));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 3, buttonWidth, buttonHeight, "showReachMeter", "Reach Metre", () -> ModConfig.showReachMeter, () -> ModConfig.showReachMeter = !ModConfig.showReachMeter));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 4, buttonWidth, buttonHeight, "showBlockHitSync", "Block-Hit Sync", () -> ModConfig.showBlockHitSync, () -> ModConfig.showBlockHitSync = !ModConfig.showBlockHitSync));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 5, buttonWidth, buttonHeight, "showHitboxCenter", "Hitbox Merkez Noktası", () -> ModConfig.showHitboxCenter, () -> ModConfig.showHitboxCenter = !ModConfig.showHitboxCenter));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 6, buttonWidth, buttonHeight, "reachBorderAlert", "Reach Sınır Uyarısı", () -> ModConfig.reachBorderAlert, () -> ModConfig.reachBorderAlert = !ModConfig.reachBorderAlert));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 7, buttonWidth, buttonHeight, "freecam", "Ruh Modu (Freecam)", () -> ModConfig.freecam, () -> ModConfig.freecam = !ModConfig.freecam));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 8, buttonWidth, buttonHeight, "fullbright", "Fullbright", () -> ModConfig.fullbright, () -> ModConfig.fullbright = !ModConfig.fullbright));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 9, buttonWidth, buttonHeight, "blockOverlay", "Block Overlay", () -> ModConfig.blockOverlay, () -> ModConfig.blockOverlay = !ModConfig.blockOverlay));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 10, buttonWidth, buttonHeight, "playerESP", "Oyuncu ESP (Chams)", () -> ModConfig.playerESP, () -> ModConfig.playerESP = !ModConfig.playerESP));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 11, buttonWidth, buttonHeight, "tracers", "Tracers (Çizgi ESP)", () -> ModConfig.tracers, () -> ModConfig.tracers = !ModConfig.tracers));

            this.addCustomButton(new ConfigButton(col2X, startY, buttonWidth, buttonHeight, "showFireballGuide", "Fireball Rehberi", () -> ModConfig.showFireballGuide, () -> ModConfig.showFireballGuide = !ModConfig.showFireballGuide));
            this.addCustomButton(new ConfigButton(col2X, startY + gap, buttonWidth, buttonHeight, "showPearlTrajectory", "EnderPearl Yörünge", () -> ModConfig.showPearlTrajectory, () -> ModConfig.showPearlTrajectory = !ModConfig.showPearlTrajectory));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 2, buttonWidth, buttonHeight, "showKbTrajectory", "TNT KB Çizgisi", () -> ModConfig.showKbTrajectory, () -> ModConfig.showKbTrajectory = !ModConfig.showKbTrajectory));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 3, buttonWidth, buttonHeight, "invisTracker", "Invis İz Sürücü", () -> ModConfig.invisTracker, () -> ModConfig.invisTracker = !ModConfig.invisTracker));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 4, buttonWidth, buttonHeight, "showMlgCountdown", "MLG Sayacı", () -> ModConfig.showMlgCountdown, () -> ModConfig.showMlgCountdown = !ModConfig.showMlgCountdown));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 5, buttonWidth, buttonHeight, "showKnockbackVector", "KB Vektör Çizgisi", () -> ModConfig.showKnockbackVector, () -> ModConfig.showKnockbackVector = !ModConfig.showKnockbackVector));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 6, buttonWidth, buttonHeight, "projectileWarning", "Mermi Dodge Uyarısı", () -> ModConfig.projectileWarning, () -> ModConfig.projectileWarning = !ModConfig.projectileWarning));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 7, buttonWidth, buttonHeight, "fireballPredictor", "FB Zıplama Açı Çizgisi", () -> ModConfig.fireballPredictor, () -> ModConfig.fireballPredictor = !ModConfig.fireballPredictor));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 8, buttonWidth, buttonHeight, "showStructureFinder", "Yapı Bulucu", () -> ModConfig.showStructureFinder, () -> ModConfig.showStructureFinder = !ModConfig.showStructureFinder));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 9, buttonWidth, buttonHeight, "storageESP", "Storage ESP", () -> ModConfig.storageESP, () -> ModConfig.storageESP = !ModConfig.storageESP));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 10, buttonWidth, buttonHeight, "smartESP", "Smart ESP (Rare Items)", () -> ModConfig.smartESP, () -> ModConfig.smartESP = !ModConfig.smartESP));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 11, buttonWidth, buttonHeight, "xray", "XRay (Madenler)", () -> ModConfig.xray, () -> ModConfig.xray = !ModConfig.xray));

            blockInputField = new TextFieldWidget(this.textRenderer, col2X, startY + gap * 12 + 4, buttonWidth, 18, text("Block ID"));
            
            blockInputField.setMaxLength(64);
            blockInputField.setText(ModConfig.targetBlockId != null ? ModConfig.targetBlockId : "");
            blockInputField.setChangedListener(text -> {
                ModConfig.targetBlockId = text;
                ModConfig.save();
            });
            //? if <=1.19.2 {
            /*this.children.add(blockInputField);
*///?} else {
            this.addDrawableChild(blockInputField);
//?}
            
            maxScroll = Math.max(0, (startY + gap * 13) - (this.height - 40));
        }

        // TAB 4: QoL/Yardımcı
        else if (currentTab == 4) {
            this.addCustomButton(new ConfigButton(col1X, startY, buttonWidth, buttonHeight, "invMove", "Envanter Hareketi", () -> ModConfig.invMove, () -> ModConfig.invMove = !ModConfig.invMove));
            this.addCustomButton(new ConfigButton(col1X, startY + gap, buttonWidth, buttonHeight, "autoArmor", "Oto-Zırh Giyici", () -> ModConfig.autoArmor, () -> ModConfig.autoArmor = !ModConfig.autoArmor));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 2, buttonWidth, buttonHeight, "legitChestStealer", "Chest Stealer", () -> ModConfig.legitChestStealer, () -> ModConfig.legitChestStealer = !ModConfig.legitChestStealer));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 3, buttonWidth, buttonHeight, "autoRefill", "Oto-Refill (Hotbar)", () -> ModConfig.autoRefill, () -> ModConfig.autoRefill = !ModConfig.autoRefill));
            this.addCustomButton(new ConfigButton(col1X, startY + gap * 4, buttonWidth, buttonHeight, "waypoints", "Waypoints", () -> ModConfig.waypoints, () -> ModConfig.waypoints = !ModConfig.waypoints));
            
            this.addCustomButton(new ConfigButton(col2X, startY, buttonWidth, buttonHeight, "noHurtCam", "NoHurtCam (Sarsıntısız)", () -> ModConfig.noHurtCam, () -> ModConfig.noHurtCam = !ModConfig.noHurtCam));
            this.addCustomButton(new ConfigButton(col2X, startY + gap, buttonWidth, buttonHeight, "fastPlace", "FastPlace", () -> ModConfig.fastPlace, () -> ModConfig.fastPlace = !ModConfig.fastPlace));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 2, buttonWidth, buttonHeight, "trueSight", "Gerçek Görüş", () -> ModConfig.trueSight, () -> ModConfig.trueSight = !ModConfig.trueSight));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 3, buttonWidth, buttonHeight, "cleanScreen", "Temiz Ekran (Efektsiz)", () -> ModConfig.cleanScreen, () -> ModConfig.cleanScreen = !ModConfig.cleanScreen));
            this.addCustomButton(new ConfigButton(col2X, startY + gap * 4, buttonWidth, buttonHeight, "streamerMode", "Yayıncı Modu", () -> ModConfig.streamerMode, () -> ModConfig.streamerMode = !ModConfig.streamerMode));
            
            maxScroll = Math.max(0, (startY + gap * 6) - (this.height - 40));
        }

        // Kapat Butonu
        this.addCustomButton(new SimpleButton(centerX - 50, this.height - 30, 100, 20, () -> "§cKapat", () -> {
            playConfigSound();
            if (this.client != null) {
                com.ersin.legitbridge.utils.VersionHelper.setScreen(this.client, null);
            }
        }));
    }

    public static void playConfigSound() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (ModConfig.soundEnabled && client != null && client.getSoundManager() != null) {
            //? if <=1.19.2 {
            /*SoundEvent event = SoundEvents.UI_BUTTON_CLICK;
*///?} else {
            SoundEvent event = SoundEvents.UI_BUTTON_CLICK.value();
//?}
            if (ModConfig.soundType.equals("PLING")) {
//? if <=1.19.2 {
                /*event = SoundEvents.BLOCK_NOTE_BLOCK_PLING;
*///?} else {
                event = SoundEvents.BLOCK_NOTE_BLOCK_PLING.value();
//?}
            }
            else if (ModConfig.soundType.equals("LEVELUP")) {
//? if <=1.19.2 {
                /*event = SoundEvents.ENTITY_PLAYER_LEVELUP;
*///?} else {
                event = SoundEvents.UI_BUTTON_CLICK.value();
//?}
            }
            client.getSoundManager().play(PositionedSoundInstance.master(event, 1.0F));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 2) { // Middle Click
            for (Object widget : getWidgetList()) {
                if (widget instanceof ConfigButton && ((ConfigButton) widget).isMouseOver(mouseX, mouseY)) {
                    waitingForKeybind = ((ConfigButton) widget).configKey;
                    playConfigSound();
                    return true;
                }
            }
        }

        int x = ModConfig.hudX;
        int y = ModConfig.hudY;
        
        int hudHeight = 15;
        if (ModConfig.hudEnabled) hudHeight += 11 * 3;
        if (ModConfig.enabled && ModConfig.blockCounter) hudHeight += 11;
        if (ModConfig.enabled && ModConfig.showCps) hudHeight += 11;
        if (ModConfig.enabled) hudHeight += 11;
        if (ModConfig.enabled && ModConfig.showCombatPanel) {
            hudHeight += 5 + (11 * 4);
            if (ModConfig.showBlockHitSync) hudHeight += 11;
        }

        if (mouseX >= x - 5 && mouseX <= x + 200 && mouseY >= y - 15 && mouseY <= y + hudHeight) {
            draggingHud = true;
            dragOffsetX = (int) mouseX - ModConfig.hudX;
            dragOffsetY = (int) mouseY - ModConfig.hudY;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (waitingForKeybind != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                ModConfig.keyBinds.remove(waitingForKeybind);
            } else {
                ModConfig.keyBinds.put(waitingForKeybind, keyCode);
            }
            ModConfig.save();
            waitingForKeybind = null;
            playConfigSound();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingHud) {
            draggingHud = false;
            ModConfig.save();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingHud) {
            ModConfig.hudX = Math.max(0, Math.min(this.width - 200, (int) mouseX - dragOffsetX));
            ModConfig.hudY = Math.max(15, Math.min(this.height - 100, (int) mouseY - dragOffsetY));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        scrollTarget -= amount * 24; // Scroll speed
        if (scrollTarget < 0) scrollTarget = 0;
        if (scrollTarget > maxScroll) scrollTarget = maxScroll;
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
//? if <=1.19.2 {
    /*public void render(net.minecraft.client.util.math.MatrixStack context, int mouseX, int mouseY, float delta) {
*///?} else {
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
//?}
        this.renderInternal(context, mouseX, mouseY, delta);
    }

    public void renderInternal(Object context, int mouseX, int mouseY, float delta) {
        // Modern blur/dark background
        //? if >1.19.2 {
        ((net.minecraft.client.gui.DrawContext)context).fillGradient(0, 0, this.width, this.height, 0xD0000000, 0xF0000000);
        //?} else {
        /*this.fillGradient((net.minecraft.client.util.math.MatrixStack)context, 0, 0, this.width, this.height, 0xD0000000, 0xF0000000);
        *///?}
        
        int centerX = this.width / 2;
        
        // Main Window Panel
        RenderUtils.fill(context, centerX - 230, 10, centerX + 230, this.height - 10, 0x50101010);
        // Border
        RenderUtils.fill(context, centerX - 230, 10, centerX + 230, 11, getThemeColorHex());
        RenderUtils.fill(context, centerX - 230, this.height - 11, centerX + 230, this.height - 10, getThemeColorHex());

        String color = ModConfig.getThemePrefix();
        RenderUtils.drawCenteredText(context, this.textRenderer, color + "§lLegitBridge §7- Modern Config", this.width / 2, 13, 0xFFFFFF);
        
        // Scroll bar
        if (maxScroll > 0) {
            int barHeight = (int) (((float) (this.height - 90) / (maxScroll + this.height - 90)) * (this.height - 90));
            int barY = 85 + (int) ((scrollCurrent / maxScroll) * (this.height - 90 - barHeight));
            RenderUtils.fill(context, centerX + 180, 85, centerX + 182, this.height - 15, 0x44FFFFFF);
            RenderUtils.fill(context, centerX + 180, barY, centerX + 182, barY + barHeight, getThemeColorHex());
        }
        
        // Scissor for button clipping
        double scale = this.client.getWindow().getScaleFactor();
        int scissorY = (int) (this.height - (this.height - 15)) * (int) scale; // bottom bound
        int scissorHeight = (int) (this.height - 95) * (int) scale; // height
        int scissorX = (int) (centerX - 225) * (int) scale;
        int scissorWidth = 450 * (int) scale;
        
        for (Object widget : getWidgetList()) {
            if (widget instanceof ConfigButton) {
                ConfigButton cb = (ConfigButton) widget;
                com.ersin.legitbridge.utils.VersionHelper.setWidgetY(cb, cb.baseY - (int) scrollCurrent);
            }
        }
        if (blockInputField != null) {
            int gap = 26;
            int startY = 85;
            com.ersin.legitbridge.utils.VersionHelper.setWidgetY(blockInputField, startY + gap * 11 + 4 - (int) scrollCurrent);
        }

        // Now render the scrollable area
        RenderSystem.enableScissor(scissorX, scissorY, scissorWidth, scissorHeight);
        
        if (currentTab == 3 && blockInputField != null) {
            RenderUtils.drawTextWithShadow(context, this.textRenderer, "Bulunacak Blok (Örn: minecraft:diamond_ore):", com.ersin.legitbridge.utils.VersionHelper.getWidgetX(blockInputField), com.ersin.legitbridge.utils.VersionHelper.getWidgetY(blockInputField) - 12, 0xAAAAAA);
            //? if >1.19.2 {
            blockInputField.render((net.minecraft.client.gui.DrawContext)context, mouseX, mouseY, delta);
            //?} else {
            /*blockInputField.render((net.minecraft.client.util.math.MatrixStack)context, mouseX, mouseY, delta);
            *///?}
        }
        
        for (Object widget : getWidgetList()) {
            if (widget instanceof ConfigButton) {
                //? if >1.19.2 {
                ((ButtonWidget) widget).render((net.minecraft.client.gui.DrawContext)context, mouseX, mouseY, delta);
                //?} else {
                /*((ButtonWidget) widget).render((net.minecraft.client.util.math.MatrixStack)context, mouseX, mouseY, delta);
                *///?}
            }
        }
        RenderSystem.disableScissor();
        
        // Render top/bottom buttons (which shouldn't scroll)
        for (Object widget : getWidgetList()) {
            if (!(widget instanceof ConfigButton)) {
                //? if >1.19.2 {
                ((ButtonWidget) widget).render((net.minecraft.client.gui.DrawContext)context, mouseX, mouseY, delta);
                //?} else {
                /*((ButtonWidget) widget).render((net.minecraft.client.util.math.MatrixStack)context, mouseX, mouseY, delta);
                *///?}
            }
        }
        
        if (waitingForKeybind != null) {
            RenderUtils.drawCenteredText(context, this.textRenderer, "§e§l" + waitingForKeybind + " §eiçin tuş bekleniyor... (İptal için ESC)", this.width / 2, this.height - 50, 0xFFFF00);
        } else {
            RenderUtils.drawCenteredText(context, this.textRenderer, "§7Tuş Atamak (Keybind) için butonun üzerindeyken §fOrta Tuşa (Tekerlek) §7tıkla.", this.width / 2, this.height - 42, 0x888888);
        }
    }

    @Override
//? if <=1.19.2 {
    /*public boolean isPauseScreen() {
*///?} else {
    public boolean shouldPause() {
//?}
        return false;
    }

    @Override
    public void tick() {
        if (blockInputField != null) blockInputField.tick();
        
        // Smooth scroll interpolation
        scrollCurrent += (scrollTarget - scrollCurrent) * 0.3;
        
        super.tick();
    }

    // ---------------------------------------------------------
    // CUSTOM UI WIDGETS
    // ---------------------------------------------------------

    class TabButton extends ButtonWidget {
        private final int tabIndex;
        private float hoverAnim = 0f;

        public TabButton(int x, int y, int width, int height, String message, int tabIndex) {
            //? if <=1.19.2 {
            /*super(x, y, width, height, text(message), button -> {});
*///?} else {
            super(x, y, width, height, text(message), button -> {}, net.minecraft.client.gui.widget.ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
//?}
            this.tabIndex = tabIndex;
        }

        @Override
        public void onPress() {
            if (currentTab != tabIndex) {
                currentTab = tabIndex;
                ModConfigScreen.this.init();
                playConfigSound();
            }
        }
        //? if <=1.19.2 {
        /*public void render(net.minecraft.client.util.math.MatrixStack matrices, int mouseX, int mouseY, float delta) {
    Object context = matrices;
*///?} else {
public void renderWidget(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
//?}
            boolean selected = (currentTab == tabIndex);
            boolean hovered = isMouseOver(mouseX, mouseY);
            
            float targetHover = hovered ? 1f : 0f;
            hoverAnim += (targetHover - hoverAnim) * 0.2f;
            
            int color = selected ? getThemeColorHex() : 0xFF777777;
            if (!selected) {
                int r = (int)(0x77 + (0xAA - 0x77) * hoverAnim);
                int g = (int)(0x77 + (0xAA - 0x77) * hoverAnim);
                int b = (int)(0x77 + (0xAA - 0x77) * hoverAnim);
                color = 0xFF000000 | (r << 16) | (g << 8) | b;
            }
            
            if (selected) {
                RenderUtils.fill(context, com.ersin.legitbridge.utils.VersionHelper.getWidgetX(this), com.ersin.legitbridge.utils.VersionHelper.getWidgetY(this) + this.height - 2, com.ersin.legitbridge.utils.VersionHelper.getWidgetX(this) + this.width, com.ersin.legitbridge.utils.VersionHelper.getWidgetY(this) + this.height, getThemeColorHex());
            } else if (hoverAnim > 0.05f) {
                int hw = (int)((this.width / 2.0f) * hoverAnim);
                int cx = com.ersin.legitbridge.utils.VersionHelper.getWidgetX(this) + this.width / 2;
                RenderUtils.fill(context, cx - hw, com.ersin.legitbridge.utils.VersionHelper.getWidgetY(this) + this.height - 1, cx + hw, com.ersin.legitbridge.utils.VersionHelper.getWidgetY(this) + this.height, 0xFF777777);
            }
            
            RenderUtils.drawCenteredText(context, MinecraftClient.getInstance().textRenderer, this.getMessage().getString(), com.ersin.legitbridge.utils.VersionHelper.getWidgetX(this) + this.width / 2, com.ersin.legitbridge.utils.VersionHelper.getWidgetY(this) + (this.height - 8) / 2, color);
        }
    }

    class SimpleButton extends ButtonWidget {
        //? if >1.19.2 {
        
        public int getX() { return super.getX(); }
        public int getY() { return super.getY(); }
        public int getWidth() { return super.getWidth(); }
        public int getHeight() { return super.getHeight(); }
        public void setX(int x) { super.setX(x); }
        public void setY(int y) { super.setY(y); }
        
        //?} else {
        /*public int getX() { return super.x; }
        public int getY() { return super.y; }
        public int getWidth() { return super.width; }
        public int getHeight() { return super.height; }
        public void setX(int x) { super.x = x; }
        public void setY(int y) { super.y = y; }
        *///?}

        private final Supplier<String> textSupplier;
        private final Runnable action;

        public SimpleButton(int x, int y, int width, int height, Supplier<String> textSupplier, Runnable action) {
            //? if <=1.19.2 {
            /*super(x, y, width, height, text(""), button -> {});
*///?} else {
            super(x, y, width, height, text(""), button -> {}, net.minecraft.client.gui.widget.ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
//?}
            this.textSupplier = textSupplier;
            this.action = action;
        }

        @Override
        public void onPress() {
            action.run();
        }
        //? if <=1.19.2 {
        /*public void render(net.minecraft.client.util.math.MatrixStack matrices, int mouseX, int mouseY, float delta) {
    Object context = matrices;
*///?} else {
public void renderWidget(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
//?}
            boolean hovered = isMouseOver(mouseX, mouseY);
            int bgColor = hovered ? 0xAA303030 : 0x80151515;
            
            RenderUtils.fill(context, this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor);
            RenderUtils.fill(context, this.getX(), this.getY(), this.getX() + this.width, this.getY() + 1, 0x44FFFFFF);
            RenderUtils.fill(context, this.getX(), this.getY() + this.height - 1, this.getX() + this.width, this.getY() + this.height, 0x22FFFFFF);
            RenderUtils.fill(context, this.getX(), this.getY(), this.getX() + 1, this.getY() + this.height, 0x33FFFFFF);
            RenderUtils.fill(context, this.getX() + this.width - 1, this.getY(), this.getX() + this.width, this.getY() + this.height, 0x33FFFFFF);
            RenderUtils.drawCenteredText(context, MinecraftClient.getInstance().textRenderer, textSupplier.get(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, 0xFFFFFF);
        }
    }

    class ConfigButton extends ButtonWidget {
        //? if >1.19.2 {
        
        public int getX() { return super.getX(); }
        public int getY() { return super.getY(); }
        public int getWidth() { return super.getWidth(); }
        public int getHeight() { return super.getHeight(); }
        public void setX(int x) { super.setX(x); }
        public void setY(int y) { super.setY(y); }
        
        //?} else {
        /*public int getX() { return super.x; }
        public int getY() { return super.y; }
        public int getWidth() { return super.width; }
        public int getHeight() { return super.height; }
        public void setX(int x) { super.x = x; }
        public void setY(int y) { super.y = y; }
        *///?}

        public final String configKey;
        private final Supplier<Boolean> stateSupplier;
        private final Runnable toggleAction;
        private float hoverAnim = 0f;
        private float toggleAnim = -1f;
        public final int baseY;
        
        // public int y;

        public ConfigButton(int x, int y, int width, int height, String configKey, String label, Supplier<Boolean> stateSupplier, Runnable toggleAction) {
            //? if <=1.19.2 {
            /*super(x, y, width, height, text(label), button -> {});
*///?} else {
            super(x, y, width, height, text(label), button -> {}, net.minecraft.client.gui.widget.ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
//?}
            this.configKey = configKey;
            this.stateSupplier = stateSupplier;
            this.toggleAction = toggleAction;
            this.baseY = y;
        }

        // @Override
        // public void setY(int y) {
        //     super.setY(y);
        //     this.getY() = y;
        // }

        @Override
        public void onPress() {
            toggleAction.run();
            ModConfig.save();
            playConfigSound();
        }
        //? if <=1.19.2 {
        /*public void render(net.minecraft.client.util.math.MatrixStack matrices, int mouseX, int mouseY, float delta) {
    Object context = matrices;
*///?} else {
public void renderWidget(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
//?}
            boolean hovered = isMouseOver(mouseX, mouseY);
            boolean active = stateSupplier.get();
            
            if (toggleAnim < 0) { toggleAnim = active ? 1f : 0f; } // init
            
            float targetHover = hovered ? 1f : 0f;
            hoverAnim += (targetHover - hoverAnim) * 0.2f;
            
            float targetToggle = active ? 1f : 0f;
            toggleAnim += (targetToggle - toggleAnim) * 0.2f;
            
            int bgR = (int)(0x15 + (0x25 - 0x15) * hoverAnim);
            int bgAlpha = (int)(0x60 + (0x90 - 0x60) * hoverAnim);
            int bgColor = (bgAlpha << 24) | (bgR << 16) | (bgR << 8) | bgR;
            
            RenderUtils.fill(context, this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor);
            RenderUtils.drawTextWithShadow(context, MinecraftClient.getInstance().textRenderer, this.getMessage().getString(), this.getX() + 8, this.getY() + (this.height - 8) / 2, hovered ? 0xFFFFFFFF : 0xFFDDDDDD);
            
            int toggleWidth = 24;
            int toggleHeight = 12;
            
            int toggleX = this.getX() + this.width - toggleWidth - 8;
            int toggleY = this.getY() + (this.height - toggleHeight) / 2;

            int themeHex = getThemeColorHex();
            int tR = (themeHex >> 16) & 0xFF;
            int tG = (themeHex >> 8) & 0xFF;
            int tB = themeHex & 0xFF;
            
            int oR = 0x44; int oG = 0x44; int oB = 0x44;
            
            int curR = (int)(oR + (tR - oR) * toggleAnim);
            int curG = (int)(oG + (tG - oG) * toggleAnim);
            int curB = (int)(oB + (tB - oB) * toggleAnim);
            int switchColor = 0xFF000000 | (curR << 16) | (curG << 8) | curB;
            
            RenderUtils.fill(context, toggleX, toggleY, toggleX + toggleWidth, toggleY + toggleHeight, switchColor);
            
            int knobSize = 10;
            int knobStartX = toggleX + 1;
            int knobEndX = toggleX + toggleWidth - knobSize - 1;
            int knobX = (int)(knobStartX + (knobEndX - knobStartX) * toggleAnim);
            int knobY = toggleY + 1;
            RenderUtils.fill(context, knobX, knobY, knobX + knobSize, knobY + knobSize, 0xFFFFFFFF);
            
            if (ModConfig.keyBinds.containsKey(configKey)) {
                int key = ModConfig.keyBinds.get(configKey);
                String keyName = GLFW.glfwGetKeyName(key, 0);
                if (keyName == null) keyName = String.valueOf(key);
                keyName = keyName.toUpperCase();
                
                RenderUtils.drawTextWithShadow(context, MinecraftClient.getInstance().textRenderer, "§e[" + keyName + "]", this.getX() + this.width + 4, this.getY() + (this.height - 8) / 2, 0xFFFFFF);
            }
        }
    }
}
