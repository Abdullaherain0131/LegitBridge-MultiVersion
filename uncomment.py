import os
import glob
import re

files = glob.glob("src/main/java/com/ersin/legitbridge/**/*.java", recursive=True)

for fpath in files:
    with open(fpath, "r", encoding="utf-8") as f:
        content = f.read()
        
    orig = content

    content = content.replace("/*client.options.rightKey*/", "client.options.rightKey")
    content = content.replace("if client.options.attackKey.isPressed()) {", "if (client.options.attackKey.isPressed()) {")
    content = content.replace("if client.options.useKey.isPressed()", "if (client.options.useKey.isPressed()")
    content = content.replace("/*getId()*/", "getId()")
    content = content.replace("/*PlayerMoveC2SPacket.LookAndOnGround*/", "PlayerMoveC2SPacket.LookAndOnGround")
    content = content.replace("/*PlayerMoveC2SPacket.Full*/", "PlayerMoveC2SPacket.Full")
    content = content.replace("/*PlayerMoveC2SPacket.PositionAndOnGround*/", "PlayerMoveC2SPacket.PositionAndOnGround")
    content = content.replace("/*client.options.sneakKey*/", "client.options.sneakKey")
    content = content.replace("/*client.options.forwardKey*/", "client.options.forwardKey")
    content = content.replace("/*client.options.getMouseSensitivity().getValue().floatValue()*/", "client.options.getMouseSensitivity().getValue().floatValue()")
    
    # Missing opening paren for `if client.options.attackKey` in LegitWTap and LegitAutoClicker
    content = content.replace("if client.options.attackKey", "if (client.options.attackKey")
    
    # LegitBridgeMod issue
    content = content.replace("/*client.setScreen(*/new ModConfigScreen());", "client.setScreen(new ModConfigScreen());")
    content = content.replace("client.setScreen(new ModConfigScreen());)", "client.setScreen(new ModConfigScreen());")

    # More replacements based on grep:
    content = content.replace("/*client.options.backKey*/", "client.options.backKey")
    content = content.replace("/*client.options.leftKey*/", "client.options.leftKey")
    content = content.replace("/*matrixStack.peek().getPositionMatrix()*/", "matrixStack.peek().getPositionMatrix()")
    content = content.replace("/*RenderSystem.disableTexture();*/", "RenderSystem.disableTexture();")
    content = content.replace("/*RenderSystem.enableTexture();*/", "RenderSystem.enableTexture();")
    content = content.replace("/*buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINES, net.minecraft.client.render.VertexFormats.POSITION_COLOR);*/", "buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINES, net.minecraft.client.render.VertexFormats.POSITION_COLOR);")
    content = content.replace("/*buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINE_STRIP, *//*net.minecraft.client.render.VertexFormats.POSITION_COLOR*/", "buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINE_STRIP, net.minecraft.client.render.VertexFormats.POSITION_COLOR")
    content = content.replace("/*buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.QUADS, net.minecraft.client.render.VertexFormats.POSITION_COLOR);*/", "buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.QUADS, net.minecraft.client.render.VertexFormats.POSITION_COLOR);")
    content = content.replace("/*client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);*/", "client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);")
    content = content.replace("/*client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, (net.minecraft.util.hit.BlockHitResult) client.crosshairTarget);*/", "client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, (net.minecraft.util.hit.BlockHitResult) client.crosshairTarget);")
    content = content.replace("/*client.interactionManager.interactBlock(client.player, hand, hitResult);*/", "client.interactionManager.interactBlock(client.player, hand, hitResult);")
    content = content.replace("/*client.player.pitch = -90f;*/", "client.player.pitch = -90f;")
    content = content.replace("/*client.player.setPitch(90f);*/", "client.player.setPitch(90f);")
    content = content.replace("/*client.player.setPitch(oldPitch);*/", "client.player.setPitch(oldPitch);")
    content = content.replace("/*item == net.minecraft.item.Items.MUSHROOM_STEW*/", "item == net.minecraft.item.Items.MUSHROOM_STEW")

    # Replace commented multiline codes that start with /* and end with */ 
    # e.g., /*float yaw = player.yaw;\n*/
    content = re.sub(r'/\*float pitch = player\.pitch;\n\*/', r'float pitch = player.pitch;\n', content)
    content = re.sub(r'/\*float yaw = player\.yaw;\n\*/', r'float yaw = player.yaw;\n', content)
    content = re.sub(r'/\*if \(entity\.removed\) continue;\n\*/', r'if (entity.removed) continue;\n', content)

    # Missing parts in LegitAutoClicker, LegitWTap, PerfectCrit, AutoWeapon
    content = content.replace("/*        if (client.options.attackKey.isPressed()) {*/", "        if (client.options.attackKey.isPressed()) {")
    content = content.replace("/*        if (client.options.attackKey.isPressed() && client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {*/", "        if (client.options.attackKey.isPressed() && client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {")
    content = content.replace("/*            client.options.attackKey.setPressed(true);*/", "            client.options.attackKey.setPressed(true);")
    content = content.replace("/*            net.minecraft.client.option.KeyBinding.onKeyPressed(client.options.attackKey.getDefaultKey());*/", "            net.minecraft.client.option.KeyBinding.onKeyPressed(client.options.attackKey.getDefaultKey());")

    content = content.replace("/*                float oldPitch = client.player.pitch;*/", "                float oldPitch = client.player.pitch;")
    content = content.replace("/*                        originalPitch = client.player.pitch;*/", "                        originalPitch = client.player.pitch;")
    content = content.replace("/*                        client.player.pitch = 90.0f;*/", "                        client.player.pitch = 90.0f;")
    content = content.replace("/*            if (client.player.pitch < 85.0f) {*/", "            if (client.player.pitch < 85.0f) {")
    
    # BowAimbot
    content = content.replace("/*                float yawDiff = MathHelper.wrapDegrees(targetYaw - client.player.yaw);*/", "                float yawDiff = MathHelper.wrapDegrees(targetYaw - client.player.yaw);")
    content = content.replace("/*                float pitchDiff = targetPitch - client.player.pitch;*/", "                float pitchDiff = targetPitch - client.player.pitch;")
    content = content.replace("/*client.player.yaw += yawDiff * smooth;*/", "client.player.yaw += yawDiff * smooth;")
    content = content.replace("/*client.player.pitch += pitchDiff * smooth;*/", "client.player.pitch += pitchDiff * smooth;")

    # InvMove
    content = content.replace("/*            client.options.forwardKey,*/", "            client.options.forwardKey,")
    content = content.replace("/*            client.options.keyJump,*/", "            client.options.keyJump,")
    content = content.replace("/*            client.options.keySprint*/", "            client.options.keySprint")

    if content != orig:
        with open(fpath, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Fixed uncomments in {fpath}")
