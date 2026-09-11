package com.ersin.legitbridge.gui;

import com.ersin.legitbridge.config.ModConfig;
//? if <=1.19.2 {
/*import net.minecraft.client.gui.DrawableHelper;
*///?} else {
import net.minecraft.client.gui.DrawContext;
//?}
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
//? if <=1.19.2 {
/*import net.minecraft.text.LiteralText;
*///?}
import com.ersin.legitbridge.utils.RenderUtils;

public class ModGuideScreen extends Screen {
    //? if >1.19.2 {
    private static net.minecraft.text.Text text(String s) { return com.ersin.legitbridge.utils.VersionHelper.literalText(s); }
    //?} else {
    /*private static net.minecraft.text.Text text(String s) { return new net.minecraft.text.LiteralText(s); }
    *///?}


    private final Screen parent;
    private int currentPage = 1;
    private final int totalPages = 4;

    private void addCustomButton(ButtonWidget button) {
        //? if >1.19.2 {
        this.addDrawableChild(button);
        //?} else {
        /*this.addCustomButton(button);
        *///?}
    }
    public ModGuideScreen(Screen parent) {
        super(com.ersin.legitbridge.utils.VersionHelper.literalText("LegitBridge Kullanım Rehberi"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int bottomY = this.height - 28;

        // Önceki Sayfa

        //? if <=1.19.2 {
        /*this.addButton(new net.minecraft.client.gui.widget.ButtonWidget(centerX - 155, bottomY, 90, 20, com.ersin.legitbridge.utils.VersionHelper.literalText("← Önceki"), button -> {
            if (currentPage > 1) {
                currentPage--;
                ModConfigScreen.playConfigSound();
            }
        }));
        *///?} else {
        this.addDrawableChild(net.minecraft.client.gui.widget.ButtonWidget.builder(com.ersin.legitbridge.utils.VersionHelper.literalText("← Önceki"), button -> {
            if (currentPage > 1) {
                currentPage--;
                ModConfigScreen.playConfigSound();
            }
        }).dimensions(centerX - 155, bottomY, 90, 20).build());
        //?}


        // Geri Dön

        //? if <=1.19.2 {
        /*this.addButton(new net.minecraft.client.gui.widget.ButtonWidget(centerX - 45, bottomY, 90, 20, com.ersin.legitbridge.utils.VersionHelper.literalText("§cAna Menü"), button -> {
            ModConfigScreen.playConfigSound();
            if (this.client != null) {
                com.ersin.legitbridge.utils.VersionHelper.setScreen(this.client, this.parent);
            }
        }));
        *///?} else {
        this.addDrawableChild(net.minecraft.client.gui.widget.ButtonWidget.builder(com.ersin.legitbridge.utils.VersionHelper.literalText("§cAna Menü"), button -> {
            ModConfigScreen.playConfigSound();
            if (this.client != null) {
                com.ersin.legitbridge.utils.VersionHelper.setScreen(this.client, this.parent);
            }
        }).dimensions(centerX - 45, bottomY, 90, 20).build());
        //?}


        // Sonraki Sayfa

        //? if <=1.19.2 {
        /*this.addButton(new net.minecraft.client.gui.widget.ButtonWidget(centerX + 65, bottomY, 90, 20, com.ersin.legitbridge.utils.VersionHelper.literalText("Sonraki →"), button -> {
            if (currentPage < totalPages) {
                currentPage++;
                ModConfigScreen.playConfigSound();
            }
        }));
        *///?} else {
        this.addDrawableChild(net.minecraft.client.gui.widget.ButtonWidget.builder(com.ersin.legitbridge.utils.VersionHelper.literalText("Sonraki →"), button -> {
            if (currentPage < totalPages) {
                currentPage++;
                ModConfigScreen.playConfigSound();
            }
        }).dimensions(centerX + 65, bottomY, 90, 20).build());
        //?}

    }

    @Override
//? if <=1.19.2 {
/*    public void render(net.minecraft.client.util.math.MatrixStack context, int mouseX, int mouseY, float delta) {*/
//?} else {
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
//?}
        // Gradient Backdrop
        //? if <=1.19.2 {
/*this.fillGradient((net.minecraft.client.util.math.MatrixStack) context, 0, 0, this.width, this.height, 0xC0101014, 0xD0050508);
*///?} else {
((net.minecraft.client.gui.DrawContext)context).fillGradient(0, 0, this.width, this.height, 0xC0101014, 0xD0050508);
//?}

        int centerX = this.width / 2;
        String color = ModConfig.getThemePrefix();

        // Central Guide Card Box
        RenderUtils.fill(context, centerX - 180, 10, centerX + 180, this.height - 35, 0xAA000000);
        RenderUtils.fill(context, centerX - 180, 10, centerX + 180, 12, ModConfig.getThemePrimaryColor());

        RenderUtils.drawCenteredText(context, this.textRenderer, color + "§lLegitBridge §7- Oyun İçi Rehber", centerX, 18, 0xFFFFFF);
        RenderUtils.drawCenteredText(context, this.textRenderer, "§7Sayfa " + currentPage + " / " + totalPages, centerX, 30, 0xAAAAAA);

        int textY = 48;
        int leftX = centerX - 165;

        switch (currentPage) {
            case 1:
                renderPage1(context, fontRenderer(), leftX, textY, color);
                break;
            case 2:
                renderPage2(context, fontRenderer(), leftX, textY, color);
                break;
            case 3:
                renderPage3(context, fontRenderer(), leftX, textY, color);
                break;
            case 4:
                renderPage4(context, fontRenderer(), leftX, textY, color);
                break;
        }

        //? if <=1.19.2 {
/*super.render((net.minecraft.client.util.math.MatrixStack) context, mouseX, mouseY, delta);
*///?} else {
super.render((net.minecraft.client.gui.DrawContext) context, mouseX, mouseY, delta);
//?}
    }

    private net.minecraft.client.font.TextRenderer fontRenderer() {
        return this.textRenderer;
    }

    private void renderPage1(Object context, net.minecraft.client.font.TextRenderer font, int x, int y, String c) {
        RenderUtils.drawTextWithShadow(context, font, c + "§l1. Temel Kullanım ve Kısayol Tuşları", x, y, 0xFFFFFF);
        y += 16;
        RenderUtils.drawTextWithShadow(context, font, "§f• §b[Sağ Shift] Tuşu: §7Gelişmiş kontrol paneli ve ayar menüsünü açar.", x, y, 0xE0E0E0);
        y += 14;
        RenderUtils.drawTextWithShadow(context, font, "§f• §b[B] Tuşu: §7Modun tüm fonksiyonlarını hızlıca Açık/Kapalı yapar.", x, y, 0xE0E0E0);
        y += 14;
        RenderUtils.drawTextWithShadow(context, font, "§f• §bProfil Desteği: §7Casual, Bedwars ve Skywars hazır ayarları mevcuttur.", x, y, 0xE0E0E0);
        y += 18;
        RenderUtils.drawTextWithShadow(context, font, c + "§lÖnerilen Başlangıç Ayarları:", x, y, 0xFFFFFF);
        y += 14;
        RenderUtils.drawTextWithShadow(context, font, "§7- Bedwars oyuncuları için: §eBEDWARS§7 profilini seçin.", x, y, 0xAAAAAA);
        y += 12;
        RenderUtils.drawTextWithShadow(context, font, "§7- Rahat oynanış için: §eCASUAL§7 profilini kullanın.", x, y, 0xAAAAAA);
    }

    private void renderPage2(Object context, net.minecraft.client.font.TextRenderer font, int x, int y, String c) {
        RenderUtils.drawTextWithShadow(context, font, c + "§l2. SafeWalk ve Zıplama Desteği (JumpBridge)", x, y, 0xFFFFFF);
        y += 16;
        RenderUtils.drawTextWithShadow(context, font, "§f• §bSafeWalk: §7Blok kenarlarında düşmeyi engelleyen hareket koruması.", x, y, 0xE0E0E0);
        y += 14;
        RenderUtils.drawTextWithShadow(context, font, "§f• §bZıplama Desteği: §7Zıplayarak yukarı doğru merdiven yaparken", x, y, 0xE0E0E0);
        y += 12;
        RenderUtils.drawTextWithShadow(context, font, "  §7ayağınızın altına bloğun tam zamanında yerleşmesine yardımcı olur.", x, y, 0xAAAAAA);
        y += 18;
        RenderUtils.drawTextWithShadow(context, font, c + "§lİpucu:", x, y, 0xFFFFFF);
        y += 14;
        RenderUtils.drawTextWithShadow(context, font, "§7Geriye doğru yürürken sağ tık basılı tutulduğunda en iyi sonucu verir.", x, y, 0xAAAAAA);
    }

    private void renderPage3(Object context, net.minecraft.client.font.TextRenderer font, int x, int y, String c) {
        RenderUtils.drawTextWithShadow(context, font, c + "§l3. Otomatik Alet ve Blok Yenileme (AutoTool & Supply)", x, y, 0xFFFFFF);
        y += 16;
        RenderUtils.drawTextWithShadow(context, font, "§f• §bAutoTool: §7Sol tıkla bir bloğa vurduğunuzda (Taş, Odun, Toprak)", x, y, 0xE0E0E0);
        y += 12;
        RenderUtils.drawTextWithShadow(context, font, "  §7envanterinizdeki en uygun aleti (Kazma, Balta, Kürek) otomatik seçer.", x, y, 0xAAAAAA);
        y += 16;
        RenderUtils.drawTextWithShadow(context, font, "§f• §bAutoSupply: §7Elinizdeki blok bittiğinde envanterdeki yedek blokları", x, y, 0xE0E0E0);
        y += 12;
        RenderUtils.drawTextWithShadow(context, font, "  §7kesintisiz şekilde elinizdeki slota aktarır.", x, y, 0xAAAAAA);
    }

    private void renderPage4(Object context, net.minecraft.client.font.TextRenderer font, int x, int y, String c) {
        RenderUtils.drawTextWithShadow(context, font, c + "§l4. Arayüz (HUD), Renk Temaları ve Oturum İstatistikleri", x, y, 0xFFFFFF);
        y += 16;
        RenderUtils.drawTextWithShadow(context, font, "§f• §bRenk Temaları: §7Cyan Neon, Zümrüt Yeşil, Gece Siyahı ve Turuncu.", x, y, 0xE0E0E0);
        y += 14;
        RenderUtils.drawTextWithShadow(context, font, "§f• §bSürüklenebilir HUD: §7Sağ Shift menüsündeyken HUD çerçevesini", x, y, 0xE0E0E0);
        y += 12;
        RenderUtils.drawTextWithShadow(context, font, "  §7fare ile istediğiniz yere sürükleyip kopyalayabilirsiniz.", x, y, 0xAAAAAA);
        y += 16;
        RenderUtils.drawTextWithShadow(context, font, "§f• §bOturum Paneli: §7Oturum süresi, yerleştirilen toplam blok sayısı", x, y, 0xE0E0E0);
        y += 12;
        RenderUtils.drawTextWithShadow(context, font, "  §7ve anlık/maksimum CPS değerlerini ekranda gösterir.", x, y, 0xAAAAAA);
    }

    @Override
//? if <=1.19.2 {
    /*public boolean isPauseScreen() {
*///?} else {
    public boolean shouldPause() {
//?}
        return false;
    }
}
