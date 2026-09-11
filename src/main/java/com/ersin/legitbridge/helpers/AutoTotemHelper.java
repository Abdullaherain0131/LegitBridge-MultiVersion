package com.ersin.legitbridge.helpers;

import com.ersin.legitbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotemHelper {

    private enum State {
        IDLE,
        WAITING_GUI,
        MOVING_TO_TOTEM,
        MOVING_TO_OFFHAND
    }

    private static State currentState = State.IDLE;
    private static int totemSlotId = -1;
    private static long actionTimer = 0;

    public static void onTick(MinecraftClient client) {
        if (!ModConfig.enabled || !ModConfig.autoTotem || client.player == null) {
            currentState = State.IDLE;
            return;
        }

        // If not idle and not moving, but GUI closed unexpectedly, reset
        if (currentState != State.IDLE && !(client.currentScreen instanceof InventoryScreen)) {
            if (currentState != State.WAITING_GUI) {
                currentState = State.IDLE;
            }
        }

        switch (currentState) {
            case IDLE:
                // Check if we need a totem
                if (client.player.getHealth() <= 6.0f) { // 3 hearts
                    if (client.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
                        totemSlotId = findTotem(com.ersin.legitbridge.utils.VersionHelper.getInventory(client.player));
                        if (totemSlotId != -1) {
                            if (!(client.currentScreen instanceof InventoryScreen)) {
                                com.ersin.legitbridge.utils.VersionHelper.setScreen(client, new net.minecraft.client.gui.screen.ingame.InventoryScreen(client.player));
                            }
                            currentState = State.WAITING_GUI;
                            actionTimer = System.currentTimeMillis();
                        }
                    }
                }
                break;

            case WAITING_GUI:
                if (client.currentScreen instanceof InventoryScreen) {
                    // GUI is open, start moving mouse to totem
                    InventoryScreen screen = (InventoryScreen) client.currentScreen;
                    // In InventoryScreen, slots are 0-45.
                    // Survival inventory slots:
                    // 0: Craft output
                    // 1-4: Craft grid
                    // 5-8: Armor
                    // 9-35: Main inventory
                    // 36-44: Hotbar
                    // 45: Offhand
                    
                    int targetSlotId = -1;
                    if (totemSlotId >= 0 && totemSlotId < 9) {
                        targetSlotId = totemSlotId + 36;
                    } else if (totemSlotId >= 9 && totemSlotId < 36) {
                        targetSlotId = totemSlotId;
                    }
                    
                    Slot targetSlot = null;
                    if (targetSlotId != -1) {
                        targetSlot = screen.getScreenHandler().slots.get(targetSlotId);
                    }

                    if (targetSlot != null) {
                        currentState = State.MOVING_TO_TOTEM;
                        final Slot finalTargetSlot = targetSlot;
                        InventoryMouseMover.moveToSlot(finalTargetSlot, 120, () -> {
                            // Arrived at totem
                            if (client.currentScreen instanceof InventoryScreen) {
                                client.interactionManager.clickSlot(
                                    ((InventoryScreen) client.currentScreen).getScreenHandler().syncId,
                                    finalTargetSlot.id,
                                    0,
                                    SlotActionType.PICKUP,
                                    client.player
                                );
                                
                                // Now move to offhand (Slot index 40 in player inventory, but in screen handler it's usually slot ID 45)
                                Slot offhandSlot = screen.getScreenHandler().slots.get(45);
                                currentState = State.MOVING_TO_OFFHAND;
                                InventoryMouseMover.moveToSlot(offhandSlot, 120, () -> {
                                    // Arrived at offhand
                                    if (client.currentScreen instanceof InventoryScreen) {
                                        client.interactionManager.clickSlot(
                                            ((InventoryScreen) client.currentScreen).getScreenHandler().syncId,
                                            45,
                                            0,
                                            SlotActionType.PICKUP,
                                            client.player
                                        );
                                        // Close GUI
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
                    currentState = State.IDLE; // Timeout
                }
                break;

            case MOVING_TO_TOTEM:
            case MOVING_TO_OFFHAND:
                // InventoryMouseMover handles the tick
                break;
        }
    }

    private static int findTotem(PlayerInventory inv) {
        for (int i = 0; i < 36; i++) {
            if (inv.getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                return i;
            }
        }
        return -1;
    }
}
