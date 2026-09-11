import os
import re

def fix_file(filepath):
    if not filepath.endswith(".java"):
        return
    with open(filepath, "r") as f:
        lines = f.readlines()
        
    new_lines = []
    i = 0
    changed = False
    while i < len(lines):
        line = lines[i]
        
        # Check if line is exactly `client.player.inventory` (possibly with leading/trailing whitespace)
        if line.strip() == "client.player.inventory" or line.strip() == "dummyPlayer.inventory.clone(client.player.inventory":
            # Let's just fix the known cases.
            if "AutoWeapon.java" in filepath:
                if i > 0 and "if (bestSlot != -1 &&" in lines[i-1]:
                    new_lines.append("            client.player.inventory.selectedSlot != bestSlot) {\n")
                    new_lines.append("            client.player.inventory.selectedSlot = bestSlot;\n")
                    i += 1 # skip the next client.player.inventory
                    changed = True
                else:
                    new_lines.append(line)
            elif "SlotRotation.java" in filepath:
                new_lines.append("            client.player.inventory.selectedSlot = (client.player.inventory.selectedSlot + 1) % 9;\n")
                changed = True
            elif "AutoMlg.java" in filepath:
                if "return -1;" in lines[i+1]:
                    new_lines.append("        for (int i = 0; i < 9; i++) {\n")
                    new_lines.append("            if (client.player.inventory.getStack(i).getItem() == Items.WATER_BUCKET) {\n")
                    new_lines.append("                return i;\n")
                    new_lines.append("            }\n")
                    new_lines.append("        }\n")
                    changed = True
                else:
                    new_lines.append(line)
            elif "SmartTorch.java" in filepath:
                if "return -1;" in lines[i+1]:
                    new_lines.append("        for (int i = 0; i < 9; i++) {\n")
                    new_lines.append("            if (client.player.inventory.getStack(i).getItem() == Items.TORCH) {\n")
                    new_lines.append("                return i;\n")
                    new_lines.append("            }\n")
                    new_lines.append("        }\n")
                    changed = True
                else:
                    new_lines.append(line)
            elif "LavaEvader.java" in filepath:
                if "return -1;" in lines[i+1]:
                    new_lines.append("        for (int i = 0; i < 9; i++) {\n")
                    new_lines.append("            if (client.player.inventory.getStack(i).getItem() == Items.WATER_BUCKET) {\n")
                    new_lines.append("                return i;\n")
                    new_lines.append("            }\n")
                    new_lines.append("        }\n")
                    changed = True
                else:
                    new_lines.append(line)
            elif "AutoTotemHelper.java" in filepath or "AutoTotemModule.java" in filepath:
                new_lines.append("        if (client.player.inventory.offHand.get(0).getItem() == Items.TOTEM_OF_UNDYING) {\n")
                changed = True
            elif "AutoSupply.java" in filepath:
                new_lines.append("        if (client.player.inventory.getStack(client.player.inventory.selectedSlot).isEmpty()) {\n")
                changed = True
            elif "AutoTool.java" in filepath:
                new_lines.append("            client.player.inventory.selectedSlot = bestSlot;\n")
                changed = True
            elif "Freecam.java" in filepath:
                new_lines.append("            dummyPlayer.inventory.clone(client.player.inventory);\n")
                changed = True
            elif "ModHudRenderer.java" in filepath:
                new_lines.append("                net.minecraft.item.ItemStack stack = client.player.inventory.getStack(i);\n")
                changed = True
            else:
                new_lines.append(line)
        else:
            new_lines.append(line)
        i += 1
        
    if changed:
        with open(filepath, "w") as f:
            f.writelines(new_lines)
        print("Fixed", filepath)

for root, dirs, files in os.walk("src/main/java"):
    for file in files:
        fix_file(os.path.join(root, file))
