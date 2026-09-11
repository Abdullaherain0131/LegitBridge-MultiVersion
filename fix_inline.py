import re
import glob

def inline_to_block(match):
    cond = match.group(1)
    code1 = match.group(2)
    code2 = match.group(3)
    return f"""
        //? if {cond} {{
        /*{code1}*/
        //?}} else {{
        {code2}
        //?}}"""

def replace_inlines(file_path):
    with open(file_path, 'r') as f:
        content = f.read()
    
    # Try to find all inline: /*? if cond {*/ code1 /*?} else {*/ code2 /*?}*/
    # This might be on a single line.
    
    # But wait, we can just replace specific known ones:
    content = content.replace('/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText(text)/*?} else {*/net.minecraft.text.Text.literal(text)/*?}*/;',
        '//? if <=1.19.2 {\n/*return new net.minecraft.text.LiteralText(text);*/\n//?} else {\nreturn net.minecraft.text.Text.literal(text);\n//?}')
    
    # Also we had: return /*? if <=1.19.2 ... */
    content = content.replace('return //? if', '//? if')
    
    # LegitBridgeMod
    content = content.replace('/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText("LegitBridge Aktif!")/*?} else {*/net.minecraft.text.Text.literal("LegitBridge Aktif!")/*?}*/',
        '//? if <=1.19.2 {\n/*new net.minecraft.text.LiteralText("LegitBridge Aktif!")*/\n//?} else {\nnet.minecraft.text.Text.literal("LegitBridge Aktif!")\n//?}')
    
    # And AutoTool.java
    content = content.replace('/*? if <=1.19.2 {*/client.player.inventory.selectedSlot = bestSlot;/*?} else {*/client.player.getInventory().selectedSlot = bestSlot;/*?}*/',
        '//? if <=1.19.2 {\n/*client.player.inventory.selectedSlot = bestSlot;*/\n//?} else {\nclient.player.getInventory().selectedSlot = bestSlot;\n//?}')

    content = content.replace('/*? if <=1.19.2 {*/client.player.inventory/*?} else {*/client.player.getInventory()/*?}*/',
        'client.player.getInventory()') # Just fix it if it's left over

    # Fullbright
    content = content.replace('/*? if <=1.19.2 {*/client.options.gamma = 100.0;/*?} else {*/client.options.getGamma().setValue(100.0);/*?}*/',
        '//? if <=1.19.2 {\n/*client.options.gamma = 100.0;*/\n//?} else {\nclient.options.getGamma().setValue(100.0);\n//?}')
    
    content = content.replace('/*? if <=1.19.2 {*/client.options.gamma = originalGamma;/*?} else {*/client.options.getGamma().setValue(originalGamma);/*?}*/',
        '//? if <=1.19.2 {\n/*client.options.gamma = originalGamma;*/\n//?} else {\nclient.options.getGamma().setValue(originalGamma);\n//?}')
    
    # FireballAssist
    content = content.replace('/*? if <=1.19.2 {*/client.player.pitch += (targetPitch - currentPitch) / 2.0f;/*?} else {*/client.player.setPitch(client.player.getPitch() + (targetPitch - currentPitch) / 2.0f);/*?}*/',
        '//? if <=1.19.2 {\n/*client.player.pitch += (targetPitch - currentPitch) / 2.0f;*/\n//?} else {\nclient.player.setPitch(client.player.getPitch() + (targetPitch - currentPitch) / 2.0f);\n//?}')
    content = content.replace('/*? if <=1.19.2 {*/client.player.pitch = targetPitch;/*?} else {*/client.player.setPitch(targetPitch);/*?}*/',
        '//? if <=1.19.2 {\n/*client.player.pitch = targetPitch;*/\n//?} else {\nclient.player.setPitch(targetPitch);\n//?}')
        
    # Mixin
    content = content.replace('/*? if <=1.19.2 {*/this.options.keyUse/*?} else {*/this.options.useKey/*?}*/.isPressed()',
        'this.options.useKey.isPressed()')
    
    # AutoFish
    content = content.replace('/*? if <=1.19.2 {*/client.interactionManager.interactItem(client.player, client.world, Hand.MAIN_HAND);/*?} else {*/client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);/*?}*/',
        '//? if <=1.19.2 {\n/*client.interactionManager.interactItem(client.player, client.world, net.minecraft.util.Hand.MAIN_HAND);*/\n//?} else {\nclient.interactionManager.interactItem(client.player, net.minecraft.util.Hand.MAIN_HAND);\n//?}')
        
    with open(file_path, 'w') as f:
        f.write(content)

for file in glob.glob('src/main/java/com/ersin/legitbridge/**/*.java', recursive=True):
    replace_inlines(file)

