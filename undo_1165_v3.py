import os
import re

replacements = [
    # Inventory
    ("//\? if <=1\.19\.2 \{\n/\*player\.inventory\*/\n//\?\} else \{\nplayer\.getInventory\(\)\n//\?\}", 
     "player.getInventory()"),
    
    ("//\? if <=1\.19\.2 \{\n/\*client\.player\.inventory\*/\n//\?\} else \{\nclient\.player\.getInventory\(\)\n//\?\}", 
     "client.player.getInventory()"),

    # Pitch & Yaw
    ("//\? if <=1\.19\.2 \{\n/\*player\.pitch\*/\n//\?\} else \{\nplayer\.getPitch\(\)\n//\?\}", 
     "player.getPitch()"),
    
    ("//\? if <=1\.19\.2 \{\n/\*player\.yaw\*/\n//\?\} else \{\nplayer\.getYaw\(\)\n//\?\}", 
     "player.getYaw()"),
    
    ("//\? if <=1\.19\.2 \{\n/\*client\.player\.pitch\*/\n//\?\} else \{\nclient\.player\.getPitch\(\)\n//\?\}", 
     "client.player.getPitch()"),
    
    ("//\? if <=1\.19\.2 \{\n/\*client\.player\.yaw\*/\n//\?\} else \{\nclient\.player\.getYaw\(\)\n//\?\}", 
     "client.player.getYaw()"),

    (r"//\? if <=1\.19\.2 \{\n/\*client\.player\.pitch = (.*?);\*/\n//\?\} else \{\nclient\.player\.setPitch\(\1\);\n//\?\}",
     r"client.player.setPitch(\1);"),

    (r"//\? if <=1\.19\.2 \{\n/\*client\.player\.yaw = (.*?);\*/\n//\?\} else \{\nclient\.player\.setYaw\(\1\);\n//\?\}",
     r"client.player.setYaw(\1);"),

    (r"//\? if <=1\.19\.2 \{\n/\*player\.pitch = (.*?);\*/\n//\?\} else \{\nplayer\.setPitch\(\1\);\n//\?\}",
     r"player.setPitch(\1);"),

    (r"//\? if <=1\.19\.2 \{\n/\*player\.yaw = (.*?);\*/\n//\?\} else \{\nplayer\.setYaw\(\1\);\n//\?\}",
     r"player.setYaw(\1);"),

    # Abilities
    ("//\? if <=1\.19\.2 \{\n/\*player\.abilities\*/\n//\?\} else \{\nplayer\.getAbilities\(\)\n//\?\}", 
     "player.getAbilities()"),
    
    ("//\? if <=1\.19\.2 \{\n/\*client\.player\.abilities\*/\n//\?\} else \{\nclient\.player\.getAbilities\(\)\n//\?\}", 
     "client.player.getAbilities()"),

    # X, Y, Z for UI widgets
    ("//\? if <=1\.19\.2 \{\n/\*blockInputField\.x\*/\n//\?\} else \{\nblockInputField\.getX\(\)\n//\?\}", 
     "blockInputField.getX()"),
    
    ("//\? if <=1\.19\.2 \{\n/\*blockInputField\.y\*/\n//\?\} else \{\nblockInputField\.getY\(\)\n//\?\}", 
     "blockInputField.getY()"),

    ("//\? if <=1\.19\.2 \{\n/\*this\.x\*/\n//\?\} else \{\nthis\.getX\(\)\n//\?\}", 
     "this.getX()"),
    
    ("//\? if <=1\.19\.2 \{\n/\*this\.y\*/\n//\?\} else \{\nthis\.getY\(\)\n//\?\}", 
     "this.getY()"),
     
    (r"//\? if <=1\.19\.2 \{\n/\*this\.y = (.*?);\*/\n//\?\} else \{\nthis\.setY\(\1\);\n//\?\}", 
     r"this.setY(\1);"),

    # Options keys
    ("//\? if <=1\.19\.2 \{\n/\*client\.options\.keyUse\*/\n//\?\} else \{\nclient\.options\.useKey\n//\?\}", 
     "client.options.useKey"),
    
    ("//\? if <=1\.19\.2 \{\n/\*client\.options\.keyAttack\*/\n//\?\} else \{\nclient\.options\.attackKey\n//\?\}", 
     "client.options.attackKey"),
     
    # Text literal
    (r"//\? if <=1\.19\.2 \{\n/\*new net\.minecraft\.text\.LiteralText\((.*?)\)\*/\n//\?\} else \{\nText\.literal\(\1\)\n//\?\}", 
     r"Text.literal(\1)"),

    # Screens
    (r"//\? if <=1\.19\.2 \{\n/\*client\.openScreen\((.*?)\);\*/\n//\?\} else \{\nclient\.setScreen\(\1\);\n//\?\}", 
     r"client.setScreen(\1);"),

    # Registries
    (r"//\? if <=1\.19\.2 \{\n/\*net\.minecraft\.util\.registry\.Registry\.ITEM\.getId\((.*?)\)\*/\n//\?\} else \{\nRegistries\.ITEM\.getId\(\1\)\n//\?\}", 
     r"Registries.ITEM.getId(\1)"),
     
    # Gamma
    ("//\? if <=1\.19\.2 \{\n/\*client\.options\.gamma\*/\n//\?\} else \{\nclient\.options\.getGamma\(\)\.getValue\(\)\n//\?\}", 
     "client.options.getGamma().getValue()"),

    # Packages and Types
    ("//\? if <=1\.19\.2 \{\n/\*import net\.minecraft\.network\.Packet;\n\*///\?\} else \{\nimport net\.minecraft\.network\.packet\.Packet;\n//\?\}", 
     "import net.minecraft.network.packet.Packet;"),

    # Matrices
    ("//\? if <=1\.19\.2 \{\n/\*matrixStack\.peek\(\)\.getModel\(\)\*/\n//\?\} else \{\nmatrixStack\.peek\(\)\.getPositionMatrix\(\)\n//\?\}", 
     "matrixStack.peek().getPositionMatrix()"),
     
    ("//\? if <=1\.19\.2 \{\n/\*3\*/\n//\?\} else \{\nVertexFormat\.DrawMode\.DEBUG_LINES\n//\?\}", 
     "VertexFormat.DrawMode.DEBUG_LINES"),
     
    # Entity World
    ("//\? if <=1\.19\.2 \{\n/\*dummyPlayer\.getEntityWorld\(\)\*/\n//\?\} else \{\ndummyPlayer\.getWorld\(\)\n//\?\}", 
     "dummyPlayer.getWorld()"),
     
    ("//\? if <=1\.19\.2 \{\n/\*entity\.getEntityWorld\(\)\*/\n//\?\} else \{\nentity\.getWorld\(\)\n//\?\}", 
     "entity.getWorld()"),
     
    # Entity discard
    (r"//\? if <=1\.19\.2 \{\n/\*client\.world\.removeEntity\((.*?)\);\*/\n//\?\} else \{\nclient\.world\.removeEntity\(\1, net\.minecraft\.entity\.Entity\.RemovalReason\.DISCARDED\);\n//\?\}", 
     r"client.world.removeEntity(\1, net.minecraft.entity.Entity.RemovalReason.DISCARDED);"),

    # Widget Children
    ("//\? if <=1\.19\.2 \{\n/\*this\.children\.clear\(\);\n\*///\?\} else \{\nthis\.clearChildren\(\);\n//\?\}", 
     "this.clearChildren()"),

    # Entity isAttackable
    ("//\? if <=1\.19\.2 \{\n/\*entity\.isAttackable\(\)\*/\n//\?\} else \{\nentity\.canHit\(\)\n//\?\}", 
     "entity.canHit()")
]

for root, _, files in os.walk("src/main/java"):
    for file in files:
        if file.endswith(".java"):
            path = os.path.join(root, file)
            with open(path, "r") as f:
                content = f.read()
            
            modified = False
            for old, new in replacements:
                content, count = re.subn(old, new, content)
                if count > 0:
                    modified = True
            
            if modified:
                with open(path, "w") as f:
                    f.write(content)
                print(f"Restored {path}")
