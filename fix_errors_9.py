import re

def patch_file(path, regexes):
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    orig = content
    for pattern, repl in regexes:
        content = re.sub(pattern, repl, content, flags=re.MULTILINE | re.DOTALL)
    if orig != content:
        with open(path, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Patched {path}")

# 1. MixinEntitySafeWalk.java
patch_file("src/main/java/com/ersin/legitbridge/mixin/MixinEntitySafeWalk.java", [
    (r'player\.world\.isClient', r'player.getWorld().isClient'),
    (r'player\.pitch > 60\.0f', r'player.getPitch() > 60.0f')
])

# 2. MixinEntity.java
patch_file("src/main/java/com/ersin/legitbridge/mixin/MixinEntity.java", [
    (r'getEntityId\(\)', r'/*? if <=1.19.2 {*/getEntityId()/*?} else {*//*getId()*//*?}*/')
])

# 3. MixinClientPlayNetworkHandler.java
patch_file("src/main/java/com/ersin/legitbridge/mixin/MixinClientPlayNetworkHandler.java", [
    (r'client\.player\.getEntityId\(\)', r'/*? if <=1.19.2 {*/client.player.getEntityId()/*?} else {*//*client.player.getId()*//*?}*/')
])

# 4. ModConfigScreen.java
patch_file("src/main/java/com/ersin/legitbridge/gui/ModConfigScreen.java", [
    (r'this\.fillGradient\(context, 0, 0, this\.width, this\.height, 0xD0000000, 0xF0000000\);', r'/*? if <=1.19.2 {*/this.fillGradient((net.minecraft.client.util.math.MatrixStack)context, 0, 0, this.width, this.height, 0xD0000000, 0xF0000000);/*?} else {*//*((net.minecraft.client.gui.DrawContext)context).fillGradient(0, 0, this.width, this.height, 0xD0000000, 0xF0000000);*//*?}*/'),
    (r'^\s*@Override\s*//\? if <=1\.19\.2 \{\s*public void render', r'        //? if <=1.19.2 {\n        public void render')
])

# 5. Fullbright.java
patch_file("src/main/java/com/ersin/legitbridge/module/impl/Fullbright.java", [
    (r'/\*\? if <=1\.19\.2 \{\*/client\.options\.gamma/\*\?\} else \{\*/\/\*client\.options\.getGamma\(\)\.getValue\(\)\*/\/\*\?\}\*/ = originalGamma != -1 \? originalGamma : 1\.0;', 
     r'/*? if <=1.19.2 {*/\n            client.options.gamma = originalGamma != -1 ? originalGamma : 1.0;\n            /*?} else {*//*            client.options.getGamma().setValue(originalGamma != -1 ? originalGamma : 1.0);*//*?}*/')
])

# 6. WorldRender.java
patch_file("src/main/java/com/ersin/legitbridge/module/impl/WorldRender.java", [
    (r'for \(net\.minecraft\.block\.entity\.BlockEntity be : client\.world\.blockEntities\) \{(.*?)\}', r'//? if <=1.19.2 {\n        for (net.minecraft.block.entity.BlockEntity be : client.world.blockEntities) {\1}\n//?}')
])

# 7. InvMove.java
patch_file("src/main/java/com/ersin/legitbridge/module/impl/InvMove.java", [
    (r'client\.options\.keyBack', r'/*? if <=1.19.2 {*/client.options.keyBack/*?} else {*//*client.options.backKey*//*?}*/'),
    (r'client\.options\.keyLeft', r'/*? if <=1.19.2 {*/client.options.keyLeft/*?} else {*//*client.options.leftKey*//*?}*/'),
    (r'client\.options\.keyRight', r'/*? if <=1.19.2 {*/client.options.keyRight/*?} else {*//*client.options.rightKey*//*?}*/')
])

# 8. LegitAutoPot.java
patch_file("src/main/java/com/ersin/legitbridge/module/impl/LegitAutoPot.java", [
    (r'client\.player\.getPitch\(\) -= 12\.0f;', r'client.player.setPitch(client.player.getPitch() - 12.0f);')
])

# 9. RotationUtil.java
patch_file("src/main/java/com/ersin/legitbridge/utils/RotationUtil.java", [
    (r'/\*\? if <=1\.19\.2 \{\*/\/\*client\.options\.mouseSensitivity\*/\/\*\?\} else \{\*/client\.options\.getMouseSensitivity\(\)\.getValue\(\)/\*\?\}\*/', r'/*? if <=1.19.2 {*//*client.options.mouseSensitivity*//*?} else {*/client.options.getMouseSensitivity().getValue().floatValue()/*?}*/')
])

