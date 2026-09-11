import os
import glob
import re

files = glob.glob("src/main/java/com/ersin/legitbridge/**/*.java", recursive=True)

for fpath in files:
    with open(fpath, "r", encoding="utf-8") as f:
        content = f.read()

    orig = content

    # Keep applying inline block un-nesting until no more match.
    # The regex matches /*? ... {*/ ... /*?} else {*/ (TARGET) /*?}*/
    # We want to extract the TARGET which is the 1.20.1 branch.
    
    # We will use a regex that matches the innermost /*? ... /*?}*/ block
    # It must NOT contain /*? inside it.
    
    while True:
        # Match an inline stonecutter block that does not contain another inline block inside it
        # and extract its 'else' part (the 1.20.1 code).
        # Format: /*? if <=1.19.2 {*/ [code1] /*?} else {*/ [code2] /*?}*/
        # We want [code2]. We might also have commented out code2: /*[code2]*/
        
        # Regex to find innermost block:
        # /\*\? if [^/]*?\{\*/(.*?)/\*\?\} else \{\*/(.*?)/\*\?\}\*/
        # wait, we must make sure (.*?) doesn't contain /*?
        
        pattern = r'/\*\?[^{]*?\{\*/(?:(?!(?:/\*\?)).)*?/\*\?\} else \{\*/((?:(?!(?:/\*\?)).)*?)/\*\?\}\*/'
        
        match = re.search(pattern, content, re.DOTALL)
        if not match:
            break
            
        # extract the target
        target = match.group(1)
        # if the target is wrapped in /* ... */, unwrap it
        if target.startswith("/*") and target.endswith("*/"):
            target = target[2:-2]
            
        content = content[:match.start()] + target + content[match.end():]

    if content != orig:
        with open(fpath, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Purged inline stonecutter from {fpath}")

