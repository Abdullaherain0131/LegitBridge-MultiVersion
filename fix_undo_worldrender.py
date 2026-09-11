with open("src/main/java/com/ersin/legitbridge/module/impl/WorldRender.java", "r") as f:
    content = f.read()

content = content.replace(
"""//? if <=1.19.2 {
/*        buffer.begin(3, net.minecraft.client.render.VertexFormats.POSITION_COLOR);
*///?} else {
        buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINE_STRIP, net.minecraft.client.render.VertexFormats.POSITION_COLOR); // GL_LINE_STRIP
//?}""", "        buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINE_STRIP, net.minecraft.client.render.VertexFormats.POSITION_COLOR); // GL_LINE_STRIP")

content = content.replace(
"""//? if <=1.19.2 {
/*        buffer.begin(1, net.minecraft.client.render.VertexFormats.POSITION_COLOR);
*///?} else {
        buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINES, net.minecraft.client.render.VertexFormats.POSITION_COLOR);
//?}""", "        buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINES, net.minecraft.client.render.VertexFormats.POSITION_COLOR);")

content = content.replace(
"""//? if <=1.19.2 {
/*        buffer.vertex(matrixStack.peek().getModel(), (float)start.x, (float)start.y, (float)start.z).color(0, 255, 0, 255).next();
*///?} else {
        buffer.vertex(matrixStack.peek().getPositionMatrix(), (float)start.x, (float)start.y, (float)start.z).color(0, 255, 0, 255).next();
//?}""", "        buffer.vertex(matrixStack.peek().getPositionMatrix(), (float)start.x, (float)start.y, (float)start.z).color(0, 255, 0, 255).next();")

with open("src/main/java/com/ersin/legitbridge/module/impl/WorldRender.java", "w") as f:
    f.write(content)
