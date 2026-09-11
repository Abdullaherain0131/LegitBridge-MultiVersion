import os
import re

def process_file(filepath):
    with open(filepath, 'r') as f:
        lines = f.readlines()
        
    # First, handle inline /*? if <=1.19 { ?*/
    inline_pattern = re.compile(r'/\*\?[ \t]*if[ \t]+<=1\.19[ \t]*\{[ \t]*\?\*/(.*?)/\*\?\}[ \t]*else[ \t]*\{[ \t]*\?\*/\s*/\*(.*?)\*/\s*/\*\?[ \t]*\}[ \t]*\?\*/')
    
    out = []
    state = "NORMAL"
    changed = False
    
    for original_line in lines:
        line = original_line
        
        # apply inline replacements first
        if inline_pattern.search(line):
            line = inline_pattern.sub(r'\1', line)
            changed = True
            
        # check for multi-line block openers
        if "//? if <=1.19 {" in line:
            state = "IN_19"
            pre = line.split("//?")[0]
            if pre.strip():
                # We need to not append \n if it's going to split an expression badly? 
                # Java doesn't care about newlines in expressions mostly.
                # But wait, let's keep it on the same line if possible, or just let Java handle the newline.
                # Actually, let's just append the prefix and we will join it.
                out.append(pre) 
                if not pre.endswith('\n'):
                    out.append('\n')
            changed = True
            continue
        elif "//?} else {" in line:
            state = "IN_20"
            changed = True
            continue
        elif "*///?}" in line:
            state = "NORMAL"
            changed = True
            continue
            
        if state == "IN_19":
            out.append(line)
        elif state == "IN_20":
            # Just ignore lines in the 1.20 block
            pass
        elif state == "NORMAL":
            out.append(line)
            
    if changed:
        with open(filepath, 'w') as f:
            f.writelines(out)
        print(f"Fixed {filepath}")

for root, _, files in os.walk('src/main/java/com/ersin/legitbridge'):
    for file in files:
        if file.endswith('.java'):
            process_file(os.path.join(root, file))
