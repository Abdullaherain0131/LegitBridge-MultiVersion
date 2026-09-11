import os
import re

replacements = [
    (r"public static void render\(net\.minecraft\.client\.gui\.DrawContext context, float tickDelta\)",
     "//? if <=1.19.2 {\n/*public static void render(net.minecraft.client.util.math.MatrixStack context, float tickDelta)*/\n//?} else {\npublic static void render(net.minecraft.client.gui.DrawContext context, float tickDelta)\n//?}"),
     
    (r"public static void renderBlockHitSync\(net\.minecraft\.client\.gui\.DrawContext context, net\.minecraft\.client\.font\.TextRenderer font, float x, float y\)",
     "//? if <=1.19.2 {\n/*public static void renderBlockHitSync(net.minecraft.client.util.math.MatrixStack context, net.minecraft.client.font.TextRenderer font, float x, float y)*/\n//?} else {\npublic static void renderBlockHitSync(net.minecraft.client.gui.DrawContext context, net.minecraft.client.font.TextRenderer font, float x, float y)\n//?}")
]

path = "src/main/java/com/ersin/legitbridge/AdvancedCombatHud.java"

with open(path, "r") as f:
    content = f.read()

modified = False
for old, new in replacements:
    content, count = re.subn(old, new, content)
    if count > 0:
        modified = True
        
if modified:
    with open(path, "w") as f:
        f.write(content)
    print(f"Patched {path}")
