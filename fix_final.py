import re
import glob

files = glob.glob('src/main/java/com/ersin/legitbridge/**/*.java', recursive=True)
for file in files:
    with open(file, 'r') as f:
        content = f.read()

    original = content
    
    # MixinMinecraftClientFastPlace
    if 'MixinMinecraftClientFastPlace' in file:
        content = content.replace('/*? if <=1.19.2 {*/client.options.keyUse/*?} else {*/client.options.useKey/*?}*/', '/*? if <=1.19.2 {*/this.options.keyUse/*?} else {*/this.options.useKey/*?}*/')

    # openScreen(InventoryScreen) -> openScreen(new net.minecraft.client.gui.screen.ingame.InventoryScreen(client.player))
    content = content.replace('new InventoryScreen(client.player)', 'new net.minecraft.client.gui.screen.ingame.InventoryScreen(client.player)')

    # AutoFish.java interactItem
    if 'AutoFish' in file:
        # In 1.20.1 interactItem takes (PlayerEntity, Hand) - NO, it takes (PlayerEntity, Hand) but in 1.16.5 it took (PlayerEntity, ClientWorld, Hand)
        content = content.replace('client.interactionManager.interactItem(client.player, client.world, Hand.MAIN_HAND);', '/*? if <=1.19.2 {*/client.interactionManager.interactItem(client.player, client.world, Hand.MAIN_HAND);/*?} else {*/client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);/*?}*/')

    # AutoTool.java inventory.selectedSlot
    content = content.replace('/*? if <=1.19.2 {*//*client.player.inventory*//*?} else {*/client.player.getInventory()/*?}*/', '/*? if <=1.19.2 {*/client.player.inventory/*?} else {*/client.player.getInventory()/*?}*/')

    # CombatAssist.java yaw, pitch 
    # player.yaw -> getYaw(), player.pitch -> getPitch() in 1.20
    content = content.replace('client.player.yaw', '/*? if <=1.19.2 {*/client.player.yaw/*?} else {*/client.player.getYaw()/*?}*/')
    content = content.replace('client.player.pitch', '/*? if <=1.19.2 {*/client.player.pitch/*?} else {*/client.player.getPitch()/*?}*/')

    # Dodge.java player.pitch = ... -> setPitch(...)
    if 'Dodge' in file:
        content = re.sub(r'player\.pitch\s*=\s*(.+?);', r'/*? if <=1.19.2 {*/player.pitch = \1;/*?} else {*/player.setPitch(\1);/*?}*/', content)
        content = content.replace('player.pitch', '/*? if <=1.19.2 {*/player.pitch/*?} else {*/player.getPitch()/*?}*/')
        content = content.replace('player.inventory', '/*? if <=1.19.2 {*/player.inventory/*?} else {*/player.getInventory()/*?}*/')

    if content != original:
        with open(file, 'w') as f:
            f.write(content)

