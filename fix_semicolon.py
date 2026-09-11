import os

for root, _, files in os.walk("src/main/java"):
    for file in files:
        if file.endswith(".java"):
            path = os.path.join(root, file)
            with open(path, "r") as f:
                content = f.read()
            
            if "););" in content:
                content = content.replace("););", "));")
                with open(path, "w") as f:
                    f.write(content)
                print(f"Fixed {path}")
            if "(););" in content:
                content = content.replace("(););", "());")
                with open(path, "w") as f:
                    f.write(content)
                print(f"Fixed {path}")
