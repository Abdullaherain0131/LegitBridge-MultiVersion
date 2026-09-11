import os

for root, _, files in os.walk("src/main/java"):
    for file in files:
        if file.endswith(".java"):
            path = os.path.join(root, file)
            with open(path, "r") as f:
                content = f.read()
            
            new_content = content.replace("net.minecraft.text.com.ersin.legitbridge.utils.VersionHelper.literalText", "com.ersin.legitbridge.utils.VersionHelper.literalText")
            new_content = new_content.replace("this.com.ersin.legitbridge.utils.VersionHelper.setScreen(client, null);;", "com.ersin.legitbridge.utils.VersionHelper.setScreen(this.client, null);")
            new_content = new_content.replace("this.com.ersin.legitbridge.utils.VersionHelper.setScreen(client, null);", "com.ersin.legitbridge.utils.VersionHelper.setScreen(this.client, null);")
            
            if new_content != content:
                with open(path, "w") as f:
                    f.write(new_content)
                print(f"Fixed {path}")
