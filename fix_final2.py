import re
import glob

def do_replacement(file_path):
    with open(file_path, 'r') as f:
        content = f.read()

    orig = content

    # 1. Fix LiteralText -> Text.literal
    # Since we can't do it inline if it breaks statements, let's look for known lines.
    content = content.replace('/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText/*?} else {*/net.minecraft.text.Text.literal/*?}*/',
                              '/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText/*?} else {*/net.minecraft.text.Text.literal/*?}*/')
    # Actually, it's easier to just strip all Stonecutter comments for LiteralText
    # and replace the whole statements.
    
    # Let's remove ALL /*? ... ?*/ that I injected and just replace it with text(..) or Text.literal depending on the file.
    # We already have a text() helper in ModConfigScreen and ModGuideScreen!
    
    # In ModCommands:
    if 'ModCommands' in file_path:
        content = re.sub(r'return\s+/\*\?.*?;\s*', 
            '//? if <=1.19.2 {\n/*return new net.minecraft.text.LiteralText(text);*/\n//?} else {\nreturn net.minecraft.text.Text.literal(text);\n//?}\n', content)

    if 'LegitBridgeMod.java' in file_path:
        content = re.sub(r'client\.player\.sendMessage\(/\*\?.*?;\s*',
'''//? if <=1.19.2 {
/*client.player.sendMessage(new net.minecraft.text.LiteralText("§7[" + ModConfig.getThemePrefix() + "LegitBridge§7] Durum: " + (ModConfig.enabled ? "§aAÇIK" : "§cKAPALI")), true);*/
//?} else {
client.player.sendMessage(net.minecraft.text.Text.literal("§7[" + ModConfig.getThemePrefix() + "LegitBridge§7] Durum: " + (ModConfig.enabled ? "§aAÇIK" : "§cKAPALI")), true);
//?}
''', content, count=1)
        # 2nd occurrence:
        content = re.sub(r'client\.player\.sendMessage\(/\*\?.*?;\s*',
'''//? if <=1.19.2 {
/*client.player.sendMessage(new net.minecraft.text.LiteralText("§7[" + ModConfig.getThemePrefix() + "LegitBridge§7] " + entry.getKey() + ": " + (!currentValue ? "§aAÇIK" : "§cKAPALI")), true);*/
//?} else {
client.player.sendMessage(net.minecraft.text.Text.literal("§7[" + ModConfig.getThemePrefix() + "LegitBridge§7] " + entry.getKey() + ": " + (!currentValue ? "§aAÇIK" : "§cKAPALI")), true);
//?}
''', content, count=1)

    if 'DurabilityWarning.java' in file_path:
        content = re.sub(r'client\.player\.sendMessage\(.*?;\s*',
'''//? if <=1.19.2 {
/*client.player.sendMessage(new net.minecraft.text.LiteralText("§a[LegitBridge] Elytra otomatik değiştirildi!"), true);*/
//?} else {
client.player.sendMessage(net.minecraft.text.Text.literal("§a[LegitBridge] Elytra otomatik değiştirildi!"), true);
//?}
''', content)
        
    if 'InstaHouse.java' in file_path:
        content = content.replace('new net.minecraft.text.LiteralText', '/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText/*?} else {*/net.minecraft.text.Text.literal/*?}*/')

    if 'MixinChatHud.java' in file_path:
        content = content.replace('new LiteralText', '/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText/*?} else {*/net.minecraft.text.Text.literal/*?}*/')
    
    if 'MixinEntity.java' in file_path:
        content = content.replace('new net.minecraft.text.LiteralText', '/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText/*?} else {*/net.minecraft.text.Text.literal/*?}*/')

    if 'ProjectileWarning.java' in file_path:
        content = content.replace('new LiteralText', '/*? if <=1.19.2 {*/new net.minecraft.text.LiteralText/*?} else {*/net.minecraft.text.Text.literal/*?}*/')
    
    # 2. Fix structure finder blocks: Registry.BLOCK -> Registries.BLOCK in 1.20!
    if 'StructureFinder.java' in file_path:
        content = content.replace('Registry.BLOCK', '/*? if <=1.19.2 {*/net.minecraft.util.registry.Registry.BLOCK/*?} else {*/net.minecraft.registry.Registries.BLOCK/*?}*/')

    # 3. WorldRender.java RenderSystem.disableTexture() -> missing in 1.20
    if 'WorldRender.java' in file_path:
        content = content.replace('RenderSystem.disableTexture();', '//? if <=1.19.2 {\n/*RenderSystem.disableTexture();*/\n//?}')
        content = content.replace('RenderSystem.enableTexture();', '//? if <=1.19.2 {\n/*RenderSystem.enableTexture();*/\n//?}')
        content = content.replace('VertexFormats.POSITION_COLOR', '/*? if <=1.19.2 {*/VertexFormats.POSITION_COLOR/*?} else {*/net.minecraft.client.render.VertexFormats.POSITION_COLOR/*?}*/')
        content = content.replace('buffer.begin(3, ', '/*? if <=1.19.2 {*/buffer.begin(3, /*?} else {*/buffer.begin(net.minecraft.client.render.VertexFormat.DrawMode.DEBUG_LINE_STRIP, /*?}*/')
    
    # 4. LegitScaffold, NinjaBridge key options:
    if 'LegitScaffold.java' in file_path or 'NinjaBridge.java' in file_path:
        content = content.replace('client.options.keyBack', '/*? if <=1.19.2 {*/client.options.keyBack/*?} else {*/client.options.backKey/*?}*/')
        content = content.replace('client.options.keyLeft', '/*? if <=1.19.2 {*/client.options.keyLeft/*?} else {*/client.options.leftKey/*?}*/')
        content = content.replace('client.options.keyRight', '/*? if <=1.19.2 {*/client.options.keyRight/*?} else {*/client.options.rightKey/*?}*/')

    # 5. SlotRotation.java
    if 'SlotRotation.java' in file_path:
        content = content.replace('client.player.getInventory().selectedSlot = (client.player.getInventory().selectedSlot + 1) % 9;', 
'''//? if <=1.19.2 {
/*client.player.inventory.selectedSlot = (client.player.inventory.selectedSlot + 1) % 9;*/
//?} else {
client.player.getInventory().selectedSlot = (client.player.getInventory().selectedSlot + 1) % 9;
//?}''')

    if orig != content:
        with open(file_path, 'w') as f:
            f.write(content)

for file in glob.glob('src/main/java/com/ersin/legitbridge/**/*.java', recursive=True):
    do_replacement(file)

