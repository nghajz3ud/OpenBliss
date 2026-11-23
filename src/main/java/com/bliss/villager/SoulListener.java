package com.bliss.villager;

import com.bliss.BlissPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

public final class SoulListener implements Listener {
    private static final double COMPASS_RETURN_RADIUS = 10.0;
    private static final long INVENTORY_LOAD_DELAY_TICKS = 20L;
    private static final long BACKGROUND_SCAN_PERIOD_TICKS = 80L;
    private final BlissPlugin plugin;
    private final EventState state;
    private final BlissItems items;
    private boolean finalTriggerPending = false;

    public SoulListener(BlissPlugin var1, EventState var2, BlissItems var3, int var4) {
        this.plugin = var1;
        this.state = var2;
        this.items = var3;
        BukkitScheduler var16 = Bukkit.getScheduler();
        Runnable var22 = this::backgroundScan;
        var16.runTaskTimer(var1, var22, 80L, 80L);
    }

    @EventHandler
    public void onSoulDrop(PlayerDropItemEvent var1) {
        if (this.state.isEventRunning(527098672)) {
            ItemStack var5 = var1.getItemDrop().getItemStack();
            BlissItems var14 = this.items;
            int var6 = var14.soulIdOf(var5, 1386206139);
            if (var6 != -1) {
                String var20 = var1.getPlayer().getName();
                EventState var30 = this.state;
                String var31 = var30.soulHolder(var6, 745178346);
                if (var20.equals(var31)) {
                    EventState var23 = this.state;
                    var23.clearSoulHolder(var6, 678506716);
                    EventState var25 = this.state;
                    Location var36 = var1.getItemDrop().getLocation();
                    var25.setSoulLastPos(var6, var36, 1364685242);
                    this.state.save(1112893273);
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPickup(EntityPickupItemEvent var1) {
        if (var1.getEntity() instanceof Player var5) {
            if (this.state.isEventRunning(527098672)) {
                BlissItems var16 = this.items;
                ItemStack var26 = var1.getItem().getItemStack();
                int var31 = var16.soulIdOf(var26, 1386206139);
                if (var31 != -1) {
                    EventState var20 = this.state;
                    String var30 = var5.getName();
                    var20.setSoulHolder(var31, var30, 1372578011);
                    EventState var22 = this.state;
                    var22.clearSoulLastPos(var31, 2020548249);
                    this.state.save(1112893273);
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onSoulPlace(BlockPlaceEvent var1) {
        BlissItems var4 = this.items;
        ItemStack var9 = var1.getItemInHand();
        if (var4.soulIdOf(var9, 1386206139) != -1) {
            var1.setCancelled(true);
            Player var8 = var1.getPlayer();
            Component var13 = Text.legacy("&cThe soul is not a seed — take it to the compass.", 373192400);
            var8.sendMessage(var13);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        EquipmentSlot var12 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var12 == var3) {
            Action var14 = var1.getAction();
            Action var45 = Action.RIGHT_CLICK_AIR;
            if (var14 != var45) {
                Action var16 = var1.getAction();
                Action var46 = Action.RIGHT_CLICK_BLOCK;
                if (var16 != var46) {
                    return;
                }
            }

            if (this.state.isEventRunning(527098672)) {
                Player var8 = var1.getPlayer();
                ItemStack var9 = var8.getInventory().getItemInMainHand();
                BlissItems var26 = this.items;
                int var10 = var26.soulIdOf(var9, 1386206139);
                if (var10 != -1) {
                    var1.setCancelled(true);
                    Location var11 = this.state.compassLoc(561536293);
                    if (var11 != null && var11.getWorld() != null) {
                        World var37 = var11.getWorld();
                        World var51 = var8.getWorld();
                        if (var37.equals(var51)) {
                            Location var53 = var8.getLocation();
                            if (var11.distance(var53) <= 10.0) {
                                this.returnSoul(var8, var10, var9, var11, 1743240663);
                                return;
                            }
                        }
                    }

                    this.summonSoul(var8, var10, var9, 1493472284);
                }
            }
        }
    }

    private void returnSoul(Player var1, int var2, ItemStack var3, Location var4, int var5) {
        boolean var11 = this.state.isFinalActive(245445441);
        if (!var11) {
            EventState var17 = this.state;
            if (var17.isVillagerReturned(var2, 889394742)) {
                Component var60 = Text.legacy("&cThis villager has already been returned.", 373192400);
                var1.sendMessage(var60);
                return;
            }
        }

        EventState var21 = this.state;
        Villager var12 = var21.getVillager(var2, 1571302102);
        if (var12 != null) {
            EventState var25 = this.state;
            if (var25.isVillagerAlive(var2, 2034788216)) {
                EventState var28 = this.state;
                var28.clearVillager(var2, 1097126249);
                EventState var30 = this.state;
                var30.setVillagerAlive(var2, false, 1445271449);
                var12.remove();
            }
        }

        EventState var33 = this.state;
        var33.setVillagerAlive(var2, false, 1445271449);
        EventState var84 = this.state;
        VillagerSetup.spawn(var2, var4, var84, 1024137934);
        int var69 = var3.getAmount() - 1;
        var3.setAmount(var69);
        EventState var38 = this.state;
        var38.clearSoulHolder(var2, 678506716);
        EventState var40 = this.state;
        var40.clearSoulLastPos(var2, 2020548249);
        if (var11) {
            this.state.save(1112893273);
            World var45 = var4.getWorld();
            Sound var86 = Sound.ENTITY_VILLAGER_CELEBRATE;
            var45.playSound(var4, var86, 2.0F, 1.0F);
            Component var74 = Cosmetics.villagerName(var2, 527184414);
            Component var88 = Text.legacy("&7 stands again.", 373192400);
            Component var75 = var74.append(var88);
            var1.sendMessage(var75);
        } else {
            this.state.incrementVillagersReturned(1276187594);
            EventState var50 = this.state;
            var50.markVillagerReturned(var2, 426820113);
            World var52 = var4.getWorld();
            Sound var89 = Sound.ENTITY_VILLAGER_CELEBRATE;
            var52.playSound(var4, var89, 2.0F, 1.0F);
            if (this.state.villagersReturned(1094152772) >= 3) {
                this.finalTriggerPending = true;
                BukkitScheduler var57 = Bukkit.getScheduler();
                BlissPlugin var81 = this.plugin;
                Runnable var91 = this::triggerFinalEvent;
                var57.runTaskLater(var81, var91, 60L);
            }
        }
    }

    private void summonSoul(Player var1, int var2, ItemStack var3, int var4) {
        if (this.state.isFinalActive(245445441)) {
            Component var31 = Text.legacy("&cBring the soul to the compass — the village is under attack.", 373192400);
            var1.sendMessage(var31);
        } else {
            EventState var14 = this.state;
            if (var14.isVillagerAlive(var2, 2034788216)) {
                EventState var17 = this.state;
                if (var17.getVillager(var2, 1571302102) == null) {
                    EventState var20 = this.state;
                    var20.setVillagerAlive(var2, false, 1445271449);
                }
            }

            EventState var22 = this.state;
            if (var22.isVillagerAlive(var2, 2034788216)) {
                Component var38 = Text.legacy("&cVillager " + var2 + " is already alive somewhere! Kill them first.", 373192400);
                var1.sendMessage(var38);
            } else {
                Location var40 = var1.getLocation();
                EventState var48 = this.state;
                VillagerSetup.spawn(var2, var40, var48, 1024137934);
                int var43 = var3.getAmount() - 1;
                var3.setAmount(var43);
                EventState var29 = this.state;
                var29.clearSoulHolder(var2, 678506716);
                Location var46 = var1.getLocation();
                Sound var50 = Sound.ENTITY_VILLAGER_AMBIENT;
                var1.playSound(var46, var50, 2.0F, 0.8F);
            }
        }
    }

    private void triggerFinalEvent() {
        this.finalTriggerPending = false;
        BlissPlugin var4 = this.plugin;
        EventState var6 = this.state;
        VillagerEventCommand.beginRaid(var4, var6, 1543134889);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        Player var5 = var1.getPlayer();
        boolean var6 = false;

        for (int var7 = 1; var7 <= 3; var7++) {
            String var13 = var5.getName();
            EventState var24 = this.state;
            String var25 = var24.soulHolder(var7, 745178346);
            if (var13.equals(var25)) {
                EventState var16 = this.state;
                Location var29 = var5.getLocation();
                var16.setSoulLastPos(var7, var29, 1364685242);
                EventState var18 = this.state;
                var18.clearSoulHolder(var7, 678506716);
                var6 = true;
            }
        }

        if (var6) {
            this.state.save(1112893273);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        BukkitScheduler var2 = Bukkit.getScheduler();
        BlissPlugin var7 = this.plugin;
        Runnable var8 = () -> {
            if (this.state.isEventRunning(527098672)) {
                Player var4 = var1.getPlayer();
                if (var4.isOnline()) {
                    this.scanInventoryForSouls(var4, 31960603);
                }
            }
        };
        var2.runTaskLater(var7, var8, 20L);
    }

    private void backgroundScan() {
        if (this.state.isEventRunning(527098672)) {
            if (!this.finalTriggerPending
                && this.state.isActive(2119994266)
                && !this.state.isFinalActive(245445441)
                && this.state.villagersReturned(1094152772) >= 3) {
                BlissPlugin var19 = this.plugin;
                EventState var29 = this.state;
                VillagerEventCommand.beginRaid(var19, var29, 1543134889);
            } else {
                for (Player var4 : Bukkit.getOnlinePlayers()) {
                    this.scanInventoryForSouls(var4, 31960603);
                }
            }
        }
    }

    private void scanInventoryForSouls(Player var1, int var2) {
        for (ItemStack var9 : var1.getInventory().getContents()) {
            BlissItems var20 = this.items;
            int var10 = var20.soulIdOf(var9, 1386206139);
            if (var10 != -1) {
                String var24 = var1.getName();
                EventState var34 = this.state;
                String var35 = var34.soulHolder(var10, 745178346);
                if (!var24.equals(var35)) {
                    EventState var27 = this.state;
                    String var39 = var1.getName();
                    var27.setSoulHolder(var10, var39, 1372578011);
                    EventState var29 = this.state;
                    var29.clearSoulLastPos(var10, 2020548249);
                }
            }
        }
    }
}
