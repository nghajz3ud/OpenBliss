package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.goldgem.GoldenDreamWorld;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.function.Function;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class WattDepositListener implements Listener {
    public static final int SIZE = 27;
    private static final long BURN_PERIOD = 20L;
    private static final Set<UUID> CHARGING = ConcurrentHashMap.newKeySet();
    private final BlissPlugin plugin;
    private final FluxGemListener flux;
    private final BlissCoreItems items;
    private final File file;
    private final YamlConfiguration cfg;
    private final Map<UUID, Deque<ItemStack>> queues;
    private final Map<UUID, BukkitTask> burners;
    private static final Map<UUID, Integer> OPENED;
    private static final int OPEN_GRACE = 2;
    private final Set<UUID> autoStart;

    public WattDepositListener(BlissPlugin var1, FluxGemListener var2, BlissCoreItems var3, int var4) {
        HashMap var6 = new HashMap();
        this.queues = var6;
        HashMap var22 = new HashMap();
        this.burners = var22;
        KeySetView var23 = ConcurrentHashMap.newKeySet();
        this.autoStart = var23;
        this.plugin = var1;
        this.flux = var2;
        this.items = var3;
        File var34 = var1.getDataFolder();
        File var27 = new File(var34, "wattdeposit.yml");
        this.file = var27;
        YamlConfiguration var30 = YamlConfiguration.loadConfiguration(this.file);
        this.cfg = var30;
        this.load(62388808);
        BukkitScheduler var20 = var1.getServer().getScheduler();
        Runnable var33 = () -> {
            for (Player var5 : var1.getServer().getOnlinePlayers()) {
                Map var16 = this.queues;
                UUID var23x = var5.getUniqueId();
                Deque var6x = (Deque)var16.get(var23x);
                if (var6x != null && !var6x.isEmpty()) {
                    UUID var25 = var5.getUniqueId();
                    this.startBurner(var25, 780353770);
                }
            }
        };
        var20.runTask(var1, var33);
    }

    @EventHandler(
        priority = EventPriority.LOW
    )
    public void onDrop(PlayerDropItemEvent var1) {
        if (opensDeposit(var1.getItemDrop().getItemStack(), 840965284)) {
            Player var7 = var1.getPlayer();
            if (GlobalState.forPlayer(var7, 1433087673).wattDeposit(552206927)) {
                var1.setCancelled(true);
                Map var18 = OPENED;
                UUID var26 = var7.getUniqueId();
                Integer var32 = this.plugin.getServer().getCurrentTick();
                var18.put(var26, var32);
                BukkitScheduler var23 = this.plugin.getServer().getScheduler();
                BlissPlugin var28 = this.plugin;
                Runnable var34 = () -> {
                    if (var7.isOnline() && !var7.isDead()) {
                        this.open(var7, 1355488593);
                    }
                };
                var23.runTask(var28, var34);
            }
        }
    }

    public static boolean justOpened(UUID var0, int var1) {
        Map var2 = OPENED;
        Integer var4 = (Integer)var2.get(var0);
        if (var4 == null) {
            return false;
        } else {
            int var5 = Bukkit.getCurrentTick();
            int var19 = var4;
            if (var5 - var19 <= 2) {
                int var22 = var4;
                if (var5 >= var22) {
                    return true;
                }
            }

            Map var15 = OPENED;
            var15.remove(var0);
            return false;
        }
    }

    private static boolean opensDeposit(ItemStack var0, int var1) {
        int var4 = BlissCoreItems.gemIdOf(var0, 382441079);
        int var8 = GemType.FLUX.id();
        boolean var7;
        if (var4 == var8 && BlissCoreItems.tierOf(var0, 1633828214) == 2) {
            var7 = true;
        } else {
            var7 = false;
        }

        return var7;
    }

    public void open(Player var1, int var2) {
        Map<UUID, Long> var14 = this.flux.fluxCharge;
        UUID var26 = var1.getUniqueId();
        Long var32 = 0L;
        long var9 = var14.getOrDefault(var26, var32);
        WattDepositListener.Holder var19 = new WattDepositListener.Holder();
        Component var36 = Text.legacy("&9Watt Deposit (" + var9 + " watts)", 373192400);
        Inventory var12 = Bukkit.createInventory(var19, 27, var36);
        var19.set(var12, 1702015339);
        var1.openInventory(var12);
        Location var31 = var1.getLocation();
        Sound var37 = Sound.BLOCK_BARREL_OPEN;
        var1.playSound(var31, var37, 0.7F, 1.4F);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent var1) {
        if (var1.getInventory().getHolder() instanceof WattDepositListener.Holder) {
            if (var1.getPlayer() instanceof Player var5) {
                Inventory var17 = var1.getInventory();
                this.drain(var5, var17, 1677491378);
            }
        }
    }

    private void drain(Player var1, Inventory var2, int var3) {
        this.drain(var1, var2, true, 1847829152);
    }

    private void drain(Player var1, Inventory var2, boolean var3, int var4) {
        boolean var9 = var1.isDead();
        Map var16 = this.queues;
        UUID var53 = var1.getUniqueId();
        Function var7 = var0 -> new ArrayDeque();
        Deque var10 = (Deque)var16.computeIfAbsent(var53, var7);
        int var11 = 0;
        int var12 = 0;

        while (true) {
            int var55 = var2.getSize();
            if (var12 >= var55) {
                if (var11 > 0) {
                    String var75;
                    if (var11 == 1) {
                        var75 = "";
                    } else {
                        var75 = "s";
                    }

                    Component var58 = Text.legacy("&7" + var11 + " item" + var75 + " that won't burn came back to you.", 373192400);
                    var1.sendMessage(var58);
                }

                if (var10.isEmpty()) {
                    Map var31 = this.queues;
                    UUID var63 = var1.getUniqueId();
                    var31.remove(var63);
                    this.save(1799310350);
                    if (var3) {
                        this.beginCharging(var1, 1479861039);
                    }

                    return;
                }

                this.save(1799310350);
                if (var3) {
                    UUID var60 = var1.getUniqueId();
                    this.startBurner(var60, 780353770);
                    this.beginCharging(var1, 1479861039);
                }

                return;
            }

            ItemStack var13 = var2.getItem(var12);
            if (var13 != null && !var13.getType().isAir()) {
                var2.setItem(var12, null);
                BlissCoreItems var68 = this.items;
                if (FluxRechargeListener.wattsFor(var13, var68, 329075510) > 0L) {
                    var10.addLast(var13);
                } else if (var9) {
                    var10.addLast(var13);
                } else {
                    this.giveBack(var1, var13, 1625976503);
                    int var73 = var13.getAmount();
                    var11 += var73;
                }
            }

            var12++;
        }
    }

    private void beginCharging(Player var1, int var2) {
        UUID var7 = var1.getUniqueId();
        FluxGemListener var10 = this.flux;
        if (var10.chargePercent(var7, 1875673459) <= 0.0) {
            Set var27 = this.autoStart;
            var27.add(var7);
        } else {
            Set var15 = this.autoStart;
            var15.remove(var7);
            Map var19 = this.flux.fluxCharging;
            Boolean var36 = true;
            var19.put(var7, var36);
            Map var23 = this.flux.fluxOvercharging;
            Boolean var38 = false;
            var23.put(var7, var38);
            Component var33 = Text.legacy("&aCharging started!", 373192400);
            var1.sendMessage(var33);
        }
    }

    private void giveBack(Player var1, ItemStack var2, int var3) {
        PlayerInventory var10 = var1.getInventory();
        ItemStack[] var22 = new ItemStack[]{var2};

        for (ItemStack var9 : var10.addItem(var22).values()) {
            World var20 = var1.getWorld();
            Location var24 = var1.getLocation();
            var20.dropItemNaturally(var24, var9);
        }
    }

    private void startBurner(UUID var1, int var2) {
        Map var11 = this.burners;
        if (!var11.containsKey(var1)) {
            Set var13 = CHARGING;
            var13.add(var1);
            Map var16 = this.burners;
            BukkitScheduler var22 = this.plugin.getServer().getScheduler();
            BlissPlugin var24 = this.plugin;
            Runnable var25 = () -> this.burn(var1, 249969823);
            BukkitTask var23 = var22.runTaskTimer(var24, var25, 20L, 20L);
            var16.put(var1, var23);
        }
    }

    private void stopBurner(UUID var1, int var2) {
        Map var6 = this.burners;
        BukkitTask var5 = (BukkitTask)var6.remove(var1);
        if (var5 != null) {
            var5.cancel();
        }

        Set var11 = CHARGING;
        var11.remove(var1);
        Set var14 = this.autoStart;
        var14.remove(var1);
    }

    private void burn(UUID var1, int var2) {
        Player var10 = Bukkit.getPlayer(var1);
        Map var26 = this.queues;
        Deque var11 = (Deque)var26.get(var1);
        if (var11 == null || var11.isEmpty()) {
            Map var111 = this.queues;
            var111.remove(var1);
            this.stopBurner(var1, 1965953645);
            this.save(1799310350);
        } else if (var10 == null) {
            this.stopBurner(var1, 1965953645);
        } else if (!var10.isDead()) {
            Map<UUID, Long> var38 = this.flux.fluxCharge;
            Long var144 = 0L;
            long var12 = var38.getOrDefault(var1, var144);
            if (var12 >= 2000000L) {
                this.stopBurner(var1, 1965953645);
                this.save(1799310350);
                Component var119 = Text.legacy("\ud83d\udd2e &bThe gem is full — the rest of your deposit is held until it has room.", 373192400);
                var10.sendMessage(var119);
            } else {
                ItemStack var14 = (ItemStack)var11.peekFirst();
                BlissCoreItems var121 = this.items;
                long var15 = FluxRechargeListener.wattsFor(var14, var121, 329075510);
                if (var15 <= 0L) {
                    var11.pollFirst();
                    this.giveBack(var10, var14, 1625976503);
                    this.save(1799310350);
                } else if (!GoldenDreamWorld.isDreamWorld(var10.getWorld())) {
                    if (var14.getAmount() <= 1) {
                        var11.pollFirst();
                    } else {
                        int var141 = var14.getAmount() - 1;
                        var14.setAmount(var141);
                    }

                    long var152 = 2000000L - var12;
                    long var17 = Math.min(var15, var152);
                    long var19 = var12 + var17;
                    Map var79 = this.flux.fluxCharge;
                    Long var158 = var19;
                    var79.put(var1, var158);
                    Map var83 = this.flux.fluxChargeHidden;
                    Long var161 = var19;
                    var83.put(var1, var161);
                    FluxGemListener var86 = this.flux;
                    var86.recomputeChargePercent(var1, 1834179235);
                    int var21 = 0;

                    for (ItemStack var23 : (Deque<ItemStack>)var11) {
                        int var128 = var23.getAmount();
                        var21 += var128;
                    }

                    FluxGemListener var164 = this.flux;
                    String var167 = formatPercent(var164.chargePercent(var1, 1875673459), 1440495250);
                    Component var132 = Text.legacy("&f\ud83d\udd2e You now have " + var19 + " watts, up to " + var167 + "% charge.", 373192400);
                    var10.sendMessage(var132);
                    Location var134 = var10.getLocation();
                    Sound var162 = Sound.BLOCK_AMETHYST_BLOCK_CHIME;
                    var10.playSound(var134, var162, 0.4F, 1.6F);
                    Set var100 = this.autoStart;
                    if (var100.contains(var1)) {
                        this.beginCharging(var10, 1479861039);
                    }

                    if (var21 == 0) {
                        Map var105 = this.queues;
                        var105.remove(var1);
                        this.stopBurner(var1, 1965953645);
                    }

                    this.save(1799310350);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        UUID var6 = var1.getPlayer().getUniqueId();
        this.stopBurner(var6, 1965953645);
        this.save(1799310350);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        UUID var4 = var1.getPlayer().getUniqueId();
        Map var9 = this.queues;
        Deque var5 = (Deque)var9.get(var4);
        if (var5 != null && !var5.isEmpty()) {
            this.startBurner(var4, 780353770);
        }
    }

    public void shutdown(int var1) {
        for (Player var51 : this.plugin.getServer().getOnlinePlayers()) {
            Inventory var8 = var51.getOpenInventory().getTopInventory();
            if (var8.getHolder() instanceof WattDepositListener.Holder) {
                try {
                    this.drain(var51, var8, false, 1847829152);
                    var51.closeInventory();
                } catch (RuntimeException var52) {
                    Logger var44 = this.plugin.getLogger();
                    String var46 = var51.getName();
                    String var49 = var52.getMessage();
                    String var47 = "Watt Deposit: could not empty " + var46 + "'s window on shutdown (" + var49 + ")";
                    var44.warning(var47);
                }
            }
        }

        Iterator var50 = this.burners.values().iterator();

        while (var50.hasNext()) {
            ((BukkitTask)var50.next()).cancel();
        }

        this.burners.clear();
        CHARGING.clear();
        this.save(1799310350);
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    private void load(int var1) {
        if (this.cfg.isConfigurationSection("pending")) {
            for (String var6 : this.cfg.getConfigurationSection("pending").getKeys(false)) {
                UUID var7;
                try {
                    var7 = UUID.fromString(var6);
                } catch (IllegalArgumentException var76) {
                    continue;
                }

                YamlConfiguration var30 = this.cfg;
                String var64 = "pending." + var6;
                String var8 = var30.getString(var64);
                if (var8 != null && !var8.isBlank()) {
                    Exception var10000;
                    label87: {
                        ArrayDeque var10;
                        ItemStack[] var11;
                        int var12;
                        int var13;
                        try {
                            Decoder var35 = Base64.getDecoder();
                            ItemStack[] var9 = ItemStack.deserializeItemsFromBytes(var35.decode(var8));
                            ArrayDeque var38 = new ArrayDeque();
                            var10 = var38;
                            var11 = var9;
                            var12 = var9.length;
                            var13 = 0;
                        } catch (Exception var79) {
                            var10000 = var79;
                            boolean var10001 = false;
                            break label87;
                        }

                        while (true) {
                            try {
                                if (var13 >= var12) {
                                    break;
                                }

                                ItemStack var14 = var11[var13];
                                if (var14 != null && !var14.getType().isAir()) {
                                    var10.addLast(var14);
                                }
                            } catch (Exception var80) {
                                var10000 = var80;
                                boolean var81 = false;
                                break label87;
                            }

                            try {
                                var13++;
                            } catch (Exception var78) {
                                var10000 = var78;
                                boolean var82 = false;
                                break label87;
                            }
                        }

                        try {
                            if (!var10.isEmpty()) {
                                Map var47 = this.queues;
                                var47.put(var7, var10);
                            }
                            continue;
                        } catch (Exception var77) {
                            var10000 = var77;
                            boolean var83 = false;
                        }
                    }

                    Exception var75 = var10000;
                    Logger var52 = this.plugin.getLogger();
                    String var73 = var75.getMessage();
                    String var69 = "wattdeposit.yml: could not read " + var6 + " (" + var73 + ")";
                    var52.warning(var69);
                }
            }
        }
    }

    private static String formatPercent(double var0, int var2) {
        double var17 = Math.floor(var0);
        String var13;
        if (var0 == var17) {
            String var12 = "%.1f";
            Object[] var16 = new Object[1];
            Double var19 = var0;
            var16[0] = var19;
            var13 = String.format(var12, var16);
        } else {
            var13 = String.valueOf(var0);
        }

        return var13;
    }

    private void save(int var1) {
        YamlConfiguration var10 = this.cfg;
        String var3 = "pending";
        var10.set(var3, null);

        for (Entry var8 : this.queues.entrySet()) {
            if (!((Deque)var8.getValue()).isEmpty()) {
                Collection<ItemStack> var44 = (Collection<ItemStack>)var8.getValue();
                ArrayList<ItemStack> var30 = new ArrayList<>(var44);
                YamlConfiguration var32 = this.cfg;
                String var41 = "pending." + var8.getKey();
                Encoder var45 = Base64.getEncoder();
                ItemStack[] var50 = new ItemStack[0];
                byte[] var49 = ItemStack.serializeItemsAsBytes(var30.toArray(var50));
                String var46 = var45.encodeToString(var49);
                var32.set(var41, var46);
            }
        }

        try {
            YamlConfiguration var18 = this.cfg;
            File var34 = this.file;
            AtomicYml.save(var18, var34, 1347473142);
        } catch (IOException var52) {
            Logger var22 = this.plugin.getLogger();
            String var37 = "wattdeposit.yml: " + var52.getMessage();
            var22.warning(var37);
        }
    }

    static {
        ConcurrentHashMap var1 = new ConcurrentHashMap();
        OPENED = var1;
    }

    public static final class Holder implements InventoryHolder {
        private Inventory inv;

        public Inventory getInventory() {
            return this.inv;
        }

        void set(Inventory var1, int var2) {
            this.inv = var1;
        }
    }
}
