import os
import re

replacements = [
    # Inventory
    (r'(?<!client\.)player\.getInventory\(\)', 
     "//? if <=1.19.2 {\n/*player.inventory*/\n//?} else {\nplayer.getInventory()\n//?}"),
    
    (r'client\.player\.getInventory\(\)', 
     "//? if <=1.19.2 {\n/*client.player.inventory*/\n//?} else {\nclient.player.getInventory()\n//?}"),

    # Pitch & Yaw for entity
    (r'(?<!client\.)player\.getPitch\(\)', 
     "//? if <=1.19.2 {\n/*player.pitch*/\n//?} else {\nplayer.getPitch()\n//?}"),
    
    (r'(?<!client\.)player\.getYaw\(\)', 
     "//? if <=1.19.2 {\n/*player.yaw*/\n//?} else {\nplayer.getYaw()\n//?}"),
    
    (r'client\.player\.getPitch\(\)', 
     "//? if <=1.19.2 {\n/*client.player.pitch*/\n//?} else {\nclient.player.getPitch()\n//?}"),
    
    (r'client\.player\.getYaw\(\)', 
     "//? if <=1.19.2 {\n/*client.player.yaw*/\n//?} else {\nclient.player.getYaw()\n//?}"),

    (r'client\.player\.setPitch\((.*?)\)',
     r"//? if <=1.19.2 {\n/*client.player.pitch = \1;*/\n//?} else {\nclient.player.setPitch(\1);\n//?}"),

    (r'client\.player\.setYaw\((.*?)\)',
     r"//? if <=1.19.2 {\n/*client.player.yaw = \1;*/\n//?} else {\nclient.player.setYaw(\1);\n//?}"),

    (r'(?<!client\.)player\.setPitch\((.*?)\)',
     r"//? if <=1.19.2 {\n/*player.pitch = \1;*/\n//?} else {\nplayer.setPitch(\1);\n//?}"),

    (r'(?<!client\.)player\.setYaw\((.*?)\)',
     r"//? if <=1.19.2 {\n/*player.yaw = \1;*/\n//?} else {\nplayer.setYaw(\1);\n//?}"),

    # Abilities
    (r'(?<!client\.)player\.getAbilities\(\)', 
     "//? if <=1.19.2 {\n/*player.abilities*/\n//?} else {\nplayer.getAbilities()\n//?}"),
    
    (r'client\.player\.getAbilities\(\)', 
     "//? if <=1.19.2 {\n/*client.player.abilities*/\n//?} else {\nclient.player.getAbilities()\n//?}"),

    # X, Y, Z for UI widgets
    (r'blockInputField\.getX\(\)', 
     "//? if <=1.19.2 {\n/*blockInputField.x*/\n//?} else {\nblockInputField.getX()\n//?}"),
    
    (r'blockInputField\.getY\(\)', 
     "//? if <=1.19.2 {\n/*blockInputField.y*/\n//?} else {\nblockInputField.getY()\n//?}"),

    (r'this\.getX\(\)', 
     "//? if <=1.19.2 {\n/*this.x*/\n//?} else {\nthis.getX()\n//?}"),
    
    (r'this\.getY\(\)', 
     "//? if <=1.19.2 {\n/*this.y*/\n//?} else {\nthis.getY()\n//?}"),
     
    (r'this\.setY\((.*?)\)', 
     r"//? if <=1.19.2 {\n/*this.y = \1;*/\n//?} else {\nthis.setY(\1);\n//?}"),

    # Options keys
    (r'client\.options\.useKey', 
     "//? if <=1.19.2 {\n/*client.options.keyUse*/\n//?} else {\nclient.options.useKey\n//?}"),
    
    (r'client\.options\.attackKey', 
     "//? if <=1.19.2 {\n/*client.options.keyAttack*/\n//?} else {\nclient.options.attackKey\n//?}"),
     
    # Text literal
    (r'Text\.literal\((.*?)\)', 
     r"//? if <=1.19.2 {\n/*new net.minecraft.text.LiteralText(\1)*/\n//?} else {\nText.literal(\1)\n//?}"),

    # Screens
    (r'client\.setScreen\((.*?)\)', 
     r"//? if <=1.19.2 {\n/*client.openScreen(\1);*/\n//?} else {\nclient.setScreen(\1);\n//?}"),

    # Registries
    (r'Registries\.ITEM\.getId\((.*?)\)', 
     r"//? if <=1.19.2 {\n/*net.minecraft.util.registry.Registry.ITEM.getId(\1)*/\n//?} else {\nRegistries.ITEM.getId(\1)\n//?}"),
     
    # Gamma
    (r'client\.options\.getGamma\(\)\.getValue\(\)', 
     "//? if <=1.19.2 {\n/*client.options.gamma*/\n//?} else {\nclient.options.getGamma().getValue()\n//?}"),

    # Packages and Types
    (r'import net\.minecraft\.network\.packet\.Packet;', 
     "//? if <=1.19.2 {\n/*import net.minecraft.network.Packet;\n*///?} else {\nimport net.minecraft.network.packet.Packet;\n//?}"),

    # Matrices
    (r'matrixStack\.peek\(\)\.getPositionMatrix\(\)', 
     "//? if <=1.19.2 {\n/*matrixStack.peek().getModel()*/\n//?} else {\nmatrixStack.peek().getPositionMatrix()\n//?}"),
     
    (r'VertexFormat\.DrawMode\.DEBUG_LINES', 
     "//? if <=1.19.2 {\n/*3*/\n//?} else {\nVertexFormat.DrawMode.DEBUG_LINES\n//?}"),
     
    # Entity World
    (r'dummyPlayer\.getWorld\(\)', 
     "//? if <=1.19.2 {\n/*dummyPlayer.getEntityWorld()*/\n//?} else {\ndummyPlayer.getWorld()\n//?}"),
     
    (r'entity\.getWorld\(\)', 
     "//? if <=1.19.2 {\n/*entity.getEntityWorld()*/\n//?} else {\nentity.getWorld()\n//?}"),
     
    # Entity discard
    (r'client\.world\.removeEntity\((.*?), net\.minecraft\.entity\.Entity\.RemovalReason\.DISCARDED\)', 
     r"//? if <=1.19.2 {\n/*client.world.removeEntity(\1);*/\n//?} else {\nclient.world.removeEntity(\1, net.minecraft.entity.Entity.RemovalReason.DISCARDED);\n//?}"),

    # Widget Children
    (r'this\.clearChildren\(\)', 
     "//? if <=1.19.2 {\n/*this.children.clear();\n*///?} else {\nthis.clearChildren();\n//?}"),

    # Entity isAttackable
    (r'entity\.canHit\(\)', 
     "//? if <=1.19.2 {\n/*entity.isAttackable()*/\n//?} else {\nentity.canHit()\n//?}")
]

for root, _, files in os.walk("src/main/java"):
    for file in files:
        if file.endswith(".java"):
            path = os.path.join(root, file)
            with open(path, "r") as f:
                content = f.read()
            
            modified = False
            for old, new in replacements:
                if re.search(old, content) and not "//? if <=1.19.2" in re.search(old, content).group(0):
                    # Check if already patched to avoid double patch
                    # Wait, if we replace `this.getY()` with the stonecutter block, 
                    # running it twice might double-patch it if the regex still matches!
                    # Actually, our regex matches `this\.getY\(\)`, which IS in the new string!
                    # We should check if it's already patched.
                    pass
            
            # Better logic:
            for old, new in replacements:
                # We need to replace only those NOT preceded by `else {\n`
                # But it's easier to just do negative lookbehind for `{ \n` or `{\n`
                # Or just simple replace, since we restored everything!
                content, count = re.subn(old, new, content)
                if count > 0:
                    modified = True
            
            if modified:
                with open(path, "w") as f:
                    f.write(content)
                print(f"Patched {path}")
