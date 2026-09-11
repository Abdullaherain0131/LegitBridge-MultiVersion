import re
import os

files_to_fix = [
    "src/main/java/com/ersin/legitbridge/helpers/AutoTotemHelper.java",
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

def fix_file(filepath):
    if not os.path.exists(filepath):
        return
    with open(filepath, 'r') as f:
        content = f.read()

    changed = False

    # 1. `oldSlot = \nclient.player.inventory\nclient.player.inventory\n` ->
    # `oldSlot = client.player.inventory.selectedSlot;\nclient.player.inventory.selectedSlot = targetSlot;\n`
    # Wait, we don't know `targetSlot`. Let's just fix the `oldSlot = ...` part first.
    
    # oldSlot = \nclient.player.inventory
    new_content, n = re.subn(r'(oldSlot\s*=\s*)\n(client\.player\.inventory)', r'\1\2.selectedSlot;', content)
    if n > 0:
        content = new_content
        changed = True
        
    # ItemStack stack = \nclient.player.inventory
    new_content, n = re.subn(r'(ItemStack\s+\w+\s*=\s*)\n(client\.player\.inventory)', r'\1\2.getStack(i);', content)
    if n > 0:
        content = new_content
        changed = True
        
    # client.player.pitch hanging
    new_content, n = re.subn(r'client\.player\.pitch(\s*)$', r'client.player.pitch = 80.0f;\1', content, flags=re.MULTILINE)
    # wait, this might match lines that are already complete but don't end in `;`? No.
    
    if changed:
        with open(filepath, 'w') as f:
            f.write(content)
        print("Fixed some things in", filepath)

for f in files_to_fix:
    fix_file(f)

