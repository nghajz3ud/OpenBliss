package com.bliss.mythics.juggernaut;

import com.bliss.core.ItemEscrow;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import com.bliss.mythics.MythicItems;
import com.bliss.mythics.util.Text;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class JuggernautKitEditor implements Listener {
    private static final int A0 = 0;
    private static final int A_LEN = 4;
    private static final int I0 = 4;
    private static final int I_LEN = 36;
    private static final int OFF = 40;
    private static final int E0 = 41;
    private static final int E_LEN = 27;
    private static final int TOTAL = 68;
    private final Plugin plugin;
    private final JuggernautManager mgr;
    private final ItemEscrow escrow;
    private UUID editor;
    private JuggernautManager.Bond editing;

    public JuggernautKitEditor(Plugin var1, JuggernautManager var2, int var3) {
        this.plugin = var1;
        this.mgr = var2;
        ItemEscrow var12 = new ItemEscrow(var1, "juggernaut_kit_edit.yml", 528395154);
        this.escrow = var12;
    }

    public boolean isEditing(Player var1, int var2) {
        if (var1 != null) {
            UUID var8 = var1.getUniqueId();
            if (this.isEditing(var8, 790091667)) {
                return true;
            }
        }

        return false;
    }

    public boolean isEditing(UUID var1, int var2) {
        if (var1 != null) {
            UUID var8 = this.editor;
            if (var1.equals(var8)) {
                return true;
            }
        }

        return false;
    }

    public UUID editorId(int var1) {
        return this.editor;
    }

    public JuggernautManager.Bond editingBond(int var1) {
        return this.editing;
    }

    public boolean begin(Player var1, JuggernautManager.Bond var2, int var3) {
        if (this.editor != null) {
            return false;
        } else {
            JuggernautManager var12 = this.mgr;
            if (var12.isJuggernaut(var1, 1070464021)) {
                return false;
            } else {
                ItemEscrow var15 = this.escrow;
                UUID var31 = var1.getUniqueId();
                ItemStack[] var41 = snapshot(var1, 564528203);
                if (!var15.hold(var31, var41, 1481203625)) {
                    Logger var19 = this.plugin.getLogger();
                    String var34 = "Juggernaut kit edit: could not save " + var1.getName() + "'s inventory — refusing to start the fitting.";
                    var19.warning(var34);
                    return false;
                } else {
                    UUID var36 = var1.getUniqueId();
                    this.editor = var36;
                    this.editing = var2;
                    clear(var1, 1566125242);
                    JuggernautManager var25 = this.mgr;
                    var25.loadKitForEditing(var1, var2, 718259960);
                    stripGems(var1, 661602410);
                    Location var40 = var1.getLocation();
                    Sound var43 = Sound.BLOCK_SMITHING_TABLE_USE;
                    var1.playSound(var40, var43, 1.0F, 1.1F);
                    return true;
                }
            }
        }
    }

    public boolean save(Player var1, int var2) {
        stripGems(var1, 661602410);
        PlayerInventory var9 = var1.getInventory();
        JuggernautManager var14 = this.mgr;
        JuggernautManager.Bond var20;
        if (this.editing == null) {
            var20 = JuggernautManager.Bond.HERETIC;
        } else {
            var20 = this.editing;
        }

        ItemStack[] var25 = var9.getArmorContents();
        ItemStack[] var27 = var9.getStorageContents();
        ItemStack var29 = var9.getItemInOffHand();
        ItemStack[] var33 = var1.getEnderChest().getContents();
        boolean var10 = var14.saveKitTemplate(var20, var25, var27, var29, var33, 2145089182);
        this.finish(var1, 1076737269);
        Location var23 = var1.getLocation();
        Sound var26 = Sound.BLOCK_ANVIL_USE;
        float var31;
        if (var10) {
            var31 = 1.2F;
        } else {
            var31 = 0.6F;
        }

        var1.playSound(var23, var26, 1.0F, var31);
        return var10;
    }

    public void cancel(Player var1, int var2) {
        this.finish(var1, 1076737269);
        Location var10 = var1.getLocation();
        Sound var5 = Sound.BLOCK_BARREL_CLOSE;
        var1.playSound(var10, var5, 1.0F, 0.9F);
    }

    private void finish(Player var1, int var2) {
        this.handBack(var1, 760338216);
        if (this.isEditing(var1, 1844728332)) {
            this.editor = null;
            this.editing = null;
        }
    }

    private void handBack(Player var1, int var2) {
        UUID var6 = var1.getUniqueId();
        ItemEscrow var11 = this.escrow;
        if (!var11.has(var6, 1463317092)) {
            Logger var30 = this.plugin.getLogger();
            String var40 = "Juggernaut kit edit: nothing in escrow for " + var1.getName() + " — leaving their inventory alone.";
            var30.warning(var40);
        } else if (!var1.isDead()) {
            ItemEscrow var16 = this.escrow;
            ItemStack[] var7 = var16.release(var6, 1427941333);
            if (var7 != null) {
                try {
                    clear(var1, 1566125242);
                    ItemStack[] var33 = fit(var7, 427008608);
                    apply(var1, var33, 546738331);
                } catch (RuntimeException var43) {
                    ItemEscrow var23 = this.escrow;
                    var23.hold(var6, var7, 1481203625);
                    Logger var27 = this.plugin.getLogger();
                    String var36 = var1.getName();
                    String var42 = var43.getMessage();
                    String var37 = "Juggernaut kit edit: could not restore " + var36 + "'s inventory (" + var42 + ") — held for their next join.";
                    var27.warning(var37);
                }
            }
        }
    }

    private static ItemStack[] fit(ItemStack[] var0, int var1) {
        ItemStack[] var6 = new ItemStack[68];
        if (var0 != null) {
            int var7 = 0;

            while (true) {
                int var16 = var0.length;
                int var14 = Math.min(68, var16);
                if (var7 >= var14) {
                    break;
                }

                ItemStack var18 = var0[var7];
                var6[var7] = var18;
                var7++;
            }
        }

        return var6;
    }

    @EventHandler(
        priority = EventPriority.LOW
    )
    public void onDrop(PlayerDropItemEvent var1) {
        Player var8 = var1.getPlayer();
        if (this.isEditing(var8, 1844728332)) {
            var1.setCancelled(true);
            Player var7 = var1.getPlayer();
            Component var11 = Text.legacy("&cNothing leaves the kit fitting.", 883008065);
            var7.sendActionBar(var11);
        }
    }

    @EventHandler(
        priority = EventPriority.LOW
    )
    public void onPickup(EntityPickupItemEvent var1) {
        if (var1.getEntity() instanceof Player var4 && this.isEditing(var4, 1844728332)) {
            var1.setCancelled(true);
        }
    }

    @EventHandler(
        priority = EventPriority.LOW
    )
    public void onOpen(InventoryOpenEvent var1) {
        if (var1.getPlayer() instanceof Player var4 && this.isEditing(var4, 1844728332)) {
            InventoryType var27 = var1.getInventory().getType();
            InventoryType var21 = InventoryType.ENDER_CHEST;
            if (var27 != var21) {
                InventoryType var22 = InventoryType.CRAFTING;
                if (var27 != var22) {
                    InventoryType var23 = InventoryType.PLAYER;
                    if (var27 != var23) {
                        var1.setCancelled(true);
                        Component var26 = Text.legacy("&cNo containers while editing the kit.", 883008065);
                        var4.sendActionBar(var26);
                        return;
                    }
                }
            }

            return;
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    public void onDeath(PlayerDeathEvent var1) {
        Player var10 = var1.getEntity();
        if (this.isEditing(var10, 1844728332)) {
            var1.getDrops().clear();
            var1.setKeepInventory(true);
            var1.setKeepLevel(true);
            var1.setDroppedExp(0);
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onQuit(PlayerQuitEvent var1) {
        Player var6 = var1.getPlayer();
        if (this.isEditing(var6, 1844728332)) {
            Player var8 = var1.getPlayer();
            this.finish(var8, 1076737269);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        Player var4 = var1.getPlayer();
        this.recover(var4, 913116880);
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onRespawn(PlayerRespawnEvent var1) {
        Player var4 = var1.getPlayer();
        this.recover(var4, 913116880);
    }

    private void recover(Player var1, int var2) {
        if (!this.isEditing(var1, 1844728332)) {
            ItemEscrow var9 = this.escrow;
            UUID var17 = var1.getUniqueId();
            if (var9.has(var17, 1463317092)) {
                BukkitScheduler var14 = this.plugin.getServer().getScheduler();
                Plugin var19 = this.plugin;
                Runnable var20 = () -> {
                    if (var1.isOnline() && !var1.isDead()) {
                        ItemEscrow var8 = this.escrow;
                        UUID var12 = var1.getUniqueId();
                        if (var8.has(var12, 1463317092)) {
                            this.handBack(var1, 760338216);
                            Component var15 = Text.legacy("&eYour own inventory is back — the kit edit did not finish.", 883008065);
                            var1.sendMessage(var15);
                            return;
                        }
                    }
                };
                var14.runTask(var19, var20);
            }
        }
    }

    public void shutdown(int var1) {
        if (this.editor != null) {
            Player var4 = Bukkit.getPlayer(this.editor);
            if (var4 != null) {
                this.finish(var4, 1076737269);
                Component var15 = Text.legacy("&eKit edit ended — the server is stopping. Nothing was saved.", 883008065);
                var4.sendMessage(var15);
            }

            this.editor = null;
            this.editing = null;
        }
    }

    private static ItemStack[] snapshot(Player var0, int var1) {
        PlayerInventory var7 = var0.getInventory();
        ItemStack[] var8 = new ItemStack[68];
        ItemStack[] var13 = var7.getArmorContents();
        System.arraycopy(var13, 0, var8, 0, 4);
        ItemStack[] var15 = var7.getStorageContents();
        System.arraycopy(var15, 0, var8, 4, 36);
        ItemStack var26 = var7.getItemInOffHand();
        var8[40] = var26;
        ItemStack[] var19 = var0.getEnderChest().getContents();
        System.arraycopy(var19, 0, var8, 41, 27);
        return var8;
    }

    private static void apply(Player var0, ItemStack[] var1, int var2) {
        PlayerInventory var7 = var0.getInventory();
        ItemStack[] var15 = Arrays.copyOfRange(var1, 0, 4);
        var7.setArmorContents(var15);
        ItemStack[] var18 = Arrays.copyOfRange(var1, 4, 40);
        var7.setStorageContents(var18);
        ItemStack var25;
        if (var1.length > 40) {
            var25 = var1[40];
        } else {
            var25 = null;
        }

        var7.setItemInOffHand(var25);
        Inventory var13 = var0.getEnderChest();
        ItemStack[] var24 = Arrays.copyOfRange(var1, 41, 68);
        var13.setContents(var24);
    }

    private static void clear(Player var0, int var1) {
        PlayerInventory var4 = var0.getInventory();
        var4.clear();
        ItemStack[] var11 = new ItemStack[4];
        var4.setArmorContents(var11);
        var4.setItemInOffHand(null);
        var0.getEnderChest().clear();
    }

    private static void stripGems(Player var0, int var1) {
        PlayerInventory var5 = var0.getInventory();
        ItemStack[] var6 = var5.getArmorContents();
        int var7 = 0;

        while (true) {
            int var44 = var6.length;
            if (var7 >= var44) {
                var5.setArmorContents(var6);
                ItemStack[] var61 = var5.getStorageContents();
                int var8 = 0;

                while (true) {
                    int var49 = var61.length;
                    if (var8 >= var49) {
                        var5.setStorageContents(var61);
                        if (isGem(var5.getItemInOffHand(), 1769716342)) {
                            var5.setItemInOffHand(null);
                        }

                        ItemStack[] var62 = var0.getEnderChest().getContents();
                        int var9 = 0;

                        while (true) {
                            int var55 = var62.length;
                            if (var9 >= var55) {
                                Inventory var43 = var0.getEnderChest();
                                var43.setContents(var62);
                                return;
                            }

                            if (isGem(var62[var9], 1769716342)) {
                                var62[var9] = null;
                            }

                            var9++;
                        }
                    }

                    if (isGem(var61[var8], 1769716342)) {
                        var61[var8] = null;
                    }

                    var8++;
                }
            }

            if (isGem(var6[var7], 1769716342)) {
                var6[var7] = null;
            }

            var7++;
        }
    }

    private static boolean isGem(ItemStack var0, int var1) {
        if (var0 == null) {
            return false;
        } else if (!MythicItems.isMythicBloodGem(var0, 824514243)
            && !MythicItems.isMythicAegisGem(var0, 73386651)
            && !GoldGem.isGoldGem(var0)
            && BlissCoreItems.gemIdOf(var0, 382441079) <= 0) {
            ItemMeta var4 = var0.getItemMeta();
            if (var4 instanceof BlockStateMeta var5 && var5.hasBlockState()) {
                BlockState var7 = var5.getBlockState();
                if (var7 instanceof Container) {
                    ItemStack[] var66 = ((Container)var7).getInventory().getContents();
                    int var8 = var66.length;

                    for (int var9 = 0; var9 < var8; var9++) {
                        if (isGem(var66[var9], 1769716342)) {
                            return true;
                        }
                    }
                }
            }

            if (var4 instanceof BundleMeta) {
                Iterator var65 = ((BundleMeta)var4).getItems().iterator();

                while (var65.hasNext()) {
                    if (isGem((ItemStack)var65.next(), 1769716342)) {
                        return true;
                    }
                }
            }

            return false;
        } else {
            return true;
        }
    }
}
