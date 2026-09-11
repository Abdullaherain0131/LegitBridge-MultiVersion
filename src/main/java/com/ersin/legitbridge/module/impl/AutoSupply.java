package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import com.ersin.legitbridge.module.Module;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.Humanizer;
import com.ersin.legitbridge.helpers.InventoryMouseMover;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.slot.Slot;

public class AutoSupply extends Module {
    public AutoSupply() {
        super("AutoSupply", "AutoSupply module", Category.LEGIT);
    }

    private enum State {
        IDLE,
        WAITING_GUI,
        MOVING_TO_SOURCE,
        MOVING_TO_HOTBAR
    }

    private State currentState = State.IDLE;
    private Item lastItem = null;
    private long actionTimer = 0;
    private int sourceSlot = -1;
    private int targetHotbarSlot = -1;

    @Override
    public void onTick() {
        if (client.player == null) {
            return;
        }

        PlayerInventory inventory = com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player);
        ItemStack currentStack = inventory.getMainHandStack();

        // Handle state machine for moving mouse
        if (currentState != State.IDLE && !(client.currentScreen instanceof InventoryScreen)) {
            if (currentState != State.WAITING_GUI) {
                currentState = State.IDLE;
            }
        }

        switch (currentState) {
            case IDLE:
                if (currentStack.isEmpty()) {
                    if (lastItem != null && lastItem instanceof BlockItem) {
                        for (int i = 9; i < 36; i++) {
                            ItemStack stack = inventory.getStack(i);
                            if (!stack.isEmpty() && stack.getItem() == lastItem) {
                                sourceSlot = i;
                                targetHotbarSlot = inventory.selectedSlot + 36;
                                
                                if (!(client.currentScreen instanceof InventoryScreen)) {
                                    com.ersin.legitbridge.utils.VersionHelper.setScreen(client, new net.minecraft.client.gui.screen.ingame.InventoryScreen(client.player));
                                }
                                currentState = State.WAITING_GUI;
                                actionTimer = System.currentTimeMillis();
                                break;
                            }
                        }
                    }
                } else {
                    if (currentStack.getItem() instanceof BlockItem) {
                        lastItem = currentStack.getItem();
                    } else {
                        lastItem = null;
                    }
                }
                break;

            case WAITING_GUI:
                if (client.currentScreen instanceof InventoryScreen) {
                    InventoryScreen screen = (InventoryScreen) client.currentScreen;
                    Slot sSlot = screen.getScreenHandler().slots.get(sourceSlot);
                    
                    if (sSlot != null) {
                        currentState = State.MOVING_TO_SOURCE;
                        InventoryMouseMover.moveToSlot(sSlot, 100, () -> {
                            if (client.currentScreen instanceof InventoryScreen) {
                                client.interactionManager.clickSlot(
                                    ((InventoryScreen) client.currentScreen).getScreenHandler().syncId,
                                    sSlot.id,
                                    0,
                                    SlotActionType.PICKUP,
                                    client.player
                                );
                                
                                Slot tSlot = screen.getScreenHandler().slots.get(targetHotbarSlot);
                                currentState = State.MOVING_TO_HOTBAR;
                                InventoryMouseMover.moveToSlot(tSlot, 100, () -> {
                                    if (client.currentScreen instanceof InventoryScreen) {
                                        client.interactionManager.clickSlot(
                                            ((InventoryScreen) client.currentScreen).getScreenHandler().syncId,
                                            tSlot.id,
                                            0,
                                            SlotActionType.PICKUP,
                                            client.player
                                        );
                                        client.player.closeHandledScreen();
                                        currentState = State.IDLE;
                                    }
                                });
                            } else {
                                currentState = State.IDLE;
                            }
                        });
                    } else {
                        currentState = State.IDLE;
                    }
                } else if (System.currentTimeMillis() - actionTimer > 1000) {
                    currentState = State.IDLE;
                }
                break;
                
            case MOVING_TO_SOURCE:
            case MOVING_TO_HOTBAR:
                break;
        }
    }
}
