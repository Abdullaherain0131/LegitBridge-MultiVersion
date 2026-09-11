import os
import glob

replacements = {
    "public void onHudRender(net.minecraft.client.gui.DrawContext context, float tickDelta) {": """//? if <=1.19.2 {
/*public void onHudRender(MatrixStack context, float tickDelta) {
*///?} else {
    public void onHudRender(net.minecraft.client.gui.DrawContext context, float tickDelta) {
//?}""",
    "import net.minecraft.network.packet.Packet;": """//? if <=1.19.2 {
/*import net.minecraft.network.Packet;
*///?} else {
import net.minecraft.network.packet.Packet;
//?}""",
    "import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;": """//? if <=1.19.2 {
/*import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
*///?} else {
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
//?}""",
    "player.getWorld()": "/*? if <=1.19.2 {*//*player.world*//*?} else {*/player.getWorld()/*?}*/",
    "player.getPitch()": "/*? if <=1.19.2 {*//*player.pitch*//*?} else {*/player.getPitch()/*?}*/",
    "player.getYaw()": "/*? if <=1.19.2 {*//*player.yaw*//*?} else {*/player.getYaw()/*?}*/",
    "client.options.useKey": "/*? if <=1.19.2 {*//*client.options.keyUse*//*?} else {*/client.options.useKey/*?}*/",
    "client.options.attackKey": "/*? if <=1.19.2 {*//*client.options.keyAttack*//*?} else {*/client.options.attackKey/*?}*/",
    "client.options.forwardKey": "/*? if <=1.19.2 {*//*client.options.keyForward*//*?} else {*/client.options.forwardKey/*?}*/",
    "client.options.backKey": "/*? if <=1.19.2 {*//*client.options.keyBack*//*?} else {*/client.options.backKey/*?}*/",
    "client.options.leftKey": "/*? if <=1.19.2 {*//*client.options.keyLeft*//*?} else {*/client.options.leftKey/*?}*/",
    "client.options.rightKey": "/*? if <=1.19.2 {*//*client.options.keyRight*//*?} else {*/client.options.rightKey/*?}*/",
    "client.options.jumpKey": "/*? if <=1.19.2 {*//*client.options.keyJump*//*?} else {*/client.options.jumpKey/*?}*/",
    "client.options.sneakKey": "/*? if <=1.19.2 {*//*client.options.keySneak*//*?} else {*/client.options.sneakKey/*?}*/",
    "client.player.getInventory()": "/*? if <=1.19.2 {*//*client.player.inventory*//*?} else {*/client.player.getInventory()/*?}*/",
    "player.getInventory()": "/*? if <=1.19.2 {*//*player.inventory*//*?} else {*/player.getInventory()/*?}*/",
    "this.clearChildren();": """//? if <=1.19.2 {
/*this.children.clear();
*///?} else {
this.clearChildren();
//?}"""
}

# Add special case for getBoundingBox -> getBoundingBox()
replacements["player.getBoundingBox()"] = "/*? if <=1.19.2 {*//*player.getBoundingBox()*//*?} else {*/player.getBoundingBox()/*?}*/"

for root, _, files in os.walk("src/main/java"):
    for file in files:
        if file.endswith(".java"):
            path = os.path.join(root, file)
            with open(path, "r") as f:
                content = f.read()
            
            modified = False
            for old, new in replacements.items():
                if old in content and new not in content:
                    content = content.replace(old, new)
                    modified = True
            
            if modified:
                with open(path, "w") as f:
                    f.write(content)
                print(f"Fixed {path}")
