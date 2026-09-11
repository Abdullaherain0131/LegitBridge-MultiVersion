import re

with open('src/main/java/com/ersin/legitbridge/gui/ModConfigScreen.java', 'r') as f:
    content = f.read()

# 1. Fix ModConfigScreen render wrapper
content = re.sub(
    r'//\? if >1\.19\.2 \{\s*@Override\s*public void render\(net\.minecraft\.client\.gui\.DrawContext context, int mouseX, int mouseY, float delta\) \{\s*this\.renderInternal\(context, mouseX, mouseY, delta\);\s*\}\s*//\?\} else \{\s*/\*\s*@Override\s*public void render\(net\.minecraft\.client\.util\.math\.MatrixStack context, int mouseX, int mouseY, float delta\) \{\s*this\.renderInternal\(context, mouseX, mouseY, delta\);\s*\}\s*\*/\s*//\?\}',
    '''//? if >1.19.2 {
    /*@Override
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderInternal(context, mouseX, mouseY, delta);
    }*///?} else {
    @Override
    public void render(net.minecraft.client.util.math.MatrixStack context, int mouseX, int mouseY, float delta) {
        this.renderInternal(context, mouseX, mouseY, delta);
    }
    //?}''', content
)

# 2. Fix the 3 classes methods
content = re.sub(
    r'//\? if >1\.19\.2 \{\s*public int getX\(\) \{ return super\.getX\(\); \}[\s\S]*?@Override public void renderWidget\(net\.minecraft\.client\.gui\.DrawContext context, int mouseX, int mouseY, float delta\) \{ this\.renderButton\(context, mouseX, mouseY, delta\); \}\s*//\?\} else \{\s*/\*\s*public int getX\(\) \{ return super\.x; \}[\s\S]*?public void setY\(int y\) \{ super\.y = y; \}\s*\*/\s*//\?\}',
    '''//? if >1.19.2 {
        /*
        public int getX() { return super.getX(); }
        public int getY() { return super.getY(); }
        public int getWidth() { return super.getWidth(); }
        public int getHeight() { return super.getHeight(); }
        public void setX(int x) { super.setX(x); }
        public void setY(int y) { super.setY(y); }
        @Override public void renderWidget(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) { this.renderButton(context, mouseX, mouseY, delta); }
        *///?} else {
        public int getX() { return super.x; }
        public int getY() { return super.y; }
        public int getWidth() { return super.width; }
        public int getHeight() { return super.height; }
        public void setX(int x) { super.x = x; }
        public void setY(int y) { super.y = y; }
        //?}''', content
)

# 3. Fix LiteralText helper
content = re.sub(
    r'//\? if <=1\.19\.2\s*/\*private static net\.minecraft\.text\.Text text\(String s\) \{ return new net\.minecraft\.text\.LiteralText\(s\); \}\*/\s*//\? if >1\.19\.2 \{\s*private static net\.minecraft\.text\.Text text\(String s\) \{ return net\.minecraft\.text\.Text\.literal\(s\); \}\s*//\?\}',
    '''//? if >1.19.2 {
    /*private static net.minecraft.text.Text text(String s) { return net.minecraft.text.Text.literal(s); }*/
    //?} else {
    private static net.minecraft.text.Text text(String s) { return new net.minecraft.text.LiteralText(s); }
    //?}''', content
)

# 4. Fix addCustomButton wrapper
content = re.sub(
    r'private void addCustomButton\(ButtonWidget button\) \{\s*//\? if <=1\.19\.2\s*/\*this\.addButton\(button\);\*/\s*//\? if >1\.19\.2 \{\s*this\.addDrawableChild\(button\);\s*//\?\}\s*\}',
    '''private void addCustomButton(ButtonWidget button) {
        //? if >1.19.2 {
        /*this.addDrawableChild(button);*/
        //?} else {
        this.addButton(button);
        //?}
    }''', content
)

# Restore this.getWidth() -> this.width in outer scope
content = content.replace('this.getWidth()', 'this.width')
content = content.replace('this.getHeight()', 'this.height')

# But we MUST put them back inside the getters for SubcategoryTab, ConfigButton, SimpleButton!
# Actually, the methods we added in good_methods are:
# public int getWidth() { return super.width; }
# So we don't need this.getWidth() inside the outer scope. The errors in 1.20 were:
# /home/.../ModConfigScreen.java:391: error: cannot find symbol this.getHeight()
# /home/.../ModConfigScreen.java:413: error: incompatible types: Object cannot be converted to DrawContext
# Wait, Object cannot be converted to DrawContext?
# Where does it say `Object cannot be converted to DrawContext`?
# In `blockInputField.render(context, mouseX, mouseY, delta);` !
# Because `context` is `Object` in `renderInternal`!
# So we need to cast `context` back to `DrawContext` in 1.20, and `MatrixStack` in 1.16!
# Or we can just call `blockInputField.render((net.minecraft.client.util.math.MatrixStack) context, ...)` in 1.16, and `(DrawContext) context` in 1.20!
# We can make a helper method `renderWidget(Object widget, Object context, ...)`
content = content.replace('blockInputField.render(context, mouseX, mouseY, delta);',
'''//? if >1.19.2 {
            /*blockInputField.render((net.minecraft.client.gui.DrawContext)context, mouseX, mouseY, delta);*/
            //?} else {
            blockInputField.render((net.minecraft.client.util.math.MatrixStack)context, mouseX, mouseY, delta);
            //?}''')

content = content.replace('((ButtonWidget) widget).render(context, mouseX, mouseY, delta);',
'''//? if >1.19.2 {
                /*((ButtonWidget) widget).render((net.minecraft.client.gui.DrawContext)context, mouseX, mouseY, delta);*/
                //?} else {
                ((ButtonWidget) widget).render((net.minecraft.client.util.math.MatrixStack)context, mouseX, mouseY, delta);
                //?}''')

with open('src/main/java/com/ersin/legitbridge/gui/ModConfigScreen.java', 'w') as f:
    f.write(content)

