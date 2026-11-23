package com.bliss.trader;

import com.bliss.BlissPlugin;
import com.bliss.core.GemType;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public final class GemSwapListener implements Listener, CommandExecutor {
    private static final double RANGE = 8.0;
    private static final int EXPIRY_SECONDS = 30;
    private static final long DEDUP_MS = 250L;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final BlissCoreItems items;
    private final TraderListener trader;
    private final Map<UUID, GemSwapListener.Offer> offers;
    private final Map<UUID, Long> lastRequest;

    public GemSwapListener(BlissPlugin var1, PlayerDataStore var2, BlissCoreItems var3, TraderListener var4, int var5) {
        HashMap var7 = new HashMap();
        this.offers = var7;
        HashMap var14 = new HashMap();
        this.lastRequest = var14;
        this.plugin = var1;
        this.players = var2;
        this.items = var3;
        this.trader = var4;
    }

    public Player lookedAtPlayer(Player var1, int var2) {
        Location var13 = var1.getEyeLocation();
        Vector var14 = var13.getDirection();
        World var23 = var1.getWorld();
        Predicate<Entity> var53 = var2x -> this.swappable(var1, var2x, 757978695);
        RayTraceResult var15 = var23.rayTraceEntities(var13, var14, 8.0, 0.3, var53);
        if (var15 == null || !(var15.getHitEntity() instanceof Player var16)) {
            return null;
        } else if (!var1.canSee(var16)) {
            return null;
        } else {
            Vector var36 = var15.getHitPosition();
            Vector var47 = var13.toVector();
            double var54 = var36.distance(var47);
            World var40 = var1.getWorld();
            FluidCollisionMode var52 = FluidCollisionMode.NEVER;
            Player var44;
            if (var40.rayTraceBlocks(var13, var14, var54, var52, true) == null) {
                var44 = var16;
            } else {
                var44 = null;
            }

            return var44;
        }
    }

    private boolean swappable(Player var1, Entity var2, int var3) {
        if (!(var2 instanceof Player var6) || var6.equals(var1)) {
            return false;
        }

        if (var2.hasMetadata("NPC")) {
            return false;
        } else {
            GameMode var15 = var6.getGameMode();
            GameMode var20 = GameMode.SPECTATOR;
            boolean var18;
            if (var15 != var20) {
                var18 = true;
            } else {
                var18 = false;
            }

            return var18;
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent var1) {
        EquipmentSlot var7 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var7 == var3) {
            if (var1.getRightClicked() instanceof Player var5) {
                Player var33 = var1.getPlayer();
                if (this.swappable(var33, var5, 757978695)) {
                    if (var33.canSee(var5)) {
                        BlissCoreItems var21 = this.items;
                        ItemStack var29 = var33.getInventory().getItemInMainHand();
                        if (var21.isTrader(var29, 904484797)) {
                            var1.setCancelled(true);
                            this.request(var33, var5, 1341144368);
                        }
                    }
                }
            }
        }
    }

    public void request(Player var1, Player var2, int var3) {
        UUID var14 = var1.getUniqueId();
        UUID var15 = var2.getUniqueId();
        long var16 = System.currentTimeMillis();
        Map var30 = this.lastRequest;
        Long var18 = (Long)var30.get(var14);
        if (var18 != null) {
            long var179 = var18;
            if (var16 - var179 < 250L) {
                return;
            }
        }

        Map var40 = this.lastRequest;
        Long var185 = var16;
        var40.put(var14, var185);
        if (JuggernautGemGuardListener.isJuggernaut(var1, 859003337)) {
            Component var178 = Text.legacy("&cJuggernauts cannot use the gem trader.", 373192400);
            var1.sendMessage(var178);
        } else if (JuggernautGemGuardListener.isJuggernaut(var2, 859003337)) {
            Component var176 = Text.legacy("&c" + var2.getName() + " has no gem to swap.", 373192400);
            var1.sendMessage(var176);
        } else {
            PlayerDataStore var47 = this.players;
            GemType var49 = var47.get(var14, 1264010596).gem(1413186337);
            GemType var110 = GemType.NONE;
            if (var49 == var110) {
                Component var172 = Text.legacy("&cYou have no gem to trade.", 373192400);
                var1.sendMessage(var172);
            } else {
                PlayerDataStore var51 = this.players;
                GemType var53 = var51.get(var15, 1264010596).gem(1413186337);
                GemType var112 = GemType.NONE;
                if (var53 == var112) {
                    Component var116 = Text.legacy("&c" + var2.getName() + " has no gem to swap.", 373192400);
                    var1.sendMessage(var116);
                } else if (this.busy(var1, 6579733) || this.busy(var2, 6579733)) {
                    Component var120 = Text.legacy("&cThat gem is busy right now — try again in a moment.", 373192400);
                    var1.sendMessage(var120);
                } else if (BlissCoreItems.findGemSlot(var1, 1702349856) < 0) {
                    Component var122 = Text.legacy("&cYour gem isn't on you — you can't trade what you aren't carrying.", 373192400);
                    var1.sendMessage(var122);
                } else if (BlissCoreItems.findGemSlot(var2, 1702349856) < 0) {
                    Component var170 = Text.legacy("&c" + var2.getName() + " isn't carrying their gem.", 373192400);
                    var1.sendMessage(var170);
                } else if (!this.hasTrader(var1, 602021652)) {
                    Component var125 = Text.legacy("&cYou need a Trader to offer a swap.", 373192400);
                    var1.sendMessage(var125);
                } else {
                    Map var69 = this.offers;
                    GemSwapListener.Offer var19 = (GemSwapListener.Offer)var69.get(var15);
                    if (var19 != null) {
                        UUID var74 = var19.from;
                        if (var74.equals(var14)) {
                            Component var131 = Text.legacy("&c" + var2.getName() + " already has your offer.", 373192400);
                            var1.sendMessage(var131);
                            return;
                        }
                    }

                    if (var19 != null) {
                        Component var166 = Text.legacy("&c" + var2.getName() + " is already deciding on another offer.", 373192400);
                        var1.sendMessage(var166);
                    } else {
                        Map var79 = this.offers;
                        GemSwapListener.Offer var20 = (GemSwapListener.Offer)var79.get(var14);
                        if (var20 != null) {
                            UUID var84 = var20.from;
                            if (var84.equals(var15)) {
                                Component var162 = Text.legacy("&e" + var2.getName() + " has already offered YOU a swap — answer theirs.", 373192400);
                                var1.sendMessage(var162);
                                return;
                            }
                        }

                        long var21 = ThreadLocalRandom.current().nextLong(1L, Long.MAX_VALUE);
                        BukkitScheduler var92 = this.plugin.getServer().getScheduler();
                        BlissPlugin var137 = this.plugin;
                        Runnable var187 = () -> this.expire(var15, var21, 6646260);
                        BukkitTask var23 = var92.runTaskLater(var137, var187, 600L);
                        Map var95 = this.offers;
                        GemSwapListener.Offer var188 = new GemSwapListener.Offer(var14, var21, var23, 833066315);
                        var95.put(var15, var188);
                        Component var142 = Text.legacy("\ud83d\udd2e &bSent gem swap request to &e" + var2.getName(), 373192400);
                        var1.sendMessage(var142);
                        Location var144 = var1.getLocation();
                        Sound var189 = Sound.BLOCK_NOTE_BLOCK_HAT;
                        var1.playSound(var144, var189, 1.0F, 1.4F);
                        Component var148 = Text.legacy("\ud83d\udd2e &e" + var1.getName() + " &bwants to swap gems with you.", 373192400);
                        var2.sendMessage(var148);
                        Builder var149 = Component.text();
                        Component var191 = Text.legacy("&a&l[ACCEPT]", 373192400);
                        ClickEvent var208 = ClickEvent.runCommand("/gemswap accept " + var21);
                        Component var192 = var191.clickEvent(var208);
                        HoverEvent var211 = HoverEvent.showText(Text.legacy("&aTake the swap", 373192400));
                        Component var193 = var192.hoverEvent(var211);
                        Builder var151 = (Builder)var149.append(var193);
                        Component var195 = Text.legacy("  ", 373192400);
                        Builder var153 = (Builder)var151.append(var195);
                        Component var197 = Text.legacy("&c&l[DECLINE]", 373192400);
                        ClickEvent var215 = ClickEvent.runCommand("/gemswap decline " + var21);
                        Component var198 = var197.clickEvent(var215);
                        HoverEvent var218 = HoverEvent.showText(Text.legacy("&cTurn it down", 373192400));
                        Component var199 = var198.hoverEvent(var218);
                        BuildableComponent var156 = ((Builder)var153.append(var199)).build();
                        var2.sendMessage(var156);
                        Location var158 = var2.getLocation();
                        Sound var200 = Sound.BLOCK_NOTE_BLOCK_BELL;
                        var2.playSound(var158, var200, 1.0F, 1.2F);
                    }
                }
            }
        }
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player var9) {
            if (var4.length < 2) {
                return false;
            } else {
                long var10;
                try {
                    var10 = Long.parseLong(var4[1]);
                } catch (NumberFormatException var64) {
                    return false;
                }

                Map var26 = this.offers;
                UUID var50 = var9.getUniqueId();
                GemSwapListener.Offer var63 = (GemSwapListener.Offer)var26.get(var50);
                if (var63 != null) {
                    long var31 = var63.token;
                    if (var31 == var10) {
                        if (var4[0].equalsIgnoreCase("accept")) {
                            this.accept(var9, var63, 1335624906);
                        } else {
                            if (!var4[0].equalsIgnoreCase("decline")) {
                                return false;
                            }

                            this.decline(var9, var63, 864196051);
                        }

                        return true;
                    }
                }

                Component var52 = Text.legacy("&cThat offer is no longer open.", 373192400);
                var9.sendMessage(var52);
                return true;
            }
        } else {
            Component var60 = Text.legacy("&cPlayers only.", 373192400);
            var1.sendMessage(var60);
            return true;
        }
    }

    private void accept(Player var1, GemSwapListener.Offer var2, int var3) {
        Player var7 = Bukkit.getPlayer(var2.from);
        if (var7 == null) {
            UUID var57 = var1.getUniqueId();
            this.close(var57, var2, 1494764998);
            Component var59 = Text.legacy("&cThey are no longer online.", 373192400);
            var1.sendMessage(var59);
        } else {
            PlayerDataStore var17 = this.players;
            UUID var61 = var7.getUniqueId();
            PlayerData var8 = var17.get(var61, 1264010596);
            PlayerDataStore var20 = this.players;
            UUID var63 = var1.getUniqueId();
            PlayerData var9 = var20.get(var63, 1264010596);
            GemType var23 = var8.gem(1413186337);
            GemType var64 = GemType.NONE;
            if (var23 != var64) {
                GemType var25 = var9.gem(1413186337);
                GemType var65 = GemType.NONE;
                if (var25 != var65
                    && BlissCoreItems.findGemSlot(var7, 1702349856) >= 0
                    && BlissCoreItems.findGemSlot(var1, 1702349856) >= 0
                    && !this.busy(var7, 6579733)
                    && !this.busy(var1, 6579733)
                    && !JuggernautGemGuardListener.isJuggernaut(var7, 859003337)
                    && !JuggernautGemGuardListener.isJuggernaut(var1, 859003337)) {
                    if (!this.consumeTrader(var7, 882438865)) {
                        UUID var70 = var1.getUniqueId();
                        this.close(var70, var2, 1494764998);
                        Component var72 = Text.legacy("&cThe swap fell through.", 373192400);
                        var1.sendMessage(var72);
                        Component var74 = Text.legacy("&cThe swap fell through — your Trader is gone.", 373192400);
                        var7.sendMessage(var74);
                        return;
                    }

                    UUID var76 = var1.getUniqueId();
                    this.close(var76, var2, 1494764998);
                    GemType var10 = var8.gem(1413186337);
                    GemType var78 = var9.gem(1413186337);
                    var8.setGem(var78, 2137390487);
                    var9.setGem(var10, 2137390487);
                    BlissCoreItems var49 = this.items;
                    var49.refreshHeldGemItem(var7, var8, 835235475);
                    BlissCoreItems var51 = this.items;
                    var51.refreshHeldGemItem(var1, var9, 835235475);
                    GemType var95 = var8.gem(1413186337);
                    this.announce(var7, var95, 1016226267);
                    GemType var97 = var9.gem(1413186337);
                    this.announce(var1, var97, 1016226267);
                    return;
                }
            }

            UUID var85 = var1.getUniqueId();
            this.close(var85, var2, 1494764998);
            Component var87 = Text.legacy("&cThe swap fell through.", 373192400);
            var1.sendMessage(var87);
            Component var89 = Text.legacy("&cThe swap fell through.", 373192400);
            var7.sendMessage(var89);
        }
    }

    private void announce(Player var1, GemType var2, int var3) {
        Location var10 = var1.getLocation();
        Sound var6 = Sound.ENTITY_PLAYER_LEVELUP;
        var1.playSound(var10, var6, 1.0F, 1.0F);
        Component var14 = Text.legacy("\ud83d\udd2e &bYou have traded your current gem to " + var2.coloredName(), 373192400);
        var1.sendMessage(var14);
    }

    private void decline(Player var1, GemSwapListener.Offer var2, int var3) {
        UUID var14 = var1.getUniqueId();
        this.close(var14, var2, 1494764998);
        Component var16 = Text.legacy("\ud83d\udd2e &bSwap offer declined.", 373192400);
        var1.sendMessage(var16);
        Player var7 = Bukkit.getPlayer(var2.from);
        if (var7 != null) {
            Component var20 = Text.legacy("\ud83d\udd2e &e" + var1.getName() + " &bdeclined your swap offer.", 373192400);
            var7.sendMessage(var20);
        }
    }

    private void expire(UUID var1, long var2, int var4) {
        Map var13 = this.offers;
        GemSwapListener.Offer var9 = (GemSwapListener.Offer)var13.get(var1);
        if (var9 != null) {
            long var18 = var9.token;
            if (var18 == var2) {
                Map var22 = this.offers;
                var22.remove(var1);
                Component var10 = Text.legacy("\ud83d\udd2e &bSwap offer has expired!", 373192400);
                Player var11 = Bukkit.getPlayer(var1);
                if (var11 != null) {
                    var11.sendMessage(var10);
                }

                Player var12 = Bukkit.getPlayer(var9.from);
                if (var12 != null) {
                    var12.sendMessage(var10);
                }

                return;
            }
        }
    }

    private void close(UUID var1, GemSwapListener.Offer var2, int var3) {
        Map var6 = this.offers;
        var6.remove(var1);
        if (var2.expiry != null) {
            var2.expiry.cancel();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        UUID var5 = var1.getPlayer().getUniqueId();
        Map var10 = this.lastRequest;
        var10.remove(var5);
        Map var13 = this.offers;
        GemSwapListener.Offer var6 = (GemSwapListener.Offer)var13.get(var5);
        if (var6 != null) {
            this.close(var5, var6, 1494764998);
        }

        Set<Map.Entry<UUID, GemSwapListener.Offer>> var20 = this.offers.entrySet();
        Predicate<Map.Entry<UUID, GemSwapListener.Offer>> var25 = var1x -> {
            UUID var6x = var1x.getValue().from;
            if (!var6x.equals(var5)) {
                return false;
            } else {
                if (var1x.getValue().expiry != null) {
                    var1x.getValue().expiry.cancel();
                }

                return true;
            }
        };
        var20.removeIf(var25);
    }

    private boolean busy(Player var1, int var2) {
        PlayerDataStore var6 = this.players;
        UUID var16 = var1.getUniqueId();
        PlayerData var5 = var6.get(var16, 1264010596);
        TraderListener var9 = this.trader;
        UUID var18 = var1.getUniqueId();
        boolean var15;
        if (!var9.isShuffling(var18, 2122195762) && !var5.tradeLock(1959314746) && !var5.restorationLock(1109120985)) {
            var15 = false;
        } else {
            var15 = true;
        }

        return var15;
    }

    private boolean hasTrader(Player var1, int var2) {
        boolean var6;
        if (this.findTrader(var1, 1351269221) >= 0) {
            var6 = true;
        } else {
            var6 = false;
        }

        return var6;
    }

    private int findTrader(Player var1, int var2) {
        PlayerInventory var6 = var1.getInventory();
        BlissCoreItems var11 = this.items;
        ItemStack var28 = var6.getItemInMainHand();
        if (var11.isTrader(var28, 904484797)) {
            return var6.getHeldItemSlot();
        } else {
            ItemStack[] var7 = var6.getStorageContents();
            int var8 = 0;

            while (true) {
                int var30 = var7.length;
                if (var8 >= var30) {
                    BlissCoreItems var24 = this.items;
                    ItemStack var34 = var6.getItemInOffHand();
                    if (var24.isTrader(var34, 904484797)) {
                        return 40;
                    }

                    return -1;
                }

                BlissCoreItems var20 = this.items;
                ItemStack var32 = var7[var8];
                if (var20.isTrader(var32, 904484797)) {
                    return var8;
                }

                var8++;
            }
        }
    }

    private boolean consumeTrader(Player var1, int var2) {
        int var6 = this.findTrader(var1, 1351269221);
        if (var6 < 0) {
            return false;
        } else {
            GameMode var11 = var1.getGameMode();
            GameMode var25 = GameMode.CREATIVE;
            if (var11 == var25) {
                return true;
            } else {
                PlayerInventory var14 = var1.getInventory();
                ItemStack var7 = var14.getItem(var6);
                if (var7 == null) {
                    return false;
                } else {
                    if (var7.getAmount() <= 1) {
                        PlayerInventory var22 = var1.getInventory();
                        var22.setItem(var6, null);
                    } else {
                        int var30 = var7.getAmount() - 1;
                        var7.setAmount(var30);
                    }

                    return true;
                }
            }
        }
    }

    private static final class Offer {
        final UUID from;
        final long token;
        final BukkitTask expiry;

        Offer(UUID var1, long var2, BukkitTask var4, int var5) {
            this.from = var1;
            this.token = var2;
            this.expiry = var4;
        }
    }
}
