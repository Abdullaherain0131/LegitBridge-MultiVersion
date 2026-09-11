with open("src/main/java/com/ersin/legitbridge/utils/VersionHelper.java", "r") as f:
    content = f.read()

import re
content = re.sub(r'public static org\.joml\.Matrix4f.*?}', '', content, flags=re.DOTALL)
content = re.sub(r'public static int getDebugLinesDrawMode.*?}', '', content, flags=re.DOTALL)

with open("src/main/java/com/ersin/legitbridge/utils/VersionHelper.java", "w") as f:
    f.write(content)
