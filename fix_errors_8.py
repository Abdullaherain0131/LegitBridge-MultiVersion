import re
import sys
import glob

def patch_file(path, regexes):
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    orig = content
    for pattern, repl in regexes:
        content = re.sub(pattern, repl, content)
    if orig != content:
        with open(path, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Patched {path}")

mod_config = "src/main/java/com/ersin/legitbridge/gui/ModConfigScreen.java"
patch_file(mod_config, [
    (r'/\*event = SoundEvents\.ENTITY_PLAYER_LEVELUP\.value\(\);\*/', r'/*event = SoundEvents.UI_BUTTON_CLICK.value();*/'),
    (r'public void renderButton', r'public void render'),
    (r'public boolean isPauseScreen\(\)', r'/*? if <=1.19.2 {*/\n    public boolean isPauseScreen() {\n/*?} else {*//*    public boolean shouldPause() {*//*?}*/')
])

mod_guide = "src/main/java/com/ersin/legitbridge/gui/ModGuideScreen.java"
patch_file(mod_guide, [
    (r'public boolean isPauseScreen\(\)', r'/*? if <=1.19.2 {*/\n    public boolean isPauseScreen() {\n/*?} else {*//*    public boolean shouldPause() {*//*?}*/')
])

fireball = "src/main/java/com/ersin/legitbridge/module/impl/FireballAssist.java"
patch_file(fireball, [
    (r'client\.player\.getPitch\(\) = (.*?);', r'client.player.setPitch(\1);'),
    (r'client\.player\.getYaw\(\) = (.*?);', r'client.player.setYaw(\1);')
])

freecam = "src/main/java/com/ersin/legitbridge/module/impl/Freecam.java"
patch_file(freecam, [
    (r'client\.player\.getYaw\(\) = (.*?);', r'client.player.setYaw(\1);'),
    (r'client\.player\.getPitch\(\) = (.*?);', r'client.player.setPitch(\1);'),
    (r'dummyPlayer\.inventory\.clone', r'/*? if <=1.19.2 {*/dummyPlayer.inventory.clone/*?} else {*//*dummyPlayer.getInventory().clone*//*?}*/')
])

fullbright = "src/main/java/com/ersin/legitbridge/module/impl/Fullbright.java"
patch_file(fullbright, [
    (r'client\.options\.getGamma\(\) = (.*?);', r'client.options.getGamma().setValue(\1);')
])

worldrender = "src/main/java/com/ersin/legitbridge/module/impl/WorldRender.java"
patch_file(worldrender, [
    (r'buffer\.begin\(1, /\*\? if <=1\.19\.2 \{\*/VertexFormats\.POSITION_COLOR/\*\?\} else \{\*/\/\*net\.minecraft\.client\.render\.VertexFormats\.POSITION_COLOR\*/\/\*\?\}\*/\);', r'/*? if <=1.19.2 {*/buffer.begin(1, VertexFormats.POSITION_COLOR);/*?} else {*//*buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINES, net.minecraft.client.render.VertexFormats.POSITION_COLOR);*//*?}*/'),
    (r'buffer\.begin\(7, /\*\? if <=1\.19\.2 \{\*/VertexFormats\.POSITION_COLOR/\*\?\} else \{\*/\/\*net\.minecraft\.client\.render\.VertexFormats\.POSITION_COLOR\*/\/\*\?\}\*/\);', r'/*? if <=1.19.2 {*/buffer.begin(7, VertexFormats.POSITION_COLOR);/*?} else {*//*buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.QUADS, net.minecraft.client.render.VertexFormats.POSITION_COLOR);*//*?}*/'),
    (r'buffer\.begin\(3, /\*\? if <=1\.19\.2 \{\*/VertexFormats\.POSITION_COLOR/\*\?\} else \{\*/\/\*net\.minecraft\.client\.render\.VertexFormats\.POSITION_COLOR\*/\/\*\?\}\*/\);', r'/*? if <=1.19.2 {*/buffer.begin(3, VertexFormats.POSITION_COLOR);/*?} else {*//*buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINE_STRIP, net.minecraft.client.render.VertexFormats.POSITION_COLOR);*//*?}*/'),
    (r'MathHelper\.sqrt\(', r'/*? if <=1.19.2 {*/MathHelper.sqrt(/*?} else {*//*Math.sqrt(*//*?}*/')
])

invmove = "src/main/java/com/ersin/legitbridge/module/impl/InvMove.java"
patch_file(invmove, [
    (r'client\.options\.keysAll', r'/*? if <=1.19.2 {*/client.options.keysAll/*?} else {*//*client.options.allKeys*//*?}*/')
])

legitwtap = "src/main/java/com/ersin/legitbridge/module/impl/LegitWTap.java"
patch_file(legitwtap, [
    (r'client\.options\.keyForward', r'/*? if <=1.19.2 {*/client.options.keyForward/*?} else {*//*client.options.forwardKey*//*?}*/')
])

for f in ["LavaEvader.java", "LegitAutoPot.java", "LegitAutoRod.java"]:
    path = f"src/main/java/com/ersin/legitbridge/module/impl/{f}"
    patch_file(path, [
        (r'client\.player\.getPitch\(\) = (.*?);', r'client.player.setPitch(\1);'),
        (r'client\.player\.getYaw\(\) = (.*?);', r'client.player.setYaw(\1);'),
        (r'client\.interactionManager\.interactItem\(client\.player, client\.world, (.*?)\);', r'/*? if <=1.19.2 {*/client.interactionManager.interactItem(client.player, client.world, \1);/*?} else {*//*client.interactionManager.interactItem(client.player, \1);*//*?}*/')
    ])

rotutil = "src/main/java/com/ersin/legitbridge/utils/RotationUtil.java"
patch_file(rotutil, [
    (r'float diff = MathHelper\.wrapDegrees\(\(float\) \(', r'float diff = MathHelper.wrapDegrees((float) (')
])

