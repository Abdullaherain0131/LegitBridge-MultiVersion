import re
import glob

def fix(file_path):
    with open(file_path, 'r') as f:
        content = f.read()
    orig = content
    
    # Let's use regex to find ALL broken inline key properties
    # /*? if <=1.19.2 {*/client.options.keySneak/*?} else {*/client.options.sneakKey/*?}*/
    pattern = r'/\*\? if <=1\.19\.2 \{\*/(client\.options\.[a-zA-Z0-9_]+)/\*\?\} else \{\*/(client\.options\.[a-zA-Z0-9_]+)/\*\?\}\*/'
    
    # We replace them by wrapping the entire statement.
    # But some statements are long.
    # Alternatively, we can just replace them with a valid stonecutter block:
    # Actually, Stonecutter inline does not work like `/*? if <= ...`.
    # It just doesn't! 
    # It's better to just use standard java reflection for these if we can't compile.
    
    # Wait, the easiest way is to find the whole line and wrap it!
    # Because most of these are like `client.options.keySneak.setPressed(true);`
    lines = content.split('\n')
    for i, line in enumerate(lines):
        if '/*? if <=1.19.2 {*/client.options.' in line:
            # We have a line with inline keys. Let's extract the two versions of the line.
            # E.g. line is: `    /*? if <=1.19.2 {*/client.options.keySneak/*?} else {*/client.options.sneakKey/*?}*/.setPressed(true);`
            # We want:
            # //? if <=1.19.2 {
            # /*    client.options.keySneak.setPressed(true); */
            # //?} else {
            #     client.options.sneakKey.setPressed(true);
            # //?}
            
            # Find the match
            match = re.search(pattern, line)
            if match:
                v116 = match.group(1)
                v120 = match.group(2)
                
                line_116 = line.replace(match.group(0), v116)
                line_120 = line.replace(match.group(0), v120)
                
                new_lines = f'//? if <=1.19.2 {{\n/*{line_116}*/\n//?}} else {{\n{line_120}\n//?}}'
                lines[i] = new_lines
                
        # NinjaBridge yaw
        elif '/*? if <=1.19.2 {*/client.player.yaw/*?} else {*/client.player.getYaw()/*?}*/' in line:
            l116 = line.replace('/*? if <=1.19.2 {*/client.player.yaw/*?} else {*/client.player.getYaw()/*?}*/', 'client.player.yaw')
            l120 = line.replace('/*? if <=1.19.2 {*/client.player.yaw/*?} else {*/client.player.getYaw()/*?}*/', 'client.player.getYaw()')
            lines[i] = f'//? if <=1.19.2 {{\n/*{l116}*/\n//?}} else {{\n{l120}\n//?}}'
            
        elif '/*? if <=1.19.2 {*/client.player.pitch/*?} else {*/client.player.getPitch()/*?}*/' in line:
            l116 = line.replace('/*? if <=1.19.2 {*/client.player.pitch/*?} else {*/client.player.getPitch()/*?}*/', 'client.player.pitch')
            l120 = line.replace('/*? if <=1.19.2 {*/client.player.pitch/*?} else {*/client.player.getPitch()/*?}*/', 'client.player.getPitch()')
            lines[i] = f'//? if <=1.19.2 {{\n/*{l116}*/\n//?}} else {{\n{l120}\n//?}}'

    content = '\n'.join(lines)
    
    # MixinMinecraftClientFastPlace special fix
    if 'MixinMinecraftClientFastPlace' in file_path:
        content = content.replace('if (ModConfig.enabled && this.player != null &&\n/*    if (ModConfig.enabled && this.player != null && this.options.keyUse.isPressed()) { */\n//?} else {\n    if (ModConfig.enabled && this.player != null && this.options.useKey.isPressed()) {\n//?}',
        '''//? if <=1.19.2 {
/*if (ModConfig.enabled && this.player != null && this.options.keyUse.isPressed()) {*/
//?} else {
if (ModConfig.enabled && this.player != null && this.options.useKey.isPressed()) {
//?}''')
        # Also let's just rewrite MixinMinecraftClientFastPlace from scratch if it's too broken
        pass

    if orig != content:
        with open(file_path, 'w') as f:
            f.write(content)

for file in glob.glob('src/main/java/com/ersin/legitbridge/**/*.java', recursive=True):
    fix(file)
    
