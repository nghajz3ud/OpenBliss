package com.bliss.dragongames;

import com.bliss.BlissPlugin;
import com.bliss.villager.Text;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

public final class DragonGamesListener implements Listener {
    private static final Component SELECT_TARGET_TITLE = Text.legacy("&cSelect Target", 373192400);
    private final BlissPlugin plugin;
    private final DragonGamesManager games;

    public DragonGamesListener(BlissPlugin var1, DragonGamesManager var2, int var3) {
        this.plugin = var1;
        this.games = var2;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        DragonGamesManager var4 = this.games;
        Player var5 = var1.getPlayer();
        var4.onJoin(var5, 1034925991);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent var1) {
        Player var4 = var1.getPlayer();
        if (this.games.isRunning(320515049)) {
            DragonGamesManager var10 = this.games;
            UUID var14 = var4.getUniqueId();
            if (var10.isParticipant(var14, 187258544)) {
                DragonGamesManager var13 = this.games;
                var13.applyBorder(var4, 2029419438);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent var1) {
        if (this.games.isRunning(320515049)) {
            Player var5 = var1.getEntity();
            Player var6 = var5.getKiller();
            if (var6 != null) {
                UUID var15 = var6.getUniqueId();
                UUID var20 = var5.getUniqueId();
                if (var15.equals(var20)) {
                    var6 = null;
                }
            }

            DragonGamesManager var19 = this.games;
            var19.onDeath(var5, var6, 1184844045);
        }
    }

    @EventHandler
    public void onConsumeHeart(PlayerInteractEvent var1) {
        EquipmentSlot var7 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var7 == var3) {
            Action var9 = var1.getAction();
            Action var29 = Action.RIGHT_CLICK_AIR;
            if (var9 != var29) {
                Action var11 = var1.getAction();
                Action var30 = Action.RIGHT_CLICK_BLOCK;
                if (var11 != var30) {
                    return;
                }
            }

            Player var5 = var1.getPlayer();
            ItemStack var6 = var5.getInventory().getItemInMainHand();
            if (DragonGamesItems.isExtraHeart(var6, 261635391)) {
                if (this.games.isRunning(320515049)) {
                    DragonGamesManager var23 = this.games;
                    UUID var32 = var5.getUniqueId();
                    if (var23.isParticipant(var32, 187258544)) {
                        var1.setCancelled(true);
                        int var36 = var6.getAmount() - 1;
                        var6.setAmount(var36);
                        DragonGamesManager var28 = this.games;
                        var28.consumeHeart(var5, 1797233792);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUseTracker(PlayerInteractEvent var1) {
        EquipmentSlot var5 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var5 == var3) {
            Action var7 = var1.getAction();
            Action var28 = Action.RIGHT_CLICK_AIR;
            if (var7 != var28) {
                Action var9 = var1.getAction();
                Action var29 = Action.RIGHT_CLICK_BLOCK;
                if (var9 != var29) {
                    return;
                }
            }

            Player var4 = var1.getPlayer();
            if (DragonGamesItems.isTracker(var4.getInventory().getItemInMainHand(), 774563610)) {
                if (this.games.isRunning(320515049)) {
                    DragonGamesManager var20 = this.games;
                    UUID var31 = var4.getUniqueId();
                    if (var20.isParticipant(var31, 187258544)) {
                        var1.setCancelled(true);
                        DragonGamesManager var24 = this.games;
                        UUID var34 = var4.getUniqueId();
                        if (var24.isTracking(var34, 614519495)) {
                            Component var37 = Text.legacy("&cYour Tracking Compass is already in use.", 373192400);
                            var4.sendMessage(var37);
                            return;
                        }

                        this.openSelectTarget(var4, 371756640);
                        return;
                    }
                }
            }
        }
    }

    private void openSelectTarget(Player var1, int var2) {
        int var48 = Bukkit.getOnlinePlayers().size() - 1;
        int var66 = (Math.max(0, var48) + 8) / 9;
        int var50 = Math.min(6, var66);
        int var8 = Math.max(1, var50);
        UUID var68 = var1.getUniqueId();
        DragonGamesListener.SelectTargetHolder var19 = new DragonGamesListener.SelectTargetHolder(var68, 1019409256);
        int var52 = var8 * 9;
        Component var70 = SELECT_TARGET_TITLE;
        Inventory var9 = Bukkit.createInventory(var19, var52, var70);
        int var10 = 0;

        for (Player var12 : Bukkit.getOnlinePlayers()) {
            if (!var12.equals(var1)) {
                int var55 = var9.getSize();
                if (var10 < var55) {
                    Material var71 = Material.PLAYER_HEAD;
                    ItemStack var32 = new ItemStack(var71);
                    if (var32.getItemMeta() instanceof SkullMeta var14) {
                        var14.setOwningPlayer(var12);
                        TextComponent var59 = Component.text(var12.getName());
                        TextDecoration var72 = TextDecoration.ITALIC;
                        Component var60 = var59.decoration(var72, false);
                        var14.displayName(var60);
                        var32.setItemMeta(var14);
                    }

                    int var62 = var10++;
                    var9.setItem(var62, var32);
                }
            }
        }

        var1.openInventory(var9);
    }

    @EventHandler
    public void onSelectTarget(InventoryClickEvent var1) {
        if (var1.getInventory().getHolder() instanceof DragonGamesListener.SelectTargetHolder var5) {
            var1.setCancelled(true);
            if (var1.getWhoClicked() instanceof Player var72) {
                UUID var24 = var72.getUniqueId();
                UUID var60 = var5.owner;
                if (var24.equals(var60)) {
                    ItemStack var73 = var1.getCurrentItem();
                    if (var73 != null) {
                        Material var30 = var73.getType();
                        Material var61 = Material.PLAYER_HEAD;
                        if (var30 == var61) {
                            if (var73.getItemMeta() instanceof SkullMeta var8 && var8.getOwningPlayer() != null) {
                                UUID var74 = var8.getOwningPlayer().getUniqueId();
                                if (this.games.isRunning(320515049)) {
                                    DragonGamesManager var46 = this.games;
                                    UUID var63 = var72.getUniqueId();
                                    if (var46.isParticipant(var63, 187258544)) {
                                        DragonGamesManager var49 = this.games;
                                        UUID var65 = var72.getUniqueId();
                                        if (!var49.isTracking(var65, 614519495)) {
                                            if (!hasTracker(var72, 2122861991)) {
                                                var72.closeInventory();
                                                return;
                                            }

                                            DragonGamesManager var54 = this.games;
                                            var54.startTracking(var72, var74, 2012983961);
                                            var72.closeInventory();
                                            Component var71 = Text.legacy("&aNow tracking &f" + var8.getOwningPlayer().getName() + " &afor 1 hour.", 373192400);
                                            var72.sendMessage(var71);
                                            return;
                                        }
                                    }
                                }

                                var72.closeInventory();
                                return;
                            }

                            return;
                        }
                    }
                }
            }
        }
    }

    private static boolean hasTracker(Player var0, int var1) {
        ItemStack[] var4 = var0.getInventory().getContents();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; var6++) {
            if (DragonGamesItems.isTracker(var4[var6], 774563610)) {
                return true;
            }
        }

        return false;
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onDropTracker(PlayerDropItemEvent var1) {
        if (DragonGamesItems.isTracker(var1.getItemDrop().getItemStack(), 774563610)) {
            DragonGamesManager var8 = this.games;
            UUID var14 = var1.getPlayer().getUniqueId();
            if (var8.isTracking(var14, 614519495)) {
                var1.setCancelled(true);
                Player var12 = var1.getPlayer();
                Component var17 = Text.legacy("&c You Can't drop tracker while it's being Used", 373192400);
                var12.sendMessage(var17);
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onMoveTracker(InventoryClickEvent var1) {
        if (var1.getWhoClicked() instanceof Player var4) {
            DragonGamesManager var14 = this.games;
            UUID var72 = var4.getUniqueId();
            if (var14.isTracking(var72, 614519495)) {
                if ((!isBundle(var1.getCursor(), 201995880) || !DragonGamesItems.isTracker(var1.getCurrentItem(), 774563610))
                    && (!DragonGamesItems.isTracker(var1.getCursor(), 774563610) || !isBundle(var1.getCurrentItem(), 201995880))) {
                    Inventory var82 = var1.getView().getTopInventory();
                    InventoryType var32 = var82.getType();
                    InventoryType var73 = InventoryType.CRAFTING;
                    if (var32 != var73) {
                        boolean var38;
                        label85: {
                            if (var1.getClickedInventory() != null) {
                                Inventory var36 = var1.getClickedInventory();
                                if (var36.equals(var82)) {
                                    var38 = true;
                                    break label85;
                                }
                            }

                            var38 = false;
                        }

                        boolean var70;
                        label110: {
                            if ((!var38 || !DragonGamesItems.isTracker(var1.getCursor(), 774563610))
                                && (var38 || !var1.isShiftClick() || !DragonGamesItems.isTracker(var1.getCurrentItem(), 774563610))) {
                                InventoryAction var50 = var1.getAction();
                                InventoryAction var75 = InventoryAction.MOVE_TO_OTHER_INVENTORY;
                                label71:
                                if (var50 != var75 || !DragonGamesItems.isTracker(var1.getCurrentItem(), 774563610)) {
                                    if (var38) {
                                        ClickType var56 = var1.getClick();
                                        ClickType var76 = ClickType.NUMBER_KEY;
                                        if (var56 == var76) {
                                            PlayerInventory var58 = var4.getInventory();
                                            int var78 = var1.getHotbarButton();
                                            if (DragonGamesItems.isTracker(var58.getItem(var78), 774563610)) {
                                                break label71;
                                            }
                                        }
                                    }

                                    if (var38) {
                                        ClickType var63 = var1.getClick();
                                        ClickType var79 = ClickType.SWAP_OFFHAND;
                                        if (var63 == var79 && DragonGamesItems.isTracker(var4.getInventory().getItemInOffHand(), 774563610)) {
                                            break label71;
                                        }
                                    }

                                    var70 = false;
                                    break label110;
                                }
                            }

                            var70 = true;
                        }

                        if (var70) {
                            var1.setCancelled(true);
                        }
                    }
                } else {
                    var1.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onFrameTracker(PlayerInteractEntityEvent var1) {
        if (var1.getRightClicked() instanceof ItemFrame var4) {
            Player var37 = var1.getPlayer();
            DragonGamesManager var15 = this.games;
            UUID var32 = var37.getUniqueId();
            if (var15.isTracking(var32, 614519495)) {
                if (var4.getItem().getType().isAir()) {
                    EquipmentSlot var22 = var1.getHand();
                    EquipmentSlot var33 = EquipmentSlot.OFF_HAND;
                    ItemStack var31;
                    if (var22 == var33) {
                        var31 = var37.getInventory().getItemInOffHand();
                    } else {
                        var31 = var37.getInventory().getItemInMainHand();
                    }

                    if (DragonGamesItems.isTracker(var31, 774563610)) {
                        var1.setCancelled(true);
                        Component var36 = Text.legacy("&c You Can't put the tracker in an item frame while it's being Used", 373192400);
                        var37.sendMessage(var36);
                    }
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onDragTracker(InventoryDragEvent var1) {
        if (var1.getWhoClicked() instanceof Player var4) {
            DragonGamesManager var14 = this.games;
            UUID var35 = var4.getUniqueId();
            if (var14.isTracking(var35, 614519495)) {
                if (DragonGamesItems.isTracker(var1.getOldCursor(), 774563610)) {
                    Inventory var40 = var1.getView().getTopInventory();
                    InventoryType var23 = var40.getType();
                    InventoryType var36 = InventoryType.CRAFTING;
                    if (var23 != var36) {
                        for (int var33 : var1.getRawSlots()) {
                            int var38 = var40.getSize();
                            if (var33 < var38) {
                                var1.setCancelled(true);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isBundle(ItemStack var0, int var1) {
        boolean var8;
        if (var0 != null && var0.getType().name().endsWith("BUNDLE")) {
            var8 = true;
        } else {
            var8 = false;
        }

        return var8;
    }

    private static final class SelectTargetHolder implements InventoryHolder {
        final UUID owner;

        SelectTargetHolder(UUID var1, int var2) {
            this.owner = var1;
        }

        public Inventory getInventory() {
            return null;
        }
    }
}
