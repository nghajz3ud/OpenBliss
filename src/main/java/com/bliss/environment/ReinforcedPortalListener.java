package com.bliss.environment;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

public final class ReinforcedPortalListener implements Listener {
    private static final int REQUIRED_OBSIDIAN = 8;
    private static final int MIN_W = 2;
    private static final int MAX_W = 21;
    private static final int MIN_H = 3;
    private static final int MAX_H = 21;
    private static final String ENCH_OBSIDIAN_GLOW_TAG = "BlissEnchObsidianGlow";

    @EventHandler(
        ignoreCancelled = true
    )
    public void onIgnite(BlockIgniteEvent var1) {
        IgniteCause var6 = var1.getCause();
        IgniteCause var3 = IgniteCause.FLINT_AND_STEEL;
        if (var6 == var3) {
            Block var5 = var1.getBlock();
            if (isInterior(var5, 1814638266)) {
                Axis var4 = Axis.X;
                if (!this.tryLight(var5, var4, 690197760)) {
                    Axis var19 = Axis.Z;
                    if (!this.tryLight(var5, var19, 690197760)) {
                        return;
                    }
                }

                var1.setCancelled(true);
            }
        }
    }

    private boolean tryLight(Block var1, Axis var2, int var3) {
        World var10 = var1.getWorld();
        if (var10 == null) {
            return false;
        } else {
            Axis var5 = Axis.X;
            BlockFace var32;
            if (var2 == var5) {
                var32 = BlockFace.WEST;
            } else {
                var32 = BlockFace.NORTH;
            }

            BlockFace var11 = var32;
            Axis var147 = Axis.X;
            BlockFace var145;
            if (var2 == var147) {
                var145 = BlockFace.EAST;
            } else {
                var145 = BlockFace.SOUTH;
            }

            BlockFace var12 = var145;
            Axis var148 = Axis.X;
            int var36;
            if (var2 == var148) {
                var36 = var1.getZ();
            } else {
                var36 = var1.getX();
            }

            int var13 = var36;
            Block var14 = var1;

            for (int var15 = 0; var15 < 21 && isInterior(var14.getRelative(var11), 1814638266); var15++) {
                var14 = var14.getRelative(var11);
            }

            Block var214 = var1;

            for (int var16 = 0; var16 < 21 && isInterior(var214.getRelative(var12), 1814638266); var16++) {
                var214 = var214.getRelative(var12);
            }

            Block var215 = var1;

            for (int var17 = 0; var17 < 21; var17++) {
                BlockFace var155 = BlockFace.DOWN;
                if (!isInterior(var215.getRelative(var155), 1814638266)) {
                    break;
                }

                BlockFace var156 = BlockFace.DOWN;
                var215 = var215.getRelative(var156);
            }

            Block var216 = var1;

            for (int var18 = 0; var18 < 21; var18++) {
                BlockFace var158 = BlockFace.UP;
                if (!isInterior(var216.getRelative(var158), 1814638266)) {
                    break;
                }

                BlockFace var159 = BlockFace.UP;
                var216 = var216.getRelative(var159);
            }

            Axis var160 = Axis.X;
            int var146;
            if (var2 == var160) {
                var146 = var14.getX();
            } else {
                var146 = var14.getZ();
            }

            int var217 = var146;
            Axis var161 = Axis.X;
            int var71;
            if (var2 == var161) {
                var71 = var214.getX();
            } else {
                var71 = var214.getZ();
            }

            int var19 = var71;
            int var20 = var215.getY();
            int var21 = var216.getY();
            int var22 = var71 - var146 + 1;
            int var23 = var21 - var20 + 1;
            if (var22 >= 2 && var22 <= 21 && var23 >= 3 && var23 <= 21) {
                for (int var24 = var146; var24 <= var19; var24++) {
                    for (int var25 = var20; var25 <= var21; var25++) {
                        if (!isInterior(at(var10, var2, var13, var24, var25, 1041629042), 1814638266)) {
                            return false;
                        }
                    }
                }

                byte var218 = 0;

                for (int var219 = var146; var219 <= var19; var219 += 1) {
                    int var204 = var20 - 1;
                    Block var26 = at(var10, var2, var13, var219, var204, 1041629042);
                    int var206 = var21 + 1;
                    Block var27 = at(var10, var2, var13, var219, var206, 1041629042);
                    if (!isFrame(var26, 361444147) || !isFrame(var27, 361444147)) {
                        return false;
                    }

                    if (isEnchantedObsidian(var26, 1052251946)) {
                        var218 += 1;
                    }

                    if (isEnchantedObsidian(var27, 1052251946)) {
                        var218 += 1;
                    }
                }

                for (int var220 = var20; var220 <= var21; var220 += 1) {
                    int var198 = var217 - 1;
                    Block var222 = at(var10, var2, var13, var198, var220, 1041629042);
                    int var200 = var19 + 1;
                    Block var224 = at(var10, var2, var13, var200, var220, 1041629042);
                    if (!isFrame(var222, 361444147) || !isFrame(var224, 361444147)) {
                        return false;
                    }

                    if (isEnchantedObsidian(var222, 1052251946)) {
                        var218 += 1;
                    }

                    if (isEnchantedObsidian(var224, 1052251946)) {
                        var218 += 1;
                    }
                }

                if (var218 != 8) {
                    return false;
                } else {
                    Orientable var221 = (Orientable)Material.NETHER_PORTAL.createBlockData();
                    var221.setAxis(var2);

                    for (int var223 = var217; var223 <= var19; var223 += 1) {
                        for (int var225 = var20; var225 <= var21; var225 += 1) {
                            Block var136 = at(var10, var2, var13, var223, var225, 1041629042);
                            var136.setBlockData(var221, false);
                        }
                    }

                    Location var186 = var1.getLocation();
                    Sound var194 = Sound.BLOCK_PORTAL_TRIGGER;
                    var10.playSound(var186, var194, 0.6F, 1.2F);
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPortalPhysics(BlockPhysicsEvent var1) {
        Material var5 = var1.getBlock().getType();
        Material var3 = Material.NETHER_PORTAL;
        if (var5 == var3) {
            if (frameIntactWithReinforced(var1.getBlock(), 1912513940)) {
                var1.setCancelled(true);
            }
        }
    }

    private static boolean frameIntactWithReinforced(Block var0, int var1) {
        BlockData var9 = var0.getBlockData();
        if (!(var9 instanceof Orientable)) {
            return false;
        } else {
            Axis var172 = ((Orientable)var9).getAxis();
            Axis var3 = Axis.X;
            if (var172 != var3) {
                Axis var105 = Axis.Z;
                if (var172 != var105) {
                    return false;
                }
            }

            World var10 = var0.getWorld();
            Axis var106 = Axis.X;
            int var103;
            if (var172 == var106) {
                var103 = var0.getZ();
            } else {
                var103 = var0.getX();
            }

            int var11 = var103;
            Axis var107 = Axis.X;
            int var104;
            if (var172 == var107) {
                var104 = var0.getX();
            } else {
                var104 = var0.getZ();
            }

            int var12 = var104;
            int var13 = var0.getY();
            int var14 = var104;
            int var15 = var104;
            int var16 = var13;
            int var17 = var13;

            while (true) {
                int var109 = var14 - 1;
                if (var12 - var109 > 21) {
                    break;
                }

                int var143 = var14 - 1;
                if (!isPortal(at(var10, var172, var11, var143, var13, 1041629042), 2011691945)) {
                    break;
                }

                var14 += -1;
            }

            while (true) {
                int var51 = var15 + 1;
                if (var51 - var12 > 21) {
                    break;
                }

                int var145 = var15 + 1;
                if (!isPortal(at(var10, var172, var11, var145, var13, 1041629042), 2011691945)) {
                    break;
                }

                var15++;
            }

            while (true) {
                int var117 = var16 - 1;
                if (var13 - var117 > 21) {
                    break;
                }

                int var158 = var16 - 1;
                if (!isPortal(at(var10, var172, var11, var12, var158, 1041629042), 2011691945)) {
                    break;
                }

                var16 += -1;
            }

            while (true) {
                int var62 = var17 + 1;
                if (var62 - var13 > 21) {
                    break;
                }

                int var160 = var17 + 1;
                if (!isPortal(at(var10, var172, var11, var12, var160, 1041629042), 2011691945)) {
                    break;
                }

                var17++;
            }

            boolean var18 = false;

            for (int var19 = var14; var19 <= var15; var19++) {
                int var166 = var16 - 1;
                Material var174 = at(var10, var172, var11, var19, var166, 1041629042).getType();
                int var168 = var17 + 1;
                Material var175 = at(var10, var172, var11, var19, var168, 1041629042).getType();
                if (!ringMaterial(var174, 1930064941) || !ringMaterial(var175, 1930064941)) {
                    return false;
                }

                Material var132 = Material.REINFORCED_DEEPSLATE;
                if (var174 != var132) {
                    Material var133 = Material.REINFORCED_DEEPSLATE;
                    if (var175 != var133) {
                        continue;
                    }
                }

                var18 = true;
            }

            for (int var173 = var16; var173 <= var17; var173 += 1) {
                int var149 = var14 - 1;
                Material var20 = at(var10, var172, var11, var149, var173, 1041629042).getType();
                int var151 = var15 + 1;
                Material var21 = at(var10, var172, var11, var151, var173, 1041629042).getType();
                if (!ringMaterial(var20, 1930064941) || !ringMaterial(var21, 1930064941)) {
                    return false;
                }

                Material var128 = Material.REINFORCED_DEEPSLATE;
                if (var20 != var128) {
                    Material var129 = Material.REINFORCED_DEEPSLATE;
                    if (var21 != var129) {
                        continue;
                    }
                }

                var18 = true;
            }

            return var18;
        }
    }

    private static boolean ringMaterial(Material var0, int var1) {
        Material var3 = Material.REINFORCED_DEEPSLATE;
        if (var0 != var3) {
            Material var6 = Material.OBSIDIAN;
            if (var0 != var6) {
                return false;
            }
        }

        return true;
    }

    private static boolean isPortal(Block var0, int var1) {
        Material var4 = var0.getType();
        Material var3 = Material.NETHER_PORTAL;
        boolean var5;
        if (var4 == var3) {
            var5 = true;
        } else {
            var5 = false;
        }

        return var5;
    }

    private static Block at(World var0, Axis var1, int var2, int var3, int var4, int var5) {
        Axis var7 = Axis.X;
        Block var12;
        if (var1 == var7) {
            var12 = var0.getBlockAt(var3, var4, var2);
        } else {
            var12 = var0.getBlockAt(var2, var4, var3);
        }

        return var12;
    }

    private static boolean isInterior(Block var0, int var1) {
        return switch (var0.getType()) {
            case AIR, CAVE_AIR, VOID_AIR, FIRE-> true;
            default -> false;
        };
    }

    private static boolean isFrame(Block var0, int var1) {
        Material var4 = var0.getType();
        Material var3 = Material.REINFORCED_DEEPSLATE;
        boolean var7;
        if (var4 != var3 && !isEnchantedObsidian(var0, 1052251946)) {
            var7 = false;
        } else {
            var7 = true;
        }

        return var7;
    }

    private static boolean isEnchantedObsidian(Block var0, int var1) {
        Material var18 = var0.getType();
        Material var3 = Material.OBSIDIAN;
        if (var18 != var3) {
            return false;
        } else {
            Location var14 = var0.getLocation().add(0.5, 0.5, 0.5);
            World var24 = var14.getWorld();

            for (Entity var16 : var24.getNearbyEntities(var14, 0.7, 0.7, 0.7)) {
                if (var16 instanceof BlockDisplay) {
                    BlockDisplay var17 = (BlockDisplay)var16;
                    if (var17.getScoreboardTags().contains("BlissEnchObsidianGlow")) {
                        Location var41 = var17.getLocation();
                        if (sameBlock(var41, var0, 243649363)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }
    }

    private static boolean sameBlock(Location var0, Block var1, int var2) {
        int var5 = var0.getBlockX();
        int var11 = var1.getX();
        if (var5 == var11) {
            int var7 = var0.getBlockY();
            int var13 = var1.getY();
            if (var7 == var13) {
                int var9 = var0.getBlockZ();
                int var15 = var1.getZ();
                if (var9 == var15) {
                    return true;
                }
            }
        }

        return false;
    }
}
