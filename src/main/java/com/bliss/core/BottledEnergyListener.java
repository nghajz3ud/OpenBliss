package com.bliss.core;

import com.bliss.BlissPlugin;
import com.bliss.goldgem.GoldGemManager;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public final class BottledEnergyListener implements Listener {
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final BlissCoreItems items;

    public BottledEnergyListener(BlissPlugin var1, PlayerDataStore var2, BlissCoreItems var3, int var4) {
        this.plugin = var1;
        this.players = var2;
        this.items = var3;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent var1) {
        EquipmentSlot var12 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var12 == var3) {
            Action var14 = var1.getAction();
            Action var77 = Action.RIGHT_CLICK_AIR;
            if (var14 != var77) {
                Action var16 = var1.getAction();
                Action var78 = Action.RIGHT_CLICK_BLOCK;
                if (var16 != var78) {
                    return;
                }
            }

            Player var8 = var1.getPlayer();
            ItemStack var9 = var8.getInventory().getItemInMainHand();
            BlissCoreItems var23 = this.items;
            if (var23.isUselessEnergy(var9, 1117390884)) {
                var1.setCancelled(true);
                Component var114 = Text.legacy("&cThis bottle of energy is useless.", 373192400);
                var8.sendMessage(var114);
            } else {
                BlissCoreItems var26 = this.items;
                if (var26.isBottledEnergy(var9, 1793320558)) {
                    Long var10 = BlissCoreItems.energyExpiryOf(var9, 374126995);
                    if (this.plugin.globalState(724248306).bottledEnergyExpiry(261106370) && var10 != null) {
                        long var36 = var10;
                        long var4 = System.currentTimeMillis();
                        if (var36 <= var4) {
                            BlissCoreItems var72 = this.items;
                            var72.makeUseless(var9, 607150677);
                            var1.setCancelled(true);
                            Component var111 = Text.legacy("&cThis bottle of energy is useless.", 373192400);
                            var8.sendMessage(var111);
                            return;
                        }
                    }

                    var1.setCancelled(true);
                    if (JuggernautGemGuardListener.isJuggernaut(var8, 859003337)) {
                        Component var83 = Text.legacy("&cJuggernauts cannot absorb energy.", 373192400);
                        var8.sendMessage(var83);
                    } else {
                        GoldGemManager var45 = this.plugin.goldGemManager(256518991);
                        UUID var85 = var8.getUniqueId();
                        if (var45.isRepurposed(var85, 1313846040)) {
                            PlayerDataStore var48 = this.players;
                            UUID var87 = var8.getUniqueId();
                            GemType var50 = var48.get(var87, 1264010596).gem(1413186337);
                            GemType var88 = GemType.NONE;
                            if (var50 == var88) {
                                Component var90 = Text.legacy("&cYour soul has been repurposed. Energy no longer answers to you.", 373192400);
                                var8.sendMessage(var90);
                                return;
                            }
                        }

                        PlayerDataStore var53 = this.players;
                        UUID var92 = var8.getUniqueId();
                        PlayerData var11 = var53.get(var92, 1264010596);
                        if (var11.energy(1160853318) <= 1) {
                            Component var95 = Text.legacy("&cYour gem is shattered!", 373192400);
                            var8.sendMessage(var95);
                        } else if (var11.energy(1160853318) >= 11) {
                            Component var98 = Text.legacy("&cYour gem is already at max energy.", 373192400);
                            var8.sendMessage(var98);
                        } else {
                            int var101 = var9.getAmount() - 1;
                            var9.setAmount(var101);
                            var11.addEnergy(1, 514979476);
                            BlissCoreItems var64 = this.items;
                            var64.refreshHeldGemItem(var8, var11, 835235475);
                            Location var105 = var8.getLocation();
                            Sound var117 = Sound.ENTITY_PLAYER_LEVELUP;
                            var8.playSound(var105, var117, 1.0F, 1.0F);
                            if (var11.energy(1160853318) >= 11) {
                                AchievementStore var70 = this.plugin.advancements(1975441698);
                                AchievementStore.Advancement var118 = AchievementStore.Advancement.OVERFLOWING;
                                var70.unlock(var8, var118, 1132982324);
                            }
                        }
                    }
                }
            }
        }
    }
}
