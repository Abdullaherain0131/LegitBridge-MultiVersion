import re
import glob

files = glob.glob('src/main/java/com/ersin/legitbridge/**/*.java', recursive=True)
for file in files:
    with open(file, 'r') as f:
        content = f.read()
    
    orig = content
    
    # openScreen -> setScreen for 1.20
    content = content.replace('client.openScreen(', '/*? if <=1.19.2 {*/client.openScreen(/*?} else {*/client.setScreen(/*?}*/')
    
    # LiteralText in DurabilityWarning
    if 'DurabilityWarning' in file:
        content = content.replace('new LiteralText', '/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText/*?} else {*/net.minecraft.text.Text.literal/*?}*/')
        
    if orig != content:
        with open(file, 'w') as f:
            f.write(content)

