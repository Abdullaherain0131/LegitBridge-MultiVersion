package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

import java.util.HashMap;
import java.util.Map;

public class AutoArmor extends Module {

    private int tickDelay = 0;

    public AutoArmor() {
        super("AutoArmor", "Envanterdeki en iyi zırhı otomatik giyer.", Category.LEGIT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.autoArmor || client.player == null) return;
        
        // Hızlıca giymek anti-cheat'e yakalanabilir, 4 tick gecikme ekliyoruz.
        if (tickDelay > 0) {
            tickDelay--;
            return;
        }

        // GUI açıkken sandık lootluyor olabiliriz, açıkken karışmamak en iyisi
        if (client.currentScreen != null) return;

        Map<EquipmentSlot, Integer> bestArmorSlots = new HashMap<>();
        Map<EquipmentSlot, Double> bestArmorValues = new HashMap<>();

        // Mevcut giyilen zırhların değerlerini hesapla
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;
            ItemStack currentArmor = client.player.getEquippedStack(slot);
            bestArmorValues.put(slot, getArmorValue(currentArmor));
            bestArmorSlots.put(slot, -1); // -1 demek mevcut zırh demek
        }

        // Envanterdeki zırhları kontrol et (9 - 35 arası ana envanter, 0 - 8 hotbar)
        for (int i = 9; i < 45; i++) {
            // PlayerScreenHandler için: 5-8 zırh slotları, 9-35 ana envanter, 36-44 hotbar
            ItemStack stack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof ArmorItem)) continue;

            ArmorItem armorItem = (ArmorItem) stack.getItem();
            EquipmentSlot slotType = armorItem.getSlotType();
            double value = getArmorValue(stack);

            if (value > bestArmorValues.get(slotType)) {
                bestArmorValues.put(slotType, value);
                // i değeri raw inventory index, clickSlot id'sine çevirmeliyiz
                int slotId = i >= 36 ? i + 9 : i;
                if (i >= 36) slotId = i;
                else slotId = i; // Envanter index -> screen slot index (yaklaşık)
                
                // Daha stabil bir slot eşleştirmesi:
                bestArmorSlots.put(slotType, getSlotId(i));
            }
        }

        // En iyi zırhı giy
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;
            
            int bestSlot = bestArmorSlots.get(slot);
            if (bestSlot != -1) {
                // Eğer üzerimizde zırh varsa önce onu çıkaralım veya doğrudan yer değiştirelim
                equipArmor(bestSlot);
                tickDelay = 4;
                return; // Sadece tick başına 1 zırh giy, delay girsin
            }
        }
    }

    private int getSlotId(int invIndex) {
        // Envanter index'inden PlayerScreenHandler slot ID'sine dönüşüm
        if (invIndex >= 0 && invIndex <= 8) {
            return 36 + invIndex; // Hotbar
        }
        return invIndex; // Main inventory (9-35)
    }

    private void equipArmor(int slot) {
        if (client.interactionManager != null && client.player != null) {
            client.interactionManager.clickSlot(
                client.player.playerScreenHandler.syncId, 
                slot, 
                0, 
                SlotActionType.QUICK_MOVE, 
                client.player
            );
        }
    }

    private double getArmorValue(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof ArmorItem)) return 0;
        
        ArmorItem armor = (ArmorItem) stack.getItem();
        double value = armor.getProtection();
        
        // Büyüleri hesaba kat (Koruma vb.)
        int protectionLevel = EnchantmentHelper.getLevel(Enchantments.PROTECTION, stack);
        value += protectionLevel * 1.5; // Kaba bir hesaplama

        return value;
    }
}
