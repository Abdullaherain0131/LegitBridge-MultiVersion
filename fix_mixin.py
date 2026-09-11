import glob
files = glob.glob("src/main/java/com/ersin/legitbridge/**/*.java", recursive=True)
for fpath in files:
    with open(fpath, "r", encoding="utf-8") as f:
        content = f.read()
    orig = content
    content = content.replace("/*client.player.getId()*/", "client.player.getId()")
    
    if content != orig:
        with open(fpath, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Fixed {fpath}")
