import os
import re

replacements = [
    (r"buffer\.begin\(net\.minecraft\.client\.render\.VertexFormat\.DrawMode\.DEBUG_LINE_STRIP, net\.minecraft\.client\.render\.VertexFormats\.POSITION_COLOR\)",
     "buffer.begin(\n//? if <=1.19.2 {\n/*3*/\n//?} else {\nnet.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINE_STRIP\n//?}\n, net.minecraft.client.render.VertexFormats.POSITION_COLOR)"),

    (r"buffer\.begin\(net\.minecraft\.client\.render\.VertexFormat\.DrawMode\.DEBUG_LINES, net\.minecraft\.client\.render\.VertexFormats\.POSITION_COLOR\)",
     "buffer.begin(\n//? if <=1.19.2 {\n/*1*/\n//?} else {\nnet.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINES\n//?}\n, net.minecraft.client.render.VertexFormats.POSITION_COLOR)"),
     
    (r"matrixStack\.peek\(\)\.getPositionMatrix\(\)",
     "\n//? if <=1.19.2 {\n/*matrixStack.peek().getModel()*/\n//?} else {\nmatrixStack.peek().getPositionMatrix()\n//?}\n")
]

for root, _, files in os.walk("src/main/java"):
    for file in files:
        if file.endswith(".java"):
            path = os.path.join(root, file)
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
