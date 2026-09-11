import os
import glob

files = glob.glob('src/main/java/com/ersin/legitbridge/**/*.java', recursive=True)

for filepath in files:
    with open(filepath, "r") as f:
        content = f.read()

    # Find where PlayerInventory inventory = ... is messed up
    if "PlayerInventory inventory =\n" in content or "PlayerInventory inventory = \n" in content:
        # We need to extract this and fix it.
        # It's better to just regex it out.
        import re
        new_content = re.sub(
            r'PlayerInventory\s+inventory\s*=\s*//\? if <=1\.19\.2 \{.*?//\?}',
            r'//? if <=1.19.2 {\n/*        PlayerInventory inventory = client.player.inventory;*/\n        //?} else {\n        PlayerInventory inventory = (PlayerInventory) client.player.getInventory();\n        //?}',
            content,
            flags=re.DOTALL
        )
        if new_content != content:
            with open(filepath, "w") as f:
                f.write(new_content)
            print("Fixed inventory variable in", filepath)

