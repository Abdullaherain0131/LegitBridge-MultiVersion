import os
import glob
import re

files = glob.glob('src/main/java/com/ersin/legitbridge/**/*.java', recursive=True)

for filepath in files:
    with open(filepath, "r") as f:
        content = f.read()

    # The previous script changed client.player.inventory to client.player.getInventory() without conditionals
    # We will replace all occurrences of `client.player.getInventory().selectedSlot` 
    # to be block-level conditional between `client.player.inventory.selectedSlot` and `client.player.getInventory().selectedSlot`

    # In 1.20.1, we cast to PlayerInventory to avoid the "value of type Inventory" error just in case.
    # Actually let's do:
    # //? if <=1.19.2 {
    # /* client.player.inventory.selectedSlot */
    # //?} else {
    # ((net.minecraft.entity.player.PlayerInventory)client.player.getInventory()).selectedSlot
    # //?}
    
    # We also need to fix `.getInventory().getStack` etc. Wait, is `.getInventory().getStack` also failing?
    # No, getStack() is part of the `Inventory` interface, so the compiler didn't complain! The compiler only complained about `.selectedSlot` because `Inventory` interface doesn't have `selectedSlot`!
    
    # Let's replace getInventory().selectedSlot
    
    new_content = content.replace(
        "client.player.getInventory().selectedSlot",
        "/*? if <=1.19.2 {*//*client.player.inventory.selectedSlot*//*?} else {*/((net.minecraft.entity.player.PlayerInventory)client.player.getInventory()).selectedSlot/*?}*/"
    )

    if new_content != content:
        with open(filepath, "w") as f:
            f.write(new_content)
        print("Fixed selectedSlot in", filepath)

