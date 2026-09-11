package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Random;

public class AutoRefill extends Module {

    private int tickDelay = 0;
    private final Random random = new Random();

    public AutoRefill() {
        super("AutoRefill", "Sıcak bardaki eşyalar azaldığında envanterden otomatik tamamlar.", Category.LEGIT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.autoRefill || client.player == null) return;
        
        // Eğer herhangi bir sandık/envanter menüsü açıksa müdahale etme
        if (client.currentScreen != null) return;

        if (tickDelay > 0) {
            tickDelay--;
            return;
        }

        // Sıcak bar (hotbar) slotlarını kontrol et (0-8 arası)
        for (int i = 0; i < 9; i++) {
            ItemStack hotbarStack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
            
            if (hotbarStack != null && !hotbarStack.isEmpty()) {
                // Eğer blok ise ve 16'dan az kaldıysa veya diğer eşyalar (pot vb) azaldıysa
                boolean shouldRefill = false;
                Item item = hotbarStack.getItem();
                int count = hotbarStack.getCount();
                
                if (item instanceof BlockItem && count <= 16) {
                    shouldRefill = true;
                } else if (count <= 1 && hotbarStack.getMaxCount() > 1) { // Örneğin inci, ok
                    shouldRefill = true;
                }

                if (shouldRefill) {
                    int slotToRefill = findItemInInventory(item);
                    if (slotToRefill != -1) {
                        // Envanterdeki eşyayı hotbar'a SWAP (takas) yöntemiyle gönder
                        if (client.interactionManager != null) {
                            boolean wasSprinting = client.player.isSprinting();
                            
                            // Yürürken/koşarken işlem yapılmasını hile saymamaları için (Sprint Spoofing)
                            if (wasSprinting) {
                                client.player.networkHandler.sendPacket(new net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket(client.player, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                            }

                            // Envanter id'si genelde 0'dır. Slot ID'si inventory slotu. (9-35 arası)
                            // Slot 9, containerSlot 9'dur.
                            client.interactionManager.clickSlot(
                                0, // Player inventory syncId is usually 0
                                slotToRefill, 
                                i, // hotbar butonu (0-8)
                                SlotActionType.SWAP, 
                                client.player
                            );

                            if (wasSprinting) {
                                client.player.networkHandler.sendPacket(new net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket(client.player, net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode.START_SPRINTING));
                            }
                            
                            // "çok hızlı" ama hile korumalarına yakalanmaması için rastgele kısa bir gecikme (2-5 tick = ~100-250ms)
                            tickDelay = 2 + random.nextInt(4);
                            return;
                        }
                    }
                }
            }
        }
    }

    // Ana envanterde (9-35 arası slotlarda) aynı eşyayı ara
    private int findItemInInventory(Item targetItem) {
        // Slot 9'dan 35'e kadar (36 hariç) player inventory'sidir. (Slot 0-8 hotbar)
        // Ancak containerSlot olarak PlayerInventoryScreenHandler'da hotbar 36-44, main 9-35'tir.
        // Fabric 1.8.9'da: 
        // 1-4 zırh, 5-8 craft, 9-35 main inv, 36-44 hotbar.
        for (int i = 9; i < 36; i++) {
            ItemStack stack = client.player.playerScreenHandler.getSlot(i).getStack();
            if (stack != null && !stack.isEmpty() && stack.getItem() == targetItem) {
                return i;
            }
        }
        return -1;
    }
}
