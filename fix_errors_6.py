import re
import sys

file_path = "src/main/java/com/ersin/legitbridge/gui/ModConfigScreen.java"

with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

# 1. Remove @Override public void renderWidget(...) from wrappers
content = re.sub(
    r'@Override public void renderWidget\(net\.minecraft\.client\.gui\.DrawContext context, int mouseX, int mouseY, float delta\) \{ this\.renderButton\(context, mouseX, mouseY, delta\); \}',
    '',
    content
)

# 2. Fix sound event values
content = content.replace(
    '/*event = SoundEvents.BLOCK_NOTE_BLOCK_PLING;*/',
    '/*event = SoundEvents.BLOCK_NOTE_BLOCK_PLING.value();*/'
)
content = content.replace(
    '/*event = SoundEvents.ENTITY_PLAYER_LEVELUP;*/',
    '/*event = SoundEvents.ENTITY_PLAYER_LEVELUP.value();*/'
)
content = content.replace(
    '/*SoundEvent event = SoundEvents.UI_BUTTON_CLICK;*/',
    '/*SoundEvent event = SoundEvents.UI_BUTTON_CLICK.value();*/'
)

# 3. Fix cb.y and blockInputField.y and x
content = re.sub(
    r'cb\.y = cb\.baseY - \(int\) scrollCurrent;',
    r'/*? if <=1.19.2 {*/cb.y = cb.baseY - (int) scrollCurrent;/*?} else {*//*cb.setY(cb.baseY - (int) scrollCurrent);*//*?}*/',
    content
)
content = re.sub(
    r'blockInputField\.y = startY \+ gap \* 11 \+ 4 - \(int\) scrollCurrent;',
    r'/*? if <=1.19.2 {*/blockInputField.y = startY + gap * 11 + 4 - (int) scrollCurrent;/*?} else {*//*blockInputField.setY(startY + gap * 11 + 4 - (int) scrollCurrent);*//*?}*/',
    content
)
content = re.sub(
    r'RenderUtils\.drawTextWithShadow\(context, this\.textRenderer, "Bulunacak Blok \(Örn: minecraft:diamond_ore\):", blockInputField\.x, blockInputField\.y - 12, 0xAAAAAA\);',
    r'/*? if <=1.19.2 {*/\n            RenderUtils.drawTextWithShadow(context, this.textRenderer, "Bulunacak Blok (Örn: minecraft:diamond_ore):", blockInputField.x, blockInputField.y - 12, 0xAAAAAA);\n            /*?} else {*//*RenderUtils.drawTextWithShadow(context, this.textRenderer, "Bulunacak Blok (Örn: minecraft:diamond_ore):", blockInputField.getX(), blockInputField.getY() - 12, 0xAAAAAA);*//*?}*/',
    content
)

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)

print("ModConfigScreen fixed.")
