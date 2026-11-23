package com.bliss.villager;

import com.bliss.BlissPlugin;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.DataComponentType.Valued;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class VillageDiscJukeboxListener implements Listener {
    public static final String SOUND_KEY = "bliss:village_clue";
    private final BlissPlugin plugin;
    private final BlissItems items;
    private final Map<Location, Integer> lastTickByBlock;
    private static final long LOOP_TICKS = 406L;
    private final Map<Location, BukkitTask> activeLoops;
    private static final int COORD_SCRAMBLE_WINDOW = 4;
    private static final long COORD_STEP_TICKS = 11L;

    public VillageDiscJukeboxListener(BlissPlugin var1, BlissItems var2, int var3) {
        HashMap var5 = new HashMap();
        this.lastTickByBlock = var5;
        HashMap var10 = new HashMap();
        this.activeLoops = var10;
        this.plugin = var1;
        this.items = var2;
    }

    private boolean claimTick(Location var1, int var2) {
        int var6 = Bukkit.getCurrentTick();
        Map var9 = this.lastTickByBlock;
        Integer var7 = (Integer)var9.get(var1);
        if (var7 != null) {
            int var14 = var7;
            if (var14 == var6) {
                return false;
            }
        }

        if (this.lastTickByBlock.size() > 256) {
            this.lastTickByBlock.clear();
        }

        Map var22 = this.lastTickByBlock;
        Integer var28 = var6;
        var22.put(var1, var28);
        return true;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent var1) {
        Action var17 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_BLOCK;
        if (var17 == var3) {
            Block var10 = var1.getClickedBlock();
            if (var10 != null) {
                Material var22 = var10.getType();
                Material var92 = Material.JUKEBOX;
                if (var22 == var92) {
                    if (var10.getState() instanceof Jukebox var11) {
                        Player var127 = var1.getPlayer();
                        Location var13 = var10.getLocation().add(0.5, 0.6, 0.5);
                        ItemStack var14 = var11.getRecord();
                        if (var14 != null) {
                            Material var38 = var14.getType();
                            Material var95 = Material.AIR;
                            if (var38 != var95) {
                                EquipmentSlot var69 = var1.getHand();
                                EquipmentSlot var109 = EquipmentSlot.HAND;
                                if (var69 != var109) {
                                    return;
                                }

                                BlissItems var71 = this.items;
                                if (!var71.isVillageDisc(var14, 1937593005)) {
                                    return;
                                }

                                var1.setCancelled(true);
                                Location var113 = var10.getLocation();
                                if (!this.claimTick(var113, 439204810)) {
                                    return;
                                }

                                ItemStack var128 = var14.clone();
                                Valued var114 = DataComponentTypes.JUKEBOX_PLAYABLE;
                                var128.unsetData(var114);
                                var11.setRecord(null);
                                var11.stopPlaying();
                                var11.update();
                                PlayerInventory var84 = var127.getInventory();
                                ItemStack[] var117 = new ItemStack[]{var128};
                                if (!var84.addItem(var117).isEmpty()) {
                                    World var88 = var10.getWorld();
                                    var88.dropItemNaturally(var13, var128);
                                }

                                this.stopFor(var10, 1620705915);
                                this.stopWhisperLoop(var10, 1229353774);
                                return;
                            }
                        }

                        ItemStack var15 = var1.getItem();
                        if (var15 != null) {
                            BlissItems var43 = this.items;
                            if (var43.isVillageDisc(var15, 1937593005)) {
                                var1.setCancelled(true);
                                Location var99 = var10.getLocation();
                                if (!this.claimTick(var99, 439204810)) {
                                    return;
                                }

                                ItemStack var16 = var15.clone();
                                var16.setAmount(1);
                                Valued var101 = DataComponentTypes.JUKEBOX_PLAYABLE;
                                var16.unsetData(var101);
                                var11.setRecord(var16);
                                var11.stopPlaying();
                                var11.update();
                                int var105 = var15.getAmount() - 1;
                                var15.setAmount(var105);
                                if (this.plugin.globalState(724248306) != null && !this.plugin.globalState(724248306).clueAudio(2027958034)) {
                                    this.revealCoordsClue(var127, 1841114083);
                                } else {
                                    World var66 = var10.getWorld();
                                    String var121 = "bliss:village_clue";
                                    SoundCategory var123 = SoundCategory.RECORDS;
                                    var66.playSound(var13, var121, var123, 4.0F, 1.0F);
                                    this.startWhisperLoop(var10, 1712857343);
                                }

                                return;
                            }
                        }

                        return;
                    }

                    return;
                }
            }
        }
    }

    private void revealCoordsClue(Player var1, int var2) {
        Location var44;
        if (this.plugin.eventState(1026927401) == null) {
            var44 = null;
        } else {
            var44 = this.plugin.eventState(1026927401).compassLoc(561536293);
        }

        if (var44 == null) {
            Component var45 = Text.legacy("&e&k████████", 373192400);
            Component var53 = Text.legacy("", 373192400);
            Duration var58 = Duration.ofMillis(400L);
            Duration var66 = Duration.ofMillis(2500L);
            Duration var68 = Duration.ofMillis(600L);
            Times var59 = Times.times(var58, var66, var68);
            Title var46 = Title.title(var45, var53, var59);
            var1.showTitle(var46);
        } else {
            UUID var14 = var1.getUniqueId();
            int var28 = var44.getBlockX();
            int var48 = var44.getBlockY();
            int var55 = var44.getBlockZ();
            String var15 = var28 + "   " + var48 + "   " + var55;
            int var16 = var15.length();

            for (int var17 = 0; var17 <= var16; var17++) {
                int var18 = var17;
                boolean var40;
                if (var17 == 0) {
                    var40 = true;
                } else {
                    var40 = false;
                }

                boolean var19 = var40;
                boolean var37;
                if (var17 == var16) {
                    var37 = true;
                } else {
                    var37 = false;
                }

                boolean var20 = var37;
                BukkitScheduler var38 = Bukkit.getScheduler();
                BlissPlugin var52 = this.plugin;
                Runnable var57 = () -> {
                    Player var11 = Bukkit.getPlayer(var14);
                    if (var11 != null && var11.isOnline()) {
                        Component var18x = Text.legacy(renderCoordFrame(var15, var18, 1125985825), 373192400);
                        Component var21 = Text.legacy("", 373192400);
                        long var24;
                        if (var19) {
                            var24 = 300L;
                        } else {
                            var24 = 0L;
                        }

                        Duration var22 = Duration.ofMillis(var24);
                        long var27;
                        if (var20) {
                            var27 = 4000L;
                        } else {
                            var27 = 2400L;
                        }

                        Duration var26 = Duration.ofMillis(var27);
                        long var30;
                        if (var20) {
                            var30 = 1200L;
                        } else {
                            var30 = 0L;
                        }

                        Duration var29 = Duration.ofMillis(var30);
                        Times var23 = Times.times(var22, var26, var29);
                        Title var19x = Title.title(var18x, var21, var23);
                        var11.showTitle(var19x);
                    }
                };
                long var64 = (long)var17 * 11L;
                var38.runTaskLater(var52, var57, var64);
            }
        }
    }

    private static String renderCoordFrame(String var0, int var1, int var2) {
        StringBuilder var3 = new StringBuilder();
        StringBuilder var5 = var3;
        int var6 = 0;

        while (true) {
            int var27 = var0.length();
            if (var6 >= var27) {
                return var5.toString();
            }

            char var7 = var0.charAt(var6);
            if (var7 == ' ') {
                var5.append(' ');
            } else if (var6 < var1) {
                StringBuilder var19 = var5.append("&e");
                var19.append(var7);
            } else {
                int var35 = var1 + 4;
                if (var6 < var35) {
                    StringBuilder var23 = var5.append("&e&k");
                    var23.append(var7).append("&r");
                }
            }

            var6++;
        }
    }

    private void stopFor(Block var1, int var2) {
        for (Player var8 : var1.getWorld().getPlayers()) {
            Location var18 = var8.getLocation();
            Location var23 = var1.getLocation();
            if (var18.distanceSquared(var23) <= 4096.0) {
                String var24 = "bliss:village_clue";
                SoundCategory var25 = SoundCategory.RECORDS;
                var8.stopSound(var24, var25);
            }
        }
    }

    private void startWhisperLoop(Block var1, int var2) {
        Location var12 = var1.getLocation();
        UUID var13 = var1.getWorld().getUID();
        this.stopWhisperLoop(var1, 1229353774);
        Location var14 = var12.clone().add(0.5, 0.6, 0.5);
        BukkitScheduler var24 = Bukkit.getScheduler();
        BlissPlugin var32 = this.plugin;
        Runnable var34 = () -> {
            World var11 = Bukkit.getWorld(var13);
            if (var11 == null) {
                this.stopWhisperLoop(var1, 1229353774);
            } else {
                int var32x = var12.getBlockX() >> 4;
                int var40 = var12.getBlockZ() >> 4;
                if (var11.isChunkLoaded(var32x, var40)) {
                    if (var12.getBlock().getState() instanceof Jukebox var12x) {
                        BlissItems var26 = this.items;
                        ItemStack var34x = var12x.getRecord();
                        if (var26.isVillageDisc(var34x, 1937593005)) {
                            String var41 = "bliss:village_clue";
                            SoundCategory var42 = SoundCategory.RECORDS;
                            var11.playSound(var14, var41, var42, 4.0F, 1.0F);
                            return;
                        }
                    }

                    this.stopWhisperLoop(var1, 1229353774);
                }
            }
        };
        BukkitTask var15 = var24.runTaskTimer(var32, var34, 406L, 406L);
        Map var27 = this.activeLoops;
        var27.put(var12, var15);
    }

    private void stopWhisperLoop(Block var1, int var2) {
        Map var6 = this.activeLoops;
        Location var11 = var1.getLocation();
        BukkitTask var5 = (BukkitTask)var6.remove(var11);
        if (var5 != null) {
            var5.cancel();
        }
    }
}
