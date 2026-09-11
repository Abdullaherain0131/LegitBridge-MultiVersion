import os
import glob
import re

files = glob.glob("src/main/java/com/ersin/legitbridge/**/*.java", recursive=True)

for fpath in files:
    with open(fpath, "r", encoding="utf-8") as f:
        content = f.read()

    orig = content
    
    # First, let's strip ALL inline stonecutter blocks for inventory, yaw, pitch, attackKey, useKey entirely
    # to get back to pure 1.20.1 syntax!
    
    # This regex matches the whole inline block and extracts the 1.20.1 else branch.
    # We do this iteratively until no more are found, so nested ones get resolved from inside out.
    while True:
        prev = content
        content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?/\*\?\} else \{\*/(.*?)/\*\?\}\*/', r'\1', content)
        if prev == content:
            break
            
    # Also strip the commented-out versions (e.g. /*client.player.getPitch()*/)
    while True:
        prev = content
        content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?/\*\?\} else \{\*/\/\*(.*?)\*/\/\*\?\}\*/', r'\1', content)
        if prev == content:
            break

    # Now we have pure 1.20.1. Let's do carefully ordered replacements so we DON'T nest.
    # 1. Match compound statements: setPitch(getPitch() + X)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.setPitch\(\1\.getPitch\(\) \+ (.*?)\);', r'/*? if <=1.19.2 {*/\1.pitch += \2;/*?} else {*/\1.setPitch(\1.getPitch() + \2);/*?}*/', content)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.setPitch\(\1\.getPitch\(\) \- (.*?)\);', r'/*? if <=1.19.2 {*/\1.pitch -= \2;/*?} else {*/\1.setPitch(\1.getPitch() - \2);/*?}*/', content)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.setYaw\(\1\.getYaw\(\) \+ (.*?)\);', r'/*? if <=1.19.2 {*/\1.yaw += \2;/*?} else {*/\1.setYaw(\1.getYaw() + \2);/*?}*/', content)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.setYaw\(\1\.getYaw\(\) \- (.*?)\);', r'/*? if <=1.19.2 {*/\1.yaw -= \2;/*?} else {*/\1.setYaw(\1.getYaw() - \2);/*?}*/', content)
    
    # 2. Match normal setters
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.setPitch\((.*?)\);', r'/*? if <=1.19.2 {*/\1.pitch = \2;/*?} else {*/\1.setPitch(\2);/*?}*/', content)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.setYaw\((.*?)\);', r'/*? if <=1.19.2 {*/\1.yaw = \2;/*?} else {*/\1.setYaw(\2);/*?}*/', content)

    # 3. Match getters
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.getPitch\(\)', r'/*? if <=1.19.2 {*/\1.pitch/*?} else {*/\1.getPitch()/*?}*/', content)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.getYaw\(\)', r'/*? if <=1.19.2 {*/\1.yaw/*?} else {*/\1.getYaw()/*?}*/', content)
    
    # 4. Match inventory
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.getInventory\(\)', r'/*? if <=1.19.2 {*/\1.inventory/*?} else {*/\1.getInventory()/*?}*/', content)
    
    # 5. Options
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.attackKey', r'/*? if <=1.19.2 {*/\1.keyAttack/*?} else {*/\1.attackKey/*?}*/', content)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.useKey', r'/*? if <=1.19.2 {*/\1.keyUse/*?} else {*/\1.useKey/*?}*/', content)
    
    # Note: Some files had `.keyAttack` in 1.20.1 because we didn't migrate them.
    # Convert them to attackKey then apply wrapping.
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.keyAttack', r'/*? if <=1.19.2 {*/\1.keyAttack/*?} else {*/\1.attackKey/*?}*/', content)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.keyUse', r'/*? if <=1.19.2 {*/\1.keyUse/*?} else {*/\1.useKey/*?}*/', content)
    
    # Strip nested wrappers if any somehow snuck through the keyAttack->attackKey conversion
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?/\*\?\} else \{\*/(/\*\? if <=1\.19\.2 \{\*/.*?\*/\/\*\?\}\*/)/\*\?\}\*/', r'\1', content)

    if content != orig:
        with open(fpath, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Cleaned {fpath}")

