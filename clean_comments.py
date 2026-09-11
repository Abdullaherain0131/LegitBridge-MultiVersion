import os
import glob
import re

files = glob.glob("src/main/java/com/ersin/legitbridge/**/*.java", recursive=True)

for fpath in files:
    with open(fpath, "r", encoding="utf-8") as f:
        content = f.read()

    orig = content
    
    # Strip commented out old lines that might cause issues
    # e.g. /*        if (client.options.keyAttack.isPressed()) {*/
    lines = content.split('\n')
    new_lines = []
    for line in lines:
        stripped = line.strip()
        if stripped.startswith("/*") and stripped.endswith("*/") and "client." in line:
            # Skip this line, it's an old commented out code line
            continue
        # also match /*? if ... that got inside a comment block
        if stripped.startswith("/*") and "/*?" in line and stripped.endswith("*/"):
            continue
        new_lines.append(line)
        
    content = "\n".join(new_lines)
    
    # Let's also fix the specific double wrap if it exists.
    content = re.sub(r'/\*\? if <=1\.19\.2 \{\*/\/\*\? if <=1\.19\.2 \{\*/(.*?)/\*\?\} else \{\*/.*?\*/\/\*\?\}\*/\/\*\?\} else \{\*/.*?\*/\/\*\?\}\*/', r'/*? if <=1.19.2 {*/\1/*?} else {*/\1/*?}*/', content)
    
    if content != orig:
        with open(fpath, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Cleaned comments in {fpath}")

