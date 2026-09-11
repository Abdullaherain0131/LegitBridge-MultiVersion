import os
import glob
import re

files = glob.glob("src/main/java/com/ersin/legitbridge/**/*.java", recursive=True)

def safe_replace(content, pattern, repl):
    # Only replace if not already wrapped
    # It's tricky to do purely with regex if it's already wrapped, so we will first revert any wraps.
    return re.sub(pattern, repl, content)

for fpath in files:
    with open(fpath, "r", encoding="utf-8") as f:
        content = f.read()

    orig = content
    
    # Let's clean up any existing stonecutter tags for these specific fields first to prevent double wrapping.
    # Note: earlier we ran revert_errors.py, so it should be mostly clean. But just in case:
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.inventory/\*\?\} else \{\*/(.*?)\.getInventory\(\)/\*\?\}\*/', r'\1.getInventory()', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.yaw/\*\?\} else \{\*/(.*?)\.getYaw\(\)/\*\?\}\*/', r'\1.getYaw()', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.pitch/\*\?\} else \{\*/(.*?)\.getPitch\(\)/\*\?\}\*/', r'\1.getPitch()', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.yaw = .*?;/\*\?\} else \{\*/(.*?)\.setYaw\((.*?)\);/\*\?\}\*/', r'\1.setYaw(\2);', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.pitch = .*?;/\*\?\} else \{\*/(.*?)\.setPitch\((.*?)\);/\*\?\}\*/', r'\1.setPitch(\2);', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.keyAttack/\*\?\} else \{\*/(.*?)\.attackKey/\*\?\}\*/', r'\1.attackKey', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.keyUse/\*\?\} else \{\*/(.*?)\.useKey/\*\?\}\*/', r'\1.useKey', content)
    
    # Also clean up any that might have been wrapped as /*? if <=1.19.2 {*/.../*?} else {*//*...*//*?}*/
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.inventory/\*\?\} else \{\*/\/\*(.*?)\.getInventory\(\)\*/\/\*\?\}\*/', r'\1.getInventory()', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.yaw/\*\?\} else \{\*/\/\*(.*?)\.getYaw\(\)\*/\/\*\?\}\*/', r'\1.getYaw()', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.pitch/\*\?\} else \{\*/\/\*(.*?)\.getPitch\(\)\*/\/\*\?\}\*/', r'\1.getPitch()', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.yaw = .*?;/\*\?\} else \{\*/\/\*(.*?)\.setYaw\((.*?)\);\*/\/\*\?\}\*/', r'\1.setYaw(\2);', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.pitch = .*?;/\*\?\} else \{\*/\/\*(.*?)\.setPitch\((.*?)\);\*/\/\*\?\}\*/', r'\1.setPitch(\2);', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.keyAttack/\*\?\} else \{\*/\/\*(.*?)\.attackKey\*/\/\*\?\}\*/', r'\1.attackKey', content)
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?\.keyUse/\*\?\} else \{\*/\/\*(.*?)\.useKey\*/\/\*\?\}\*/', r'\1.useKey', content)

    # Now we apply the correct wrappers.
    # Note: we use \b to ensure we match the exact method/field name
    
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.getInventory\(\)', r'/*? if <=1.19.2 {*/\1.inventory/*?} else {*/\1.getInventory()/*?}*/', content)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.getYaw\(\)', r'/*? if <=1.19.2 {*/\1.yaw/*?} else {*/\1.getYaw()/*?}*/', content)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.getPitch\(\)', r'/*? if <=1.19.2 {*/\1.pitch/*?} else {*/\1.getPitch()/*?}*/', content)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.setYaw\((.*?)\)', r'/*? if <=1.19.2 {*/\1.yaw = \2/*?} else {*/\1.setYaw(\2)/*?}*/', content)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.setPitch\((.*?)\)', r'/*? if <=1.19.2 {*/\1.pitch = \2/*?} else {*/\1.setPitch(\2)/*?}*/', content)
    
    # options fields might be client.options.attackKey or just options.attackKey.
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.attackKey', r'/*? if <=1.19.2 {*/\1.keyAttack/*?} else {*/\1.attackKey/*?}*/', content)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.useKey', r'/*? if <=1.19.2 {*/\1.keyUse/*?} else {*/\1.useKey/*?}*/', content)
    
    # There are also some `keyAttack` still remaining from the old codebase that we should target directly,
    # because some files were not converted to attackKey by previous agents!
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.keyAttack', r'/*? if <=1.19.2 {*/\1.keyAttack/*?} else {*/\1.attackKey/*?}*/', content)
    content = re.sub(r'(\b[a-zA-Z0-9_\.]+)\.keyUse', r'/*? if <=1.19.2 {*/\1.keyUse/*?} else {*/\1.useKey/*?}*/', content)
    
    # We should clean double wraps if the above hit a double wrap because of keyAttack replaced -> /*? if ... /*? if ...
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?/\*\?\} else \{\*/(/\*\? if <=1\.19\.2 \{\*/.*?\*/\/\*\?\}\*/)/\*\?\}\*/', r'\1', content)

    if content != orig:
        with open(fpath, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Updated {fpath}")

