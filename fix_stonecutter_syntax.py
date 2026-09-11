import os
import glob
import re

files = glob.glob('src/main/java/com/ersin/legitbridge/**/*.java', recursive=True)

for file in files:
    with open(file, 'r') as f:
        content = f.read()
    
    # Replace my broken inline tags:
    content = content.replace('/*? if <=1.19.2 { ?*/', '/*? if <=1.19.2 {*/')
    content = content.replace('/*?} else { ?*/', '/*?} else {*/')
    content = content.replace('/*?} ?*/', '/*?}*/')

    with open(file, 'w') as f:
        f.write(content)

