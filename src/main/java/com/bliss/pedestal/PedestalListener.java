package com.bliss.pedestal;

import com.bliss.BlissPlugin;
import com.bliss.core.AchievementStore;
import com.bliss.core.GemType;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGemManager;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public final class PedestalListener implements Listener {
    private static final double PROTECT_RADIUS = 1.8;
    private static final double PILLAR_RADIUS = 2.0;
    private static final double DROP_RADIUS = 2.0;
    private static final double DEPOSIT_RADIUS = 8.0;
    private static final double BEACON_HIT_RADIUS = 1.3;
    private static final int ENERGY_THROW_TIMEOUT = 100;
    private final BlissPlugin plugin;
    private final PedestalState state;
    private final BlissCoreItems items;
    private final PlayerDataStore players;

    public PedestalListener(BlissPlugin var1, PedestalState var2, BlissCoreItems var3, PlayerDataStore var4, int var5) {
        this.plugin = var1;
        this.state = var2;
        this.items = var3;
        this.players = var4;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent var1) {
        label86: {
            Location var11 = var1.getBlock().getLocation();
            if (this.state.mainLoc(15453813) != null) {
                Location var96 = this.state.mainLoc(15453813);
                if (this.near(var11, var96, 10.0, 1588631092)) {
                    Material var12 = var1.getBlock().getType();
                    Material var72 = Material.PISTON;
                    if (var12 == var72) {
                        break label86;
                    }

                    Material var73 = Material.STICKY_PISTON;
                    if (var12 == var73) {
                        break label86;
                    }

                    Material var74 = Material.TNT;
                    if (var12 == var74) {
                        break label86;
                    }

                    Material var75 = Material.RAIL;
                    if (var12 == var75) {
                        break label86;
                    }

                    Material var76 = Material.POWERED_RAIL;
                    if (var12 == var76) {
                        break label86;
                    }

                    Material var77 = Material.DETECTOR_RAIL;
                    if (var12 == var77) {
                        break label86;
                    }

                    Material var78 = Material.ACTIVATOR_RAIL;
                    if (var12 == var78) {
                        break label86;
                    }
                }
            }

            boolean var71;
            label66: {
                if (!this.isPedestalBlock(var11, 319288900)) {
                    Location var83 = var11.clone().add(0.0, -1.0, 0.0);
                    if (!this.isPedestalBlock(var83, 319288900)) {
                        var71 = false;
                        break label66;
                    }
                }

                var71 = true;
            }

            boolean var45;
            label61: {
                Material var39 = var1.getBlock().getType();
                Material var84 = Material.BEACON;
                if (var39 == var84 && this.state.mainLoc(15453813) != null) {
                    Location var101 = this.state.mainLoc(15453813);
                    if (this.near(var11, var101, 1.8, 1588631092)) {
                        var45 = true;
                        break label61;
                    }
                }

                var45 = false;
            }

            if (var71 && !var45) {
                var1.setCancelled(true);
                return;
            }

            if (this.state.mainLoc(15453813) != null) {
                Location var104 = this.state.mainLoc(15453813);
                if (this.near(var11, var104, 1.8, 1588631092)) {
                    Material var56 = var1.getBlock().getType();
                    Material var88 = Material.BEACON;
                    if (var56 != var88) {
                        var1.setCancelled(true);
                        return;
                    }

                    if (this.state.beacon(313684215)) {
                        var1.setCancelled(true);
                        return;
                    }

                    if (!this.state.active(80985509)) {
                        var1.setCancelled(true);
                        return;
                    }

                    Player var67 = var1.getPlayer();
                    Component var92 = Text.legacy("&f You Have Placed &c The Beacon", 373192400);
                    var67.sendMessage(var92);
                    this.state.setBeacon(true, 116258295);
                    return;
                }
            }

            return;
        }

        var1.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent var1) {
        Location var8;
        boolean var21;
        label58: {
            var8 = var1.getBlock().getLocation();
            if (this.state.mainLoc(15453813) != null) {
                Location var68 = this.state.mainLoc(15453813);
                if (this.near(var8, var68, 0.8, 1588631092)) {
                    var21 = true;
                    break label58;
                }
            }

            var21 = false;
        }

        Location var10 = this.state.ancientDebrisLoc(1617501032);
        boolean var28;
        if (var10 != null && this.near(var8, var10, 0.8, 1588631092)) {
            var28 = true;
        } else {
            var28 = false;
        }

        if (!var21 && !var28) {
            if (this.nearAnyPillar(var8, 1500642339)) {
                var1.setCancelled(true);
                var1.getPlayer().damage(0.001);
            }
        } else {
            var1.setCancelled(true);
            GameMode var40 = var1.getPlayer().getGameMode();
            GameMode var60 = GameMode.CREATIVE;
            if (var40 != var60) {
                PedestalState.Ritual var43 = this.state.ritual(1751702735);
                PedestalState.Ritual var61 = PedestalState.Ritual.NONE;
                if (var43 != var61) {
                    if (var28) {
                        String var66 = "&4Ancient debris tampered with — ritual terminated.";
                        this.terminateRitual(var66, 634095804);
                    } else {
                        this.state.decrementDurability(804091144);
                        int var13 = this.state.durability(919222756);
                        if (var13 <= 0) {
                            String var62 = "&4Pedestal beacon broken 3 times — ritual terminated.";
                            this.terminateRitual(var62, 634095804);
                        } else {
                            Location var65 = this.state.mainLoc(15453813);
                            String var74 = "&cPedestal break attempt, durability is " + var13 + "/3";
                            this.broadcastNear(var65, 8.0, var74, 918360238);
                        }
                    }
                }
            }
        }
    }

    private void terminateRitual(String var1, int var2) {
        Location var44 = this.state.mainLoc(15453813);
        this.broadcastNear(var44, 8.0, var1, 918360238);
        this.state.resetRitual(1772872156);
        this.state.setActive(false, 1028669057);
        PedestalReviveRitual.restoreWeather(781471445);
        Location var10 = this.state.mainLoc(15453813);
        if (var10 != null && var10.getWorld() != null) {
            World var25 = var10.getWorld();

            for (Entity var12 : var25.getNearbyEntities(var10, 500.0, 500.0, 500.0)) {
                if (var12 instanceof EnderCrystal) {
                    EnderCrystal var13 = (EnderCrystal)var12;
                    if (var13.getScoreboardTags().contains("BlissPedestalCrystal")) {
                        var13.remove();
                    }
                }
            }
        }

        BukkitRunnable var41 = new BukkitRunnable() {
            public void run() {
                PedestalListener.this.state.unbuildStructure(280713799);
                PedestalListener.this.state.save(973788971);
            }
        };
        BlissPlugin var49 = this.plugin;
        var41.runTaskLater(var49, 1L);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent var1) {
        Player var7 = var1.getPlayer();
        if (this.state.mainLoc(15453813) != null) {
            Location var34 = var7.getLocation();
            Location var44 = this.state.mainLoc(15453813);
            if (this.near(var34, var44, 5.0, 1588631092)) {
                Item var8 = var1.getItemDrop();
                ItemStack var9 = var8.getItemStack();
                BlissCoreItems var21 = this.items;
                if (var21.isRepairKit(var9, 58420938)) {
                    this.startRepair(var7, var8, 1867795466);
                } else {
                    BlissCoreItems var24 = this.items;
                    if (var24.isRestoration(var9, 300938899)) {
                        this.startRestoration(var7, var8, 785249200);
                    } else {
                        BlissCoreItems var27 = this.items;
                        if (var27.isBottledEnergy(var9, 1793320558)) {
                            Location var39 = var7.getLocation();
                            Location var47 = this.state.mainLoc(15453813);
                            if (this.near(var39, var47, 8.0, 1588631092)) {
                                this.trackEnergyThrow(var7, var8, 1864207385);
                            }
                        }
                    }
                }
            }
        }
    }

    private void trackEnergyThrow(final Player var1, final Item var2, int var3) {
        Location var13 = this.state.mainLoc(15453813);
        if (var13 != null) {
            final Location var14 = var13.clone().add(0.5, 0.5, 0.5);
            BukkitRunnable var21 = new BukkitRunnable() {
                int ticks = 0;

                public void run() {
                    if (!var2.isDead() && var2.isValid() && PedestalListener.this.state.mainLoc(15453813) != null) {
                        Location var5 = var2.getLocation();
                        World var19 = var5.getWorld();
                        World var32 = var14.getWorld();
                        if (var19.equals(var32)) {
                            Location var34 = var14;
                            if (var5.distanceSquared(var34) <= 1.6900000000000002) {
                                this.cancel();
                                PedestalListener var27 = PedestalListener.this;
                                Player var36 = var1;
                                Item var41 = var2;
                                var27.depositEnergy(var36, var41, 1458620843);
                                return;
                            }
                        }

                        int var38 = this.ticks + 1;
                        this.ticks = var38;
                        if (var38 >= 100) {
                            this.cancel();
                        }
                    } else {
                        this.cancel();
                    }
                }
            };
            BlissPlugin var24 = this.plugin;
            var21.runTaskTimer(var24, 1L, 1L);
        }
    }

    private boolean pedestalDeactivatedByDecree(int var1) {
        boolean var9;
        if (this.plugin.kitRules(1205957746) != null && this.plugin.kitRules(1205957746).pedestalDeactivated(1391354599)) {
            var9 = true;
        } else {
            var9 = false;
        }

        return var9;
    }

    private void startRepair(Player var1, Item var2, int var3) {
        if (this.pedestalDeactivatedByDecree(1567044325)) {
            Component var36 = Text.legacy("&cThe pedestal has been deactivated by the Honored One.", 373192400);
            var1.sendMessage(var36);
        } else if (!this.state.active(80985509)) {
            Component var51 = Text.legacy("&cPedestal is not active.", 373192400);
            var1.sendMessage(var51);
        } else {
            PedestalState.Ritual var16 = this.state.ritual(1751702735);
            PedestalState.Ritual var37 = PedestalState.Ritual.NONE;
            if (var16 != var37) {
                Component var49 = Text.legacy("&cAnother ritual is already in progress.", 373192400);
                var1.sendMessage(var49);
            } else {
                PedestalState var18 = this.state;
                PedestalState.Ritual var38 = PedestalState.Ritual.REPAIR;
                var18.setRitual(var38, 779480626);
                this.state.resetCount(406144441);
                this.state.setDepositCooldown(0, 174029610);
                this.state.setDurability(3, 257734481);
                takeOne(var2, 862953476);
                World var29 = this.state.mainLoc(15453813).getWorld();
                Location var43 = this.state.mainLoc(15453813);
                Sound var6 = Sound.BLOCK_BEACON_ACTIVATE;
                var29.playSound(var43, var6, 30.0F, 1.0F);
                Server var32 = this.plugin.getServer();
                Component var47 = Text.legacy("&d" + var1.getName() + " has started a repair ritual!", 373192400);
                var32.broadcast(var47);
            }
        }
    }

    private void startRestoration(Player var1, Item var2, int var3) {
        if (this.pedestalDeactivatedByDecree(1567044325)) {
            Component var60 = Text.legacy("&cThe pedestal has been deactivated by the Honored One.", 373192400);
            var1.sendMessage(var60);
        } else if (!this.state.active(80985509)) {
            Component var92 = Text.legacy("&cPedestal is not active.", 373192400);
            var1.sendMessage(var92);
        } else {
            PedestalState.Ritual var17 = this.state.ritual(1751702735);
            PedestalState.Ritual var61 = PedestalState.Ritual.NONE;
            if (var17 != var61) {
                Component var90 = Text.legacy("&cAnother ritual is already in progress.", 373192400);
                var1.sendMessage(var90);
            } else {
                GoldGemManager var20 = this.plugin.goldGemManager(256518991);
                UUID var63 = var1.getUniqueId();
                if (var20.isRepurposed(var63, 1313846040)) {
                    PlayerDataStore var24 = this.plugin.players(1969902410);
                    UUID var65 = var1.getUniqueId();
                    GemType var26 = var24.get(var65, 1264010596).gem(1413186337);
                    GemType var66 = GemType.NONE;
                    if (var26 == var66) {
                        Component var88 = Text.legacy("&cYour soul has been repurposed. The pedestal has nothing left to restore.", 373192400);
                        var1.sendMessage(var88);
                        return;
                    }
                }

                if (JuggernautGemGuardListener.isJuggernaut(var1, 859003337)) {
                    Component var68 = Text.legacy("&cThe bond leaves nothing for the pedestal to restore.", 373192400);
                    var1.sendMessage(var68);
                } else {
                    PlayerDataStore var31 = this.players;
                    UUID var70 = var1.getUniqueId();
                    PlayerData var9 = var31.get(var70, 1264010596);
                    if (var9.energy(1160853318) != 1) {
                        Component var75 = Text.legacy("&cRestoration requires exactly 1 energy. You have " + var9.energy(1160853318) + ".", 373192400);
                        var1.sendMessage(var75);
                    } else {
                        PedestalState var37 = this.state;
                        PedestalState.Ritual var76 = PedestalState.Ritual.REVIVE;
                        var37.setRitual(var76, 779480626);
                        PedestalState var39 = this.state;
                        UUID var78 = var1.getUniqueId();
                        var39.setRevivingPlayer(var78, 159973914);
                        this.state.resetCount(406144441);
                        this.state.setDurability(3, 257734481);
                        takeOne(var2, 862953476);
                        PedestalReviveRitual.forceStormAndThunder(this.state.mainLoc(15453813).getWorld(), 1382490695);
                        World var52 = this.state.mainLoc(15453813).getWorld();
                        Location var82 = this.state.mainLoc(15453813);
                        Sound var6 = Sound.BLOCK_BEACON_ACTIVATE;
                        var52.playSound(var82, var6, 30.0F, 1.0F);
                        Server var55 = this.plugin.getServer();
                        Component var86 = Text.legacy("&d" + var1.getName() + " has started a restoration ritual!", 373192400);
                        var55.broadcast(var86);
                    }
                }
            }
        }
    }

    private void depositEnergy(Player var1, Item var2, int var3) {
        PedestalState.Ritual var11 = this.state.ritual(1751702735);
        PedestalState.Ritual var5 = PedestalState.Ritual.NONE;
        if (var11 == var5) {
            Component var62 = Text.legacy("&cNo ritual active. Drop a Repair Kit (or a Restoration with 1 energy) to start one.", 373192400);
            var1.sendMessage(var62);
        } else if (this.state.count(629418704) >= 5) {
            Location var66 = this.state.mainLoc(15453813);
            String var9 = "<##FFD773>Energy deposit requirement has been fulfilled!";
            this.broadcastNear(var66, 8.0, var9, 918360238);
        } else if (this.state.depositCooldown(51099791) != 0) {
            Component var102 = Text.legacy("&cPedestal cooldown: " + this.state.depositCooldown(51099791) + "s", 373192400);
            var1.sendMessage(var102);
        } else {
            World var23 = this.state.mainLoc(15453813).getWorld();
            Location var69 = this.state.mainLoc(15453813);
            Sound var103 = Sound.BLOCK_BEACON_AMBIENT;
            var23.playSound(var69, var103, 40.0F, 1.6F);
            takeOne(var2, 862953476);
            World var28 = this.state.mainLoc(15453813).getWorld();
            Location var72 = this.state.mainLoc(15453813);
            Sound var104 = Sound.ITEM_AXE_SCRAPE;
            var28.playSound(var72, var104, 10.0F, 0.5F);
            World var32 = this.state.mainLoc(15453813).getWorld();
            Location var75 = this.state.mainLoc(15453813);
            Sound var105 = Sound.BLOCK_AMETHYST_BLOCK_BREAK;
            var32.playSound(var75, var105, 20.0F, 1.2F);
            this.state.incrementCount(162866538);
            PedestalState.Ritual var37 = this.state.ritual(1751702735);
            PedestalState.Ritual var76 = PedestalState.Ritual.REPAIR;
            if (var37 == var76) {
                AchievementStore var40 = this.plugin.advancements(1975441698);
                AchievementStore.Advancement var106 = AchievementStore.Advancement.GOOD_AS_NEW;
                var40.progress(var1, var106, 1L, 223202830);
            }

            PedestalState var42 = this.state;
            byte var97;
            if (var1.isOp()) {
                var97 = 2;
            } else {
                var97 = 30;
            }

            var42.setDepositCooldown(var97, 174029610);
            if (this.state.count(629418704) >= 5) {
                Location var89 = this.state.mainLoc(15453813);
                String var131 = "<##FFD773>Energy deposit requirement has been fulfilled!";
                this.broadcastNear(var89, 8.0, var131, 918360238);
                this.state.summonBeam(1156554300);
                PedestalState.Ritual var53 = this.state.ritual(1751702735);
                PedestalState.Ritual var90 = PedestalState.Ritual.REPAIR;
                if (var53 == var90) {
                    BlissPlugin var55 = this.plugin;
                    PedestalState var92 = this.state;
                    PlayerDataStore var114 = this.players;
                    BlissCoreItems var122 = this.items;
                    PedestalRitualRunner.complete(var55, var92, var114, var122, 27651565);
                } else {
                    PedestalState.Ritual var58 = this.state.ritual(1751702735);
                    PedestalState.Ritual var93 = PedestalState.Ritual.REVIVE;
                    if (var58 == var93) {
                        Location var96 = this.state.mainLoc(15453813);
                        String var132 = "<##96FFD9>Please stand on the pedestal center!";
                        this.broadcastNear(var96, 8.0, var132, 918360238);
                    }
                }
            } else {
                Location var83 = this.state.mainLoc(15453813);
                String var129 = "<##96FFD9>" + this.state.count(629418704) + "/5 energy deposited!";
                this.broadcastNear(var83, 8.0, var129, 918360238);
                Location var86 = this.state.mainLoc(15453813);
                String var130 = "&cPlease wait for pedestal cooldown...";
                this.broadcastNear(var86, 8.0, var130, 918360238);
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onEntityExplode(EntityExplodeEvent var1) {
        List<Block> var4 = var1.blockList();
        Predicate<Block> var6 = var1x -> {
            Location var5 = var1x.getLocation();
            return this.isPedestalBlock(var5, 319288900);
        };
        var4.removeIf(var6);
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onBlockExplode(BlockExplodeEvent var1) {
        List<Block> var4 = var1.blockList();
        Predicate<Block> var6 = var1x -> {
            Location var5 = var1x.getLocation();
            return this.isPedestalBlock(var5, 319288900);
        };
        var4.removeIf(var6);
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPistonExtend(BlockPistonExtendEvent var1) {
        Iterator var5 = var1.getBlocks().iterator();

        Location var22;
        do {
            if (!var5.hasNext()) {
                return;
            }

            Block var6 = (Block)var5.next();
            Location var19 = var6.getLocation();
            if (this.isPedestalBlock(var19, 319288900)) {
                break;
            }

            BlockFace var24 = var1.getDirection();
            var22 = var6.getRelative(var24).getLocation();
        } while (!this.isPedestalBlock(var22, 319288900));

        var1.setCancelled(true);
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPistonRetract(BlockPistonRetractEvent var1) {
        Iterator var5 = var1.getBlocks().iterator();

        Location var22;
        do {
            if (!var5.hasNext()) {
                return;
            }

            Block var6 = (Block)var5.next();
            Location var19 = var6.getLocation();
            if (this.isPedestalBlock(var19, 319288900)) {
                break;
            }

            BlockFace var24 = var1.getDirection();
            var22 = var6.getRelative(var24).getLocation();
        } while (!this.isPedestalBlock(var22, 319288900));

        var1.setCancelled(true);
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPedestalBurn(BlockBurnEvent var1) {
        Location var7 = var1.getBlock().getLocation();
        if (this.isPedestalBlock(var7, 319288900)) {
            var1.setCancelled(true);
        }
    }

    private static void takeOne(Item var0, int var1) {
        ItemStack var5 = var0.getItemStack();
        if (var5.getAmount() > 1) {
            int var14 = var5.getAmount() - 1;
            var5.setAmount(var14);
            var0.setItemStack(var5);
        } else {
            var0.remove();
        }
    }

    private boolean nearAnyPillar(Location var1, int var2) {
        PedestalState var11 = this.state;
        if (var11.isCustomLayoutBlock(var1, 483158695)) {
            return true;
        } else {
            if (this.state.subLoc(135424274) != null) {
                Location var44 = this.state.subLoc(135424274);
                if (this.near(var1, var44, 2.0, 1588631092)) {
                    return true;
                }
            }

            for (int var8 = 1; var8 <= 4; var8++) {
                PedestalState var23 = this.state;
                Location var9 = var23.gilded(var8, 544076823);
                if (var9 != null && this.near(var1, var9, 2.0, 1588631092)) {
                    return true;
                }

                PedestalState var29 = this.state;
                Location var10 = var29.gold(var8, 678978195);
                if (var10 != null && this.near(var1, var10, 2.0, 1588631092)) {
                    return true;
                }
            }

            return false;
        }
    }

    private boolean isPedestalBlock(Location var1, int var2) {
        if (var1 != null && this.state.mainLoc(15453813) != null) {
            World var17 = var1.getWorld();
            World var53 = this.state.mainLoc(15453813).getWorld();
            if (!var17.equals(var53)) {
                return false;
            } else {
                PedestalState var21 = this.state;
                if (var21.isCustomLayoutBlock(var1, 483158695)) {
                    return true;
                } else {
                    Location var65 = this.state.mainLoc(15453813);
                    if (this.sameBlock(var1, var65, 1775853020)) {
                        return true;
                    } else {
                        Location var70 = this.state.mainLoc(15453813).clone().add(0.0, -1.0, 0.0);
                        if (this.sameBlock(var1, var70, 1775853020)) {
                            return true;
                        } else {
                            Location var73 = this.state.subLoc(135424274);
                            if (this.sameBlock(var1, var73, 1775853020)) {
                                return true;
                            } else {
                                for (int var12 = 1; var12 <= 4; var12++) {
                                    PedestalState var75 = this.state;
                                    Location var76 = var75.gilded(var12, 544076823);
                                    if (this.sameBlock(var1, var76, 1775853020)) {
                                        return true;
                                    }

                                    PedestalState var78 = this.state;
                                    Location var79 = var78.gold(var12, 678978195);
                                    if (this.sameBlock(var1, var79, 1775853020)) {
                                        return true;
                                    }

                                    PedestalState var81 = this.state;
                                    Location var82 = var81.cherry(var12, 1778384962);
                                    if (this.sameBlock(var1, var82, 1775853020)) {
                                        return true;
                                    }
                                }

                                for (int var90 = 1; var90 <= 8; var90++) {
                                    PedestalState var84 = this.state;
                                    Location var85 = var84.rawGold(var90, 2076901304);
                                    if (this.sameBlock(var1, var85, 1775853020)) {
                                        return true;
                                    }
                                }

                                return false;
                            }
                        }
                    }
                }
            }
        } else {
            return false;
        }
    }

    private boolean sameBlock(Location var1, Location var2, int var3) {
        if (var1 != null && var2 != null && var1.getWorld() != null && var2.getWorld() != null) {
            World var12 = var1.getWorld();
            World var23 = var2.getWorld();
            if (!var12.equals(var23)) {
                return false;
            } else {
                int var16 = var1.getBlockX();
                int var25 = var2.getBlockX();
                if (var16 == var25) {
                    int var18 = var1.getBlockY();
                    int var27 = var2.getBlockY();
                    if (var18 == var27) {
                        int var20 = var1.getBlockZ();
                        int var29 = var2.getBlockZ();
                        if (var20 == var29) {
                            return true;
                        }
                    }
                }

                return false;
            }
        } else {
            return false;
        }
    }

    private boolean near(Location var1, Location var2, double var3, int var5) {
        World var12 = var1.getWorld();
        World var20 = var2.getWorld();
        if (!var12.equals(var20)) {
            return false;
        } else {
            double var15 = var1.distanceSquared(var2);
            double var22 = var3 * var3;
            boolean var18;
            if (var15 <= var22) {
                var18 = true;
            } else {
                var18 = false;
            }

            return var18;
        }
    }

    private void broadcastNear(Location var1, double var2, String var4, int var5) {
        for (Player var13 : var1.getWorld().getPlayers()) {
            World var23 = var13.getWorld();
            World var31 = var1.getWorld();
            if (var23.equals(var31)) {
                Location var26 = var13.getLocation();
                double var27 = var26.distanceSquared(var1);
                double var35 = var2 * var2;
                if (var27 <= var35) {
                    Component var34 = Text.legacy(var4, 373192400);
                    var13.sendMessage(var34);
                }
            }
        }
    }
}
