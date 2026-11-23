package com.bliss.villager;

import com.bliss.BlissPlugin;
import java.util.UUID;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;

public final class VillagerListener implements Listener {
    private final BlissPlugin plugin;
    private final EventState state;
    private final BlissItems items;
    private static final double COMPASS_GLOW_RADIUS = 40.0;

    public VillagerListener(BlissPlugin var1, EventState var2, BlissItems var3, int var4) {
        this.plugin = var1;
        this.state = var2;
        this.items = var3;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent var1) {
        if (this.state.isEventRunning(527098672)) {
            Player var5 = var1.getEntity();

            for (int var6 = 1; var6 <= 3; var6++) {
                String var14 = var5.getName();
                EventState var21 = this.state;
                String var22 = var21.soulHolder(var6, 745178346);
                if (var14.equals(var22)) {
                    EventState var17 = this.state;
                    var17.clearSoulHolder(var6, 678506716);
                    EventState var19 = this.state;
                    Location var26 = var5.getLocation();
                    var19.setSoulLastPos(var6, var26, 1364685242);
                }
            }
        }
    }

    @EventHandler
    public void onVillagerDeath(EntityDeathEvent var1) {
        if (this.state.isEventRunning(527098672)) {
            if (var1.getEntity() instanceof ZombieVillager var92) {
                this.onZombieVillagerDeath(var92, 738245930);
            } else if (var1.getEntity() instanceof Villager var5) {
                UUID var94 = var5.getUniqueId();
                EventState var26 = this.state;
                if (!var26.isSoulDropped(var94, 1066122989)) {
                    for (int var7 = 1; var7 <= 3; var7++) {
                        EventState var31 = this.state;
                        if (var31.villagerUuid(var7, 1186869288) != null) {
                            EventState var34 = this.state;
                            if (var34.isVillagerAlive(var7, 2034788216)) {
                                EventState var72 = this.state;
                                UUID var73 = var72.villagerUuid(var7, 1186869288);
                                if (var94.equals(var73)) {
                                    this.dropSoul(var7, var5, 1914611203);
                                    return;
                                }
                            }
                        }
                    }

                    if (var5.customName() != null) {
                        if (var5.getScoreboardTags().contains("BlissEventVillager")) {
                            PlainTextComponentSerializer var44 = PlainTextComponentSerializer.plainText();
                            Component var77 = var5.customName();
                            String var95 = var44.serialize(var77);

                            for (int var8 = 1; var8 <= 3; var8++) {
                                EventState var49 = this.state;
                                if (var49.isVillagerAlive(var8, 2034788216)) {
                                    EventState var52 = this.state;
                                    UUID var9 = var52.villagerUuid(var8, 1186869288);
                                    if (var9 == null || Bukkit.getEntity(var9) == null) {
                                        PlainTextComponentSerializer var57 = PlainTextComponentSerializer.plainText();
                                        Component var82 = Cosmetics.villagerName(var8, 527184414);
                                        String var10 = var57.serialize(var82);
                                        if (var95.equals(var10)) {
                                            Logger var63 = this.plugin.getLogger();
                                            String var90 = String.valueOf(var9);
                                            String var85 = "Villager event: soul "
                                                + var8
                                                + " dropped via name fallback (registered entity "
                                                + var90
                                                + " was replaced or missing).";
                                            var63.info(var85);
                                            this.dropSoul(var8, var5, 1914611203);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void dropSoul(int var1, Villager var2, int var3) {
        EventState var9 = this.state;
        UUID var28 = var2.getUniqueId();
        var9.lockSoulDrop(var28, 1603042612);
        EventState var11 = this.state;
        var11.setVillagerAlive(var1, false, 1445271449);
        EventState var13 = this.state;
        var13.markVillagerKilled(var1, 1192413300);
        EventState var15 = this.state;
        Location var39 = var2.getLocation();
        var15.setVillagerLoc(var1, var39, 1133457113);
        EventState var17 = this.state;
        var17.clearVillager(var1, 1097126249);
        World var19 = var2.getWorld();
        Location var34 = var2.getLocation();
        BlissItems var41 = this.items;
        ItemStack var42 = var41.soul(var1, 888486940);
        var19.dropItemNaturally(var34, var42);
        World var22 = var2.getWorld();
        Location var36 = var2.getLocation();
        Sound var43 = Sound.ENTITY_WITHER_SPAWN;
        var22.playSound(var36, var43, 0.5F, 2.0F);
        EventState var24 = this.state;
        Location var45 = var2.getLocation();
        var24.setSoulLastPos(var1, var45, 1364685242);
        this.state.save(1112893273);
        this.checkRaidLost(1609663332);
    }

    private void checkRaidLost(int var1) {
        if (this.state.isFinalActive(245445441)) {
            for (int var4 = 1; var4 <= 3; var4++) {
                EventState var10 = this.state;
                if (var10.isVillagerAlive(var4, 2034788216)) {
                    return;
                }
            }

            BlissPlugin var13 = this.plugin;
            EventState var16 = this.state;
            VillagerEventCommand.failRaid(var13, var16, 126871405);
        }
    }

    private void onZombieVillagerDeath(ZombieVillager var1, int var2) {
        for (int var9 = 1; var9 <= 3; var9++) {
            EventState var13 = this.state;
            if (var13.isVillagerAlive(var9, 2034788216)) {
                EventState var16 = this.state;
                if (var16.villagerUuid(var9, 1186869288) != null) {
                    EventState var19 = this.state;
                    Location var10 = var19.villagerLoc(var9, 1908420537);
                    if (var10 != null && var10.getWorld() != null) {
                        World var25 = var10.getWorld();
                        World var53 = var1.getWorld();
                        if (var25.equals(var53)) {
                            Location var55 = var1.getLocation();
                            if (var10.distance(var55) <= 5.0) {
                                EventState var32 = this.state;
                                var32.setVillagerAlive(var9, false, 1445271449);
                                EventState var34 = this.state;
                                var34.markVillagerKilled(var9, 1192413300);
                                EventState var36 = this.state;
                                Location var67 = var1.getLocation();
                                var36.setVillagerLoc(var9, var67, 1133457113);
                                EventState var38 = this.state;
                                var38.clearVillager(var9, 1097126249);
                                World var40 = var1.getWorld();
                                Location var61 = var1.getLocation();
                                BlissItems var69 = this.items;
                                ItemStack var70 = var69.soul(var9, 888486940);
                                var40.dropItemNaturally(var61, var70);
                                World var43 = var1.getWorld();
                                Location var63 = var1.getLocation();
                                Sound var71 = Sound.ENTITY_WITHER_SPAWN;
                                var43.playSound(var63, var71, 0.5F, 2.0F);
                                EventState var45 = this.state;
                                Location var73 = var1.getLocation();
                                var45.setSoulLastPos(var9, var73, 1364685242);
                                this.state.save(1112893273);
                                this.checkRaidLost(1609663332);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onVillagerTransform(EntityTransformEvent var1) {
        if (this.state.isEventRunning(527098672)) {
            if (var1.getEntity() instanceof Villager var4) {
                if (this.isEventVillager(var4, 899575226)) {
                    var1.setCancelled(true);
                }
            }
        }
    }

    private boolean isEventVillager(Villager var1, int var2) {
        if (var1.getScoreboardTags().contains("BlissEventVillager")) {
            return true;
        } else {
            for (int var5 = 1; var5 <= 3; var5++) {
                EventState var14 = this.state;
                UUID var6 = var14.villagerUuid(var5, 1186869288);
                if (var6 != null) {
                    UUID var23 = var1.getUniqueId();
                    if (var6.equals(var23)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    @EventHandler
    public void onVillagerDamage(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Villager var4) {
            for (int var28 = 1; var28 <= 3; var28++) {
                EventState var15 = this.state;
                UUID var6 = var15.villagerUuid(var28, 1186869288);
                if (var6 != null) {
                    UUID var26 = var4.getUniqueId();
                    if (var6.equals(var26)) {
                        if (!this.state.isEventRunning(527098672)) {
                            var1.setCancelled(true);
                        }

                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMerchantOpen(InventoryOpenEvent var1) {
        if (this.state.isEventRunning(527098672)) {
            if (var1.getPlayer() instanceof Player var5) {
                Inventory var7 = var1.getInventory();
                if (var7 instanceof MerchantInventory) {
                    if (((MerchantInventory)var7).getMerchant() instanceof Villager var46) {
                        for (int var47 = 1; var47 <= 3; var47++) {
                            EventState var33 = this.state;
                            UUID var9 = var33.villagerUuid(var47, 1186869288);
                            if (var9 != null) {
                                UUID var42 = var46.getUniqueId();
                                if (var9.equals(var42)) {
                                    EventState var39 = this.state;
                                    UUID var44 = var5.getUniqueId();
                                    var39.setTradingWith(var44, var47, 591324095);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent var1) {
        EventState var4 = this.state;
        UUID var6 = var1.getPlayer().getUniqueId();
        var4.clearTradingWith(var6, 13643297);
    }

    private static void protectSoulItem(Item var0, int var1) {
        var0.setWillAge(false);
        var0.setInvulnerable(true);
    }

    @EventHandler
    public void onSoulItemSpawn(ItemSpawnEvent var1) {
        BlissItems var4 = this.items;
        ItemStack var16 = var1.getEntity().getItemStack();
        if (var4.soulIdOf(var16, 1386206139) > 0) {
            protectSoulItem(var1.getEntity(), 1788737149);
            if (this.state.isEventRunning(527098672)) {
                Location var19 = var1.getEntity().getLocation();
                if (this.nearCompass(var19, 1089844234)) {
                    var1.getEntity().setGlowing(true);
                }
            }
        }
    }

    private boolean nearCompass(Location var1, int var2) {
        Location var7 = this.state.compassLoc(561536293);
        if (var7 != null && var7.getWorld() != null && var1.getWorld() != null) {
            World var16 = var7.getWorld();
            World var25 = var1.getWorld();
            if (!var16.equals(var25)) {
                return false;
            } else {
                boolean var23;
                if (var7.distanceSquared(var1) <= 1600.0) {
                    var23 = true;
                } else {
                    var23 = false;
                }

                return var23;
            }
        } else {
            return false;
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onSoulItemDespawn(ItemDespawnEvent var1) {
        BlissItems var4 = this.items;
        ItemStack var10 = var1.getEntity().getItemStack();
        if (var4.soulIdOf(var10, 1386206139) > 0) {
            var1.setCancelled(true);
            protectSoulItem(var1.getEntity(), 1788737149);
        }
    }

    @EventHandler(
        priority = EventPriority.LOWEST
    )
    public void onSoulItemDamaged(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Item var4) {
            DamageCause var12 = var1.getCause();
            DamageCause var3 = DamageCause.VOID;
            if (var12 != var3) {
                BlissItems var14 = this.items;
                ItemStack var20 = var4.getItemStack();
                if (var14.soulIdOf(var20, 1386206139) > 0) {
                    var1.setCancelled(true);
                    protectSoulItem(var4, 1788737149);
                    var4.setFireTicks(0);
                }
            }
        }
    }

    @EventHandler
    public void onSoulBundleClick(InventoryClickEvent var1) {
        label20: {
            ItemStack var4 = var1.getCursor();
            ItemStack var5 = var1.getCurrentItem();
            if (isBundle(var4, 1616796712)) {
                BlissItems var12 = this.items;
                if (var12.soulIdOf(var5, 1386206139) > 0) {
                    break label20;
                }
            }

            if (!isBundle(var5, 1616796712)) {
                return;
            }

            BlissItems var17 = this.items;
            if (var17.soulIdOf(var4, 1386206139) <= 0) {
                return;
            }
        }

        var1.setCancelled(true);
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
}
