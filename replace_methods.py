import os
import re

replacements = [
    # Keys
    (r"client\.options\.useKey\.isPressed\(\)", "com.ersin.legitbridge.utils.VersionHelper.isUseKeyPressed(client)"),
    (r"client\.options\.attackKey\.isPressed\(\)", "com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client)"),
    (r"client\.options\.useKey\.setPressed\((.*?)\)", r"com.ersin.legitbridge.utils.VersionHelper.setUseKeyPressed(client, \1)"),
    
    # World
    (r"player\.getWorld\(\)", "com.ersin.legitbridge.utils.VersionHelper.getWorld(player)"),
    (r"this\.player\.getWorld\(\)", "com.ersin.legitbridge.utils.VersionHelper.getWorld(this.player)"),
    (r"client\.player\.getWorld\(\)", "com.ersin.legitbridge.utils.VersionHelper.getWorld(client.player)"),
    
    # Remove entity
    (r"client\.world\.removeEntity\((.*?), Entity\.RemovalReason\.DISCARDED\)", r"com.ersin.legitbridge.utils.VersionHelper.removeEntity(client, \1)"),

    # Packet import
    (r"import net\.minecraft\.network\.packet\.Packet;", 
     "//? if <=1.19.2 {\n/*import net.minecraft.network.Packet;\n*///?} else {\nimport net.minecraft.network.packet.Packet;\n//?}"),
]

for root, _, files in os.walk("src/main/java"):
    for file in files:
        if file.endswith(".java") and file != "VersionHelper.java":
            path = os.path.join(root, file)
            with open(path, "r") as f:
                content = f.read()
            
            modified = False
            for old, new in replacements:
                content, count = re.subn(old, new, content)
                if count > 0:
                    modified = True
            
            if modified:
                with open(path, "w") as f:
                    f.write(content)
                print(f"Patched {path}")
