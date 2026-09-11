package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.Humanizer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LegitChestStealer extends Module {

    private int tickDelay = 0;
    private final Random random = new Random();

    public LegitChestStealer() {
        super("LegitChestStealer", "Sandıkları insan hızında boşaltır.", Category.LEGIT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.legitChestStealer || client.player == null) return;
        
        if (tickDelay > 0) {
            tickDelay--;
            return;
        }

        if (client.player.currentScreenHandler instanceof GenericContainerScreenHandler) {
            GenericContainerScreenHandler container = (GenericContainerScreenHandler) client.player.currentScreenHandler;
            int rows = container.getRows();
            int slots = rows * 9;
            
            // Tüm slot numaralarını listeye al ve karıştır (İnsancıl Toplama Yolu - Pathing)
            List<Integer> slotOrder = new ArrayList<>();
            for (int i = 0; i < slots; i++) {
                slotOrder.add(i);
            }
            Collections.shuffle(slotOrder, random);

            boolean empty = true;

            for (int i : slotOrder) {
                ItemStack stack = container.getSlot(i).getStack();
                if (stack != null && !stack.isEmpty()) {
                    empty = false;
                    
                    if (client.interactionManager != null) {
                        client.interactionManager.clickSlot(
                            container.syncId, 
                            i, 
                            0, 
                            SlotActionType.QUICK_MOVE, 
                            client.player
                        );
                    }
                    
                    // Rastgele 1 ile 5 tick arası Gaussian gecikme (ortalama 3 tick ~150 ms)
                    tickDelay = Humanizer.getGaussianDelay(1, 5);
                    return; // Bir eşya aldık, beklemeye geç
                }
            }
            
            if (empty && client.currentScreen != null) {
                // Eğer sandık boşaldıysa ekranı kapatmadan önce insancıl 2-5 tick bekle
                if (tickDelay <= 0) {
                     client.player.closeHandledScreen(); 
                }
            }
        }
    }
}
