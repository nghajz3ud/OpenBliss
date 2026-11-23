package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

public final class WealthPocketsListener implements Listener {
    private static final int POCKETS_SIZE = 9;
    private static final String POCKETS_TITLE = "Pockets";
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final File file;
    private final Random random;
    private final Map<UUID, ItemStack[]> store;
    private final Set<UUID> doubleClickLatch;
    private final Map<UUID, Integer> lastInteractTick;
    private final Set<UUID> openHolders;

    public WealthPocketsListener(BlissPlugin var1, PlayerDataStore var2, int var3) {
        Random var5 = new Random();
        this.random = var5;
        ConcurrentHashMap var16 = new ConcurrentHashMap();
        this.store = var16;
        KeySetView var17 = ConcurrentHashMap.newKeySet();
        this.doubleClickLatch = var17;
        ConcurrentHashMap var18 = new ConcurrentHashMap();
        this.lastInteractTick = var18;
        KeySetView var19 = ConcurrentHashMap.newKeySet();
        this.openHolders = var19;
        this.plugin = var1;
        this.players = var2;
        File var23 = var1.getDataFolder();
        File var22 = new File(var23, "pockets.yml");
        this.file = var22;
    }

    public void load(int var1) {
        if (this.file.exists()) {
            ConfigurationSection var8 = YamlConfiguration.loadConfiguration(this.file).getConfigurationSection("pockets");
            if (var8 != null) {
                for (String var10 : var8.getKeys(false)) {
                    UUID var11;
                    try {
                        var11 = UUID.fromString(var10);
                    } catch (IllegalArgumentException var48) {
                        continue;
                    }

                    ItemStack[] var12 = new ItemStack[9];

                    for (int var13 = 0; var13 < 9; var13++) {
                        String var47 = var10 + "." + var13;
                        ItemStack var46 = var8.getItemStack(var47);
                        var12[var13] = var46;
                    }

                    Map var37 = this.store;
                    var37.put(var11, var12);
                }
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var5 = var2;

        for (Entry var7 : this.store.entrySet()) {
            ItemStack[] var8 = (ItemStack[])var7.getValue();
            if (var8 != null) {
                for (int var9 = 0; var9 < 9; var9++) {
                    String var47 = String.valueOf(var7.getKey());
                    String var48 = "pockets." + var47 + "." + var9;
                    ItemStack var50 = var8[var9];
                    var5.set(var48, var50);
                }
            }
        }

        try {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
            File var40 = this.file;
            var5.save(var40);
        } catch (IOException var51) {
            Logger var28 = this.plugin.getLogger();
            String var43 = "Failed to save pockets.yml: " + var51.getMessage();
            var28.warning(var43);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        Action var14 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var14 != var3) {
            Action var16 = var1.getAction();
            Action var74 = Action.RIGHT_CLICK_BLOCK;
            if (var16 != var74) {
                return;
            }
        }

        Player var6 = var1.getPlayer();
        UUID var7 = var6.getUniqueId();
        int var8 = Bukkit.getCurrentTick();
        Map var23 = this.lastInteractTick;
        Integer var9 = (Integer)var23.get(var7);
        if (var9 != null) {
            int var28 = var9;
            if (var28 == var8) {
                return;
            }
        }

        Map var30 = this.lastInteractTick;
        Integer var90 = var8;
        var30.put(var7, var90);
        int var36 = BlissCoreItems.gemIdOf(var6.getInventory().getItemInOffHand(), 382441079);
        int var79 = GemType.WEALTH.id();
        if (var36 != var79) {
            GemType var80 = GemType.WEALTH;
            if (!GoldGem.isWieldingSoulOff(var6, var80, 1989261555)) {
                return;
            }
        }

        PlayerDataStore var40 = this.players;
        if (var40.get(var7, 1264010596).canUseAbilities(325243089)) {
            Material var12 = var6.getInventory().getItemInMainHand().getType();
            Material var82 = Material.AIR;
            if (var12 == var82 || isSword(var12, 442495571) || isAxe(var12, 794957086)) {
                if (GlobalState.forPlayer(var6, 1433087673).sneakCast(704684993)) {
                    if (!var6.isSneaking()) {
                        return;
                    }

                    Set var72 = this.doubleClickLatch;
                    var72.remove(var7);
                } else {
                    Set var58 = this.doubleClickLatch;
                    if (!var58.contains(var7)) {
                        Set var61 = this.doubleClickLatch;
                        var61.add(var7);
                        BukkitScheduler var63 = Bukkit.getScheduler();
                        BlissPlugin var86 = this.plugin;
                        Runnable var92 = () -> {
                            Set var4 = this.doubleClickLatch;
                            var4.remove(var7);
                        };
                        var63.runTaskLater(var86, var92, 20L);
                        return;
                    }

                    Set var66 = this.doubleClickLatch;
                    var66.remove(var7);
                }

                this.openPockets(var6, 665563815);
            }
        }
    }

    private void openPockets(Player var1, int var2) {
        UUID var6 = var1.getUniqueId();
        WealthPocketsListener.PocketsHolder var12 = new WealthPocketsListener.PocketsHolder(var6, 31653828);
        InventoryType var32 = InventoryType.DROPPER;
        TextComponent var43 = Component.text("Pockets");
        Inventory var8 = Bukkit.createInventory(var12, var32, var43);
        var12.inv = var8;
        Map var17 = this.store;
        ItemStack[] var9 = (ItemStack[])var17.get(var6);
        if (var9 != null) {
            for (int var10 = 0; var10 < 9; var10++) {
                int var37 = var9.length;
                if (var10 >= var37) {
                    break;
                }

                if (var9[var10] != null) {
                    ItemStack var45 = var9[var10];
                    var8.setItem(var10, var45);
                }
            }
        }

        Set var28 = this.openHolders;
        var28.add(var6);
        var1.openInventory(var8);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent var1) {
        if (var1.getInventory().getHolder() instanceof WealthPocketsListener.PocketsHolder var6) {
            UUID var36 = var6.owner;
            ItemStack[] var8 = new ItemStack[9];

            for (int var9 = 0; var9 < 9; var9++) {
                Inventory var34 = var1.getInventory();
                ItemStack var35 = var34.getItem(var9);
                var8[var9] = var35;
            }

            Map var23 = this.store;
            var23.put(var36, var8);
            Set var26 = this.openHolders;
            var26.remove(var36);
            this.save(1917997178);
        }
    }

    public void shutdown(int var1) {
        Set<UUID> var52 = this.openHolders;

        for (UUID var6 : new ArrayList<>(var52)) {
            Player var7 = Bukkit.getPlayer(var6);
            if (var7 == null) {
                Set var44 = this.openHolders;
                var44.remove(var6);
            } else {
                Inventory var8 = var7.getOpenInventory().getTopInventory();
                if (var8.getHolder() instanceof WealthPocketsListener.PocketsHolder var9) {
                    ItemStack[] var56 = new ItemStack[9];

                    for (int var11 = 0; var11 < 9; var11++) {
                        ItemStack var55 = var8.getItem(var11);
                        var56[var11] = var55;
                    }

                    Map var36 = this.store;
                    UUID var48 = var9.owner;
                    var36.put(var48, var56);
                }

                Set var39 = this.openHolders;
                var39.remove(var6);
                var7.closeInventory();
            }
        }

        this.save(1917997178);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent var1) {
        UUID var6 = var1.getEntity().getUniqueId();
        Player var7 = var1.getEntity();
        Inventory var8 = null;
        Set var20 = this.openHolders;
        if (var20.contains(var6) && var7.getOpenInventory().getTopInventory().getHolder() instanceof WealthPocketsListener.PocketsHolder) {
            var8 = var7.getOpenInventory().getTopInventory();
            ItemStack[] var9 = new ItemStack[9];

            for (int var10 = 0; var10 < 9; var10++) {
                ItemStack var87 = var8.getItem(var10);
                var9[var10] = var87;
            }

            Map var36 = this.store;
            var36.put(var6, var9);
        }

        Map var39 = this.store;
        ItemStack[] var93 = (ItemStack[])var39.get(var6);
        if (var93 != null) {
            ArrayList var43 = new ArrayList(9);
            ArrayList var94 = var43;

            for (int var11 = 0; var11 < 9; var11++) {
                Integer var86 = var11;
                var94.add(var86);
            }

            Random var74 = this.random;
            Collections.shuffle(var94, var74);

            for (int var95 = 0; var95 < 4; var95++) {
                int var77 = var94.size();
                if (var95 >= var77) {
                    break;
                }

                int var12 = (Integer)var94.get(var95);
                ItemStack var13 = var93[var12];
                if (var13 != null) {
                    Material var58 = var13.getType();
                    Material var80 = Material.AIR;
                    if (var58 != var80) {
                        World var60 = var7.getWorld();
                        Location var82 = var7.getLocation();
                        var60.dropItemNaturally(var82, var13);
                        var93[var12] = null;
                        if (var8 != null) {
                            var8.setItem(var12, null);
                        }
                    }
                }
            }

            this.save(1917997178);
        }
    }

    private static boolean isSword(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD-> true;
            default -> false;
        };
    }

    private static boolean isAxe(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE-> true;
            default -> false;
        };
    }

    private static final class PocketsHolder implements InventoryHolder {
        private final UUID owner;
        private Inventory inv;

        PocketsHolder(UUID var1, int var2) {
            this.owner = var1;
        }

        public Inventory getInventory() {
            return this.inv;
        }
    }
}
