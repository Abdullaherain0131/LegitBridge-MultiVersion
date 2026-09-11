package com.ersin.legitbridge.module.impl;

import com.ersin.legitbridge.Humanizer;
import com.ersin.legitbridge.config.ModConfig;
import com.ersin.legitbridge.module.Category;
import com.ersin.legitbridge.module.Module;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.hit.HitResult;

public class LegitWTap extends Module {

    private int releaseTicks = 0;
    private int pressTicks = 0;
    private boolean isWtapping = false;
    private boolean waitingForHit = false;

    public LegitWTap() {
        super("LegitWTap", "Düşmana vurduğunuzda W tuşunu Gaussian gecikme ile bırakıp geri basarak maksimum knockback verir.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (!ModConfig.enabled || !ModConfig.legitWTap || client.player == null || client.world == null) return;

        // Vuruş Tespiti (Legit olması için sol tıka basıldığında bir hit olup olmadığını kontrol etmeliyiz)
        // Fabric'te onAttackEvent eventleri daha iyidir ancak onTick'te basitçe kontrol edebiliriz
        
//? if <=1.19.2 {
        /*if (com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client)) {
*///?} else {
        if (com.ersin.legitbridge.utils.VersionHelper.isAttackKeyPressed(client)) {
//?}
            if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
                // Rakibe vurduysak ve W-tap döngüsünde değilsek
                if (client.player.isSprinting() && !isWtapping) {
                    isWtapping = true;
                    // Vuruştan sonra W'yi bırakma gecikmesi (Rastgele, makro hissi vermez)
                    releaseTicks = Humanizer.getGaussianDelay(1, 3);
                    // W'yi bıraktıktan sonra tekrar basma gecikmesi
                    pressTicks = Humanizer.getGaussianDelay(2, 5); 
                }
            }
        }

        if (isWtapping) {
            if (releaseTicks > 0) {
                releaseTicks--;
                if (releaseTicks == 0) {
                    // W bırak
                    client.player.setSprinting(false);
                    // Garanti olsun diye paketi de yolla
                    client.player.networkHandler.sendPacket(new ClientCommandC2SPacket(client.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                }
            } else if (pressTicks > 0) {
                pressTicks--;
                if (pressTicks == 0) {
                    // Tekrar W bas (Eğer W'ye basılı tutuyorsak)
                    //? if >1.19.2 {
if (client.options.forwardKey.isPressed()) {
//?} else {
/*if (client.options.keyForward.isPressed()) {*///?}
                        client.player.setSprinting(true);
                        client.player.networkHandler.sendPacket(new ClientCommandC2SPacket(client.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
                    }
                    isWtapping = false;
                }
            }
        }
    }
}
