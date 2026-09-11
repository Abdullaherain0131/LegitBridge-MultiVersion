import os
import re

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    original_content = content

    # 1. client.player.inventory -> getInventory()
    content = re.sub(
        r'(client\.player)\.inventory',
        r'//? if <=1.19 {\n\1.inventory\n//?} else {\n/*\1.getInventory()\n*///?}',
        content
    )

    # 2. client.player.pitch (getter)
    # Be careful not to replace setters. Look for expressions, not assignments.
    # We will do setter first.
    
    # setter += 
    content = re.sub(
        r'(client\.player)\.pitch\s*\+=\s*([^;]+);',
        r'//? if <=1.19 {\n\1.pitch += \2;\n//?} else {\n/*\1.setPitch(\1.getPitch() + \2);\n*///?}',
        content
    )
    
    # setter -= 
    content = re.sub(
        r'(client\.player)\.pitch\s*-=\s*([^;]+);',
        r'//? if <=1.19 {\n\1.pitch -= \2;\n//?} else {\n/*\1.setPitch(\1.getPitch() - \2);\n*///?}',
        content
    )

    # setter = 
    content = re.sub(
        r'(client\.player)\.pitch\s*=\s*([^;]+);',
        r'//? if <=1.19 {\n\1.pitch = \2;\n//?} else {\n/*\1.setPitch(\2);\n*///?}',
        content
    )

    # getter
    # Note: re.sub won't replace already replaced text if we don't match the new text, but to be safe:
    content = re.sub(
        r'(?<!\.)(client\.player\.pitch)(?![\s\+\-\=]*(?:\+|-)?=)',
        r'/*? if <=1.19 { ?*/\1/*?} else { ?*//*client.player.getPitch()*//*? } ?*/',
        content
    )


    # 3. client.player.yaw (getter and setter)
    # setter += 
    content = re.sub(
        r'(client\.player)\.yaw\s*\+=\s*([^;]+);',
        r'//? if <=1.19 {\n\1.yaw += \2;\n//?} else {\n/*\1.setYaw(\1.getYaw() + \2);\n*///?}',
        content
    )
    
    # setter -= 
    content = re.sub(
        r'(client\.player)\.yaw\s*-=\s*([^;]+);',
        r'//? if <=1.19 {\n\1.yaw -= \2;\n//?} else {\n/*\1.setYaw(\1.getYaw() - \2);\n*///?}',
        content
    )

    # setter = 
    content = re.sub(
        r'(client\.player)\.yaw\s*=\s*([^;]+);',
        r'//? if <=1.19 {\n\1.yaw = \2;\n//?} else {\n/*\1.setYaw(\2);\n*///?}',
        content
    )

    # getter
    content = re.sub(
        r'(?<!\.)(client\.player\.yaw)(?![\s\+\-\=]*(?:\+|-)?=)',
        r'/*? if <=1.19 { ?*/\1/*?} else { ?*//*client.player.getYaw()*//*? } ?*/',
        content
    )

    # 4. interactItem
    content = re.sub(
        r'(client\.interactionManager\.interactItem\s*\(\s*client\.player\s*),\s*client\.world\s*,\s*(Hand\.MAIN_HAND\s*\))',
        r'//? if <=1.19 {\n\1, client.world, \2\n//?} else {\n/*\1, \2\n*///?}',
        content
    )
    
    # 5. interactBlock
    content = re.sub(
        r'(client\.interactionManager\.interactBlock\s*\(\s*client\.player\s*),\s*client\.world\s*,\s*(net\.minecraft\.util\.Hand\.MAIN_HAND|Hand\.MAIN_HAND)\s*,\s*([^)]+\))',
        r'//? if <=1.19 {\n\1, client.world, \2, \3\n//?} else {\n/*\1, \2, \3\n*///?}',
        content
    )

    # 6. client.options.keyAttack
    content = re.sub(
        r'(client\.options)\.keyAttack',
        r'/*? if <=1.19 { ?*/\1.keyAttack/*?} else { ?*//*\1.attackKey*//*? } ?*/',
        content
    )

    # 7 & 8. DrawableHelper (only inside RenderUtils.java, let's just do it directly here)
    if 'RenderUtils.java' in filepath:
        content = re.sub(
            r'net\.minecraft\.client\.gui\.DrawableHelper\.fill\(\(MatrixStack\) context, (x1, y1, x2, y2, color)\);',
            r'//? if <=1.19 {\n        net.minecraft.client.gui.DrawableHelper.fill((MatrixStack) context, \1);\n//?} else {\n/*        ((net.minecraft.client.gui.DrawContext) context).fill(\1);\n*///?}',
            content
        )
        content = re.sub(
            r'net\.minecraft\.client\.gui\.DrawableHelper\.drawCenteredText\(\(MatrixStack\) context, (textRenderer, text, centerX, y, color)\);',
            r'//? if <=1.19 {\n        net.minecraft.client.gui.DrawableHelper.drawCenteredText((MatrixStack) context, \1);\n//?} else {\n/*        ((net.minecraft.client.gui.DrawContext) context).drawCenteredTextWithShadow(\1);\n*///?}',
            content
        )

    if content != original_content:
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Updated {filepath}")

for root, _, files in os.walk('src/main/java/com/ersin/legitbridge'):
    for file in files:
        if file.endswith('.java'):
            process_file(os.path.join(root, file))

