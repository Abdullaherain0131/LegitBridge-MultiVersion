import os
import re

files_to_fix = [
    "src/main/java/com/ersin/legitbridge/module/impl/BowAimbot.java",
    "src/main/java/com/ersin/legitbridge/module/impl/CombatAssist.java",
    "src/main/java/com/ersin/legitbridge/module/impl/FireballAssist.java",
    "src/main/java/com/ersin/legitbridge/module/impl/LegitAutoPot.java",
    "src/main/java/com/ersin/legitbridge/module/impl/Killaura.java",
    "src/main/java/com/ersin/legitbridge/module/impl/AutoPot.java",
]

for fpath in files_to_fix:
    if not os.path.exists(fpath):
        continue
    with open(fpath, "r", encoding="utf-8") as f:
        content = f.read()

    # Generic un-nester for inline yaw/pitch replacements inside structural blocks
    content = re.sub(r'client\./\*\? if <=1\.19\.2 \{\*/player\.pitch = client\./\*\? if <=1\.19\.2 \{\*/player\.pitch/\*\?\} else \{\*/\/\*player\.getPitch\(\/\*\?\} else \{\*/\/\*player\.setPitch\(client\.player\.getPitch\(\)\*/\/\*\?\}\*/ \+ (.*?)\);', r'client.player.setPitch(client.player.getPitch() + \1);', content)
    content = re.sub(r'client\./\*\? if <=1\.19\.2 \{\*/player\.pitch = client\./\*\? if <=1\.19\.2 \{\*/player\.pitch/\*\?\} else \{\*/\/\*player\.getPitch\(\/\*\?\} else \{\*/\/\*player\.setPitch\(client\.player\.getPitch\(\)\*/\/\*\?\}\*/ \- (.*?)\);', r'client.player.setPitch(client.player.getPitch() - \1);', content)
    
    # Generic un-nester for yaw
    content = re.sub(r'client\./\*\? if <=1\.19\.2 \{\*/player\.yaw = client\./\*\? if <=1\.19\.2 \{\*/player\.yaw/\*\?\} else \{\*/\/\*player\.getYaw\(\/\*\?\} else \{\*/\/\*player\.setYaw\(client\.player\.getYaw\(\)\*/\/\*\?\}\*/ \+ (.*?)\);', r'client.player.setYaw(client.player.getYaw() + \1);', content)
    content = re.sub(r'client\./\*\? if <=1\.19\.2 \{\*/player\.yaw = client\./\*\? if <=1\.19\.2 \{\*/player\.yaw/\*\?\} else \{\*/\/\*player\.getYaw\(\/\*\?\} else \{\*/\/\*player\.setYaw\(client\.player\.getYaw\(\)\*/\/\*\?\}\*/ \- (.*?)\);', r'client.player.setYaw(client.player.getYaw() - \1);', content)

    # Some had /*? if <=1.19.2 {*/client.player.yaw += ...
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/client\.player\.yaw \+= (.*?);/\*\?\} else \{\*/\/\*.*?\*/\/\*\?\}\*/', r'client.player.setYaw(client.player.getYaw() + \1);', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/client\.player\.pitch \+= (.*?);/\*\?\} else \{\*/\/\*.*?\*/\/\*\?\}\*/', r'client.player.setPitch(client.player.getPitch() + \1);', content)

    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/client\.player\.yaw = (.*?);/\*\?\} else \{\*/\/\*.*?\*/\/\*\?\}\*/', r'client.player.setYaw(\1);', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/client\.player\.pitch = (.*?);/\*\?\} else \{\*/\/\*.*?\*/\/\*\?\}\*/', r'client.player.setPitch(\1);', content)
    
    # LegitAutoPot specific fixes
    if "LegitAutoPot" in fpath:
        content = content.replace("client.player.setPitch(client.player.getPitch() + 12.0f);", "client.player.setPitch(client.player.getPitch() + 12.0f);") # Ensure normal syntax
    
    with open(fpath, "w", encoding="utf-8") as f:
        f.write(content)
        
