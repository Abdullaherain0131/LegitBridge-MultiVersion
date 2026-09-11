import os

replacements = {
    """//? if <=1.19.2 {
/*player.world*/
//?} else {
player.getWorld()
//?}""": "player.getWorld()",
    """//? if <=1.19.2 {
/*player.pitch*/
//?} else {
player.getPitch()
//?}""": "player.getPitch()",
    """//? if <=1.19.2 {
/*player.yaw*/
//?} else {
player.getYaw()
//?}""": "player.getYaw()",
    """//? if <=1.19.2 {
/*client.options.keyUse*/
//?} else {
client.options.useKey
//?}""": "client.options.useKey",
    """//? if <=1.19.2 {
/*client.options.keyAttack*/
//?} else {
client.options.attackKey
//?}""": "client.options.attackKey",
    """//? if <=1.19.2 {
/*client.options.keyForward*/
//?} else {
client.options.forwardKey
//?}""": "client.options.forwardKey",
    """//? if <=1.19.2 {
/*client.options.keyBack*/
//?} else {
client.options.backKey
//?}""": "client.options.backKey",
    """//? if <=1.19.2 {
/*client.options.keyLeft*/
//?} else {
client.options.leftKey
//?}""": "client.options.leftKey",
    """//? if <=1.19.2 {
/*client.options.keyRight*/
//?} else {
client.options.rightKey
//?}""": "client.options.rightKey",
    """//? if <=1.19.2 {
/*client.options.keyJump*/
//?} else {
client.options.jumpKey
//?}""": "client.options.jumpKey",
    """//? if <=1.19.2 {
/*client.options.keySneak*/
//?} else {
client.options.sneakKey
//?}""": "client.options.sneakKey",
    """//? if <=1.19.2 {
/*client.player.inventory*/
//?} else {
client.player.getInventory()
//?}""": "client.player.getInventory()",
    """//? if <=1.19.2 {
/*player.inventory*/
//?} else {
player.getInventory()
//?}""": "player.getInventory()",
    """//? if <=1.19.2 {
/*player.getBoundingBox()*/
//?} else {
player.getBoundingBox()
//?}""": "player.getBoundingBox()"
}

for root, _, files in os.walk("src/main/java"):
    for file in files:
        if file.endswith(".java"):
            path = os.path.join(root, file)
            with open(path, "r") as f:
                content = f.read()
            
            modified = False
            for old, new in replacements.items():
                if old in content:
                    content = content.replace(old, new)
                    modified = True
            
            # Undo the inline ones just in case
            inline_replacements = {
                "/*? if <=1.19.2 {*//*player.world*//*?} else {*/player.getWorld()/*?}*/": "player.getWorld()",
                "/*? if <=1.19.2 {*//*player.pitch*//*?} else {*/player.getPitch()/*?}*/": "player.getPitch()",
                "/*? if <=1.19.2 {*//*player.yaw*//*?} else {*/player.getYaw()/*?}*/": "player.getYaw()",
                "/*? if <=1.19.2 {*//*client.options.keyUse*//*?} else {*/client.options.useKey/*?}*/": "client.options.useKey",
                "/*? if <=1.19.2 {*//*client.options.keyAttack*//*?} else {*/client.options.attackKey/*?}*/": "client.options.attackKey",
                "/*? if <=1.19.2 {*//*client.options.keyForward*//*?} else {*/client.options.forwardKey/*?}*/": "client.options.forwardKey",
                "/*? if <=1.19.2 {*//*client.options.keyBack*//*?} else {*/client.options.backKey/*?}*/": "client.options.backKey",
                "/*? if <=1.19.2 {*//*client.options.keyLeft*//*?} else {*/client.options.leftKey/*?}*/": "client.options.leftKey",
                "/*? if <=1.19.2 {*//*client.options.keyRight*//*?} else {*/client.options.rightKey/*?}*/": "client.options.rightKey",
                "/*? if <=1.19.2 {*//*client.options.keyJump*//*?} else {*/client.options.jumpKey/*?}*/": "client.options.jumpKey",
                "/*? if <=1.19.2 {*//*client.options.keySneak*//*?} else {*/client.options.sneakKey/*?}*/": "client.options.sneakKey",
                "/*? if <=1.19.2 {*//*client.player.inventory*//*?} else {*/client.player.getInventory()/*?}*/": "client.player.getInventory()",
                "/*? if <=1.19.2 {*//*player.inventory*//*?} else {*/player.getInventory()/*?}*/": "player.getInventory()",
                "/*? if <=1.19.2 {*//*player.getBoundingBox()*//*?} else {*/player.getBoundingBox()/*?}*/": "player.getBoundingBox()"
            }

            for old, new in inline_replacements.items():
                if old in content:
                    content = content.replace(old, new)
                    modified = True
            
            if modified:
                with open(path, "w") as f:
                    f.write(content)
                print(f"Restored {path}")
