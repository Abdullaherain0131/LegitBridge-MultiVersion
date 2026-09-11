import os
import re

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Revert block replacements
    # Pattern: 
    # [indent]//? if <=1.19 {
    # [content19]
    # [indent]//?} else {
    # [indent]/*[content20]
    # [indent]*///?}
    
    # We want to replace the whole block with content19, keeping the original formatting.
    # Actually we can just do a multi-line regex
    pattern = re.compile(r'^[ \t]*//\? if <=1\.19 \{\n(.*?)\n[ \t]*//\?\} else \{\n[ \t]*/\*(.*?)\n[ \t]*\*\/\//\?\}\n', re.MULTILINE | re.DOTALL)
    
    def replacer(match):
        return match.group(1) + '\n'
        
    new_content = pattern.sub(replacer, content)
    
    if new_content != content:
        with open(filepath, 'w') as f:
            f.write(new_content)
        print(f"Reverted blocks in {filepath}")

for root, _, files in os.walk('src/main/java/com/ersin/legitbridge'):
    for file in files:
        if file.endswith('.java'):
            process_file(os.path.join(root, file))

