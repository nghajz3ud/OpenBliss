package com.bliss.environment;

import com.bliss.BlissPlugin;
import com.bliss.core.GlobalState;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

public final class EnvironmentListener implements Listener {
    private final BlissPlugin plugin;
    private final GlobalState state;
    private static final String ENCH_OBSIDIAN_GLOW_TAG = "BlissEnchObsidianGlow";

    public EnvironmentListener(BlissPlugin var1, GlobalState var2, int var3) {
        this.plugin = var1;
        this.state = var2;
    }

    @EventHandler
    public void onDragonEggPickup(EntityPickupItemEvent var1) {
        if (var1.getEntity() instanceof Player var4) {
            Material var14 = var1.getItem().getItemStack().getType();
            Material var3 = Material.DRAGON_EGG;
            if (var14 == var3) {
                GlobalState var16 = this.state;
                UUID var18 = var4.getUniqueId();
                var16.setDragonEggOwner(var18, 1926817658);
            }
        }
    }

    @EventHandler
    public void onDragonEggPlace(BlockPlaceEvent var1) {
        Material var5 = var1.getBlockPlaced().getType();
        Material var3 = Material.DRAGON_EGG;
        if (var5 == var3) {
            this.state.setDragonEggOwner(null, 1926817658);
        }
    }

    @EventHandler
    public void onDragonEggDrop(PlayerDropItemEvent var1) {
        Material var6 = var1.getItemDrop().getItemStack().getType();
        Material var3 = Material.DRAGON_EGG;
        if (var6 == var3) {
            this.state.setDragonEggOwner(null, 1926817658);
        }
    }

    @EventHandler
    public void onObsidianInteract(PlayerInteractEvent var1) {
        if (var1.getAction().isRightClick()) {
            if (GlobalState.forPlayer(var1.getPlayer(), 1433087673).prog1(494064183)) {
                Player var4 = var1.getPlayer();
                ItemStack var5 = var4.getInventory().getItemInMainHand();
                ItemStack var6 = var4.getInventory().getItemInOffHand();
                ItemStack var7 = null;
                Material var26 = var5.getType();
                Material var3 = Material.OBSIDIAN;
                if (var26 == var3) {
                    var7 = var5;
                } else {
                    Material var42 = var6.getType();
                    Material var48 = Material.OBSIDIAN;
                    if (var42 == var48) {
                        var7 = var6;
                    }
                }

                if (var7 != null) {
                    if (!isEnchanted(var7, 72267525)) {
                        Block var8 = var1.getClickedBlock();
                        if (var8 != null) {
                            Material var35 = var8.getType();
                            Material var44 = Material.REINFORCED_DEEPSLATE;
                            if (var35 == var44) {
                                return;
                            }

                            BlockFace var46 = var1.getBlockFace();
                            if (touchesReinforcedDeepslate(var8.getRelative(var46), 467670163)) {
                                return;
                            }
                        }

                        var1.setCancelled(true);
                    }
                }
            }
        }
    }

    private static boolean touchesReinforcedDeepslate(Block var0, int var1) {
        BlockFace[] var9 = new BlockFace[6];
        BlockFace var4 = BlockFace.UP;
        var9[0] = var4;
        BlockFace var30 = BlockFace.DOWN;
        var9[1] = var30;
        BlockFace var31 = BlockFace.NORTH;
        var9[2] = var31;
        BlockFace var32 = BlockFace.SOUTH;
        var9[3] = var32;
        BlockFace var33 = BlockFace.EAST;
        var9[4] = var33;
        BlockFace var34 = BlockFace.WEST;
        var9[5] = var34;

        for (BlockFace var8 : var9) {
            Material var18 = var0.getRelative(var8).getType();
            Material var24 = Material.REINFORCED_DEEPSLATE;
            if (var18 == var24) {
                return true;
            }
        }

        return false;
    }

    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    public void onEnchantedObsidianPlace(BlockPlaceEvent var1) {
        Material var10 = var1.getBlockPlaced().getType();
        Material var3 = Material.OBSIDIAN;
        if (var10 == var3) {
            if (isEnchanted(var1.getItemInHand(), 72267525)) {
                Location var6 = var1.getBlockPlaced().getLocation();
                boolean var7 = this.state.enchObsidianGlow(1536924725);
                World var21 = var6.getWorld();
                Consumer<BlockDisplay> var25 = var1x -> {
                    BlockData var13 = Material.OBSIDIAN.createBlockData();
                    var1x.setBlock(var13);
                    var1x.setGlowing(var7);
                    Color var15 = Color.YELLOW;
                    var1x.setGlowColorOverride(var15);
                    Brightness var16 = new Brightness(15, 15);
                    var1x.setBrightness(var16);
                    var1x.setPersistent(true);
                    var1x.getScoreboardTags().add("BlissEnchObsidianGlow");
                };
                BlockDisplay var10000 = (BlockDisplay)var21.spawn(var6, BlockDisplay.class, var25);
            }
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST,
        ignoreCancelled = true
    )
    public void onObsidianPickup(EntityPickupItemEvent var1) {
        if (GlobalState.forEntity(var1.getEntity(), 601947313).prog1(494064183)) {
            ItemStack var6 = var1.getItem().getItemStack();
            Material var15 = var6.getType();
            Material var3 = Material.OBSIDIAN;
            if (var15 == var3) {
                if (!isEnchanted(var6, 72267525)) {
                    Material var4 = Material.STONE;
                    int var22 = var6.getAmount();
                    ItemStack var18 = new ItemStack(var4, var22);
                    Item var20 = var1.getItem();
                    var20.setItemStack(var18);
                }
            }
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST,
        ignoreCancelled = true
    )
    public void onObsidianBreak(BlockBreakEvent var1) {
        Material var12 = var1.getBlock().getType();
        Material var3 = Material.OBSIDIAN;
        if (var12 == var3) {
            Location var10 = var1.getBlock().getLocation();
            if (findGlowMarker(var10, 1822710185) != null) {
                var1.setDropItems(false);
                GameMode var26 = var1.getPlayer().getGameMode();
                GameMode var39 = GameMode.CREATIVE;
                if (var26 != var39) {
                    World var29 = var1.getBlock().getWorld();
                    Location var42 = var10.clone().add(0.5, 0.5, 0.5);
                    ItemStack var48 = this.plugin.coreItems(401009135).enchantedObsidian(1193659648);
                    var29.dropItemNaturally(var42, var48);
                }

                removeMarkersAt(var10, 499593338);
                BukkitScheduler var35 = this.plugin.getServer().getScheduler();
                BlissPlugin var44 = this.plugin;
                Runnable var50 = () -> removeMarkersAt(var10, 499593338);
                var35.runTask(var44, var50);
            } else if (GlobalState.forPlayer(var1.getPlayer(), 1433087673).prog1(494064183)) {
                var1.setCancelled(true);
            }
        }
    }

    private static void removeMarkersAt(Location var0, int var1) {
        Location var15 = var0.clone().add(0.5, 0.5, 0.5);
        World var22 = var15.getWorld();

        for (Entity var17 : var22.getNearbyEntities(var15, 1.0, 1.0, 1.0)) {
            if (var17 instanceof BlockDisplay) {
                BlockDisplay var18 = (BlockDisplay)var17;
                if (var17.getScoreboardTags().contains("BlissEnchObsidianGlow")) {
                    Location var38 = var17.getLocation();
                    if (sameBlock(var38, var0, 1171586603)) {
                        var18.remove();
                    }
                }
            }
        }
    }

    public void startGhostSweep(int var1) {
        BukkitScheduler var11 = this.plugin.getServer().getScheduler();
        BlissPlugin var13 = this.plugin;
        Runnable var14 = () -> {
            for (World var4 : this.plugin.getServer().getWorlds()) {
                boolean var5 = this.state.enchObsidianGlow(1536924725);

                for (BlockDisplay var7 : var4.getEntitiesByClass(BlockDisplay.class)) {
                    if (var7.getScoreboardTags().contains("BlissEnchObsidianGlow")) {
                        Material var34 = var7.getLocation().getBlock().getType();
                        Material var40 = Material.OBSIDIAN;
                        if (var34 != var40) {
                            var7.remove();
                        } else {
                            boolean var37 = var7.isGlowing();
                            if (var37 != var5) {
                                var7.setGlowing(var5);
                            }
                        }
                    }
                }
            }
        };
        var11.runTaskTimer(var13, var14, 100L, 100L);
    }

    private static BlockDisplay findGlowMarker(Location var0, int var1) {
        Location var15 = var0.clone().add(0.5, 0.5, 0.5);
        World var22 = var15.getWorld();

        for (Entity var17 : var22.getNearbyEntities(var15, 1.0, 1.0, 1.0)) {
            if (var17 instanceof BlockDisplay) {
                BlockDisplay var18 = (BlockDisplay)var17;
                if (var17.getScoreboardTags().contains("BlissEnchObsidianGlow")) {
                    Location var38 = var17.getLocation();
                    if (sameBlock(var38, var0, 1171586603)) {
                        return var18;
                    }
                }
            }
        }

        return null;
    }

    private static boolean sameBlock(Location var0, Location var1, int var2) {
        if (var0.getWorld() != null) {
            World var7 = var0.getWorld();
            World var16 = var1.getWorld();
            if (var7.equals(var16)) {
                int var10 = var0.getBlockX();
                int var18 = var1.getBlockX();
                if (var10 == var18) {
                    int var12 = var0.getBlockY();
                    int var20 = var1.getBlockY();
                    if (var12 == var20) {
                        int var14 = var0.getBlockZ();
                        int var22 = var1.getBlockZ();
                        if (var14 == var22) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @EventHandler
    public void onEntitiesLoad(EntitiesLoadEvent var1) {
        for (Entity var5 : var1.getEntities()) {
            if (var5 instanceof BlockDisplay) {
                BlockDisplay var6 = (BlockDisplay)var5;
                if (var5.getScoreboardTags().contains("BlissEnchObsidianGlow")) {
                    Material var24 = var5.getLocation().getBlock().getType();
                    Material var26 = Material.OBSIDIAN;
                    if (var24 != var26) {
                        var6.remove();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onObsidianForm(BlockFormEvent var1) {
        if (GlobalState.forWorld(var1.getBlock().getWorld(), 728926677).prog1(494064183)) {
            Material var10 = var1.getNewState().getType();
            Material var3 = Material.OBSIDIAN;
            if (var10 == var3) {
                BlockState var12 = var1.getNewState();
                Material var13 = Material.STONE;
                var12.setType(var13);
            }
        }
    }

    @EventHandler
    public void onEnchantedObsidianDrop(PlayerDropItemEvent var1) {
        ItemStack var7 = var1.getItemDrop().getItemStack();
        Material var12 = var7.getType();
        Material var3 = Material.OBSIDIAN;
        if (var12 == var3) {
            if (isEnchanted(var7, 72267525)) {
                Item var8 = var1.getItemDrop();
                BukkitScheduler var20 = this.plugin.getServer().getScheduler();
                BlissPlugin var23 = this.plugin;
                Runnable var24 = () -> {
                    if (var8.isValid()) {
                        var8.setGlowing(true);
                    }
                };
                var20.runTaskLater(var23, var24, 5L);
            }
        }
    }

    private static boolean isEnchanted(ItemStack var0, int var1) {
        Enchantment var3 = Enchantment.MENDING;
        boolean var10;
        if (var0.getEnchantmentLevel(var3) <= 0 && (!var0.hasItemMeta() || !var0.getItemMeta().hasEnchants())) {
            var10 = false;
        } else {
            var10 = true;
        }

        return var10;
    }
}
