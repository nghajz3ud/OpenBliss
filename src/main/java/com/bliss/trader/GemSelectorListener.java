package com.bliss.trader;

import com.bliss.BlissPlugin;
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
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

public final class GemSelectorListener implements Listener {
    private static final GemType[] CASE;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final BlissCoreItems items;
    private final TraderListener trader;

    public GemSelectorListener(BlissPlugin var1, PlayerDataStore var2, BlissCoreItems var3, TraderListener var4, int var5) {
        this.plugin = var1;
        this.players = var2;
        this.items = var3;
        this.trader = var4;
    }

    private boolean busy(Player var1, PlayerData var2, int var3) {
        TraderListener var6 = this.trader;
        UUID var13 = var1.getUniqueId();
        boolean var12;
        if (!var6.isShuffling(var13, 2122195762) && !var2.tradeLock(1959314746) && !var2.restorationLock(1109120985)) {
            var12 = false;
        } else {
            var12 = true;
        }

        return var12;
    }

    private void deny(Player var1, String var2, int var3) {
        var1.closeInventory();
        Component var7 = Text.legacy(var2, 373192400);
        var1.sendMessage(var7);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent var1) {
        EquipmentSlot var7 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var7 == var3) {
            switch (var1.getAction()) {
                case RIGHT_CLICK_AIR:
                case RIGHT_CLICK_BLOCK:
                    Player var5 = var1.getPlayer();
                    BlissCoreItems var13 = this.items;
                    ItemStack var37 = var5.getInventory().getItemInMainHand();
                    if (!var13.isGemSelector(var37, 1538829506)) {
                        return;
                    } else {
                        var1.setCancelled(true);
                        if (JuggernautGemGuardListener.isJuggernaut(var5, 859003337)) {
                            Component var40 = Text.legacy("&cA Juggernaut cannot carry a gem.", 373192400);
                            var5.sendMessage(var40);
                            return;
                        } else {
                            PlayerDataStore var20 = this.players;
                            UUID var42 = var5.getUniqueId();
                            PlayerData var6 = var20.get(var42, 1264010596);
                            GemType var23 = var6.gem(1413186337);
                            GemType var43 = GemType.NONE;
                            if (var23 == var43) {
                                Component var51 = Text.legacy("&cYou have no gem to trade.", 373192400);
                                var5.sendMessage(var51);
                                return;
                            } else if (this.busy(var5, var6, 145671566)) {
                                Component var46 = Text.legacy("&cYour gem is busy — try again in a moment.", 373192400);
                                var5.sendMessage(var46);
                                return;
                            } else {
                                if (BlissCoreItems.findGemSlot(var5, 1702349856) < 0) {
                                    Component var48 = Text.legacy("&cYour gem has to be on you — take it out of the chest first.", 373192400);
                                    var5.sendMessage(var48);
                                    return;
                                }

                                this.open(var5, var6, 657668146);
                                return;
                            }
                        }
                    }
            }
        }
    }

    private void open(Player var1, PlayerData var2, int var3) {
        Server var12 = this.plugin.getServer();
        GemSelectorListener.SelectorHolder var5 = new GemSelectorListener.SelectorHolder();
        Component var37 = Text.legacy("<##82F3FF>ɢᴇᴍ ꜱᴇʟᴇᴄᴛᴏʀ", 373192400);
        Inventory var8 = var12.createInventory(var5, 9, var37);
        int var9 = 0;

        while (true) {
            int var23 = CASE.length;
            if (var9 >= var23) {
                var1.openInventory(var8);
                Location var28 = var1.getLocation();
                Sound var35 = Sound.BLOCK_ENDER_CHEST_OPEN;
                var1.playSound(var28, var35, 0.7F, 1.4F);
                return;
            }

            GemType[] var16 = CASE;
            GemType var10 = var16[var9];
            GemType var39 = var2.gem(1413186337);
            ItemStack var36;
            if (var10 == var39) {
                BlissCoreItems var34 = this.items;
                var36 = var34.gemDisplayCurrent(var10, 1341706571);
            } else {
                BlissCoreItems var32 = this.items;
                var36 = var32.gemDisplay$1463922612(var10, 1977498201);
            }

            var8.setItem(var9, var36);
            var9++;
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent var1) {
        if (var1.getInventory().getHolder() instanceof GemSelectorListener.SelectorHolder) {
            var1.setCancelled(true);
            if (var1.getWhoClicked() instanceof Player var7) {
                if (var1.getRawSlot() >= 0) {
                    int var22 = var1.getRawSlot();
                    int var31 = CASE.length;
                    if (var22 < var31) {
                        GemType[] var23 = CASE;
                        int var33 = var1.getRawSlot();
                        GemType var37 = var23[var33];
                        BukkitScheduler var28 = this.plugin.getServer().getScheduler();
                        BlissPlugin var35 = this.plugin;
                        Runnable var36 = () -> this.commit(var7, var37, 1407514424);
                        var28.runTask(var35, var36);
                        return;
                    }
                }
            }
        }
    }

    private void commit(Player var1, GemType var2, int var3) {
        if (var1.isOnline()) {
            ItemStack var9 = var1.getInventory().getItemInMainHand();
            BlissCoreItems var16 = this.items;
            if (!var16.isGemSelector(var9, 1538829506)) {
                String var81 = "&cYou are no longer holding the Gem Selector.";
                this.deny(var1, var81, 162339232);
            } else if (JuggernautGemGuardListener.isJuggernaut(var1, 859003337)) {
                String var6 = "&cA Juggernaut cannot carry a gem.";
                this.deny(var1, var6, 162339232);
            } else {
                PlayerDataStore var22 = this.players;
                UUID var48 = var1.getUniqueId();
                PlayerData var10 = var22.get(var48, 1264010596);
                GemType var25 = var10.gem(1413186337);
                GemType var49 = GemType.NONE;
                if (var25 == var49) {
                    String var73 = "&cYou have no gem to trade.";
                    this.deny(var1, var73, 162339232);
                } else if (this.busy(var1, var10, 145671566)) {
                    String var75 = "&cYour gem is busy — try again in a moment.";
                    this.deny(var1, var75, 162339232);
                } else if (BlissCoreItems.findGemSlot(var1, 1702349856) < 0) {
                    String var80 = "&cYour gem has to be on you — take it out of the chest first.";
                    this.deny(var1, var80, 162339232);
                } else {
                    GemType var54 = var10.gem(1413186337);
                    if (var2 == var54) {
                        Component var56 = Text.legacy("&cThat is already your gem.", 373192400);
                        var1.sendMessage(var56);
                        Location var58 = var1.getLocation();
                        Sound var76 = Sound.BLOCK_NOTE_BLOCK_BASS;
                        var1.playSound(var58, var76, 0.7F, 0.6F);
                    } else {
                        GameMode var36 = var1.getGameMode();
                        GameMode var59 = GameMode.CREATIVE;
                        if (var36 != var59) {
                            int var62 = var9.getAmount() - 1;
                            var9.setAmount(var62);
                        }

                        var10.setGem(var2, 2137390487);
                        BlissCoreItems var40 = this.items;
                        var40.refreshHeldGemItem(var1, var10, 835235475);
                        var1.closeInventory();
                        Component var68 = Text.legacy("\ud83d\udd2e &aYou took the " + var2.coloredName() + " &agem.", 373192400);
                        var1.sendMessage(var68);
                        Location var70 = var1.getLocation();
                        Sound var79 = Sound.BLOCK_AMETHYST_BLOCK_CHIME;
                        var1.playSound(var70, var79, 1.0F, 1.2F);
                    }
                }
            }
        }
    }

    static {
        GemType[] var3 = new GemType[8];
        GemType var2 = GemType.STRENGTH;
        var3[0] = var2;
        GemType var11 = GemType.FIRE;
        var3[1] = var11;
        GemType var12 = GemType.WEALTH;
        var3[2] = var12;
        GemType var13 = GemType.SPEED;
        var3[3] = var13;
        GemType var14 = GemType.PUFF;
        var3[4] = var14;
        GemType var15 = GemType.ASTRA;
        var3[5] = var15;
        GemType var16 = GemType.LIFE;
        var3[6] = var16;
        GemType var17 = GemType.FLUX;
        var3[7] = var17;
        CASE = var3;
    }

    private static final class SelectorHolder implements InventoryHolder {
        public Inventory getInventory() {
            return null;
        }
    }
}
