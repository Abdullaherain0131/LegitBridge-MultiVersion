import os
import re

replacements = [
    (r"public void onHudRender\(net\.minecraft\.client\.gui\.DrawContext context, float tickDelta\)",
     "//? if <=1.19.2 {\n/*public void onHudRender(net.minecraft.client.util.math.MatrixStack context, float tickDelta)*/\n//?} else {\npublic void onHudRender(net.minecraft.client.gui.DrawContext context, float tickDelta)\n//?}"),
     
    (r"public void render\(net\.minecraft\.client\.gui\.DrawContext context, int mouseX, int mouseY, float delta\)",
     "//? if <=1.19.2 {\n/*public void render(net.minecraft.client.util.math.MatrixStack context, int mouseX, int mouseY, float delta)*/\n//?} else {\npublic void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta)\n//?}")
]

files_to_patch = [
    "src/main/java/com/ersin/legitbridge/ModHudRenderer.java",
    "src/main/java/com/ersin/legitbridge/gui/ModConfigScreen.java",
    "src/main/java/com/ersin/legitbridge/gui/ModGuideScreen.java"
]

for path in files_to_patch:
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
