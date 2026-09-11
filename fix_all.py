import os
import re

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Match ANY Stonecutter 1.19 block and replace with the 1.19 code
    # Block pattern:
    # //? if <=1.19 {
    # [code1]
    # //?} else {
    # /*[code2]
    # *///?}
    
    # We want to catch everything that looks like that, regardless of starting characters.
    # We also want to catch the inline comments: /*? if <=1.19 { ?*/ [code1] /*?} else { ?*//*[code2]*//*? } ?*/
    
    changed = False
    
    # 1. Fix the inline ones first:
    inline_pattern = re.compile(r'/\*\?[ \t]*if[ \t]+<=1\.19[ \t]*\{[ \t]*\?\*/(.*?)/\*\?\}[ \t]*else[ \t]*\{[ \t]*\?\*/\s*/\*(.*?)\*/\s*/\*\?[ \t]*\}[ \t]*\?\*/')
    if inline_pattern.search(content):
        content = inline_pattern.sub(r'\1', content)
        changed = True

    # 2. Fix the multi-line ones:
    # Sometimes it's like: int oldSlot = //? if <=1.19 {\n client.player.inventory \n //?} else { ...
    # This regex is a bit tricky, let's use a non-greedy dotall
    block_pattern = re.compile(r'//\?[ \t]*if[ \t]+<=1\.19[ \t]*\{\n(.*?)\n[ \t]*//\?\}[ \t]*else[ \t]*\{\n[ \t]*/\*(.*?)\n[ \t]*\*\/\//\?\}(?:\n|$)', re.DOTALL)
    
    # Actually, we can just replace the whole block with group(1)
    if block_pattern.search(content):
        content = block_pattern.sub(r'\1\n', content)
        changed = True
        
    # Wait, what if it's:
    # int oldSlot = //? if <=1.19 {
    # client.player...
    # We need to handle this by stripping out the stonecutter comments and leaving the code.
    # A generic stonecutter stripper for 1.19:
    # Just remove `//? if <=1.19 {`, `//?} else {`, `/*`, `*///?}`
    # Let's write a simple state machine to strip stonecutter blocks and keep 1.19 branch.
    
    if changed:
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Fixed {filepath}")

for root, _, files in os.walk('src/main/java/com/ersin/legitbridge'):
    for file in files:
        if file.endswith('.java'):
            process_file(os.path.join(root, file))
