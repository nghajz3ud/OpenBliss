package com.bliss.villager;

import com.bliss.BlissPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class EnergyTokenListener implements Listener {
    private final BlissPlugin plugin;
    private final EventState state;
    private final BlissItems items;

    public EnergyTokenListener(BlissPlugin var1, EventState var2, BlissItems var3, int var4) {
        this.plugin = var1;
        this.state = var2;
        this.items = var3;
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent var1) {
        EquipmentSlot var11 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var11 == var3) {
            Action var13 = var1.getAction();
            Action var44 = Action.LEFT_CLICK_AIR;
            if (var13 != var44) {
                Action var15 = var1.getAction();
                Action var45 = Action.LEFT_CLICK_BLOCK;
                if (var15 != var45) {
                    return;
                }
            }

            if (this.state.isEventRunning(527098672)) {
                Player var7 = var1.getPlayer();
                ItemStack var8 = var7.getInventory().getItemInMainHand();
                BlissItems var25 = this.items;
                if (var25.isBottledEnergy(var8, 1333282282)) {
                    var1.setCancelled(true);
                    int var50 = var8.getAmount() - 1;
                    var8.setAmount(var50);
                    PlayerInventory var30 = var7.getInventory();
                    ItemStack[] var52 = new ItemStack[1];
                    ItemStack var63 = this.items.energyToken(624817347);
                    var52[0] = var63;

                    for (ItemStack var10 : var30.addItem(var52).values()) {
                        World var40 = var7.getWorld();
                        Location var54 = var7.getLocation();
                        var40.dropItemNaturally(var54, var10);
                    }

                    Location var56 = var7.getLocation();
                    Sound var60 = Sound.BLOCK_AMETHYST_BLOCK_CHIME;
                    var7.playSound(var56, var60, 1.0F, 1.5F);
                    Component var58 = Text.legacy(
                        "<##90EE90>⚗ Converted 1 &b&lBottled Energy &r<##90EE90>into an &a&lEnergy Token&r<##90EE90>. Irreversible.", 373192400
                    );
                    var7.sendMessage(var58);
                }
            }
        }
    }
}
