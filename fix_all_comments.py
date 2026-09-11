import os
import glob
import re

files = glob.glob("src/main/java/**/*.java", recursive=True)

def repl(m):
    inner = m.group(1)
    if inner.startswith('?') or inner.startswith('*'):
        return m.group(0)
    return inner

for fpath in files:
    with open(fpath, "r", encoding="utf-8") as f:
        content = f.read()
    orig = content

    content = re.sub(r'/\*(.*?)\*/', repl, content, flags=re.DOTALL)
    
    if content != orig:
        with open(fpath, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Fixed {fpath}")
