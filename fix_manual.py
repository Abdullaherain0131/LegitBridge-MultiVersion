import os
import re

files_to_fix = [
    "src/main/java/com/ersin/legitbridge/helpers/AutoTotemHelper.java",
    "src/main/java/com/ersin/legitbridge/ModHudRenderer.java",
    "src/main/java/com/ersin/legitbridge/module/impl/AutoArmor.java",
    "src/main/java/com/ersin/legitbridge/module/impl/AutoBlockDefense.java",
    "src/main/java/com/ersin/legitbridge/module/impl/AutoEat.java",
    "src/main/java/com/ersin/legitbridge/module/impl/AutoMlg.java",
    "src/main/java/com/ersin/legitbridge/module/impl/AutoPot.java",
    "src/main/java/com/ersin/legitbridge/module/impl/AutoRefill.java",
    "src/main/java/com/ersin/legitbridge/module/impl/AutoTotemModule.java",
    "src/main/java/com/ersin/legitbridge/module/impl/AutoWeapon.java",
    "src/main/java/com/ersin/legitbridge/module/impl/CombatAssist.java",
    "src/main/java/com/ersin/legitbridge/module/impl/DurabilityWarning.java",
    "src/main/java/com/ersin/legitbridge/module/impl/LavaEvader.java",
    "src/main/java/com/ersin/legitbridge/module/impl/LegitAutoPot.java",
    "src/main/java/com/ersin/legitbridge/module/impl/LegitAutoRod.java",
    "src/main/java/com/ersin/legitbridge/module/impl/SmartTorch.java",
    "src/main/java/com/ersin/legitbridge/module/impl/VoidSave.java"
]

def fix_content(filepath):
    if not os.path.exists(filepath):
        return
    with open(filepath, 'r') as f:
        content = f.read()

    # The most common breaks are:
    # `oldSlot = \nclient.player.inventory` -> `oldSlot = client.player.inventory.selectedSlot;`
    # `client.player.inventory\nclient.player.inventory` -> `client.player.inventory.selectedSlot = oldSlot;` (or something similar)
    # `ItemStack stack = \nclient.player.inventory` -> `ItemStack stack = client.player.inventory.getStack(i);`
    # `client.player.inventory\n                client.options.keyUse.setPressed(true);` -> `client.player.inventory.selectedSlot = foodSlot;`
    # `client.player.pitch` hanging
    
    # Let's just fix the hanging references by replacing newlines appropriately if it's broken.
    pass

