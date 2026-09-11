import os
import re

def replace_in_file(path, replacements):
    if not os.path.exists(path):
        return
    with open(path, 'r') as f:
        content = f.read()
    
    modified = False
    for old, new in replacements:
        if old in content or isinstance(old, re.Pattern):
            if isinstance(old, re.Pattern):
                content = old.sub(new, content)
            else:
                content = content.replace(old, new)
            modified = True
            
    if modified:
        with open(path, 'w') as f:
            f.write(content)

base_dir = "src/main/java/com/ersin/legitbridge"

# interactItem -> VersionHelper.interactItem
interact_pattern = re.compile(r'client\.interactionManager\.interactItem\(([^,]+),\s*(Hand\.[A-Z_]+)\);')
interact_repl = r'com.ersin.legitbridge.utils.VersionHelper.interactItem(client, \2);'

for root, _, files in os.walk(base_dir):
    for file in files:
        if file.endswith(".java"):
            path = os.path.join(root, file)
            with open(path, 'r') as f:
                c = f.read()
            if interact_pattern.search(c):
                c = interact_pattern.sub(interact_repl, c)
                with open(path, 'w') as f:
                    f.write(c)

# StepHeight
replace_in_file(f"{base_dir}/utils/VersionHelper.java", [
    ("public static net.minecraft.block.Block getBlockById(String id) {",
     """public static void setStepHeight(net.minecraft.client.network.ClientPlayerEntity player, float height) {
//? if >1.19.2 {
        player.setStepHeight(height);
//?} else {
        /*player.stepHeight = height;*///?}
    }
    
    public static net.minecraft.block.Block getBlockById(String id) {""")
])

replace_in_file(f"{base_dir}/module/impl/Step.java", [
    ("client.player.setStepHeight(stepHeight);", "com.ersin.legitbridge.utils.VersionHelper.setStepHeight(client.player, stepHeight);"),
    ("client.player.setStepHeight(previousStepHeight);", "com.ersin.legitbridge.utils.VersionHelper.setStepHeight(client.player, previousStepHeight);")
])

# NoFall & Derp & Freecam
replace_in_file(f"{base_dir}/module/impl/NoFall.java", [
    ("new PlayerMoveC2SPacket.OnGroundOnly(true)",
     """//? if >1.19.2 {
            new PlayerMoveC2SPacket.OnGroundOnly(true)
            //?} else {
            /*new PlayerMoveC2SPacket(true)*///?}""")
])

derp_path = f"{base_dir}/module/impl/Derp.java"
replace_in_file(derp_path, [
    ("boolean changesLook = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;",
     """//? if >1.19.2 {
            boolean changesLook = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
            //?} else {
            /*boolean changesLook = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookOnly || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Both;*///?}"""),
    
    ("boolean changesPosition = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;",
     """//? if >1.19.2 {
            boolean changesPosition = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
            //?} else {
            /*boolean changesPosition = p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionOnly || p instanceof net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Both;*///?}"""),
    
    ("newPacket = new PlayerMoveC2SPacket.Full(x, y, z, newYaw, newPitch, p.isOnGround());",
     """//? if >1.19.2 {
                    newPacket = new PlayerMoveC2SPacket.Full(x, y, z, newYaw, newPitch, p.isOnGround());
                    //?} else {
                    /*newPacket = new PlayerMoveC2SPacket.Both(x, y, z, newYaw, newPitch, p.isOnGround());*///?}"""),
                    
    ("newPacket = new PlayerMoveC2SPacket.LookAndOnGround(newYaw, newPitch, p.isOnGround());",
     """//? if >1.19.2 {
                    newPacket = new PlayerMoveC2SPacket.LookAndOnGround(newYaw, newPitch, p.isOnGround());
                    //?} else {
                    /*newPacket = new PlayerMoveC2SPacket.LookOnly(newYaw, newPitch, p.isOnGround());*///?}""")
])

freecam_path = f"{base_dir}/module/impl/Freecam.java"
replace_in_file(freecam_path, [
    ("client.world.removeEntity(-1000, net.minecraft.entity.Entity.RemovalReason.DISCARDED);",
     """//? if >1.19.2 {
                client.world.removeEntity(-1000, net.minecraft.entity.Entity.RemovalReason.DISCARDED);
                //?} else {
                /*client.world.removeEntity(-1000);*///?}""")
])

replace_in_file(f"{base_dir}/utils/RotationUtil.java", [
    ("client.options.getMouseSensitivity().getValue().floatValue()",
     """//? if >1.19.2 {
        client.options.getMouseSensitivity().getValue().floatValue()
        //?} else {
        /*(float)client.options.mouseSensitivity*///?}""")
])

print("Patches applied")
