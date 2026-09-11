import os

def fix_file(path, old, new):
    with open(path, "r") as f:
        content = f.read()
    if old in content:
        with open(path, "w") as f:
            f.write(content.replace(old, new))
        print(f"Fixed {path}")

# ModHudRenderer.java
fix_file("src/main/java/com/ersin/legitbridge/ModHudRenderer.java",
"""        //? if <=1.19.2 {
/*public void onHudRender(net.minecraft.client.util.math.MatrixStack context, float tickDelta)*/
//?} else {
public void onHudRender(net.minecraft.client.gui.DrawContext context, float tickDelta)
//?} {""",
"""//? if <=1.19.2 {
/*        public void onHudRender(net.minecraft.client.util.math.MatrixStack context, float tickDelta) {*/
//?} else {
        public void onHudRender(net.minecraft.client.gui.DrawContext context, float tickDelta) {
//?}""")

# ModConfigScreen.java
fix_file("src/main/java/com/ersin/legitbridge/gui/ModConfigScreen.java",
"""    //? if <=1.19.2 {
/*public void render(net.minecraft.client.util.math.MatrixStack context, int mouseX, int mouseY, float delta)*/
//?} else {
public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta)
//?} {""",
"""//? if <=1.19.2 {
/*    public void render(net.minecraft.client.util.math.MatrixStack context, int mouseX, int mouseY, float delta) {*/
//?} else {
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
//?}""")

# ModGuideScreen.java
fix_file("src/main/java/com/ersin/legitbridge/gui/ModGuideScreen.java",
"""    //? if <=1.19.2 {
/*public void render(net.minecraft.client.util.math.MatrixStack context, int mouseX, int mouseY, float delta)*/
//?} else {
public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta)
//?} {""",
"""//? if <=1.19.2 {
/*    public void render(net.minecraft.client.util.math.MatrixStack context, int mouseX, int mouseY, float delta) {*/
//?} else {
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
//?}""")

# AdvancedCombatHud.java
fix_file("src/main/java/com/ersin/legitbridge/AdvancedCombatHud.java",
"""    //? if <=1.19.2 {
/*public static void render(net.minecraft.client.util.math.MatrixStack context, float tickDelta)*/
//?} else {
public static void render(net.minecraft.client.gui.DrawContext context, float tickDelta)
//?} {""",
"""//? if <=1.19.2 {
/*    public static void render(net.minecraft.client.util.math.MatrixStack context, float tickDelta) {*/
//?} else {
    public static void render(net.minecraft.client.gui.DrawContext context, float tickDelta) {
//?}""")

fix_file("src/main/java/com/ersin/legitbridge/AdvancedCombatHud.java",
"""    //? if <=1.19.2 {
/*public static void renderBlockHitSync(net.minecraft.client.util.math.MatrixStack context, net.minecraft.client.font.TextRenderer font, float x, float y)*/
//?} else {
public static void renderBlockHitSync(net.minecraft.client.gui.DrawContext context, net.minecraft.client.font.TextRenderer font, float x, float y)
//?} {""",
"""//? if <=1.19.2 {
/*    public static void renderBlockHitSync(net.minecraft.client.util.math.MatrixStack context, net.minecraft.client.font.TextRenderer font, float x, float y) {*/
//?} else {
    public static void renderBlockHitSync(net.minecraft.client.gui.DrawContext context, net.minecraft.client.font.TextRenderer font, float x, float y) {
//?}""")

