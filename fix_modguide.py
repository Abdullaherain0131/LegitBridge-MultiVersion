import re

with open('src/main/java/com/ersin/legitbridge/gui/ModGuideScreen.java', 'r') as f:
    content = f.read()

# LiteralText -> Text.literal
bad_text = """
    //? if <=1.19.2
    /*private static net.minecraft.text.Text text(String s) { return new net.minecraft.text.LiteralText(s); }*/
    //? if >1.19.2 {
    private static net.minecraft.text.Text text(String s) { return net.minecraft.text.Text.literal(s); }
    //?}
"""
good_text = """
    //? if >1.19.2 {
    /*private static net.minecraft.text.Text text(String s) { return net.minecraft.text.Text.literal(s); }*/
    //?} else {
    private static net.minecraft.text.Text text(String s) { return new net.minecraft.text.LiteralText(s); }
    //?}
"""
content = content.replace('public class ModGuideScreen extends Screen {', 'public class ModGuideScreen extends Screen {' + good_text)
content = content.replace('new LiteralText', 'text')

# addButton
bad_add = """
    private void addCustomButton(ButtonWidget button) {
        //? if >1.19.2 {
        /*this.addDrawableChild(button);*/
        //?} else {
        this.addButton(button);
        //?}
    }
"""
content = content.replace('public ModGuideScreen', bad_add.strip() + '\n    public ModGuideScreen')
content = content.replace('this.addButton', 'this.addCustomButton')

# render wrapper
wrapper = """
    //? if >1.19.2 {
    /*@Override
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderInternal(context, mouseX, mouseY, delta);
    }*///?} else {
    @Override
    public void render(net.minecraft.client.util.math.MatrixStack context, int mouseX, int mouseY, float delta) {
        this.renderInternal(context, mouseX, mouseY, delta);
    }
    //?}
"""
content = content.replace('public void render(MatrixStack context, int mouseX, int mouseY, float delta)', 'public void renderInternal(Object context, int mouseX, int mouseY, float delta)')
content = content.replace('public void renderInternal(Object context', wrapper + '\n    public void renderInternal(Object context')

# MatrixStack usages inside renderInternal
content = content.replace('(MatrixStack) context', '(Object) context')
content = content.replace('super.render((MatrixStack) context, mouseX, mouseY, delta);',
'''//? if >1.19.2 {
        /*super.render((net.minecraft.client.gui.DrawContext)context, mouseX, mouseY, delta);*/
        //?} else {
        super.render((net.minecraft.client.util.math.MatrixStack)context, mouseX, mouseY, delta);
        //?}''')

# fillGradient uses MatrixStack in 1.16, DrawContext in 1.20?
# In ModConfigScreen we had this.fillGradient(context, ...). If it fails in 1.20, we can wrap it:
content = content.replace('this.fillGradient((Object) context, 0, 0, this.width, this.height, 0xC0101014, 0xD0050508);',
'''//? if >1.19.2 {
        /*this.fillGradient((net.minecraft.client.gui.DrawContext)context, 0, 0, this.width, this.height, 0xC0101014, 0xD0050508);*/
        //?} else {
        this.fillGradient((net.minecraft.client.util.math.MatrixStack)context, 0, 0, this.width, this.height, 0xC0101014, 0xD0050508);
        //?}''')

with open('src/main/java/com/ersin/legitbridge/gui/ModGuideScreen.java', 'w') as f:
    f.write(content)

