package com.bliss.mythics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class MythicsCommand implements CommandExecutor, Listener {
    private static final int SLOT_HERETIC = 11;
    private static final int SLOT_AURATUS = 15;
    private static final String ADMIN = "bliss.admin";

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player var8) {
            if (!var8.hasPermission("bliss.admin")) {
                String var20 = "You don't have permission to do that.";
                NamedTextColor var7 = NamedTextColor.RED;
                TextComponent var21 = Component.text(var20, var7);
                var8.sendMessage(var21);
                return true;
            } else {
                open(var8, 1167438110);
                return true;
            }
        } else {
            String var22 = "Player only.";
            NamedTextColor var24 = NamedTextColor.RED;
            TextComponent var23 = Component.text(var22, var24);
            var1.sendMessage(var23);
            return true;
        }
    }

    public static void open(Player var0, int var1) {
        MythicsCommand.Holder var2 = new MythicsCommand.Holder();
        String var3 = "Mythic Gems";
        NamedTextColor var4 = NamedTextColor.DARK_PURPLE;
        TextComponent var23 = Component.text(var3, var4);
        TextDecoration var28 = TextDecoration.BOLD;
        Component var24 = var23.decoration(var28, true);
        Inventory var7 = Bukkit.createInventory(var2, 27, var24);
        var2.inv = var7;
        ItemStack var25 = MythicItems.bloodGem(1434502583);
        var7.setItem(11, var25);
        ItemStack var26 = MythicItems.aegisGem(1323939072);
        var7.setItem(15, var26);
        var0.openInventory(var7);
        Location var22 = var0.getLocation();
        Sound var27 = Sound.BLOCK_CHEST_OPEN;
        var0.playSound(var22, var27, 0.8F, 1.0F);
    }

    @EventHandler
    public void onClick(InventoryClickEvent var1) {
        if (var1.getInventory().getHolder() instanceof MythicsCommand.Holder) {
            var1.setCancelled(true);
            if (var1.getWhoClicked() instanceof Player var6) {
                Inventory var21 = var1.getClickedInventory();
                Inventory var43 = var1.getInventory();
                if (var21 == var43) {
                    ItemStack var56;
                    if (var1.getSlot() == 11) {
                        var56 = MythicItems.bloodGem(1434502583);
                    } else {
                        if (var1.getSlot() != 15) {
                            return;
                        }

                        var56 = MythicItems.aegisGem(1323939072);
                    }

                    PlayerInventory var26 = var6.getInventory();
                    ItemStack[] var46 = new ItemStack[]{var56};

                    for (ItemStack var9 : var26.addItem(var46).values()) {
                        World var36 = var6.getWorld();
                        Location var48 = var6.getLocation();
                        var36.dropItemNaturally(var48, var9);
                    }

                    Location var50 = var6.getLocation();
                    Sound var53 = Sound.ENTITY_ITEM_PICKUP;
                    var6.playSound(var50, var53, 1.0F, 1.2F);
                }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent var1) {
        if (var1.getInventory().getHolder() instanceof MythicsCommand.Holder) {
            var1.setCancelled(true);
        }
    }

    private static final class Holder implements InventoryHolder {
        private Inventory inv;

        public Inventory getInventory() {
            return this.inv;
        }
    }
}
