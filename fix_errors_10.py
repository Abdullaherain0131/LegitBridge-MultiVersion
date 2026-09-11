import re

def patch_file(path, regexes):
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    orig = content
    for pattern, repl in regexes:
        content = re.sub(pattern, repl, content, flags=re.MULTILINE | re.DOTALL)
    if orig != content:
        with open(path, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Patched {path}")

import glob
files = glob.glob("src/main/java/com/ersin/legitbridge/**/*.java", recursive=True)

for f in files:
    # Do replacements safely. Note that some might have already been done by earlier scripts,
    # so we should be careful to only replace things that aren't inside stonecutter blocks.
    # Instead of regex for all files, let's target specific known strings since we have the list.

    # 1. getInventory()
    patch_file(f, [
        (r'(?<!\*/)client\.player\.getInventory\(\)', r'/*? if <=1.19.2 {*/client.player.inventory/*?} else {*//*client.player.getInventory()*//*?}*/')
    ])
    
    # 2. getPitch() and getYaw() with NO arguments.
    # Be careful not to replace getPitch(float) if it exists, but the error said "no arguments".
    patch_file(f, [
        (r'player\.getPitch\(\)', r'/*? if <=1.19.2 {*/player.pitch/*?} else {*//*player.getPitch()*//*?}*/'),
        (r'player\.getYaw\(\)', r'/*? if <=1.19.2 {*/player.yaw/*?} else {*//*player.getYaw()*//*?}*/'),
        (r'player\.setPitch\((.*?)\)', r'/*? if <=1.19.2 {*/player.pitch = \1/*?} else {*//*player.setPitch(\1)*//*?}*/'),
        (r'player\.setYaw\((.*?)\)', r'/*? if <=1.19.2 {*/player.yaw = \1/*?} else {*//*player.setYaw(\1)*//*?}*/'),
    ])

    # 3. isRemoved()
    patch_file(f, [
        (r'entity\.isRemoved\(\)', r'/*? if <=1.19.2 {*/entity.removed/*?} else {*//*entity.isRemoved()*//*?}*/')
    ])

    # 4. attackKey and sneakKey
    patch_file(f, [
        (r'client\.options\.attackKey', r'/*? if <=1.19.2 {*/client.options.keyAttack/*?} else {*//*client.options.attackKey*//*?}*/'),
        (r'client\.options\.sneakKey', r'/*? if <=1.19.2 {*/client.options.keySneak/*?} else {*//*client.options.sneakKey*//*?}*/')
    ])
    
# Fix RotationUtil.java unclosed scope manually
patch_file("src/main/java/com/ersin/legitbridge/utils/RotationUtil.java", [
    (r'/\*\? if <=1\.19\.2 \{\*/client\.options\.mouseSensitivity/\*\?\} else \{\*/\/\*client\.options\.getMouseSensitivity\(\)\.getValue\(\)/\*\?\}\*/', r'/*? if <=1.19.2 {*/client.options.mouseSensitivity/*?} else {*//*client.options.getMouseSensitivity().getValue().floatValue()*//*?}*/')
])

