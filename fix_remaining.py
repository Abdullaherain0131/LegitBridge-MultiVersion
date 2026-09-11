import re

# ModCommands.java
with open('src/main/java/com/ersin/legitbridge/command/ModCommands.java', 'r') as f:
    text = f.read()
text = text.replace('/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText/*?} else {*/net.minecraft.text.Text.literal/*?}*/(text)',
                    '/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText(text)/*?} else {*/net.minecraft.text.Text.literal(text)/*?}*/')
with open('src/main/java/com/ersin/legitbridge/command/ModCommands.java', 'w') as f:
    f.write(text)

# LegitBridgeMod.java
with open('src/main/java/com/ersin/legitbridge/LegitBridgeMod.java', 'r') as f:
    text = f.read()
text = text.replace('/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText/*?} else {*/net.minecraft.text.Text.literal/*?}*/("LegitBridge Aktif!")',
                    '/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText("LegitBridge Aktif!")/*?} else {*/net.minecraft.text.Text.literal("LegitBridge Aktif!")/*?}*/')
with open('src/main/java/com/ersin/legitbridge/LegitBridgeMod.java', 'w') as f:
    f.write(text)

# AutoTool.java
with open('src/main/java/com/ersin/legitbridge/module/impl/AutoTool.java', 'r') as f:
    text = f.read()
text = text.replace('/*? if <=1.19.2 {*/client.player.inventory/*?} else {*/client.player.getInventory()/*?}*/.selectedSlot = bestSlot;',
                    '/*? if <=1.19.2 {*/client.player.inventory.selectedSlot = bestSlot;/*?} else {*/client.player.getInventory().selectedSlot = bestSlot;/*?}*/')
text = text.replace('/*? if <=1.19.2 {*//*client.player.inventory*//*?} else {*/client.player.getInventory()/*?}*/.selectedSlot = bestSlot;',
                    '/*? if <=1.19.2 {*/client.player.inventory.selectedSlot = bestSlot;/*?} else {*/client.player.getInventory().selectedSlot = bestSlot;/*?}*/')
with open('src/main/java/com/ersin/legitbridge/module/impl/AutoTool.java', 'w') as f:
    f.write(text)

# Freecam.java
with open('src/main/java/com/ersin/legitbridge/module/impl/Freecam.java', 'r') as f:
    text = f.read()
text = text.replace('/*? if <=1.19.2 {*//*client.player.inventory*//*?} else {*/client.player.getInventory()/*?}*/',
                    '/*? if <=1.19.2 {*/client.player.inventory/*?} else {*/client.player.getInventory()/*?}*/')
text = text.replace('/*? if <=1.19.2 {*//*client.player.yaw*//*?} else {*/client.player.getYaw()/*?}*/ = originalYaw;',
                    '/*? if <=1.19.2 {*/client.player.yaw = originalYaw;/*?} else {*/client.player.setYaw(originalYaw);/*?}*/')
text = text.replace('/*? if <=1.19.2 {*//*client.player.pitch*//*?} else {*/client.player.getPitch()/*?}*/ = originalPitch;',
                    '/*? if <=1.19.2 {*/client.player.pitch = originalPitch;/*?} else {*/client.player.setPitch(originalPitch);/*?}*/')
text = text.replace('client.player.abilities', '/*? if <=1.19.2 {*/client.player.abilities/*?} else {*/client.player.getAbilities()/*?}*/')
text = text.replace('client.world.removeEntity(-1000);', '/*? if <=1.19.2 {*/client.world.removeEntity(-1000);/*?} else {*/client.world.removeEntity(-1000, net.minecraft.entity.Entity.RemovalReason.DISCARDED);/*?}*/')
with open('src/main/java/com/ersin/legitbridge/module/impl/Freecam.java', 'w') as f:
    f.write(text)

# Fullbright.java
with open('src/main/java/com/ersin/legitbridge/module/impl/Fullbright.java', 'r') as f:
    text = f.read()
text = text.replace('client.options.gamma', '/*? if <=1.19.2 {*/client.options.gamma/*?} else {*/client.options.getGamma().getValue()/*?}*/')
text = text.replace('/*? if <=1.19.2 {*/client.options.gamma/*?} else {*/client.options.getGamma().getValue()/*?}*/ = 100.0;',
                    '/*? if <=1.19.2 {*/client.options.gamma = 100.0;/*?} else {*/client.options.getGamma().setValue(100.0);/*?}*/')
text = text.replace('/*? if <=1.19.2 {*/client.options.gamma/*?} else {*/client.options.getGamma().getValue()/*?}*/ = originalGamma;',
                    '/*? if <=1.19.2 {*/client.options.gamma = originalGamma;/*?} else {*/client.options.getGamma().setValue(originalGamma);/*?}*/')
with open('src/main/java/com/ersin/legitbridge/module/impl/Fullbright.java', 'w') as f:
    f.write(text)

# FireballAssist.java
with open('src/main/java/com/ersin/legitbridge/module/impl/FireballAssist.java', 'r') as f:
    text = f.read()
text = text.replace('/*? if <=1.19.2 {*//*client.player.pitch*//*?} else {*/client.player.getPitch()/*?}*/ += (targetPitch - currentPitch) / 2.0f;',
                    '/*? if <=1.19.2 {*/client.player.pitch += (targetPitch - currentPitch) / 2.0f;/*?} else {*/client.player.setPitch(client.player.getPitch() + (targetPitch - currentPitch) / 2.0f);/*?}*/')
text = text.replace('/*? if <=1.19.2 {*//*client.player.pitch*//*?} else {*/client.player.getPitch()/*?}*/ = targetPitch;',
                    '/*? if <=1.19.2 {*/client.player.pitch = targetPitch;/*?} else {*/client.player.setPitch(targetPitch);/*?}*/')
with open('src/main/java/com/ersin/legitbridge/module/impl/FireballAssist.java', 'w') as f:
    f.write(text)

