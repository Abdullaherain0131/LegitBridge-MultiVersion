package com.ersin.legitbridge.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Invoker("doAttack")
    void invokeDoAttack();
    
    @Invoker("doItemUse")
    void invokeDoItemUse();
    
    @Accessor("attackCooldown")
    void setAttackCooldown(int cooldown);
}
