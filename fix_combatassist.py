import re
with open('src/main/java/com/ersin/legitbridge/module/impl/CombatAssist.java', 'r') as f:
    content = f.read()

# Revert my bad assignment replacements
content = content.replace('/*? if <=1.19.2 {*/client.player.yaw/*?} else {*/client.player.getYaw()/*?}*/ += yawDiff / smoothing;',
    '/*? if <=1.19.2 {*/client.player.yaw += yawDiff / smoothing;/*?} else {*/client.player.setYaw(client.player.getYaw() + yawDiff / smoothing);/*?}*/')

content = content.replace('/*? if <=1.19.2 {*/client.player.pitch/*?} else {*/client.player.getPitch()/*?}*/ += pitchDiff / (smoothing * 0.9f);',
    '/*? if <=1.19.2 {*/client.player.pitch += pitchDiff / (smoothing * 0.9f);/*?} else {*/client.player.setPitch(client.player.getPitch() + pitchDiff / (smoothing * 0.9f));/*?}*/')

with open('src/main/java/com/ersin/legitbridge/module/impl/CombatAssist.java', 'w') as f:
    f.write(content)
