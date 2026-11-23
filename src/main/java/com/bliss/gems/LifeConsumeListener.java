package com.bliss.gems;

import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class LifeConsumeListener implements Listener {
    private final PlayerDataStore players;

    public LifeConsumeListener(PlayerDataStore var1, int var2) {
        this.players = var1;
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onConsume(PlayerItemConsumeEvent var1) {
        Player var11 = var1.getPlayer();
        Material var12 = var1.getItem().getType();
        Material var3 = Material.GOLDEN_APPLE;
        if (var12 != var3) {
            Material var40 = Material.ENCHANTED_GOLDEN_APPLE;
            if (var12 != var40) {
                return;
            }
        }

        int var25 = BlissCoreItems.gemIdOf(var11.getInventory().getItemInOffHand(), 382441079);
        int var42 = GemType.LIFE.id();
        if (var25 != var42) {
            GemType var43 = GemType.LIFE;
            if (!GoldGem.hasAbsorbedSoul(var11, var43, 1603837876)) {
                return;
            }
        }

        PlayerDataStore var29 = this.players;
        UUID var45 = var11.getUniqueId();
        PlayerData var14 = var29.get(var45, 1264010596);
        if (var14.energy(1160853318) > 3) {
            if (var14.canUsePassives(2051432579)) {
                String var35 = "life";
                String var47 = "golden-apple-absorption";
                if (GemTuning.passive(var35, var47, true, 14309468)) {
                    Material var48 = Material.GOLDEN_APPLE;
                    if (var12 == var48) {
                        PotionEffectType var51 = PotionEffectType.ABSORPTION;
                        PotionEffect var50 = new PotionEffect(var51, 2400, 1, true, false, false);
                        GrantedEffect.grant(var11, var50, 491118892);
                    } else {
                        PotionEffectType var5 = PotionEffectType.ABSORPTION;
                        PotionEffect var49 = new PotionEffect(var5, 6000, 4, true, false, false);
                        GrantedEffect.grant(var11, var49, 491118892);
                    }
                }
            }
        }
    }
}
