import os

filepath = "src/main/java/com/ersin/legitbridge/module/impl/AutoTool.java"
with open(filepath, "r") as f:
    lines = f.readlines()

new_lines = []
skip = False
for i, line in enumerate(lines):
    if "PlayerInventory inventory =" in line:
        new_lines.append("""        //? if <=1.19.2 {
/*        PlayerInventory inventory = client.player.inventory;*/
        //?} else {
        PlayerInventory inventory = (PlayerInventory) client.player.getInventory();
        //?}\n""")
        skip = True
        continue
    
    if skip:
        if "//?}" in line:
            # check if it's the last one
            if i+1 < len(lines) and "//?}" not in lines[i+1] and "Auto Weapon on Entity Target" in lines[i+1] or i>30:
                # wait, let's just skip until the next comment
                pass
            if "        // Auto Weapon on Entity Target" in line:
                skip = False
                new_lines.append(line)
            continue
        if "        // Auto Weapon on Entity Target" in line:
            skip = False
            new_lines.append(line)
            continue
        continue
    
    new_lines.append(line)

with open(filepath, "w") as f:
    f.writelines(new_lines)
