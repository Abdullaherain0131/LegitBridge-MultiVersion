import os
import glob
import re

files = glob.glob('src/main/java/**/*.java', recursive=True)

# Pattern to find: /*? if <=1.19.2 {*/ TRUE_BLOCK /*?} else {*/ FALSE_BLOCK /*?}*/
pattern = re.compile(r'/\*\?\s*if\s*(.*?)\s*\{\*/(.*?)/\*\?\} else \{\*/(.*?)/\*\?\}\*/')

def replacer(match):
    condition = match.group(1)
    true_block = match.group(2)
    false_block = match.group(3)
    
    # Strip any existing comments inside to be safe
    if true_block.startswith('/*') and true_block.endswith('*/'):
        true_block = true_block[2:-2]
    if false_block.startswith('/*') and false_block.endswith('*/'):
        false_block = false_block[2:-2]
        
    # Reconstruct properly: true_block is uncommented, false_block is commented
    return f"/*? if {condition} {{*/{true_block}/*?}} else {{*//*{false_block}*//*?}}*/"

for filepath in files:
    with open(filepath, "r") as f:
        content = f.read()

    new_content = pattern.sub(replacer, content)
    
    if new_content != content:
        with open(filepath, "w") as f:
            f.write(new_content)
        print("Fixed inline in", filepath)

