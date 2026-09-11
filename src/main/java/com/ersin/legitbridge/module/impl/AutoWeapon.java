package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.item.SwordItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class AutoWeapon extends Module {

    public AutoWeapon() {
        super("AutoWeapon", "Birine vururken otomatik olarak en güçlü kılıcı/baltayı eline alır.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (client.player == null || client.world == null) return;

        // Sadece sol tıka basıldığında ve hedef bir entity (oyuncu/yaratık) ise çalışır.
//? if <=1.19.2 {
        /*if (com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client)) {
*///?} else {
        if (com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client)) {
//?}
            if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
                Entity target = ((EntityHitResult) client.crosshairTarget).getEntity();
                
                // Oyuncu ya da yaratıklara vuruyorsak
                if (target instanceof net.minecraft.entity.LivingEntity) {
                    switchToBestWeapon();
                }
            }
        }
    }

    private void switchToBestWeapon() {
        int bestSlot = -1;
        float bestDamage = 0f;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player).getStack(i);
            Item item = stack.getItem();
            
            float damage = 0f;
            if (item instanceof SwordItem) {
                damage = ((SwordItem) item).getAttackDamage();
            } else if (item instanceof AxeItem) {
                damage = ((AxeItem) item).getAttackDamage();
            }

            if (damage > bestDamage) {
                bestDamage = damage;
                bestSlot = i;
            }
        }

        if (bestSlot != -1 && 
            com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot != bestSlot) {
            com.ersin.legitbridge.utils.PlayerUtils.getInventory(client.player).selectedSlot = bestSlot;
        }
    }
}
