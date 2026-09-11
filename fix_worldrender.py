import os

file_path = "src/main/java/com/ersin/legitbridge/module/impl/WorldRender.java"
with open(file_path, "r") as f:
    content = f.read()

# Fix pitch/yaw
content = content.replace("float pitch = player.pitch;", """//? if <=1.19.2 {
/*float pitch = player.pitch;
*///?} else {
float pitch = player.getPitch();
//?}""")

content = content.replace("float yaw = player.yaw;", """//? if <=1.19.2 {
/*float yaw = player.yaw;
*///?} else {
float yaw = player.getYaw();
//?}""")

# Fix entity.removed
content = content.replace("if (entity.removed) continue;", """//? if <=1.19.2 {
/*if (entity.removed) continue;
*///?} else {
if (entity.isRemoved()) continue;
//?}""")

# Fix matrixStack.peek().getModel()
content = content.replace("matrixStack.peek().getModel()", """/*? if <=1.19.2 {*//*matrixStack.peek().getModel()*//*?} else {*/matrixStack.peek().getPositionMatrix()/*?}*/""")

# Fix buffer.begin(1, ...)
content = content.replace("buffer.begin(1, /*? if <=1.19.2 {*//*VertexFormats.POSITION_COLOR*//*?} else {*/net.minecraft.client.render.VertexFormats.POSITION_COLOR/*?}*/);", """//? if <=1.19.2 {
/*buffer.begin(1, VertexFormats.POSITION_COLOR);
*///?} else {
buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINES, net.minecraft.client.render.VertexFormats.POSITION_COLOR);
//?}""")

# Fix buffer.begin(7, ...)
content = content.replace("buffer.begin(7, /*? if <=1.19.2 {*//*VertexFormats.POSITION_COLOR*//*?} else {*/net.minecraft.client.render.VertexFormats.POSITION_COLOR/*?}*/);", """//? if <=1.19.2 {
/*buffer.begin(7, VertexFormats.POSITION_COLOR);
*///?} else {
buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.QUADS, net.minecraft.client.render.VertexFormats.POSITION_COLOR);
//?}""")

with open(file_path, "w") as f:
    f.write(content)
print("WorldRender fixed")
