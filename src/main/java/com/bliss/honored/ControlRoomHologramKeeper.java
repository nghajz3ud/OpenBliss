package com.bliss.honored;

import com.bliss.BlissPlugin;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.scheduler.BukkitScheduler;

public final class ControlRoomHologramKeeper implements Listener {
    private final BlissPlugin plugin;
    private final ControlRoomHologramKeeper.Piece[] pieces;

    public ControlRoomHologramKeeper(BlissPlugin var1, int var2) {
        this.plugin = var1;
        ControlRoomHologramKeeper.Piece[] var13 = new ControlRoomHologramKeeper.Piece[4];
        Supplier var20 = () -> {
            Location var5;
            if (var1.controlRoom(1275615420) == null) {
                var5 = null;
            } else {
                var5 = var1.controlRoom(1275615420).enchantSpot(944846913);
            }

            return var5;
        };
        BooleanSupplier var27 = () -> {
            boolean var6x;
            if (var1.enchantLimit(1301378516) != null && var1.enchantLimit(1301378516).standing(0)) {
                var6x = true;
            } else {
                var6x = false;
            }

            return var6x;
        };
        Runnable var34 = () -> {
            if (var1.enchantLimit(1301378516) != null) {
                var1.enchantLimit(1301378516).reconcile(1408290875);
            }
        };
        ControlRoomHologramKeeper.Piece var6 = new ControlRoomHologramKeeper.Piece(var20, var27, var34);
        var13[0] = var6;
        Supplier var22 = () -> {
            Location var5;
            if (var1.controlRoom(1275615420) == null) {
                var5 = null;
            } else {
                var5 = var1.controlRoom(1275615420).potionSpot(491047341);
            }

            return var5;
        };
        BooleanSupplier var29 = () -> {
            boolean var6x;
            if (var1.potionLimit(621168953) != null && var1.potionLimit(621168953).standing(933307280)) {
                var6x = true;
            } else {
                var6x = false;
            }

            return var6x;
        };
        Runnable var36 = () -> {
            if (var1.potionLimit(621168953) != null) {
                var1.potionLimit(621168953).reconcile(1560808647);
            }
        };
        ControlRoomHologramKeeper.Piece var17 = new ControlRoomHologramKeeper.Piece(var22, var29, var36);
        var13[1] = var17;
        Supplier var24 = () -> {
            Location var3;
            if (var1.controlRoom(1275615420) == null) {
                var3 = null;
            } else {
                var3 = var1.controlRoom(1275615420).armorSpot(1529295521);
            }

            return var3;
        };
        BooleanSupplier var31 = () -> {
            boolean var6x;
            if (var1.armorLimit(630906960) != null && var1.armorLimit(630906960).standing(2121409999)) {
                var6x = true;
            } else {
                var6x = false;
            }

            return var6x;
        };
        Runnable var38 = () -> {
            if (var1.armorLimit(630906960) != null) {
                var1.armorLimit(630906960).reconcile(1764668093);
            }
        };
        ControlRoomHologramKeeper.Piece var18 = new ControlRoomHologramKeeper.Piece(var24, var31, var38);
        var13[2] = var18;
        Supplier var26 = () -> {
            Location var5;
            if (var1.controlRoom(1275615420) == null) {
                var5 = null;
            } else {
                var5 = var1.controlRoom(1275615420).borderSpot(983401541);
            }

            return var5;
        };
        BooleanSupplier var33 = () -> {
            boolean var6x;
            if (var1.worldBorderHologram(1866199369) != null && var1.worldBorderHologram(1866199369).standing(1428793479)) {
                var6x = true;
            } else {
                var6x = false;
            }

            return var6x;
        };
        Runnable var40 = () -> {
            if (var1.worldBorderHologram(1866199369) != null) {
                var1.worldBorderHologram(1866199369).reconcile(380967189);
            }
        };
        ControlRoomHologramKeeper.Piece var19 = new ControlRoomHologramKeeper.Piece(var26, var33, var40);
        var13[3] = var19;
        this.pieces = var13;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent var1) {
        Chunk var5 = var1.getChunk();

        for (ControlRoomHologramKeeper.Piece var9 : this.pieces) {
            Location var10 = var9.spot().get();
            if (var10 != null && var10.getWorld() != null) {
                World var28 = var10.getWorld();
                World var47 = var5.getWorld();
                if (var28.equals(var47)) {
                    int var32 = var10.getBlockX() >> 4;
                    int var50 = var5.getX();
                    if (Math.abs(var32 - var50) <= 1) {
                        int var37 = var10.getBlockZ() >> 4;
                        int var54 = var5.getZ();
                        if (Math.abs(var37 - var54) <= 1 && !var9.standing().getAsBoolean()) {
                            BukkitScheduler var43 = Bukkit.getScheduler();
                            BlissPlugin var57 = this.plugin;
                            Runnable var58 = () -> {
                                if (!var9.standing().getAsBoolean()) {
                                    var9.rebuild().run();
                                }
                            };
                            var43.runTask(var57, var58);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent var1) {
        BukkitScheduler var2 = Bukkit.getScheduler();
        BlissPlugin var7 = this.plugin;
        Runnable var8 = () -> {
            for (ControlRoomHologramKeeper.Piece var7x : this.pieces) {
                Location var8x = var7x.spot().get();
                if (var8x != null && var8x.getWorld() != null) {
                    World var24 = var8x.getWorld();
                    World var33 = var1.getWorld();
                    if (var24.equals(var33) && !var7x.standing().getAsBoolean()) {
                        var7x.rebuild().run();
                    }
                }
            }
        };
        var2.runTask(var7, var8);
    }

    private static record Piece(Supplier<Location> spot, BooleanSupplier standing, Runnable rebuild) {
    }
}
