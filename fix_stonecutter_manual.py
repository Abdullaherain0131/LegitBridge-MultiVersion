import re
import os

files = [
    "src/main/java/com/ersin/legitbridge/command/ModCommands.java",
    "src/main/java/com/ersin/legitbridge/gui/ModConfigScreen.java",
    "src/main/java/com/ersin/legitbridge/gui/ModGuideScreen.java",
    "src/main/java/com/ersin/legitbridge/mixin/MixinChatHud.java",
    "src/main/java/com/ersin/legitbridge/module/impl/StructureFinder.java"
]

def fix_file(filepath):
    if not os.path.exists(filepath):
        return
    with open(filepath, 'r') as f:
        content = f.read()

    # Remove unmatched openers and closers to avoid Stonecutter errors.
    # We will just strip ALL `//?` comments from these files.
    new_content = re.sub(r'[ \t]*//\?.*?\n', '', content)
    
    # if it's inline, remove it too
    new_content = re.sub(r'/\*\?.*?\?\*/', '', new_content)
    
    if new_content != content:
        with open(filepath, 'w') as f:
            f.write(new_content)
        print("Stripped stonecutter from", filepath)

for f in files:
    fix_file(f)

