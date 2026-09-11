import os
import glob
import re

files = glob.glob('src/main/java/com/ersin/legitbridge/**/*.java', recursive=True)

for file in files:
    with open(file, 'r') as f:
        content = f.read()
    
    original_content = content

    # ModGuideScreen
    if "ModGuideScreen.java" in file:
        content = content.replace('new LiteralText', 'net.minecraft.text.Text.literal')
        content = content.replace('this.addButton', 'this.addDrawableChild')
        content = content.replace('((net.minecraft.client.util.math.MatrixStack) context)', '((net.minecraft.client.gui.DrawContext) context)')
        # It's better to just fix it with stonecutter for multi version
        # Actually I can just do standard stonecutter
        pass

    # Quick stonecutter wrapper for inventory
    content = content.replace('client.player.inventory', '/*? if <=1.19.2 { ?*/client.player.inventory/*?} else { ?*/client.player.getInventory()/*?} ?*/')
    
    # client.options keys
    content = content.replace('client.options.keyUse', '/*? if <=1.19.2 { ?*/client.options.keyUse/*?} else { ?*/client.options.useKey/*?} ?*/')
    content = content.replace('client.options.keyAttack', '/*? if <=1.19.2 { ?*/client.options.keyAttack/*?} else { ?*/client.options.attackKey/*?} ?*/')
    content = content.replace('client.options.keyJump', '/*? if <=1.19.2 { ?*/client.options.keyJump/*?} else { ?*/client.options.jumpKey/*?} ?*/')
    content = content.replace('client.options.keySneak', '/*? if <=1.19.2 { ?*/client.options.keySneak/*?} else { ?*/client.options.sneakKey/*?} ?*/')
    content = content.replace('client.options.keySprint', '/*? if <=1.19.2 { ?*/client.options.keySprint/*?} else { ?*/client.options.sprintKey/*?} ?*/')
    
    # entity.collides() vs entity.isCollidable() ?
    if "AdvancedCombatHud.java" in file:
        content = content.replace('entity.collides()', '/*? if <=1.19.2 { ?*/entity.collides()/*?} else { ?*/entity.canHit()/*?} ?*/')

    if content != original_content:
        with open(file, 'w') as f:
            f.write(content)

