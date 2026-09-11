import os
import glob
import re

files = glob.glob('src/main/java/com/ersin/legitbridge/**/*.java', recursive=True)

for filepath in files:
    with open(filepath, "r") as f:
        content = f.read()

    # Pattern 1: The recent one
    content = content.replace(
        "/*? if <=1.19.2 {*//*client.player.inventory.selectedSlot*//*?} else {*/((net.minecraft.entity.player.PlayerInventory)client.player.getInventory()).selectedSlot/*?}*/",
        "com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot"
    )
    
    # Pattern 2: The old one from fix_global.py
    content = content.replace(
        "/*? if <=1.19.2 { ?*/client.player.inventory/*?} else { ?*/client.player.getInventory()/*?} ?*/",
        "com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player)"
    )
    
    # What about the ones fix_inline2 just changed? It might have missed some or matched some incorrectly.
    # We should search for any `client.player.getInventory()` without comments maybe?
    # No, we will fix other inline comments separately.
    
    with open(filepath, "w") as f:
        f.write(content)
