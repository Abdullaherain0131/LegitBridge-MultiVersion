import re
import glob

files = glob.glob("src/main/java/com/ersin/legitbridge/**/*.java", recursive=True)
for path in files:
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    orig = content
    
    # Revert getInventory()
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/client\.player\.inventory/\*\?\} else \{\*/\/\*client\.player\.getInventory\(\)\*/\/\*\?\}\*/', r'client.player.getInventory()', content)
    
    # Revert getPitch/getYaw
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/player\.pitch/\*\?\} else \{\*/\/\*player\.getPitch\(\)\*/\/\*\?\}\*/', r'player.getPitch()', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/player\.yaw/\*\?\} else \{\*/\/\*player\.getYaw\(\)\*/\/\*\?\}\*/', r'player.getYaw()', content)
    
    # Revert setPitch/setYaw - since (.*?) matched anything, we have to use a regex that matches the whole stonecutter block
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/player\.pitch = (.*?)/\*\?\} else \{\*/\/\*player\.setPitch\(\1\)\*/\/\*\?\}\*/', r'player.setPitch(\1)', content, flags=re.DOTALL)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/player\.yaw = (.*?)/\*\?\} else \{\*/\/\*player\.setYaw\(\1\)\*/\/\*\?\}\*/', r'player.setYaw(\1)', content, flags=re.DOTALL)
    
    # Revert removed
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/entity\.removed/\*\?\} else \{\*/\/\*entity\.isRemoved\(\)\*/\/\*\?\}\*/', r'entity.isRemoved()', content)

    # Clean up any nested blocks that might have been created (e.g., in Killaura.java)
    # Actually Killaura had something like:
    # client./*? if <=1.19.2 {*/player.yaw = client.player.getYaw() + yawDiff / smoothing/*?} else {*//*player.setYaw(...)*//*?}*/
    # If the above replacements didn't fix it, we'll see.
    
    if orig != content:
        with open(path, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Reverted {path}")
