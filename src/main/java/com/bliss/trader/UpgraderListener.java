package com.bliss.trader;

import com.bliss.BlissPlugin;
import com.bliss.core.AchievementStore;
import com.bliss.core.GemType;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public final class UpgraderListener implements Listener {
    private final BlissPlugin plugin;
    private final BlissCoreItems items;
    private final PlayerDataStore players;
    private final TraderListener trader;

    public UpgraderListener(BlissPlugin var1, PlayerDataStore var2, BlissCoreItems var3, TraderListener var4, int var5) {
        this.plugin = var1;
        this.items = var3;
        this.players = var2;
        this.trader = var4;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent var1) {
        EquipmentSlot var12 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var12 == var3) {
            Action var14 = var1.getAction();
            Action var60 = Action.RIGHT_CLICK_AIR;
            if (var14 != var60) {
                Action var16 = var1.getAction();
                Action var61 = Action.RIGHT_CLICK_BLOCK;
                if (var16 != var61) {
                    return;
                }
            }

            Player var7 = var1.getPlayer();
            ItemStack var8 = var7.getInventory().getItemInMainHand();
            BlissCoreItems var23 = this.items;
            if (var23.isUpgrader(var8, 280446299)) {
                var1.setCancelled(true);
                if (JuggernautGemGuardListener.isJuggernaut(var7, 859003337)) {
                    Component var89 = Text.legacy("&cJuggernauts cannot use gem upgraders.", 373192400);
                    var7.sendMessage(var89);
                } else {
                    if (this.trader != null) {
                        TraderListener var31 = this.trader;
                        UUID var65 = var7.getUniqueId();
                        if (var31.isShuffling(var65, 2122195762)) {
                            Component var67 = Text.legacy("&cWait for your gem roll to finish.", 373192400);
                            var7.sendMessage(var67);
                            return;
                        }
                    }

                    PlayerDataStore var35 = this.players;
                    UUID var69 = var7.getUniqueId();
                    PlayerData var9 = var35.get(var69, 1264010596);
                    GemType var38 = var9.gem(1413186337);
                    GemType var70 = GemType.NONE;
                    if (var38 == var70) {
                        Component var72 = Text.legacy("&cYou have no gem to upgrade!", 373192400);
                        var7.sendMessage(var72);
                    } else if (var9.tier2(629984660)) {
                        Component var74 = Text.legacy("\ud83d\udd2e &bYour gem is already upgraded", 373192400);
                        var7.sendMessage(var74);
                    } else {
                        GameMode var44 = var7.getGameMode();
                        GameMode var75 = GameMode.CREATIVE;
                        if (var44 != var75) {
                            int var78 = var8.getAmount() - 1;
                            var8.setAmount(var78);
                        }

                        var9.setTier2(true, 1757992594);
                        int var10 = BlissCoreItems.findGemSlot(var7, 1702349856);
                        TraderListener.clearGemItems(var7, 579055745);
                        BlissCoreItems var51 = this.items;
                        GemType var81 = var9.gem(1413186337);
                        int var94 = var9.energy(1160853318);
                        ItemStack var11 = var51.gemItem(var81, true, var94, 256965471);
                        BlissCoreItems.giveGemOrHold(var7, var10, var11, 2133739666);
                        Location var84 = var7.getLocation();
                        Sound var92 = Sound.ENTITY_PLAYER_LEVELUP;
                        var7.playSound(var84, var92, 1.0F, 1.0F);
                        Component var86 = Text.legacy("\ud83d\udd2e &bYou have upgraded your gem to tier 2", 373192400);
                        var7.sendMessage(var86);
                        AchievementStore var58 = this.plugin.advancements(1975441698);
                        AchievementStore.Advancement var93 = AchievementStore.Advancement.THE_NEXT_LEVEL;
                        var58.unlock(var7, var93, 1132982324);
                    }
                }
            }
        }
    }
}
