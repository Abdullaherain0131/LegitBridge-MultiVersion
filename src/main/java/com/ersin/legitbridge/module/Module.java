package com.ersin.legitbridge.module;

import net.minecraft.client.MinecraftClient;

public abstract class Module {
    private String name;
    private String description;
    private Category category;
    private boolean enabled;
    private int keyBind;
    public final MinecraftClient client = MinecraftClient.getInstance();

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
        this.keyBind = -1; // Unbound by default
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    // Lifecycle events to be overridden by modules
    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
}
