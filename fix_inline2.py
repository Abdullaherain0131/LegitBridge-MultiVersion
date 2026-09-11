import os
import glob
import re

files = glob.glob('src/main/java/**/*.java', recursive=True)

# We want to find lines containing /*? if <=1.19.2 {*/ ... /*?} else {*/ ... /*?}*/
# and convert them to block conditionals.

for filepath in files:
    with open(filepath, "r") as f:
        content = f.read()

    # Pattern to match the entire line containing the inline conditional
    # This might fail if there are multiple inline conditionals on one line, but let's assume there's one.
    
    # Let's find lines with `/*? if ` and `/*?}*/`
    
    new_lines = []
    changed = False
    
    lines = content.split('\n')
    for line in lines:
        if '/*? if' in line and '/*?}*/' in line:
            # We have an inline conditional!
            # Example: client.player.sendMessage(/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText/*?} else {*/net.minecraft.text.Text.literal/*?}*/("msg"), true);
            
            # Use regex to extract the parts
            match = re.search(r'(.*?)/\*?\s*if\s*(.*?)\s*\{\*/(.*?)/\*?\} else \{\*/(.*?)/\*?\}\*/(.*)', line)
            if match:
                prefix = match.group(1)
                condition = match.group(2)
                if_true = match.group(3)
                if_false = match.group(4)
                suffix = match.group(5)
                
                # construct the new block
                # get indentation from prefix
                indent_match = re.match(r'^(\s*)', prefix)
                indent = indent_match.group(1) if indent_match else ""
                
                new_block = f"""{indent}//? if {condition} {{
{prefix}{if_true}{suffix}
{indent}//?}} else {{
{indent}/*{prefix.lstrip()}{if_false}{suffix}*/
{indent}//?}}"""
                new_lines.append(new_block)
                changed = True
                continue
            
            # Try if it's a bit different spacing
            # client.openScreen(/*?} else {*/client.setScreen(/*?}*/
            match2 = re.search(r'(.*?)/\*?\s*if\s*(.*?)\s*\{\*/(.*?)/\*?\} else \{\*/(.*?)/\*?\}\*/(.*)', line.replace(" ", " ")) # just to be sure
            # Actually, what if it doesn't match?
            
            print(f"Warning: unmatched inline in {filepath}: {line}")
            new_lines.append(line)
        else:
            new_lines.append(line)
            
    if changed:
        with open(filepath, "w") as f:
            f.write('\n'.join(new_lines))
        print("Fixed inline in", filepath)

