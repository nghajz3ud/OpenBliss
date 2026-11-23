package com.bliss.trader;

import com.bliss.BlissPlugin;
import com.bliss.core.AchievementStore;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

public final class TraderListener implements Listener {
    private static final int EXCLUDE_MIN_ENERGY = 3;
    private static final int TRADER_SHUFFLE_STAGES = 5;
    private static final long TRADER_SHUFFLE_DELAY = 10L;
    private static final int TRADER_DISABLE_SECONDS = 3;
    private final BlissPlugin plugin;
    private final BlissCoreItems items;
    private final PlayerDataStore players;
    private final Random random;
    private static final int AVOID_RECENT = 3;
    private final Map<UUID, Deque<GemType>> recentPicks;
    private final Map<UUID, TraderListener.TradeState> tradeStates;
    private GemSwapListener swaps;
    private final Set<UUID> shuffling;
    private final Set<UUID> pendingRegrant;

    private static List<GemType> buildTradePool(GemType var0, Collection<GemType> var1, Collection<GemType> var2, int var3) {
        ArrayList var4 = new ArrayList();
        ArrayList var5 = var4;

        for (GemType var9 : GemType.values()) {
            GemType var28 = GemType.NONE;
            if (var9 != var28 && var9 != var0 && !var1.contains(var9) && !var2.contains(var9)) {
                var5.add(var9);
            }
        }

        return var5;
    }

    public void setSwaps(GemSwapListener var1, int var2) {
        this.swaps = var1;
    }

    public TraderListener(BlissPlugin var1, PlayerDataStore var2, BlissCoreItems var3, int var4) {
        Random var6 = new Random();
        this.random = var6;
        HashMap var15 = new HashMap();
        this.recentPicks = var15;
        HashMap var16 = new HashMap();
        this.tradeStates = var16;
        HashSet var17 = new HashSet();
        this.shuffling = var17;
        HashSet var18 = new HashSet();
        this.pendingRegrant = var18;
        this.plugin = var1;
        this.items = var3;
        this.players = var2;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent var1) {
        EquipmentSlot var9 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var9 == var3) {
            Player var6 = var1.getPlayer();
            ItemStack var7 = var6.getInventory().getItemInMainHand();
            BlissCoreItems var16 = this.items;
            if (var16.isTrader(var7, 904484797)) {
                if (JuggernautGemGuardListener.isJuggernaut(var6, 859003337)) {
                    var1.setCancelled(true);
                    Component var45 = Text.legacy("&cJuggernauts cannot use the gem trader.", 373192400);
                    var6.sendMessage(var45);
                } else {
                    Action var23 = var1.getAction();
                    Action var46 = Action.LEFT_CLICK_AIR;
                    if (var23 != var46) {
                        Action var25 = var1.getAction();
                        Action var47 = Action.LEFT_CLICK_BLOCK;
                        if (var25 != var47) {
                            Action var27 = var1.getAction();
                            Action var48 = Action.RIGHT_CLICK_AIR;
                            if (var27 != var48) {
                                Action var29 = var1.getAction();
                                Action var49 = Action.RIGHT_CLICK_BLOCK;
                                if (var29 != var49) {
                                    return;
                                }
                            }

                            var1.setCancelled(true);
                            Player var35;
                            if (this.swaps == null) {
                                var35 = null;
                            } else {
                                GemSwapListener var34 = this.swaps;
                                var35 = var34.lookedAtPlayer(var6, 1705873698);
                            }

                            if (var35 != null) {
                                GemSwapListener var39 = this.swaps;
                                var39.request(var6, var35, 1341144368);
                                return;
                            }

                            this.rerollGem(var6, var7, 1934994509);
                            return;
                        }
                    }

                    var1.setCancelled(true);
                    BlissCoreItems var56 = this.items;
                    PlayerDataStore var59 = this.players;
                    UUID var62 = var6.getUniqueId();
                    GemType var61 = var59.get(var62, 1264010596).gem(1413186337);
                    TraderMenu.open(var6, var56, var61, 827475372);
                }
            }
        }
    }

    @EventHandler
    public void onRejoinRegrant(PlayerJoinEvent var1) {
        Player var8 = var1.getPlayer();
        PlayerDataStore var13 = this.players;
        UUID var32 = var8.getUniqueId();
        PlayerData var9 = var13.get(var32, 1264010596);
        var9.setTradeLock(false, 469012538);
        Set var17 = this.shuffling;
        UUID var35 = var8.getUniqueId();
        var17.remove(var35);
        Set var20 = this.pendingRegrant;
        UUID var37 = var8.getUniqueId();
        if (var20.remove(var37)) {
            int var10 = findGemSlot(var8, 514870444);
            clearGemItems(var8, 579055745);
            if (!JuggernautGemGuardListener.isJuggernaut(var8, 859003337)) {
                GemType var29 = var9.gem(1413186337);
                GemType var39 = GemType.NONE;
                if (var29 != var39) {
                    BlissCoreItems var47 = this.items;
                    GemType var49 = var9.gem(1413186337);
                    boolean var50 = var9.tier2(629984660);
                    int var51 = var9.energy(1160853318);
                    ItemStack var48 = var47.gemItem(var49, var50, var51, 256965471);
                    BlissCoreItems.giveGemOrHold(var8, var10, var48, 2133739666);
                    Component var45 = Text.legacy("\ud83d\udd2e &bYou have traded your current gem to " + var9.gem(1413186337).coloredName(), 373192400);
                    var8.sendMessage(var45);
                }
            }
        } else {
            this.reconcileStaleGem(var8, var9, 92070007);
        }
    }

    private void reconcileStaleGem(Player var1, PlayerData var2, int var3) {
        GemType var14 = var2.gem(1413186337);
        GemType var5 = GemType.NONE;
        if (var14 != var5) {
            boolean var7 = false;
            boolean var8 = false;

            for (ItemStack var12 : var1.getInventory().getContents()) {
                if (BlissCoreItems.gemIdOf(var12, 382441079) > 0) {
                    var7 = true;
                    byte var42;
                    if (var2.tier2(629984660)) {
                        var42 = 2;
                    } else {
                        var42 = 1;
                    }

                    int var36 = BlissCoreItems.gemIdOf(var12, 382441079);
                    int var48 = var2.gem(1413186337).id();
                    if (var36 == var48) {
                        int var38 = BlissCoreItems.tierOf(var12, 1633828214);
                        if (var38 == var42 && !isLockedVisual(var12, 2104503351)) {
                            continue;
                        }
                    }

                    var8 = true;
                }
            }

            if (var7 && var8) {
                BlissCoreItems var27 = this.items;
                var27.refreshHeldGemItem(var1, var2, 835235475);
            }
        }
    }

    private static boolean isLockedVisual(ItemStack var0, int var1) {
        ItemMeta var4 = var0.getItemMeta();
        if (var4 == null) {
            return false;
        } else {
            Component var5 = var4.displayName();
            if (var5 == null) {
                return false;
            } else {
                PlainTextComponentSerializer var12 = PlainTextComponentSerializer.plainText();
                return var12.serialize(var5).contains("\ud83d\udd12");
            }
        }
    }

    private void rerollGem(Player var1, ItemStack var2, int var3) {
        UUID var7 = var1.getUniqueId();
        Set var10 = this.shuffling;
        if (!var10.contains(var7)) {
            PlayerDataStore var13 = this.players;
            GemType var15 = var13.get(var7, 1264010596).gem(1413186337);
            GemType var30 = GemType.NONE;
            if (var15 == var30) {
                Component var32 = Text.legacy("&cYou have no gem to trade.", 373192400);
                var1.sendMessage(var32);
            } else {
                PlayerDataStore var18 = this.players;
                if (var18.get(var7, 1264010596).restorationLock(1109120985)) {
                    Component var41 = Text.legacy("&cWait for the ritual to finish.", 373192400);
                    var1.sendMessage(var41);
                } else {
                    Set var22 = this.shuffling;
                    var22.add(var7);
                    GameMode var25 = var1.getGameMode();
                    GameMode var35 = GameMode.CREATIVE;
                    if (var25 != var35) {
                        int var38 = var2.getAmount() - 1;
                        var2.setAmount(var38);
                    }

                    this.beginReroll(var1, 1323479289);
                }
            }
        }
    }

    public boolean isShuffling(UUID var1, int var2) {
        Set var5 = this.shuffling;
        return var5.contains(var1);
    }

    public boolean adminReroll(Player var1, int var2) {
        UUID var5 = var1.getUniqueId();
        PlayerDataStore var8 = this.players;
        GemType var10 = var8.get(var5, 1264010596).gem(1413186337);
        GemType var26 = GemType.NONE;
        if (var10 == var26) {
            return false;
        } else {
            PlayerDataStore var12 = this.players;
            if (var12.get(var5, 1264010596).restorationLock(1109120985)) {
                return false;
            } else {
                Set var17 = this.shuffling;
                if (var17.contains(var5)) {
                    return false;
                } else {
                    Set var20 = this.shuffling;
                    var20.add(var5);
                    this.beginReroll(var1, 1323479289);
                    return true;
                }
            }
        }
    }

    private void beginReroll(Player var1, int var2) {
        UUID var12 = var1.getUniqueId();
        PlayerDataStore var24 = this.players;
        PlayerData var13 = var24.get(var12, 1264010596);
        GemType var14 = var13.gem(1413186337);
        boolean var15 = var13.tier2(629984660);
        Map var31 = this.tradeStates;
        TraderListener.TradeState var16 = (TraderListener.TradeState)var31.get(var12);
        Object var69;
        if (var16 != null) {
            var69 = var16.excluded;
        } else {
            var69 = EnumSet.noneOf(GemType.class);
        }

        if (var16 != null) {
            var16.committed = true;
        }

        Map var39 = this.recentPicks;
        Function var5 = var0 -> new ArrayDeque();
        Deque var18 = (Deque)var39.computeIfAbsent(var12, var5);
        List var19 = buildTradePool(var14, (Collection<GemType>)var69, var18, 1609796768);
        if (var19.isEmpty()) {
            List var86 = List.of();
            var19 = buildTradePool(var14, (Collection<GemType>)var69, var86, 1609796768);
        }

        if (var19.isEmpty()) {
            List var75 = List.of();
            List var87 = List.of();
            var19 = buildTradePool(var14, var75, var87, 1609796768);
        }

        Random var77 = this.random;
        int var89 = var19.size();
        int var78 = var77.nextInt(var89);
        GemType var20 = (GemType)var19.get(var78);
        var18.addLast(var20);

        while (var18.size() > 3) {
            var18.removeFirst();
        }

        Map var59 = this.tradeStates;
        var59.remove(var12);
        int var21 = findGemSlot(var1, 514870444);
        var13.setTradeLock(true, 469012538);
        var13.addTagSeconds(3, 1722133252);
        this.runTraderShuffle(var1, var13, var15, var14, var20, null, var21, 0, 1166225244);
    }

    private static int findGemSlot(Player var0, int var1) {
        return BlissCoreItems.findGemSlot(var0, 1702349856);
    }

    private static void giveGemAt(Player var0, int var1, ItemStack var2, int var3) {
        if (var1 >= 0) {
            PlayerInventory var8 = var0.getInventory();
            var8.setItem(var1, var2);
        } else {
            BlissCoreItems.giveGemOrHold(var0, -1, var2, 2133739666);
        }
    }

    private void runTraderShuffle(Player var1, PlayerData var2, boolean var3, GemType var4, GemType var5, GemType var6, int var7, int var8, int var9) {
        UUID var21 = var1.getUniqueId();
        Player var22 = Bukkit.getPlayer(var21);
        if (var22 == null) {
            var2.setGem(var5, 2137390487);
            var2.setTradeLock(false, 469012538);
            Set var32 = this.shuffling;
            var32.remove(var21);
            Set var35 = this.pendingRegrant;
            var35.add(var21);
        } else if (var22 != var1) {
            var2.setGem(var5, 2137390487);
            var2.setTradeLock(false, 469012538);
            Set var71 = this.shuffling;
            var71.remove(var21);
            int var158 = findGemSlot(var22, 514870444);
            clearGemItems(var22, 579055745);
            BlissCoreItems var131 = this.items;
            int var157 = var2.energy(1160853318);
            ItemStack var132 = var131.gemItem(var5, var3, var157, 256965471);
            BlissCoreItems.giveGemOrHold(var22, var158, var132, 2133739666);
            Location var112 = var22.getLocation();
            Sound var133 = Sound.ENTITY_PLAYER_LEVELUP;
            var22.playSound(var112, var133, 1.0F, 1.0F);
            Component var116 = Text.legacy("\ud83d\udd2e &bYou have traded your current gem to " + var5.coloredName(), 373192400);
            var22.sendMessage(var116);
        } else {
            clearGemItems(var1, 579055745);
            if (var8 >= 5) {
                Set var41 = this.shuffling;
                UUID var85 = var1.getUniqueId();
                var41.remove(var85);
                var2.setGem(var5, 2137390487);
                var2.setTradeLock(false, 469012538);
                BlissCoreItems var117 = this.items;
                int var150 = var2.energy(1160853318);
                ItemStack var118 = var117.gemItem(var5, var3, var150, 256965471);
                giveGemAt(var1, var7, var118, 1764141803);
                Location var90 = var1.getLocation();
                Sound var119 = Sound.ENTITY_PLAYER_LEVELUP;
                var1.playSound(var90, var119, 1.0F, 1.0F);
                Component var94 = Text.legacy("\ud83d\udd2e &bYou have traded your current gem to " + var5.coloredName(), 373192400);
                var1.sendMessage(var94);
                AchievementStore var50 = this.plugin.advancements(1975441698);
                AchievementStore.Advancement var120 = AchievementStore.Advancement.TIME_FOR_A_CHANGE;
                var50.unlock(var1, var120, 1132982324);
            } else {
                GemType var23;
                do {
                    Random var97 = this.random;
                    var23 = GemType.randomDifferent(var4, var97);
                } while (var23 == var6 || var8 == 4 && var23 == var5);

                if (var7 >= 0) {
                    PlayerInventory var58 = var1.getInventory();
                    BlissCoreItems var122 = this.items;
                    int var152 = var2.energy(1160853318);
                    ItemStack var123 = var122.gemItem(var23, var3, var152, 256965471);
                    var58.setItem(var7, var123);
                } else {
                    BlissCoreItems var128 = this.items;
                    int var155 = var2.energy(1160853318);
                    ItemStack var129 = var128.gemItem(var23, var3, var155, 256965471);
                    BlissCoreItems.placeGem(var1, -1, var129, 2132502548);
                }

                Location var103 = var1.getLocation();
                Sound var124 = Sound.BLOCK_AMETHYST_BLOCK_BREAK;
                var1.playSound(var103, var124, 1.0F, 1.0F);
                BukkitScheduler var64 = this.plugin.getServer().getScheduler();
                BlissPlugin var105 = this.plugin;
                final GemType var23Final = var23;
                Runnable var126 = () -> {
                    int var19 = var8 + 1;
                    this.runTraderShuffle(var1, var2, var3, var4, var5, var23Final, var7, var19, 1166225244);
                };
                var64.runTaskLater(var105, var126, 10L);
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onShuffleClick(InventoryClickEvent var1) {
        if (var1.getWhoClicked() instanceof Player var4) {
            Set var12 = this.shuffling;
            UUID var15 = var4.getUniqueId();
            if (var12.contains(var15)) {
                var1.setCancelled(true);
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onShuffleDrag(InventoryDragEvent var1) {
        if (var1.getWhoClicked() instanceof Player var4) {
            Set var12 = this.shuffling;
            UUID var15 = var4.getUniqueId();
            if (var12.contains(var15)) {
                var1.setCancelled(true);
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onShuffleSwapHands(PlayerSwapHandItemsEvent var1) {
        Set var4 = this.shuffling;
        UUID var8 = var1.getPlayer().getUniqueId();
        if (var4.contains(var8)) {
            var1.setCancelled(true);
        }
    }

    public static void clearGemItems(Player var0, int var1) {
        ItemStack[] var5 = var0.getInventory().getContents();
        boolean var6 = false;
        int var7 = 0;

        while (true) {
            int var44 = var5.length;
            if (var7 >= var44) {
                if (var6) {
                    PlayerInventory var23 = var0.getInventory();
                    var23.setContents(var5);
                }

                if (BlissCoreItems.gemIdOf(var0.getOpenInventory().getCursor(), 382441079) > 0) {
                    var0.getOpenInventory().setCursor(null);
                }

                Inventory var8 = var0.getOpenInventory().getTopInventory();
                InventoryType var9 = var8.getType();
                InventoryType var49 = InventoryType.CRAFTING;
                if (var9 != var49) {
                    InventoryType var50 = InventoryType.WORKBENCH;
                    if (var9 != var50) {
                        return;
                    }
                }

                int var10 = 0;

                while (true) {
                    int var52 = var8.getSize();
                    if (var10 >= var52) {
                        return;
                    }

                    if (BlissCoreItems.gemIdOf(var8.getItem(var10), 382441079) > 0) {
                        var8.setItem(var10, null);
                    }

                    var10++;
                }
            }

            if (BlissCoreItems.gemIdOf(var5[var7], 382441079) > 0) {
                var5[var7] = null;
                var6 = true;
            }

            var7++;
        }
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent var1) {
        if (var1.getView().getTopInventory().getHolder() instanceof TraderMenu.Holder) {
            var1.setCancelled(true);
            int var18 = var1.getRawSlot();
            int var68 = var1.getView().getTopInventory().getSize();
            if (var18 < var68) {
                if (var1.getWhoClicked() instanceof Player var7) {
                    int var116 = var1.getRawSlot();
                    UUID var9 = var7.getUniqueId();
                    if (var116 == 13) {
                        this.commitTradeFromMenu(var7, 967793921);
                    } else {
                        Map var30 = TraderMenu.SLOTS;
                        Integer var71 = var116;
                        GemType var10 = (GemType)var30.get(var71);
                        if (var10 != null) {
                            PlayerDataStore var73 = this.players;
                            GemType var75 = var73.get(var9, 1264010596).gem(1413186337);
                            if (var10 == var75) {
                                Location var97 = var7.getLocation();
                                Sound var108 = Sound.BLOCK_NOTE_BLOCK_HAT;
                                var7.playSound(var97, var108, 1.0F, 0.6F);
                            } else {
                                Map var36 = this.tradeStates;
                                Function var99 = var0 -> new TraderListener.TradeState();
                                TraderListener.TradeState var11 = (TraderListener.TradeState)var36.computeIfAbsent(var9, var99);
                                Set var40 = var11.excluded;
                                if (var40.contains(var10)) {
                                    if (!this.refundOneExclusion(var7, var11, 1300627359)) {
                                        return;
                                    }

                                    Set var45 = var11.excluded;
                                    var45.remove(var10);
                                    Location var81 = var7.getLocation();
                                    Sound var101 = Sound.BLOCK_NOTE_BLOCK_HAT;
                                    var7.playSound(var81, var101, 1.0F, 1.4F);
                                } else {
                                    PlayerDataStore var51 = this.players;
                                    GemType var53 = var51.get(var9, 1264010596).gem(1413186337);
                                    Set var86 = var11.excluded;
                                    if (TraderMenu.poolSize(var53, var86, 739645699) <= 1) {
                                        Component var95 = Text.legacy("&cYou can't exclude every gem — leave at least one in the pool.", 373192400);
                                        var7.sendMessage(var95);
                                        return;
                                    }

                                    if (!this.spendOneExclusion(var7, var11, 859098812)) {
                                        Component var93 = Text.legacy("&cNo Bottled Energy and gem energy too low to exclude a gem.", 373192400);
                                        var7.sendMessage(var93);
                                        return;
                                    }

                                    Set var58 = var11.excluded;
                                    var58.add(var10);
                                    Location var91 = var7.getLocation();
                                    Sound var107 = Sound.BLOCK_NOTE_BLOCK_BASS;
                                    var7.playSound(var91, var107, 1.0F, 0.8F);
                                }

                                Inventory var49 = var1.getInventory();
                                BlissCoreItems var83 = this.items;
                                PlayerDataStore var103 = this.players;
                                GemType var105 = var103.get(var9, 1264010596).gem(1413186337);
                                Set var111 = var11.excluded;
                                TraderMenu.render(var49, var83, var105, var111, 17054106);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean spendOneExclusion(Player var1, TraderListener.TradeState var2, int var3) {
        if (this.consumeOneBottle(var1, var2, 1298548970)) {
            int var27 = var2.bottlesSpent + 1;
            var2.bottlesSpent = var27;
            return true;
        } else {
            PlayerDataStore var12 = this.players;
            UUID var29 = var1.getUniqueId();
            PlayerData var7 = var12.get(var29, 1264010596);
            if (GlobalState.forPlayer(var1, 1433087673).noEnergyMode(340448998)) {
                return false;
            } else if (var7.energy(1160853318) < 3) {
                return false;
            } else {
                var7.addEnergy(-1, 514979476);
                BlissCoreItems var22 = this.items;
                var22.refreshHeldGemItem(var1, var7, 835235475);
                int var34 = var2.gemEnergySpent + 1;
                var2.gemEnergySpent = var34;
                return true;
            }
        }
    }

    private boolean refundOneExclusion(Player var1, TraderListener.TradeState var2, int var3) {
        if (var2.bottlesSpent > 0) {
            this.giveBottlesOrDrop(var1, 1, var2, 287502996);
            int var27 = var2.bottlesSpent - 1;
            var2.bottlesSpent = var27;
            return true;
        } else if (var2.gemEnergySpent > 0) {
            PlayerDataStore var16 = this.players;
            UUID var29 = var1.getUniqueId();
            PlayerData var8 = var16.get(var29, 1264010596);
            if (var8.energy(1160853318) < 11) {
                var8.addEnergy(1, 514979476);
                BlissCoreItems var22 = this.items;
                var22.refreshHeldGemItem(var1, var8, 835235475);
            }

            int var34 = var2.gemEnergySpent - 1;
            var2.gemEnergySpent = var34;
            return true;
        } else {
            return false;
        }
    }

    private boolean consumeOneBottle(Player var1, TraderListener.TradeState var2, int var3) {
        PlayerInventory var8 = var1.getInventory();
        ItemStack[] var9 = var8.getContents();
        int var10 = 0;

        ItemStack var11;
        Long var12;
        while (true) {
            int var49 = var9.length;
            if (var10 >= var49) {
                return false;
            }

            var11 = var9[var10];
            if (var11 != null) {
                BlissCoreItems var23 = this.items;
                if (var23.isBottledEnergy(var11, 1793320558)) {
                    var12 = BlissCoreItems.energyExpiryOf(var11, 374126995);
                    if (this.plugin.globalState(724248306) == null || !this.plugin.globalState(724248306).bottledEnergyExpiry(261106370) || var12 == null) {
                        break;
                    }

                    long var36 = var12;
                    long var6 = System.currentTimeMillis();
                    if (var36 > var6) {
                        break;
                    }

                    BlissCoreItems var40 = this.items;
                    var40.makeUseless(var11, 607150677);
                    var8.setItem(var10, var11);
                }
            }

            var10++;
        }

        ArrayDeque var43 = var2.bottleExpiries;
        long var62;
        if (var12 == null) {
            var62 = -1L;
        } else {
            var62 = var12;
        }

        Long var55 = var62;
        var43.push(var55);
        if (var11.getAmount() <= 1) {
            var8.setItem(var10, null);
        } else {
            int var59 = var11.getAmount() - 1;
            var11.setAmount(var59);
        }

        return true;
    }

    private void giveBottlesOrDrop(Player var1, int var2, TraderListener.TradeState var3, int var4) {
        PlayerInventory var11 = var1.getInventory();

        for (int var12 = 0; var12 < var2; var12++) {
            Long var13 = var3.bottleExpiries.poll();
            ItemStack var47;
            if (var13 != null && var13 > 0L) {
                BlissCoreItems var31 = this.items;
                long var49 = var13;
                var47 = var31.bottledEnergy(var49, 1993701379);
            } else {
                var47 = this.items.bottledEnergy(1532783840);
            }

            ItemStack[] var52 = new ItemStack[]{var47};

            for (ItemStack var17 : var11.addItem(var52).values()) {
                World var43 = var1.getWorld();
                Location var54 = var1.getLocation();
                var43.dropItemNaturally(var54, var17);
            }
        }
    }

    private void commitTradeFromMenu(Player var1, int var2) {
        ItemStack var8 = var1.getInventory().getItemInMainHand();
        ItemStack var9 = var1.getInventory().getItemInOffHand();
        BlissCoreItems var16 = this.items;
        ItemStack var35;
        if (var16.isTrader(var8, 904484797)) {
            var35 = var8;
        } else {
            BlissCoreItems var25 = this.items;
            if (!var25.isTrader(var9, 904484797)) {
                Component var33 = Text.legacy("&cTrader item not found in your hand.", 373192400);
                var1.sendMessage(var33);
                return;
            }

            var35 = var9;
        }

        BukkitScheduler var22 = this.plugin.getServer().getScheduler();
        BlissPlugin var30 = this.plugin;
        ItemStack var7 = var35;
        Runnable var34 = () -> {
            this.rerollGem(var1, var7, 1934994509);
            var1.closeInventory();
        };
        var22.runTask(var30, var34);
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent var1) {
        if (var1.getInventory().getHolder() instanceof TraderMenu.Holder) {
            if (var1.getPlayer() instanceof Player var6) {
                UUID var80 = var6.getUniqueId();
                Map var24 = this.tradeStates;
                TraderListener.TradeState var8 = (TraderListener.TradeState)var24.remove(var80);
                if (var8 != null && !var8.committed) {
                    int var9 = var8.bottlesSpent;
                    int var10 = var8.gemEnergySpent;
                    if (var9 > 0) {
                        this.giveBottlesOrDrop(var6, var9, var8, 287502996);
                    }

                    if (var10 > 0) {
                        PlayerDataStore var38 = this.players;
                        PlayerData var11 = var38.get(var80, 1264010596);
                        var11.addEnergy(var10, 514979476);
                        BlissCoreItems var42 = this.items;
                        var42.refreshHeldGemItem(var6, var11, 835235475);
                    }

                    if (var9 > 0 || var10 > 0) {
                        String var78 = "&7Refunded ";
                        StringBuilder var45 = new StringBuilder(var78);
                        if (var9 > 0) {
                            StringBuilder var49 = var45.append(var9).append(" bottle");
                            String var76;
                            if (var9 == 1) {
                                var76 = "";
                            } else {
                                var76 = "s";
                            }

                            var49.append(var76);
                        }

                        if (var9 > 0 && var10 > 0) {
                            var45.append(" and ");
                        }

                        if (var10 > 0) {
                            var45.append(var10).append(" energy");
                        }

                        var45.append(" from your unfinished trade.");
                        Component var75 = Text.legacy(var45.toString(), 373192400);
                        var6.sendMessage(var75);
                    }
                }
            }
        }
    }

    private static final class TradeState {
        final Set<GemType> excluded;
        int bottlesSpent;
        int gemEnergySpent;
        final ArrayDeque<Long> bottleExpiries;
        boolean committed;

        private TradeState() {
            EnumSet var8 = EnumSet.noneOf(GemType.class);
            this.excluded = var8;
            this.bottlesSpent = 0;
            this.gemEnergySpent = 0;
            ArrayDeque var11 = new ArrayDeque();
            this.bottleExpiries = var11;
            this.committed = false;
        }
    }
}
