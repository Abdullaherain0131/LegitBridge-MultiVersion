import re
import os

log = "/home/ersin/.gemini/antigravity/brain/926fd19a-6261-4a73-9af4-f973c9141592/.system_generated/tasks/task-5626.log"
with open(log, 'r') as f:
    text = f.read()

errors = re.findall(r'file:///home/ersin/[^:]+/(src/main/java/.*?\.java):(\d+):', text)
unique_errors = sorted(list(set(errors)))

for filepath, linenum in unique_errors:
    if os.path.exists(filepath):
        line = int(linenum)
        print(f"--- {filepath}:{line} ---")
        with open(filepath, 'r') as f:
            lines = f.readlines()
            start = max(0, line - 3)
            end = min(len(lines), line + 2)
            for i in range(start, end):
                print(f"{i+1}: {lines[i].rstrip()}")
        print("")

