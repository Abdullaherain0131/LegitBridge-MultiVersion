import os
import glob
import re

files = glob.glob("src/main/java/com/ersin/legitbridge/**/*.java", recursive=True)

for fpath in files:
    with open(fpath, "r", encoding="utf-8") as f:
        content = f.read()

    orig = content
    
    # 1. Clean dangling else blocks.
    # Match from the start of the line or previous statement up to /*?} else {*/ and replace with group 1
    # Actually, we can just match any non-whitespace before it if it's part of the same identifier,
    # but the easiest way is to match from /*? if <=1.19.2 {*/ if it exists.
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/.*?/\*\?\} else \{\*/(.*?)/\*\?\}\*/', r'\1', content, flags=re.DOTALL)
    
    # For dangling ones that lost their `/*? if ...` we just match from the previous whitespace
    content = re.sub(r'\S+?/\*\?\} else \{\*/(.*?)/\*\?\}\*/', r'\1', content)
    
    # Sometimes it's like: client.options.keyUse/*?} else {*/client.options.useKey/*?}*/
    content = re.sub(r'[a-zA-Z0-9_\.]*/\*\?\} else \{\*/(.*?)/\*\?\}\*/', r'\1', content)
    
    # In CombatAssist: client.player.getYaw(/*?} else {*/client.player.setYaw
    # This is missing the end! Let's just nuke all `/*?} else {*/` and `/*?}*/` and clean up the syntax manually for those 3 files if they are broken.
    content = content.replace("/*?} else {*/", "")
    content = content.replace("/*?}*/", "")
    content = content.replace("/*? if <=1.19.2 {*/", "")
    
    # Now we have raw concatenated strings like `client.player.getYaw(client.player.setYaw`
    # Let's fix specific known mangled words
    content = content.replace("client.options.keyAttackclient.options.attackKey", "client.options.attackKey")
    content = content.replace("client.options.keyUseclient.options.useKey", "client.options.useKey")
    content = content.replace("client.player.getYaw(client.player.setYaw", "client.player.setYaw")
    content = content.replace("client.player.getPitch(client.player.setPitch", "client.player.setPitch")
    content = content.replace("player.getPitch() = 70.0f;player.setPitch(70.0f);", "player.setPitch(70.0f);")
    content = content.replace("player.getPitch() = originalPitch;player.setPitch(originalPitch);", "player.setPitch(originalPitch);")
    
    if content != orig:
        with open(fpath, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Purged completely: {fpath}")

