import re
import os

def fix_file(filepath):
    if not os.path.exists(filepath):
        return
    with open(filepath, 'r') as f:
        content = f.read()

    changed = False

    # 1. `client.interactionManager.interact` without `;`
    new_content, n = re.subn(r'(client\.interactionManager\.interact(?:Item|Block)\([^;]+?\))(\s*\n)', r'\1;\2', content)
    if n > 0:
        content = new_content
        changed = True
        
    # 2. LegitAutoPot.java
    if "LegitAutoPot" in filepath:
        new_content, n = re.subn(r'private int\[\] findHealingItem\(\) \{\s*private int\[\] findHealingItem\(\) \{', r'private int[] findHealingItem() {', content)
        if n > 0:
            content = new_content
            changed = True
        # also the missing closing brace?
        if "}" not in content[-5:]:
            content += "\n}\n"
            changed = True

    # 3. LegitAutoRod.java
    if "LegitAutoRod" in filepath:
        new_content, n = re.subn(r'client\.player\.inventory\s*\n\s*return -1;', r'client.player.inventory.selectedSlot = oldSlot;\n        return -1;', content)
        if n > 0:
            content = new_content
            changed = True

    # 4. AutoBlockDefense
    if "AutoBlockDefense" in filepath:
        new_content, n = re.subn(r'(int oldSlot =[^;]+;)\s*client\.player\.inventory\s*(client\.player\.pitch)', r'\1\n                        client.player.inventory.selectedSlot = i;\n                        \2', content)
        if n > 0:
            content = new_content
            changed = True
        
        # the end of the method might be broken
        new_content, n = re.subn(r'private int findBlockInHotbar\(\) \{\s*for', r'private int findBlockInHotbar() {\n        for', content)
        if n > 0:
            content = new_content
            changed = True
            
        new_content, n = re.subn(r'net\.minecraft\.item\.ItemStack stack = \s*client\.player\.inventory', r'net.minecraft.item.ItemStack stack = client.player.inventory.getStack(i);', content)
        if n > 0:
            content = new_content
            changed = True

    if changed:
        with open(filepath, 'w') as f:
            f.write(content)
        print("Fixed", filepath)

for root, _, files in os.walk('src/main/java'):
    for file in files:
        if file.endswith('.java'):
            fix_file(os.path.join(root, file))

