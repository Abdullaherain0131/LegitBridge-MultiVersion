package com.ersin.legitbridge.utils;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class RenderUtils {

    public static void fill(Object context, int x1, int y1, int x2, int y2, int color) {
                //? if <=1.19.2 {
                /*net.minecraft.client.gui.DrawableHelper.fill((net.minecraft.client.util.math.MatrixStack) context, x1, y1, x2, y2, color);
*///?} else {
                ((net.minecraft.client.gui.DrawContext)context).fill(x1, y1, x2, y2, color);
//?}
    }



    public static void drawTextWithShadow(Object context, TextRenderer textRenderer, String text, float x, float y, int color) {
        //? if <=1.19.2 {
        /*textRenderer.drawWithShadow((net.minecraft.client.util.math.MatrixStack) context, text, x, y, color);
*///?} else {
        ((net.minecraft.client.gui.DrawContext)context).drawTextWithShadow(textRenderer, text, (int)x, (int)y, color);
//?}
    }

    public static void drawCenteredText(Object context, TextRenderer textRenderer, String text, int centerX, int y, int color) {
                //? if <=1.19.2 {
                /*net.minecraft.client.gui.DrawableHelper.drawCenteredText((net.minecraft.client.util.math.MatrixStack) context, textRenderer, text, centerX, y, color);
*///?} else {
                ((net.minecraft.client.gui.DrawContext)context).drawCenteredTextWithShadow(textRenderer, text, centerX, y, color);
//?}
    }
}
