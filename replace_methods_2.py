import os
import re

replacements = [
    # Pitch / Yaw
    (r"([a-zA-Z0-9_\.]+)\.getPitch\(\)", r"com.ersin.legitbridge.utils.VersionHelper.getPitch(\1)"),
    (r"([a-zA-Z0-9_\.]+)\.getYaw\(\)", r"com.ersin.legitbridge.utils.VersionHelper.getYaw(\1)"),
    (r"([a-zA-Z0-9_\.]+)\.setPitch\((.*?)\)", r"com.ersin.legitbridge.utils.VersionHelper.setPitch(\1, \2)"),
    (r"([a-zA-Z0-9_\.]+)\.setYaw\((.*?)\)", r"com.ersin.legitbridge.utils.VersionHelper.setYaw(\1, \2)"),
    
    # Text
    (r"Text\.literal\((.*?)\)", r"com.ersin.legitbridge.utils.VersionHelper.literalText(\1)"),
    
    # Registries
    (r"Registries\.ITEM\.getId\((.*?)\)", r"com.ersin.legitbridge.utils.VersionHelper.getItemId(\1)"),
    
    # Abilities and Inventory
    (r"([a-zA-Z0-9_\.]+)\.getAbilities\(\)", r"com.ersin.legitbridge.utils.VersionHelper.getAbilities(\1)"),
    (r"([a-zA-Z0-9_\.]+)\.getInventory\(\)", r"com.ersin.legitbridge.utils.VersionHelper.getInventory(\1)"),
    
    # client.setScreen
    (r"client\.setScreen\((.*?)\)", r"com.ersin.legitbridge.utils.VersionHelper.setScreen(client, \1)"),
    
    # Entity canHit
    (r"([a-zA-Z0-9_\.]+)\.canHit\(\)", r"com.ersin.legitbridge.utils.VersionHelper.isAttackable(\1)"),
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
