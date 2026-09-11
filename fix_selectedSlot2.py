import os
import glob
import re

files = glob.glob('src/main/java/com/ersin/legitbridge/**/*.java', recursive=True)

for filepath in files:
    with open(filepath, "r") as f:
        content = f.read()

    new_content = re.sub(
        r'client\.player\.getInventory\(\)\.selectedSlot',
        r'/*? if <=1.19.2 {*//*client.player.inventory.selectedSlot*//*?} else {*/((net.minecraft.entity.player.PlayerInventory)client.player.getInventory()).selectedSlot/*?}*/',
        content
    )

    if new_content != content:
        with open(filepath, "w") as f:
            f.write(new_content)
        print("Fixed selectedSlot in", filepath)
