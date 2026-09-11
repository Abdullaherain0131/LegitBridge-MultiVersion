import re
import glob

def fix(file_path):
    with open(file_path, 'r') as f:
        content = f.read()

    # Mixin
    if 'MixinMinecraftClientFastPlace' in file_path:
        content = content.replace('this.options.useKey.isPressed()', 
'''//? if <=1.19.2 {
/*this.options.keyUse.isPressed()*/
//?} else {
this.options.useKey.isPressed()
//?}''')
        # wait! It's inside an IF statement!
        # if (ModConfig.enabled && this.player != null && this.options.useKey.isPressed()) {
        content = content.replace('if (ModConfig.enabled && this.player != null && //? if <=1.19.2 {', 'if (ModConfig.enabled && this.player != null &&')

    # Many places use client.player.getInventory()
    # It's hard to replace unconditionally. 
    
    with open(file_path, 'w') as f:
        f.write(content)

for file in glob.glob('src/main/java/com/ersin/legitbridge/**/*.java', recursive=True):
    fix(file)

