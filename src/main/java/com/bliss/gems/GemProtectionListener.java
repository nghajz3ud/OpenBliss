package com.bliss.gems;

import com.bliss.core.GlobalState;
import com.bliss.items.BlissCoreItems;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Crafter;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class GemProtectionListener implements Listener {
    private final GlobalState state;

    public GemProtectionListener(GlobalState var1, int var2) {
        this.state = var1;
    }

    @EventHandler(
        priority = EventPriority.LOWEST
    )
    public void onGemItemDamaged(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Item var4) {
            DamageCause var12 = var1.getCause();
            DamageCause var3 = DamageCause.VOID;
            if (var12 != var3) {
                if (BlissCoreItems.gemIdOf(var4.getItemStack(), 382441079) > 0) {
                    var1.setCancelled(true);
                    var4.setInvulnerable(true);
                    var4.setFireTicks(0);
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent var1) {
        if (BlissCoreItems.gemIdOf(var1.getItemDrop().getItemStack(), 382441079) > 0) {
            var1.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent var1) {
        if (var1.getWhoClicked() instanceof Player var4) {
            if (GlobalState.forPlayer(var4, 1433087673).bundleGemProtection(468873788)) {
                ItemStack var84 = var1.getCursor();
                ItemStack var6 = var1.getCurrentItem();
                if (isBundle(var84, 1147792264) && BlissCoreItems.gemIdOf(var6, 382441079) > 0
                    || isBundle(var6, 1147792264) && BlissCoreItems.gemIdOf(var84, 382441079) > 0) {
                    var1.setCancelled(true);
                    return;
                }
            }

            Inventory var85 = var1.getView().getTopInventory();
            InventoryType var34 = var85.getType();
            InventoryType var72 = InventoryType.CRAFTING;
            if (var34 != var72) {
                int var86 = var85.getSize();
                int var38 = var1.getRawSlot();
                boolean var39;
                if (var38 < var86) {
                    var39 = true;
                } else {
                    var39 = false;
                }

                if (!var39) {
                    ClickType var42 = var1.getClick();
                    ClickType var74 = ClickType.SHIFT_LEFT;
                    if (var42 != var74) {
                        ClickType var44 = var1.getClick();
                        ClickType var75 = ClickType.SHIFT_RIGHT;
                        if (var44 != var75) {
                            return;
                        }
                    }

                    if (BlissCoreItems.gemIdOf(var1.getCurrentItem(), 382441079) > 0) {
                        var1.setCancelled(true);
                    }
                } else if (BlissCoreItems.gemIdOf(var1.getCursor(), 382441079) <= 0 && BlissCoreItems.gemIdOf(var1.getCurrentItem(), 382441079) <= 0) {
                    ClickType var56 = var1.getClick();
                    ClickType var77 = ClickType.NUMBER_KEY;
                    if (var56 == var77) {
                        PlayerInventory var58 = var4.getInventory();
                        int var79 = var1.getHotbarButton();
                        if (BlissCoreItems.gemIdOf(var58.getItem(var79), 382441079) > 0) {
                            var1.setCancelled(true);
                            return;
                        }
                    }

                    ClickType var64 = var1.getClick();
                    ClickType var81 = ClickType.SWAP_OFFHAND;
                    if (var64 == var81 && BlissCoreItems.gemIdOf(var4.getInventory().getItemInOffHand(), 382441079) > 0) {
                        var1.setCancelled(true);
                    }
                } else {
                    var1.setCancelled(true);
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

    @EventHandler
    public void onDrag(InventoryDragEvent var1) {
        Inventory var4 = var1.getView().getTopInventory();
        InventoryType var11 = var4.getType();
        InventoryType var3 = InventoryType.CRAFTING;
        if (var11 != var3) {
            if (BlissCoreItems.gemIdOf(var1.getOldCursor(), 382441079) > 0) {
                int var5 = var4.getSize();

                for (int var26 : var1.getRawSlots()) {
                    if (var26 < var5) {
                        var1.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent var1) {
        for (ItemStack var7 : var1.getInventory().getMatrix()) {
            if (var7 != null && BlissCoreItems.gemIdOf(var7, 382441079) > 0) {
                var1.getInventory().setResult(null);
                return;
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onCraft(CraftItemEvent var1) {
        for (ItemStack var7 : var1.getInventory().getMatrix()) {
            if (var7 != null && BlissCoreItems.gemIdOf(var7, 382441079) > 0) {
                var1.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onCrafterCraft(CrafterCraftEvent var1) {
        BlockState var5 = var1.getBlock().getState();
        if (var5 instanceof Crafter) {
            ItemStack[] var29 = ((Crafter)var5).getInventory().getContents();
            int var6 = var29.length;

            for (int var7 = 0; var7 < var6; var7++) {
                if (BlissCoreItems.gemIdOf(var29[var7], 382441079) > 0) {
                    var1.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onShelfInsert(PlayerInteractEvent var1) {
        Action var6 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_BLOCK;
        if (var6 == var3) {
            Block var4 = var1.getClickedBlock();
            if (var4 != null && var4.getType().name().endsWith("_SHELF")) {
                EquipmentSlot var15 = var1.getHand();
                EquipmentSlot var27 = EquipmentSlot.OFF_HAND;
                ItemStack var25;
                if (var15 == var27) {
                    var25 = var1.getPlayer().getInventory().getItemInOffHand();
                } else {
                    var25 = var1.getPlayer().getInventory().getItemInMainHand();
                }

                if (BlissCoreItems.gemIdOf(var25, 382441079) > 0) {
                    var1.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPotInsert(PlayerInteractEvent var1) {
        Action var6 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_BLOCK;
        if (var6 == var3) {
            Block var4 = var1.getClickedBlock();
            if (var4 != null) {
                Material var11 = var4.getType();
                Material var24 = Material.DECORATED_POT;
                if (var11 == var24) {
                    EquipmentSlot var13 = var1.getHand();
                    EquipmentSlot var25 = EquipmentSlot.OFF_HAND;
                    ItemStack var17;
                    if (var13 == var25) {
                        var17 = var1.getPlayer().getInventory().getItemInOffHand();
                    } else {
                        var17 = var1.getPlayer().getInventory().getItemInMainHand();
                    }

                    if (BlissCoreItems.gemIdOf(var17, 382441079) > 0) {
                        var1.setCancelled(true);
                    }

                    return;
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPotFeed(InventoryMoveItemEvent var1) {
        InventoryType var5 = var1.getDestination().getType();
        InventoryType var3 = InventoryType.DECORATED_POT;
        if (var5 == var3) {
            if (BlissCoreItems.gemIdOf(var1.getItem(), 382441079) > 0) {
                var1.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFrameInsert(PlayerInteractEntityEvent var1) {
        if (var1.getRightClicked() instanceof ItemFrame) {
            EquipmentSlot var8 = var1.getHand();
            EquipmentSlot var3 = EquipmentSlot.OFF_HAND;
            ItemStack var18;
            if (var8 == var3) {
                var18 = var1.getPlayer().getInventory().getItemInOffHand();
            } else {
                var18 = var1.getPlayer().getInventory().getItemInMainHand();
            }

            if (BlissCoreItems.gemIdOf(var18, 382441079) > 0) {
                var1.setCancelled(true);
            }
        }
    }
}
