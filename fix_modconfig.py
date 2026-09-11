import re

with open('src/main/java/com/ersin/legitbridge/gui/ModConfigScreen.java', 'r') as f:
    content = f.read()

# Replace this.x -> this.getX() etc
content = content.replace('this.x', 'this.getX()')
content = content.replace('this.y', 'this.getY()')
content = content.replace('this.width', 'this.getWidth()')
content = content.replace('this.height', 'this.getHeight()')

# Add getters/setters for ClickableWidget to the 3 classes
methods = """
        //? if >1.19.2 {
        public int getX() { return super.getX(); }
        public int getY() { return super.getY(); }
        public int getWidth() { return super.getWidth(); }
        public int getHeight() { return super.getHeight(); }
        public void setX(int x) { super.setX(x); }
        public void setY(int y) { super.setY(y); }
        @Override public void renderWidget(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) { this.renderButton(context, mouseX, mouseY, delta); }
        //?} else {
        /*
        public int getX() { return super.x; }
        public int getY() { return super.y; }
        public int getWidth() { return super.width; }
        public int getHeight() { return super.height; }
        public void setX(int x) { super.x = x; }
        public void setY(int y) { super.y = y; }
        */
        //?}
"""

content = content.replace('class ConfigButton extends ButtonWidget {', 'class ConfigButton extends ButtonWidget {' + methods)
content = content.replace('class SimpleButton extends ButtonWidget {', 'class SimpleButton extends ButtonWidget {' + methods)
content = content.replace('class SubcategoryTab extends ButtonWidget {', 'class SubcategoryTab extends ButtonWidget {' + methods)

# Replace MatrixStack context with Object context in the overridden renderButton
content = content.replace('public void renderButton(MatrixStack context, int mouseX, int mouseY, float delta)', 'public void renderButton(Object context, int mouseX, int mouseY, float delta)')
# Wait, for the main ModConfigScreen render method:
content = content.replace('public void render(MatrixStack context, int mouseX, int mouseY, float delta)', 'public void renderInternal(Object context, int mouseX, int mouseY, float delta)')
# Add wrapper for ModConfigScreen.render
wrapper = """
    //? if >1.19.2 {
    @Override
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderInternal(context, mouseX, mouseY, delta);
    }
    //?} else {
    /*
    @Override
    public void render(net.minecraft.client.util.math.MatrixStack context, int mouseX, int mouseY, float delta) {
        this.renderInternal(context, mouseX, mouseY, delta);
    }
    */
    //?}
"""
content = content.replace('public void renderInternal(Object context', wrapper + '\n    public void renderInternal(Object context')

# LiteralText helper
text_helper = """
    //? if <=1.19.2
    /*private net.minecraft.text.Text text(String s) { return new net.minecraft.text.LiteralText(s); }*/
    //? if >1.19.2 {
    private net.minecraft.text.Text text(String s) { return net.minecraft.text.Text.literal(s); }
    //?}
"""
content = content.replace('public class ModConfigScreen extends Screen {', 'public class ModConfigScreen extends Screen {' + text_helper)
content = content.replace('new LiteralText', 'text')

# addButton wrapper
addButtonFix = """
    private void addCustomButton(ButtonWidget button) {
        //? if <=1.19.2
        /*this.addButton(button);*/
        //? if >1.19.2 {
        this.addDrawableChild(button);
        //?}
    }
"""
content = re.sub(r'private void addCustomButton\(ButtonWidget button\) \{[\s\S]*?\}', addButtonFix.strip(), content)

# Remove old unused imports
content = content.replace('import net.minecraft.client.util.math.MatrixStack;\n', '')
content = content.replace('import net.minecraft.text.LiteralText;\n', '')

# Fix main class constructor
content = content.replace('super(new LiteralText("LegitBridge Ayar Menüsü"));', 'super(text("LegitBridge Ayar Menüsü"));')
# Wait, super(text(...)) won't work if text() is instance method and we are in super().
# Make text() static!
content = content.replace('private net.minecraft.text.Text text(String s)', 'private static net.minecraft.text.Text text(String s)')


with open('src/main/java/com/ersin/legitbridge/gui/ModConfigScreen.java', 'w') as f:
    f.write(content)
