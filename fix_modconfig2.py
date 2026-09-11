import re

with open('src/main/java/com/ersin/legitbridge/gui/ModConfigScreen.java', 'r') as f:
    content = f.read()

# Fix the outer class width/height that I accidentally changed
# I'll change getWidth() -> width, getHeight() -> height globally, and then inside the 3 subclasses I will change them back to getters.
content = content.replace('this.getWidth()', 'this.width')
content = content.replace('this.getHeight()', 'this.height')

# Now fix the stonecutter directives to make 1.16 the active code
# Let's replace the getters/setters block I injected earlier
bad_methods = """
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
good_methods = """
        //? if >1.19.2 {
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
        //?}
"""
content = content.replace(bad_methods, good_methods)

# Fix wrapper for ModConfigScreen.render
bad_wrapper = """
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
good_wrapper = """
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
content = content.replace(bad_wrapper, good_wrapper)

# Fix LiteralText helper
bad_text_helper = """
    //? if <=1.19.2
    /*private static net.minecraft.text.Text text(String s) { return new net.minecraft.text.LiteralText(s); }*/
    //? if >1.19.2 {
    private static net.minecraft.text.Text text(String s) { return net.minecraft.text.Text.literal(s); }
    //?}
"""
good_text_helper = """
    //? if >1.19.2 {
    /*private static net.minecraft.text.Text text(String s) { return net.minecraft.text.Text.literal(s); }*/
    //?} else {
    private static net.minecraft.text.Text text(String s) { return new net.minecraft.text.LiteralText(s); }
    //?}
"""
content = content.replace(bad_text_helper, good_text_helper)

# Fix addButtonFix
bad_addbutton = """
    private void addCustomButton(ButtonWidget button) {
        //? if <=1.19.2
        /*this.addButton(button);*/
        //? if >1.19.2 {
        this.addDrawableChild(button);
        //?}
    }
"""
good_addbutton = """
    private void addCustomButton(ButtonWidget button) {
        //? if >1.19.2 {
        /*this.addDrawableChild(button);*/
        //?} else {
        this.addButton(button);
        //?}
    }
"""
content = content.replace(bad_addbutton.strip(), good_addbutton.strip())

# Double check if any @Override render(MatrixStack) was duplicated
content = content.replace('@Override\n    @Override\n    public void render', '@Override\n    public void render')

with open('src/main/java/com/ersin/legitbridge/gui/ModConfigScreen.java', 'w') as f:
    f.write(content)

