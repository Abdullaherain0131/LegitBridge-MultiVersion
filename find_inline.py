import os
import glob
import re

files = glob.glob("src/main/java/com/ersin/legitbridge/**/*.java", recursive=True)

for fpath in files:
    with open(fpath, "r", encoding="utf-8") as f:
        content = f.read()

    orig = content

    # Keep applying the inline block extraction until no more match.
    # This time we also use non-greedy dotall and account for cases where
    # the /*? if was missing but /*?} else {*/ is present.
    # Actually, let's just find all remaining `/*?` and print them for debugging.
    
    matches = re.finditer(r'(.{0,20})/\*\?(.*?)\*/(.{0,20})', content, flags=re.DOTALL)
    for m in matches:
        print(f"Found in {fpath}: {repr(m.group(0))}")

