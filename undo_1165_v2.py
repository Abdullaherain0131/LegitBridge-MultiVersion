import os

replacements = {
    """//? if <=1.19.2 {
/*public void onHudRender(MatrixStack context, float tickDelta) {
*///?} else {
    public void onHudRender(net.minecraft.client.gui.DrawContext context, float tickDelta) {
//?}""": "    public void onHudRender(net.minecraft.client.gui.DrawContext context, float tickDelta) {",
    
    """//? if <=1.19.2 {
/*import net.minecraft.network.Packet;
*///?} else {
import net.minecraft.network.packet.Packet;
//?}""": "import net.minecraft.network.packet.Packet;",
    
    """//? if <=1.19.2 {
/*import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
*///?} else {
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
//?}""": "import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;",
    
    """//? if <=1.19.2 {
/*this.children.clear();
*///?} else {
this.clearChildren();
//?}""": "this.clearChildren();",

    "client.//? if <=1.19.2 {\n/*player.inventory*/\n//?} else {\nplayer.getInventory()\n//?}": "client.player.getInventory()",
}

for root, _, files in os.walk("src/main/java"):
    for file in files:
        if file.endswith(".java"):
            path = os.path.join(root, file)
            with open(path, "r") as f:
                content = f.read()
            
            modified = False
            for old, new in replacements.items():
                if old in content:
                    content = content.replace(old, new)
                    modified = True
            
            if modified:
                with open(path, "w") as f:
                    f.write(content)
                print(f"Restored {path}")
