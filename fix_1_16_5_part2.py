import os

base_dir = "src/main/java/com/ersin/legitbridge/module/impl"

def replace_in_file(path, replacements):
    if not os.path.exists(path):
        return
    with open(path, 'r') as f:
        content = f.read()
    
    modified = False
    for old, new in replacements:
        if old in content:
            content = content.replace(old, new)
            modified = True
            
    if modified:
        with open(path, 'w') as f:
            f.write(content)

replace_in_file(f"{base_dir}/Fullbright.java", [
    ("originalGamma = client.options.getGamma().getValue();",
"""//? if >1.19.2 {
            originalGamma = client.options.getGamma().getValue();
//?} else {
            /*originalGamma = client.options.gamma;*///?}"""),
    ("client.options.getGamma().setValue(originalGamma != -1 ? originalGamma : 1.0);",
"""//? if >1.19.2 {
                        client.options.getGamma().setValue(originalGamma != -1 ? originalGamma : 1.0);
//?} else {
                        /*client.options.gamma = (originalGamma != -1 ? originalGamma : 1.0);*///?}"""),
    ("client.options.getGamma().getValue() < 100.0",
"""(//? if >1.19.2 {
client.options.getGamma().getValue()
//?} else {
/*client.options.gamma*///?}) < 100.0""")
])

replace_in_file(f"{base_dir}/WorldRender.java", [
    ("buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.QUADS, net.minecraft.client.render.VertexFormats.POSITION_COLOR);",
"""//? if >1.19.2 {
        buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.QUADS, net.minecraft.client.render.VertexFormats.POSITION_COLOR); // GL_QUADS
//?} else {
        /*buffer.begin(7, net.minecraft.client.render.VertexFormats.POSITION_COLOR);*///?}""")
])

replace_in_file(f"{base_dir}/InvMove.java", [
    ("client.options.forwardKey,",
"""//? if >1.19.2 {
client.options.forwardKey,
//?} else {
/*client.options.keyForward,*///?}"""),
    ("client.options.backKey,",
"""//? if >1.19.2 {
client.options.backKey,
//?} else {
/*client.options.keyBack,*///?}"""),
    ("client.options.leftKey,",
"""//? if >1.19.2 {
client.options.leftKey,
//?} else {
/*client.options.keyLeft,*///?}"""),
    ("client.options.rightKey,",
"""//? if >1.19.2 {
client.options.rightKey,
//?} else {
/*client.options.keyRight,*///?}""")
])

replace_in_file(f"{base_dir}/LegitAutoClicker.java", [
    ("client.options.attackKey.setPressed(true);",
"""//? if >1.19.2 {
client.options.attackKey.setPressed(true);
//?} else {
/*client.options.keyAttack.setPressed(true);*///?}"""),
    ("net.minecraft.client.option.KeyBinding.onKeyPressed(client.options.attackKey.getDefaultKey());",
"""//? if >1.19.2 {
net.minecraft.client.option.KeyBinding.onKeyPressed(client.options.attackKey.getDefaultKey());
//?} else {
/*net.minecraft.client.options.KeyBinding.onKeyPressed(client.options.keyAttack.getDefaultKey());*///?}""")
])

replace_in_file(f"{base_dir}/LegitWTap.java", [
    ("if (client.options.forwardKey.isPressed()) {",
"""//? if >1.19.2 {
if (client.options.forwardKey.isPressed()) {
//?} else {
/*if (client.options.keyForward.isPressed()) {*///?}""")
])

print("Patched 10 errors")
