import re
import sys

file_path = "src/main/java/com/ersin/legitbridge/gui/ModConfigScreen.java"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

# Fix getWidgetList
content = re.sub(
    r'    private Iterable<\?> getWidgetList\(\) \{\n        return this\.buttons;\n    \}',
    '    private Iterable<?> getWidgetList() {\n/*? if <=1.19.2 {*/\n        return this.buttons;\n/*?} else {*//*        return this.children();\n*//*?}*/\n    }',
    content
)

# Fix init clear
content = re.sub(
    r'        this\.buttons\.clear\(\);\n        //\? if <=1\.19\.2 \{\n        this\.children\.clear\(\);\n//\?\} else \{\n        /\*\(\(java\.util\.List<net\.minecraft\.client\.gui\.Element>\)this\.children\(\)\)\.clear\(\);\*/\n//\?\}',
    '/*? if <=1.19.2 {*/\n        this.buttons.clear();\n        this.children.clear();\n/*?} else {*//*        this.clearChildren();\n*//*?}*/',
    content
)

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)

file_path2 = "src/main/java/com/ersin/legitbridge/mixin/MixinEntitySafeWalk.java"
with open(file_path2, "r", encoding="utf-8") as f:
    content2 = f.read()

content2 = re.sub(
    r'this\.world\.',
    r'/*? if <=1.19.2 {*/this.world/*?} else {*//*this.getWorld()*//*?}*/.',
    content2
)
content2 = re.sub(
    r'this\.pitch\)',
    r'/*? if <=1.19.2 {*/this.pitch/*?} else {*//*this.getPitch()*//*?}*/)',
    content2
)
with open(file_path2, "w", encoding="utf-8") as f:
    f.write(content2)

print("Fixed ModConfigScreen children and MixinEntitySafeWalk.")
